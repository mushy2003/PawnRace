package utils;

public final class File {
  private final char columnLetter;

  public File(char columnLetter) {
    assert (columnLetter >= 'a' && columnLetter <= 'h');
    this.columnLetter = Character.toLowerCase(columnLetter);
  }

  public int getBoardIndex() {
    return columnLetter - 'a';
  }

  public char getColumnLetter() {
    return columnLetter;
  }
}
