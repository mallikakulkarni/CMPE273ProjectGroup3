package stickynote;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class CreateClientObject {
	
	public DbxClient getClientObject(String code,DbxWebAuthNoRedirect webAuth,DbxRequestConfig config) throws DbxException
	{
		 DbxAuthFinish authFinish = webAuth.finish(code);
	        String accessToken = authFinish.accessToken;

	        DbxClient client = new DbxClient(config, accessToken);
			return client;
	}
}
