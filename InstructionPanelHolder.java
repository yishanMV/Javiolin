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
    private Game game;
	private Image[] images;
	private InstructionPanelHolder iphln= this;

    //Makes a card layout to hold all the instruction panels and adds panels to it
    public InstructionPanelHolder(Game gameln, GamePanelHolder gphIn)
    {
        game = gameln;
        gph = gphIn;
        images = game.getImages();

        //Set layout to card layout
        infoPages = new CardLayout();
        setLayout(infoPages);

        //Create instances of all instruction panels and add them to the card layout
        firstInstructionPanel fip = new firstInstructionPanel(this);
        secondInstructionPanel sip = new secondInstructionPanel(this);
        thirdInstructionPanel tip = new thirdInstructionPanel(this);
        fourthInstructionPanel foip = new fourthInstructionPanel(this);

        //Add all the panels to the card layout
        add(fip, "firstInstructionPanel");
        add(sip, "secondInstructionPanel");
        add(tip, "thirdInstructionPanel");
        add(foip, "fourthInstructionPanel");

        infoPages.show(this, "firstInstructionPanel");  //Show the first instruction panel by default
    }

    //The first instruction panel
    class firstInstructionPanel extends JPanel
    {
        //Constructor to initialize the first instruction panel
        public firstInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Next button to go to second panel
            JButton nextButton = new JButton("");
            nextButton.setBounds(875, 600, 100, 100);
            nextButton.setContentAreaFilled(false); 
            nextButton.setBorderPainted(false); 
            nextButton.setFocusPainted(true); 
            nextButton.addActionListener(new NextButtonHandler(iph, "secondInstructionPanel"));

            //Home button
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.addActionListener(new HomeButtonHandler());
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 

            //Instruction text
            JTextArea text = new JTextArea("Welcome to Javiolin!\n\nUse your keyboard to play violin notes based"
				+ " on notes that will drop across the screen.\nMatch the correct pitch to score points.\n\nIn this game, " 
				+ "timing AND accuracy matter.\nUse the buttons to look at how the game works. \nClick 'Next' to learn about the interface.");
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setEditable(false);
            JScrollPane jsp = new JScrollPane(text);
            jsp.setBounds(250, 250, 500, 300);

            //Add components
            add(homeButton);
            add(nextButton);
            add(jsp);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);    //Background
            g.drawImage(images[8], 50, 600, 100, 100, this); //home
            g.drawImage(images[25], 175, 50, 650, 100, this);	//Instructions title text
            g.drawImage(images[3], 875, 600, 100, 100, this); //next
        }
    }

    //The second instruction panel
    class secondInstructionPanel extends JPanel
    {
        public secondInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Back button
            JButton backButton = new JButton("");
            backButton.setBounds(750, 600, 100, 100);
            backButton.setContentAreaFilled(false); 
            backButton.setBorderPainted(false); 
            backButton.setFocusPainted(true); 
            backButton.addActionListener(new NextButtonHandler(iph, "firstInstructionPanel"));

            //Next button
            JButton nextButton = new JButton("");
            nextButton.setBounds(875, 600, 100, 100);
            nextButton.setContentAreaFilled(false); 
            nextButton.setBorderPainted(false); 
            nextButton.setFocusPainted(true); 
            nextButton.addActionListener(new BackButtonHandler(iph, "thirdInstructionPanel"));

            //Home button
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 
            homeButton.addActionListener(new HomeButtonHandler());

            //Add components
            add(backButton);
            add(nextButton);
            add(homeButton);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[27], 0, 0, getWidth(), getHeight(), this);    
            g.drawImage(images[3], 875, 600, 100, 100, this); //next
            g.drawImage(images[6], 750, 600, 100, 100, this); //back
            g.drawImage(images[8], 50, 600, 100, 100, this); //home
        }
    }

    //The third instruction panel
    class thirdInstructionPanel extends JPanel
    {
        public thirdInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Next button
            JButton nextButton = new JButton("");
            nextButton.setBounds(875, 600, 100, 100);
            nextButton.setContentAreaFilled(false); 
            nextButton.setBorderPainted(false); 
            nextButton.setFocusPainted(true); 
            nextButton.addActionListener(new NextButtonHandler(iph, "fourthInstructionPanel"));
            
            //Back button
            JButton backButton = new JButton("");
            backButton.setBounds(750, 600, 100, 100);
            backButton.setContentAreaFilled(false); 
            backButton.setBorderPainted(false); 
            backButton.setFocusPainted(true); 
            backButton.addActionListener(new BackButtonHandler(iph, "secondInstructionPanel"));

            //Home button
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 
            homeButton.addActionListener(new HomeButtonHandler());

            //Add components
            add(nextButton);
            add(backButton);
            add(homeButton);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[28], 0, 0, getWidth(), getHeight(), this);
            g.drawImage(images[3], 875, 600, 100, 100, this); //next    
            g.drawImage(images[8], 50, 600, 100, 100, this); //home
            g.drawImage(images[6], 750, 600, 100, 100, this); //back
        }
    }

    class fourthInstructionPanel extends JPanel
    {
        public fourthInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null
            
            //Back button
            JButton backButton = new JButton("");
            backButton.setBounds(875, 600, 100, 100);
            backButton.setContentAreaFilled(false); 
            backButton.setBorderPainted(false); 
            backButton.setFocusPainted(true); 
            backButton.addActionListener(new BackButtonHandler(iph, "thirdInstructionPanel"));

            //Home button
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 
            homeButton.addActionListener(new HomeButtonHandler());
            

            //Add components
            add(backButton);
            add(homeButton);
        }
        
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[29], 0, 0, getWidth(), getHeight(), this);
            g.drawImage(images[3], 875, 600, 100, 100, this); //next    
            g.drawImage(images[8], 50, 600, 100, 100, this); //home
            g.drawImage(images[6], 875, 600, 100, 100, this); //back
        }
    }

    //Action listeners

    class NextButtonHandler implements ActionListener
    {
        private InstructionPanelHolder iph;
        private String targetPanel;

        public NextButtonHandler(InstructionPanelHolder iphIn, String targetPanelIn)
        {
            iph = iphIn;
            targetPanel = targetPanelIn;
        }

        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, targetPanel);
        }
    }

    class BackButtonHandler implements ActionListener
    {
        private InstructionPanelHolder iph;
        private String targetPanel;

        public BackButtonHandler(InstructionPanelHolder iphIn, String targetPanelIn)
        {
            iph = iphIn;
            targetPanel = targetPanelIn;
        }

        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, targetPanel);
        }
    }

    class HomeButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iphln, "firstInstructionPanel");
            gph.showCard("home");
        }
    }
}
