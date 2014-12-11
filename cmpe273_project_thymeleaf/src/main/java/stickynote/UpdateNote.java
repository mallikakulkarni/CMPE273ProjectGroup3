package stickynote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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


	public String updateFile(String userid, DbxClient client, String file_name) throws DbxException {
		
		this.setUserid(userid);
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
			bw.write(file_data);
			bw.close();
			
			  File inputFile = new File("./UserNote/"+userid+"/"+file_name+".doc");
		       // inputFile.createNewFile();
		        FileInputStream inputStream = new FileInputStream(inputFile);
		        
		        try {
		            DbxEntry.File uploadedFile = client.uploadFile("/"+file_name+".doc",
		                DbxWriteMode.add(), inputFile.length(), inputStream);
		            updateDbMetadata(userid,file_name);
		            
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
	
	
	public void updateDbMetadata(String userid,String file_name) throws java.net.UnknownHostException
	{
		DBCollection coll;
		coll =  DBConnection.getConnection();
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		df.setTimeZone(tz);
		DBObject query = new BasicDBObject().append("userid", userid).append("notes.filename", file_name);
		DBObject update = new BasicDBObject();
		update.put("$set", new BasicDBObject("notes.$.updated_time",df.format(new Date())));
		coll.update(query, update);
	}	


}
