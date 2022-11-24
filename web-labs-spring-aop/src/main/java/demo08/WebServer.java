package demo08;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpServer;

@Component
public class WebServer implements AutoCloseable {
	
	private HttpServer server;
	
	@Autowired
	private WebHandler handler;

	public void open(int port) throws IOException {
		InetSocketAddress address =
			new InetSocketAddress(InetAddress.getLocalHost(), port);
		server = HttpServer.create(address, 0);
		server.createContext("/", handler);
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
	}

	@Override
	public void close() throws Exception {
		if (server != null) {
			server.stop(0);
			server = null;
		}
	}

}