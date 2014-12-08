package stickynote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.validator.constraints.NotEmpty;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;

public class CreateNote {
	
	public CreateNote()
	{
		this.setCreated_at();
	}

	@NotEmpty (message = "Please enter file_name")
	String file_name="";
	@NotEmpty (message = "Please enter file_title")
	String file_title="";
	String created_at="";
	String userid ="";
	
	String authorizationCode ="";
	
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
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
	public String getFile_title() {
		return file_title;
	}
	public void setFile_title(String file_title) {
		this.file_title = file_title;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T':HH:mm:ss'Z'");
		df.setTimeZone(tz);
		this.created_at = df.format(new Date());
	}
	
	public String createFile(String userid,DbxClient client) throws DbxException
	{
		
		try{
		File fileDir = new File("./UserNote/"+userid);
		if(!(fileDir.exists()))
		{
			fileDir.mkdirs();
		}
		
		File file = new File("./UserNote/"+userid+"/"+file_name);
		if(!(file.exists()))
		{
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Title: "+file_title);
			bw.close();
			
			  File inputFile = new File("./UserNote/"+userid+"/"+file_name);
		       // inputFile.createNewFile();
		        FileInputStream inputStream = new FileInputStream(inputFile);
		        
		        try {
		            DbxEntry.File uploadedFile = client.uploadFile("/"+file_name,
		                DbxWriteMode.add(), inputFile.length(), inputStream);
		            System.out.println("Uploaded: " + uploadedFile.toString());
		        } finally {
		            inputStream.close();
		        }
		        
		    /*    DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
		        System.out.println("Files in the root path:");
		        for (DbxEntry child : listing.children) {
		            System.out.println("	" + child.name + ": " + child.toString());
		        }
		        
		        FileOutputStream outputStream = new FileOutputStream("xxxxxxAmanFile7.doc");
		        try {
		            DbxEntry.File downloadedFile = client.getFile("/AmanFile7.doc", null,
		                outputStream);
		            System.out.println("Metadata: " + downloadedFile.toString());
		        } finally {
		            outputStream.close();
		        }
		      */  
		        
			return "created";
		}
		else
		{
			return "file already exist";}
		}
		catch(IOException e)
		{
			return e.toString();
		}
		
	}
	
	
}
