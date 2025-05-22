//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//Game.java
//
//This is the main class of the game which should be run to start the game
//It also methods to read files, and getter setter methods to access field variables
//Code v4, Game is almost finished, just need some touch ups and extra features/ new songs/ Finish up instructions panel content

//Imports
import java.awt.CardLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException; 
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.PrintWriter;

//Class header 
public class Game
{
    //All field variables

    //Field variables for game statistics
	public int accuracyPercent;    //Stores the accuracy of how many notes the user got correctly
    public int highScore;
	public int notesCountTotal; //To track how many notes the user has played through the entire game session
	public int noteCorrectTotal;	//To track how many notes the user has played CORRECTLY through the entire game session
	public int lifetimeNotesTracker;	//Tracks how many notes have been played by the user in the game session, as a milestone marker
    public int currentScore;    //Saves the current score of the game session

    //Field variables for game configurations
    public int gameDuration;    //The duration of the game in seconds (adjustable)
    public String gameMode;   //The mode of the game, either "song" or "freeplay"
    public String songName;   //The name of the song that is currently being played for the game
    public boolean hint;	//Whether hints are toggled on or off

    //Field variables for game state
	public boolean oneRoundOver;  //Boolean to track if one round is over, to update home page stats accordingly
    public boolean gameInSession; //Boolean to track if the game is in session

    //Field variables for information
    public String[] noteNames;  //Array to store all playable notes in the game

    //Field variables for files
	public Image[] images;  //Array of all images used in the game
    public File[] audioFiles;   //Array of all audio files used in the game

    //Field variables for objects
    public GamePanelHolder gph;
    public Scanner musicScanner; 
    
    public PrintWriter output;
    public File outFile;

    //Field variables for layout
    public CardLayout cl;  //Main CardLayout to switch between the home, game, instructions, and settings

    public Game()   //Constructor to initialize variables
    {
        currentScore = 0;	//Saves the current score
        
        //Below is an array of all notes
        noteNames = new String[]{"error", "A3", "B3", "C4", "D4", //G string excluding note G3
            "E4", "F4", "G4","A4",  //D string
            "B4", "C5", "D5", "E5", //A string
            "F5", "G5", "A5", "B5", "C6", "D6", "E6", "F6"};    //E string
        cl = new CardLayout();	//The card layout to switch between main panels, e.g home page/settings
        
        //Calls Methods to read all files
        getMyImage();	
        getMyAudioFiles();
        getMyFiles();
        getMyMusicScanner();

        //Initialize game statistics
        gameMode = "song";	//Stores the gamemode selected by the user
        songName = ""; //Default song name for testing
        notesCountTotal = 0;
        noteCorrectTotal = 0;
        gameInSession = false; //Game is not in session at the start
        hint = true;

        gph = new GamePanelHolder(this); //Initialize GamePanelHolder instance

    }

    public static void main(String[] args)  //Main
    {
        Game g = new Game();    //New instance of Game
        g.start();  //Starts the game
    }

    //Creates JPanel and GamePanelHolder, which starts the game
    public void start() 
    {

        //Creates the JFrame
        JFrame frame = new JFrame("Javiolin");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        //Adds the GamePanelHolder to the JFrame
        frame.getContentPane().add(gph);
        frame.setVisible(true); //Sets the JFrame to be visible
    }
    
    //Reads all images from the Images folder
   	public void getMyImage() 
	{
        //Array of all image file names
		String[] imageName = new String[]{"settings.png", "background1.png", "title.png", "next.png", "selection.png", "settings.png", 
            "back.png", "fingerboard.png", "instructionsHome.png", "pause.png", "home.png", 
            "timer.png", "note.png", "character1.png", "selectionscreen.png", "S.png",
            "A.png", "B.png", "C.png", "D.png", "F.png",
            "chatBox.png", "feedbackKey.png", "feedbackTitle.png", "songBox.png", "instructions.png",
            "feedbackButton.png" 
            };

        //Array to store all images
		images = new Image[imageName.length];

        //Loop to try and read all images from the Images folder and saves them in an images array
		for(int i = 0; i < imageName.length; i++)
		{
            //Catches any IOExceptions if the image file is not found
			try 
			{
				images[i] = ImageIO.read(new File("Images/" + imageName[i]));
			}
			catch(IOException e)
			{
				System.err.println("\n\n\n" + imageName[i] + " can't be found.\n\n");
				e.printStackTrace();
			}
		}
	}

    //Reads all audio files from the Audio folder
    public void getMyAudioFiles()
    {
        audioFiles = new File[noteNames.length];
        for(int i = 0; i < noteNames.length; i++)
        {
            try
            {
                audioFiles[i] = new File("Audio/" + noteNames[i] + ".wav");
            }
            catch(Exception e)
            {
                System.err.println("\n\n\n" + noteNames[i] + " can't be found.\n\n");
                e.printStackTrace();
            }
        }
    }

    //Reads all text files from the Data folder
    public void getMyFiles()
    {
        //Array of all text file names
        String[] fileNames = new String[]{"music.txt", "highscore.txt"};

        //Loops through all text files and tries to reads them
        for(int i = 0; i < fileNames.length; i++)
        {
            //Catches any IOExceptions if the text file is not found
            try 
            {
                File musicFile = new File("Data/" + fileNames[i]);
            }
            catch(Exception e)
            {
                System.err.println("\n\n file can't be found.\n\n");
                e.printStackTrace();
            }
        }
    }

    //Creates a scanner to read the music.txt file
    public void getMyMusicScanner()
    {
        //Try to create a scanner to read the music.txt file, catches any IOExceptions if the file is not found
        try
        {
            musicScanner = new Scanner(new File("Data/music.txt"));
        }
        catch (IOException e)
        {
            System.err.println("Error loading music.txt");
            e.printStackTrace();
            System.exit(1);
        }
    }

    //Resets the music scanner, used when the game is restarted
    public void resetMusicScanner()
    {
        //Try to close the existing scanner and reinitialize it to read the music.txt file again
        try
        {
            if (musicScanner != null)
            {
                musicScanner.close(); // Close the existing Scanner
            }
            musicScanner = new Scanner(new File("Data/music.txt")); // Reinitialize the Scanner
        }
        catch (IOException e)
        {
            System.err.println("Error resetting music.txt");
            e.printStackTrace();
            System.exit(1);
        }
         try
		{
			 outFile= new File("temp.txt");
			output = new PrintWriter("temp.txt");
		}
		catch(IOException e)
		{
			System.err.println("\n\n\nERROR: Cannot create"+"temp.txt"+"file.\n\n\n");
			System.exit(2);
		}
		
		
		String line = "";
		boolean end= true;
		while(musicScanner.hasNext()&&end)
		{
			line=musicScanner.nextLine();
			if(line.equals("freeplay"))
			{
				end=false;
			}
			output.println(line);
		}
		
		for(int i=0;i<=120;i++)
		{
			int rand = (int)(Math.random()*noteNames.length-1)+1;
			char randc = (char) rand;
			int rand2 = (int)(Math.random()*2)+1;
			output.print(noteNames[rand]+"("+ rand2+") 1 ");

			if (i%8==0&&i!=0)
			{
				output.println();
			}
		}
		output.print("end");
		output.close();
		outFile.renameTo(new File("Data/music.txt"));
		
    }	


    //Below I wrote many getter setter methods for all the field variables in the class,
    //so that I can access all of them from other classes. 

    //All the field variables usually have a get method, which returns the variable,
    //and a set method, which accepts a parameter of the same data type as its corresponding
    //field variable and sets the field variable to that parameter.

    // Getters and Setters for statistics
    public int getAccuracyPercent()
    {
        return accuracyPercent;
    }

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public int getNotesCountTotal()
    {
        return notesCountTotal;
    }

    public void addNotesCountTotal(int notesCountTotalIn)
    {
        notesCountTotal += notesCountTotalIn;
        lifetimeNotesTracker += notesCountTotalIn;
        
        //Note: notesCountTotal is used to calculate accuracy, while lifetimeNotesTracker
        //is a variable to track how many notes the user played so far to act as a milestone,
        //and both can be seperately reset so they do not interfere with accuracy calculations.
    }
    
    public void resetNotesCountTotal()
    {
		notesCountTotal = 0;
	}
    
    public int getNoteCorrectTotal()
    {
		return noteCorrectTotal;
	}
	
	public void addNoteCorrectTotal(int noteCorrectTotalIn)
	{
		noteCorrectTotal += noteCorrectTotalIn;
	}
	
	public int getLifetimeNotes()
	{
		return lifetimeNotesTracker;
	}
	
	public void resetLifetimeNotes()
	{
		lifetimeNotesTracker = 0;
	}
	
    public void resetNotesCorrectTotal()
    {
		noteCorrectTotal = 0;
	}

    public int getCurrentScore()
    {
        return currentScore;
    }

    public void setCurrentScore(int currentScore)
    {
        this.currentScore = currentScore;
    }

    // Getters and Setters for game configurations
    public int getGameDuration()
    {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration)
    {
        this.gameDuration = gameDuration;
    }

    public String getGameMode()
    {
        return gameMode;
    }

    public void setGameMode(String gameMode)
    {
        this.gameMode = gameMode;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
    }
    
    public boolean getHint()
    {
		return hint;
	}
	
	public void setHint(boolean hintIn)
	{
		hint = hintIn;
	}

    // Getters and Setters for game state

    public boolean isOneRoundOver()
    {
        return oneRoundOver;
    }

    public void setOneRoundOver(boolean oneRoundOver)
    {
        this.oneRoundOver = oneRoundOver;
    }

    public boolean isGameInSession()
    {
        return gameInSession;
    }

    public void setGameInSession(boolean gameInSession)
    {
        this.gameInSession = gameInSession;
    }

    // Getters for information
    public String[] getNoteNames()
    {
        return noteNames;
    }

    // Getters for files
    public Image[] getImages()
    {
        return images;
    }

    public File[] getAudioFiles()
    {
        return audioFiles;
    }

    // Getters and Setters for classes
    public GamePanelHolder getGamePanelHolder()
    {
        return gph;
    }

    public void setGamePanelHolder(GamePanelHolder gph)
    {
        this.gph = gph;
    }

    public Scanner getMusicScanner()
    {
        return musicScanner;
    }

    public void setMusicScanner(Scanner musicScanner)
    {
        this.musicScanner = musicScanner;
    }

    // Getters for layout
    public CardLayout getCardLayout()
    {
        return cl;
    }

}
