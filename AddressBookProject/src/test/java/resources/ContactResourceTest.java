package resources;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import addressbook.domain.Contact;
import addressbook.resource.ContactResource;
import addressbook.service.FirebaseService;
import addressbook.serviceI.FirebaseServiceI;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;

@ExtendWith(DropwizardExtensionsSupport.class)
public class ContactResourceTest {

	private static final FirebaseServiceI firebaseService = mock(FirebaseService.class);
	private static final ResourceExtension contactResource = ResourceExtension.builder()
			.addResource(new ContactResource(firebaseService)).build();
	private Contact contact;
	String id;

	@BeforeEach
	void setup() {
		contact = new Contact("id", "name", "firstName", "address", new ArrayList<String>(), "homePage", new Date(), "note");
		id = "id";
	}

	@Test
	void getAllContacts_Success() throws InterruptedException, ExecutionException {
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(contact);
		contacts.add(contact);
		when(firebaseService.getAllContacts()).thenReturn(contacts);
		contactResource.target("/contact/" + id).request().get();
		Assert.assertEquals(contacts.size(), 2);
	}
	
	@Test
	void getContactById_Success() throws InterruptedException, ExecutionException {
		when(firebaseService.getContactById(id)).thenReturn(contact);
		Contact found = contactResource.target("/contact/" + id).request().get(Contact.class);
		Assert.assertEquals(contact.getId(), found.getId());
	}

	@Test
	void getContactById_Failure() throws InterruptedException, ExecutionException {
		when(firebaseService.getContactById(id)).thenReturn(null);
		Response response = contactResource.target("/contact/" + id).request().get();
		Assert.assertEquals(response.getStatusInfo().getStatusCode(), Response.Status.NOT_FOUND.getStatusCode());
	}


	@Test
	void deleteContact_Success() throws InterruptedException, ExecutionException {
		when(firebaseService.getContactById(id)).thenReturn(contact);
		when(firebaseService.getContactById(contact.getId())).thenReturn(contact);
		when(firebaseService.deleteContact(contact.getId())).thenReturn(id);
		contactResource.target("/contact/delete/" + id).request().delete();
		Assert.assertEquals(contact.getId(), id);
	}
	
	@Test
	void deleteContact_Failure() throws InterruptedException, ExecutionException {
		when(firebaseService.getContactById(id)).thenReturn(null);
		Response response = contactResource.target("/contact/delete/" + id).request().delete();
		Assert.assertEquals(response.getStatusInfo().getStatusCode(), Response.Status.NOT_FOUND.getStatusCode());
	}
}
