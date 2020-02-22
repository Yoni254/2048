package game;

import javax.swing.*;


public class mainGame {

	static int count = 0;
	private static JFrame currentGame;
	
	public static void win() {
		JOptionPane.showMessageDialog(currentGame, "You Win!");
		currentGame.dispose();
		main(null);
	}
	
	public static void lose() {
		JOptionPane.showMessageDialog(currentGame, "You Lost :(");
		currentGame.dispose();
		main(null);
	}
	
	public static void main(String[] args)  {		
		//creating the frame
		JFrame window = new JFrame("2048");
		currentGame = window;
		GameBoard gameBoard = new GameBoard();
		
		for (int i = 0; i < 2; i++) {
			gameBoard.addPiece();
		}
		
		window.add(gameBoard);
		window.setSize(499, 649);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
	}
}
