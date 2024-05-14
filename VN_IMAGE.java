package VN;




import java.awt.Image;



import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;



public class VN_IMAGE implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String OGname;
	final static String filePath = "RESOURCES/IMAGES/Texture2D/";
	
	JLabel picLabel;
	transient Image IMG;
	ImageIcon icon ;
	public VN_IMAGE(String name) {
		OGname = name;
		this.name = name;
		picLabel = new JLabel();
		if(name.equals("loading"))icon = new ImageIcon(filePath+name+".gif");
		else icon = new ImageIcon(filePath+name+".png");
		IMG = icon.getImage();
		picLabel = new JLabel(new ImageIcon(IMG));
		picLabel.setOpaque(false);
		if(name.equals("loading"))picLabel.setBounds(900, 600, 2560, 1440);
		else picLabel.setBounds(0, 0, 2560, 1440);
		
		
	}
	public void ChangeImage(String n) {
		this.name = n;
		icon = new ImageIcon(filePath+name+".png");
		IMG = icon.getImage();
		picLabel.setIcon(icon);
		
	}
	public void ChangeImage(VN_IMAGE VNI) {
		name = VNI.getName();
		OGname = VNI.getOGName();
		IMG = VNI.IMG;
		icon = VNI.getimgicon();
		picLabel.setIcon(icon);
	}
	public void ResizeImage(int Width, int Height) {
		icon = new ImageIcon(IMG.getScaledInstance(Width, Height, 1));
		picLabel.setIcon(icon);
	}
	JLabel getLabel(){return picLabel;}
	Image getimage() {return IMG;}
	void setimage(Image i) {IMG = i;}
	ImageIcon getimgicon() {return icon;}
	String getOGName() {return OGname;}
	String getName() {return name;}
	void setOGName(String s) {OGname = s;}
	void refresh (){picLabel = new JLabel(new ImageIcon(IMG));}
}
