package demo;

import java.nio.file.Paths;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

public class Program {

	public static void main(String[] args) {
		try {
			Server server = new Server(8080);
			WebAppContext webapp = new WebAppContext();
			webapp.setContextPath("/");
			webapp.setWarResource(new PathResource(Paths.get("src/main/webapp").toFile()));
			Configuration.ClassList.setServerDefault(server)
				.addBefore(JettyWebXmlConfiguration.class.getName(),
					AnnotationConfiguration.class.getName());
			server.setHandler(webapp);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
