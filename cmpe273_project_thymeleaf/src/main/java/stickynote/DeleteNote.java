package stickynote;

import java.io.File;

import com.dropbox.core.DbxClient;

public class DeleteNote {

	public String deleteNoteAction(DbxClient client, String file_name, String userid) {
		try{
		
		File file = new File("./UserNote/"+userid+"/"+file_name+".doc");
		file.delete();
		client.delete("/"+file_name+".doc");
		return "success";
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	}

	
	
}
