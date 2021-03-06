package addressbook.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;

import addressbook.domain.Contact;
import addressbook.serviceI.FirebaseServiceI;

public class FirebaseService implements FirebaseServiceI {

	private Firestore dbFirestore;
	
	
	public FirebaseService() {
		this.dbFirestore = FirestoreClient.getFirestore();
	}

	public List<Contact> getAllContacts() throws InterruptedException, ExecutionException {
		List<Contact> contacts = new ArrayList<Contact>();
		List<QueryDocumentSnapshot> documents = dbFirestore.collection("contacts").get().get().getDocuments();
		for (QueryDocumentSnapshot document : documents) {
			contacts.add(document.toObject(Contact.class));
		}
		return contacts;
	}

	public Contact getContactById(String id) throws InterruptedException, ExecutionException {
		DocumentReference docRef = dbFirestore.collection("contacts").whereEqualTo("id", id).get().get().getDocuments()
				.get(0).getReference();
		return docRef.get().get().toObject(Contact.class);
	}

	public Contact addContact(Contact contact, InputStream fileInputStream) throws InterruptedException, ExecutionException, IOException {
//		byte[] bytes = IOUtils.toByteArray(fileInputStream);
//		contact.setPicture(bytes);
		DocumentReference docRef = dbFirestore.collection("contacts").document();
		docRef.set(contact);
		return docRef.get().get().toObject(Contact.class);
	}

	public String deleteContact(String id) throws InterruptedException, ExecutionException {
		DocumentReference docRef = dbFirestore.collection("contacts").whereEqualTo("id", id).get().get().getDocuments().get(0).getReference();
		String deletedId = docRef.get().get().toObject(Contact.class).getId();
		docRef.delete();
		return deletedId;
	}

	public Contact updateContact(Contact contact) throws InterruptedException, ExecutionException {
		DocumentReference docRef = dbFirestore.collection("contacts").whereEqualTo("id", contact.getId()).get().get().getDocuments().get(0).getReference();
		docRef.set(contact);
		return docRef.get().get().toObject(Contact.class);
	}
}
