package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;



public class NumberPiece extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int value;
	public int pieceLocationX, pieceLocationY;
	public int id;
	private JPanel gameBoard;
	public boolean addedThisTurn;
	
	
	public void printLoc() {
		System.out.println("Piece value: " + value + " in x: " + pieceLocationX + " y: " + pieceLocationY);
	}
	/**
	 * Updates the picture
	 */
	private void updateNumber() {
		String fileName = "";
		
		switch (this.value) {
		case 2:
			fileName = "2.png";
			break;
		case 4:
			fileName = "4.png";
			break;
		case 8:
			fileName = "8.png";
			break;
		case 16:
			fileName = "16.png";
			break;
		case 32:
			fileName = "32.png";
			break;
		case 64:
			fileName = "64.png";
			break;
		case 128:
			fileName = "128.png";
			break;
		case 256:
			fileName = "256.png";
			break;
		case 512:
			fileName = "512.png";
			break;
		case 1024:
			fileName = "1024.png";
			break;
		case 2048:
			fileName = "2048.png";
			mainGame.win();
		}
		//System.out.println("File: " + fileName);
		try {
			//System.out.println("Setting Label picture");
			this.setIcon(new ImageIcon(ImageIO.read(new File("textures/" + fileName))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addPieceAnimation(int x, int y) {
		int delay = 2;
		Timer timer = new Timer(delay, null);
		int count = 20;
		class addPieceAnimationActionListener implements ActionListener {
			
			private Timer timer;
			private int timesLeft = count;
			private int size = 110;
			private boolean stop = true;
			
			public addPieceAnimationActionListener(Object time) {
				timer = (Timer) time;
				
			}
			
			public void actionPerformed(ActionEvent e) {
				//System.out.println("adding piece animation");
				if (timesLeft > 0 && stop) {
					size--;
					timesLeft--;
					
				} else if (timesLeft < count) {
					stop = false;
					size++;
					timesLeft++;
				} else {
					timer.stop();
				}
				setBounds(x, y, size, size);
				
			}
		}
		timer.addActionListener(new addPieceAnimationActionListener(timer));
		timer.start();
		
	}
	
	
	public NumberPiece(Object board) {
		
		gameBoard = (JPanel) board;
		Random rand = new Random();
		int num = rand.nextInt(4) + 1;
		if (num == 1) {
			value = 4;
		} else {
			value = 2;
		}
		//value = (rand.nextInt(2) + 1) * 2;
		
		this.id = mainGame.count;
		mainGame.count++;
		
		Boolean keepLooking = true;
		
		while (keepLooking) {
			int x = rand.nextInt(4);
			int y = rand.nextInt(4);
			if (((GameBoard) gameBoard).location[y * 4 + x] == 0) {
				this.setBounds(((GameBoard) gameBoard).locationX[x], ((GameBoard) gameBoard).locationY[y], 110, 110);
				addPieceAnimation(((GameBoard) gameBoard).locationX[x], ((GameBoard) gameBoard).locationY[y]);
				((GameBoard) gameBoard).location[y * 4 + x] = this.value;
				keepLooking = false;
				pieceLocationX = x;
				pieceLocationY = y;
			}
		}

		updateNumber();

	}
	
	
	public void updateValue() {
		if (value != ((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX]) {
			addedThisTurn = true;
		}
		this.value = ((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX];
		updateNumber();
		this.setBounds(((GameBoard) gameBoard).locationX[this.pieceLocationX], ((GameBoard) gameBoard).locationY[this.pieceLocationY], 110, 110);
	}
	
	
	public boolean tryToUpgrade(int y, int x) {
		// Default state is clear
		boolean clear = true;
		
		//if the values are the same
		if (this.value == ((GameBoard) gameBoard).location[y * 4 + x]) {
			
			// Check for issues in Y
			if (this.pieceLocationY == y) {
				for (int i = Math.min(this.pieceLocationX, x) + 1; i < Math.max(this.pieceLocationX, x); i++) {
					if (((GameBoard) gameBoard).location[y * 4 + i] != 0) {
						clear = false;
					}
				}
			} 
			
			// Check for issues in x
			if (this.pieceLocationX == x) {
				for (int i = Math.min(this.pieceLocationY, y) + 1; i < Math.max(this.pieceLocationY, y); i++) {
					if (((GameBoard) gameBoard).location[i * 4 + x] != 0) {
						clear = false;
					}
				}
			}
			
			// Check if piece was upgraded already
			for (Object piece : ((GameBoard) gameBoard).pieces) {
				if (((NumberPiece) piece).pieceLocationX == x && ((NumberPiece) piece).pieceLocationY == y) {
					if (((NumberPiece) piece).addedThisTurn) {
						clear = false;
					}
				}
			}
			
			
			// If can upgrade
			if (clear) {
				// Run animation
				if (this.pieceLocationX == x && Math.max(this.pieceLocationY, y) == y  || this.pieceLocationY == y && Math.max(this.pieceLocationX, x)  == x) {
					animationRD(x, y);
				} else {
					animationLU(x, y);
				}
				// Log
				System.out.println("adding to : " + y + ", " + x);
				
				// Change value
				((GameBoard) gameBoard).location[y * 4 + x] *= 2;
				// Change score
				((GameBoard) gameBoard).score += ((GameBoard) gameBoard).location[y * 4 + x];
				
				// Log remove
				System.out.println("removing from: " + this.pieceLocationY + ", " + this.pieceLocationX);
				
				// Remove
				((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = 0;
				((GameBoard) gameBoard).pieces.remove(this);
				
				// Update
				((GameBoard) gameBoard).updateAll();
				//this.disable();
				this.setVisible(false);
				
			}
		} else {
			clear = false;
		}
		return clear;
	}

	
	private void animationRD(int x, int y) {
		
		int startLocY = ((GameBoard) gameBoard).locationY[this.pieceLocationY];
		int startLocX = ((GameBoard) gameBoard).locationX[this.pieceLocationX];
		int delay = 2;
		Timer timer = new Timer(delay, null);
		
		class AnimationActionListener implements ActionListener {
			
			private int locY = startLocY;
			private int locX = startLocX;
			private Timer timer;
			
			public AnimationActionListener(Object time) {
				timer = (Timer) time;
				
			}
			
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Moving: " + value + ", locX: " + locX + ", LocY: " + locY + ", x: " + x + ", Y: " + y);
				setBounds(locX, locY, 110, 110);
				if (locY != y) {
					locY+=8;
				}
				if (locX != x) {
					locX+=8;
				}
				if (locY >= y && locX >= x) {
					timer.stop();
					System.out.println("Piece value: " + value + ", locX: " + locX + ", LocY: " + locY + ", x: " + x + ", Y: " + y);
					setBounds(x, y, 110, 110);
				}
			}
		}
		ActionListener taskPerformer = new AnimationActionListener(timer);
		timer.addActionListener(taskPerformer);
		timer.start();
		
	}
	
	private void animationLU(int x, int y) {
		
		int startLocY = ((GameBoard) gameBoard).locationY[this.pieceLocationY];
		int startLocX = ((GameBoard) gameBoard).locationX[this.pieceLocationX];
		int delay = 2;
		Timer timer = new Timer(delay, null);
		
		class AnimationActionListener implements ActionListener {
			
			private int locY = startLocY;
			private int locX = startLocX;
			private Timer timer;
			
			public AnimationActionListener(Object time) {
				timer = (Timer) time;
				
			}
			
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Moving: " + value + ", locX: " + locX + ", LocY: " + locY + ", x: " + x + ", Y: " + y);
				setBounds(locX, locY, 110, 110);
				if (locY != y) {
					locY-=8;
				}
				if (locX != x) {
					locX-=8;
				}
				if (locY <= y && locX <= x) {
					timer.stop();
					System.out.println("Piece value: " + value + ", locX: " + locX + ", LocY: " + locY + ", x: " + x + ", Y: " + y);
					setBounds(x, y, 110, 110);
				}
			}
		}
		ActionListener taskPerformer = new AnimationActionListener(timer);
		timer.addActionListener(taskPerformer);
		timer.start();
		
	}
	
	public void moveLeft() {
		if (this.pieceLocationX != 0) {
			int tryX = 0;
			while (true) {
				if (((GameBoard) gameBoard).location[this.pieceLocationY * 4 + tryX] == 0) {
					animationLU(((GameBoard) gameBoard).locationX[tryX], ((GameBoard) gameBoard).locationY[this.pieceLocationY]);
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = 0;
					this.pieceLocationX = tryX;
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = this.value;
					break;
				} else {
					if (tryToUpgrade(this.pieceLocationY, tryX)) {
						break;
					}
				}
				tryX++;
				if (tryX >= this.pieceLocationX) {
					System.out.println("Couldn't move piece with value: " + value + " in x: " + this.pieceLocationX + " y: " + this.pieceLocationY);
					break;
				}
			}
		}
	}
	
	
	public void moveRight() {
		if (this.pieceLocationX != 3) {
			int tryX = 3;
			while (true) {
				if (((GameBoard) gameBoard).location[this.pieceLocationY * 4 + tryX] == 0) {
					animationRD(((GameBoard) gameBoard).locationX[tryX], ((GameBoard) gameBoard).locationY[this.pieceLocationY]);
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = 0;
					this.pieceLocationX = tryX;
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = this.value;
					break;
				} else {
					if (tryToUpgrade(this.pieceLocationY, tryX)) {
						break;
					}
				}
				tryX--;
				if (tryX <= this.pieceLocationX ) {
					break;
				}
			}
		}
		
	}
	
	
	public void moveUp() {
		if (this.pieceLocationY != 0) {
			int tryY = 0;
			while (true) {
				if (((GameBoard) gameBoard).location[tryY * 4 + this.pieceLocationX] == 0) {
					animationLU(((GameBoard) gameBoard).locationX[this.pieceLocationX], ((GameBoard) gameBoard).locationY[tryY]);
					
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = 0;
					this.pieceLocationY = tryY;
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = this.value;
					break;
				} else {
					if (tryToUpgrade(tryY, this.pieceLocationX)) {
						break;
					}
				}
				tryY++;
				if (tryY >= this.pieceLocationY) {
					break;
				}
			}
		}
	}
	
	
	public void moveDown() {
		if (this.pieceLocationY != 3) {
			int tryY = 3;
			while (true) {
				if (((GameBoard) gameBoard).location[tryY * 4 + this.pieceLocationX] == 0) {
					//this.setBounds(((GameBoard) gameBoard).locationX[this.pieceLocationX], ((GameBoard) gameBoard).locationY[tryY], 110, 110);
					animationRD(((GameBoard) gameBoard).locationX[this.pieceLocationX], ((GameBoard) gameBoard).locationY[tryY]);
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = 0;
					this.pieceLocationY = tryY;
					((GameBoard) gameBoard).location[this.pieceLocationY * 4 + this.pieceLocationX] = this.value;
					break;
				} else {
					if (tryToUpgrade(tryY, this.pieceLocationX)) {
						break;
					}
				}
				tryY--;
				if (tryY <= this.pieceLocationY) {
					break;
				}
			}
		};
	}
	
}

