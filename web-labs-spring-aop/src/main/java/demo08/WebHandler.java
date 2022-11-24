package demo08;

import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@Component
public class WebHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (new Random().nextBoolean()) {
			throw new WebException(404);
		}
		byte[] buffer = "It's working!".getBytes();
		exchange.sendResponseHeaders(200, buffer.length);
		exchange.getResponseBody().write(buffer, 0, buffer.length);
		exchange.close();
	}

}

