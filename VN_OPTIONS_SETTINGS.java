package VN;

import java.awt.Color;


import java.io.BufferedReader;
import java.io.File;


import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VN_OPTIONS_SETTINGS {
	ArrayList<JTextField> SETTINGS_DATA_LIST = new ArrayList<JTextField>();
	ArrayList<String> SETTINGS_TYPE_LIST = new ArrayList<String>();
	VN_OPTIONS HOST;
	VN_OPTIONS_SETTINGS(VN_OPTIONS host){
		HOST = host;
		try {
			readSettings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void readSettings() throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("RESOURCES/SYSTEM/SETTINGS.txt")));
		String line;
		int cntr = 0;
		while((line =reader.readLine())!=null) {
			
			String[] splitline = line.split(" ");
			JTextField temp = new JTextField(10);
			temp.setText(splitline[1]);
			temp.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			temp.setFont(HOST.VNF.SCNMNG.getFont().deriveFont(30f));
			temp.setForeground(Color.white);
			temp.setOpaque(false);
			temp.setBounds(625,280+cntr*70,160,35);
			
			temp.getDocument().addDocumentListener(new DocumentListener() {
					
				 
				  public void changedUpdate(DocumentEvent e) {
					  saveSettings();
				  }
				  public void removeUpdate(DocumentEvent e) {
					  saveSettings();
				  }
				  public void insertUpdate(DocumentEvent e) {
					  saveSettings();
				  }

				  public void saveSettings() {
						
						try (PrintWriter out = new PrintWriter("RESOURCES/SYSTEM/SETTINGS.txt")){
							for(int i = 0; i<SETTINGS_TYPE_LIST.size();i++) {
								out.println(SETTINGS_TYPE_LIST.get(i)+" "+SETTINGS_DATA_LIST.get(i).getText());
							}
							out.close();
						} catch ( IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				});
			SETTINGS_DATA_LIST.add(temp);
			SETTINGS_TYPE_LIST.add(splitline[0]);
			cntr++;
		}
		reader.close();
	}
	
	
	
	
}
