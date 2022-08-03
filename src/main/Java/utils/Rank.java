package utils;

public final class Rank {

  private final int rowNumber;

  public Rank(int rowNumber) {
    assert (rowNumber >= 1 && rowNumber <= 8);
    this.rowNumber = rowNumber;
  }

  public char getRowNumber() {
    // returns the number as a character.
    return (char) (rowNumber + '0');
  }

  public int getBoardIndex() {
    return rowNumber - 1;
  }
}
