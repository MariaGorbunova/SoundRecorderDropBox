/*
 * Maria Gorbunova
 * 
 * */
import java.io.InputStream;
import java.io.FileInputStream;



import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;



public class Driver {
	public static void main(String[] args) {
		String ACCESS_TOKEN = "add your access token here";
		
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/SoundRecorder_YourFolder").build();
		
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		
		JavaSoundRecorder recorder = new JavaSoundRecorder(client);
		recorder.recordAudio(10000);	
		
	}

}
