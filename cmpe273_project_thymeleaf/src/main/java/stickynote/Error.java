package stickynote;

public class Error {

	String error="";
	//single attributed error constructor
	public Error(String input)
	{
		this.error = "Invalid "+input;
	}
	public Error(String input, int i)
	{
		if(i==1)
		{
		this.error = input;
		}
		else if(i==2)
		{
			this.error = "Register App with your Dropbox Go to authorization_url "+input;
		}
	}
	public Error(String input1, String input2)
	{
		this.error = input1 +"\n"+ input2 ;		
	}
	
	public String getError() {
		return error;
	}
	
}
