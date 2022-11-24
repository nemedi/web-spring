package demo;

import static demo.PaymentException.accountCouldNotBeRemoved;
import static demo.PaymentException.badCredentials;
import static demo.PaymentException.userCouldNotBeRemoved;
import static demo.PaymentException.userNotFound;
import static demo.PaymentException.userWithSameEmailAlreadyExists;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.AccountModel.Currency;

@WebMvcTest(controllers = PaymentController.class)
@Import(PaymentTestConfiguation.class)
public class PaymentControllerIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private PaymentRepository repository;
	
	@Autowired
	private PaymentService service;
	
	@Value("${administrator.email}")
	private String administratorEmail;
	
	@Value("${administrator.password}")
	private String administratorPassword;
	
	private static UserModel sender;
	private static UserModel receiver;
	private static List<AccountModel> senderAccounts;
	private static List<AccountModel> receiverAccounts;
	
	@BeforeAll
	public static void setup() {
		sender = new UserModel(id(), "John", "Doe", "john.doe@gmail.com", "pa$$M()rd");
		receiver = new UserModel(id(), "Ion", "Popescu", "ion.popescu@yahoo.ro", "par()l@");
		senderAccounts = new ArrayList<AccountModel>();
		senderAccounts.add(new AccountModel(id(), sender.getId(), Currency.EUR, 100000));
		senderAccounts.add(new AccountModel(id(), sender.getId(), Currency.USD, 100000));
		receiverAccounts = new ArrayList<AccountModel>();
		receiverAccounts.add(new AccountModel(id(), receiver.getId(), Currency.EUR, 100000));
		receiverAccounts.add(new AccountModel(id(), receiver.getId(), Currency.USD, 100000));
	}
	
	@Test
	public void testGetExchangeRate() throws Exception {
		double exchangeRate = service.getExchangeRate(Currency.EUR, Currency.USD);
		String endpoint = String.format("/api/rates/%s/%s",
				Currency.EUR, Currency.USD);
		mockMvc.perform(get(endpoint)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(exchangeRate));
	}
	
	@Test
	public void testGetAccounts() throws Exception {
		when(repository.getUser(sender.getEmail())).thenReturn(Optional.of(sender));
		when(repository.getUser(administratorEmail)).thenReturn(Optional.empty());
		when(repository.getUserAccounts(sender.getId())).thenReturn(emptyList());
		String endpoint = "/api/accounts";
		mockMvc.perform(get(endpoint)
				.accept(MediaType.APPLICATION_JSON)
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword)))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$").value(userNotFound().toString()));
		mockMvc.perform(get(endpoint)
				.accept(MediaType.APPLICATION_JSON)
				.header(PaymentController.AUTHORIZATION, credentials(sender)))
			.andExpect(status().isNoContent());
		when(repository.getUserAccounts(sender.getId())).thenReturn(senderAccounts);
		mockMvc.perform(get(endpoint)
				.accept(MediaType.APPLICATION_JSON)
				.header(PaymentController.AUTHORIZATION, credentials(sender)))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testSendPayment() throws Exception {
		when(repository.getUser(sender.getEmail())).thenReturn(Optional.of(sender));
		when(repository.getUserAccounts(sender.getId())).thenReturn(senderAccounts);
		when(repository.getUser(receiver.getEmail())).thenReturn(Optional.of(sender));
		when(repository.getUserAccounts(receiver.getId())).thenReturn(receiverAccounts);
		when(repository.savePayment(any())).thenReturn(true);
		String endpoint = "/api/payments?receiver=%s&currency=%s&amount=%f";
		mockMvc.perform(post(String.format(endpoint,
				receiver.getEmail(),
				senderAccounts.get(0).getCurrency(),
				10 * senderAccounts.get(0).getAmount()))
				.header(PaymentController.AUTHORIZATION, credentials(sender)))
			.andExpect(status().isInternalServerError());
		mockMvc.perform(post(String.format(endpoint,
				receiver.getEmail(),
				senderAccounts.get(0).getCurrency(),
				senderAccounts.get(0).getAmount() / 10))
				.header("Authorization", credentials(sender)))
			.andExpect(status().isAccepted());
	}
	
	@Test
	public void testSaveuser() throws Exception {
		when(repository.getUser(sender.getEmail())).thenReturn(Optional.of(receiver));
		when(repository.saveUser(any())).thenReturn(true);
		String endpoint = "/api/users";
		mockMvc.perform(put(endpoint)
				.header(PaymentController.AUTHORIZATION, credentials(sender))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sender)))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$").value(badCredentials().toString()));
		mockMvc.perform(put(endpoint)
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sender)))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$").value(userWithSameEmailAlreadyExists().toString()));
		when(repository.getUser(sender.getEmail())).thenReturn(Optional.of(sender));
		mockMvc.perform(put(endpoint)
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sender)))
			.andExpect(status().isAccepted());
	}
	
	@Test
	public void testRemoveUser() throws Exception {
		when(repository.removeUser(sender.getEmail())).thenReturn(false);
		when(repository.removeUser(receiver.getEmail())).thenReturn(true);
		String endpoint = "/api/users?email=%s";
		mockMvc.perform(delete(String.format(endpoint, sender.getEmail()))
				.header(PaymentController.AUTHORIZATION, credentials(sender)))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$").value(badCredentials().toString()));
		mockMvc.perform(delete(String.format(endpoint, sender.getEmail()))
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword)))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$").value(userCouldNotBeRemoved().toString()));
		mockMvc.perform(delete(String.format(endpoint, receiver.getEmail()))
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword)))
			.andExpect(status().isAccepted());
	}
	
	@Test
	public void testSaveAccount() throws Exception {
		when(repository.saveAccount(any())).thenReturn(true);
		String endpoint = "/api/accounts";
		mockMvc.perform(put(endpoint)
				.header(PaymentController.AUTHORIZATION, credentials(sender))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(senderAccounts.get(0))))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$").value(badCredentials().toString()));
		mockMvc.perform(put(endpoint)
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(senderAccounts.get(0))))
			.andExpect(status().isAccepted());
	}
	
	@Test
	public void testRemoveAccount() throws Exception {
		when(repository.removeAccount(sender.getEmail(), senderAccounts.get(0).getCurrency()))
			.thenReturn(false);
		when(repository.removeAccount(receiver.getEmail(), receiverAccounts.get(0).getCurrency()))
			.thenReturn(true);
		String endpoint = "/api/accounts?email=%s&currency=%s";
		mockMvc.perform(delete(String.format(endpoint,
				sender.getEmail(), senderAccounts.get(0).getCurrency()))
				.header(PaymentController.AUTHORIZATION, credentials(sender)))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$").value(badCredentials().toString()));
		mockMvc.perform(delete(String.format(endpoint,
				sender.getEmail(), senderAccounts.get(0).getCurrency()))
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword)))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$").value(accountCouldNotBeRemoved().toString()));
		mockMvc.perform(delete(String.format(endpoint,
				receiver.getEmail(), receiverAccounts.get(0).getCurrency()))
				.header(PaymentController.AUTHORIZATION,
						credentials(administratorEmail, administratorPassword)))
			.andExpect(status().isAccepted());
	}
	
	private static String id() {
		return UUID.randomUUID().toString();
	}
	
	private static String credentials(String email, String password) {
		return String.format("Basic %s",
				Base64.getEncoder().encodeToString(String.format("%s:%s",
						email, password).getBytes()));
	}
	
	private static String credentials(UserModel user) {
		return credentials(user.getEmail(), user.getPassword());
	}
	
}
