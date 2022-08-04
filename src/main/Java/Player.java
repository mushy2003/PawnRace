import utils.Move;
import utils.Piece;
import utils.Position;

import java.util.List;

public class Player {

  private final Piece piece;
  private Player opponent;
  private final Board board;
  private final Game game;

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
    //TODO
    return null;
  }

  public Player getOpponent() {
    return opponent;
  }

  public Piece getPiece() {
    return piece;
  }
}
