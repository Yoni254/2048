package game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The basic game board
 * @author Yonatan
 *
 */
public class GameBoard extends JPanel {


	private static final long serialVersionUID = 1L;
	private KeyLis listener;
	private BufferedImage background;
	
	/** the grid */
	public int[] locationX = {8, 128, 250, 370};
	public int[] locationY = {132, 255, 375, 496};
	public int[] location = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	/** keep track of score */
	public int score;
	public JLabel keepScore;

	public ArrayList<Object> pieces = new ArrayList<Object>();
	
	
	
	public GameBoard() {
		
		// Reset the score
		score = 0;
		
		// Background
		try {
			System.out.println("Setting background");
			this.background = ImageIO.read(new File("textures/grid.png"));
		} catch (Exception err) {
			System.out.println(err); 
		}
		
		//Score label
		keepScore = new JLabel("Score: " + score);
		keepScore.setFont(new Font("Serif", Font.BOLD, 50));
		keepScore.setBounds(20, 30, 400, 50);
		this.add(keepScore);
		
		
		listener = new KeyLis();
		
		this.setSize(500, 500);
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(listener);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.background, -5, 122, null);
		
	}
	
	/**
	 * Reset the added this turn boolean
	 */
	private void newTurn() {
		for (Object piece : pieces) {
			((NumberPiece) piece).addedThisTurn = false;
		}
	}
	
	public class KeyLis extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			
			//Left
			if (key == KeyEvent.VK_LEFT) {
				newTurn();
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationX == 1) {
						((NumberPiece) pieces.get(i)).moveLeft();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationX == 2) {
						((NumberPiece) pieces.get(i)).moveLeft();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationX == 3) {
						((NumberPiece) pieces.get(i)).moveLeft();
					}
				}
				addPiece();
			}
			
			//Right
			if (key == KeyEvent.VK_RIGHT) {
				newTurn();
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationX == 2) {
						((NumberPiece) pieces.get(i)).moveRight();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationX == 1) {
						((NumberPiece) pieces.get(i)).moveRight();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationX == 0) {
						((NumberPiece) pieces.get(i)).moveRight();
					}
				}
				addPiece();
			}
			
			//Down
			if (key == KeyEvent.VK_DOWN) {
				newTurn();
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationY == 2) {
						((NumberPiece) pieces.get(i)).moveDown();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationY == 1) {
						((NumberPiece) pieces.get(i)).moveDown();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationY == 0) {
						((NumberPiece) pieces.get(i)).moveDown();
					}
				}
				addPiece();
			}
			
			
			//up
			if (key == KeyEvent.VK_UP) {
				newTurn();
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationY == 1) {
						((NumberPiece) pieces.get(i)).moveUp();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationY == 2) {
						((NumberPiece) pieces.get(i)).moveUp();
					}
				}
				for (int i = 0; i < pieces.size(); i++) {
					if (((NumberPiece) pieces.get(i)).pieceLocationY == 3) {
						((NumberPiece) pieces.get(i)).moveUp();
					}
				}
				addPiece();
				/*System.out.println("running updates");
				for (Object piece : pieces) {
					((NumberPiece) piece).updateValue();
				}*/
			}
			
			if (key == KeyEvent.VK_D) {
				for (int i = 0; i < 4; i++) {
					for (int i2 = 0; i2 < 4; i2++) {
						System.out.print(location[i * 4 + i2]);
					}
					System.out.println();
				}
				System.out.println("=============");
				for (Object piece : pieces) {
					((NumberPiece) piece).printLoc();
				}
			}
			
			if (key == KeyEvent.VK_U) {
				for (Object piece : pieces) {
					((NumberPiece) piece).updateValue();
				}
				repaint();
			}
			
			
			
		}

		@Override
		public void keyReleased(KeyEvent e) {}
	}
	
	/**
	 * Add new piece to the board
	 */
	public void addPiece() {
		NumberPiece piece = new NumberPiece(this);
		pieces.add(piece);
		this.add(piece);
		this.repaint();
	}

	/**
	 * Run updates for all game pieces
	 */
	public void updateAll() {
		// TODO Auto-generated method stub
		repaint();
		for (Object piece : pieces) {
			((NumberPiece) piece).updateValue();
		}
		keepScore.setText("Score: " + score);
	}
	
	/**
	 * repaint
	 */
	public void animationUpdate() {
		this.repaint();
	}
	
}

