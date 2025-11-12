package holmes.match_gems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class GameBoard extends JFrame implements ActionListener {
	//Dimensions for the game board can be adjusted here
	final int rows = 10;
	final int cols = 10;
	final Gem[][] board = new Gem[rows][cols];
	final ImageCache cache = Helper.cache;
	boolean gemSelected = false;
	Gem lastSelection;
	JPanel gamePanel;

	public GameBoard(String name) throws IOException {
		super(name);
		setResizable(false);

		/*
		Initializes the cache and populates it with all the gem images which are used as animation frames.
		Due to the symmetry of the gems, only half of the total frames are necessary to achieve what looks like
		a full revolution of the gems spinning.

		Total memory reserved is about 900KB when using the 64x64 images,
		~650KB for the 32x32 images,
		or ~2MB for the 144x144 images.
		*/
		for (int i = 1; i <= 30; i++) {
			for (GemColor color : GemColor.values()) {
				cache.getImage(Constants.gemFilePath(color, i));
			}
		}
	}


	public void addComponentsToPane(final Container pane) {
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(rows, cols, 0, 0));
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(0, 3));

		//Add gems to main board
		generateGems();
		generateBoard(gamePanel);

		/*
		Animate the gems spinning by repainting the game board 30 times per second.
		Since Gem objects extend JButton, this triggers their getIcon method,
		which has been overridden to return a new Icon each time it is called.
		*/
		ActionListener timerListener = _ -> gamePanel.repaint();
		Timer timer = new Timer(1000 / 30, timerListener);
		timer.start();

		pane.add(gamePanel, BorderLayout.NORTH);
		pane.add(new JSeparator(), BorderLayout.CENTER);
		pane.add(controlPanel, BorderLayout.SOUTH);
	}


	/**
	 * Instantiates the game board with new, randomly-colored gem objects
	 */
	private void generateGems() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				// nextInt is normally exclusive of the top value,
				// so add 1 to make it inclusive
				int randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
				Gem gem = new Gem(GemColor.values()[randomNum], x, y);
				gem.addActionListener(this);
				board[x][y] = gem;
			}
		}
	}


	/**
	 * Adds all the Gem objects to the gameBoard panel
	 *
	 * @param gameBoard - panel to add Gem objects to
	 */
	private void generateBoard(JPanel gameBoard) {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				gameBoard.add(board[x][y]);
			}
		}
	}


	private static void createAndShowGUI() throws IOException {
		//Create and set up the window.
		GameBoard frame = new GameBoard("Match Gems");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set up the content pane.
		frame.addComponentsToPane(frame.getContentPane());
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		//Handle clicks on gems
		if (e.getSource() instanceof Gem selection) {
			if (!gemSelected) {
				lastSelection = board[selection.x][selection.y];
				lastSelection.setBackground(Color.GRAY);
			} else {
				lastSelection.setBackground(Color.WHITE);

				//Record color and position for both gem selections
				GemColor firstColor = lastSelection.color;
				int firstX = lastSelection.x;
				int firstY = lastSelection.y;
				GemColor secondColor = selection.color;
				int secondX = selection.x;
				int secondY = selection.y;

				System.out.printf("""
							---SELECTION---
							First selection: %s at %d, %d
							Second selection: %s at %d, %d
							""",
						firstColor.name(), firstX, firstY,
						secondColor.name(), secondX, secondY);

				//Swap color for selected gems
				board[firstX][firstY].color = secondColor;
				board[secondX][secondY].color = firstColor;

				//TODO: evaluate if swap is valid
				//TODO: evaluate matches
				//TODO: clear matches
				//TODO: refill board
				//TODO: points?
			}

			gemSelected = !gemSelected;
		}
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				createAndShowGUI();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
