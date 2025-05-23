//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//SettingsPanel.java
//
//This class is the settings screen of the game. It contains buttons to go to the instructions, 
//change Freeplay mode game duration, reset stats, toggle hints on, and go back to home.

//Imports
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.PrintWriter;
import java.io.IOException;

//Makes panels that show the settings of the game, which the use can adjust
public class SettingsPanel extends JPanel
{
	//Field variables
	
	//Classes
    private Game game;
    private GamePanelHolder gph;
    	
    //Game information
    private Image[] images;

	//Constructor
    public SettingsPanel(Game gameIn, GamePanelHolder gphIn)
    {
		//Initialize variables 
        game = gameIn;
        gph = gphIn;
        images = game.getImages();
        
        setLayout(null);	//Set panel layout
        setOpaque(false);	//Make the panel not opaque (to allow transparency to occur)

		//Adds a new instance of a panel, which shows the setting options, to the null layout
        SettingsPaneldown spd = new SettingsPaneldown();	
        spd.setBounds(100, 150, 800, 500);
        add(spd);
    }
    
    //Draws the background and label
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);	//Clears the panel
        g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);	//Draws the background image
        g.drawImage(images[5], 300, 30, 400, 100, this);	//Draws the Settings label
    }
	
	//Clears all high scores when called during stats reset
	public void clearHighScoreFile()
	{
		//Try catch to make a print writer to clear a file
		try
		{
			PrintWriter fileClearer = new PrintWriter("Data/highscore.txt");	//New PrintWriter in highscore.txt
			fileClearer.print("");  //Clears file contents
			fileClearer.close();	//Flushes the buffer
		} 
		catch (IOException e)	//Catches errors in accessing the file and prints an error message
		{
			System.err.println("Cannot access highscore.txt");
			System.exit(1);							
		}
	}    

    //Holds all settings options
    class SettingsPaneldown extends JPanel
    {
		//Constructor
        public SettingsPaneldown()
        {
            setLayout(new GridLayout(1, 2, 40, 0)); //Set layout
            
            //Makes background translucent
            setOpaque(true);	
            setBackground(new Color(0, 0, 0, 150));
            
            //Adds panels to the holder
            add(new SettingsLeftPanel());
            add(new SettingsRightPanel());
        }

		//Makes labels for the setting options
        class SettingsLeftPanel extends JPanel
        {
			//Constructor
            public SettingsLeftPanel()
            {
                setOpaque(false);	//Allows transparency to occur
                setLayout(new GridLayout(6, 1, 0, 20));	//Sets the layout

                Font labelFont = new Font("Arial", Font.BOLD, 20); 	//Makes a default font
                
                //Stores an array of all settings option label text
                String[] labels = new String[]
                {
                    "Instructions", "Game Length", "Reset One Stat",
                    "Reset All Stats", "Hints", "Home"
                };

				//Loops through all label names and makes them into a JLabel, and adds it to the panel
                for (int i = 0; i < labels.length; i++)
                {
                    JLabel label = new JLabel(labels[i]);	//New JLabel of the next String in the array
                    label.setFont(labelFont);	//Sets the font
                    label.setHorizontalAlignment(JLabel.CENTER);	//Aligns the label in the middle
                    label.setForeground(Color.WHITE);  //Makes the text white text for better contrast
                    add(label);	//Adds the new label to the panel
                }
            }
        }

        class SettingsRightPanel extends JPanel
        {
            private JSlider slider1;

            public SettingsRightPanel()
            {
                setLayout(new GridLayout(6, 1, 0, 20)); // Added vertical gap 20
                setOpaque(false);

                add(new Hold2());
                makeSlider();
                add(slider1);
                add(new ResetOne());
                add(new Reset());
                add(new hint1());
                add(new Hold1());
            }

            class Hold1 extends JPanel
            {
                public Hold1()
                {
                    setOpaque(false);
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));  // Centered with spacing
                    JButton homeButton = new JButton("Home");
                    homeButton.setFont(new Font("Arial", Font.PLAIN, 18));
                    homeButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            gph.showCard("home");
                        }
                    });
                    add(homeButton);
                }
            }

            class Hold2 extends JPanel
            {
                public Hold2()
                {
                    setOpaque(false);
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    JButton instructionsButton = new JButton("Go To Instructions");
                    instructionsButton.setFont(new Font("Arial", Font.PLAIN, 18));
                    instructionsButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            gph.showCard("instructions");
                        }
                    });
                    add(instructionsButton);
                }
            }

            class ResetOne extends JPanel
            {
                private JCheckBox checkBoxTF;
                private JCheckBox checkBoxNevermind;
                private String command;

                public ResetOne()
                {
                    setOpaque(false);
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    JMenuBar menu = makeStatsMenuBar();
                    menu.setOpaque(false);
                    add(menu);

                    checkBoxTF = new JCheckBox("Are you sure?");
                    checkBoxTF.setForeground(Color.WHITE);
                    checkBoxTF.setContentAreaFilled(false); 
					checkBoxTF.setBorderPainted(false); 
					checkBoxTF.setFocusPainted(false); 
                    checkBoxTF.setVisible(false);
                    checkBoxTF.setOpaque(false);
                    checkBoxTF.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxTF.addActionListener(new CheckBoxListener());
                    add(checkBoxTF);
                    
                    checkBoxNevermind = new JCheckBox("Nevermind");
                    checkBoxNevermind.setForeground(Color.WHITE);
                    checkBoxNevermind.setContentAreaFilled(false); 
					checkBoxNevermind.setBorderPainted(false); 
					checkBoxNevermind.setFocusPainted(false); 
                    checkBoxNevermind.setVisible(false);
                    checkBoxNevermind.setOpaque(false);
                    checkBoxNevermind.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxNevermind.addActionListener(new CheckBoxListener());
                    add(checkBoxNevermind);
                }

                class CheckBoxListener implements ActionListener
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        if (checkBoxTF.isSelected())
                        {
                            if (command.equals("Overall Accuracy"))
                            {
                                game.resetNotesCountTotal();
                                game.resetNotesCorrectTotal();
                                gph.updateStatsInHome();
                            }
                            else if (command.equals("Total Note Count"))
                            {
                                game.resetLifetimeNotes();
                                gph.updateStatsInHome();
                            }
                            else if (command.equals("Highscore"))
                            {
                                clearHighScoreFile();
                                gph.updateStatsInHome();
                            }

                        }
                        checkBoxTF.setSelected(false);
                        checkBoxTF.setVisible(false);		
                        checkBoxNevermind.setSelected(false);
                        checkBoxNevermind.setVisible(false);				
							
                    }
                }

                public JMenuBar makeStatsMenuBar()
                {
                    JMenuBar bar = new JMenuBar();
                    bar.setOpaque(false);
                    JMenu picture = new JMenu("Click To Choose Stats To Reset");
                    picture.setOpaque(false);
                    picture.setFont(new Font("Arial", Font.PLAIN, 14));

                    JMenuItem accuracy = new JMenuItem("Overall Accuracy");
                    JMenuItem notes = new JMenuItem("Total Note Count");
                    JMenuItem highscore = new JMenuItem("Highscore");

                    ActionListener listener = new resetStatMenuHandler();
                    accuracy.addActionListener(listener);
                    notes.addActionListener(listener);
                    highscore.addActionListener(listener);

                    picture.add(accuracy);
                    picture.add(notes);
                    picture.add(highscore);
                    bar.add(picture);

                    return bar;
                }

                class resetStatMenuHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        checkBoxTF.setSelected(false);
                        checkBoxNevermind.setSelected(false);
                        command = evt.getActionCommand();
                        checkBoxTF.setVisible(true);
                        checkBoxNevermind.setVisible(true);
                    }
                }
            }

            class Reset extends JPanel
            {
                private JCheckBox checkBoxAll;
                private JCheckBox checkBoxTFAll;
                private JCheckBox checkBoxAllNevermind;

                public Reset()
                {
                    setOpaque(false);
                    setForeground(Color.WHITE);
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

                    checkBoxAll = new JCheckBox("Click To Reset All Stats");
                    checkBoxAll.setOpaque(false);
                    checkBoxAll.setContentAreaFilled(false); 
					checkBoxAll.setBorderPainted(false); 
					checkBoxAll.setFocusPainted(false); 
                    checkBoxAll.setForeground(Color.WHITE);
                    checkBoxAll.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxAll.addActionListener(new CheckBoxListener());

                    checkBoxTFAll = new JCheckBox("Are you sure?");
                    checkBoxTFAll.setOpaque(false);
                    checkBoxTFAll.setContentAreaFilled(false); 
					checkBoxTFAll.setBorderPainted(false); 
					checkBoxTFAll.setFocusPainted(false); 
                    checkBoxTFAll.setForeground(Color.WHITE);
                    checkBoxTFAll.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxTFAll.setVisible(false);
                    checkBoxTFAll.addActionListener(new CheckBoxListener());
                    
                    checkBoxAllNevermind = new JCheckBox("Nevermind");
                    checkBoxAllNevermind.setOpaque(false);
                    checkBoxAllNevermind.setContentAreaFilled(false); 
					checkBoxAllNevermind.setBorderPainted(false); 
					checkBoxAllNevermind.setFocusPainted(false); 
                    checkBoxAllNevermind.setForeground(Color.WHITE);
                    checkBoxAllNevermind.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxAllNevermind.setVisible(false);
                    checkBoxAllNevermind.addActionListener(new CheckBoxListener());                    

                    add(checkBoxAll);
                    add(checkBoxTFAll);
                    add(checkBoxAllNevermind);
                }

                class CheckBoxListener implements ActionListener
                {
                    public void actionPerformed(ActionEvent evt)
                    {
						
                        if (checkBoxAll.isSelected())
                        {
                            checkBoxTFAll.setVisible(true);
                            checkBoxAllNevermind.setVisible(true);
                        }
                        else
                        {
                            checkBoxTFAll.setVisible(false);
                            checkBoxTFAll.setSelected(false);
                            checkBoxAllNevermind.setVisible(false);
                            checkBoxAllNevermind.setSelected(false);
                        }
                        
                        if (checkBoxAll.isSelected() && checkBoxTFAll.isSelected())
                        {
                            game.resetNotesCountTotal();
                            game.resetNotesCorrectTotal();
                            game.resetLifetimeNotes();
                            clearHighScoreFile();
                            
                            checkBoxAll.setSelected(false);
                            checkBoxTFAll.setSelected(false);
                            checkBoxTFAll.setVisible(false);
                            checkBoxAllNevermind.setSelected(false);
                            checkBoxAllNevermind.setVisible(false);
                                                       
                            gph.updateStatsInHome();
                        }
                        
                        if(checkBoxAllNevermind.isSelected())
                        {
                            checkBoxTFAll.setVisible(false);
                            checkBoxTFAll.setSelected(false);
                            checkBoxAllNevermind.setVisible(false);
                            checkBoxAllNevermind.setSelected(false);
                            checkBoxAll.setSelected(false);
                        }                        
                    }
                }
            }

            class hint1 extends JPanel
            {
                private JCheckBox checkBoxHint;

                public hint1()
                {
                    setOpaque(false);
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

                    checkBoxHint = new JCheckBox("Turn Hints Off (Difficult)");
                    checkBoxHint.setForeground(Color.WHITE);
                    checkBoxHint.setContentAreaFilled(false); 
					checkBoxHint.setBorderPainted(false); 
					checkBoxHint.setFocusPainted(false); 
                    checkBoxHint.setOpaque(false);
                    checkBoxHint.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxHint.addActionListener(new CheckBoxListener());
                    add(checkBoxHint);
                }

                class CheckBoxListener implements ActionListener
                {
                    public void actionPerformed(ActionEvent evt)
                    {
						if(checkBoxHint.isSelected())
							game.setHint(false);
						else
							game.setHint(true);
                    }
                }
            }

            class SliderListener implements ChangeListener
            {
                public void stateChanged(ChangeEvent evt)
                {
                    int gameDuration = slider1.getValue();
                    // use gameDuration if needed
                }
            }

            public void makeSlider()
            {
                slider1 = new JSlider(0, 200, 20);
                slider1.setForeground(Color.WHITE);
                slider1.setMajorTickSpacing(20);
                slider1.setPaintTicks(true);
                slider1.setLabelTable(slider1.createStandardLabels(20));
                slider1.setPaintLabels(true);
                slider1.setOrientation(JSlider.HORIZONTAL);
                slider1.setOpaque(false);
                slider1.setFont(new Font("Arial", Font.PLAIN, 12));
                slider1.addChangeListener(new SliderListener());
            }
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
        }
    }
}
