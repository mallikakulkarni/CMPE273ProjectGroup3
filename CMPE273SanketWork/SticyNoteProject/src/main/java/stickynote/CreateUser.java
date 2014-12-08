package stickynote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class CreateUser {

	String userid = "";
	@Email (message = "Email format is not valid")	//Email format validation
	@NotEmpty (message = "Please enter email id")	//Email should not empty
	String email="";
	@NotEmpty (message = "Please enter password")   //password should not empty
	String password="";
	String created_at ="";
	String updated_at ="";

	
	public CreateUser()
	{
		//System.out.println("yessss it is calling");
		
		this.setUserid();
		this.setCreated_at();	
		this.setUpdated_at();
	}
	
	public String getUserid() {
		return userid;
	}

	//Set random unique user id
	public void setUserid() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setTimeZone(tz);
		this.userid =  "U-"+df.format(new Date());
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

	public String getCreated_at() {
		return created_at;
	}
	
	//Set current time
	public void setCreated_at() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		df.setTimeZone(tz);
		this.created_at = df.format(new Date());
	}	
	
	public String getUpdated_at() {
		return updated_at;
	}
	
	//Set initial update time
	public void setUpdated_at() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		df.setTimeZone(tz);
		this.updated_at = df.format(new Date());
	}
	
}
