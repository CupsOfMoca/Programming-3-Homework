package VN;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import VN.VN_OPTIONS.SETTINGS_STATE;




public class VN_Frame extends JFrame {
	
	public VN_Audio BGM;
	public VN_Audio YuukaTitle;
	public ArrayList<JLayeredPane> PANE_LIST = new ArrayList<JLayeredPane>();
	
	public ArrayList<VN_IMAGE> IMAGE_LIST = new ArrayList<VN_IMAGE>();
	public ArrayList<VN_Audio> AUDIO_LIST = new ArrayList<VN_Audio>();
	
	
	volatile public VN_Audio_Manager AUDIO_MNGR = new VN_Audio_Manager(AUDIO_LIST);
	
	public VN_SCENE_MANAGER SCNMNG ;
	
	private VN_OPTIONS OPTIONS_MENU;
	public VN_Cursor CURSOR = new VN_Cursor(this);
	private static final long serialVersionUID = 1L;
	String[] imagenames = {"TITLE_UI_START","TITLE_UI_LOAD","TITLE_UI_QUICKLOAD","TITLE_UI_SETTINGS","TITLE_UI_EXIT","TITLE_UI_HELP","YUUKA_TITLE","TITLE_BG"};
	Integer[] buttonpos = {490, 608,729,849,1090};
	String titletheme;
	enum LAST_PAGE {TITLE,SCENE}
	private LAST_PAGE last_page;
	Image screenshot;
	public VN_Frame() throws IOException, FontFormatException, ClassNotFoundException{
		
		setDefaultCloseOperation(2);
		setPreferredSize(new Dimension(2560,1440));
		setTitle("Blue*Archive");
		setResizable(false);
		getContentPane().setBackground(Color.black);
		AUDIO_MNGR.start();
		
		
		PANE_LIST.add(new JLayeredPane());
		PANE_LIST.add(new JLayeredPane());
		PANE_LIST.add(new JLayeredPane());
		SCNMNG = new VN_SCENE_MANAGER(this,PANE_LIST.get(1),1,0);
		setOPTIONS_MENU(new VN_OPTIONS(SETTINGS_STATE.SAVE,this,PANE_LIST.get(2)));
		this.addKeyListener(new SpaceKeyListener());
		
		PANE_LIST.get(0).setName("TITLE");
		for(String str : imagenames) {
			LoadImageToList(str,PANE_LIST,"TITLE",true,false ,IMAGE_LIST);
		}
		for(int i=0;i<5;i++) {
			IMAGE_LIST.get(i).getLabel().setBounds(1388, buttonpos[i],1172 , 101);
			IMAGE_LIST.get(i).getLabel().addMouseListener(new ButtonMouseListener(IMAGE_LIST.get(i),PANE_LIST.get(0),this));
		}
		IMAGE_LIST.get(5).getLabel().setBounds(1452 , 1240,1108 , 80);
		//IMAGE_LIST.add(new VN_IMAGE("NOA_SMILECLOSEDEYESS"));
		//PANE_LIST.get(1).add(IMAGE_LIST.get(8).getLabel());
		add(PANE_LIST.get(0));
		setLast_page(LAST_PAGE.TITLE);
		
		
		
	}
	public void LoadImageToList(String name, ArrayList<JLayeredPane> PL,String PName,boolean addToPane,boolean createPane, ArrayList<VN_IMAGE> IL ) throws IOException {
		VN_IMAGE IMG = new VN_IMAGE(name);
		IL.add(IMG);
		if(createPane) {
			JLayeredPane JP = new JLayeredPane();
			JP.setPreferredSize(new Dimension(2560,1440));
			JP.setName(PName);
			PL.add(JP);
		}
		if(addToPane) {
			for(JLayeredPane temp : PL) {
				
				if(temp.getName() == PName) { 
					
					temp.add(IMG.getLabel());
				
				}
			}
		}
	}
	public class ButtonMouseListener implements MouseListener{
		VN_IMAGE VNI;
		JLayeredPane LP;
		VN_Frame VNf;
		ButtonMouseListener(VN_IMAGE img, JLayeredPane lp, VN_Frame f){
			VNI = img;
			LP = lp;
			VNf = f;
		}
		public void mouseEntered(MouseEvent me) {
			
			VNI.ChangeImage(VNI.getOGName()+"_ACTIVE");
			LP.repaint();
			IMAGE_LIST.get(5).ChangeImage(IMAGE_LIST.get(5).getOGName()+"_"+VNI.getOGName().split("_")[2]);
			
        	try {
				AUDIO_LIST.add(new VN_Audio("BUTTON_HOVER",false));
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
        }
        @Override
        public void mouseExited(MouseEvent me ) {
        	
        	VNI.ChangeImage(VNI.getOGName());
        	LP.repaint();
        	IMAGE_LIST.get(5).ChangeImage(IMAGE_LIST.get(5).getOGName());
        	
        }
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			if(VNI.getOGName()=="TITLE_UI_EXIT") {
				try {
    				getOPTIONS_MENU().saveSavesOrLoads(getOPTIONS_MENU().SAVEFRAMES,"saves");
    				getOPTIONS_MENU().saveSavesOrLoads(getOPTIONS_MENU().LOADFRAMES,"loads");
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.exit(0);
			}
			if(VNI.getOGName()=="TITLE_UI_START") {
				VNf.add(VNf.PANE_LIST.get(1));
				try {
					SCNMNG = new VN_SCENE_MANAGER(VNf,VNf.PANE_LIST.get(1),1,0);
				} catch (FontFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				SCNMNG.start();
				VNI.ChangeImage(VNI.getOGName());
				VNf.remove(VNf.PANE_LIST.get(0));
				try {
					AUDIO_MNGR.deleteTrack(titletheme);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				setLast_page(LAST_PAGE.SCENE);
				SwingUtilities.updateComponentTreeUI(VNf);
				
			}
			if(VNI.getOGName()=="TITLE_UI_QUICKLOAD") {
				VNf.add(VNf.PANE_LIST.get(1));
				try {
					SCNMNG.stopVSM();
				} catch (InterruptedException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					SCNMNG = new VN_SCENE_MANAGER(VNf,VNf.PANE_LIST.get(1),getOPTIONS_MENU().SAVEFRAMES.FRAMES.get(0).qschapter,getOPTIONS_MENU().SAVEFRAMES.FRAMES.get(0).qsindex);
				} catch (FontFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				SCNMNG.start();
				VNI.ChangeImage(VNI.getOGName());
				VNf.remove(VNf.PANE_LIST.get(0));
				try {
					AUDIO_MNGR.deleteTrack(titletheme);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				setLast_page(LAST_PAGE.SCENE);
				SwingUtilities.updateComponentTreeUI(VNf);
				
			}
			if(VNI.getOGName()=="TITLE_UI_SETTINGS") {
				try {
					getOPTIONS_MENU().setState(SETTINGS_STATE.TEXT);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				VNf.add(VNf.PANE_LIST.get(2));
				VNI.ChangeImage(VNI.getOGName());
				VNf.remove(VNf.PANE_LIST.get(0));
				SwingUtilities.updateComponentTreeUI(VNf);
			}
			if(VNI.getOGName()=="TITLE_UI_LOAD") {
				
				try {
					getOPTIONS_MENU().setState(SETTINGS_STATE.LOAD);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				VNf.add(VNf.PANE_LIST.get(2));
				VNI.ChangeImage(VNI.getOGName());
				VNf.remove(VNf.PANE_LIST.get(0));
				SwingUtilities.updateComponentTreeUI(VNf);
			}
			try {
				AUDIO_LIST.add(new VN_Audio("BUTTON_CLICK",false));
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException ae) {
				ae.printStackTrace();
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
	public class SpaceKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent keyEvent) {
			super.keyPressed(keyEvent);
			if(keyEvent.getKeyChar() == KeyEvent.VK_SPACE||keyEvent.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
				
				
				
				
				if(SCNMNG.getScene().getTYPEWRITER_TXT().getState()!= Thread.State.WAITING&&SCNMNG.getScene().getTYPEWRITER_TXT_SHDW().getState()!= Thread.State.WAITING&&SCNMNG.getState()!=Thread.State.TIMED_WAITING) {
					SCNMNG.getScene().getTYPEWRITER_TXT().completeLine();
					SCNMNG.getScene().getTYPEWRITER_TXT_SHDW().completeLine();
					
					
				}else {
					if(SCNMNG.getState()==Thread.State.WAITING &&SCNMNG.getScene().getTYPEWRITER_TXT().getState()==Thread.State.WAITING&&SCNMNG.getScene().getTYPEWRITER_TXT_SHDW().getState()==Thread.State.WAITING&& SCNMNG.VSE.getState()==Thread.State.WAITING&&SCNMNG.VSE.getState()!=Thread.State.TIMED_WAITING) {
						
						if(keyEvent.getKeyChar() == KeyEvent.VK_SPACE)SCNMNG.getScene().setIndex(SCNMNG.getScene().getIndex() + 1);
						else if(keyEvent.getKeyChar() == KeyEvent.VK_BACK_SPACE) SCNMNG.getScene().setIndex(SCNMNG.getScene().getIndex() - 1);
						
							synchronized(SCNMNG) {
								SCNMNG.notify();
							}
							
							synchronized(SCNMNG.getScene().getTYPEWRITER_TXT()) {	
								SCNMNG.getScene().getTYPEWRITER_TXT().notify();
							}
							synchronized(SCNMNG.getScene().getTYPEWRITER_TXT_SHDW()) {
								SCNMNG.getScene().getTYPEWRITER_TXT_SHDW().notify();
							}
							SCNMNG.getScene().getTYPEWRITER_TXT().setPause(false);
							SCNMNG.getScene().getTYPEWRITER_TXT_SHDW().setPause(false);
							SCNMNG.setPause(false);
							
						
					}
				}
				
				
				
				
			}
			if(keyEvent.getKeyChar() == KeyEvent.VK_ESCAPE) {

    			try {
    				getOPTIONS_MENU().saveSavesOrLoads(getOPTIONS_MENU().SAVEFRAMES,"saves");
    				getOPTIONS_MENU().saveSavesOrLoads(getOPTIONS_MENU().LOADFRAMES,"loads");
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
    			
				System.exit(0);
			}
			
			
			
			
		}
	}
	public VN_Audio_Manager getAUDIO_MNGR() {return AUDIO_MNGR;}
	public LAST_PAGE getLast_page() {
		return last_page;
	}
	public void setLast_page(LAST_PAGE last_page) {
		this.last_page = last_page;
	}
	public VN_OPTIONS getOPTIONS_MENU() {
		return OPTIONS_MENU;
	}
	public void setOPTIONS_MENU(VN_OPTIONS oPTIONS_MENU) {
		OPTIONS_MENU = oPTIONS_MENU;
	}
	
	
}
