package stickynote;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.validator.constraints.NotEmpty;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class CreateNote {
	
	public CreateNote()
	{
	}

	@NotEmpty (message = "Please enter file_name")
	String file_name="";
	String userid ="";
	String file_data="";
	
	public String getFile_data() {
		return file_data;
	}
	public void setFile_data(String file_data) {
		this.file_data = file_data;
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
	
	
	public String createFile(String userid, DbxClient client, DBCursor cursor) throws DbxException, IOException
	{
		
		
		File fileDir = new File("./UserNote/"+userid);
		if(!(fileDir.exists()))
		{
			fileDir.mkdirs();
		}
		
		File file = new File("./UserNote/"+userid+"/"+file_name+".doc");
		
		if(cursor.hasNext())
		{
			BasicDBObject query1 = (BasicDBObject) cursor.next();
    		try{
    			BasicDBList dblist = (BasicDBList) query1.get("notes");
				if(dblist.toString().contains(this.getFile_name()))
				{
					return "file already exists";
				}
				else
				{
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
				            createDbMetadata(userid,file_name);
				            
				        } finally {
				            inputStream.close();
				        }
				        
					return "created";
				}
				}
    		
    		catch(Exception e)
    		{
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
			            createDbMetadata(userid,file_name);
			            
			        } finally {
			            inputStream.close();
			        }
			   return "created";
    		 }
		}
		else
		{
			return "user does not exist";
		}
			
	}
	
	
	public void createDbMetadata(String userid,String file_name) throws UnknownHostException
	{
		 DBCollection coll;
		 coll =  DBConnection.getConnection();
		 TimeZone tz = TimeZone.getTimeZone("UTC");
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		 df.setTimeZone(tz);
		 DBObject basicQuery = new BasicDBObject("userid", userid);
		 BasicDBObject newNote =  new BasicDBObject()
		 				.append("filename",file_name)
		 				.append("created_time",df.format(new Date()))
		 				.append("updated_time",df.format(new Date()));
		 				
		 DBCursor cur = coll.find(basicQuery);
		 boolean isNotFirstFile = cur.hasNext();
		 
		 if(isNotFirstFile)
		 {
			 //System.out.println("Not first File for user :"+userid);
			 BasicDBObject userNotes = new BasicDBObject()
			 .append("notes", newNote);
			 DBObject updateQuery = new BasicDBObject("$push", userNotes);
			 coll.update(basicQuery, updateQuery);
			 File file = new File("./UserNote/"+userid+"/"+file_name+".doc");
			 if(file.exists())
	  		 {
	  			file.delete();
	  		 }
		 }
		 
		 else
		 {
			  //System.out.println("is first note");
			  List<BasicDBObject> dbObjList = new ArrayList<BasicDBObject>();
			  dbObjList.add(newNote);
			  basicQuery.put("notes",dbObjList);
			  coll.insert(basicQuery);
			  File file = new File("./UserNote/"+userid+"/"+file_name+".doc");
			  if(file.exists())
  			{
  				file.delete();
  			}
		}
		// System.out.println("Inserting the note info in the DB");
		
	}

	
	
}