//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//GamePanel.java
//
//This class is the main game panel of the game. It contains all the game logic, including the game timer, note dropping, and score tracking.
//I will implement key press logic/note guessing next week.

//toimplement: "e" instead of e5 in hints, baseline 0 points, and catch lowercase e

//Imports
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException; 
import java.io.FileNotFoundException; 
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.Timer;

//Class header
public class GamePanel extends JPanel
{
    //All field variables

    //Field variables for objects/panels
    private Game game;
    private GamePanelHolder gph;
    private loadingPanel loading;
    private mainGamePanel mainGame;
    private resultsPanel results;
    private FeedbackPanel feedback;
    private Scanner musicScanner;

    //Field variables for files
    private Image[] images;

    //Field variables for game stats
    private int currentScore;
    private double currentAccuracy;
    private int totalNoteCount;
    private int correctNoteCount;
    private String currentNote;
    private int[] missedNotes;
    private int[] correctNotes;

    //Field variables for game configuration
    private String songName;
    private int gameDuration;

    //Field variables for information
    private String[] noteNames;

    //Field variables for layout
    private CardLayout gamePages;


    //Constructor to initialize the GamePanel and add all the panels to the card layout
    public GamePanel(Game gameIn, GamePanelHolder gphIn)
    {
        //Initialize field variables
        game = gameIn;
        gph = gphIn;
        songName = game.getSongName();
        gameDuration = game.getGameDuration();
        currentScore = 0;
        musicScanner = game.getMusicScanner();
        noteNames = game.getNoteNames();
        images = game.getImages();
        currentNote = "N/A"; //Initialize currentNote to a default value
        
        gamePages = new CardLayout();	//Make a new card layout to switch between scenes of the game

        //Set layout to card layout
        setLayout(gamePages);

        //Create instances of all panels and add them to the card layout
        loading = new loadingPanel();
        mainGame = new mainGamePanel(this);
        results = new resultsPanel(this);
        feedback = new FeedbackPanel(this, images, noteNames);

        add(loading, "loading");
        add(mainGame, "main game");
        add(results, "results");
        add(feedback, "feedback");


        gamePages.show(this, "loading");    //Show the loading panel by default

        setFocusable(true); // Ensure the panel is focusable
        requestFocusInWindow(); // Request focus for the panel
        
    }

    //Shows the loading screen
    public void showLoadingScreen()
    {
        gamePages.show(this, "loading");    //Show the loading panel
        loading.selection();  //Start the selection screen
    }

    //Switches to a different scene (card)
    public void changeScene(String scene)
    {
        gamePages.show(this, scene);    //Switch to the specified card
    }

    //Loading panel with starts a transition to the main game
    class loadingPanel extends JPanel
    {
        private Timer timer;    //Timer for loading transition

        //Constructor
        public loadingPanel()
        {
            setLayout(null); //Set layout to null
            setBackground(new Color(30, 30, 30));   //Set background color to approximately black
        }

        //Waits for a second before switching to the main game
        public void startTransition()   
        {			
            //Resets timer
            if (timer != null && timer.isRunning())
            {
                timer.stop();   //Stop the timer if it is already running
            }

            //Create a new timer to wait for 1 second
            timer = new Timer(1000, new LoadingTransitionHandler());
            game.setGameInSession(true); //Set gameInSession to true when loading the game
            timer.setRepeats(false);    //Ensure the timer only triggers once
            timer.start();  //Start the timer
        }

        //ActionListener for the loading transition
        class LoadingTransitionHandler implements ActionListener
        {
            //When the timer triggers, switch to the main game
            public void actionPerformed(ActionEvent e)
            {
                boolean gameInSession = game.isGameInSession(); //Check if the game is in session
                timer.stop();
                if (gameInSession) //Ensure the game is in session before proceeding
                {
                    changeScene("main game");   //Switch to the main game panel
                    mainGame.startGame(); //Start the game (timer and notes)
                }
            }
        }

    public void selection()
    {

        String[] songOptions = {
          "test",
          "Ode To Joy",
          "Minuet in G",
          "Twinkle Twinkle Little Star",
          "Demo - Easy Twinkle Twinkle Little Star",
          "freeplay (practice with random notes!)"
        };

        JList<String> songList = new JList<>(songOptions);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setVisibleRowCount(6);

        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setBounds(360, 270, 300, 150); // position and size
        add(scrollPane);
        
        JButton songSelectButton = new JButton("Select this song");
        songSelectButton.setBounds(360, 500, 300, 50);
        
        add(songSelectButton);

        class songListHandler implements ListSelectionListener
        {
          public void valueChanged(ListSelectionEvent e)
          {
            if (!e.getValueIsAdjusting())
            {
              String selected = songList.getSelectedValue();
              game.setGameMode("song");

              if (selected.equals("test"))
              {
                songName = "test";
              }
              else if (selected.equals("Ode To Joy"))
              {
                songName = "Ode";
              }
              else if (selected.equals("Minuet in G"))
              {
                songName = "Minuet";
              }
              else if (selected.equals("Twinkle Twinkle Little Star"))
              {
                songName = "twinkle";
              }
              else if (selected.equals("Demo - Easy Twinkle Twinkle Little Star"))
              {
                songName = "demo";
              }
              else if (selected.contains("freeplay"))
              {
                songName = "freeplay";
                game.setGameMode("freeplay");
              }
            }
          }
        }
        
        class songSelectHandler implements ActionListener
        {
			public void actionPerformed(ActionEvent evt)
			{
				if(!songName.equals(""))
				{	
					game.setSongName(songName);
					startTransition();
				}
			}
		}
		
		songListHandler lsl = new songListHandler();
        songList.addListSelectionListener(lsl);
        
        songSelectHandler ssh = new songSelectHandler();
        songSelectButton.addActionListener(ssh);
        
    }

		//Draws the background image
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);    //Clear the panel
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this); //Draw the background image

        } 
    }

    //Main game panel that contains the game logic and graphics
    class mainGamePanel extends JPanel
    {
        //All field variables

        //Field variables for objects/components
        private AudioPlayer audio;
        private Timer gameTimer;
        private mainGamePanel mainGame;
        private noteDestination[] noteDest;
        private noteFall[] notes;
        private infoBarPanel infoBar;
        private statsBarPanel statsBar;
        private JLabel timerLabel; // Label to display the time
        private GamePanel gp;
        private NoteDropperHandler dropNextNote;

        //Field variables for game state
        private int timeLeft = 10; // Time left in seconds

        //Field variables for files
        private Image noteImage; // Image for the note

        //Field variables for info
        private int x;
        private int arrayIndex;
        private int y;
        private int noteLength;
        private int noteX;
        private int noteY;
        private int rng;
        private int freeplayNoteID;
        private String previousNote;
        private boolean gamePaused;   
        private boolean gameContinued;   

        //Constructor
        public mainGamePanel(GamePanel gpIn)
        {
			audio = new AudioPlayer(game.getNoteNames(), game.getAudioFiles());
			gp = gpIn;
            mainGame = this;    //Set the main game panel instance
            gamePaused = false;
            gameContinued = false;

            setLayout(new BorderLayout());  //Set layout to border layout
            setOpaque(false);

            //Make an instance of the stats bar and info bar, sets their size, and adds them to the panel
            statsBar = new statsBarPanel();
            statsBar.setPreferredSize(new Dimension(getWidth(), 50));
            infoBar = new infoBarPanel();
            infoBar.setPreferredSize(new Dimension(getWidth(), 50));
            add(infoBar, BorderLayout.NORTH);
            add(statsBar, BorderLayout.SOUTH);

            noteImage = images[12]; //Ensures efficient access to images
            
            //Initializes arrays for all the note objects
            noteDest = new noteDestination[100];
            notes = new noteFall[100];
            

            setFocusable(true); // Make the panel focusable to receive key events
        }

        //Starts the game
        public void startGame()
        {
            String gameMode = game.getGameMode(); //Get the game mode
            missedNotes =  new int[noteNames.length];
            correctNotes = new int[noteNames.length];
            gamePaused = false;
            correctNoteCount = 0;
            totalNoteCount = 0;
            currentNote = "";
            infoBar.repaint();
            statsBar.repaint();
            
            noteDest = new noteDestination[100];
            notes = new noteFall[100];
             
            if(!gameMode.equals("song"))    //Check if the gamemode is not song, and is Freeplay mode
            {
				freeplayNoteID = 0; //Reset note ID for freeplay mode
                gameDuration = game.getGameDuration(); //Set the game duration to 10 seconds
                startGameTimer(); //Start the 10-second timer
                game.setGameMode("freeplay");                
                generateRandomNotesForFreeplay();
            }
            else
				readMusicFileAndDropNotes(mainGame); //Start dropping notes

        }

        //Starts the game timer in freeplay mode
        public void startGameTimer()
        {
            timeLeft = gameDuration; // Reset the timer to the intended game duration in seconds
            //Stops timer 
            if (gameTimer != null && gameTimer.isRunning())
            {
                gameTimer.stop();   //Stop the timer if it is already running
            }

            //Create a new timer to count down the time
            class timerHandler implements ActionListener
            {
                //When the timer triggers, update the time left and repaint the panel
                public void actionPerformed(ActionEvent e)
                {
                    timeLeft--; //Subtracts a second from the time display

                    //Checks if the time is over
                    if (timeLeft <= 0)
                    {
                        gameTimer.stop();
                        changeScene("results"); //Switch to results page after timer ends
                    }
                    statsBar.repaint(); //Updates the timer display
                }
            }

            timerHandler th = new timerHandler(); // Create a new timer handler
            gameTimer = new Timer(1000,th); // Create a new timer to count down the time, repainting every 1 second
            gameTimer.setRepeats(true);   // Ensure the timer repeats
            gameTimer.start();  // Start the timer
        }

        //Stops the game timer
        public void stopGameTimer()
        {
            //Stops timer if the timer exists
            if (gameTimer != null && gameTimer.isRunning())
            {
                gameTimer.stop();   //Stop the timer if it is already running
            }
        }

		public void generateRandomNotesForFreeplay()
		{
			Timer noteWait = new Timer(1000, null);
			noteWait.setRepeats(false);

			class FreeplayNoteHandler implements ActionListener
			{

				public void actionPerformed(ActionEvent e)
				{
					if (timeLeft > 0 && game.isGameInSession()) // Ensure the game is still running
					{
						System.out.println(freeplayNoteID + "note dest id");
						int randomIndex = 0;
						
						while(randomIndex == 0)
							randomIndex = (int) (Math.random() * noteNames.length); // Generate a random note
							
						String randomNote = noteNames[randomIndex];
						int randomDuration = 1 + (int) (Math.random() * 3); // Random duration between 1 and 3 seconds
						int randomWaitTime = 1000 + (int) (Math.random() * 2000); //Random wait time between 1 and 3 seconds (in ms)

						if(randomDuration < timeLeft)
						{
							currentNote = randomNote;
							dropNoteAcrossScreen(mainGame, randomNote, randomDuration, freeplayNoteID); // Drop the random note
						
							freeplayNoteID++;
						
							noteWait.setInitialDelay(randomWaitTime); // Set next wait time
							noteWait.restart(); // Wait before next drop
						}
						
						infoBar.repaint();
						statsBar.repaint();
						results.updateResults();
						
					}
					else
					{
						results.updateResults();	//Updates all the labels in the results panel to show the correct stats of the user	
						System.out.println("results updated");					
						gp.changeScene("results");	//Go to results screen
						noteWait.stop(); // Stop note waiting/generatint timer
					}
				}
			}

			FreeplayNoteHandler fnh = new FreeplayNoteHandler();
			noteWait.addActionListener(fnh);
			noteWait.start(); // Start waiting before the first note
		}
		
        //Reads the music file and drops notes
        public void readMusicFileAndDropNotes(mainGamePanel mainGameIn)
        {
            game.resetMusicScanner(); // Ensure the Scanner is reset before use
            game.getMyMusicScanner(); // Get the Scanner for reading music files
            
            game.getMyMusicScanner();	//Resets Scanner
            musicScanner = game.getMusicScanner(); //Get the Scanner for reading music files

            String name;

            mainGamePanel mainGameInstance = mainGameIn;    //Initializes the main game instance

            for (int i = 0; i < noteDest.length; i++) //When game starts, all the note objects are null
            {
                noteDest[i] = null; //Sets all note objects to null
            }
			
            statsBar.resetScore();

            Timer noteTimer = new Timer(0, null); //Timer for dropping notes
            noteTimer.setRepeats(false); //Ensure the timer only triggers once per note

            do
            {
                name = musicScanner.next(); // Read the next word
            } while (!name.equals(songName)); // Skip to the specified song

            System.out.println("Starting to drop notes for song: " + name);

            // Create an ActionListener for dropping notes
            dropNextNote = new NoteDropperHandler(mainGameInstance, noteTimer, musicScanner, infoBar);
            noteTimer.addActionListener(dropNextNote);
            noteTimer.start(); // Start the note dropping process
        }

        //Drops a note across the screen
        public void dropNoteAcrossScreen(mainGamePanel gamePanelIn, String note, int durationIn, int noteDestIDIn) //Triggers events that cause a note fall in drawDestination
        {
            System.out.println("Dropping note: " + note);   
            drawDestination(gamePanelIn, note, durationIn, noteDestIDIn); // Update the destination rectangle location

        }

        //Draws the destination rectangle for the note
        public void drawDestination(mainGamePanel gamePanelIn, String note, int durationIn, int noteDestIDIn)
        {
            //Declare local variables
            String savedNote;
            int duration;
            int section;	//Finds which block location the destination rectangle is at
            
            //Initialize variables
            duration = durationIn;
            x = 525;
            y = 211; //Default y position for the destination rectangle
            noteLength = 48;
            section = -1;
            
            //Prevents same back-to-back notes from switching strings, to help regulate tempo correctly.
            if(!note.equalsIgnoreCase(previousNote))
				rng = (int)(Math.random() * 2 );    //To solve confusion between notes available on both strings and improve variation
            
            //The rng value (between 0 and 1), is used to to decide whether the note should be placed on what string in case it is playable on two strings,
            //and you can see that in the for loop below
            
            //Loops through the length of an array of all note names to decide where to place the note on the fingerboard
            for(int i = 0; i < noteNames.length; i++)
            {
                if(note.equals(noteNames[i]))// Checks if the name of the current note scanned equals a note in the note names array
                {
                    if(i <= 4)	//The note can only be on the G string
                    {
                        x = 749; //G String X location

                        y += (i - 1) * 52;  //Moves down string to find correct Y value
                        section = i;
                    }
                    else if(i <= 8 && i > 4) //Technically playable on G and D string
                    {
                        if(rng == 0)
                        {
                            x = 749;    //G string X location
                            y += (i - 1) * 52;  //Moves down string to find correct Y value
                            section = i;
                        }
                        else
                        {
                            x = 763;    //D string  X location
                            y += (i - 5) * 52;  //Moves down string to find correct Y value
                            section = i - 4;
                        }

                    }
                    else if(i <= 12 && i > 8)   //Technically playable on D and A string
                    {
                        if(rng == 0)
                        {
                            x = 763;    //D string X location
                            y += (i - 5) * 52;  //Moves down string to find correct Y value
                            section = i - 4;
                        }
                        else
                        {
                            x = 776;    //A string X location
                            y += (i - 9) * 52;  //Moves down string to find correct Y value
                            section = i - 8;
                        }
                    }
                    else if(i <= 16 && i > 12)  //Technically playable on A and E string
                    {
                        if(rng == 0)
                        {
                            x = 776;    //A string location X location
                            y += (i - 9) * 52;  //Moves down string to find correct Y value
                            section = i - 8;
                        }
                        else
                        {
                            x = 789;    //E string location X location
                            y += (i - 13) * 52;  //Moves down string to find correct Y value
                            section = i - 12;
                        }
                    }
                    else //Only E string is possible
                    {
                        y += (i - 13) * 52;  //Moves down string
                        x = 789;	//E string X location
                        section = i - 12;
                    }
                }
            }

            //Creates new noteDestination object
            System.out.println(section + "  section");
            previousNote = note;	//Keeps track of previous note to prevent the next same note hopping strings (Which messes up the tempo)
            noteDest[noteDestIDIn] = new noteDestination(gamePanelIn, note, noteDestIDIn, x, y, duration, section);
            repaint();  //Repaint the panel to update the destination rectangle
        }

        //Separate ActionListener class for dropping notes
        class NoteDropperHandler implements ActionListener
        {
            //Field variables
            private mainGamePanel gamePanel;
            private Timer noteTimer;
            private Scanner musicScanner;
            private int delay;
            private int duration;
            private int noteDestID;
            private infoBarPanel infoBar;
            private int rest;
            private String noteInfo;

            //Constructor
            public NoteDropperHandler(mainGamePanel gamePanelIn, Timer noteTimer, Scanner musicScanner, infoBarPanel infoBarIn)
            {
                //Initialize field variables
                gamePanel = gamePanelIn;
                this.noteTimer = noteTimer;
                this.musicScanner = musicScanner;
                noteDestID = 0;
                infoBar = infoBarIn;
                
            }
            
            public void pauseTimer()
            {
				noteTimer.stop();
			}
			
			public void continueTimer()
			{
				noteTimer.start();
			}

            //Action performed when the timer triggers
            public void actionPerformed(ActionEvent e) 
            {
                //Check if the scanner has more notes to read, and the game session has not ended
                if (musicScanner.hasNext() && game.isGameInSession()) 
                {
					
                    String noteInfo = musicScanner.next(); //Read the next note
                    
                    //Ensures the note is in correct format and the song has not ended
                    if (noteInfo.length() >= 2 && !noteInfo.equals("end")) 
                    {
                        String note = noteInfo.substring(0, 2); //Store the current note
                        duration = Integer.parseInt(noteInfo.substring(noteInfo.indexOf("(") + 1, noteInfo.indexOf(")"))); //Read the duration

                        currentNote = note; //Update the current note
                        infoBar.repaint(); //Update the hint label with the current note
                        noteDestID++;   //Increment the note destination ID

                        //Resets the note destination ID if it larger than the array length, so ID starts back at 0
                        if(noteDestID >= noteDest.length)
                        {
                            noteDestID = 0; //Reset the note destination ID
                        }

                        //Check if the scanner has a duration between two notes
                        if (musicScanner.hasNextInt())
                        {
                            rest = musicScanner.nextInt(); //Read the rest
                            delay = rest * 1000; //Convert seconds to milliseconds

                            gamePanel.dropNoteAcrossScreen(gamePanel, note, duration, noteDestID);  //Drop the note across the screen


                            //Schedule the next note drop
                            noteTimer.setInitialDelay(delay);
                            noteTimer.restart();

                        } 
                        else 
                        {
                            //Drop the note immediately if no duration is specified
                            gamePanel.dropNoteAcrossScreen(gamePanel, note, delay, noteDestID);
                            noteTimer.setInitialDelay(0);   //No delay
                            noteTimer.restart();
                        }
                    }
                    else  //Ends the game if the note format is incorrect
                    {
						noteTimer.stop();	//Stops timer to stop dropping notes
						gp.changeScene("results");	//Go to results screen
						results.updateResults();	//Updates all the labels in the results panel to show the correct stats of the user
					}
                }
                else  //Ends the game if there are no notes left
                {
                    noteTimer.stop(); //Stop the timer when all notes are processed
                    gp.changeScene("results");	//Go to results screen
                    results.updateResults();	//Updates all the labels in the results panel to show the correct stats of the user
                }
            }
        }

        //Paints the main game panel
        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);    //Clear the panel
            
            //Draw the background images
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
            g.drawImage(images[7], 550, 50, 450, 675, this);
            g.drawImage(images[13], 100, 210, 340, 400, this);
            g.setColor(Color.GREEN);    //Defaultly set color to green
        
            //Loops through the note destination array and draws the notes
            for (int noteID = 0; noteID < noteDest.length; noteID++) 
            {
                noteDestination noteDestObject = noteDest[noteID]; //Get the destination note object from the array
                noteFall noteFallObject = notes[noteID]; //Get the falling note object from the array
        
                if (noteDestObject != null) 
                { // Check if noteDestObject is not null for testing
                    boolean noteEnded = noteDestObject.getState();  //Check if the note has ended

                    //Initializes all the variables for the note
                    int noteFallX = 0;
                    int noteFallY = 0; 
                    char guess = '?';
                    char note = '!';
                    
                    //Stores information of the note destination	
                    int noteX = noteDestObject.getX();
                    int noteY = noteDestObject.getY();
                    int duration = noteDestObject.getDuration();
        
					g.setColor(Color.GREEN);	//Defaultly color is green (to signify note is to be played)
					
                    if (noteFallObject != null) 	//Check if noteFallObject is not null
                    { 
						if(gamePaused)
						{
							noteDestObject.pauseDestTimer();
							noteFallObject.pauseFall();						
						}
						
						if(gameContinued)
						{
							noteDestObject.resumeTimer();
							noteFallObject.resumeFall();						
						}

                        //Initialize note fall variables to store the location of the falling note
                        noteFallX = noteFallObject.getX();
                        noteFallY = noteFallObject.getY();

                        if(noteFallY >= noteY - noteLength && noteFallY <= noteY + noteLength)	//Only checks note if note falling is within range of note destination
                        {
							noteFallObject.setGuessOn();
							guess = noteFallObject.getGuess();	//Gets what the user last input
							note = noteFallObject.getNote();	//Gets what the note should be
                        
                        
							if(guess == note && !(noteFallY > (noteLength + noteY)))	//Checks if note is guessed correctly and in range 
							{
									noteDest[noteID].noteCorrectlyGuessed();	//Marks note as correctly guessed
							}
                        
							if(noteDest[noteID].noteIsCorrect())	//Checks if the note is marked as correctly guessed
							{							
							
								if(!noteFallObject.getScoreDone())  //Check if the score has not been updated already
								{
									//Updates game statistics to help calculate accuracy in StatsBarPanel
									totalNoteCount++;
									correctNoteCount++;
									
									statsBar.updateScore(50);  //Update the score if note correctly guessed
									
									audio.play(noteDestObject.getNote() ,duration);
									
									
									for(int y = 0; y < noteNames.length; y++)
									{
										if(noteDestObject.getNote().equals(noteNames[y]))
										{
											System.out.println(noteNames[y] + "SAME CORRECT");
											correctNotes[y] ++;
										}
									}
									
									//for(int x = 0; x < correctNotes.length; x++)
										//System.out.println(noteNames[x] + "TOTAL CORRECT: " + correctNotes[x]);
									noteFallObject.setScoreDone();	//Avoid duplicate scoring
								}
							
								g.setColor(Color.CYAN);	//Sets color to cyan to mark a correct note
							}
						}
						else if(noteFallY > (noteLength + noteY) && !noteDest[noteID].noteIsCorrect())    //Checks if note falling is guessed incorrectly and bypasses the note destination and updates score
						{
								if(!noteFallObject.getScoreDone())  //Check if the score has not been updated already
								{
									totalNoteCount++;	//Updates game statistics to help calculate accuracy in StatsBarPanel
									statsBar.updateScore(-10);  //Update the score if the note is missed
									audio.play("error", 2000);
									
									for(int y = 0; y < noteNames.length; y++)
									{
										if(noteDestObject.getNote().equals(noteNames[y]))
										{
											System.out.println(noteNames[y] + "NOT CORRECT");
											missedNotes[y] ++;
										}
									}									
								}

								g.setColor(Color.RED);   //Set color to red to paint the destination rectangle if it is missed
								noteFallObject.setScoreDone(); //Set scoreDone to true to avoid duplicate scoring
						}

                        //Draw the destination note, chdcks if the note has ended 
                        if(!noteEnded && noteDestObject != null)
                            g.fillRect(noteX, noteY, 12, noteLength); //Draw the destination rectangle
                        
                        g.drawImage(noteImage, noteFallX, noteFallY, 12, noteLength, this); // Draw the falling note
                        
                        if(noteEnded)
							noteDest[noteID] = null;
					}
                } 
            }
            
            if(gameContinued == true)
				gameContinued = false;
        }

        class noteDestination	//Object for note destinations, a new object is created each time a new note is read form the scanner
        {						//All notes are reset to null after the round ends

            //Field variables
            //Field variable for note information
            private int noteID;
            private int noteX;
            private int noteY;
            private int duration;
            private String note;
            
            //Field variables for note state
            private boolean noteEnd;
            private boolean noteCorrect;
            
            //Field variables for objects
            private Timer noteDuration;
            
            //Constructor
            public noteDestination(mainGamePanel mgp, String noteIn, int noteDestIDIn, int noteXIn, int noteYIn, int durationIn, int sectionIn)
            {
                //Initialize field variables
                noteCorrect = false;
                noteID = noteDestIDIn;
                noteX = noteXIn;
                noteY = noteYIn;
                note = noteIn;
                duration = durationIn * 1000; //Convert duration seconds to milliseconds
                noteEnd = false;

                notes[noteID] = new noteFall(note, noteX, noteY, duration, sectionIn); //Create a new falling note object
                
                //Nested handler to check when the note ends
                class TimerHandler implements ActionListener
                {
					//Updates once the duration ends
                    public void actionPerformed(ActionEvent evt)
                    {
                        noteEnd = true;	//Sets note ended as true
                        mgp.repaint();	//Repaints to clear the current note destination object from the screen
                    }
                }
                
                TimerHandler th = new TimerHandler();	//Makes a handler for the timer
                noteDuration = new Timer(duration * 2, th);	//Makes the timer that stops once the duration of the note is reached
                noteDuration.setRepeats(false); //Ensure the timer only triggers once
                noteDuration.start(); //Start the timer
            }
            
            public void pauseDestTimer()
            {
				noteDuration.stop();
			}
			
			public void resumeTimer()
			{
				noteDuration.start();
			}
            
            //Getter methods to access note destination information
            
            public int getID()
            {
                return noteID;
            }
            
            public int getX()
            {
                return noteX;
            }
            
            public int getY()
            {
                return noteY;
            }
            
            public int getDuration()
            {
                return duration;
            }
            
            public String getNote()
            {
				return note;
			}
            
            //Getter setter methods to access note destination state
            public boolean noteIsCorrect()
            {
				return noteCorrect;
			}
			
			public void noteCorrectlyGuessed()
			{
				noteCorrect = true;
			}
            
            public boolean getState()
            {
                return noteEnd;
            }
        } 

        class noteFall  //Object for falling notes
        {
            //D&I variables
            private int x; //Image x coordinate
            private int y;   //Image y coordinate
            private int speed; //Speed of fall in pixels per tick
            private char guess;	//Stores the user's guess
            private Timer noteTimer;
            private String note;    
            private boolean correctGuess;
            private boolean scoreDone; //To check if score has been updated already to avoid duplicated score counts
            private boolean guessOn;

            public noteFall(String noteIn, int xIn, int yIn, int durationIn, int sectionIn) 
            {
				int section; //The location of the note destination, helps ensure proportional falling speed in speed calculation
				int duration;	//duration of the note, helps calculate falling speed
				note = noteIn;
				note = note.substring(0, 1).toLowerCase();	//Gets note name 
				guess = '?';
                x = xIn; //Set the x coordinate
                y = 50; //Set the y coordinate
                section = sectionIn; //Set the section of the violin the note destination is at
                duration = durationIn;
                guessOn = false;	//Cannot guess until note reaches note dest bounds
                
                //speed = 750*blockdest/13				750/(durationIn/1000)/30; //Set the speed of the fall (pixels falling per tick) 13 blocks
                System.out.println(durationIn);
                speed = (int)(((section + 3) * (700 / 13.0) * 30.0) / duration);
                System.out.println(noteIn + "dropping at " + speed);
                //speed = 750/13*(durationIn/1000 + 3)/30;
                scoreDone = false; //Initialize scoreDone to false

                //Set up the Timer to update the position every 30ms
                noteTimer = new Timer(30, null); //Timer for falling notes
                noteTimer.setRepeats(true); //Ensure the timer repeats

				//Nested handler for the timer to update the Y location, which is basically an animation that shows the note falling
                class timerHandler implements ActionListener
                {
					//Updates y value for every period the timer ends
                    public void actionPerformed(ActionEvent e)
                    {
                        y += speed; // Move the rectangle down
                        
                        if (y > getHeight()) 	//Checks if the note has fell off screen
                        { 
                            noteTimer.stop(); // Stop the timer when the note goes off the screen
                        }
                        mainGame.repaint(); // Repaint the panel to update the rectangle's position
                    }
                }
                timerHandler th = new timerHandler(); //Create a new timer handler
                noteTimer.addActionListener(th); //Add the action listener to the timer
                startFalling();	//Calls a method to start the timer
                
				//Make a new handler to detect key presses and adds it to the object
                noteFallHandler nfh = new noteFallHandler();
                addKeyListener(nfh);
                
                //Gets focus to the window
                setFocusable(true);
                requestFocusInWindow();                
            }
            
            public void pauseFall()
            {
				noteTimer.stop();
			}
			
			public void resumeFall()
			{
				noteTimer.start();
			}

			//Getter methods to access note fall information
			
            public int getX()
            {
                return x;
            }
            public int getY()
            {
                return y;
            }
            
            public char getNote()
            {
				return note.charAt(0);
			}
            
            public char getGuess()
            {
				return guess;
			}

			//Getter setter methods to access note fall state
			
            public boolean getScoreDone()
            {
                return scoreDone;
            }

            public void setScoreDone()
            {
                scoreDone = true;
            }

			public boolean isCorrectGuess()
			{
				return isCorrectGuess();
			}
			
			public void setGuessOn()
			{
				guessOn = true;
			}
			
			
			//Starts the timer
            public void startFalling() 
            {
                noteTimer.start(); // Start the Timer manually
            }
            
            //Nested class to deal with key presses (Which are basically the user's guesses)
            class noteFallHandler implements KeyListener
            {
				//Gets the key the user pressed and stores it in a field variable called "guess"
				public void keyPressed(KeyEvent evt)
				{
					if(guessOn)	//Guesses only count once noteFall reaches note dest bounds
					{
						requestFocusInWindow();	//Requests focus once a key is pressed
						guess = evt.getKeyChar();	//Sets guess as the user's key press
						
						if(guess >= 'A' && guess <= 'Z')	//Checks if the user's input is uppercase
							guess += 32;	//Converts the uppercase character to lowercase
					}
				}
				
				//Unused methods part of the key listener interface
				public void keyReleased(KeyEvent evt){}				
				public void keyTyped(KeyEvent evt){}
			}
        }

		//Shows information about the current game session
        class infoBarPanel extends JPanel
        {
			//Field variables for components
            private JButton home;
            private JButton pause;
            private JLabel timerLabel;
            private JLabel hintLabel;
            private JLabel pauseLabel;

            public infoBarPanel()	//Constructor
            {	
                setBackground(Color.BLACK);	//Sets background to black
                setLayout(null);	//Sets layout to null

				//Makes a JButton to direct user back home 
                home = new JButton();
                home.setContentAreaFilled(false); 	//Make it transparent (So that a button image can be drawn over)
                home.setBorderPainted(false); 
                home.setFocusPainted(true); 
                home.setBounds(15, 5, 40, 40);
            
                pause = new JButton();
                pause.setContentAreaFilled(false); 	//Make it transparent (So that a button image can be drawn over)
                pause.setBorderPainted(false); 
                pause.setFocusPainted(true); 
                pause.setBounds(940, 5, 40, 40);

				//Make a JLabel that displays the time left in the game (if the game is in freeplay mode)
                timerLabel = new JLabel("Time Left: " + timeLeft + "s");
                timerLabel.setForeground(Color.WHITE);
                timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
                timerLabel.setBounds(100, 5, 200, 40);

				//Make a JLabel that displays the current note as a hint (if hints are toggled on)
                hintLabel = new JLabel("Hint: " + currentNote);
                hintLabel.setForeground(Color.WHITE);
                hintLabel.setFont(new Font("Arial", Font.BOLD, 16));
                hintLabel.setBounds(300, 5, 400, 40);
                

				//Make a JLabel that tells the user what to press to pause the game
                pauseLabel = new JLabel("Pause");
                pauseLabel.setForeground(Color.WHITE);
                pauseLabel.setFont(new Font("Arial", Font.BOLD, 16));
                pauseLabel.setBounds(890, 5, 400, 40);
                
                //Add all components to the panel
                add(pause);
                add(home);                
                add(timerLabel);
                add(hintLabel);        
                add(pauseLabel);       

                homeButtonHandler hb = new homeButtonHandler();	//Make a new instance of the handler for the home button
                pauseButtonListener pbl = new pauseButtonListener();
                home.addActionListener(hb); //Add the handler as an action listener to the home button
                pause.addActionListener(pbl);
            }

			//Updates labels and draws images
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);	//Clears the panel
                g.drawImage(images[9], 950, 10, 30, 30, this); // Draw pause.png on the home button
                g.drawImage(images[10], 20, 10, 30, 30, this); // Draw home.png on the home button
                g.drawImage(images[11], 65, 10, 30, 30, this); // Draw timer.png next to the timer label
                
                if(game.getGameMode().equals("freeplay"))
					timerLabel.setText("Time Left: " + timeLeft + "s"); // Update the timer label
				else
					timerLabel.setText("Timer Off for Song Mode"); // Update the timer label
                if(game.getHint())
					hintLabel.setText("Hint: " + currentNote.substring(0, 1)); // Update the hint label
				else
					hintLabel.setText("Hints: Off, Can Turn On in Settings");
            }
            
            class pauseButtonListener implements ActionListener
            {
				//Ends game session and returns user back home once pressed
                public void actionPerformed(ActionEvent e)
                {
					dropNextNote.pauseTimer();
					gamePaused = true;
					
					int choice = JOptionPane.showConfirmDialog(null, "Resume Game? (Please give the game time to load)",
                                                   "Game Paused", JOptionPane.DEFAULT_OPTION,
													JOptionPane.INFORMATION_MESSAGE);  
					if (choice == JOptionPane.OK_OPTION || choice == JOptionPane.CLOSED_OPTION)
					{
						dropNextNote.continueTimer();
						gamePaused = false;
						gameContinued = true;					
					}
                }
            }
           

			//Nested handler class for the home button
            class homeButtonHandler implements ActionListener
            {
				//Ends game session and returns user back home once pressed
                public void actionPerformed(ActionEvent e)
                {
					dropNextNote.pauseTimer();
					gamePaused = true;
					
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game? Stats won't save!",
                                                   "Game Paused", JOptionPane.OK_CANCEL_OPTION);  
					if (choice == JOptionPane.OK_OPTION)
					{
						stopGameTimer(); //Stop the timer when exiting the main game page
						game.setGameInSession(false); //Set gameInSession to false when returning to home                    
						gph.showCard("home"); //Navigate to the home page						          
						JOptionPane.showMessageDialog(null, "Round Over!");						
					}
					else
					{
						dropNextNote.continueTimer();
						gamePaused = false;
						gameContinued = true;
					}
                }
            }
        }
    }

	//Displays the user's stats in the main game panel
    class statsBarPanel extends JPanel
    {

		//Declare field variable for JLabels
        private JLabel scoreLabel;
        private JLabel accuracyLabel;
        
        public statsBarPanel()	//Constructor
        {
			//Sets background color and layout 
            setBackground(Color.BLACK);
            setLayout(null);

			//Initializes score label
            scoreLabel = new JLabel("Score:" + currentScore);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            scoreLabel.setBounds(100, 5, 200, 40);
            
            //Initializes accuracy label
            accuracyLabel = new JLabel("Accuracy: " + "N/A");
            accuracyLabel.setForeground(Color.WHITE);
            accuracyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            accuracyLabel.setBounds(300, 5, 200, 40);

			//Adds label to the panel
            add(scoreLabel);
            add(accuracyLabel);
        }
        
        //Resets all user stats (As game restarts)
        public void resetScore()
        {
			//Sets user stats to 0 
			currentScore = 0;
			currentAccuracy = 0.0;
			totalNoteCount = 0;
			correctNoteCount = 0;
			
			//Resets JLabels to default
			scoreLabel.setText("Score: " + currentScore);
			accuracyLabel.setText("Accuracy: N/A");
		}

		//Updates the current score and score JLabel
        public void updateScore(int score)
        {
            currentScore += score;	//Adds the score in parameters to the score variable
            scoreLabel.setText("Score: " + currentScore);	//Updates score label to show current score
            updateAccuracy();	//Calls updateAccuracy to make sure it is directly updated along with the score
        }
        
        //Updates the accuracy score and the accuracy JLabel
        public void updateAccuracy()
        {
			if(totalNoteCount != 0)	//Checks if there are more than 0 total notes to avoid math error (cannot divide by 0)
			{
				currentAccuracy = ((double)correctNoteCount/totalNoteCount) * 100;	//Basic formula to calculate accuracy, updates as game goes on
				accuracyLabel.setText("Accuracy: " + String.format("%.2f", currentAccuracy) + " %");	//Updates accuracy label to show current accuracy
			}																							//Utilizes format to display up to two decimal places of the accuracy
		}
    }

	//A panel which shows up after the main game ends, displaying the user's stats and results for the game
    class resultsPanel extends JPanel	
    {
		private Font title; //Font for title
		private Font buttonText;    //Font for button text
		private Font subtitle;  //Font for subtitle
		private JLabel scoreLabel;
		private JLabel accuracyLabelResults;
		private JLabel notesPlayedLabel;
		private gradePanel gp;
		private GamePanel gameP;
		private PrintWriter output;
		private Scanner lineReader;
		private File highscoreFile;
		private String savedLines;
		
		
        public resultsPanel(GamePanel gamePIn) // Constructor
        {
                highscoreFile = new File("Data/highscore.txt");
                savedLines = "";

                gameP = gamePIn;

                try 
                {
                        lineReader = new Scanner(highscoreFile);
                } 
                catch(FileNotFoundException e) 
                {
                        System.err.println("Error, line highscore.txt not found");
                        System.exit(1);
                }

                title = new Font("Arial", Font.BOLD, 100);
                buttonText = new Font("Arial", Font.PLAIN, 30);
                subtitle = new Font("Arial", Font.BOLD, 24); 

                setLayout(null);
                setOpaque(false);

                JPanel results = new JPanel();
                results.setLayout(new GridLayout(2, 2));
                results.setOpaque(false);
                results.setBounds(50, 50, 900, 625);
                add(results);

                gp = new gradePanel();
                results.add(gp);

                JPanel allStatsPanel = new JPanel();
                results.add(allStatsPanel);
                

                allStatsPanel.setBackground(new Color(0, 0, 0, 150));
                allStatsPanel.setLayout(new GridLayout(4, 1, 10, 10));

                scoreLabel = new JLabel("Score: " + currentScore);
                
                scoreLabel.setFont(subtitle);
                scoreLabel.setForeground(Color.WHITE);


                accuracyLabelResults = new JLabel("Accuracy: " + currentAccuracy);
                accuracyLabelResults.setFont(subtitle);
                accuracyLabelResults.setForeground(Color.WHITE);


                notesPlayedLabel = new JLabel(correctNoteCount + " out of " + totalNoteCount + " correct!");
                notesPlayedLabel.setFont(subtitle);
                notesPlayedLabel.setForeground(Color.WHITE);


                allStatsPanel.add(scoreLabel);
                allStatsPanel.add(accuracyLabelResults);
                allStatsPanel.add(notesPlayedLabel);

                JPanel rhp = new returnHomePanel();
                results.add(rhp);

                JPanel feedbackP = new feedbackPanel();
                results.add(feedbackP);
        }

        class gradePanel extends JPanel
        {
                private int gradeImageIndex;

                public gradePanel()
                {
                        setLayout(null);
                        setBackground(new Color(0, 0, 0, 150));
                        gradeImageIndex = 0;
                }

                public void paintComponent(Graphics g)
                {
                        super.paintComponent(g);
                        g.drawImage(images[gradeImageIndex], 125, 50, 200, 200, this);
                }

                public void updateGrade(int score)
                {
                        gradeImageIndex = score;
                        repaint();
                }
        }

        class feedbackPanel extends JPanel
        {
                public feedbackPanel()
                {
                        setLayout(null);
                        setBackground(new Color(0, 0, 0, 150));
                        
                        ImageIcon originalIcon = new ImageIcon(images[26]);
                        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);

                        JButton feedbackButton = new JButton(scaledIcon);
                        feedbackButton.setFont(buttonText);
                        feedbackButton.setContentAreaFilled(false);

                        feedbackButton.setFocusPainted(false);
                        feedbackButton.setBounds(100, 60, 200, 200);
                        add(feedbackButton);

                        feedbackButtonHandler fbh = new feedbackButtonHandler();
                        feedbackButton.addActionListener(fbh);
                }

                class feedbackButtonHandler implements ActionListener
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                gameP.changeScene("feedback");
                        }
                }

                public void paintComponent(Graphics g)
                {
                        super.paintComponent(g);
                }
        }

        class returnHomePanel extends JPanel
        {
                public returnHomePanel()
                {
                        setLayout(null);
                        setBackground(new Color(0, 0, 0, 150));                        

                        ImageIcon originalIcon = new ImageIcon(images[8]);
                        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);

                        JButton homeButton = new JButton(scaledIcon);
                        homeButton.setFont(buttonText);
                        homeButton.setContentAreaFilled(false);
                        homeButton.setFocusPainted(false);
                        homeButton.setBounds(100, 60, 200, 200);
                        homeButton.setToolTipText("Return to Home");
                        add(homeButton);

                        homeButtonHandler hbl = new homeButtonHandler();
                        homeButton.addActionListener(hbl);
                }

                class homeButtonHandler implements ActionListener
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                game.setGameInSession(false);
                                gph.showCard("home");
                                gph.updateStatsInHome();
                        }
                }

                public void paintComponent(Graphics g)
                {
                        super.paintComponent(g);
                }
        }

        public void updateResults()
        {
				char grade;
				grade = '?';
                System.out.println("updated djnkcsiannkwjsnfdjl");
                scoreLabel.setText("Score: " + currentScore);
                accuracyLabelResults.setText("Accuracy: " + String.format("%.2f", currentAccuracy) + " %");
                notesPlayedLabel.setText(correctNoteCount + " out of " + totalNoteCount + " correct!");
                game.addNotesCountTotal(totalNoteCount);
                game.addNoteCorrectTotal(correctNoteCount);
               

                savedLines = "";
                try 
                {
                        lineReader = new Scanner(highscoreFile);
                        while (lineReader.hasNextLine()) 
                        {
                                savedLines += lineReader.nextLine() + "\n";
                        }
                        lineReader.close();
                        
                } 
                catch (FileNotFoundException e) 
                {
                        System.err.println("Error: highscore.txt not found.");
                        System.exit(1);
                }

                try 
                {
                        PrintWriter output = new PrintWriter(highscoreFile);
                        output.print(savedLines);
                        output.println(currentScore);
                        output.close();
                } 
                catch (IOException e) 
                {
                        System.err.println("Error: Cannot write to highscore.txt");
                        System.exit(2);
                }

                if(currentAccuracy >= 95)
                {
					grade = 'S';
                    gp.updateGrade(15);
                }
                else if(currentAccuracy >= 90)
                {
					grade = 'A';
                    gp.updateGrade(16);
                }
                else if(currentAccuracy >= 80)
				{
					grade = 'B';
                    gp.updateGrade(17);
                }
                else if(currentAccuracy >= 70)
                {
					grade = 'C';
					gp.updateGrade(18);
				}
                else if(currentAccuracy >= 60)
                {
                    grade = 'D';					
                    gp.updateGrade(19);
				}
                else
                {
					grade = 'F';
                    gp.updateGrade(20);
                }
                     
                feedback.updateFeedback(correctNotes, missedNotes, grade);
        }

        public void paintComponent(Graphics g)
        {
                super.paintComponent(g);
                g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
        }

    }
}
