package stickynote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;


public class GetNote {

	String file_name = "";
	String userid= "";
	String file_data= "";
	
	
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
	
	public String getFile(String userid, DbxClient client) throws IOException {
		
		    FileOutputStream outputStream = null;
			try{
			File fileDir = new File("./DownloadedNote/"+userid);
			if(!(fileDir.exists()))
			{
				fileDir.mkdirs();
			}
		
				outputStream = new FileOutputStream("./DownloadedNote/"+userid+"/"+this.getFile_name()+".doc");
			
			    DbxEntry.File downloadedFile = client.getFile("/"+this.getFile_name()+".doc", null,
	                outputStream);
	            			    
			    return "success";
	        }
	        catch(Exception e)
	        {
	        	return e.getMessage();
	        }
	        finally
	        {
	        	outputStream.close();     
	        }			
			
		}
	
	public String readFile(String file_name) throws IOException
	{
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
			
			br = new BufferedReader(new FileReader("./DownloadedNote/"+userid+"/"+this.getFile_name()+".doc"));
			while ((sCurrentLine = br.readLine()) != null) 
			{				
				file_data = file_data +"\n"+ sCurrentLine;
			}
			
			this.setFile_data(file_data);
			return "success";
			} 
			catch (IOException e) 
			{
			return e.getMessage();
			}
			finally 
			{
				br.close();
			}
		}
}
