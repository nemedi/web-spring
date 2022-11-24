package demo;

import static demo.PaymentException.accountCouldNotBeRemoved;
import static demo.PaymentException.accountCouldNotBeSaved;
import static demo.PaymentException.accountHasNotEnoughAmountForPayment;
import static demo.PaymentException.badCredentials;
import static demo.PaymentException.couldNotGetExchangeRate;
import static demo.PaymentException.paymentCouldNotBeProcessed;
import static demo.PaymentException.transferCouldNotBeProcessed;
import static demo.PaymentException.userCouldNotBeRemoved;
import static demo.PaymentException.userCouldNotBeSaved;
import static demo.PaymentException.userHasNoAccountForCurrency;
import static demo.PaymentException.userNotFound;
import static demo.PaymentException.userWithSameEmailAlreadyExists;

import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import demo.AccountModel.Currency;

@Service
public class PaymentService {

	private PaymentRepository repository;
	
	@Value("${exchangeRateUrl}")
	private String exchangeRateUrl;
	
	public PaymentService(PaymentRepository repository) {
		this.repository = repository;
	}
	
	public List<AccountModel> getUserAccounts(String email, String password) {
		return repository.getUserAccounts(getUser(email, password).getId());
	}
	
	public List<PaymentModel> getPaymentsSentByUser(String email, String password) {
		return repository.getPaymentsSentByUser(getUser(email, password).getId());
	}
	
	public List<PaymentModel> getPaymentsReceivedByUser(String email, String password) {
		return repository.getPaymentsReceivedByUser(getUser(email, password).getId());
	}
	
	@Transactional
	public void sendPayment(String senderEmail,
			String senderPassword,
			String receiverEmail,
			Currency currency,
			double amount) {
		UserModel sender = getUser(senderEmail, senderPassword);
		UserModel receiver = getUser(receiverEmail);
		AccountModel senderAccount = getUserAccountForCurrency(sender, currency);
		AccountModel receiverAccount = getUserAccountForCurrency(receiver, currency);
		if (amount > senderAccount.getAmount()) {
			throw accountHasNotEnoughAmountForPayment();
		}
		PaymentModel payment = new PaymentModel(UUID.randomUUID().toString(),
				senderAccount.getId(),
				receiverAccount.getId(),
				currency,
				amount);
		if (!repository.savePayment(payment)) {
			throw paymentCouldNotBeProcessed();
		}
	}
	
	@Transactional
	public void transferMoney(String email,
			String password,
			Currency fromCurrency,
			Currency toCurrency,
			double amount) {
		UserModel user = getUser(email, password);
		AccountModel fromAccount = getUserAccountForCurrency(user, fromCurrency);
		AccountModel toAccount = getUserAccountForCurrency(user, toCurrency);
		if (amount > fromAccount.getAmount()) {
			throw accountHasNotEnoughAmountForPayment();
		}
		double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
		if (!repository.transferMoney(fromAccount.getId(),
				toAccount.getId(),
				amount,
				exchangeRate * amount)) {
			throw transferCouldNotBeProcessed();
		}
	}
	
	@Transactional
	public void saveUser(UserModel user) {
		Optional<UserModel> existingUser = repository.getUser(user.getEmail());
		if (existingUser.isPresent()
				&& !existingUser.get().getId().equals(user.getId())) {
			throw userWithSameEmailAlreadyExists();
		}
		if (!repository.saveUser(user)) {
			throw userCouldNotBeSaved();
		}
	}
	
	@Transactional
	public void removeUser(String email) {
		if (!repository.removeUser(email)) {
			throw userCouldNotBeRemoved();
		}
	}
	
	@Transactional
	public void saveAccount(AccountModel account) {
		if (!repository.saveAccount(account)) {
			throw accountCouldNotBeSaved();
		}
	}
	
	@Transactional
	public void removeAccount(String email, Currency currency) {
		if (!repository.removeAccount(email, currency)) {
			throw accountCouldNotBeRemoved();
		}
	}
	
	private UserModel getUser(String email) {
		Optional<UserModel> user = repository.getUser(email);
		if (!user.isPresent()) {
			throw userNotFound();
		}
		return user.get();
	}
	
	private UserModel getUser(String email, String password) {
		UserModel user = getUser(email);
		if (!password.equals(user.getPassword())) {
			throw badCredentials();
		}
		return user;
	}
	
	private AccountModel getUserAccountForCurrency(UserModel user, Currency currency) {
		Optional<AccountModel> account = repository.getUserAccounts(user.getId())
				.stream().filter(a -> currency.equals(a.getCurrency()))
				.findFirst();
		if (!account.isPresent()) {
			throw userHasNoAccountForCurrency();
		}
		return account.get();
	}
	
	public double getExchangeRate(Currency fromCurrency, Currency toCurrency) {
		try {
			double exchangeRate = 1;
			if (!fromCurrency.equals(toCurrency)) {
				RestTemplate restTemplate = restTemplate();
				ResponseEntity<String> response = restTemplate
						.getForEntity(exchangeRateUrl, String.class);
				if (response.getStatusCode() == HttpStatus.OK) {
					String xml = response.getBody();
					Document document = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
					String expression = null;
					if (Currency.RON.equals(toCurrency)) {
						expression = String.format("//Rate[@currency='%s']/text()",
								fromCurrency.name().toUpperCase());
						exchangeRate = (double) XPathFactory.newInstance()
							.newXPath()
							.compile(expression)
							.evaluate(document, XPathConstants.NUMBER);
					} else if (Currency.RON.equals(fromCurrency)) {
						expression = String.format("//Rate[@currency='%s']/text()",
								toCurrency.name().toUpperCase());
						exchangeRate = 1 / (double) XPathFactory.newInstance()
							.newXPath()
							.compile(expression)
							.evaluate(document, XPathConstants.NUMBER);
					} else {
						expression = String.format("//Rate[@currency='%s']/text()",
								fromCurrency.name().toUpperCase());
						double fromExchangeRate = (double) XPathFactory.newInstance()
							.newXPath()
							.compile(expression)
							.evaluate(document, XPathConstants.NUMBER);
						expression = String.format("//Rate[@currency='%s']/text()",
								toCurrency.name().toUpperCase());
						double toExchangeRate = (double) XPathFactory.newInstance()
							.newXPath()
							.compile(expression)
							.evaluate(document, XPathConstants.NUMBER);
						exchangeRate = fromExchangeRate / toExchangeRate;
					}
				}
			}
			exchangeRate = Math.round(10000 * exchangeRate) / 10000.0;
			return exchangeRate;
		} catch (Exception e) {
			throw couldNotGetExchangeRate();
		}
	}
	
	public RestTemplate restTemplate()
			throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}
	
}
