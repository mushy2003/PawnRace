package utils;

public class Position {
  private final File file;
  private final Rank rank;

  public Position(String position) {
    file = new File(Character.toLowerCase(position.charAt(0)));
    rank = new Rank(Integer.parseInt(String.valueOf(position.charAt(1))));
  }

  public Position(int rankIndex, int fileIndex) {
    file = new File((char) ('a' + fileIndex));
    rank = new Rank(rankIndex + 1);
  }

  public File getFile() {
    return file;
  }

  public Rank getRank() {
    return rank;
  }

  public int getFileIndex() {
    return file.getBoardIndex();
  }

  public int getRankIndex() {
    return rank.getBoardIndex();
  }

  @Override
  public String toString() {
    return String.valueOf(file.getColumnLetter()) + rank.getRowNumber();
  }
}
