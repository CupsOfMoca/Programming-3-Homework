package VN;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class VN_Audio_Manager extends Thread {
	ArrayList<VN_Audio> AUDIO_LIST;
	private volatile boolean stopper = false;
	private volatile boolean reset = false;
	VN_Audio_Manager(ArrayList<VN_Audio> AL){
		AUDIO_LIST = AL;
	}
	synchronized public void run(){
		task();
		
		
	}
	synchronized public void task() {
		while(!stopper) {
			for(int i = 0;i<AUDIO_LIST.size();i++) {
				if(AUDIO_LIST.get(i).getStatus()=="stopped"&&AUDIO_LIST.get(i).paused!=true)
					try {
						deleteTrack(AUDIO_LIST.get(i).getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			if(reset) {
				for(VN_Audio vna: AUDIO_LIST) {
					
					try {
						deleteTrack(vna.getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				reset = false;
			}
			try {
				wait(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		
		
	}
	public void addTrack(String name,boolean play,boolean loop) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		VN_Audio newtrack = new VN_Audio(name,loop);
		if(play)newtrack.AUDIOClip.start();
		else {
			newtrack.AUDIOClip.stop();
			newtrack.paused = true;
		}
		AUDIO_LIST.add(newtrack);
		
	}
	
	public void playpausetrack(boolean play, String name,boolean restart) {
		for(VN_Audio VNA: AUDIO_LIST) {
			if(VNA.getName().compareTo(name)==0) 
				if(play) {
					if(restart)VNA.AUDIOClip.setFramePosition(0);
					VNA.AUDIOClip.start();
					VNA.paused = false;
				}
				else {
					VNA.AUDIOClip.stop();
					VNA.paused = true;
				}
		}
		
	}
	
	public void deleteTrack(String name) throws IOException {
		if(!AUDIO_LIST.isEmpty()) {
			for(int i = 0; i<AUDIO_LIST.size();i++){
				
				if(AUDIO_LIST.get(i).getName().equals(name)) {
					AUDIO_LIST.get(i).AUDIOClip.stop();
					AUDIO_LIST.get(i).AUDIOClip.close();
					AUDIO_LIST.get(i).audioInputStream.close();
					AUDIO_LIST.remove(i);
				}
			}
		}
		
		
	}
	public boolean isTrackInList(String name) {
		
		for(VN_Audio VNA: AUDIO_LIST) {
			if(VNA.getName().compareTo(name)==0)return true;
			
		}
		return false;
	}
	public void stopAMNGR() {
		
		stopper = true;
		for(VN_Audio vna: AUDIO_LIST) {
			
			try {
				deleteTrack(vna.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void resetAMNGR() {
		reset = true;
		
		
	}
}
