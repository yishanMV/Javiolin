//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//GamePanelHolder.java
//
//This class holds all the panels of the game. It uses a CardLayout to switch between panels, and also has 
//methods to allow different panels to communicate and call each other's methods to update information.

//Import layouts
import java.awt.CardLayout;
import javax.swing.JPanel;

//class header
public class GamePanelHolder extends JPanel
{
    //All field variables
    //Field variables for objects
    public CardLayout cl;
    public Game game;
    private GamePanel gamePanel;
    HomePanel hp;
    InstructionPanelHolder iph;
    SettingsPanel sp;

    //Constructor to initialize the GamePanelHolder, and add all the panels to the card layout
    public GamePanelHolder(Game gameIn)
    {
        game = gameIn;  //Game object

        cl = game.getCardLayout();  //Gets the card layout from the game object
        setLayout(cl);  //Sets the layout of the panel to the card layout

        //Make an instance of all panels and send in an instance of game and this GamePanelHolder 
        hp = new HomePanel(game, this); 
        gamePanel = new GamePanel(game, this);
        iph = new InstructionPanelHolder(game, this);
        sp = new SettingsPanel(game, this);

        //Add all the panels to the card layout
        add(iph, "instructions");
        add(hp, "home");
        add(gamePanel, "game");
        add(sp, "settings");

        cl.show(this, "home");  //Show the home panel by default
    }

    //Shows a specific card in the card layout, very useful for switching between panels
    public void showCard(String cardName)
    {
        cl.show(this, cardName);    //Switch to the specified card
    }
    
    //Helps connect the game panel class to the home panel class when updating all home panel displayed stats after a round of the game is finished
    public void updateStatsInHome()
    {
		hp.updateStats();
	}

    //Gets the game panel object
    public GamePanel getGamePanel()
    {
            return gamePanel;   //Returns the game panel
    }
}
