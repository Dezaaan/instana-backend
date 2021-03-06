package addressbook.serviceI;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import addressbook.domain.Contact;

public interface FirebaseServiceI {
	
	public List<Contact> getAllContacts() throws InterruptedException, ExecutionException;
	
	public Contact getContactById(String id) throws InterruptedException, ExecutionException;

	public Contact addContact(Contact contact, InputStream fileInputStream) throws InterruptedException, ExecutionException, IOException;

	public String deleteContact(String id) throws InterruptedException, ExecutionException;

	public Contact updateContact(Contact contact) throws InterruptedException, ExecutionException;
}
