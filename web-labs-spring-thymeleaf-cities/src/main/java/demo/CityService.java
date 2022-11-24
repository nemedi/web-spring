package demo;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {
	
	@Autowired
	private CityRepository repository;

	public List<CityModel> searchCitiesByName(String name) {
		return name == null
				? emptyList()
				: repository.getCities()
					.stream()
					.filter(city -> city.getName()
							.toLowerCase()
							.startsWith(name.toLowerCase()))
					.sorted((firstCity, secondCity) ->
						firstCity.getName().compareTo(secondCity.getName()))
					.collect(toList());
	}
}
