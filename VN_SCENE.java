package VN;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.*;
import java.util.*;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;



public class VN_SCENE {
	
	
	private int chapter = 1;
	private int index = 1;
	
	String lastmusic = "pause";
	ArrayList<JTextArea> Texts = new ArrayList<JTextArea>();
	private VN_TYPEWRITER TYPEWRITER_TXT;
	private VN_TYPEWRITER TYPEWRITER_TXT_SHDW;
	VN_IMAGE TXTBOX;
	VN_SCENE_MANAGER VSM;
	private boolean init = true;
	String[] line;
	String[] lastline;
	
	public VN_SCENE( int chp, int indx, VN_SCENE_MANAGER vsm ) throws UnsupportedAudioFileException, LineUnavailableException, IOException, HeadlessException, AWTException {
		VSM = vsm;
		setChapter(chp);
		setIndex(indx);
		VSM.getJLP().addMouseListener(new SceneMouseListener(VSM));
		//image =new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		
		
		readdata(chp);
		
		
		
		
		try {
			TXTBOX = new VN_IMAGE("TEXTBOX");
			TXTBOX.getLabel().setVisible(false);
			Texts.add(new JTextArea(5,1));
			
			Texts.get(0).setBorder(javax.swing.BorderFactory.createEmptyBorder());
			Texts.get(0).setFont(VSM.getFontBold().deriveFont(64f));
			Texts.get(0).setBounds(385,980,1660, 450);
			Texts.get(0).setForeground(Color.white);
			Texts.get(0).setEditable(false);
			Texts.get(0).setOpaque(false);
			
			Texts.add(new JTextArea(5,1));
			Texts.get(1).setBorder(javax.swing.BorderFactory.createEmptyBorder());
			Texts.get(1).setFont(VSM.getFontBold().deriveFont(64f));
			Texts.get(1).setBounds(390,985,1660, 450);
			Texts.get(1).setForeground(new Color(0,0,0,129));
			Texts.get(1).setEditable(false);
			Texts.get(1).setOpaque(false);
			
			Texts.add(new JTextArea(5,1));
			setTYPEWRITER_TXT(new VN_TYPEWRITER(Texts.get(2),"",VSM.textspeed,this));
			Texts.get(2).setBorder(javax.swing.BorderFactory.createEmptyBorder());
			Texts.get(2).setFont(VSM.getFont().deriveFont(42f));
			Texts.get(2).setBounds(385,1075,1800, 450);
			Texts.get(2).setForeground(Color.white);
			Texts.get(2).setEditable(false);
			Texts.get(2).setOpaque(false);
			
			
			Texts.add( new JTextArea(5,1));
			setTYPEWRITER_TXT_SHDW(new VN_TYPEWRITER(Texts.get(3),"",VSM.textspeed,this));
			Texts.get(3).setBorder(javax.swing.BorderFactory.createEmptyBorder());
			Texts.get(3).setFont(VSM.getFont().deriveFont(42f));
			Texts.get(3).setBounds(390,1075,1800, 450);
			Texts.get(3).setForeground(new Color(0,0,0,129));
			Texts.get(3).setEditable(false);
			Texts.get(3).setOpaque(false);
			
			
			
			VSM.getJLP().add(Texts.get(0));
			VSM.getJLP().add(Texts.get(1));
			VSM.getJLP().add(Texts.get(2));
			VSM.getJLP().add(Texts.get(3));
			
			VSM.getJLP().add(TXTBOX.getLabel());
			
			
			for(int i = 0; i<3;i++) {
				VSM.current_sprites.add(new VN_IMAGE("SPRITE_TEMP"));
				VSM.getJLP().add(VSM.current_sprites.get(i).getLabel());
				VSM.current_sprites.get(i).getLabel().setVisible(false);
				
			}
			VSM.current_BG =new VN_IMAGE("SPRITE_TEMP");
			VSM.getJLP().add(VSM.current_BG .getLabel());
			
			
			
			VSM.getJLP().revalidate();
			VSM.getJLP().repaint();
			
			line = VSM.getLines().get(getIndex());
			lastline = VSM.getLines().get(0);
		}catch( NullPointerException ex) {
			System.out.println("An error has occured, the apllication has been shut down. xD");
			
			System.exit(1);
			
		}
		
		

		
	}
	public void UPDATE(ArrayList<VN_IMAGE> sprites, JTextArea txtbox, JLayeredPane LP, Font font, Font font_bold) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException, HeadlessException, AWTException {
			
		try {
			line = VSM.getLines().get(getIndex());
			
			
			
			
			//if(index==VSM.lines.size()) VSM.loadnewscene(chapter++);
			
			if(line[9].compareTo("pause")==0&&lastmusic.compareTo("pause")!=0)VSM.frame.AUDIO_MNGR.playpausetrack(false, lastmusic,true);
			else {
				if(lastmusic.compareTo(line[9])!=0) {
					VSM.frame.AUDIO_MNGR.playpausetrack(false, lastmusic,true);
					
					VSM.frame.AUDIO_MNGR.playpausetrack(true,line[9],true);
					
					lastmusic = line[9];
				}
				else VSM.frame.AUDIO_MNGR.playpausetrack(true,line[9],false);
			}
			
			
			
			Texts.get(0).setText(line[8]);
			Texts.get(1).setText(line[8]);
			
			Texts.get(2).setFont(font.deriveFont((float)Integer.parseInt(line[7])));
			Texts.get(3).setFont(font.deriveFont((float)Integer.parseInt(line[7])));
			
			if(init ) {
				
				TXTBOX.getLabel().setVisible(true);
				getTYPEWRITER_TXT().start();
				getTYPEWRITER_TXT_SHDW().start();
				init = false;
			}
			
			getTYPEWRITER_TXT().update_stats(line[2], VSM.textspeed);
			getTYPEWRITER_TXT_SHDW().update_stats(line[2], VSM.textspeed);
			
			
			
			String[] sprite_names = line[3].split(",");
			String[] sprite_emotions= line[4].split(",");
			String[] sprite_movement= (line[5].split(","));
			
			String[] lastsprite_names = lastline[3].split(",");
			String[] lastsprite_emotions= lastline[4].split(",");
			String[] lastsprite_movement= lastline[5].split(",");
			
			
			for(int i = 0; i<3;i++) {
				
				if(sprite_names[i]=="N/A") VSM.current_sprites.get(i).getLabel().setVisible(false);
				
				else if((sprite_names[i]+"_"+sprite_emotions[i]).compareTo(lastsprite_names[i]+"_"+lastsprite_emotions[i])!=0) {
						
					VSM.current_sprites.get(i).ChangeImage(VSM.getImage(sprite_names[i]+"_"+sprite_emotions[i],VSM.getSprites()));
					
					
					
					
				}
				
				if(sprite_names[i]!="N/A"&& sprite_movement[i].compareTo(lastsprite_movement[i])!=0) {
					switch(sprite_movement[i]) {
					case "MIDSTAND":
						
						VSM.current_sprites.get(i).ResizeImage(1000,1440);
						VSM.current_sprites.get(i).getLabel().setBounds(800, 300,1000 , 1440);
						
						
						
						break;
					case "MIDZOOM":
						VSM.current_sprites.get(i).ResizeImage(2000, 2880);
						VSM.current_sprites.get(i).getLabel().setBounds(400, 50,2000 , 2880);
						
						
						
						break;
					
					case "LEFTSTAND":
						VSM.current_sprites.get(i).ResizeImage(1000,1440);
						VSM.current_sprites.get(i).getLabel().setBounds(300, 300,1000 , 1440);
						
						
						break;
					case "RIGHTSTAND":
						VSM.current_sprites.get(i).ResizeImage(1000,1440);
						VSM.current_sprites.get(i).getLabel().setBounds(1300, 300,1000 , 1440);
						
						
						break;
					case "LEFTTHINK":
						VSM.current_sprites.get(i).ResizeImage(1000,1440);
						VSM.current_sprites.get(i).getLabel().setBounds(300, 300,1000 , 1440);
						
						
						VSM.VSE.notification(sprite_movement[i]);
						
						break;	
					case "RIGHTTHINK":
						VSM.current_sprites.get(i).ResizeImage(1000,1440);
						VSM.current_sprites.get(i).getLabel().setBounds(1300, 300,1000 , 1440);
						
						
						VSM.VSE.notification(sprite_movement[i]);
						
						break;
					case "MIDZOOMTHINK":
						VSM.current_sprites.get(i).ResizeImage(2000, 2880);
						VSM.current_sprites.get(i).getLabel().setBounds(400, 50,2000 , 2880);
						
						
						VSM.VSE.notification(sprite_movement[i]);
						
						break;	
					}
					LP.revalidate();
					LP.repaint();
					SwingUtilities.updateComponentTreeUI(VSM.frame);
					if(VSM.current_sprites.get(i).getLabel().isVisible()==false) VSM.current_sprites.get(i).getLabel().setVisible(true);
				}
			}
			
			if(VSM.current_BG.getName()!=line[6]) VSM.current_BG.ChangeImage(VSM.getImage(line[6],VSM.BG));
			if(line[10].compareTo("N/A")!=0) {
				synchronized(VSM.frame.AUDIO_MNGR) {
					VSM.frame.AUDIO_MNGR.addTrack(line[10], true, false);
				}
				
			}
			
			
			
			
			LP.revalidate();
			LP.repaint();
			lastline = line;
			
		}
			
			
			
		catch(IOException | NullPointerException ex) {
			System.out.println("An error has occured, the apllication has been shut down. xd");
			
			System.exit(1);
		
		}
		
	
	}
	public void readdata(int chp) throws UnsupportedAudioFileException, LineUnavailableException, IOException  {
		
		
		
		VSM.getLines().removeAll(VSM.getLines());
		
		
		String line;
		VSM.loading = new VN_IMAGE("loading");
		VSM.getJLP().add(VSM.loading.getLabel());
		
		boolean stopreading = false;
		int nmbrofsprts = -1;
		try {
			while((line =VSM.reader.readLine())!=null&&stopreading != true) {
				
				String[] splitline =  line.split("§");
				
				
				
				if((Integer.parseInt(splitline[0])==chp)){
					String[] sprite_names = splitline[3].split(",");
					String[] sprite_emotions= splitline[4].split(",");
					
					
					VSM.getLines().add(splitline);
					
					for(int i = 0; i<3;i++) {
						
						if(sprite_names[i]!="N/A"&&VSM.isImageInContainer(sprite_names[i]+"_"+sprite_emotions[i],VSM.getSprites())==false) {
							
							
							VSM.getSprites().add(new VN_IMAGE(sprite_names[i]+"_"+sprite_emotions[i]));
							nmbrofsprts++;
							VSM.getSprites().get(nmbrofsprts).setOGName(sprite_names[i]);
							VSM.getSprites().get(nmbrofsprts).getLabel().setBounds(0, 0,2560 , 1440);
							
						}
					}
					if(splitline[6]!="N/A"&&VSM.isImageInContainer(splitline[6],VSM.BG)==false) VSM.BG.add(new VN_IMAGE(splitline[6]));
					if(splitline[9].compareTo("pause")!=0)if(VSM.frame.AUDIO_MNGR.isTrackInList(splitline[9])==false) {
						synchronized(VSM.frame.AUDIO_MNGR) {
							VSM.frame.AUDIO_MNGR.addTrack(splitline[9],false, true) ;
						}
					}
							
						
							
					
				}else if((Integer.parseInt(splitline[0])>=chp)) stopreading= true;
				VSM.loading.getLabel().setVisible(false);
			}
		}catch(IOException | NullPointerException ex) {
			System.out.println("An error has occured, the apllication has been shut down. Dx");
			ex.printStackTrace();
			System.exit(1);
			
		}
		
	}
	
	
	public int getChapter() {
		return chapter;
	}
	public void setChapter(int chapter) {
		this.chapter = chapter;
	}


	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}


	public VN_TYPEWRITER getTYPEWRITER_TXT() {
		return TYPEWRITER_TXT;
	}
	public void setTYPEWRITER_TXT(VN_TYPEWRITER tYPEWRITER_TXT) {
		TYPEWRITER_TXT = tYPEWRITER_TXT;
	}


	public VN_TYPEWRITER getTYPEWRITER_TXT_SHDW() {
		return TYPEWRITER_TXT_SHDW;
	}
	public void setTYPEWRITER_TXT_SHDW(VN_TYPEWRITER tYPEWRITER_TXT_SHDW) {
		TYPEWRITER_TXT_SHDW = tYPEWRITER_TXT_SHDW;
	}


	public class SceneMouseListener implements MouseListener{
		
		private VN_SCENE_MANAGER V_S_M;
		
		SceneMouseListener(VN_SCENE_MANAGER vsm){
			V_S_M = vsm;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				V_S_M.frame.AUDIO_LIST.add(new VN_Audio("UI_bubble",false));
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(V_S_M.getScene().getTYPEWRITER_TXT().getState()!= Thread.State.WAITING&&V_S_M.getScene().getTYPEWRITER_TXT_SHDW().getState()!= Thread.State.WAITING&&V_S_M.getState()!=Thread.State.TIMED_WAITING) {
				V_S_M.getScene().getTYPEWRITER_TXT().completeLine();
				V_S_M.getScene().getTYPEWRITER_TXT_SHDW().completeLine();
				
				
			}else {
				if(V_S_M.getState()==Thread.State.WAITING &&V_S_M.getScene().getTYPEWRITER_TXT().getState()==Thread.State.WAITING&&V_S_M.getScene().getTYPEWRITER_TXT_SHDW().getState()==Thread.State.WAITING&&V_S_M.VSE.getState()==Thread.State.WAITING&&V_S_M.VSE.getState()!=Thread.State.TIMED_WAITING) {
					if(getIndex()<V_S_M.getLines().size()-1) {
						setIndex(getIndex() + 1);
					}
					if(getIndex()<V_S_M.getLines().size()) {
						synchronized(V_S_M) {
							V_S_M.notify();
						}
						
						synchronized(V_S_M.getScene().getTYPEWRITER_TXT()) {
							V_S_M.getScene().getTYPEWRITER_TXT().notify();
						}
						synchronized(V_S_M.getScene().getTYPEWRITER_TXT_SHDW()) {
							V_S_M.getScene().getTYPEWRITER_TXT_SHDW().notify();
						}
						V_S_M.getScene().getTYPEWRITER_TXT().setPause(false);
						V_S_M.getScene().getTYPEWRITER_TXT_SHDW().setPause(false);
						V_S_M.setPause(false);
					}
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	
	
	
	
}
