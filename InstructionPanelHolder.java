//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//InstructionPanelHolder.java
//
//This class holds all the instruction panels of the game, that includes instructions on
//how to play the game. It uses a CardLayout to switch between panels.

//imports
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//Class header
public class InstructionPanelHolder extends JPanel
{
    //All field variables
    //Field variables for objects
    private CardLayout infoPages;
    private GamePanelHolder gph;

    //Makes a card layout to hold all the instruction panels and adds panels to it
    public InstructionPanelHolder(Game game, GamePanelHolder gphIn)
    {
        gph = gphIn;    //Initialize GamePanelHolder object

        //Set layout to card layout
        infoPages = new CardLayout();
        setLayout(infoPages);

        //Create instances of all instruction panels and add them to the card layout
        firstInstructionPanel fip = new firstInstructionPanel(this);
        secondInstructionPanel sip = new secondInstructionPanel(this);
        thirdInstructionPanel tip = new thirdInstructionPanel(this);

        //Add all the panels to the card layout
        add(fip, "firstInstructionPanel");
        add(sip, "secondInstructionPanel");
        add(tip, "thirdInstructionPanel");

        infoPages.show(this, "firstInstructionPanel");  //Show the first instruction panel by default
    }

    //The first instruction panel
    class firstInstructionPanel extends JPanel
    {
        //Constructor to initialize the first instruction panel
        public firstInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Create a button to go to the next instruction panel and add an action listener to it
            JButton nextButton = new JButton("Next");
            nextButton.setBounds(550, 50, 450, 675);
            nextButton.addActionListener(new NextButtonHandler(iph, "secondInstructionPanel"));

            //Tries to load an image for the button and set it as the icon for the button
            try
            {
                Image img = ImageIO.read(new File("Images/fingerboard.png"));
                img = img.getScaledInstance(450, 675, Image.SCALE_DEFAULT);
                nextButton.setIcon(new ImageIcon(img));
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            //Create a text area for the instructions and add it to a scroll pane
            JTextArea text = new JTextArea("This is the first instruction panel. Follow the instructions carefully.");
            text.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(text);
            jsp.setBounds(50, 50, 400, 200);

            //Add components to the panel
            add(jsp);
            add(nextButton);
        }

        //Resets background of the panel
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);    //Clear the panel background
            // Add any background image or color if needed later
        }
    }

    //The second instruction panel, that contains more information
    class secondInstructionPanel extends JPanel
    {
        //Constructor to initialize the second instruction panel, with more instructions
        //This panel is currently work in progress
        public secondInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Create a button to go to the next instruction panel and add an action listener to it
            JButton backButton = new JButton("Back");
            backButton.setBounds(450, 350, 100, 50);
            backButton.addActionListener(new NextButtonHandler(iph, "thirdInstructionPanel"));

            //Create a button to go to the previous instruction panel and add an action listener to it
            JButton nextButton = new JButton("Next");
            nextButton.setBounds(550, 50, 450, 675);
            nextButton.addActionListener(new BackButtonHandler(iph, "firstInstructionPanel"));

            //Create a button to go back to the home page and add an action listener to it
            JButton homeButton = new JButton("Home");
            homeButton.setBounds(600, 350, 100, 50);
            homeButton.addActionListener(new HomeButtonHandler());

            //Add components to panel
            add(backButton);
            add(homeButton);
        }
    }

    //Currently working on the layout for the third instruction panel
    class thirdInstructionPanel extends JPanel
    {
        //Constructor to initialize the third instruction panel
        public thirdInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Create a button to go to the previous instruction panel and add an action listener to it
            JButton backButton = new JButton("Back");
            backButton.setBounds(450, 350, 100, 50);
            backButton.addActionListener(new BackButtonHandler(iph, "firstInstructionPanel"));

            //Create a button to go back to the home page and add an action listener to it
            JButton homeButton = new JButton("Home");
            homeButton.setBounds(600, 350, 100, 50);
            homeButton.addActionListener(new HomeButtonHandler());

            //Add components to the panel
            add(backButton);
            add(homeButton);
        }
    }

    //Action listeners for the buttons

    //Action listener for the next button, to go to the next panel
    class NextButtonHandler implements ActionListener
    {
        //Field variables for objects
        private InstructionPanelHolder iph;
        private String targetPanel;

        //Constructor that receives the InstructionPanelHolder and the target panel to switch to
        public NextButtonHandler(InstructionPanelHolder iphIn, String targetPanelIn)
        {
            //Initialize field variables
            iph = iphIn;
            targetPanel = targetPanelIn;
        }

        //Action performed that switches to the target panel when button clicked
        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, targetPanel);   //Switch to the target panel
        }
    }

    //Action listener for the back button, to go oto the previous panel
    class BackButtonHandler implements ActionListener
    {
        //Field variables for objects
        private InstructionPanelHolder iph;
        private String targetPanel;

        //Constructor that receives the InstructionPanelHolder and the target panel to switch to
        public BackButtonHandler(InstructionPanelHolder iphIn, String targetPanelIn)
        {
            //Initialize field variables
            iph = iphIn;
            targetPanel = targetPanelIn;
        }

        //Action performed that switches to the target panel when button clicked
        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, targetPanel);   //Switch to the target panel
        }
    }

    //Action listener for the home button, to go back to the home page
    class HomeButtonHandler implements ActionListener
    {
        //Action performed that switches to the home panel when button clicked
        public void actionPerformed(ActionEvent e)
        {
            gph.showCard("home");   //Switches to the home panel in the main card layout in GamePanelHolder
        }
    }
}