package VN;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class VN_Cursor {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image image = toolkit.getImage("/Users/bence/eclipse-workspace/HW_VN/RESOURCES/IMAGES/Texture2D/XDCURSOR.png");
	static JFrame frame;
	Cursor c;
	VN_Cursor(JFrame f){
		frame = f;
		c = toolkit.createCustomCursor(image , new Point(frame.getX(), frame.getY()), "img");
		
	}
	Cursor getCursor() {return c;}
	
}
