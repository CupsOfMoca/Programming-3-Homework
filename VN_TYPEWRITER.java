package VN;

import javax.swing.JTextArea;


public class VN_TYPEWRITER extends Thread {
	
	JTextArea text;
	String line;
	String lastline;
	
	VN_SCENE VNS;
	int speed;
	private boolean pause = false;
	boolean skip = false;
	static boolean stop = false;
	
	VN_TYPEWRITER(JTextArea txt, String input,int spd, VN_SCENE vns){
		text = txt;
		
		line = input;
		lastline = "";
		speed = spd;
		VNS = vns;
		
	}
	public void run() {
		update();
		
		
	}
	synchronized public void update()  {
		int cntr = 0;
		char[] linechars = line.toCharArray();
		int insrtcntr = 0;
		while(cntr<linechars.length&&!stop) {
			if(insrtcntr > 3550/text.getFont().getSize() ) {
				if (linechars[cntr]==' ')text.append(System.getProperty("line.separator"));
				else text.append("-"+System.getProperty("line.separator"));
				insrtcntr = 0;
			}else {
				if(insrtcntr==0&& linechars[cntr]==' ');
				else {
					text.append(String.valueOf(linechars[cntr]));
					insrtcntr++;
				}
				cntr++;
			}
			
			try {
				sleep(speed);
			} catch (InterruptedException e) {
				break;
			}
			
		}
		
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastline = line;
		while(true) {
			
				if(isPause() == true) {
					
					
					if(lastline != line&&!skip) {
						text.setText("");
						cntr = 0;
						insrtcntr = 0;
						linechars = line.toCharArray();
						while(cntr<linechars.length&&!stop) {
							
							if(insrtcntr > 3550/text.getFont().getSize() ) {
								if (linechars[cntr]==' ')text.append(System.getProperty("line.separator"));
								else text.append("-"+System.getProperty("line.separator"));
								
								insrtcntr = 0;
							}else {
								if(insrtcntr==0&& linechars[cntr]==' ');
								else {
									text.append(String.valueOf(linechars[cntr]));
									insrtcntr++;
								}
								cntr++;
							}
							
							
							
							try {
								sleep(speed);
							} catch (InterruptedException e) {
								break;
							}
							
						}
						
						
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}else if (lastline != line) {
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}else {
					stop = false;
					setPause(true);
					lastline = line;
					
					
				}
			
			
		
		
		}
	}
	public void completeLine() {
		stop = true;
		int insrtcntr = 0;
		String templine = "";
		for(char c:line.toCharArray()) {
			if(insrtcntr==0&&c == ' ');
			else templine+=c;
			insrtcntr++;
			if(insrtcntr>3550/text.getFont().getSize()) {
				if(c == ' ') {
					templine+=System.getProperty("line.separator");
				}else templine+= "-"+System.getProperty("line.separator");
				
				insrtcntr = 0;
			}
				
		}
		text.setText(templine);
		
		
	}
	public void update_stats( String input, int spd) {
		if(line.compareTo(input)==0)skip = true;
		else skip = false;
		line = input;
		
		speed = spd;
		
		
		
	}
	public boolean isPause() {
		return pause;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
