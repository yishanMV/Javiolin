//Imports
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class AudioPlayer 
{
    private String[] noteNames;
    private Clip[] clips; 
    private File[] audioFiles;
	
	public AudioPlayer(String[] noteNamesIn, File[] audioFilesIn)
	{
		noteNames = noteNamesIn;
		audioFiles = audioFilesIn;
		clips = new Clip[audioFiles.length];
		loadAudioFiles();
	}
    //Load clips from the "Audio" folder
    public void loadAudioFiles() 
    {
		
        for (int i = 0; i < audioFiles.length; i++)
        {
            try 
            {
                AudioInputStream stream = AudioSystem.getAudioInputStream(audioFiles[i]);
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clips[i] = clip;                
            } 
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
            {
                System.err.println("Failed to load: " + audioFiles[i]);
                e.printStackTrace();
            }
        }
    }

	public void play(String noteName, int durationIn)
	{
		int duration = (int)(durationIn * 0.8);
		Timer clipTimer;
	
		int index = findNoteIndex(noteName);

		Clip clip = clips[index];

		if (clip.isRunning())
		{
			clip.stop();
			System.out.println("duplicate note");
			System.out.println(noteName);
			clip.setFramePosition(0);
			clip.start();
		}
		else
		{
			clip.setFramePosition(0);
			System.out.println("note restarted");
			clip.start();
		}

		class clipTimerHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				clip.stop();
				clip.setFramePosition(0);
			}
		}

		clipTimerHandler cth = new clipTimerHandler();
		clipTimer = new Timer(duration, cth);
		clipTimer.setRepeats(false);
		clipTimer.start();
	}
	
    //Helper method to get index of a note
    private int findNoteIndex(String noteName)
    {
        for (int i = 0; i < noteNames.length; i++) 
        {
            if (noteNames[i].equalsIgnoreCase(noteName))
            {
				return i;
			}
        }
        return -1;
    }
}
