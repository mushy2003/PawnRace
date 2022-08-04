package utils;

public class Move {

  private final Piece piece;
  private final Position start;
  private final Position end;
  private final MoveType moveType;

  public Move(Piece piece, Position start, Position end, MoveType moveType) {
    this.piece = piece;
    this.start = start;
    this.end = end;
    this.moveType = moveType;
  }

  @Override
  public String toString() {
    if (moveType == MoveType.PEACEFUL) {
      return end.toString();
    }

    return start.getFile().getColumnLetter() + "x" + end.toString();
  }

  public Piece getPiece() {
    return piece;
  }

  public Position getStart() {
    return start;
  }

  public Position getEnd() {
    return end;
  }

  public MoveType getMoveType() {
    return moveType;
  }

  public enum MoveType {
    PEACEFUL,
    CAPTURE,
    EN_PASSANT
  }
}
