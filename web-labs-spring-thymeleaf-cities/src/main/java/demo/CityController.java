package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CityController {
	
	@Autowired
	private CityService service;

	@RequestMapping("/")
	public String search(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("name", name);
		model.addAttribute("cities", service.searchCitiesByName(name));
		return "index";
	}
}
