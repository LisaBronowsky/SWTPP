package de.tuberlin.sese.swtpp.gameserver.model.ploy;


import java.util.ArrayList;

public class Token implements java.io.Serializable  {
	/**
	 * 
	 */
	public char column;
	public int row;
	private char suit;
	private int figure;
	private ArrayList<Integer> orientation = new ArrayList<Integer>(); 
	
	public Token(char c, int r, String o) {
		this.column = c;
		this.row = r;
		if (o.length() > 0) {
			suit = o.charAt(0);
			this.setOrientation(o.substring(1));
			figure = quersumme(this.orientation);
		} else {
			suit = 'e';
			figure = 0;
		}
	}
	
	
	private int quersumme(ArrayList<Integer> a) {
		int summe = 0;
		for (int element : a) {
			summe +=element;
		}
		return summe;
	}
	
	
	public void setOrientation(String o) {
		int ori = Integer.parseInt(o);
		while (ori != 0) {
			this.orientation.add(ori%2);
			ori /= 2;
		}
		while (orientation.size() < 8) {
			this.orientation.add(0);
		}
	}
	
	public int getFigure() {
		return figure;
	}
	
	public char getSuit() {
		return this.suit;
	}
	
	public String getOrientation() {
		Integer ori = 0;
		if (!this.orientation.isEmpty()) {
			for(int i = 0; i < this.orientation.size(); i++){
				ori += this.orientation.get(i)*((int) Math.pow(2, i));
			}
			if (ori > 0) {
				return ori.toString();
			} else {
				return "";
			}
		} else {
			return "";
		}
	
	}
}