package VN_Test;

import VN.*;

import java.awt.FontFormatException;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;

public class VNTest {

	VN_Frame engine;
	VN_IMAGE testImage;
	VN_Audio testAudio;
	
	
	@Before
	public void setUp() throws ClassNotFoundException, IOException, FontFormatException {
		 engine = new VN_Frame();
		 testImage = new VN_IMAGE("SPRITE_TEMP") ;
		
	}
	
	
	
	@Test
	public void testSCNMNGloadsSuccessfully()  {
		 
		engine.add(engine.PANE_LIST.get(1));
		try {
			engine.SCNMNG = new VN_SCENE_MANAGER(engine,engine.PANE_LIST.get(1),2,1);
		} catch (FontFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		engine.SCNMNG.start();
		
		engine.remove(engine.PANE_LIST.get(0));
		
		
		
		Assert.assertEquals(engine.SCNMNG.getLines().get(1)[2], "I-BU-KI!");
		
	}
	
	@Test
	public void testQuickSceneChange()  {
		 
		engine.SCNMNG.ChangeSceneQuick();
		Assert.assertEquals(engine.getOPTIONS_MENU().SAVEFRAMES.FRAMES.get(0).qschapter, 2);
		Assert.assertEquals(engine.getOPTIONS_MENU().SAVEFRAMES.FRAMES.get(0).qsindex, 6);
	}
	@Test
	public void testSounds() throws UnsupportedAudioFileException, IOException, LineUnavailableException  {
		 
		engine.AUDIO_MNGR.addTrack("EXPLOSION", true, false);
		Assert.assertFalse(engine.AUDIO_MNGR.isTrackInList("grab"));
		Assert.assertTrue(engine.AUDIO_MNGR.isTrackInList("EXPLOSION"));
	}
	
	@Test
	public void testSceneChange() throws UnsupportedAudioFileException, IOException, LineUnavailableException  {
		 
		engine.AUDIO_MNGR.addTrack("EXPLOSION", true, false);
		Assert.assertFalse(engine.AUDIO_MNGR.isTrackInList("grab"));
		Assert.assertTrue(engine.AUDIO_MNGR.isTrackInList("EXPLOSION"));
	}
	@Rule
	public ExpectedException thrownimgerr ;
	
	@Test(expected=NullPointerException.class )
	public void testImageNotFound()  {
		
		testImage = new VN_IMAGE("eznemletezik");
		thrownimgerr.expect(NullPointerException.class);
		
		
	}
	
	@Rule
	public ExpectedException thrownauderr ;
	
	@Test(expected=IOException.class )
	public void testAudioNotFound() throws UnsupportedAudioFileException, IOException, LineUnavailableException  {
		
		testAudio = new VN_Audio("eznemletezik", false);
		thrownauderr.expect(IOException.class);
		
		
	}
	@Rule
	public ExpectedException thrownindxerr ;
	
	@Test(expected=NullPointerException.class )
	public void sceneinsidethread()    {
		engine.SCNMNG.getScene().setChapter(33);
		thrownauderr.expect(IndexOutOfBoundsException.class);
	}
	
	@Before
	public void setUp2() throws ClassNotFoundException, IOException, FontFormatException {
		 engine = new VN_Frame();
		 
		
	}
	
	@Test
	public void indexoutsideofrangeguarded()    {
		
		engine.SCNMNG.getScene().setIndex(25);
		String result = "";
	
		if(engine.SCNMNG.getScene().getIndex()<engine.SCNMNG.getLines().size()-1) {
			engine.SCNMNG.getScene().setIndex(engine.SCNMNG.getScene().getIndex() + 1);
		}
		if(engine.SCNMNG.getScene().getIndex()<engine.SCNMNG.getLines().size()) {
			result = "I have notified the scnmng and the typewriters to step";
		}
		Assert.assertEquals(engine.SCNMNG.getScene().getIndex(),25);
		Assert.assertEquals(result,"I have notified the scnmng and the typewriters to step");
	}
	@Test
	public void testSerializedData() throws ClassNotFoundException, IOException   {
		int initchp = engine.getOPTIONS_MENU().SAVEFRAMES.FRAMES.get(1).qschapter;
		
		if (initchp == 1) {
			engine.getOPTIONS_MENU().setQuickSave(2,3);
		}else {
			engine.getOPTIONS_MENU().setQuickSave(1,3);
		}
		
		
		
		engine.getOPTIONS_MENU().saveSavesOrLoads(engine.getOPTIONS_MENU().SAVEFRAMES,"saves");
		engine.getOPTIONS_MENU().SAVEFRAMES = engine.getOPTIONS_MENU().readSavesOrLoads("saves");
		Assert.assertNotEquals(engine.getOPTIONS_MENU().SAVEFRAMES.FRAMES.get(1).qschapter,initchp);
	}
	
	
}