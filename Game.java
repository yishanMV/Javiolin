//Yishan Lin and Stanley Wang
//Javiolin
//Code v1, home page, game page, card layout demonstration

//Import layouts
import java.awt.BorderLayout;
import java.awt.CardLayout;

//import JComponents
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

//Import listeners
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Import graphics
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

//Import IO
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;


//Class header 
public class Game
{
    //All field variables

    //Field variables for statistics
	private int accuracyPercent;    //Stores the accuracy of how many notes the user got correctly
	private int notesCountTotal;

    //Field variables for game configurations
    private int gameDuration;
    private int rng;

    //Field variables for note objects

    //Field variables for game state
    private String currentNote;
	private boolean oneRoundOver;
    private boolean gameInSession = false; // Field to track if the game is in session

    //Field variables for information
    private String[] noteNames;

    //Field variables for files
	private Image[] images;
    private File[] audioFiles;

    //Field variables for classes
    private GamePanelHolder gph;
    private Scanner musicScanner; 

    //Field variables for layout
    private CardLayout cl;  //Main CardLayout to switch between the home, game, instructions, and settings

    public Game()
    {
        noteNames = new String[]{"N/A", "A3", "B3", "C4", "D4", //g string excluding g
            "E4", "F4", "G4","A4",  //d string
            "B4", "C5", "D5", "E5", //a
            "F5", "G5", "A5", "B5", "C6", "D6", "E6", "F6"};    //e
        currentNote = "N/A!";
        cl = new CardLayout();
        getMyImage();
        getMyAudioFiles();
        getMyFiles();
        getMyMusicScanner();
    }

    public static void main(String[] args)
    {
        Game g = new Game();
        g.start();
    }

    public void start()
    {
        JFrame frame = new JFrame("Javiolin");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100,100);
        frame.setResizable(false);

        gph = new GamePanelHolder();
        frame.getContentPane().add(gph);
        frame.setVisible(true);
        
    }

    class GamePanelHolder extends JPanel
    {
        private GamePanel gamePanel;

        public GamePanelHolder()
        {
            setLayout(cl);

            HomePanel hp = new HomePanel();
            gamePanel = new GamePanel();
            InstructionPanelHolder ip = new InstructionPanelHolder();
            SettingsPanel sp = new SettingsPanel();

            add(ip, "instructions");
            add(hp, "home");
            add(gamePanel, "game");
            add(sp, "settings");

            cl.show(this, "home");
        }

        public void showCard(String cardName)
        {
            cl.show(this, cardName);
        }

        public GamePanel getGamePanel()
        {
            return gamePanel;
        }
    }
    
   	public void getMyImage() 
	{
		String[] imageName = new String[]{"settings.png", "background1.png", "title.png", "paper.png", "selection.png", "loading.png", "homebutton.png", "fingerboard.png", "hotbar.png", "sprites.png", "home.png", "timer.png", "note.png"};
		images = new Image[imageName.length];
		for(int i = 0; i < imageName.length; i++)
		{
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

    public void getMyAudioFiles()
    {
        String[] audioNames = new String[]{};
        audioFiles = new File[audioNames.length];
        for(int i = 0; i < audioNames.length; i++)
        {
            try
            {
                audioFiles[i] = new File("Audio/" + audioNames[i]);
            }
            catch(Exception e)
            {
                System.err.println("\n\n\n" + audioNames[i] + " can't be found.\n\n");
                e.printStackTrace();
            }
        }
    }

    public void getMyFiles()
    {
        String[] fileNames = new String[]{"music.txt", "highscore.txt"};
        for(int i = 0; i < fileNames.length; i++)
        {
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

    public void getMyMusicScanner()
    {
        try
        {
            musicScanner = new Scanner(new File("Data/music.txt"));
        }
        catch (IOException e)
        {
            System.err.println("Error loading music.txt");
            e.printStackTrace();
        }
    }

    public void resetMusicScanner()
    {
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
        }
    }

    class HomePanel extends JPanel
    {
        Font title;
        Font buttonText;
        Font subtitle;

        public HomePanel()
        {
			
			Color WHITE = new Color(255, 255, 255);
			setBackground(WHITE);
            title = new Font("Arial", Font.BOLD, 100);
            buttonText = new Font("Arial", Font.PLAIN, 50);
            subtitle = new Font("Arial", Font.PLAIN, 20);

            setLayout(null);
            JButton play = new JButton(""); 
            play.setContentAreaFilled(false); 
            play.setBorderPainted(false); 
            play.setFocusPainted(true); 
            play.setFont(buttonText);

            JButton help = new JButton("");
            help.setContentAreaFilled(false);
            help.setBorderPainted(false);
            help.setFocusPainted(true);
            help.setFont(buttonText);

            JButton settings = new JButton("");
            settings.setContentAreaFilled(false);
            settings.setBorderPainted(false);
            settings.setFocusPainted(true);
            settings.setFont(buttonText);

            JLabel accuracy = new JLabel("Accuracy: N/A");
            accuracy.setFont(subtitle);
            JLabel notesPlayed = new JLabel("Notes Played: N/A");
            notesPlayed.setFont(subtitle);
            JLabel highscore = new JLabel("Highscore: N/A");
            highscore.setFont(subtitle);
            
            if(oneRoundOver)
            {
				accuracy.setText("Accuracy: %" + accuracyPercent);
				notesPlayed.setText("Notes Played: " + notesCountTotal);
			}

            play.setBounds(520, 308, 280, 90);
            help.setBounds(520, 406, 280, 90);
            
            accuracy.setBounds(275, 350, 200, 30);
            notesPlayed.setBounds(275, 375, 200, 30);
            highscore.setBounds(275, 400, 200, 30);

            settings.setBounds(520, 503, 280, 86);

            add(play);
            add(help);
            add(accuracy);
            add(notesPlayed);
            add(highscore);
            add(settings);
            
			play.addActionListener(new PlayButtonListener());
            help.addActionListener(new HelpButtonListener());
            settings.addActionListener(new SettingsButtonListener());
        }
        

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
            g.drawImage(images[2], 300, 50, 400, 100, this);
            g.drawImage(images[4], 225, 250, 600, 400, this); 
            g.setFont(subtitle);
            g.drawString("My Stats", 275, 325);
        }

        class PlayButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                gameInSession = true; // Set gameInSession to true when Play is pressed
                gph.showCard("game");
                gph.getGamePanel().showLoadingScreen();
            }
        }

        class HelpButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                gph.showCard("instructions");
            }
        }

        class SettingsButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                gph.showCard("settings");
            }
        }
    }

    class InstructionPanelHolder extends JPanel
    {
        private CardLayout infoPages;

        public InstructionPanelHolder()
        {
            infoPages = new CardLayout();
            setLayout(infoPages);
            
            JButton home = new JButton("home");
            add(home);
            
            
            class homeButtonHandler implements ActionListener
            {
				public void actionPerformed(ActionEvent evt)
				{
					gph.showCard("home");
				}
			}
			
			homeButtonHandler hbhandler = new homeButtonHandler();
			home.addActionListener(hbhandler);
        }


        class firstInstructionPanel extends JPanel
        {
            public firstInstructionPanel()
            {
                setLayout(null);
            }
        }
        class secondInstructionPanel extends JPanel
        {
            public secondInstructionPanel()
            {
                setLayout(null);
            }
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
        }



    }

    class GamePanel extends JPanel
    {
        private CardLayout gamePages;
        private loadingPanel loading;
        private mainGamePanel mainGame;
        private resultsPanel results;

        public GamePanel()
        {
            gamePages = new CardLayout();
            setLayout(gamePages);

            loading = new loadingPanel();
            mainGame = new mainGamePanel();
            results = new resultsPanel();

            add(loading, "loading");
            add(mainGame, "main game");
            add(results, "results");

            gamePages.show(this, "loading");
        }

        public void showLoadingScreen()
        {
            gamePages.show(this, "loading");
            loading.startTransition();
        }

        public void changeScene(String scene)
        {
            gamePages.show(this, scene);
        }

        class loadingPanel extends JPanel
        {
            private Timer timer;

            public loadingPanel()
            {
                setBackground(new Color(30, 30, 30));
            }

            public void startTransition()
            {
                if (timer != null && timer.isRunning())
                {
                    timer.stop();
                }
                timer = new Timer(1000, new LoadingTransitionHandler());
                timer.setRepeats(false);
                timer.start();
            }

            class LoadingTransitionHandler implements ActionListener
            {
                public void actionPerformed(ActionEvent e)
                {
                    timer.stop();
                    if (gameInSession) // Ensure the game is in session before proceeding
                    {
                        changeScene("main game");
                        mainGame.startGame(); // Start the game (timer and notes)
                    }
                }
            }
        }

        class mainGamePanel extends JPanel
        {
            private Timer gameTimer;
            private int timeLeft = 10; // Time left in seconds
            private JLabel timerLabel; // Label to display the time
            private Image noteImage; // Image for the note
            private int x;
            private int arrayIndex;
            private int y;

            private int noteLength;
            private int noteX;
            private int noteY;
            
            private noteDestination[] noteDest;
            private infoBarPanel infoBar;
            private statsBarPanel statsBar;
            
            

            public mainGamePanel()
            {
                setLayout(new BorderLayout());
                statsBar = new statsBarPanel();
                statsBar.setPreferredSize(new Dimension(getWidth(), 50));
				infoBar = new infoBarPanel();
                infoBar.setPreferredSize(new Dimension(getWidth(), 50));

                add(infoBar, BorderLayout.NORTH);
                add(statsBar, BorderLayout.SOUTH);

                noteImage = images[12]; // Easy access
                
                noteDest = new noteDestination[100];
            }

            public void startGame()
            {
                startGameTimer(); // Start the 10-second timer
                readMusicFileAndDropNotes(); // Start dropping notes
            }

            public void startGameTimer()
            {
                timeLeft = 10; // Reset the timer to 10 seconds
                if (gameTimer != null && gameTimer.isRunning())
                {
                    gameTimer.stop();
                }

                class timerHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        timeLeft--;
                        if (timeLeft <= 0)
                        {
                            gameTimer.stop();
                            changeScene("results"); // Switch to results page after timer ends
                        }
                        statsBar.repaint(); // Updates the timer display
                    }
                }
                timerHandler th = new timerHandler(); // Create a new timer handler
                gameTimer = new Timer(1000,th);
                gameTimer.setRepeats(true);
                gameTimer.start();
            }

            public void stopGameTimer()
            {
                if (gameTimer != null && gameTimer.isRunning())
                {
                    gameTimer.stop();
                }
            }

            public void readMusicFileAndDropNotes()
            {
				for(int i = 0; i < noteDest.length; i++)	//when game starts, all the note objects are null
				{
					noteDest[i] = null;
				}
                resetMusicScanner(); // Reset the Scanner before starting to drop notes
                Timer noteTimer = new Timer(0, null); // Timer for dropping notes
                noteTimer.setRepeats(false); // Ensure the timer only triggers once per note

                ActionListener dropNextNote = new NoteDropperHandler(this, noteTimer, musicScanner, infoBar);
                noteTimer.addActionListener(dropNextNote);
                noteTimer.start(); // Start the note dropping process
            }

            private void dropNoteAcrossScreen(String note, int durationIn, int noteDestIDIn) //Triggers events that cause a note fall in drawDestination
            {
                System.out.println("Dropping note: " + note);
                drawDestination(note, durationIn, noteDestIDIn); // Update the destination rectangle

            }

      /*noteNames = new String[]{"N/A", "A3", "B3", "C4", "D4", //g string excluding g
            "E4", "F4", "G4","A4",  //d string
            "B4", "C5", "D5", "E5", //a
            "F5", "G5", "A5", "B5", "C6", "D6", "E6", "F6"};    //e */ 
            public void drawDestination(String note, int durationIn, int noteDestIDIn)
            {
				//Field variables
				String savedNote;
				int stringLocation;
				int duration;
				
				//Initialize variables
				duration = durationIn;
				stringLocation = -1;
                System.out.println("Drawing destination for note: " + note);
                x = 525;
                y = 211; //Default y position for the destination rectangle
                noteLength = 48;
                rng = (int)(Math.random() * 2 );    //To solve confusion between notes available on both strings and improve variation
                
                
                System.out.println(rng + "");	//Testing
                
                
                for(int i = 0; i < noteNames.length; i++)
                {
                    if(note.equals(noteNames[i]))   //y start at 211, note =48, account for  ?3 gaps
                    {
                        if(i <= 4)
                        {
                            x = 749; //reset
                            //means g string, nothing different changed

                            y += (i - 1) * 52;  //moves down string

                            System.out.println("G string");
                            stringLocation = 0;
                        }
                        else if(i <= 8 && i > 4) //Technically playable on G and D string
                        {
                            if(rng == 0)
                            {
                                x = 749;    //G string
                                y += (i - 1) * 52;  //moves down string
                                System.out.println("G string");
                                stringLocation = 0;
                            }
                            else
                            {
                                x = 763;    //D string
                                y += (i - 5) * 52;  //moves down string
                                System.out.println("D string");
                                stringLocation = 1;
                            }

                        }
                        else if(i <= 12 && i > 8)   //Technically playable on D and A string
                        {
                            if(rng == 0)
                            {
                                x = 763;    //D string
                                y += (i - 5) * 52;  //moves down string
                                System.out.println("D string");
                                stringLocation = 1;
                            }
                            else
                            {
                                x = 776;    //A string
                                y += (i - 9) * 52;  //moves down string

                                System.out.println("A string");
                                stringLocation = 2;
                            }
                        }
                        else if(i <= 16 && i > 12)  //Technically playable on A and E string
                        {
                            if(rng == 0)
                            {
                                x = 776;    //A string
                                y += (i - 9) * 52;  //moves down string
                                System.out.println("A string");
                                stringLocation = 2;
                            }
                            else
                            {
                                x = 789;    //E string
                                y += (i - 13) * 52;  //moves down string
                                System.out.println("E string");
                                stringLocation = 3;
                            }
                        }
                        else //Only E string
                        {
                            System.out.println("E string");
                            y += (i - 13) * 52;  //moves down string
                            x = 789;
                            stringLocation = 3;
                        }
                    }
                    
					noteFall(note, stringLocation); // Call the method to animate the note falling
                }
                noteDest[noteDestIDIn] = new noteDestination(noteDestIDIn, x, y, duration);
                repaint();
            }

            public void noteFall(String note, int location)
            {
                System.out.println("Note falling: " + note);
            }

            // Separate ActionListener class for dropping notes
            class NoteDropperHandler implements ActionListener
            {
                private final mainGamePanel gamePanel;
                private final Timer noteTimer;
                private final Scanner musicScanner;
                private int delay;
                private int duration;
                private int noteDestID;
                private infoBarPanel infoBar;

                public NoteDropperHandler(mainGamePanel gamePanel, Timer noteTimer, Scanner musicScanner, infoBarPanel infoBarIn)
                {
                    this.gamePanel = gamePanel;
                    this.noteTimer = noteTimer;
                    this.musicScanner = musicScanner;
                    noteDestID = 0;
                    infoBar = infoBarIn;
                    
                }

                public void actionPerformed(ActionEvent e) 
                {
                    if (musicScanner.hasNext()) 
                    {
                        String note = musicScanner.next(); // Read the note name
                        currentNote = note; // Store the current note
                        infoBar.repaint(); // Update the hint label with the current note
                        noteDestID += 1;

                        if (musicScanner.hasNextInt())
                        {
                            duration = musicScanner.nextInt(); // Read the duration
                            delay = duration * 1000; // Convert seconds to milliseconds

                            gamePanel.dropNoteAcrossScreen(note, duration, noteDestID);


                            // Schedule the next note drop
                            noteTimer.setInitialDelay(delay);
                            noteTimer.restart();

                        } 
                        else 
                        {
                            // Drop the note immediately if no duration is specified
                            gamePanel.dropNoteAcrossScreen(note, delay, noteDestID);
                            noteTimer.setInitialDelay(0);
                            noteTimer.restart();
                        }
                    }
                    else
                    {
                        noteTimer.stop(); // Stop the timer when all notes are processed
                    }
                }
            }

            public void paintComponent(Graphics g)
            {
				int noteX;
				int noteY;
				int duration;
				boolean noteEnded;
				
				noteDestination note;
				
                super.paintComponent(g);
                g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
                g.drawImage(images[7], 550, 50, 450, 675, this);
                g.setColor(Color.GREEN);
                
                for(int noteID = 0; noteID < noteDest.length; noteID++)
                {
					note = noteDest[noteID];
					if(note != null)
					{
						noteEnded = note.getState();
						if(!noteEnded)
						{
							noteX = note.getX();
							noteY = note.getY();
							duration = note.getDuration();
					
							System.out.println(noteID + " " + noteX + " " + noteY + " " + duration);
						
							g.fillRect(noteX, noteY, 12, noteLength); // Draw the destination rectangle
						}
					}
				}
					
            }

            class noteDestination	//Object for note destinations, a new object is created each time a new note is read form the scanner
            {						//All notes are reset to null after the round ends
				private int noteID;
				private int noteX;
				private int noteY;
				private int duration;
				private boolean noteEnd;
				private Timer noteDuration;
				
				public noteDestination(int noteDestIDIn, int noteXIn, int noteYIn, int durationIn)
				{
					noteID = noteDestIDIn;
					noteX = noteXIn;
					noteY = noteYIn;
					duration = durationIn;
					noteEnd = false;
					System.out.println(noteID + " " + noteX + " hi");
					
					class TimerHandler implements ActionListener
					{
						public void actionPerformed(ActionEvent evt)
						{
							noteEnd = true;
						}
					}
					
					TimerHandler th = new TimerHandler();
					noteDuration = new Timer(duration, th);
				}
				
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
				
				public boolean getState()
				{
					return noteEnd;
				}
            } 

            class infoBarPanel extends JPanel
            {
                private JButton home;
                private JLabel timerLabel;
                private JLabel hintLabel;

                public infoBarPanel()
                {
                    setBackground(Color.BLACK);
                    setLayout(null);

                    home = new JButton();
                    home.setContentAreaFilled(false); 
                    home.setBorderPainted(false); 
                    home.setFocusPainted(true); 
                    home.setBounds(15, 5, 40, 40); //adjust size
                    add(home);

                    timerLabel = new JLabel("Time Left: " + timeLeft + "s");
                    timerLabel.setForeground(Color.WHITE);
                    timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    timerLabel.setBounds(100, 5, 200, 40);
                    add(timerLabel);

                    hintLabel = new JLabel("Hint: " + currentNote);
                    hintLabel.setForeground(Color.WHITE);
                    hintLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    hintLabel.setBounds(300, 5, 200, 40);
                    add(hintLabel);

                    homeButtonHandler hb = new homeButtonHandler();
                    home.addActionListener(hb); 
                }

                public void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    g.drawImage(images[10], 20, 10, 30, 30, this); // Draw home.png on the home button
                    g.drawImage(images[11], 65, 10, 30, 30, this); // Draw timer.png next to the timer label
                    timerLabel.setText("Time Left: " + timeLeft + "s"); // Update the timer label
                    hintLabel.setText("Hint: " + currentNote); // Update the hint label
                }

                class homeButtonHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        stopGameTimer(); // Stop the timer when exiting the main game page
                        gameInSession = false; // Set gameInSession to false when returning to home
                        gph.showCard("home"); // Navigate to the home page
                    }
                }
            }
        }

        class statsBarPanel extends JPanel
        {
            public statsBarPanel()
            {
            }

            public void paintComponent(Graphics g)
            {
                g.drawImage(images[8], 0, 0, getWidth(), getHeight(), this);
            }
        }

        class resultsPanel extends JPanel
        {
            public resultsPanel()
            {
                setLayout(null);
                JButton homeButton = new JButton("Home");
                homeButton.setBounds(450, 350, 100, 50); // Center the button
                homeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        gameInSession = false; // Set gameInSession to false when returning to home
                        gph.showCard("home");
                    }
                });
                add(homeButton);
            }

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
            }
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
        }
    }

    class SettingsPanel extends JPanel
    {
        public SettingsPanel()
        {
            setLayout(null);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
        }
    }

}
