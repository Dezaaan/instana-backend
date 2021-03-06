package addressbook.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import addressbook.domain.Contact;
import addressbook.serviceI.FirebaseServiceI;

@Path("/contact")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContactResource {

	private FirebaseServiceI firebaseService;

	public ContactResource(FirebaseServiceI firebaseService) {
		this.firebaseService = firebaseService;
	}

	@GET
	public Response getAllContacts() throws InterruptedException, ExecutionException {
		return Response.ok(firebaseService.getAllContacts()).build();
	}

	@GET
	@Path("/{id}")
	public Response getContact(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		Contact c = firebaseService.getContactById(id);
		if (c != null) {
			return Response.ok(c).build();
		} else
			return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addContact(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData,
			@FormDataParam("contact") String contactJson)
			throws URISyntaxException, InterruptedException, ExecutionException, IOException {
		Contact contact = new ObjectMapper().readValue(contactJson, Contact.class);
		contact = firebaseService.addContact(contact, fileInputStream);
		return Response.ok(contact).build();
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response updateContact(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData,
			@FormDataParam("contact") String contactJson)
			throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {
		System.out.println(contactJson);
		Contact contact = new ObjectMapper().readValue(contactJson, Contact.class);
		Contact c = firebaseService.getContactById(contact.getId());
		if (c != null) {
			contact = firebaseService.updateContact(contact);
			return Response.ok(contact).build();
		} else
			return Response.status(Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("/delete/{id}")
	public Response removeContactById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		Contact contact = firebaseService.getContactById(id);
		if (contact != null) {
			String deletedId = firebaseService.deleteContact(id);
			return Response.ok(deletedId).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
