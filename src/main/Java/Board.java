import utils.File;
import utils.Move;
import utils.Piece;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {

  public final int MAXINDEX = 7;
  private final Piece[][] boardGrid = new Piece[MAXINDEX + 1][MAXINDEX + 1];

  public Board(File whiteGap, File blackGap) {
    for (int i = 0; i <= MAXINDEX; i++) {
      if (i != whiteGap.getBoardIndex()) {
        boardGrid[1][i] = Piece.WHITE;
      }

      if (i != blackGap.getBoardIndex()) {
        boardGrid[6][i] = Piece.BLACK;
      }
    }
  }

  public Optional<Piece> pieceAt(Position position) {
    return Optional.ofNullable(
        boardGrid[position.getRank().getBoardIndex()][position.getFile().getBoardIndex()]);
  }

  public List<Position> positionsOf(Piece piece) {
    List<Position> positions = new ArrayList<>();

    for (int i = 0; i <= MAXINDEX; i++) {
      for (int j = 0; j <= MAXINDEX; j++) {
        if (boardGrid[i][j] == piece) {
          positions.add(new Position(i, j));
        }
      }
    }

    return positions;
  }

  public boolean isValidMove(Move move, Move prevMove) {
    int currRank = move.getEnd().getRankIndex();
    int currFile = move.getEnd().getFileIndex();
    int vertical = currRank - move.getStart().getRankIndex();
    int horizontal = currFile - move.getStart().getFileIndex();

    if (currRank > 7 || currFile > 7 || currRank < 0 || currFile < 0) {
      return false;
    }

    if (pieceAt(move.getStart()).isEmpty() || pieceAt(move.getStart()).get() != move.getPiece()) {
      return false;
    }
    switch (move.getMoveType()) {
      case PEACEFUL -> {
        if (pieceAt(move.getEnd()).isPresent()) {
          return false;
        }
        if (move.getPiece() == Piece.WHITE) {
          return (move.getStart().getRankIndex() == 1 && vertical <= 2 && vertical > 0 && horizontal == 0)
                  || (vertical == 1 && horizontal == 0);
        } else {
          return (move.getStart().getRankIndex() == 6 && vertical >= -2 && vertical < 0 && horizontal == 0)
                  || (vertical == -1 && horizontal == 0);
        }
      }
      case CAPTURE -> {
        if (pieceAt(move.getEnd()).isEmpty() || (horizontal != 1 && horizontal != -1)) {
          return false;
        }
        if (move.getPiece() == Piece.WHITE) {
          return (vertical == 1 && pieceAt(move.getEnd()).get() == Piece.BLACK);
        } else {
          return (vertical == -1 && pieceAt(move.getEnd()).get() == Piece.WHITE);
        }
      }
      default -> {
        //En-Passant move.
        if (pieceAt(move.getEnd()).isPresent() || prevMove == null) {
          return false;
        }

        int prevRank = prevMove.getEnd().getRankIndex();
        int prevFile = prevMove.getEnd().getFileIndex();
        int prevVertical = prevRank - prevMove.getStart().getRankIndex();
        if (prevMove.getMoveType() != Move.MoveType.PEACEFUL
                || (prevMove.getPiece() == Piece.WHITE && prevVertical != 2)
                || (prevMove.getPiece() == Piece.BLACK && prevVertical != -2)) {
          return false;
        }

        if (move.getPiece() == Piece.WHITE) {
          return (vertical == 1
                  && (horizontal == 1 || horizontal == -1)
                  && currRank == prevRank + 1
                  && currFile == prevFile);
        } else {
          return (vertical == -1
                  && (horizontal == 1 || horizontal == -1)
                  && currRank == prevRank - 1
                  && currFile == prevFile);
        }
      }
    }
  }

  public Board move(Move move) {
    int newRankIndex = move.getEnd().getRankIndex();
    int newFileIndex = move.getEnd().getFileIndex();
    int oldRankIndex = move.getStart().getRankIndex();
    int oldFileIndex = move.getStart().getFileIndex();

    boardGrid[newRankIndex][newFileIndex] = move.getPiece();

    if (move.getMoveType() == Move.MoveType.EN_PASSANT) {
      if (move.getPiece() == Piece.WHITE) {
        boardGrid[newRankIndex - 1][newFileIndex] = null;
      } else {
        boardGrid[newRankIndex + 1][newFileIndex] = null;
      }
    }

    boardGrid[oldRankIndex][oldFileIndex] = null;

    return this;
  }

  public void unApplyMove(Move move) {
    int newRankIndex = move.getEnd().getRankIndex();
    int newFileIndex = move.getEnd().getFileIndex();
    int oldRankIndex = move.getStart().getRankIndex();
    int oldFileIndex = move.getStart().getFileIndex();

    boardGrid[newRankIndex][newFileIndex] = null;
    if (move.getMoveType() == Move.MoveType.CAPTURE) {
      boardGrid[newRankIndex][newFileIndex] = move.getPiece().getOpposite();
    }

    if (move.getMoveType() == Move.MoveType.EN_PASSANT) {
      if (move.getPiece() == Piece.WHITE) {
        boardGrid[newRankIndex - 1][newFileIndex] = Piece.BLACK;
      } else {
        boardGrid[newRankIndex + 1][newFileIndex] = Piece.WHITE;
      }
    }

    boardGrid[oldRankIndex][oldFileIndex] = move.getPiece();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("   A B C D E F G H  \n");

    for (int i = MAXINDEX; i >= 0; i--) {
      sb.append((char) (i + 1 + '0')).append("  ");
      for (int j = 0; j <= MAXINDEX; j++) {
        if (boardGrid[i][j] == null) {
          sb.append(". ");
        } else {
          sb.append(boardGrid[i][j].toString()).append(" ");
        }
      }
      sb.append(" ");
      sb.append((char) (i + 1 + '0')).append("\n");
    }

    sb.append("   A B C D E F G H  ");

    return sb.toString();
  }
}
