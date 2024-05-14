package VN;




import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


import VN.VN_Frame.LAST_PAGE;


public class VN_OPTIONS  {
	public ArrayList<VN_IMAGE> BUTTON_IMAGES = new ArrayList<VN_IMAGE>();
	public ArrayList<VN_IMAGE> BACKGROUND_IMAGES = new ArrayList<VN_IMAGE>();
	public VN_OPTIONS_SAVEFRAME_LIST SAVEFRAMES = new VN_OPTIONS_SAVEFRAME_LIST();
	public VN_OPTIONS_SAVEFRAME_LIST LOADFRAMES = new VN_OPTIONS_SAVEFRAME_LIST();
	public ArrayList<JLayeredPane> OPTIONS_PAGES = new ArrayList<JLayeredPane>();
	VN_Frame VNF;
	VN_OPTIONS_SETTINGS VOSet;
	JLayeredPane OPTIONS_CONTAINER = new JLayeredPane(); ;
	
	String selected_menu = "SELECT_BUTTON_TEXTSETTINGS";
	
	enum SETTINGS_STATE {SAVE, LOAD, TEXT} 
	SETTINGS_STATE CURRENT_STATE = SETTINGS_STATE.TEXT;
	VN_OPTIONS(SETTINGS_STATE newState,VN_Frame vnf, JLayeredPane component) throws ClassNotFoundException{
		VNF = vnf;
		VOSet = new VN_OPTIONS_SETTINGS(this);
		OPTIONS_CONTAINER.setPreferredSize(new Dimension(2560,1440));
		CURRENT_STATE = newState;
		for(int i = 0; i<4;i++) {
			OPTIONS_PAGES.add(new JLayeredPane());
			
		}
		for(JTextField Jtf : VOSet.SETTINGS_DATA_LIST) {
			OPTIONS_PAGES.get(0).add(Jtf);
		}
		
		try {
			loadImages();
			/*
			
			*/
			
			SAVEFRAMES =   readSavesOrLoads("saves");
			LOADFRAMES =  readSavesOrLoads("loads");
			
			for(int i = 0; i<10; i++) {
				SAVEFRAMES.FRAMES.get(i).addToPane(OPTIONS_PAGES.get(2));;
				SAVEFRAMES.FRAMES.get(i).SAVEFRAME.getLabel().addMouseListener(new SAVEFRAME_MOUSE_LISTENER(SAVEFRAMES.FRAMES.get(i),"save"));
			}
			for(int i = 0; i<10; i++) {
				LOADFRAMES.FRAMES.get(i).addToPane(OPTIONS_PAGES.get(1));;
				LOADFRAMES.FRAMES.get(i).SAVEFRAME.getLabel().addMouseListener(new SAVEFRAME_MOUSE_LISTENER(LOADFRAMES.FRAMES.get(i),"load"));
				SAVEFRAMES.FRAMES.get(i).sibling = LOADFRAMES.FRAMES.get(i);
				LOADFRAMES.FRAMES.get(i).sibling = SAVEFRAMES.FRAMES.get(i);
			}
			OPTIONS_PAGES.get(2).add(getImage("OPTIONS_BG_SAVE",BACKGROUND_IMAGES).getLabel());
			OPTIONS_PAGES.get(1).add(getImage("OPTIONS_BG_LOAD",BACKGROUND_IMAGES).getLabel());
				
			OPTIONS_PAGES.get(0).add(getImage("OPTIONS_BG_TEXT",BACKGROUND_IMAGES).getLabel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OPTIONS_CONTAINER.setVisible(false);
		
		
		switch(newState) {
		case SAVE:
			
			for(VN_IMAGE img:BUTTON_IMAGES) {
				if(img.getName().equals("OPTIONS_BUTTON_SAVE_NORMAL"))img.ChangeImage(img.getOGName()+"_SELECTED");
				OPTIONS_CONTAINER.add(img.getLabel(),0);
				
				
			}
			for(Component c: OPTIONS_PAGES.get(2).getComponents()) {
				OPTIONS_CONTAINER.add(c);
			}
			
			
			
			break;
		case LOAD:
			for(VN_IMAGE img:BUTTON_IMAGES) {
				if(img.getName().equals("OPTIONS_BUTTON_LOAD_NORMAL"))img.ChangeImage(img.getOGName()+"_SELECTED");
				OPTIONS_CONTAINER.add(img.getLabel(),0);
				
				
			}
			for(Component c: OPTIONS_PAGES.get(1).getComponents()) {
				OPTIONS_CONTAINER.add(c);
			}
			
			break;
		case TEXT:
			for(VN_IMAGE img:BUTTON_IMAGES) {
				if(img.getName().equals("OPTIONS_BUTTON_TEXTSETTINGS_NORMAL"))img.ChangeImage(img.getOGName()+"_SELECTED");
				OPTIONS_CONTAINER.add(img.getLabel(),0);
				
				
			}
			for(Component c: OPTIONS_PAGES.get(0).getComponents()) {
				OPTIONS_CONTAINER.add(c);
			}
			
			break;
		}
		
		VNF.PANE_LIST.set(2,OPTIONS_CONTAINER);
		
		
	}
	
	
	
	public void loadImages() throws IOException {
		final File folder = new File("RESOURCES/IMAGES/Texture2D/");
	    for (final File fileEntry : folder.listFiles()) {
	    	
	    	String[] name = fileEntry.getName().split("_");
	    	String used_name = fileEntry.getName().split("\\.")[0];
	        if(name[0].equals("OPTIONS")){
	        	if(name[1].equals("BUTTON")) {
	        		if(name[3].split("\\.")[0].equals("NORMAL")) {
	        			VN_IMAGE newButton =new VN_IMAGE(used_name);
	        			BUTTON_IMAGES.add( newButton);
		        		
		        		switch(name[2]) {
		        		case "EXIT":
		        			newButton.getLabel().setBounds(2240, 1318, 263, 111);
		        			
		        			break;
		        		case "TITLE":
		        			newButton.getLabel().setBounds(1960, 1318, 263, 111);
		        			break;
		        		case "BACK":
		        			newButton.getLabel().setBounds(1680, 1318, 263, 111);
		        			break;
		        		case "TEXTSETTINGS":
		        			newButton.getLabel().setBounds(2240, 40, 263, 111);
		        			break;
		        		case "LOAD":
		        			newButton.getLabel().setBounds(2000, 40, 263, 111);
		        			break;
		        		case "SAVE":
		        			newButton.getLabel().setBounds(1780, 40, 263, 111);
		        			break;
		        		default:
		        			break;
		        		}
		        		newButton.setOGName(name[0]+"_"+name[1]+"_"+name[2]);
		        		newButton.getLabel().addMouseListener(new OptionsButtonMouseListener(newButton,VNF));
		        		OPTIONS_PAGES.get(0).add(newButton.getLabel());
		        		
		        		
	        		}
	        		
	        		
	        		
	        	}
	        	else if(name[1].equals("BG")) {
	        		BACKGROUND_IMAGES.add(new VN_IMAGE(used_name));
	        		
	        	}
	        	
	        }
	        
	    }
	}
	public void setOptionsVisible(boolean b) {
		OPTIONS_CONTAINER.setVisible(b);
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
	public class OptionsButtonMouseListener implements MouseListener{
		VN_IMAGE VNI;
		
		VN_Frame VNf;
		OptionsButtonMouseListener(VN_IMAGE img,  VN_Frame f){
			VNI = img;
			
			VNf = f;
		}
		public void mouseEntered(MouseEvent me) {
			String[] buttonname = VNI.getName().split("_");
			if(!buttonname[3].equals("SELECTED")) {
				if(!((buttonname[2].equals("BACK")||buttonname[2].equals("SAVE"))&&VNf.getLast_page() == LAST_PAGE.TITLE)) {
					VNI.ChangeImage(VNI.getOGName()+"_HOVER");
					try {
		        		VNf.AUDIO_MNGR.addTrack("BUTTON_HOVER",true,false);
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						e.printStackTrace();
					}
				}
				
			}
        	VNf.PANE_LIST.get(2).repaint();
        }
		@Override
		public void mouseClicked(MouseEvent e) {
			String[] buttonname = VNI.getName().split("_");
			
			if(!buttonname[3].equals("SELECTED")&&buttonname[3].equals("HOVER")) {
				VNI.ChangeImage(VNI.getOGName()+"_SELECTED");
				try {
					getImage(selected_menu+"_SELECTED",BUTTON_IMAGES).ChangeImage(selected_menu+"_NORMAL");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				switch(VNI.getName().split("_")[2]) {
	    		case "EXIT":
	    			
	    			try {
						saveSavesOrLoads(SAVEFRAMES,"saves");
						saveSavesOrLoads(LOADFRAMES,"loads");
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
	    			System.exit(0);
	    			
	    			break;
	    		case "TITLE":
	    			if(VNf.getLast_page() == LAST_PAGE.SCENE) {
	    				
	    				VNf.PANE_LIST.get(1).removeAll();
	    				try {
							VNF.AUDIO_MNGR.deleteTrack(VNF.SCNMNG.getLines().get(VNF.SCNMNG.getScene().getIndex())[9]);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							
							VNf.SCNMNG.stopVSM();
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
							
						
						
	    				try {

	    					
	    					VNf.AUDIO_MNGR.addTrack(VNF.titletheme, true,true);
	    					
							
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    			}
	    			VNf.setLast_page(LAST_PAGE.TITLE);
	    			
	    			VNI.ChangeImage(VNI.getOGName()+"_NORMAL");
					VNf.remove(VNf.PANE_LIST.get(2));
					VNf.add(VNf.PANE_LIST.get(0));
					SwingUtilities.updateComponentTreeUI(VNf);
	    			break;
	    		case "BACK":
	    			if(VNf.getLast_page() == LAST_PAGE.SCENE) {
	    				VNf.add(VNf.PANE_LIST.get(1));
						VNf.remove(VNf.PANE_LIST.get(2));
						VNI.ChangeImage(VNI.getOGName()+"_NORMAL");
						SwingUtilities.updateComponentTreeUI(VNf);
	    			}
	    				
	    			break;
	    		case "TEXTSETTINGS":
	    			
	    			try {
						setState(SETTINGS_STATE.TEXT);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
	    			SwingUtilities.updateComponentTreeUI(VNf);
	    			
	    			break;
	    		case "LOAD":
	    			try {
						setState(SETTINGS_STATE.LOAD);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
	    			SwingUtilities.updateComponentTreeUI(VNf);
	    			
	    			break;
	    		case "SAVE":
	    			try {
						setState(SETTINGS_STATE.SAVE);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
	    			SwingUtilities.updateComponentTreeUI(VNf);
	    			break;
	    		default:
	    			break;
	    		}
				try {
					
					VNf.AUDIO_MNGR.addTrack("BUTTON_CLICK",true,false);
					
					
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e3) {
					e3.printStackTrace();
				}
				selected_menu = VNI.getOGName();
				VNf.PANE_LIST.get(2).repaint();
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
		@Override
		public void mouseExited(MouseEvent e) {
			if(!VNI.getName().split("_")[3].equals("SELECTED"))VNI.ChangeImage(VNI.getOGName()+"_NORMAL");
			VNf.PANE_LIST.get(2).repaint();
			
		}
	}
	public void setState(SETTINGS_STATE nState) throws IOException {
		if(CURRENT_STATE!= nState) {
			switch(nState) {
			case SAVE:
				int cntr3 = 0;
    			for(Component c: OPTIONS_CONTAINER.getComponents()) {
    				cntr3++;
    				if(cntr3>6) {
    					if(CURRENT_STATE == SETTINGS_STATE.LOAD) {
    						OPTIONS_PAGES.get(1).add(c);
    						String temp = "OPTIONS_BUTTON_LOAD_SELECTED";
    						
    						try {
    							
								getImage(temp,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_LOAD_NORMAL");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    					}
    					else {
    						OPTIONS_PAGES.get(0).add(c);
    						
    						String temp = "OPTIONS_BUTTON_TEXTSETTINGS_SELECTED";
    						try {
								getImage(temp,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_TEXTSETTINGS_NORMAL");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    					}
    					String temp2 = "OPTIONS_BUTTON_SAVE_NORMAL";
    					getImage(temp2,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_SAVE_SELECTED");
    				}
    			}
				for(Component c:OPTIONS_PAGES.get(2).getComponents()) {
					
					OPTIONS_CONTAINER.add(c);
				}
				CURRENT_STATE= SETTINGS_STATE.SAVE;
    			
				break;
			case LOAD:
				int cntr2 = 0;
    			for(Component c: OPTIONS_CONTAINER.getComponents()) {
    				cntr2++;
    				if(cntr2>6) {
    					if(CURRENT_STATE == SETTINGS_STATE.SAVE) {
    						OPTIONS_PAGES.get(2).add(c);
    						String temp = "OPTIONS_BUTTON_SAVE_SELECTED";
    						try {
								getImage(temp,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_SAVE_NORMAL");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    						
    					}
    					else {
    						
    						OPTIONS_PAGES.get(0).add(c);
    						String temp = "OPTIONS_BUTTON_TEXTSETTINGS_SELECTED";
    						try {
								getImage(temp,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_TEXTSETTINGS_NORMAL");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    					}
    					String temp2 = "OPTIONS_BUTTON_LOAD_NORMAL";
    					getImage(temp2,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_LOAD_SELECTED");
    				}
    			}
    			cntr2 = 0;
				for(Component c:OPTIONS_PAGES.get(1).getComponents()) {
					
					OPTIONS_CONTAINER.add(c);
				}
    			
    			CURRENT_STATE= SETTINGS_STATE.LOAD;
				break;
			case TEXT:
				int cntr = 0;
				for(Component c: OPTIONS_CONTAINER.getComponents()) {
					cntr++;
					if(cntr>6) {
						if(CURRENT_STATE == SETTINGS_STATE.SAVE) {
							OPTIONS_PAGES.get(2).add(c);
							String temp = "OPTIONS_BUTTON_SAVE_SELECTED";
							try {
								getImage(temp,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_SAVE_NORMAL");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else{
							OPTIONS_PAGES.get(1).add(c);
							String temp = "OPTIONS_BUTTON_LOAD_SELECTED";
							try {
								getImage(temp,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_LOAD_NORMAL");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						String temp2 = "OPTIONS_BUTTON_TEXTSETTINGS_NORMAL";
    					getImage(temp2,BUTTON_IMAGES).ChangeImage("OPTIONS_BUTTON_TEXTSETTINGS_SELECTED");
					}
				}
				cntr=0;
				for(Component c:OPTIONS_PAGES.get(0).getComponents()) {
					
					OPTIONS_CONTAINER.add(c);
				}
				
				CURRENT_STATE= SETTINGS_STATE.TEXT;
				
				break;
			}
		}
		
	}
	public VN_OPTIONS_SAVEFRAME_LIST readSavesOrLoads(String s) throws  ClassNotFoundException, FileNotFoundException{
		String dir;
		if(s.equals("saves"))dir ="RESOURCES/SYSTEM/SAVES.ser";
		else dir ="RESOURCES/SYSTEM/LOADS.ser";
		VN_OPTIONS_SAVEFRAME_LIST VOSL=new VN_OPTIONS_SAVEFRAME_LIST();
        try (FileInputStream fis=new FileInputStream(dir)) {
        	
    		
        	ObjectInputStream ois = new ObjectInputStream(fis);
        	VOSL= (VN_OPTIONS_SAVEFRAME_LIST) ois.readObject();
        	fis.close();
        	ois.close();
        	return VOSL;
			
		}catch(IOException F) {
			//generáljunk le új save fileokat ha azokat nem találjuk.
			for(int i = 1; i<11;i++) {
				try {
					SAVEFRAMES.FRAMES.add(new VN_OPTIONS_SAVEFRAME("save",i,VNF.SCNMNG.getFont(),0,0,i));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				SAVEFRAMES.FRAMES.get(i-1).SAVEFRAME.getLabel().addMouseListener(new SAVEFRAME_MOUSE_LISTENER(SAVEFRAMES.FRAMES.get(i-1),"save"));
			}
			for(int i = 1; i<11;i++) {
				
				try {
					LOADFRAMES.FRAMES.add(new VN_OPTIONS_SAVEFRAME("load",i,VNF.SCNMNG.getFont(),0,0,i));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				LOADFRAMES.FRAMES.get(i-1).SAVEFRAME.getLabel().addMouseListener(new SAVEFRAME_MOUSE_LISTENER(LOADFRAMES.FRAMES.get(i-1),"load"));
				
				SAVEFRAMES.FRAMES.get(i-1).sibling = LOADFRAMES.FRAMES.get(i-1);
				LOADFRAMES.FRAMES.get(i-1).sibling = SAVEFRAMES.FRAMES.get(i-1);
			}
		}
		if(s.equals("saves"))return SAVEFRAMES;
		else return LOADFRAMES;
	}
	public void saveSavesOrLoads (VN_OPTIONS_SAVEFRAME_LIST al, String s) throws IOException {
		String dir;
		if(s.equals("saves"))dir ="RESOURCES/SYSTEM/SAVES.ser";
		else dir ="RESOURCES/SYSTEM/LOADS.ser";
		
		 
		try {
	        FileOutputStream fop=new FileOutputStream(dir);
	        try (ObjectOutputStream oos = new ObjectOutputStream(fop)) {
	        	oos.writeObject(al);
	        	fop.close();
	        	oos.close();
				
			}

	    } catch( FileNotFoundException e) {
	    	
	    }
	}
	public void setQuickSave(int chpt, int indx) {
		for(VN_OPTIONS_SAVEFRAME VOS: SAVEFRAMES.FRAMES) {
			VOS.qschapter = chpt;
			VOS.qsindex = indx;
		}
		
	}
	public int[] getQuickSave() {
		return new int[]{SAVEFRAMES.FRAMES.get(0).qschapter,SAVEFRAMES.FRAMES.get(0).qsindex};
		
	}
	
	public class SAVEFRAME_MOUSE_LISTENER implements MouseListener{
		VN_OPTIONS_SAVEFRAME target;
		String type;
		
		SAVEFRAME_MOUSE_LISTENER(VN_OPTIONS_SAVEFRAME t, String ty){
			target = t;
			type = ty;
			
		}
		
		public void updateSAVEFRAME() {
			target.chapter = VNF.SCNMNG.getScene().getChapter();
			target.index=VNF.SCNMNG.getScene().getIndex();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			
			
			target.SaveDate.setText(dtf.format(now));
			target.Comment.setText(VNF.SCNMNG.getScene().Texts.get(3).getText());
			target.sibling.SaveDate.setText(target.SaveDate.getText()); 
			target.sibling.Comment.setText(target.Comment.getText());
			target.sibling.chapter = VNF.SCNMNG.getScene().getChapter();
			target.sibling.index=VNF.SCNMNG.getScene().getIndex();		
			
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(type.equals("save")) {
				updateSAVEFRAME();	
				try {
					
					ImageIO.write((RenderedImage) VNF.screenshot, "png", new File("RESOURCES/IMAGES/Texture2D/screenshot"+Integer.toString(target.ID)+".png"));
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				target.SCREENSHOT.ChangeImage("screenshot"+Integer.toString(target.ID));
				
				target.SCREENSHOT.icon.setImage(target.SCREENSHOT.icon.getImage().getScaledInstance(256, 144, 0 ));
				target.sibling.SCREENSHOT.ChangeImage("screenshot"+Integer.toString(target.ID));
				target.sibling.SCREENSHOT.icon.setImage(target.SCREENSHOT.icon.getImage().getScaledInstance(256, 144, 0 ));
				try {
					VNF.AUDIO_LIST.add(new VN_Audio("BUTTON_CLICK",false));
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException ae) {
					ae.printStackTrace();
				}
			}
			if(type.equals("load")) {
				if(target.SCREENSHOT.getName().contains("screenshot")) {
					try {
						VNF.AUDIO_MNGR.addTrack("loadscene", true, false);
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					if(VNF.getLast_page() == LAST_PAGE.TITLE) {
						try {
							VNF.SCNMNG = new VN_SCENE_MANAGER(VNF,VNF.PANE_LIST.get(1),target.chapter,target.index);
						} catch (FontFormatException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						VNF.SCNMNG.start();
						try {
							VNF.AUDIO_MNGR.deleteTrack(VNF.titletheme);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						VNF.setLast_page(LAST_PAGE.SCENE);
					}else {
						
						
						try {
							VNF.SCNMNG.ChangeScene(target );
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
							
					}
						
					
					
					
					VNF.add(VNF.PANE_LIST.get(1));	
					VNF.remove(VNF.PANE_LIST.get(2));	
					SwingUtilities.updateComponentTreeUI(VNF);
				}
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

		@Override
		public void mouseEntered(MouseEvent e) {
			
			try {
				
				
				VNF.AUDIO_MNGR.addTrack("BUTTON_HOVER", true, false);
				
				
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(type.equals("save")) {
				target.SAVEFRAME.ChangeImage("OPTIONS_SAVE_SAVEFRAME_ACTIVE");
			}else {
				target.SAVEFRAME.ChangeImage("OPTIONS_LOAD_SAVEFRAME_ACTIVE");
			}
			
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(type.equals("save")) {
				target.SAVEFRAME.ChangeImage("OPTIONS_SAVE_SAVEFRAME");
			}else {
				target.SAVEFRAME.ChangeImage("OPTIONS_LOAD_SAVEFRAME");
			}
			
		}
	}
}
