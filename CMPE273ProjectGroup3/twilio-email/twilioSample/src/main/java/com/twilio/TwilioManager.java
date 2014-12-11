package com.twilio;

// Install the Java helper library from twilio.com/docs/java/install
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.MessageFactory;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Notification;
import com.twilio.sdk.verbs.Sms;
import com.twilio.sdk.verbs.TwiMLException;
 
public class TwilioManager { 
 
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "ACff81294b57103fc0703840b437b00366";
  public static final String AUTH_TOKEN = "6362fda5310d6b95be6bb848a3b5ecc9";
 
  public static void main(String[] args) throws TwilioRestException {
    TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
   

 
 // Build a filter for the SmsList
    
  
    
   List<NameValuePair> params = new ArrayList<NameValuePair>();
   params.add(new BasicNameValuePair("Body", "Hi!"));
    params.add(new BasicNameValuePair("To", "+14157128116"));
    params.add(new BasicNameValuePair("From", "+14088053287"));
     
    
    
    SmsFactory smsFactory = client.getAccount().getSmsFactory();
    com.twilio.sdk.resource.instance.Sms sms = smsFactory.create(params);
    System.out.println(sms.getSid());
     
  }
}
