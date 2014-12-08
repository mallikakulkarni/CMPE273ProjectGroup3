package stickynote;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Controller
@Configuration
@ComponentScan
@EnableAutoConfiguration
@RequestMapping("/stickynote")
public class Application {
	 
	 DBCollection coll; 
	 BasicDBObject doc;
	 DBCursor dbcursor;
	 
	 final String APP_KEY = "mwa414ncuw5f56p";
	 final String APP_SECRET = "23w343owiztqqwb";

	 DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

	 DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
	 Locale.getDefault().toString());
	 DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
	 String authorizeUrl = webAuth.start();
	 DbxClient client;
	 HashMap<String,Object> clientInfo = new HashMap<String, Object>();
	    
	 public static void main(String[] args) 
	 {
		//new DBConnection();
		 System.out.println("in main");
   	    SpringApplication.run(Application.class, args);
	 }
	  
	 //welcome message
	 @RequestMapping(value = "", method = RequestMethod.GET)
	 
	 public String welcomeMessage()
	 {
	   	System.out.println("Welcome to Sticky notes");
	  // model.addAttribute("createuser",new CreateUser());
	   
		
	   	return "index";
	 }
	 
	 
 @RequestMapping(value = "/register", method = RequestMethod.GET)
	 
	 public String registerForm(Model model)
	 {
	   	System.out.println("In register method");
	   model.addAttribute("createuser",new CreateUser());
	
	   	return "register";
	 }
	 
//	 @RequestMapping(value = "/postm", method = RequestMethod.POST)
//	 
//	 public CreateUser welcomepost(@ModelAttribute CreateUser createuser, Model model)
//	 {
//	   	System.out.println("post connected");
//	   	model.addAttribute("createuser",createuser);
//	   	System.out.println(createuser.getEmail());
//	   
//	  return createuser;
//	 }
	 
	 
	 
	 //Creating user
	 
 @RequestMapping(value= "/users", method = RequestMethod.POST)
 public String createUser(@Valid @ModelAttribute CreateUser createuser,Model model) throws UnknownHostException
	 {
		 System.out.println("in post");
		 model.addAttribute("createuser",createuser);
		 coll =  DBConnection.getConnection();
		 BasicDBObject query = new BasicDBObject("email", createuser.getEmail()); // for eamil already exist validation
		 DBCursor cursor = coll.find(query);
		 try {
			 	if(cursor.hasNext())
			 	{	System.out.println(createuser.getEmail());
			 		return "get";
			 	}else{    		
				 doc = new BasicDBObject("userid", createuser.getUserid()).append("email", createuser.getEmail()).append("password", createuser.getPassword()).append("created_at", createuser.getCreated_at()).append("updated_at", createuser.getUpdated_at());
				 coll.insert(doc);
				 System.out.println("inserted");
				 return "get";
			 	}
			 }
		 finally {cursor.close();}  	
	  }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

	// Creating user
//	 @RequestMapping(value= "/users", method = RequestMethod.POST)
//	 @ResponseBody
//	 public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUser user) throws UnknownHostException
//	 {
//		 System.out.println("in post");
//		 coll =  DBConnection.getConnection();
//		 BasicDBObject query = new BasicDBObject("email", user.getEmail()); // for eamil already exist validation
//		 DBCursor cursor = coll.find(query);
//		 try {
//			 	if(cursor.hasNext())
//			 	{	return new ResponseEntity<Object>(new Error(user.getEmail()),HttpStatus.BAD_REQUEST);
//			 	}else{    		
//				 doc = new BasicDBObject("userid", user.getUserid()).append("email", user.getEmail()).append("password", user.getPassword()).append("created_at", user.getCreated_at()).append("updated_at", user.getUpdated_at());
//				 coll.insert(doc);
//				 return new ResponseEntity<Object>(user, HttpStatus.CREATED);
//			 	}
//			 }
//		 finally {cursor.close();}  	
//	  }
	  
	 //get user details
//	 @RequestMapping(value ="/users/{userid}", method = RequestMethod.GET)
//	 @ResponseBody
//	 public ResponseEntity<Object> getUser(@PathVariable String userid) throws UnknownHostException
//	 {
//	    coll =  DBConnection.getConnection();
//	    BasicDBObject query = new BasicDBObject("userid", userid);
//	    DBCursor cursor = coll.find(query);
//	    try {
//	    		if(cursor.hasNext())
//	    		{	GetUser getUser = new GetUser(cursor);
//	    			return new ResponseEntity<Object>(getUser, HttpStatus.OK);
//	    		}
//	    		else{
//	    		return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
//	    		}
//	    	}
//	    finally{cursor.close();}
//	 }
	        

    @RequestMapping(value="/users/{email}/settings", method = RequestMethod.GET)
    public String getSettings(@PathVariable String email,Model model) throws UnknownHostException
    {
        coll =  DBConnection.getConnection();
        BasicDBObject query = new BasicDBObject("email", email);
        System.out.println("email entered is "+email);
        DBCursor cursor = coll.find(query);
        try {
            if (cursor.hasNext()) {
                GetUser getUser = new GetUser(cursor);
                model.addAttribute("getUser", getUser);
                return "settings";
            } else {
                return "No such user";
            }
        }
        finally{cursor.close();}
    }
	 
	 @RequestMapping(value ="/users/homepage", method = RequestMethod.GET)
	 public String getUser(@RequestParam String useremail,Model model) throws UnknownHostException
	 {
	    coll =  DBConnection.getConnection();
	    BasicDBObject query = new BasicDBObject("email", useremail);
	    System.out.println("email entered is "+useremail);
	    DBCursor cursor = coll.find(query);
         System.out.println(cursor);
         System.out.println(query);
	    try {
	    		if(cursor.hasNext())
	    		{	GetUser getUser = new GetUser(cursor);
	    			model.addAttribute("getUser", getUser);
	    			return "homepage";
	    		}
	    		else{
	    		return "nosuchuser";
	    		}
	    	}
	    finally{cursor.close();}
	 }



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
	    		 if(!(createNote.getAuthorizationCode().equals("")))
	    		 {
	    			 if(!(clientInfo.containsKey(userid)))
	    			 {
	    				 CreateClientObject cc = new CreateClientObject();
	    			        client = cc.getClientObject(createNote.getAuthorizationCode(), webAuth, config);
	    			        clientInfo.put(userid, client);
	    			        System.out.println("Linked account: " + client.getAccountInfo().displayName);
	    			 }
	    			 createNote.setUserid(userid);
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
	    		 else
	    		 {
	    			return new ResponseEntity<Object>(new Error(authorizeUrl,2), HttpStatus.OK);
	    		 }
	    		}else
	    		{
	    			return new ResponseEntity<Object>(new Error(userid), HttpStatus.BAD_REQUEST);
	    		}
	    	}
	    finally{cursor.close();}
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
