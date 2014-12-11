package stickynote;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


public class UpdateUser {
	
	String userid="";
	//@NotEmpty (message = "Please enter name")
	String name="";
	//@Email (message = "Email format is not valid")
	//@NotEmpty (message = "Please enter email id")
	String email="";
	//@NotEmpty (message = "Please enter contact number") 
	String contactNumber = "";
	//@NotEmpty (message = "Please enter password") 
	String password="";
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
