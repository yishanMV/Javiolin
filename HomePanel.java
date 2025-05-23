//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//HomePanel.java
//
//This class is the home screen of the game. It displays stats and contains
//buttons to play the game, view instructions, and access settings.

//Import layouts
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Scanner;
import java.io.FileNotFoundException; 
import java.io.File;

//Class header
public class HomePanel extends JPanel
{
    //All field variables 

    //Field variables for files
    private Image[] images; //Array of images

    //Field variables for classes
    private Game game;
    private GamePanelHolder gph;  
    
    //Field variables for components
    private JLabel accuracy;
    private JLabel notesPlayed;
    private JLabel highscore;

    //Field variables for fonts
    private Font title; //Font for title
    private Font buttonText;    //Font for button text
    private Font subtitle;  //Font for subtitle

    //Field variables for glow effect
    private double glowValue = 0.0; //Glow value for the glow effect
    private boolean glowIncreasing = true; //Direction of the glow animation (Whether it is becoming darker/lighter)

    //Constructor
    public HomePanel(Game gameIn, GamePanelHolder gphIn)
    {
        //Initialize field variables 
        game = gameIn;
        images = game.getImages();
        gph = gphIn;
			
        //Set background color
		Color WHITE = new Color(255, 255, 255);
		setBackground(WHITE);

        //Set fonts for title, button text, and subtitle
        title = new Font("Arial", Font.BOLD, 100);
        buttonText = new Font("Arial", Font.PLAIN, 50);
        subtitle = new Font("Arial", Font.PLAIN, 18);

        //Set layout to null and add buttons and labels
        setLayout(null);

        //For buttons, I put transparent JButtons over a drawn image of the button to improve aesthetics

        //Create Jbuttons
        
        //Button to start the game
        JButton play = new JButton("");
        play.setContentAreaFilled(false); 
        play.setBorderPainted(false); 
        play.setFocusPainted(true); 
        play.setFont(buttonText);

		//Button to go to instructions
        JButton help = new JButton("");
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);
        help.setFocusPainted(true);
        help.setFont(buttonText);

		//Button to go to settings
        JButton settings = new JButton("");
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        settings.setFocusPainted(true);
        settings.setFont(buttonText);

        //Set the text for stats as JLabels
        
        //Shows the accuracy
        accuracy = new JLabel("Overall Accuracy: N/A");
        accuracy.setFont(subtitle);

		//Shows total notes played
        notesPlayed = new JLabel("Total Notes Played: 0");
        notesPlayed.setFont(subtitle);

		//Shows the highscore
        highscore = new JLabel("Highscore: N/A");
        highscore.setFont(subtitle);
            
		updateStats();	//Updates when game starts to load highscore
		
        //Set the bounds for the buttons and labels
        //I set very specific bounds so everything is in the right exact place (On top of the image)
        play.setBounds(520, 308, 280, 90);
        help.setBounds(520, 406, 280, 90);
        settings.setBounds(520, 503, 280, 86);
        
        accuracy.setBounds(250, 350, 400, 30);
        notesPlayed.setBounds(250, 375, 400, 30);
        highscore.setBounds(250, 400, 400, 30);

        //Adds the buttons and labels to the panel
        add(play);
        add(help);
        add(accuracy);
        add(notesPlayed);
        add(highscore);
        add(settings);
        
        //Adds action listeners to the buttons
		play.addActionListener(new PlayButtonListener());
        help.addActionListener(new HelpButtonListener());
        settings.addActionListener(new SettingsButtonListener());

        //Initialize and start the glow animation timer
        Timer glowTimer = new Timer(20, new GlowTimerHandler());
        glowTimer.start();
    }
        
    //Draws the images and text on the panel
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);    //Clear the panel

        g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);

        // Draw the glowing effect covering the entire screen
        Color glowColor = new Color(0, 0, 0, (int) (glowValue * 175)); // Dark black with varying alpha
        g.setColor(glowColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        //Draws background images
        g.drawImage(images[2], 300, 50, 400, 100, this);
        g.drawImage(images[4], 225, 250, 600, 400, this);

        //Draws the title for the stats 
        g.setFont(subtitle);
        g.drawString("My Stats", 320, 325);
    }
    
    //Called to update all stats on the homescreen labels
    public void updateStats()
    {
		//D&I variables based on field variables from Game.java
		int lifetimeNotes = game.getLifetimeNotes();
		int totalNotes = game.getNotesCountTotal();
		int correctNotes = game.getNoteCorrectTotal();
		int highscoreOverall = getHighestScore();	//Calls method to calculate highscore
		
		//Updates the accuracy label
		//Checks if there are no total notes to avoid math error (division by 0)
		if(totalNotes != 0)	//If total notes is not 0
		{
			double overallAccuracy = ((double)correctNotes/totalNotes) * 100;	//Calculate accuracy
			accuracy.setText("Overall Accuracy: " + String.format("%.2f", overallAccuracy) + " %");	//Update accuracy label
		}
		else
		{
			accuracy.setText("Overall Accuracy: N/A");	//Updates accuracy label to show that this stat is not available yet
		}
		
		//Updates total notes and highscore stats labels
		notesPlayed.setText("Total Notes Played: " + lifetimeNotes);
		highscore.setText("Highscore: " + highscoreOverall);		
	}
	
	//Reads highscore.txt to find the highscore
	public int getHighestScore() 
	{
		int highestScore = 0;	//D&I variable to store high score
		File highscoreFile = new File("Data/highscore.txt");	//Makes the file
		Scanner lineReader = null;	//
		try 
		{
			lineReader = new Scanner(highscoreFile);
			while (lineReader.hasNextInt()) 
			{
				int score = lineReader.nextInt();
				if (score > highestScore)
					highestScore = score;
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("Error: highscore.txt not found.");
		} 
        if (lineReader != null)
            lineReader.close();
    
		return highestScore;
	}

    // Timer handler for the glow animation
    private class GlowTimerHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Update the alpha value for the glow effect
            if (glowIncreasing) 
            {
                glowValue += 0.005;
                if (glowValue >= 0.8) // Limit the glow to a subtle level
                {
                    glowIncreasing = false;
                }
            } 
            else 
            {
                glowValue -= 0.005;
                if (glowValue <= 0.3) // Minimum glow level
                {
                    glowIncreasing = true;
                }
            }

            repaint(); // Repaint the panel to reflect changes
        }
    }

    //Action listeners for the buttons

    //Action listener for the play button
    class PlayButtonListener implements ActionListener
    {
        //When the play button is pressed, updates game session status, and shows the game panel
        public void actionPerformed(ActionEvent evt)
        {
            game.setGameInSession(true); //Set the game in session status to true

            gph.showCard("game");   //Shows game panel
            gph.getGamePanel().showLoadingScreen(); //Shows game panel loading screen first
        }
    }

    //Action listeners for the help button
    class HelpButtonListener implements ActionListener
    {
        //When the help button is pressed, shows the instructions panel
        public void actionPerformed(ActionEvent evt)
        {
            gph.showCard("instructions");   //Shows instructions panel
        }
    }

    //Action listeners for the settings button
    class SettingsButtonListener implements ActionListener
    {
        //When the settings button is pressed, shows the settings panel
        public void actionPerformed(ActionEvent evt)
        {
            gph.showCard("settings");   //Shows settings panel
        }
    }
}

