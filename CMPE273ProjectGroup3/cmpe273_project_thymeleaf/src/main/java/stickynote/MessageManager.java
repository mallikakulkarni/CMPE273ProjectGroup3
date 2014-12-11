package stickynote;

// Install the Java helper library from twilio.com/docs/java/install
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
 
public class MessageManager { 
 
  // Find your Account Sid and Token at twilio.com/user/account
/*  public static final String ACCOUNT_SID = "ACCOUNT_SID";
  public static final String AUTH_TOKEN = "AUTH_TOKEN";
 
  public static void main(String[] args) throws TwilioRestException {
    TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
   

 
 // Build a filter for the SmsList
    
  
    
   List<NameValuePair> params = new ArrayList<NameValuePair>();
   params.add(new BasicNameValuePair("Body", "Hi!"));
    params.add(new BasicNameValuePair("To", "+1"));
    params.add(new BasicNameValuePair("From", "+1"));
     
    
    
    SmsFactory smsFactory = client.getAccount().getSmsFactory();
    com.twilio.sdk.resource.instance.Sms sms = smsFactory.create(params);
    System.out.println(sms.getSid());
     
  }*/
  
  public static boolean sendMessage(String title) {
	  
	  String ACCOUNT_SID = "ACff81294b57103fc0703840b437b00366";
	  String AUTH_TOKEN = "6362fda5310d6b95be6bb848a3b5ecc9";
	  
	  TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
	   

	  
	  // Build a filter for the SmsList
	     
	   
	     
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("Body", title));
	     params.add(new BasicNameValuePair("To", "+14157128116"));
	     params.add(new BasicNameValuePair("From", "+14088053287"));
	      
	     
	     
	     SmsFactory smsFactory = client.getAccount().getSmsFactory();
	     try {
			com.twilio.sdk.resource.instance.Sms sms = smsFactory.create(params);
		} catch (TwilioRestException e) {
			
			e.printStackTrace();
			return false;
		}
	     return true;
  }
  
  public static boolean sendEmail(String title) {
		{
			// Recipient's email ID needs to be mentioned.
			String to = "To@gmail.com";

			// Sender's email ID needs to be mentioned
			String from = "From@gmail.com";

			// Assuming you are sending email from localhost
			String host = "localhost";

			// Get system properties
			Properties properties = System.getProperties();

			// Setup mail server
			properties.setProperty("mail.smtp.host", host);

			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);

			try {
				// Create a default MimeMessage object.
				MimeMessage message = new MimeMessage(session);

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));

				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(to));

				// Set Subject: header field
				message.setSubject(title);

				// Now set the actual message
				message.setText("MESSAGE BODY HERE:This is actual message");

				// Send message
				Transport.send(message);
				System.out.println("Sent message successfully....");
			} catch (MessagingException mex) {
				mex.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
