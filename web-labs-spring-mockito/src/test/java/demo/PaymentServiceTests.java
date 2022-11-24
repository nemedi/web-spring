package demo;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import demo.AccountModel.Currency;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {

	@Mock
	private PaymentRepository repository;
	
	@InjectMocks
	private PaymentService service;
	
	private static final Optional<UserModel> SENDER;
	private static final Optional<UserModel> RECEIVER;
	private static final Currency CURRENCY;
	private static final double AMOUNT;
	private static final int TIMES;
	private static final List<AccountModel> SENDER_ACCOUNTS;
	private static final List<AccountModel> RECEIVER_ACCOUNTS;
	
	static {
		SENDER = Optional.of(new UserModel(UUID.randomUUID().toString(),
				"John", "Doe", "jdoe@gmail.com", "p@$$M()rd"));
		RECEIVER = Optional.of(new UserModel(UUID.randomUUID().toString(),
    			"Ion", "Popescu", "ipopescu@gmail.com", "p@r()l@"));
		CURRENCY = Currency.USD;
		AMOUNT = 10000;
		TIMES = 10;
		SENDER_ACCOUNTS = new ArrayList<AccountModel>();
		SENDER_ACCOUNTS.add(new AccountModel(UUID.randomUUID().toString(),
				SENDER.get().getId(), CURRENCY, AMOUNT));
		RECEIVER_ACCOUNTS = new ArrayList<AccountModel>();
		RECEIVER_ACCOUNTS.add(new AccountModel(UUID.randomUUID().toString(),
				RECEIVER.get().getId(), CURRENCY, AMOUNT));
	}
	
    @Test
    @DisplayName("Test invalid sender")
    public void testInvalidSender() throws Exception {
    	doReturn(Optional.empty()).when(repository).getUser(SENDER.get().getEmail());
    	PaymentException exception = assertThrows(PaymentException.class, () ->
			service.sendPayment(SENDER.get().getEmail(),
					SENDER.get().getPassword(),
					RECEIVER.get().getEmail(),
					CURRENCY,
					AMOUNT / TIMES));
    	assertEquals(PaymentException.PaymentErrors.USER_NOT_FOUND, exception.getError());
    }
    
    @Test
    @DisplayName("Test invalid password")
    public void testInvalidPassword() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	PaymentException exception = assertThrows(PaymentException.class, () ->
    		service.sendPayment(SENDER.get().getEmail(),
				"invalid_password@gmail.com",
				RECEIVER.get().getEmail(),
				CURRENCY,
				AMOUNT / TIMES));
    	assertEquals(PaymentException.PaymentErrors.BAD_CREDENTIALS, exception.getError());
    }
    
    @Test
    @DisplayName("Test invalid receiver")
    public void testInvalidReceiver() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	doReturn(Optional.empty()).when(repository).getUser(RECEIVER.get().getEmail());
    	PaymentException exception = assertThrows(PaymentException.class, () ->
			service.sendPayment(SENDER.get().getEmail(),
					SENDER.get().getPassword(),
					RECEIVER.get().getEmail(),
					CURRENCY,
					AMOUNT / TIMES));
    	assertEquals(PaymentException.PaymentErrors.USER_NOT_FOUND, exception.getError());
    }
    
    @Test
    @DisplayName("Test sender has no account for currency")
    public void testSenderHasNoAccountForCurrency() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	doReturn(RECEIVER).when(repository).getUser(RECEIVER.get().getEmail());
    	doReturn(emptyList()).when(repository).getUserAccounts(SENDER.get().getId());
    	PaymentException exception = assertThrows(PaymentException.class, () ->
			service.sendPayment(SENDER.get().getEmail(),
					SENDER.get().getPassword(),
					RECEIVER.get().getEmail(),
					CURRENCY,
					AMOUNT / TIMES));
    	assertEquals(PaymentException.PaymentErrors.USER_HAS_NO_ACCOUNT_FOR_CURRENCY, exception.getError());
    }
    
    @Test
    @DisplayName("Test receiver has no account for currency")
    public void testReceiverHasNoAccountForCurrency() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	doReturn(RECEIVER).when(repository).getUser(RECEIVER.get().getEmail());
    	doReturn(SENDER_ACCOUNTS).when(repository).getUserAccounts(SENDER.get().getId());
    	doReturn(emptyList()).when(repository).getUserAccounts(RECEIVER.get().getId());
    	PaymentException exception = assertThrows(PaymentException.class, () ->
			service.sendPayment(SENDER.get().getEmail(),
					SENDER.get().getPassword(),
					RECEIVER.get().getEmail(),
					CURRENCY,
					AMOUNT / TIMES));
    	assertEquals(PaymentException.PaymentErrors.USER_HAS_NO_ACCOUNT_FOR_CURRENCY, exception.getError());
    }
    
    @Test
    @DisplayName("Test sender account has not enough amount")
    public void testSenderAccountHasNotEnoughAmount() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	doReturn(RECEIVER).when(repository).getUser(RECEIVER.get().getEmail());
    	doReturn(SENDER_ACCOUNTS).when(repository).getUserAccounts(SENDER.get().getId());
    	doReturn(RECEIVER_ACCOUNTS).when(repository).getUserAccounts(RECEIVER.get().getId());
    	PaymentException exception = assertThrows(PaymentException.class, () ->
			service.sendPayment(SENDER.get().getEmail(),
					SENDER.get().getPassword(),
					RECEIVER.get().getEmail(),
					CURRENCY,
					TIMES * AMOUNT));
    	assertEquals(PaymentException.PaymentErrors.ACCOUNT_HAS_NOT_ENOUGH_AMOUNT_FOR_PAYMENT, exception.getError());
    }
    
    @Test
    @DisplayName("Test payment could not be processed")
    public void testPaymentCouldNotBeProcessed() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	doReturn(RECEIVER).when(repository).getUser(RECEIVER.get().getEmail());
    	doReturn(SENDER_ACCOUNTS).when(repository).getUserAccounts(SENDER.get().getId());
    	doReturn(RECEIVER_ACCOUNTS).when(repository).getUserAccounts(RECEIVER.get().getId());
    	doReturn(false).when(repository).savePayment(any(PaymentModel.class));
    	PaymentException exception = assertThrows(PaymentException.class, () ->
			service.sendPayment(SENDER.get().getEmail(),
					SENDER.get().getPassword(),
					RECEIVER.get().getEmail(),
					CURRENCY,
					AMOUNT / TIMES));
    	assertEquals(PaymentException.PaymentErrors.PAYMENT_COULD_NOT_BE_PROCESSED, exception.getError());
    }
    
    @Test
    @DisplayName("Test payment sent")
    public void testPaymentSent() throws Exception {
    	doReturn(SENDER).when(repository).getUser(SENDER.get().getEmail());
    	doReturn(RECEIVER).when(repository).getUser(RECEIVER.get().getEmail());
    	doReturn(SENDER_ACCOUNTS).when(repository).getUserAccounts(SENDER.get().getId());
    	doReturn(RECEIVER_ACCOUNTS).when(repository).getUserAccounts(RECEIVER.get().getId());
    	doReturn(true).when(repository).savePayment(any(PaymentModel.class));
		service.sendPayment(SENDER.get().getEmail(),
				SENDER.get().getPassword(),
				RECEIVER.get().getEmail(),
				CURRENCY,
				AMOUNT / TIMES);
    }
    
}
