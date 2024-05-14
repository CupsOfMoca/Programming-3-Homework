package VN;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class VN_Audio  {
	
	Long currentFrame;
	Clip AUDIOClip ;
	
	
	AudioInputStream audioInputStream;
	String name;
		
	final static String filePath = "RESOURCES/AUDIO/";
	
	boolean paused = false;;
	
	public VN_Audio(String name, boolean loop)throws UnsupportedAudioFileException,IOException, LineUnavailableException{
		try {
			this.name = name;
			audioInputStream = AudioSystem.getAudioInputStream(new File(filePath + name+".wav").getAbsoluteFile());
			AUDIOClip = AudioSystem.getClip();
			AUDIOClip.open(audioInputStream);
			if(loop)AUDIOClip.loop(Clip.LOOP_CONTINUOUSLY);else AUDIOClip.loop(0);
			
			FloatControl gainControl = (FloatControl) AUDIOClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f* (float) Math.log10(0.1f));
		}catch(FileNotFoundException e) {
			//ez a feltoltes miatt van hogy minden hangfajl a clickel legyen kicserelve
			this.name = name;
			audioInputStream = AudioSystem.getAudioInputStream(new File(filePath + "BUTTON_CLICK"+".wav").getAbsoluteFile());
			AUDIOClip = AudioSystem.getClip();
			AUDIOClip.open(audioInputStream);
			if(loop)AUDIOClip.loop(Clip.LOOP_CONTINUOUSLY);else AUDIOClip.loop(0);
			
			FloatControl gainControl = (FloatControl) AUDIOClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f* (float) Math.log10(0.1f));
		}
		
		
	}
	String getStatus() {
		if(AUDIOClip.isRunning()) return "running";
		else return "stopped";
	}
	String getName() {
		return name;
	}
}
