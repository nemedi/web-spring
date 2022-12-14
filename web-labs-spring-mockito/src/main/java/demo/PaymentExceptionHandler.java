package demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import demo.PaymentException.PaymentErrors;

@ControllerAdvice
public class PaymentExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(PaymentException.class)
	public final ResponseEntity<String> handlePaymentException(PaymentException exception,
			WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (exception.getError() == PaymentErrors.USER_NOT_FOUND) {
			status = HttpStatus.UNAUTHORIZED;
		} else if (exception.getError() == PaymentErrors.BAD_CREDENTIALS) {
			status = HttpStatus.FORBIDDEN;
		}
		return new ResponseEntity<String>(exception.getError().name(), status);
	}
}
