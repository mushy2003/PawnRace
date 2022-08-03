package utils;

public enum Piece {
  BLACK,
  WHITE;

  public Piece getOpposite() {
    assert (this == BLACK || this == WHITE);
    if (this == BLACK) {
      return WHITE;
    }

    return BLACK;
  }

  @Override
  public String toString() {
    assert (this == BLACK || this == WHITE);
    if (this == BLACK) {
      return "B";
    }

    return "W";
  }
}
