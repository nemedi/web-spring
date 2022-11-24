package demo;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.AccountModel.Currency;

import static demo.PaymentException.badCredentials;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	public static final String AUTHORIZATION = "Authorization";
	
	private static class Credentials {
		
		private String email;
		private String password;
		
		public Credentials(String header) {
			if (header.startsWith("Basic ")) {
				header = new String(Base64.getDecoder()
						.decode(header.substring("Basic ".length())));
				if (header.indexOf(':') > 0) {
					email = header.substring(0, header.indexOf(':'));
					password = header.substring(header.indexOf(':') + 1);
				}
			}
		}
		
		public String getEmail() {
			return email;
		}
		
		public String getPassword() {
			return password;
		}
	}

	private PaymentService service;
	
	@Value("${administrator.email}")
	private String administratorEmail;
	
	@Value("${administrator.password}")
	private String administratorPassword;

	public PaymentController(PaymentService service) {
		this.service = service;
	}
	
	@GetMapping("/accounts")
	public ResponseEntity<List<AccountModel>> getAccounts(@RequestHeader(AUTHORIZATION) String header) {
		Credentials credentials = new Credentials(header);
		List<AccountModel> accounts = service.getUserAccounts(credentials.getEmail(),
				credentials.getPassword());
		return accounts.isEmpty()
				? ResponseEntity.noContent().build() : ResponseEntity.ok(accounts);
	}
	
	@GetMapping("/payments/sent")
	public ResponseEntity<List<PaymentModel>> getPaymentsSent(@RequestHeader(AUTHORIZATION) String header) {
		Credentials credentials = new Credentials(header);
		List<PaymentModel> payments = service.getPaymentsSentByUser(credentials.getEmail(),
				credentials.getPassword());
		return payments.isEmpty()
				? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
	}
	
	@GetMapping("/payments/received")
	public ResponseEntity<List<PaymentModel>> getPaymentsReceived(@RequestHeader(AUTHORIZATION) String header) {
		Credentials credentials = new Credentials(header);
		List<PaymentModel> payments = service.getPaymentsReceivedByUser(credentials.getEmail(),
				credentials.getPassword());
		return payments.isEmpty()
				? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
	}

	@PostMapping("/payments")
	public ResponseEntity<Void> sendPayment(@RequestHeader(AUTHORIZATION) String header,
			@RequestParam String receiver,
			@RequestParam Currency currency,
			@RequestParam double amount) {
		Credentials credentials = new Credentials(header);
		service.sendPayment(credentials.getEmail(), credentials.getPassword(), receiver, currency, amount);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	public PaymentService getService() {
		return service;
	}
	
	@GetMapping("/rates/{fromCurrency}/{toCurrency}")
	public ResponseEntity<Double> getExchangeRate(@PathVariable Currency fromCurrency,
			@PathVariable Currency toCurrency) {
		double exchangeRate = service.getExchangeRate(fromCurrency, toCurrency);
		return ResponseEntity.ok(exchangeRate);
	}
	
	@PutMapping("/users")
	public ResponseEntity<Void> saveUser(@RequestHeader(AUTHORIZATION) String header,
			@RequestBody UserModel user) {
		Credentials credentials = new Credentials(header);
		if (administratorEmail.equals(credentials.getEmail())
				&& administratorPassword.equals(credentials.getPassword())) {
			service.saveUser(user);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} else {
			throw badCredentials();
		}
	}
	
	@DeleteMapping("/users")
	public ResponseEntity<Void> removeUser(@RequestHeader(AUTHORIZATION) String header,
			@RequestParam String email) {
		Credentials credentials = new Credentials(header);
		if (administratorEmail.equals(credentials.getEmail())
				&& administratorPassword.equals(credentials.getPassword())) {
			service.removeUser(email);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} else {
			throw badCredentials();
		}
	}
	
	@PutMapping("/accounts")
	public ResponseEntity<Void> saveAccount(@RequestHeader(AUTHORIZATION) String header,
			@RequestBody AccountModel account) {
		Credentials credentials = new Credentials(header);
		if (administratorEmail.equals(credentials.getEmail())
				&& administratorPassword.equals(credentials.getPassword())) {
			service.saveAccount(account);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} else {
			throw badCredentials();
		}
	}
	
	@DeleteMapping("/accounts")
	public ResponseEntity<Void> removeAccount(@RequestHeader(AUTHORIZATION) String header,
			@RequestParam String email,
			@RequestParam Currency currency) {
		Credentials credentials = new Credentials(header);
		if (administratorEmail.equals(credentials.getEmail())
				&& administratorPassword.equals(credentials.getPassword())) {
			service.removeAccount(email, currency);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} else {
			throw badCredentials();
		}
	}
}
