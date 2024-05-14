package VN;



import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.HeadlessException;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import VN.VN_OPTIONS.SETTINGS_STATE;


public class VN_SCENE_MANAGER extends Thread {
	final static String filepath = "RESOURCES/SYSTEM/DATA.txt";
	VN_Frame frame;
	BufferedReader reader;
	private volatile VN_SCENE scene;
	private JLayeredPane JLP;
	
	private ArrayList<VN_IMAGE> sprites = new ArrayList<VN_IMAGE>();
	ArrayList<VN_IMAGE> current_sprites = new ArrayList<VN_IMAGE>();
	ArrayList<VN_IMAGE> BG = new ArrayList<VN_IMAGE>();
	private ArrayList<String[]> lines = new ArrayList<String[]>();
	
	VN_IMAGE loading;
	private JTextArea txtbox;
	VN_IMAGE current_BG ;
	volatile ArrayList<VN_IMAGE> BUTTONS = new ArrayList<VN_IMAGE>();
	VN_SCENE_EMOTE VSE ;
	
	private Font MOTOYAI;
	private Font MOTOYAI_BOLD;
	volatile boolean auto = false;
	private boolean pause = false;
	volatile boolean stopper = false;
	int strtchapter;
	int strtindex;
	int textspeed=30;
	int autospeed=0;
	public VN_SCENE_MANAGER(VN_Frame VNf,Component comp,int chpt, int indx ) throws FontFormatException, IOException{
		frame = VNf;
		setJLP((JLayeredPane) comp);
		getJLP().setPreferredSize(new Dimension(2560,1440));
		loadButtons()
;		reader = new BufferedReader(new FileReader(new File(filepath)));
		VSE = new VN_SCENE_EMOTE(getJLP(), this);
		
		VSE.start();
		try {
			
			MOTOYAI = Font.createFont(Font.TRUETYPE_FONT, new File("RESOURCES/SYSTEM/MainFont.ttf"));
		    MOTOYAI_BOLD = Font.createFont(Font.TRUETYPE_FONT, new File("RESOURCES/SYSTEM/MainFont_Bold.ttf"));
		} catch (IOException|FontFormatException e) {}
		getJLP().setVisible(false);
		strtchapter = chpt;
		strtindex = indx;
		
		if(strtindex==0) {
			try {
				setScene(new VN_SCENE( strtchapter, 0, this));
			} catch (HeadlessException | UnsupportedAudioFileException | LineUnavailableException | IOException
					| AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				setScene(new VN_SCENE( strtchapter, 0, this));
			} catch (HeadlessException | UnsupportedAudioFileException | LineUnavailableException | IOException
					| AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			getScene().setIndex(strtindex);
			
		}
		
	}
	synchronized public void run() {
		try {
			task();
		} catch (InterruptedException | UnsupportedAudioFileException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	synchronized public void task() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException, HeadlessException, AWTException {
		if(!isPause()) {
			textspeed = Integer.parseInt(frame.getOPTIONS_MENU().VOSet.SETTINGS_DATA_LIST.get(0).getText());
			autospeed = Integer.parseInt(frame.getOPTIONS_MENU().VOSet.SETTINGS_DATA_LIST.get(1).getText());
			getJLP().setVisible(true);
			
			try {
				getScene().UPDATE(current_sprites, getTxtbox(),  getJLP(), MOTOYAI_BOLD, MOTOYAI);
			} catch (HeadlessException | IOException | InterruptedException | UnsupportedAudioFileException
					| LineUnavailableException | AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			frame.revalidate();
			frame.repaint();
			reader.close();
			try {
				wait(2000);
				
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			while(!stopper) {
				
				if(isPause() == true) {
					if(auto == true) {
						textspeed = Integer.parseInt(frame.getOPTIONS_MENU().VOSet.SETTINGS_DATA_LIST.get(0).getText());
						autospeed = Integer.parseInt(frame.getOPTIONS_MENU().VOSet.SETTINGS_DATA_LIST.get(1).getText());
						getScene().UPDATE(current_sprites, getTxtbox(),  getJLP(), MOTOYAI_BOLD, MOTOYAI);
						
						
						SwingUtilities.updateComponentTreeUI(frame);
						sleep(500);
						try {
							wait(autospeed);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(getScene().getIndex()<getLines().size()-1) {
							getScene().setIndex(getScene().getIndex() + 1);
						}
						if(getScene().getIndex()<getLines().size()) {
							synchronized(getScene().getTYPEWRITER_TXT()) {
								getScene().getTYPEWRITER_TXT().notify();
							}
							synchronized(getScene().getTYPEWRITER_TXT_SHDW()) {
								getScene().getTYPEWRITER_TXT_SHDW().notify();
							}
							getScene().getTYPEWRITER_TXT().setPause(false);
							getScene().getTYPEWRITER_TXT_SHDW().setPause(false);
							setPause(false);
						}
						
					}else {
						textspeed = Integer.parseInt(frame.getOPTIONS_MENU().VOSet.SETTINGS_DATA_LIST.get(0).getText());
						autospeed = Integer.parseInt(frame.getOPTIONS_MENU().VOSet.SETTINGS_DATA_LIST.get(1).getText());
						getScene().UPDATE(current_sprites, getTxtbox(),  getJLP(), MOTOYAI_BOLD, MOTOYAI);
						SwingUtilities.updateComponentTreeUI(frame);
						sleep(500);
						try {
							wait();
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				else {
					setPause(true);
				}
				
				
			}
			
		}	
	}
	public Font getFont() {return MOTOYAI;}
	public Font getFontBold() {return MOTOYAI_BOLD;}
	public VN_Frame getFrame() {return frame;}
	public void setFrame(VN_Frame n) {frame = n;}
	synchronized void setAuto(boolean b) {auto = b;}
	synchronized boolean getAuto() {return auto;}
	public void loadnewscene(int chpt) throws UnsupportedAudioFileException, LineUnavailableException, IOException, HeadlessException, AWTException {
		setScene(new VN_SCENE(chpt,1,this));
	}
	
	public boolean isImageInContainer(String name, ArrayList<VN_IMAGE>container) {
		for(VN_IMAGE VNIMG: container) {
			if(VNIMG.getName().compareTo(name)==0)return true;
		}
		return false;
	}
	
	public VN_IMAGE getImage(String name,ArrayList<VN_IMAGE>container) throws IOException {
		for(VN_IMAGE VNI:container) {
			if(VNI.getName().compareTo(name)==0) {
				return VNI;
			}
			
		}
		return new VN_IMAGE("SPRITE_TEMP");
	}
	synchronized public void ChangeScene(VN_OPTIONS_SAVEFRAME VOS) throws IOException {
		
		getJLP().removeAll();
		
		
		try {
			frame.AUDIO_MNGR.deleteTrack(getLines().get(getScene().getIndex())[9]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		getSprites().removeAll(getSprites());
		current_sprites.removeAll(current_sprites);
		BG.removeAll(BG);
		getLines().removeAll(getLines());
		
		for(VN_IMAGE VNI: BUTTONS) {
			getJLP().add(VNI.getLabel());
		}
		reader = new BufferedReader(new FileReader(new File(filepath)));
		
		try {
			setScene(new VN_SCENE(VOS.chapter, 0	, this));
		} catch (HeadlessException | UnsupportedAudioFileException | LineUnavailableException | IOException
				| AWTException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
		getScene().setIndex(VOS.index);
		
		try {
			getScene().UPDATE(current_sprites, getTxtbox(),  getJLP(), MOTOYAI_BOLD, MOTOYAI);
		} catch (HeadlessException | IOException | InterruptedException | UnsupportedAudioFileException
				| LineUnavailableException | AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SwingUtilities.updateComponentTreeUI(frame);
			
		
	}
	synchronized public void ChangeSceneQuick() {
		getJLP().removeAll();
		
		
		try {
			frame.AUDIO_MNGR.deleteTrack(getLines().get(getScene().getIndex())[9]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		getSprites().removeAll(getSprites());
		current_sprites.removeAll(current_sprites);
		BG.removeAll(BG);
		getLines().removeAll(getLines());
		
		for(VN_IMAGE VNI: BUTTONS) {
			getJLP().add(VNI.getLabel());
		}
		try {
			reader = new BufferedReader(new FileReader(new File(filepath)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			setScene(new VN_SCENE(frame.getOPTIONS_MENU().getQuickSave()[0], 0	, this));
		} catch (HeadlessException | UnsupportedAudioFileException | LineUnavailableException | IOException
				| AWTException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
		getScene().setIndex(frame.getOPTIONS_MENU().getQuickSave()[1]);
		
		try {
			getScene().UPDATE(current_sprites, getTxtbox(),  getJLP(), MOTOYAI_BOLD, MOTOYAI);
		} catch (HeadlessException | IOException | InterruptedException | UnsupportedAudioFileException
				| LineUnavailableException | AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SwingUtilities.updateComponentTreeUI(frame);
	}
	
	public void loadButtons() throws IOException {
		final File folder = new File("RESOURCES/IMAGES/Texture2D/");
	    for (File fileEntry : folder.listFiles()) {
	    	String[] name = fileEntry.getName().split("_");
	    	String used_name = fileEntry.getName().split("\\.")[0];
	        if(name[0].equals("SCENE")){
	        	
	        	VN_IMAGE newButton = new VN_IMAGE(used_name);
	        	
	        	if(name[3].split("\\.")[0].equals("HOVER"))newButton.getLabel().setVisible(false);
	        	
	        	switch(name[2]) {
	        		case "QSAVE":
	        			newButton.getLabel().setBounds(1100,1370,124,59);
	        			break;
	        		case "QLOAD":
	        			newButton.getLabel().setBounds(1260,1370,124,59);
	        			break;
	        		case "SETTINGS":
	        			newButton.getLabel().setBounds(1420,1370,124,59);
	        			break;
	        		case "AUTO":
	        			newButton.getLabel().setBounds(940,1370,124,59);
	        			break;
	        		default:
	        			break;
	        			
	        	}
	        	newButton.getLabel().addMouseListener(new SCENE_BUTTON_LISTENER(newButton,this));
	        	getJLP().add(newButton.getLabel());
	        	BUTTONS.add(newButton);
	        	
	        }
	    }
	}
	public class SCENE_BUTTON_LISTENER implements MouseListener{
		VN_IMAGE VNI;
		String[] name;
		String target;
		VN_SCENE_MANAGER VSM;
		SCENE_BUTTON_LISTENER(VN_IMAGE vni,VN_SCENE_MANAGER vsm){
			VNI = vni;
			VSM = vsm;
			name = vni.getName().split("_");
			if(name[3].equals("NORMAL")) target = name[0]+"_"+name[1]+"_"+name[2]+"_HOVER";
			else target = name[0]+"_"+name[1]+"_"+name[2]+"_NORMAL";
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
				
				
				if(name[3].equals("NORMAL")) {
					try {
						
						frame.AUDIO_MNGR.addTrack("BUTTON_HOVER", true, false);
						
						
					} catch (UnsupportedAudioFileException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (LineUnavailableException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						VNI.getLabel().setVisible(false);
						getImage(target,BUTTONS).getLabel().setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			if(!(VSM.getAuto() && name[2].equals("AUTO"))) {
				if(name[3].equals("HOVER")) {
			
					try {
						VNI.getLabel().setVisible(false);
						getImage(target,BUTTONS).getLabel().setVisible(true);
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				
			}
			
				
				
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				
				frame.AUDIO_MNGR.addTrack("BUTTON_CLICK", true, false);
				
				
			} catch (UnsupportedAudioFileException|IOException|LineUnavailableException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} 	
			switch(name[2]) {
			case "SETTINGS":
				auto = false;
				try {
					frame.screenshot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					
					
				} catch (HeadlessException | AWTException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				
				try {
					frame.getOPTIONS_MENU().setState(SETTINGS_STATE.LOAD);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.add(frame.PANE_LIST.get(2));
				
				frame.remove(frame.PANE_LIST.get(1));
				SwingUtilities.updateComponentTreeUI(frame);
				break;
			
			case "AUTO":
				
				VNI.getLabel().setVisible(true);
				synchronized(VSM) {
					if(VSM.getAuto()== true) {
						VSM.setAuto( false);
					}else {
						VSM.setAuto( true);
					}
				}
				
				
				
				
				
				break;
			case "QSAVE":
				synchronized(this) {
					frame.getOPTIONS_MENU().setQuickSave(getScene().getChapter(), getScene().getIndex());
				}
				
				break;
			case "QLOAD":

				synchronized(this) {

				ChangeSceneQuick();
				}
				break;
			}
			
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		

		
	
	}
	public void stopVSM() throws InterruptedException {
		
		
		stopper = true;
	}
	public VN_SCENE getScene() {
		return scene;
	}
	public void setScene(VN_SCENE scene) {
		this.scene = scene;
	}
	public ArrayList<String[]> getLines() {
		return lines;
	}
	public void setLines(ArrayList<String[]> lines) {
		this.lines = lines;
	}
	public ArrayList<VN_IMAGE> getSprites() {
		return sprites;
	}
	public void setSprites(ArrayList<VN_IMAGE> sprites) {
		this.sprites = sprites;
	}
	public JTextArea getTxtbox() {
		return txtbox;
	}
	public void setTxtbox(JTextArea txtbox) {
		this.txtbox = txtbox;
	}
	public JLayeredPane getJLP() {
		return JLP;
	}
	public void setJLP(JLayeredPane jLP) {
		JLP = jLP;
	}
	public boolean isPause() {
		return pause;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
