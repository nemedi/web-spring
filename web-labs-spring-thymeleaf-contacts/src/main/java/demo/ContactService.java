package demo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
	
	@Autowired
	private ContactRepository repository;

	public Collection<ContactModel> getContacts() {
		return repository.findAll();
	}
	
	public ContactModel getContact(String id) {
		return repository.findById(id).orElseThrow(() ->
			new IllegalArgumentException("Invalid user id: " + id));
	}
	
	public void saveContact(ContactModel contact) throws Exception {
		repository.save(contact);
	}
	
	public void removeContact(String id) throws Exception {
		repository.deleteById(id);
	}
}
