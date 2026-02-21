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
	Gem[][] board;
	final IconCache cache = Constants.cache;
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
		for (int i = 1; i <= Constants.SPIN_FRAMES; i++) {
			for (GemColor color : GemColor.values()) {
				cache.getIcon(Constants.gemFilePath(color, i));
			}
		}
	}


	private void addComponentsToPane(final Container pane) {
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(rows, cols, 0, 0));
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(0, 3));
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		controlPanel.add(resetButton);

		//Add gems to main board
		generateGems();
		generateBoard(gamePanel);

		/*
		Animate the gems spinning by repainting the game board 30 times per second.
		Since Gem objects extend JButton, this triggers their getIcon method,
		which has been overridden to return a new Icon each time it is called.
		*/
		ActionListener repaintListener = _ -> gamePanel.repaint();
		Timer repaintTimer = new Timer(1000 / Constants.REPAINTS_PER_SECOND, repaintListener);
		repaintTimer.start();

		/*
		Start a timer running that will apply gravity to settle gems in the board and
		introduce new random gems to the top layer of the board to keep it full.
		 */
		ActionListener gemDropListener = _ -> {
			applyGravity();
			populateTopRow();
		};
		Timer gemDropTimer = new Timer(1000 / Constants.DROPS_PER_SECOND, gemDropListener);
		gemDropTimer.start();

		pane.add(gamePanel, BorderLayout.NORTH);
		pane.add(new JSeparator(), BorderLayout.CENTER);
		pane.add(controlPanel, BorderLayout.SOUTH);
	}


	/**
	 * Instantiates the game board with new, null-colored gem objects
	 */
	private void generateGems() {
		board = new Gem[rows][cols];

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				Gem gem = new Gem(null, x, y);
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
		gameBoard.removeAll();

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				gameBoard.add(board[x][y]);
			}
		}
	}


	/**
	 * Scans the top row of the game board and assigns a new random color
	 * to all gems that currently have a 'null' color.
	 * This is meant to simulate empty spaces being filled with new gems.
	 */
	private void populateTopRow() {
		for (int y = 0; y < cols; y++) {
			if (board[0][y].color == null) {
				// nextInt is normally exclusive of the top value,
				// so add 1 to make it inclusive
				int randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
				board[0][y].color = GemColor.values()[randomNum];
			}
		}
	}


	/**
	 * Scans the board row-by-row from bottom to top, progressively trying to
	 * update null gem colors by 'pulling' colors from the row directly above.
	 * <p>
	 * If a gem is null color, and the gem directly above it is not null color,
	 * then swap colors between these 2 gems, simulating that type of gem 'falling down'.
	 */
	private void applyGravity() {
		for (int x = rows - 1; x > 0; x--) {
			for (int y = 0; y < cols; y++) {
				if (board[x][y].color == null) {
					if (board[x - 1][y].color != null) {
						board[x][y].color = board[x - 1][y].color;
						board[x - 1][y].color = null;
					}
				}
			}
		}
	}


	private void evaluateMatches(Gem first, Gem second) {
		//Record color and position for both gem selections
		GemColor firstColor = first.color;
		int firstX = first.x;
		int firstY = first.y;
		GemColor secondColor = second.color;
		int secondX = second.x;
		int secondY = second.y;

		//Tentatively make the swap, then verify if it's legal
		board[firstX][firstY].color = secondColor;
		board[secondX][secondY].color = firstColor;

		if (checkAroundCell(firstX, firstY, secondColor) && checkAroundCell(secondX, secondY, firstColor)) {
			//Swap back if not legal
			board[firstX][firstY].color = firstColor;
			board[secondX][secondY].color = secondColor;

			return;
		}

		findAllMatches();
	}


	private boolean checkAroundCell(int x, int y, GemColor color) {
		// Check vertical
		int vertical = 1;

		// Up
		int cx = x - 1;
		while (cx >= 0 && board[cx][y].color == color) {
			vertical++;
			cx--;
		}

		// Down
		cx = x + 1;
		while (cx < rows && board[cx][y].color == color) {
			vertical++;
			cx++;
		}

		if (vertical >= 3) {
			return false; // a match exists
		}

		// Check horizontal
		int horizontal = 1;

		// Left
		int cy = y - 1;
		while (cy >= 0 && board[x][cy].color == color) {
			horizontal++;
			cy--;
		}

		// Right
		cy = y + 1;
		while (cy < cols && board[x][cy].color == color) {
			horizontal++;
			cy++;
		}

		if (horizontal >= 3) {
			return false; // a match exists
		}

		// No matches
		return true;
	}



	private void findAllMatches() {
		//Check horizontal
		for (int row = 0; row < rows; row++) {
			int col = 0;
			while (col < cols) {
				int start = col;
				GemColor color = board[row][col].color;

				while (col < cols && board[row][col].color == color) {
					col++;
				}

				if (col - start >= 3) {
					for (int c = start; c < col; c++) {
						board[row][c].color = null;
					}
				}
			}
		}

		//Check vertical
		for (int col = 0; col < cols; col++) {
			int row = 0;
			while (row < rows) {
				int start = row;
				GemColor color = board[row][col].color;

				while (row < rows && board[row][col].color == color) {
					row++;
				}

				if (row - start >= 3) {
					for (int r = start; r < row; r++) {
						board[r][col].color = null;
					}
				}
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

				evaluateMatches(lastSelection, board[selection.x][selection.y]);

				//TODO: points?
			}

			gemSelected = !gemSelected;
		} else {//Handle clicks on other buttons
			try {
				JButton clicked = (JButton) e.getSource();

				if (clicked.getText().equals("Reset")) {
					System.out.println("Reset");

					//Sets all gem colors to null, which will cause the board to repopulate with new colors
					for (int x = 0; x < rows; x++) {
						for (int y = 0; y < cols; y++) {
							board[x][y].color = null;
						}
					}
				}
			} catch (Exception _) {
			}
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
