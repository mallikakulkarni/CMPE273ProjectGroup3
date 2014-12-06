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
			this.error = "authorization_url: "+input;
		}
	}
	
	public String getError() {
		return error;
	}
	
}
