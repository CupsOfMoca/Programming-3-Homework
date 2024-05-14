package VN;

import java.io.IOException;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;

import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;


public class VN_main extends Thread {
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public volatile static VN_Frame vn;
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException, FontFormatException, ClassNotFoundException {
		vn = new VN_Frame();
		device.setFullScreenWindow(vn);
		
		SwingUtilities.updateComponentTreeUI(vn);
		vn.setVisible(true);
		
		device.setFullScreenWindow(vn);
		sleep(500);
		vn.AUDIO_LIST.add(new VN_Audio("YuukaTitle",false));
		sleep(1000);
		
		vn.titletheme="Theme_140";
		vn.AUDIO_LIST.add(new VN_Audio(vn.titletheme, true));
		
		
	    
	}
	public VN_Frame getFrame() {return vn;}
}
