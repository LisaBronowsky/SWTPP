package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.util.ArrayList;
import java.util.regex.*;

public class Moves {
	
	public boolean validateString(String move) {
		if (!Pattern.matches("[a-i]\\d[-][a-i]\\d[-][0-7]", move)){
			return false;
		}
		if (move.substring(0, 3) == move.substring(3, 6) && Pattern.matches("......[1-7]", move)) {
			return true;
		} else if (move.substring(0, 3) != move.substring(3, 6) && Pattern.matches("......0", move)) {
			return (distance(move) == 1) ? true : false;
		}
		return false;
	}
	
	public int distance(String move) {
		int column = Math.abs(move.charAt(0) - move.charAt(3));
		int row = Math.abs(move.charAt(1) - move.charAt(4));
		if (Math.abs(row-column) == 0) {
			return row;
		} else if (Pattern.matches("[1-3]0", "" + row + column) || Pattern.matches("0[1-3]", "" + row + column)) {
			return row+column;
		} 
		return -1;
	}
	
	private int direction(String move) {
		if (move.charAt(0) < move.charAt(3)) {
			if (move.charAt(1) < move.charAt(4)) {
				return 1;
			} else if (move.charAt(1) > move.charAt(4)) {
				return 3;
			} else {
				return 2;
			}
		} else if (move.charAt(0) > move.charAt(3)) {
			if (move.charAt(1) < move.charAt(4)) {
				return 7;
			} else if (move.charAt(1) > move.charAt(4)) {
				return 5;
			} else {
				return 6;
			}			
		} else {
			if (move.charAt(1) < move.charAt(4)) {
				return 0;
			} else if (move.charAt(1) > move.charAt(4)) {
				return 4;
			}
		}
		return -1;
	}
	
	public boolean moveShield(Token t, String move, ArrayList<Token> board) {
		if (move.substring(0, 3) == move.substring(3, 6) && Pattern.matches("......[1-7]", move)) {
			turnOnInput(move, t);
			return true;
		} else if (distance(move) == 1 && Pattern.matches("......[1-7]", move)) {
			int dirEqOri = t.getOrientation().charAt(direction(move)) - 48;
			if (dirEqOri == 0 || board.get(calculateIndex(move.substring(3, 6))).getSuit() == t.getSuit()) {
				return false;
			} else {
				t.column = move.charAt(4);
				t.row = move.charAt(3);
			}
			turnOnInput(move, t);
			return true;		
		} else if (distance(move) == 1 && Pattern.matches("......[0]", move)) {
			int dirEqOri = t.getOrientation().charAt(direction(move)) - 48;
			if (dirEqOri == 0 || board.get(calculateIndex(move.substring(3, 6))).getSuit() == t.getSuit()) {
				return false;
			} else {
				t.column = move.charAt(4);
				t.row = move.charAt(3);
			}
			return true;
		}
		return false;
	}

	public boolean moveProbe(Token t, String move, ArrayList<Token> board) {
		if (move.substring(0, 3) == move.substring(3, 6) && Pattern.matches("......[1-7]", move)) {
			turnOnInput(move, t);
			return true;		
		} else if ((distance(move) == 1 || distance(move) == 2) && Pattern.matches("......[0]", move)) {
			int dirEqOri = t.getOrientation().charAt(direction(move)) - 48;
			if (dirEqOri == 0 || board.get(calculateIndex(move.substring(3, 6))).getSuit() == t.getSuit()) {
				return false;
			} else {
				t.column = move.charAt(4);
				t.row = move.charAt(3);
			}
			return true;
		}
		return false;
	}
	
	public boolean moveLance(Token t, String move, ArrayList<Token> board) {
		if (move.substring(0, 3) == move.substring(3, 6) && Pattern.matches("......[1-7]", move)) {
			turnOnInput(move, t);
			return true;		
		} else if ((distance(move) == 1 || distance(move) == 2 || distance(move) == 3) && Pattern.matches("......[0]", move)) {
			int dirEqOri = t.getOrientation().charAt(direction(move)) - 48;
			if (dirEqOri == 0 || board.get(calculateIndex(move.substring(3, 6))).getSuit() == t.getSuit()) {
				return false;
			} else {
				t.column = move.charAt(4);
				t.row = move.charAt(3);
			}
			return true;
		}
		return false;
	}

	public boolean moveCommander(Token t, String move, ArrayList<Token> board) {
		if (move.substring(0, 3) == move.substring(3, 6) && Pattern.matches("......[1-7]", move)) {
			turnOnInput(move, t);
			return true;
		} else if (distance(move) == 1 && Pattern.matches("......[0]", move)) {
			int dirEqOri = t.getOrientation().charAt(direction(move)) - 48;
			if (dirEqOri == 0 || board.get(calculateIndex(move.substring(3, 6))).getSuit() == t.getSuit()) {
				return false;
			} else {
				t.column = move.charAt(4);
				t.row = move.charAt(3);
			}
			return true;
		}
		return false;
	}
	
	private void turnOnInput(String move, Token t) {
		String newOrientation = t.getOrientation();
		t.setOrientation(newOrientation.substring(move.charAt(move.length()-1)) + newOrientation.substring(0, move.charAt(move.length())));
	}
	
	public int calculateIndex(String position) {
		if (Pattern.matches("[a-i]\\d", position)) {
			return 9*(9-(position.charAt(1)-48))+(position.charAt(0)-97);
		}
		return -1;
	}
}
