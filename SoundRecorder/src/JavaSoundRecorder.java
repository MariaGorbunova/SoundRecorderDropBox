import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;

import com.dropbox.core.v2.DbxClientV2;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
 
/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class JavaSoundRecorder {
    // record duration, in milliseconds
    //static final long RECORD_TIME = 60000;  // 1 minute
 
    private DbxClientV2 client;
    // path of the wav file
    //File wavFile = new File("/RecordAudio.wav");
 
    // format of audio file
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private AudioFormat format;
    
    private DataLine.Info info;
    // the line from which audio data is captured
    private TargetDataLine line;
    
    public JavaSoundRecorder() {
		// TODO Auto-generated constructor stub
	} 
    
    
    /**
     * Defines an audio format
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }
 

 
    public JavaSoundRecorder(DbxClientV2 client) {
    	this.client = client;	
    	//trying various format depending on your mic
    	//format = new AudioFormat(8000, 8, 1, true, false);
//    	AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,  //     the audio encoding technique
//    	          48000.0F,// sampleRate           the number of samples per second
//    	          16,      // sampleSizeInBits the number of bits in each sample
//    	          2,       // channels           the number of channels (1 for mono, 2 for stereo, and so on)
//    	          4,       // frameSize           the number of bytes in each frame
//    	          48000.0F,// frameRate          the number of frames per second
//    	          false);  // bigEndian          indicates whether the data for a single sample is stored in big-endian byte order (<code>false</code> means little-endian) 
//    	
    	
    	format = getAudioFormat();
        info = new DataLine.Info(TargetDataLine.class, format);

    }

	public void recordAudio(int recordtime) {
    	//different name simpledateformat
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = dateformat.format(new Date());
		
		String filePath ="/Users/maria/eclipse-workspace/SoundRecorder/"+date+".wav";
		File file = new File(filePath);
		start(file);
		stop(file,recordtime);
    }
	
	
    /**
     * Captures the sound and record into a WAV file
     */
    void start(File file) {

		Thread thread = new Thread () {
    		public void run() {
    			try {
    	            // checks if system supports the data line
    	            if (!AudioSystem.isLineSupported(info)) {
    	                System.out.println("Line not supported");
    	                System.exit(0);
    	            }
    	            line = (TargetDataLine) AudioSystem.getLine(info);
    	            line.open(format);
    	            line.start();   // start capturing
    	 
    	            AudioInputStream ais = new AudioInputStream(line);
    	 ////////
    	            /*
    	            AudioFormat af = ais.getFormat();

    	            if( (!af.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) &&
    	                (!af.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED))) {

    	                try {
    	                    AudioFormat newFormat =
    	                        new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,
    	                                         af.getSampleRate(),
    	                                         16,
    	                                         af.getChannels(),
    	                                         af.getChannels() * 2,
    	                                         af.getSampleRate(),
    	                                         true);
    	                    ais = AudioSystem.getAudioInputStream(newFormat, ais);
    	                } catch (Exception e) {
    	                    e.printStackTrace();
    	                    ais = null;
    	                }
    	            }     
    	            
    	 */
    	 //////////
    	            System.out.println("Start recording...");
    	 
    	            // start recording
    	            AudioSystem.write(ais, fileType, file);
    	 
    	        } catch (Exception ex) {
    	            ex.printStackTrace();
    	        }
    		}
    	};
    	thread.start();
        
    }
    
    
    private void stop(File file,  int recordtime) {
    	
    	Thread thread  = new Thread() {
    		public void run() {
    			try {
    				sleep(recordtime);
    				line.stop();
    				line.close();
    				System.out.println("Finished");
    				//recordAudio(recordtime);
    				System.out.println("/"+file.getName());
    				
    				InputStream in = new FileInputStream(file.getName());	
    				client.files().uploadBuilder("/"+file.getName()).uploadAndFinish(in);	
    				
    				//in.close();
    				file.delete();
    				
    				
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	};
    	thread.start();
    } 
   
}