package stickynote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class GetAllNotes 
{
	
	
	
	String file_name = "";
	String created_at ="";
	String updated_at="";
	


	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	

public List<GetAllNotes>getNotesList(DBCursor cursor)
	{
		DBObject obj = cursor.next();
		BasicDBList dbList = (BasicDBList) obj.get("notes");
		List<GetAllNotes> notesList = new ArrayList<GetAllNotes>();
		
		if(dbList == null)
		{
			GetNoNotes notes = new GetNoNotes();
			notes.setMessage("null");
			notesList.add(notes);
		}
		else
		{
			for (int i = 0; i < dbList.size(); i++) 
			{
		        BasicDBObject idObj = (BasicDBObject) dbList.get(i);
		        String filename = idObj.getString("filename");
		        String created_at = idObj.getString("created_time");
		        String updated_at = idObj.getString("updated_time");
		        GetAllNotes notes = new GetAllNotes();
		        notes.setFile_name(filename);
		        notes.setCreated_at(created_at);
		        notes.setUpdated_at(updated_at);
		        notesList.add(notes);
		    }
		}
		
		return notesList;
	}


}