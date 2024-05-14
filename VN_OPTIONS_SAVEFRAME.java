package VN;


import java.awt.Color;
import java.awt.Font;



import java.io.IOException;


import java.io.Serializable;


import javax.swing.JLayeredPane;
import javax.swing.JTextArea;




public class VN_OPTIONS_SAVEFRAME implements Serializable {
	
	private static final long serialVersionUID = 1L;
	VN_IMAGE SAVEFRAME;

	JTextArea Number = new JTextArea();

	JTextArea SaveDate = new JTextArea();

	JTextArea Comment = new JTextArea();
	
	VN_OPTIONS_SAVEFRAME sibling;
	
	VN_IMAGE SCREENSHOT;
	public int chapter;
	public int index;
	public int qschapter = 2;
	public int qsindex = 1;
	public int ID;
	
	VN_OPTIONS_SAVEFRAME(String s,int slot,Font fnt,int chpt,int indx, int id) throws IOException{
		chapter = chpt;
		index = indx;
		if(s.equals("save")){
			SAVEFRAME = new VN_IMAGE("OPTIONS_SAVE_SAVEFRAME");
		}else if (s.equals("load")) {
			SAVEFRAME = new VN_IMAGE("OPTIONS_LOAD_SAVEFRAME");
			
		}
		ID = id;
		
		Number.setText("No. "+Integer.toString(slot));
		Number.setFont(fnt.deriveFont(30f));
		Number.setForeground(Color.white);
		Number.setOpaque(false);
		
		
		
		SaveDate.setFont(fnt.deriveFont(22f));
		SaveDate.setForeground(Color.white);
		SaveDate.setEditable(false);
		SaveDate.setOpaque(false);
		
		Comment.setText("");
		Comment.setFont(fnt.deriveFont(30f));
		Comment.setForeground(Color.white);
		Comment.setOpaque(false);
		
		SCREENSHOT = new VN_IMAGE("OPTIONS_NOSAVEDATA");
		SCREENSHOT.ResizeImage(256, 144);
		int column;
		if(((slot-1)%10)<5) column = 0;
		else column = 1;
		switch(column) {
		case 0:
			SAVEFRAME.getLabel().setBounds(57,320+((slot+4)%5)*170,775,168);
			Number.setBounds(340,340+((slot+4)%5)*170,100,35);
			SaveDate.setBounds(340,390+((slot+4)%5)*170,330,35);
			Comment.setBounds(340,440+((slot+4)%5)*170,330,35);
			SCREENSHOT.getLabel().setBounds(69,333+((slot+4)%5)*170,256,144);
			
			break;
		case 1:
			SAVEFRAME.getLabel().setBounds(838,320+((slot+4)%5)*170,775,168);
			Number.setBounds(1115,340+((slot+4)%5)*170,100,35);
			SaveDate.setBounds(1115,390+((slot+4)%5)*170,330,35);
			Comment.setBounds(1115,440+((slot+4)%5)*170,330,35);
			SCREENSHOT.getLabel().setBounds(849,333+((slot+4)%5)*170,256,144);
			break;
		}
		
			
		
		
	}
	
	
	
	public void addToPane(JLayeredPane jlp) {
		jlp.add(Number);
		jlp.add(SaveDate);
		jlp.add(Comment);
		jlp.add(SAVEFRAME.getLabel());
		jlp.add(SCREENSHOT.getLabel());
		
	}
	
	
}
