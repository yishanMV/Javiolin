//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//HomePanel.java
//
//This class is the home screen of the game. It contains buttons to play the game, view instructions, and access settings.

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

//Class header
public class HomePanel extends JPanel
{
    //All field variables 

    //Field variables for files
    private Image[] images; //Array of images

    //Field variables for objects
    private Game game;  //Game object
    private GamePanelHolder gph;    //GamePanelHolder object

    //Field variables for game stats
    private int notesCountTotal;    //Total number of notes played
    private int accuracyPercent;    //Accuracy percentage

    //Field variables for fonts
    private Font title; //Font for title
    private Font buttonText;    //Font for button text
    private Font subtitle;  //Font for subtitle

    //Constructor
    public HomePanel(Game gameIn, GamePanelHolder gphIn)
    {
        //Initialize field variables 
        game = gameIn;
        images = game.getImages();
        gph = gphIn;
        notesCountTotal = game.getNotesCountTotal();
        accuracyPercent = game.getAccuracyPercent();
			
        //Set background color
		Color WHITE = new Color(255, 255, 255);
		setBackground(WHITE);

        //Set fonts for title, button text, and subtitle
        title = new Font("Arial", Font.BOLD, 100);
        buttonText = new Font("Arial", Font.PLAIN, 50);
        subtitle = new Font("Arial", Font.PLAIN, 20);

        //Set layout to null and add buttons and labels
        setLayout(null);

        //For buttons, I put transparent JButtons over a drawn image of the button to improve aesthetics

        //Create Jbuttons with no content area filled, no border painted, with focus painted, and with font set.
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

        //Set the text for the buttons as JLabels
        JLabel accuracy = new JLabel("Accuracy: N/A");
        accuracy.setFont(subtitle);

        JLabel notesPlayed = new JLabel("Notes Played: N/A");
        notesPlayed.setFont(subtitle);

        JLabel highscore = new JLabel("Highscore: N/A");
        highscore.setFont(subtitle);
            
        //Update the text of the home page stats once the first game ends and the user receives their first stats
        boolean oneRoundOver = game.isOneRoundOver();
        if(oneRoundOver)
        {
			accuracy.setText("Accuracy: %" + accuracyPercent);
			notesPlayed.setText("Notes Played: " + notesCountTotal);
		}

        //Set the bounds for the buttons and labels
        //I set very specific bounds so everything is in the right exact place
        play.setBounds(520, 308, 280, 90);
        help.setBounds(520, 406, 280, 90);
        settings.setBounds(520, 503, 280, 86);
            
        accuracy.setBounds(275, 350, 200, 30);
        notesPlayed.setBounds(275, 375, 200, 30);
        highscore.setBounds(275, 400, 200, 30);

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
    }
        
    //Draws the images and text on the panel
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);    //Clear the panel

        //Draws background images
        g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
        g.drawImage(images[2], 300, 50, 400, 100, this);
        g.drawImage(images[4], 225, 250, 600, 400, this);

        //Draws the title for the stats 
        g.setFont(subtitle);
        g.drawString("My Stats", 275, 325);
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

