package stickynote;

import java.io.File;
import java.net.UnknownHostException;

import com.dropbox.core.DbxClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

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

	public void deleteNoteMetaDataDb(String userid,String file_name) throws UnknownHostException
	{
		DBCollection coll =  DBConnection.getConnection();
		BasicDBObject query = new BasicDBObject();
		query.put("userid", userid);
		 BasicDBObject delNote = new BasicDBObject();
		 delNote.put("filename", file_name);
		 BasicDBObject remove = new BasicDBObject();
		 remove.put("$pull", new BasicDBObject("notes",delNote));
		 coll.update(query,remove);
	}

	
	
}