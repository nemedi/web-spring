package demo02;

import org.springframework.stereotype.Component;

@Component
@Properties("settings")
public class Settings {

	private String host;
	
	public String getHost() {
		return host;
	}
	
}
