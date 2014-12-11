package stickynote;

public class Success {

	public Success(String message)
	{
		this.setMessage(message);
	}
	
	String message = "";
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
