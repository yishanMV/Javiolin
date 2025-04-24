import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Game
{
	private int accuracyPercent;
	private int notesCountTotal;
    private int gameDuration;
	private boolean oneRoundOver;
	private Image[] images;

    private CardLayout cl;

    public Game()
    {
        cl = new CardLayout(); // Initialize cl
        getMyImage();
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

        GamePanelHolder gph = new GamePanelHolder();
        frame.getContentPane().add(gph);
        frame.setVisible(true);
        
    }

    class GamePanelHolder extends JPanel
    {
        public GamePanelHolder()
        {
			
            setLayout(cl); // cl is now initialized

            HomePanel hp = new HomePanel();
            GamePanel gp = new GamePanel();
            InstructionPanelHolder ip = new InstructionPanelHolder();
            SettingsPanel sp = new SettingsPanel();


            add(ip, "instructions");
            add(hp, "home");
            add(gp, "game");
            add(sp, "settings");

            cl.show(this, "home");


        }
    }
    
   	public void getMyImage() 
	{
		String[] imageName = new String[]{"settings.png", "background1.png", "title.png", "paper.png", "selection.png"};
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
            play.setActionCommand("Play");


            JButton help = new JButton("");
            help.setContentAreaFilled(false);
            help.setBorderPainted(false);
            help.setFocusPainted(true);
            help.setFont(buttonText);
            help.setActionCommand("Help");


            JButton settings = new JButton("");
            settings.setContentAreaFilled(false);
            settings.setBorderPainted(false);
            settings.setFocusPainted(true);
            settings.setFont(buttonText);
            settings.setActionCommand("Settings");
          
            

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

            play.setFont(buttonText);
            help.setFont(buttonText);


            JButton settingsIcon = new JButton("Settings");

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
            
			class ButtonHandler implements ActionListener 
			{
				public void actionPerformed(ActionEvent evt) 
				{
					String command = evt.getActionCommand();
					if(command.equals("Play")) 
					{
						
					}
					else if(command.equals("Help"))
					{

					}
					else if(command.equals(settingsIcon))
					{
						
					}
				}
			}
			
			ButtonHandler bh = new ButtonHandler();
			
			play.addActionListener(bh);
            help.addActionListener(bh);
            settings.addActionListener(bh);            
        }
        

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this); // Set background image
            g.drawImage(images[2], 300, 50, 400, 100, this); // Set title image
            g.drawImage(images[4], 225, 250, 600, 400, this); // Draw selection image beneath title
            g.setFont(subtitle);
            g.drawString("My Stats", 275, 325);
        }
    }

    class InstructionPanelHolder extends JPanel
    {
        private CardLayout infoPages;

        public InstructionPanelHolder()
        {
            infoPages = new CardLayout(); // Initialize infoPages
            setLayout(infoPages); // infoPages is now initialized
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


    }

    class GamePanel extends JPanel
    {
        private CardLayout gamePages;

        public GamePanel()
        {
            gamePages = new CardLayout(); // Initialize gamePages
            setLayout(gamePages); // gamePages is now initialized
            mainGamePanel mainGame = new mainGamePanel();
            resultsPanel results = new resultsPanel();

            add(mainGame, "main game");
            add(results, "results");
            

            gamePages.show(this, "main game");
            

        }

        class mainGamePanel extends JPanel
        {
            public mainGamePanel()
            {
                setLayout(null);
            }
        }

        class resultsPanel extends JPanel
        {
            public resultsPanel()
            {
                setLayout(null);
            }
        }
    }

    class SettingsPanel extends JPanel
    {
        public SettingsPanel()
        {
            setLayout(null);
        }
    }

}
