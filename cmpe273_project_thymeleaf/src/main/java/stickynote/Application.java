package stickynote;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@RestController
@Configuration
@ComponentScan
@EnableAutoConfiguration
@RequestMapping("/stickynote")
public class Application {
	 
	 DBCollection coll; 
	 BasicDBObject doc;
	 
	 final String APP_KEY = "mwa414ncuw5f56p";
	 final String APP_SECRET = "23w343owiztqqwb";

	 DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

	 DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
	 Locale.getDefault().toString());
	 DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
	 String authorizeUrl = webAuth.start();
	 DbxClient client;
	 HashMap<String,Object> clientDropboxInfo = new HashMap<String, Object>();
	
	    
	 public static void main(String[] args) 
	 {
		//new DBConnection();
	    SpringApplication.run(Application.class, args);
	 }
	 
	 //welcome message
	 @RequestMapping(value = "", method = RequestMethod.GET)
	 @ResponseBody
	 public String welcomeMessage()
	 {
	   	return "Welcome to Sticky notes";    	
	 }
	 	 
	 //Creating user
	 @RequestMapping(value= "/users", method = RequestMethod.POST)
	 @ResponseBody
	 public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUser user) throws UnknownHostException
	 {
		 coll =  DBConnection.getConnection();
		 BasicDBObject query = new BasicDBObject("email", user.getEmail()); // for eamil already exist validation
		 DBCursor cursor = coll.find(query);
		 try {
			 	if(cursor.hasNext())
			 	{	return new ResponseEntity<Object>(new Error(user.getEmail()),HttpStatus.BAD_REQUEST);
			 	}else{    		
				 doc = new BasicDBObject("userid", user.getUserid()).append("name",user.getName()).append("email", user.getEmail()).append("contactNumber", user.getContactNumber()).append("password", user.getPassword()).append("created_at", user.getCreated_at());
				 coll.insert(doc);
				 return new ResponseEntity<Object>(user, HttpStatus.CREATED);
			 	}
			 }
		 finally {cursor.close();}  	
	  }
	  
	 //get user details
	 @RequestMapping(value ="/users/{userid}", method = RequestMethod.GET)
	 @ResponseBody
	 public ResponseEntity<Object> getUser(@PathVariable String userid) throws UnknownHostException
	 {
	    coll =  DBConnection.getConnection();
	    BasicDBObject query = new BasicDBObject("userid", userid);
	    DBCursor cursor = coll.find(query);
	    try {
	    		if(cursor.hasNext())
	    		{	GetUser getUser = new GetUser(cursor);
	    			return new ResponseEntity<Object>(getUser, HttpStatus.OK);
	    		}
	    		else{
	    		return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
	    		}
	    	}
	    finally{cursor.close();}
	 }
	   
	 //Update User Details
	 @RequestMapping(value ="/users/{userid}", method = RequestMethod.PUT)
	 @ResponseBody
	 public ResponseEntity<Object> updateUser(@PathVariable String userid,@Valid @RequestBody UpdateUser updateUser) 
			 
			 throws UnknownHostException
	 {
	    coll =  DBConnection.getConnection();
	    BasicDBObject query = new BasicDBObject("userid", userid);
	    DBCursor cursor = coll.find(query);
	    try {
	    		if(cursor.hasNext())
	    		{
	    			//GetUser user = new GetUser(cursor);
	    			BasicDBObject update = new BasicDBObject();
	    			update.put("$set", new BasicDBObject("email",updateUser.getEmail())
	    					.append("password", updateUser.getPassword()).append("name", updateUser.getName()).append("contactNumber", updateUser.getContactNumber()));
	    		 	coll.update(query,update);
	    		 	updateUser.setUserid(userid);
	    		 	//user.setEmail(updateUser.getEmail());
	    		 	//user.setPassword(updateUser.getPassword());
	    			return new ResponseEntity<Object>(updateUser, HttpStatus.OK);
	    		}
	    		else{
	    		return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
	    		}
	    	}
	    finally{cursor.close();}
	 }
	 
	 //Deleting user
	 @RequestMapping(value ="/users/{userid}", method = RequestMethod.DELETE)
	 @ResponseBody
	 public ResponseEntity<Object> deleteUser(@PathVariable String userid) 
			 
			 throws UnknownHostException
	 {
	    coll =  DBConnection.getConnection();
	    BasicDBObject query = new BasicDBObject("userid", userid);
	    DBCursor cursor = coll.find(query);
	    try {
	    		if(cursor.hasNext())
	    		{	
	    			GetUser getUser = new GetUser(cursor);
	    			coll.remove(query);
	    			DeleteUser user = new DeleteUser(getUser.getUserid()," user has been deleted");
	    			return new ResponseEntity<Object>(user, HttpStatus.OK);
	    		}
	    		else{
	    		return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
	    		}
	    	}
	    finally{cursor.close();}
	 }
	 
	 //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	 //getAuthorizationUrl
	 @RequestMapping(value="users/{userid}/authorizationUrl", method = RequestMethod.GET)
	 @ResponseBody
	 public ResponseEntity<Object> getAuthorizationUrl(@PathVariable String userid) throws UnknownHostException
	 {
		 coll =  DBConnection.getConnection();
		    BasicDBObject query = new BasicDBObject("userid", userid);
		    DBCursor cursor = coll.find(query);
		    try {
		    		if(cursor.hasNext())
		    		{	
		    			return new ResponseEntity<Object>(authorizeUrl, HttpStatus.OK);
		    		}
		    		else{
		    		return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
		    		}
		    	}
		    finally{cursor.close();}
	 }
	 
	 
	 //Authorize User for Dropbox
	 @RequestMapping(value="users/{userid}/authorizationCode/{authorizationCode}", method = RequestMethod.GET)
	 @ResponseBody
	 public ResponseEntity<Object> authorizeUser(@PathVariable String userid, @PathVariable String authorizationCode) throws UnknownHostException
	 {
		 coll =  DBConnection.getConnection();
		 BasicDBObject query = new BasicDBObject("userid", userid);
		 DBCursor cursor = coll.find(query);
		 try {
		  		if(cursor.hasNext())
		   		{	
		  			 if(!(clientDropboxInfo.containsKey(userid)))
		    		 {
		  				 CreateClientObject cc = new CreateClientObject();
	    				 try{
	    			        client = cc.getClientObject(authorizationCode, webAuth, config);
	    			        clientDropboxInfo.put(userid, client);
	    			        return new ResponseEntity<Object>(new Success("User Authorized"), HttpStatus.OK);
	    			        
	    				 }catch(Exception e)
	    				 {
	    					 return new ResponseEntity<Object>(new Error(e.getMessage(), "AuthorizationCode not valid Go to url   "+ authorizeUrl), HttpStatus.BAD_REQUEST);
	    				 }
		    		 }
		  			 else
		  			 {
		  				CreateClientObject cc = new CreateClientObject();
	    				 try{
	    			        client = cc.getClientObject(authorizationCode, webAuth, config);
	    			        //System.out.println(createNote.getAuthorizationCode());
	    			        clientDropboxInfo.remove(userid);
	    			        clientDropboxInfo.put(userid, client);
	    			        return new ResponseEntity<Object>(new Success("User Authorization Updated"), HttpStatus.OK);
	    			        
	    				 }catch(Exception e)
	    				 {
	    					 return new ResponseEntity<Object>(new Error(e.getMessage(), "user already authorized new AuthorizationCode is not valid Go to url : "+ authorizeUrl), HttpStatus.BAD_REQUEST);
	    				 }
		  			 }
		   		}
		  		else
		  		{
		  			return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
		  		}
		 	}
		 
			catch(Exception e)
	    	{
	    		return new ResponseEntity<Object>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
	    	}
	    	finally
	    	{
	    		cursor.close();
	    	}
	 }
	 
	 //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	 
	 //Create StickyNote
	 @RequestMapping(value ="/users/{userid}/note", method = RequestMethod.POST)
	 @ResponseBody
	 public ResponseEntity<Object> createNote(@PathVariable String userid, @Valid @RequestBody CreateNote createNote) throws UnknownHostException, DbxException
	 {
	    coll =  DBConnection.getConnection();
	    BasicDBObject query = new BasicDBObject("userid", userid);
	    DBCursor cursor = coll.find(query);
	    try {
	    		if(cursor.hasNext())
	    		{	
	    			 if(!(clientDropboxInfo.containsKey(userid)))
	    			 {
	    			     return new ResponseEntity<Object>(new Error("User is not Authorized with Dropbox. Go to Settings and authorize user.",1), HttpStatus.BAD_REQUEST);
	    			 }
	    			 else
	    			 {
	    				 createNote.setUserid(userid);
	    				 client = (DbxClient)clientDropboxInfo.get(userid);
	    				 
		    			 String response = createNote.createFile(userid,client);
		    			 if(response.equals("created"))
		    			 {	
		    				 return new ResponseEntity<Object>(createNote, HttpStatus.CREATED);
		    			 }
		    			 else
		    			 {
		    			     return new ResponseEntity<Object>(new Error(response,1), HttpStatus.BAD_REQUEST);
		    			 }
	    			 }   		  
	    		  }
	    		  else
	    		  {
	    			return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
	    		  }
	    	}
	    	catch(Exception e)
	    	{
	    		return new ResponseEntity<Object>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
	    	}
	    	finally
	    	{
	    		cursor.close();
	    	}
	 }
	 
		 
	 
	//get particular note 
	@RequestMapping(value ="/users/{userid}/note/{file_name}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getNote(@PathVariable String userid, @PathVariable String file_name) throws IOException, UnknownHostException
	{
		  coll =  DBConnection.getConnection();
		  BasicDBObject query = new BasicDBObject("userid", userid);
		  DBCursor cursor = coll.find(query);
		  try{
		    if(cursor.hasNext())
	    	 {	
	    		 if(!(clientDropboxInfo.containsKey(userid)))
	    		 {
	    			 return new ResponseEntity<Object>(new Error("User is not Authorized with Dropbox. Go to Settings and authorize user.",1), HttpStatus.BAD_REQUEST);
	    		 }
	    		else
	    		{
	    			GetNote getNote = new GetNote();
	    			getNote.setFile_name(file_name);
	    			getNote.setUserid(userid);
	    			String response = getNote.getFile(userid, (DbxClient)clientDropboxInfo.get(userid));
		    		if(response.equals("success"))
		    		{
		    			String res = getNote.readFile(file_name);
		    			if(res.equals("success"))
		    			{
		    			File f = new File("./DownloadedNote/"+userid+"/"+file_name+".doc");
		    			if(f.exists())
		    			{
		    				f.delete();
		    			}
		    			 return new ResponseEntity<Object>(getNote, HttpStatus.OK);
		    			}
		    			else
		    			{
		    			return new ResponseEntity<Object>(new Error(res,1), HttpStatus.BAD_REQUEST);
		    			}
		    		}
		    		else
		    		{
		    			 return new ResponseEntity<Object>(new Error(response,1), HttpStatus.BAD_REQUEST);
		    		}
	    		}
		    }
			else
			{
				return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
			}
		   }
		   finally
		   {cursor.close();}
	}
	
	//Update note 
	@RequestMapping(value ="/users/{userid}/note/{file_name}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Object> updateNote(@PathVariable String userid, @PathVariable String file_name, @RequestBody UpdateNote updateNote) throws IOException, UnknownHostException, DbxException
		{
			coll =  DBConnection.getConnection();
			BasicDBObject query = new BasicDBObject("userid", userid);
			DBCursor cursor = coll.find(query);
			try{
			 if(cursor.hasNext())
		     {	
				if(!(clientDropboxInfo.containsKey(userid)))
		    	{
				 return new ResponseEntity<Object>(new Error("User is not Authorized with Dropbox. Go to Settings and authorize user.",1), HttpStatus.BAD_REQUEST);
		    	}
		    	else
		    	{
		    		
		    		DbxClient client = (DbxClient)clientDropboxInfo.get(userid);
		    		DeleteNote deleteNote = new DeleteNote();
		    		String res = deleteNote.deleteNoteAction(client, file_name, userid);
		    		if(res.equals("success"))
		    		{
		    			 String response = updateNote.updateFile(userid,client,file_name);
		    			 if(response.equals("created"))
		    			 {	
		    				 return new ResponseEntity<Object>(updateNote, HttpStatus.CREATED);
		    			 }
		    			 else
		    			 {
		    			     return new ResponseEntity<Object>(new Error(response,1), HttpStatus.BAD_REQUEST);
		    			 }
		    			
		    		}
		    		else
		    		{
		    			return new ResponseEntity<Object>(new Error(res,1), HttpStatus.BAD_REQUEST);
		    		}
		    				    		
			    }
		     }
			else
			{
				return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
			}
			}
			finally
			{cursor.close();}
		}
	
	
	//Delete note 
	@RequestMapping(value ="/users/{userid}/note/{file_name}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteNote(@PathVariable String userid, @PathVariable String file_name) throws IOException, UnknownHostException
		{
			coll =  DBConnection.getConnection();
			BasicDBObject query = new BasicDBObject("userid", userid);
			DBCursor cursor = coll.find(query);
			try{
			 if(cursor.hasNext())
		     {	
				if(!(clientDropboxInfo.containsKey(userid)))
		    	{
				 return new ResponseEntity<Object>(new Error("User is not Authorized with Dropbox. Go to Settings and authorize user.",1), HttpStatus.BAD_REQUEST);
		    	}
		    	else
		    	{
		    		
		    		DbxClient client = (DbxClient)clientDropboxInfo.get(userid);
		    		DeleteNote deleteNote = new DeleteNote();
		    		String res = deleteNote.deleteNoteAction(client, file_name, userid);
		    		if(res.equals("success"))
		    		{
		    			return new ResponseEntity<Object>(new Success(file_name+ " successfully deleted..!!"), HttpStatus.OK);
		    		}
		    		else
		    		{
		    			return new ResponseEntity<Object>(new Error(res,1), HttpStatus.BAD_REQUEST);
		    		}
		    				    		
			    }
		     }
			else
			{
				return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
			}
			}
			finally
			{cursor.close();}
		}
			
	
	 //handling exceptions
	 @ExceptionHandler({MethodArgumentNotValidException.class, ServletRequestBindingException.class})
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 public ModelMap exceptionHandler(MethodArgumentNotValidException error)
	 {
		List<FieldError> errors = error.getBindingResult().getFieldErrors();
		ModelMap errorMapping = new ModelMap();
		int error_count =1;
		for(FieldError e : errors)
		{
			errorMapping.addAttribute("error"+error_count, e.getDefaultMessage());
			error_count++;
		}
		return errorMapping;
     }
}
