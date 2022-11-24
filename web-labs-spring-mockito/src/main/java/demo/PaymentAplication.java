package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PaymentAplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentAplication.class, args);
	}

}
