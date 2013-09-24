import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;


public class Run implements ActionListener {
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newGame, score, exit;
	private HeartsGUI game;
	private JFrame window;
	static final Dimension windowSize = new Dimension(800,700);
	private Timer t;

	public Run()
	{}

	public void start(){
		game = new HeartsGUI();
		window = new JFrame();                    //create new JFrame for game
		window.setContentPane(game);
		window.setVisible(true);
		window.setTitle ("Hearts");
		window.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);  
		window.setResizable(false);
		window.setPreferredSize(windowSize);

		//initialize JMenu items
		menuBar = new JMenuBar();
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);

		newGame = new JMenuItem("New Game", KeyEvent.VK_N);
		newGame.addActionListener(this);
		score = new JMenuItem("Score", KeyEvent.VK_S);    //shows score
		score.addActionListener(this);
		exit = new JMenuItem("Quit", KeyEvent.VK_Q);
		exit.addActionListener(this);

		window.setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(newGame);
		menu.add(score);
		menu.add(exit);

		window.pack();
		window.setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {   //action performed changes depending on choice
		JMenuItem source = (JMenuItem) e.getSource();
		String text = source.getText();   //gets the choice
		if (text.equals("New Game"))
			newGame();
		else if (text.equals("Score"))
			game.showScore();
		else if (text.equals("Quit")){
			window.dispose();
		}
	}

	private void newGame(){       //called when new game
		OpenWindow playTimer = new OpenWindow();
		t = new Timer(10, playTimer);    //call newGame after createBox
		game.createBox();       //creates loading frame
		t.start();
	}

	class OpenWindow implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			game.newGame(true);
			t.stop();
			game.closeBox();      //closes loading frame
		}
	}
}
