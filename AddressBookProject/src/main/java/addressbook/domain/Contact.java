package addressbook.domain;

import java.util.Date;
import java.util.List;

import javax.management.ConstructorParameters;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Contact {
	
	private String id; 

	private String name;

	private String firstName;

	private String address;

	private List<String> phone;

	@JsonIgnore
	private byte[] image; // save as bytes or in firebase storage

	private String homePage;

	private Date birthday;

	private String note;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", firstName=" + firstName + ", address=" + address + ", phone="
				+ phone + ", homePage=" + homePage + ", note=" + note + "]";
	}

	@JsonIgnore
	public byte[] getimage() {
		return image;
	}

	@JsonIgnore
	public void setimage(byte[] image) {
		this.image = image;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Contact() {}
	
	@ConstructorParameters({"od", "name", "firstName", "address", "phone", "homePage", "birthday", "note"})
	public Contact(String id, String name, String firstName, String address, List<String> phone, String homePage, Date birthday,
			String note) {
		super();
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.address = address;
		this.phone = phone;
		this.homePage = homePage;
		this.birthday = birthday;
		this.note = note;
	}
}
