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


//Imports
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private Scanner musicScanner;

    //Field variables for files
    private Image[] images;

    //Field variables for game stats
    private int currentScore;
    private String currentNote;

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
        currentScore = game.getCurrentScore();
        musicScanner = game.getMusicScanner();
        noteNames = game.getNoteNames();
        images = game.getImages();
        currentNote = "N/A"; //Initialize currentNote to a default value
        gamePages = new CardLayout();

        //Set layout to card layout
        setLayout(gamePages);

        //Create instances of all panels and add them to the card layout
        loading = new loadingPanel();
        mainGame = new mainGamePanel();
        results = new resultsPanel();

        add(loading, "loading");
        add(mainGame, "main game");
        add(results, "results");

        gamePages.show(this, "loading");    //Show the loading panel by default
    }

    //Shows the loading screen
    public void showLoadingScreen()
    {
        gamePages.show(this, "loading");    //Show the loading panel
        loading.startTransition();  //Start the transition to the main game
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
    }

    //Main game panel that contains the game logic and graphics
    class mainGamePanel extends JPanel
    {
        //All field variables

        //Field variables for objects/components
        private Timer gameTimer;
        private mainGamePanel mainGame;
        private noteDestination[] noteDest;
        private noteFall[] notes;
        private infoBarPanel infoBar;
        private statsBarPanel statsBar;
        private JLabel timerLabel; // Label to display the time

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

        //Constructor
        public mainGamePanel()
        {
            mainGame = this;    //Set the main game panel instance

            setLayout(new BorderLayout());  //Set layout to border layout

            //Make an instance of the stats bar and info bar and adds them to the panel
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
            
            if(!gameMode.equals("song"))    //Check if the gamemode is not song, and is Freeplay mode
            {
                gameDuration = 10; //Set the game duration to 10 seconds
                startGameTimer(); //Start the 10-second timer
            }
            readMusicFileAndDropNotes(mainGame); //Start dropping notes

        }

        //Starts the game timer in freeplay mode
        public void startGameTimer()
        {
            timeLeft = 10; // Reset the timer to 10 seconds
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

        //Reads the music file and drops notes
        public void readMusicFileAndDropNotes(mainGamePanel mainGameIn)
        {
            game.resetMusicScanner(); // Ensure the Scanner is reset before use
            game.getMyMusicScanner(); // Get the Scanner for reading music files
            musicScanner = game.getMusicScanner(); // Get the Scanner for reading music files

            String name;

            mainGamePanel mainGameInstance = mainGameIn;    //Initializes the main game instance

            for (int i = 0; i < noteDest.length; i++) //When game starts, all the note objects are null
            {
                noteDest[i] = null; //Sets all note objects to null
            }
            currentScore = 0; //Reset current score
            statsBar.updateScore(currentScore); //Update the score display

            Timer noteTimer = new Timer(0, null); //Timer for dropping notes
            noteTimer.setRepeats(false); //Ensure the timer only triggers once per note

            do
            {
                name = musicScanner.next(); // Read the next word
            } while (!name.equals(songName)); // Skip to the specified song

            System.out.println("Starting to drop notes for song: " + name);

            // Create an ActionListener for dropping notes
            ActionListener dropNextNote = new NoteDropperHandler(mainGameInstance, noteTimer, musicScanner, infoBar);
            noteTimer.addActionListener(dropNextNote);
            noteTimer.start(); // Start the note dropping process
        }

        //Drops a note across the screen
        private void dropNoteAcrossScreen(mainGamePanel gamePanelIn, String note, int durationIn, int noteDestIDIn) //Triggers events that cause a note fall in drawDestination
        {
            System.out.println("Dropping note: " + note);   
            drawDestination(gamePanelIn, note, durationIn, noteDestIDIn); // Update the destination rectangle

        }

        //Draws the destination rectangle for the note
        public void drawDestination(mainGamePanel gamePanelIn, String note, int durationIn, int noteDestIDIn)
        {
            //Field variables
            String savedNote;
            int stringLocation;
            int duration;
            int rng;
            
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
            }

            //Creates new noteDestination object
            noteDest[noteDestIDIn] = new noteDestination(gamePanelIn, noteDestIDIn, x, y, duration);
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

            //Action performed when the timer triggers
            public void actionPerformed(ActionEvent e) 
            {
                System.out.print("Note dropper: " + noteDestID + " ");

                //Check if the scanner has more notes to read
                if (musicScanner.hasNext()) 
                {
                    String noteInfo = musicScanner.next(); //Read the next not
                    
                    //Ensures the note is in correct format
                    if (noteInfo.length() >= 2) 
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
                }
                else
                {
                    noteTimer.stop(); //Stop the timer when all notes are processed
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
                    int noteX = noteDestObject.getX();
                    int noteY = noteDestObject.getY();
                    int duration = noteDestObject.getDuration();
        
        
                    if (noteFallObject != null) 
                    { //Check if noteFallObject is not null

                        //Initialize note fall variables
                        noteFallX = noteFallObject.getX();
                        noteFallY = noteFallObject.getY();
                        g.setColor(Color.GREEN);    //Set color to green to paint the destination rectangle if it is not missed


                        if(noteFallY > (noteLength + noteY))    //If note falling bypasses the note destination and updates score
                        {
                            if(!noteFallObject.getScoreDone())  //Check if the score has not been updated already
                                statsBar.updateScore(-10);  //Update the score to -10 if the note is missed

                            g.setColor(Color.RED);   //Set color to red to paint the destination rectangle if it is missed
                            noteFallObject.setScoreDone(); //Set scoreDone to true to avoid duplicate scoring
                        }

                        //Draw the destination note, chdcks if the note has ended 
                        if(!noteEnded)
                            g.fillRect(noteX, noteY, 12, noteLength); //Draw the destination rectangle
                        
                        g.drawImage(noteImage, noteFallX, noteFallY, 12, noteLength, this); // Draw the falling note

                        }

                    
/*                    else
                    {
                        //Remove the note from the array if it has ended
                        noteDest[noteID] = null;
                    } */
                } 
            }
        }

        class noteDestination	//Object for note destinations, a new object is created each time a new note is read form the scanner
        {						//All notes are reset to null after the round ends

            //Field variables
            private int noteID;
            private int noteX;
            private int noteY;
            private int duration;
            private boolean noteEnd;
            private Timer noteDuration;
            
            //Constructor
            public noteDestination(mainGamePanel mgp, int noteDestIDIn, int noteXIn, int noteYIn, int durationIn)
            {
                //Initialize field variables
                noteID = noteDestIDIn;
                noteX = noteXIn;
                noteY = noteYIn;
                duration = durationIn * 1000; // Convert duration seconds to milliseconds
                noteEnd = false;
                System.out.println(noteID + " " + noteX + " hi");

                notes[noteID] = new noteFall(noteX, noteY, duration); //Create a new falling note object
                
                class TimerHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        noteEnd = true;
                        mgp.repaint();
                    }
                }
                
                TimerHandler th = new TimerHandler();
                noteDuration = new Timer(duration, th);
                noteDuration.setRepeats(false); // Ensure the timer only triggers once
                noteDuration.start(); // Start the timer
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

        class noteFall  //Object for falling notes
        {
            //D&I variables
            private int x; //Image x coordinate
            private int y;   //Image y coordinate
            private int speed; //Speed of fall in pixels per tick
            private Timer noteTimer;    
            private boolean scoreDone; //To check if score has been updated already to avoid duplicated score counts

            public noteFall(int xIn, int yIn, int durationIn) 
            {
                x = xIn; // Set the x coordinate
                y = 50; // Set the y coordinate
                speed = 700/(durationIn/1000)/30; // Set the speed of the fall (pixels per tick) 650 screen size
                scoreDone = false; //Initialize scoreDone to false

                // Set up the Timer to update the position every 30ms
                noteTimer = new Timer(30, null); // Timer for falling notes
                noteTimer.setRepeats(true); // Ensure the timer repeats

                class timerHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        y += speed; // Move the rectangle down
                        if (y > getHeight()) 
                        { // Reset if it goes off the screen
                            noteTimer.stop(); // Stop the timer when the note goes off the screen
                        }
                        mainGame.repaint(); // Repaint the panel to update the rectangle's position
                    }
                }
                timerHandler th = new timerHandler(); //Create a new timer handler
                noteTimer.addActionListener(th); //Add the action listener to the timer
                startFalling();
                
            }

            public int getX()
            {
                return x;
            }
            public int getY()
            {
                return y;
            }

            public boolean getScoreDone()
            {
                return scoreDone;
            }

            public void setScoreDone()
            {
                scoreDone = true;
            }


            public void startFalling() 
            {
                noteTimer.start(); // Start the Timer manually
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
                    stopGameTimer(); //Stop the timer when exiting the main game page
                    game.setGameInSession(false); //Set gameInSession to false when returning to home
                    gph.showCard("home"); //Navigate to the home page
                }
            }
        }
    }

    class statsBarPanel extends JPanel
    {

        private JLabel scoreLabel;
        public statsBarPanel()
        {
            setBackground(Color.BLACK);

            scoreLabel = new JLabel("Score:" + currentScore);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            scoreLabel.setBounds(500, 5, 200, 40);

            add(scoreLabel);
        }

        public void updateScore(int score)
        {
            currentScore += score;
            scoreLabel.setText("Score: " + currentScore);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
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
                    game.setGameInSession(false);
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