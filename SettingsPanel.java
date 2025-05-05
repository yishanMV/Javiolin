//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//SettingsPanel.java
//
//This class is the settings screen of the game. It contains buttons to go to the instructions, 
//change Freeplay mode game duration, reset stats, and go back to home.

//Imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
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

//Class header
public class SettingsPanel extends JPanel
{
    //All field variables

    //Field variables for files
    private Image[] images;

    //Field variables for objects
    private Game game;
    private GamePanelHolder gph;

    //Field variables for game configurations
    private boolean hint;

    //Instialize the settings panel
    public SettingsPanel(Game gameIn, GamePanelHolder gphIn)
    {
        //Initialize field variables
        game = gameIn;
        gph = gphIn;
        images = game.getImages();

        setLayout(new BorderLayout(10, 10)); //Set layout, add spacing for better aesthetics

        //Add title and content panels
        TitlePanel titlePanel = new TitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        SettingsContentPanel contentPanel = new SettingsContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    //Title panel for the settings screen, contains the title "Settings"
    class TitlePanel extends JPanel
    {
        //constructor
        public TitlePanel()
        {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));    // Center the title

            //Creates a label and adds it to panel
            JLabel titleLabel = new JLabel("Settings");
            titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
            add(titleLabel);
        }
    }

    //Panel for the settings screen, contains all the settings options
    class SettingsContentPanel extends JPanel
    {
        //Constructor
        public SettingsContentPanel()
        {
            setLayout(new GridLayout(1, 2, 20, 0)); //Set as grid layout, add spacing between panels
            setBackground(Color.LIGHT_GRAY);

            //Create left and right panels, one which has a label and one which has the buttons/slider/menu
            SettingsLeftPanel leftPanel = new SettingsLeftPanel();
            SettingsRightPanel rightPanel = new SettingsRightPanel();

            //Add the left and right panels to the main panel
            add(leftPanel);
            add(rightPanel);
        }

        //Left panel for the settings screen, contains the labels for each setting
        class SettingsLeftPanel extends JPanel
        {
            //Constructor
            public SettingsLeftPanel()
            {
                setLayout(new GridLayout(6, 1, 0, 10)); //Set as grid layout, add spacing between labels
                setBackground(Color.WHITE); // Set background to white

                //Create labels for each setting 
                JLabel instructionsLabel = createLabel("Instructions");
                JLabel gameLengthLabel = createLabel("Game Length");
                JLabel resetOneStatLabel = createLabel("Reset One Stat");
                JLabel resetAllStatsLabel = createLabel("Reset All Stats");
                JLabel hintsLabel = createLabel("Hints");
                JLabel homeLabel = createLabel("Home");

                //Add labels to the panel
                add(instructionsLabel);
                add(gameLengthLabel);
                add(resetOneStatLabel);
                add(resetAllStatsLabel);
                add(hintsLabel);
                add(homeLabel);
            }

            //Create a label with specific font and alignment
            private JLabel createLabel(String text)
            {
                //Create a label with the specified text in parameters
                JLabel label = new JLabel(text);

                //Set font and alignment
                label.setFont(new Font("Arial", Font.PLAIN, 20));
                label.setHorizontalAlignment(JLabel.CENTER);

                return label;   //Return the finished label
            }
        }

        //Right panel for the settings screen, contains the buttons, menu, and slider
        class SettingsRightPanel extends JPanel
        {
            private JSlider slider1;    //Slider for game length

            //Constructor
            public SettingsRightPanel()
            {
                setLayout(new GridLayout(6, 1, 0, 10)); //Set as grid layout, add spacing between components
                setBackground(Color.WHITE); //Set background to white

                //Create button to go to instructions and add action listener
                JButton instructionsButton = createButton("Instructions", new InstructionsButtonHandler());
                add(instructionsButton);

                //Create slider to adjust game length and add it to the panel
                slider1 = createSlider();
                add(slider1);

                //Create reset one stat panel and add it to the panel that can rest a stat
                ResetOneStatPanel resetOneStatPanel = new ResetOneStatPanel();
                add(resetOneStatPanel);

                //Create button to reset all stats and add action listener
                JButton resetAllStatsButton = createButton("Reset All Stats", new ResetAllStatsHandler());
                add(resetAllStatsButton);

                //Create check box to toggle on hints and add action listener
                JCheckBox hintsCheckBox = createCheckBox("Hints", new HintsCheckBoxHandler());
                add(hintsCheckBox);

                //Create button to go back to home and add action listener
                JButton homeButton = createButton("Home", new HomeButtonHandler());
                add(homeButton);
            }

            //Create buttons and check boxes with specific font and action listeners
            private JButton createButton(String text, ActionListener handler)
            {

                JButton button = new JButton(text); //Create a button with the specified text in parameters
                button.addActionListener(handler);  //Add action listener to the button
                button.setFont(new Font("Arial", Font.PLAIN, 18));  //Set font for the button

                return button;  //Return the finished button
            }

            //Create check boxes with specific font and action listeners
            private JCheckBox createCheckBox(String text, ActionListener handler)
            {
                JCheckBox checkBox = new JCheckBox(text);   //Create a check box with the specified text in parameters  
                checkBox.addActionListener(handler);    //Add action listener to the check box
                checkBox.setFont(new Font("Arial", Font.PLAIN, 18));    //Set font for the check box

                return checkBox;    //Return the finished check box
            }

            //Create a slider with specific features
            private JSlider createSlider()
            {
                JSlider slider = new JSlider(0, 200, 20);   //Creates slider with min, max, and initial value

                //Set the slider label/tick/orientation properties
                slider.setMajorTickSpacing(20);
                slider.setPaintTicks(true);
                slider.setLabelTable(slider.createStandardLabels(20));
                slider.setPaintLabels(true);
                slider.setOrientation(JSlider.HORIZONTAL);

                slider.addChangeListener(new SliderHandler());  //Add change listener to the slider

                return slider;  //Return the finishedslider
            }

            //Panel for resetting one chosen stat
            class ResetOneStatPanel extends JPanel
            {
                private String selectedStat;    //Field variable to store the selected stat to reset

                //Constructor
                public ResetOneStatPanel()
                {
                    setLayout(new BorderLayout(5, 5));  //Set layout, with spacing for better aesthetics
                    setBackground(Color.WHITE); // Set background to white

                    //Create a JMenuBar to select the stat to reset
                    JMenuBar menuBar = createMenuBar();


                    //Create a button to reset the selected stat and add action listener
                    JButton resetButton = new JButton("Reset Selected Stat");
                    resetButton.addActionListener(new ResetSelectedStatHandler());

                    //Add components to the panel
                    add(resetButton, BorderLayout.CENTER);
                    add(menuBar, BorderLayout.NORTH);
                }

                //Creates a menu bar with options to select the stat to reset
                private JMenuBar createMenuBar()
                {
                    //Create a menu bar with a menu 
                    JMenuBar menuBar = new JMenuBar();
                    JMenu menu = new JMenu("Click Me To Select Stat To Reset");

                    //Create menu items for each stat to reset and add action listeners
                    JMenuItem resetHighScore = new JMenuItem("High Score");
                    ResetHighScoreHandler highScoreHandler = new ResetHighScoreHandler();
                    resetHighScore.addActionListener(highScoreHandler);

                    JMenuItem resetAccuracy = new JMenuItem("Accuracy");
                    ResetAccuracyHandler accuracyHandler = new ResetAccuracyHandler();
                    resetAccuracy.addActionListener(accuracyHandler);

                    //Add menu items to the menu
                    menu.add(resetHighScore);
                    menu.add(resetAccuracy);

                    menuBar.add(menu);  //Add the menu to the menu bar

                    return menuBar; //Return the finished menu bar
                }

                //Action listeners change the selected stat to reset to whatever was selected in the JMenu
                class ResetHighScoreHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        selectedStat = "highScore"; //Set the selected stat to high score
                        System.out.println("High score selected for reset");
                    }
                }


                class ResetAccuracyHandler implements ActionListener
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        selectedStat = "accuracyPercent";   //Set the selected stat to accuracy percent
                        System.out.println("Accuracy selected for reset");
                    }
                }

                //Action listener for the reset button, resets the selected stat
                class ResetSelectedStatHandler implements ActionListener
                {
                    //When the reset button is pressed, uses an if--else statement to check which stat the user
                    //wants to reset, and resets it
                    public void actionPerformed(ActionEvent e)
                    {
                        if ("highScore".equals(selectedStat))
                        {
                            game.setHighScore(0);
                            System.out.println("High Score reset");
                        }
                        else if ("accuracyPercent".equals(selectedStat))
                        {
                            game.setAccuracyPercent(0);
                            System.out.println("Accuracy Percent reset");
                        }
                        else    //prints an error message if no stat was selected
                        {
                            System.out.println("No stat selected to reset");
                        }
                    }
                }
            }
        }
    }

    //Action listeners for the buttons

    //Action listener for the instructions button, to go to the instructions panel
    class InstructionsButtonHandler implements ActionListener
    {
        //When the button is clicked, change card to instructions
        public void actionPerformed(ActionEvent e)
        {
            gph.showCard("instructions");   //Shows instructions panel
        }
    }

    //Action listener for the reset all stats button, to reset all stats
    class ResetAllStatsHandler implements ActionListener
    {
        //When clicked, Resets all stats to 0
        public void actionPerformed(ActionEvent e)
        {
            game.setAccuracyPercent(0);
            game.setNotesCountTotal(0);
            game.setHighScore(0);
            System.out.println("All stats reset");
        }
    }

    //Action listener for the hints check box, to toggle hints on and off
    class HintsCheckBoxHandler implements ActionListener
    {
        //When the check box is clicked, checks for whether the box is selected or not and toggles hint settings
        public void actionPerformed(ActionEvent e)
        {
            JCheckBox source = (JCheckBox) e.getSource();   //Get the source of the event
            hint = source.isSelected();  //Check if the check box is selected, sets the result (yes/no), as a boolean to toggle hints
            System.out.println("Hints: " + (hint ? "On" : "Off"));  
        }
    }

    //Action listener for the home button, to go back to the home page
    class HomeButtonHandler implements ActionListener
    {
        //When the button is clicked, change card to home
        public void actionPerformed(ActionEvent e)
        {
            gph.showCard("home");   //Shows home panel
        }
    }

    //Action listener for the slider, to set the game duration (Only useful in freeplay mode, NOT song mode)
    class SliderHandler implements ChangeListener
    {
        //When the slider is changed, gets the value and sets the game duration to that value
        public void stateChanged(ChangeEvent e)
        {
            JSlider source = (JSlider) e.getSource();   //Get the source of the event
            int gameDuration = source.getValue();   //Get the value of the slider
            game.setGameDuration(gameDuration); //Set the game duration to the value of the slider
            System.out.println("Game Duration set to: " + gameDuration);
        }
    }

    //Paints the background image of the settings panel, will change to another image by Stanley
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);    //Clear the panel
        g.drawImage(images[0], 0, 0, getWidth(), getHeight(), this);    //Draws background image
    }
}