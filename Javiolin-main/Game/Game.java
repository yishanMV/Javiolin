import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class Game
{
    private CardLayout cl;

    public Game()
    {
        cl = new CardLayout(); // Initialize cl
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

    class HomePanel extends JPanel
    {
        Font title;
        Font buttonText;
        Font subtitle;

        public HomePanel()
        {
            title = new Font("Arial", Font.BOLD, 100);
            buttonText = new Font("Arial", Font.PLAIN, 50);
            subtitle = new Font("Arial", Font.PLAIN, 30);

            setLayout(null);
            JButton play = new JButton("Play"); //note im playing on making ImageIcon, do some research!!
            JButton help = new JButton("Help");

            JLabel accuracy = new JLabel("Accuracy: 0%");
            JLabel notesPlayed = new JLabel("Notes Played: 0");
            JLabel highscore = new JLabel("Highscore: 0");

            play.setFont(buttonText);
            help.setFont(buttonText);


            JButton settings = new JButton("Settings");

            play.setBounds(100, 250, 200, 100);
            help.setBounds(100, 400, 200, 100);

            add(play);
            add(help);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setFont(title);
            g.drawString("Javiolin", 300, 150);
            g.setFont(subtitle);
            g.drawString("My Stats", 600, 250);
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
