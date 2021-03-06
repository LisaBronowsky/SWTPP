package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

import java.util.ArrayList;

/**
 * Class Cannon extends the abstract class Game as a concrete game instance that
 * allows to play Cannon.
 *
 */
public class PloyGame extends Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424778147226994452L;

	/************************
	 * member
	 ***********************/

	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state
	// TODO: insert additional game data here
	private ArrayList<Token> ployBoard;	
	
	/************************
	 * constructors
	 ***********************/

	public PloyGame() {
		super();

		// TODO: init internal representation
		ployBoard  = new ArrayList<Token>();
		setBoard(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,");
		tryMove("e4-e4-7", blackPlayer);
	}

	public String getType() {
		return "ploy";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.blackPlayer= players.get(0);
				this.whitePlayer = players.get(1);
				nextPlayer = blackPlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (whiteGaveUp())
				gameInfo = "white gave up";
			else if (didWhiteDraw() && !didBlackDraw())
				gameInfo = "white called draw";
			else if (!didWhiteDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "white won";
		}

		return gameInfo;
	}

	@Override
	public String nextPlayerString() {
		return isWhiteNext() ? "w" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(p -> p.requestedDraw())) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) {
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/*******************************************
	 * Helpful stuff
	 ******************************************/

	/**
	 * 
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 * 
	 * @param player
	 * @return
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	@Override
	public void setBoard(String state) {
		// TODO: implement
		String[] rows = state.split("/");
		for (int i = 0; i < rows.length; i++){
			String[] temp = rows[i].split(",");
			String[] field = new String[9];
			for (int j = 0; j < field.length; j++) {
				if (temp.length > j) {
					field[j] = temp[j];
				} else {
					field[j] = "";
				}
				Token newToken = new Token((char) (j+'a'), (9-i), field[j]);
				this.ployBoard.add(newToken);
			}
		}
		// Note: This method is for automatic testing. A regular game would not start at some artificial state. 
		//       It can be assumed that the state supplied is a regular board that can be reached during a game. 
	}

	@Override
	public String getBoard() {
		// TODO: implement and replace dummy with actual board
		String boardToString = "";
		if(this.ployBoard.isEmpty()) {
			throw new RuntimeException("Board is empty!");
		} else {	
			for(Token current : this.ployBoard) {
				if (current.getSuit() == 'e') {
					boardToString += "";
				} else {
					boardToString += current.getSuit();
					boardToString += current.getOrientation();
				}
				if (current.column != 'i') {
					boardToString += ",";
				} else if (current.column == 'i' && current.row != 1){
					
					boardToString += "/";
				}
			}
		return boardToString;
		}
	}

	@Override
	public boolean tryMove(String moveString, Player player) {
		// TODO: implement 
		Moves check = new Moves();
		if(check.validateString(moveString) && player == nextPlayer) {
			int index = check.calculateIndex(moveString.substring(0,2));
			if (index != -1) {
				Token moving = ployBoard.get(index);
				if ((""+ moving.getSuit()) == nextPlayerString()) {
					switch (moving.getFigure()) {
					case 1: 
						return check.moveShield(moving, moveString, ployBoard);
					case 2:
						return check.moveProbe(moving, moveString, ployBoard);
					case 3:
						return check.moveLance(moving, moveString, ployBoard);
					case 4:
						return check.moveCommander(moving, moveString, ployBoard);
					default: return false;
					}
				}
			}			
			return false;
		}
		return false;
	}
}
