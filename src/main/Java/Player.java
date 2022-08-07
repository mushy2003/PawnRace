import utils.Move;
import utils.Piece;
import utils.Position;

import java.util.List;
import java.util.Random;

public class Player {

  private final Piece piece;
  private final Board board;
  private final Game game;
  private Player opponent;

  public Player(Piece piece, Player opponent, Board board, Game game) {
    this.piece = piece;
    this.opponent = opponent;
    this.board = board;
    this.game = game;
  }

  public List<Position> getAllPawns() {
    return board.positionsOf(piece);
  }

  public List<Move> getAllValidMoves() {
    return game.allValidMoves(piece);
  }

  public boolean isPassedPawn(Position position) {
    if (board.pieceAt(position).isEmpty()) {
      return false;
    }
    List<Position> opponentPawns = opponent.getAllPawns();
    for (Position oppPosition : opponentPawns) {
      if (piece == Piece.WHITE && position.getRankIndex() <= oppPosition.getRankIndex()) {
        return false;
      }

      if (piece == Piece.BLACK && position.getRankIndex() >= oppPosition.getRankIndex()) {
        return false;
      }
    }

    return true;
  }

  public Move makeMove(Game game) {
    List<Move> validMoves = getAllValidMoves();
    Random generator = new Random();
    Move randomMove = validMoves.get(generator.nextInt(validMoves.size() - 1));
    System.out.println("Ai played the move: " + randomMove);
    game.applyMove(randomMove);
    return randomMove;
  }

  public Player getOpponent() {
    return opponent;
  }

  public void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  public Piece getPiece() {
    return piece;
  }
}
