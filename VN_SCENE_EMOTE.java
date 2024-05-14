package VN;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLayeredPane;
/*
 0-2 dot dot dot
 3-SpeechBubble

 4-
 */
public class VN_SCENE_EMOTE extends Thread {
	 ArrayList<VN_IMAGE> EMOTE_IMG_LIST = new ArrayList<VN_IMAGE>();
	 JLayeredPane LP;
	 String argument = "NOTHING";
	 VN_SCENE_MANAGER VSM;
	 VN_SCENE_EMOTE(JLayeredPane JLP,VN_SCENE_MANAGER vsm) throws IOException{
		 LP = JLP;
		 VSM = vsm;
		 for(int i = 0;i<3; i++) EMOTE_IMG_LIST.add(new VN_IMAGE("Emoticon_Idea"));
		 EMOTE_IMG_LIST.add(new VN_IMAGE("Emoticon_Balloon_N"));
		 for(int i = 0; i<EMOTE_IMG_LIST.size();i++) {
			 EMOTE_IMG_LIST.get(i).getLabel().setVisible(false);
			 LP.add(EMOTE_IMG_LIST.get(i).getLabel());
		 }
	 }
	 public void run() {
		 try {
			task();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 synchronized public void task() throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		 while (true) {
			 switch(argument) {
			 case "RIGHTTHINK":
				 
				 EMOTE_IMG_LIST.get(3).getLabel().setBounds(1405,275,255 , 198);
				 EMOTE_IMG_LIST.get(3).getLabel().setVisible(true);
				
				 sleep(100);
				
				 for(int i = 0;i<3; i++) {
					 EMOTE_IMG_LIST.get(i).getLabel().setBounds(1420+i*65, 350,39 , 39);
					 EMOTE_IMG_LIST.get(i).getLabel().setVisible(true);
					 
					 VSM.frame.AUDIO_LIST.add(new VN_Audio("UI_Bubble",false));
					 sleep(400);
						
				 }
				 for(int i = 0; i<4;i++) EMOTE_IMG_LIST.get(i).getLabel().setVisible(false);
				 wait();
				 
				 break;
			
			 	case "LEFTTHINK":
				 
				 EMOTE_IMG_LIST.get(3).getLabel().setBounds(305,275,255 , 198);
				 EMOTE_IMG_LIST.get(3).getLabel().setVisible(true);
				
				 sleep(100);
				
				 for(int i = 0;i<3; i++) {
					 EMOTE_IMG_LIST.get(i).getLabel().setBounds(350+i*65, 350,39 , 39);
					 EMOTE_IMG_LIST.get(i).getLabel().setVisible(true);
					 VSM.frame.AUDIO_LIST.add(new VN_Audio("UI_Bubble",false));
					 sleep(400);
						
				 }
				 for(int i = 0; i<4;i++) EMOTE_IMG_LIST.get(i).getLabel().setVisible(false);
				 wait();
				 
				 break;
			 	case "MIDZOOMTHINK":
					 
					 EMOTE_IMG_LIST.get(3).getLabel().setBounds(875,275,255 , 198);
					 EMOTE_IMG_LIST.get(3).getLabel().setVisible(true);
					
					 sleep(100);
					
					 for(int i = 0;i<3; i++) {
						 EMOTE_IMG_LIST.get(i).getLabel().setBounds(925+i*65, 350,39 , 39);
						 EMOTE_IMG_LIST.get(i).getLabel().setVisible(true);
						 VSM.frame.AUDIO_LIST.add(new VN_Audio("UI_Bubble",false));
						 sleep(400);
							
					 }
					 for(int i = 0; i<4;i++) EMOTE_IMG_LIST.get(i).getLabel().setVisible(false);
					 wait();
					 
					 break;
			 case "NOTHING":
				 
				 wait();
				                      
				 break;
			 }
			 
		 }
	 }
	 synchronized public void notification(String s) {
		 argument = s;
		 notify();
	 }
}
