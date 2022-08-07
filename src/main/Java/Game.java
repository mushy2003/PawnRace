import utils.Move;
import utils.Move.MoveType;
import utils.Piece;
import utils.Position;

import java.util.*;

public final class Game {

  private final Board board;
  private final Deque<Move> moves = new ArrayDeque<>();
  private Player currentPlayer;

  public Game(Board board, Player currentPlayer) {
    this.board = board;
    this.currentPlayer = currentPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
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

    allValidMoves.removeAll(Collections.singletonList(null));

    return allValidMoves;
  }

  public boolean gameOver() {
    return (allValidMoves(Piece.WHITE).isEmpty()
        || allValidMoves(Piece.BLACK).isEmpty()
        || whitePromoted()
        || blackPromoted());
  }

  public Player winner() {
    assert (gameOver());

    if (allValidMoves(Piece.WHITE).isEmpty() && allValidMoves(Piece.BLACK).isEmpty()) {
      // Stalemate.
      return null;
    } else if (allValidMoves(currentPlayer.getPiece()).isEmpty()) {
      return currentPlayer.getOpponent();
    } else if (allValidMoves(currentPlayer.getOpponent().getPiece()).isEmpty()) {
      return currentPlayer;
    }

    return (currentPlayer.getPiece() == Piece.WHITE && whitePromoted())
        ? currentPlayer
        : currentPlayer.getOpponent();
  }

  public Move parseMove(String san) {
    Piece piece = this.currentPlayer.getPiece();
    Position start;
    Position end;
    MoveType moveType;
    Move result;

    if (san.length() == 2) {
      moveType = MoveType.PEACEFUL;
      end = new Position(san);

      int oldRank = (piece == Piece.WHITE) ? end.getRankIndex() - 1 : end.getRankIndex() + 1;
      start = new Position(oldRank, end.getFileIndex());
      result = new Move(piece, start, end, moveType);
      if (board.isValidMove(result, moves.peekFirst())) {
        return result;
      }

      if (end.getRankIndex() == 3 && piece == Piece.WHITE
          || (end.getRankIndex() == 4 && piece == Piece.BLACK)) {
        // So possible to move from start position 2 steps
        oldRank = (piece == Piece.WHITE) ? oldRank - 1 : oldRank + 1;
        start = new Position(oldRank, end.getFileIndex());
        result = new Move(piece, start, end, moveType);
        if (board.isValidMove(result, moves.peekFirst())) {
          return result;
        }
      }

      return null;
    }

    end = new Position(san.substring(2));
    int oldFileIndex = san.charAt(0) - 'a';
    int oldRankIndex = (piece == Piece.WHITE) ? end.getRankIndex() - 1 : end.getRankIndex() + 1;
    start = new Position(oldRankIndex, oldFileIndex);
    result = new Move(piece, start, end, MoveType.CAPTURE);
    if (board.isValidMove(result, moves.peekFirst())) {
      return result;
    }

    result.setMoveType(MoveType.EN_PASSANT);

    if (board.isValidMove(result, moves.peekFirst())) {
      return result;
    }

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

  private Move moveDiagonal(
      Position position, boolean isLeft, boolean isRight, Piece piece, MoveType moveType) {
    int horizontalSteps = (isLeft) ? -1 : 1;
    int verticalSteps = (piece == Piece.WHITE) ? 1 : -1;

    Move newMove =
        new Move(
            piece,
            position,
            new Position(
                position.getRankIndex() + verticalSteps, position.getFileIndex() + horizontalSteps),
            moveType);

    if (board.isValidMove(newMove, moves.peekFirst())) {
      return newMove;
    }

    return null;
  }

  private boolean blackPromoted() {
    List<Position> blackPositions = board.positionsOf(Piece.BLACK);
    for (Position blackPosition : blackPositions) {
      if (blackPosition.getRankIndex() == 0) {
        return true;
      }
    }

    return false;
  }

  private boolean whitePromoted() {
    List<Position> whitePositions = board.positionsOf(Piece.WHITE);
    for (Position whitePosition : whitePositions) {
      if (whitePosition.getRankIndex() == 7) {
        return true;
      }
    }

    return false;
  }
}
