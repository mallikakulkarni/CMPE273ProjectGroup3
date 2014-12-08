package stickynote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.hibernate.validator.constraints.NotEmpty;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;

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
	
	public String createFile(String userid,DbxClient client) throws DbxException
	{
		
		try{
		File fileDir = new File("./UserNote/"+userid);
		if(!(fileDir.exists()))
		{
			fileDir.mkdirs();
		}
		
		File file = new File("./UserNote/"+userid+"/"+file_name+".doc");
		if(!(file.exists()))
		{
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
		            
		        } finally {
		            inputStream.close();
		        }
		        
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
