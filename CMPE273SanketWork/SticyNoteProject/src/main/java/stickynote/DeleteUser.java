package stickynote;

public class DeleteUser {
	
	String user_id;
	String message;
	
	
	public DeleteUser(String user_id,String message)
	{
		this.message = user_id + message;
		this.user_id = user_id;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

}
