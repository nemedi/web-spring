package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {
	
	@Autowired
	private ContactService service;
	
	@GetMapping("/")
	public String showListForm(Model model) {
		model.addAttribute("contacts", service.getContacts());
		return "listForm";
	}

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        ContactModel contact = "<new>".equals(id)
        		? new ContactModel()
        		: service.getContact(id);
        model.addAttribute("contact", contact);
        return "editForm";
    }
    
    @PostMapping("/save")
    public String saveContact(ContactModel contact, Model model) throws Exception {
    	service.saveContact(contact);
        return "redirect:/";
    }
    
    @GetMapping("/remove/{id}")
    public String removeContact(@PathVariable("id") String id, Model model) throws Exception {
        service.removeContact(id);
        return "redirect:/";
    }
    
}
