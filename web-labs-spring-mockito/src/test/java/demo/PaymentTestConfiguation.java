package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static org.mockito.Mockito.mock;

@TestConfiguration
@EnableTransactionManagement
public class PaymentTestConfiguation {

	@Bean
	public PaymentService service(@Autowired PaymentRepository repository) {
		return new PaymentService(repository);
	}
	
	@Bean
	public TransactionManager transactionManager() {
		return mock(PlatformTransactionManager.class);
	}
}
