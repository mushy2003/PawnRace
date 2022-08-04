import utils.Move;
import utils.Move.MoveType;
import utils.Piece;
import utils.Position;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class Game {

  private final Board board;
  private final Deque<Move> moves = new ArrayDeque<>();
  private Player currentPlayer;

  public Game(Board board, Player currentPlayer) {
    this.board = board;
    this.currentPlayer = currentPlayer;
  }

  public void applyMove(Move move) {
    if (board.isValidMove(move, moves.peekFirst())) {
      board.move(move);
      moves.push(move);
      currentPlayer = currentPlayer.getOpponent();
    }
  }

  public void unApplyMove() {
    if (!moves.isEmpty()) {
      board.unApplyMove(moves.removeFirst());
      currentPlayer = currentPlayer.getOpponent();
    }
  }

  public List<Move> allValidMoves(Piece piece) {
    List<Move> allValidMoves = new ArrayList<>();
    List<Position> positionsList = board.positionsOf(piece);

    for (Position position : positionsList) {
      validMoves(position, allValidMoves);
    }

    return allValidMoves;
  }

  public boolean gameOver() {
    return (allValidMoves(Piece.WHITE).isEmpty() || allValidMoves(Piece.BLACK).isEmpty());
  }

  public Player winner() {
    assert (gameOver());

    if (allValidMoves(Piece.WHITE).isEmpty() && allValidMoves(Piece.BLACK).isEmpty()) {
      // Stalemate.
      return null;
    }

    if (allValidMoves(currentPlayer.getPiece()).isEmpty()) {
      return currentPlayer;
    }

    return currentPlayer.getOpponent();
  }

  public Move parseMove(String san) {
    //TODO
    return null;
  }

  private void validMoves(Position position, List<Move> allValidMoves) {
    Piece piece = board.pieceAt(position).get();
    allValidMoves.add(moveForwardBy(position, 1, piece));
    allValidMoves.add(moveForwardBy(position, 2, piece));
    allValidMoves.add(moveDiagonal(position, true, false, piece, MoveType.CAPTURE));
    allValidMoves.add(moveDiagonal(position, false, true, piece, MoveType.CAPTURE));
    allValidMoves.add(moveDiagonal(position, true, false, piece, MoveType.EN_PASSANT));
    allValidMoves.add(moveDiagonal(position, false, true, piece, MoveType.EN_PASSANT));

  }

  private Move moveForwardBy(Position position, int steps, Piece piece) {
    steps = (piece == Piece.BLACK) ? (-1 * steps) : steps;
    Position newPosition = new Position(position.getRankIndex() + steps, position.getFileIndex());
    Move newMove = new Move(piece, position, newPosition, MoveType.PEACEFUL);
    if (board.isValidMove(newMove, moves.peekFirst())) {
      return newMove;
    }

    return null;
  }

  private Move moveDiagonal(Position position, boolean isLeft, boolean isRight, Piece piece, MoveType moveType) {
    int horizontalSteps = (isLeft) ? -1 : 1;
    int verticalSteps = (piece == Piece.WHITE) ? 1 : -1;

    Move newMove =
        new Move(
            piece,
            position,
            new Position(position.getRankIndex() + verticalSteps, position.getFileIndex() + horizontalSteps),
            moveType);

    if (board.isValidMove(newMove, moves.peekFirst())) {
      return newMove;
    }

    return null;
  }
}
