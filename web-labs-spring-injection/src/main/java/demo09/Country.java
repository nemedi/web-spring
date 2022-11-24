package demo09;

import static java.util.stream.Collectors.joining;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
	
	private String name;

	@Autowired
	private List<String> cities;
	
	private Country(@Value("Romania") String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("List of cities in %s: %s.",
				name, cities.stream().collect(joining(", ")));
	}
}
