package demo;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepository {

	private List<CityModel> cities;
	
	public CityRepository(@Value("${csv}") String csv) throws IOException {
		cities = Files.readAllLines(new ClassPathResource(csv).getFile().toPath())
				.stream()
				.map(line -> line.split(","))
				.filter(values -> values.length == 4)
				.map(values -> new CityModel(values))
				.collect(toList());
	}
	
	public List<CityModel> getCities() {
		return cities;
	}
}
