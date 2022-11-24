package demo;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ContactRepository {

	private File file;
	private Map<String, ContactModel> contacts;
	
	public ContactRepository(@Value("${repository}") String repository) throws Exception {
		file = Paths.get(repository).toFile();
		contacts = file.exists()
				? Arrays.stream(new ObjectMapper()
							.readValue(file, ContactModel[].class))
						.collect(toMap(contact -> contact.getId(), contact -> contact))
				: new HashMap<String, ContactModel>();
	}
	
	public Collection<ContactModel> findAll() {
		return contacts.values();
	}
	
	public Optional<ContactModel> findById(String id) {
		return contacts.containsKey(id)
				? Optional.of(contacts.get(id))
				: Optional.empty();
	}

	public void save(ContactModel contact) throws Exception {
		if (contacts.containsKey(contact.getId())) {
			contacts.get(contact.getId()).setFirstName(contact.getFirstName());
			contacts.get(contact.getId()).setLastName(contact.getLastName());
			contacts.get(contact.getId()).setEmail(contact.getEmail());
		} else {
			contacts.put(contact.getId(), contact);
		}
		save();
	}
	
	public void deleteById(String id) throws Exception {
		if (contacts.containsKey(id)) {
			contacts.remove(id);
			save();
		}
	}

	private void save() throws Exception {
		new ObjectMapper().writeValue(file, findAll());
	}
	
}
