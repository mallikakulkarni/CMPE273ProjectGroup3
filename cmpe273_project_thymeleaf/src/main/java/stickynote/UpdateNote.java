package stickynote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class UpdateNote {
	
	public UpdateNote()
	{
		this.setUpdate_time();
	}
	
	String update_time ="";
	String create_time ="";
	String userid ="";
	String file_name ="";
	String file_data ="";
	
	public String getUpdate_time() {
		return update_time;
	}
	
	public void setUpdate_time() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		df.setTimeZone(tz);
		this.update_time = df.format(new Date());
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_data() {
		return file_data;
	}
	public void setFile_data(String file_data) {
		this.file_data = file_data;
	}


	public String updateFile(String userid, DbxClient client, String file_name) throws DbxException 
	{
		
		this.setUserid(userid);
		
		// CREATE TIME WILL COME FROM DB
		
		
		try{
		File fileDir = new File("./UserNote/"+userid);
		if(!(fileDir.exists()))
		{
			fileDir.mkdirs();
		}
		
		File file = new File("./UserNote/"+userid+"/"+file_name+".doc");
		
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Title: "+file_name);
			bw.write("\n");
			bw.write("============================================================================================================================");
			bw.write("\n");
			bw.write("\n");
			bw.write(file_data);
			bw.write("\n");
			bw.write("\n");
			bw.write("============================================================================================================================");
			bw.close();
			
			  File inputFile = new File("./UserNote/"+userid+"/"+file_name+".doc");
		       // inputFile.createNewFile();
		        FileInputStream inputStream = new FileInputStream(inputFile);
		        
		        try {
		            DbxEntry.File uploadedFile = client.uploadFile("/"+file_name+".doc",
		                DbxWriteMode.add(), inputFile.length(), inputStream);
		          //updateDbMetadata(userid,file_name);
		            
		        } finally {
		            inputStream.close();
		        }
		        
			return "created";
		}
		catch(IOException e)
		{
			return e.toString();
		}
		
	}		
	
	public void updateDbMetadata(String userid,String file_name) throws UnknownHostException
	{
		DBCollection coll;
		coll =  DBConnection.getConnection();
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		df.setTimeZone(tz);
		BasicDBObject query = new BasicDBObject();
		query.put("userid", userid);
		DBCursor cursor = coll.find(query);
		DBObject obj = cursor.next();
		
//		BasicDBObject query = new BasicDBObject();
//		query.put("userid", userid);
//		BasicDBObject updateTime = new BasicDBObject();
//		updateTime.put("updated_time", df.format(new Date()));
//		BasicDBObject update = new BasicDBObject();
//		//update.append("$set", new BasicDBObject().append("updated_time", df.format(new Date())));
//		//update.append("$set", new BasicDBObject().append("updated_time", "just testing"));
//		coll.update(query, update);
		
//		BasicDBObject query = new BasicDBObject("userid", userid);
//		BasicDBObject time = new BasicDBObject();
//		time.put("updated_time", df.format(new Date()));
//		BasicDBObject note = new BasicDBObject("filename",file_name);
//		query.put("notes", note);
//		note.append("$set", new BasicDBObject("notes",time));
//		coll.update(query,note);
		
//		BasicDBObject query = new BasicDBObject();
//		query.put("userid", userid);
//		BasicDBObject time = new BasicDBObject();
//		time.put("updated_time", df.format(new Date()));
//		BasicDBObject update = new BasicDBObject();
//		update.put("$push", new BasicDBObject("notes",time));
//		coll.update(query, update);
		
		
//		DBObject query = new BasicDBObject("userid", userid);
//		query.put("notes.filename", file_name);
//		DBObject update = new BasicDBObject();
//		update.put("$set", new BasicDBObject("notes.updated_time",df.format(new Date())));
//		//coll.update({"userid", userid,"notes.filename",query, update);
//		coll.update(query, update);
//		

//		DBCursor cursor = coll.find(query);
//		DBObject obj = cursor.next();
//		BasicDBList dbList = (BasicDBList) obj.get("notes");
//		List<GetAllNotes> notesList = new ArrayList<GetAllNotes>();
//		
//		
//			for (int i = 0; i < dbList.size(); i++) 
//			{
//		        BasicDBObject idObj = (BasicDBObject) dbList.get(i);
//		        String filename = idObj.getString("filename");
//		        String created_at = idObj.getString("created_time");
//		        String updated_at = idObj.getString("updated_time");
//		        GetAllNotes notes = new GetAllNotes();
//		        notes.setFile_name(filename);
//		        notes.setCreated_at(created_at);
//		        notes.setUpdated_at(updated_at);
//		        notesList.add(notes);
//		    }
//		
		
	}


}
