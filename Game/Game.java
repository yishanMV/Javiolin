import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Color;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class Game
{
	private int accuracyPercent;
	private int notesCountTotal;
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
		String[] imageName = new String[]{"settings.png"};
		images = new Image[imageName.length];
		for(int i = 0; i < imageName.length; i++)
		{
			try
			{
				images[i] = ImageIO.read(new File("Images/" + imageName[i]));
			}
			catch(IOException e)
			{
				System.err.println("\n\n\n" + imageName + " can't be found.\n\n");
				e.printStackTrace();
			}
				
			
		}
	}

    class HomePanel extends JPanel
    {
        Font title;
        Font buttonText;
        Font subtitle;
        Icon settingsIcon;

        public HomePanel()
        {
			
			Color WHITE = new Color(255, 255, 255);
			setBackground(WHITE);
			settingsIcon = new ImageIcon(images[0]);
            title = new Font("Arial", Font.BOLD, 100);
            buttonText = new Font("Arial", Font.PLAIN, 50);
            subtitle = new Font("Arial", Font.PLAIN, 20);

            setLayout(null);
            JButton play = new JButton("Play"); //note im playing on making ImageIcon, do some research!!
            JButton help = new JButton("Help");
            JButton settings = new JButton(settingsIcon);
          
            

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

            play.setBounds(100, 250, 200, 100);
            help.setBounds(100, 400, 200, 100);
            
            accuracy.setBounds(700, 300, 200, 30);
            notesPlayed.setBounds(700, 350, 200, 30);
            highscore.setBounds(700, 400, 200, 30);
            settings.setBounds(700, 75, 100, 100);

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
						cl.show(
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
            g.setFont(title);
            g.drawString("Javiolin", 300, 150);
            g.setFont(subtitle);
            g.drawString("My Stats", 700, 250);
            //g.drawImage(images[0], 300, 300, 100, 100, this);
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
