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

  public int numberOfPassedPawns() {
    List<Position> positions = board.positionsOf(piece);
    int count = 0;

    for (Position position : positions) {
      if (isPassedPawn(position)) {
        count++;
      }
    }

    return count;
  }

  public boolean isPassedPawn(Position position) {
    if (board.pieceAt(position).isEmpty()) {
      return false;
    }
    List<Position> opponentPawns = opponent.getAllPawns();
    for (Position oppPosition : opponentPawns) {
      if (piece == Piece.WHITE
          && position.getRankIndex() < oppPosition.getRankIndex()
          && Math.abs(position.getFileIndex() - oppPosition.getFileIndex()) <= 1) {
        return false;
      }

      if (piece == Piece.BLACK
          && position.getRankIndex() > oppPosition.getRankIndex()
          && Math.abs(position.getFileIndex() - oppPosition.getFileIndex()) <= 1) {
        return false;
      }
    }

    return true;
  }

  public Move makeRandomMove(Game game) {
    List<Move> validMoves = getAllValidMoves();
    Random generator = new Random();
    Move randomMove = validMoves.get(generator.nextInt(validMoves.size() - 1));
    System.out.println("Ai played the move: " + randomMove);
    game.applyMove(randomMove);
    return randomMove;
  }

  public Move makeMove(Game game) {
    List<Move> moves = getAllValidMoves();
    int maxScore = Integer.MIN_VALUE;
    Move bestMove = null;
    Player currentPlayer = (piece == Piece.WHITE) ? this : opponent;

    for (Move move : moves) {
      game.applyMove(move);
      int currentScore = minimax(6, Integer.MIN_VALUE, Integer.MAX_VALUE, currentPlayer.getOpponent());
      if (currentScore > maxScore) {
        maxScore = currentScore;
        bestMove = move;
      }
      game.unApplyMove();
    }

    game.applyMove(bestMove);

    return bestMove;
  }

  private int minimax(int depth, int alpha, int beta, Player currentPlayer) {
    if (currentPlayer.game.gameOver() && game.winner() == null) {
      return 0;
    }

    if (currentPlayer.game.gameOver() && game.winner().piece == Piece.WHITE) {
      return 100000;
    }

    if (currentPlayer.game.gameOver() && game.winner().piece == Piece.BLACK) {
      return -100000;
    }

    if (depth == 0) {
      if (currentPlayer.piece == Piece.WHITE) {
        return evaluateScore(currentPlayer);
      }

      return evaluateScore(currentPlayer) * -1;
      //return board.positionsOf(Piece.WHITE).size() - board.positionsOf(Piece.BLACK).size();
    }

    if (currentPlayer.piece == Piece.WHITE) {
      List<Move> moves = currentPlayer.getAllValidMoves();
      int maxScore = Integer.MIN_VALUE;

      for (Move move : moves) {
        currentPlayer.game.applyMove(move);
        int currentScore = minimax(depth - 1, alpha, beta, currentPlayer.getOpponent());
        currentPlayer.game.unApplyMove();
        maxScore = Math.max(maxScore, currentScore);
        alpha = Math.max(currentScore, alpha);
        if (beta <= alpha) {
          break;
        }
      }

      return maxScore;
    }

    List<Move> moves = currentPlayer.getAllValidMoves();
    int minScore = Integer.MAX_VALUE;

    for (Move move : moves) {
      currentPlayer.game.applyMove(move);
      int currentScore = minimax(depth - 1, alpha, beta, currentPlayer.getOpponent());
      currentPlayer.game.unApplyMove();
      minScore = Math.min(minScore, currentScore);
      beta = Math.min(currentScore, beta);
      if (beta <= alpha) {
        break;
      }
    }

    return minScore;
  }

  private int evaluateScore(Player currentPlayer) {
    Board board = currentPlayer.board;
    Piece piece = currentPlayer.piece;
    Player opponent = currentPlayer.opponent;
    List<Position> positions = board.positionsOf(piece);

    int currentScore = board.positionsOf(piece).size() - board.positionsOf(piece.getOpposite()).size();
    currentScore += (currentPlayer.numberOfPassedPawns() - opponent.numberOfPassedPawns()) * 10;

    for (Position position : positions) {
      if (piece == Piece.WHITE && position.getRankIndex() >= 5) {
        currentScore += 100;
      }



      if (piece == Piece.BLACK && position.getRankIndex() <= 2) {
        currentScore += 100;
      }
    }

    currentScore += numberOfPawnChains(currentPlayer.piece);

    return currentScore;
  }

  private int numberOfPawnChains(Piece piece) {
    int count = 0;
    List<Position> positions = board.positionsOf(piece);

    for (Position position : positions) {
      if (position.getRankIndex() < board.MAXINDEX && position.getRankIndex() >= 1 && position.getFileIndex() >= 1 && position.getFileIndex() < board.MAXINDEX) {
        count += pawnChains(position, piece);
      }
    }

    return count;
  }

  private int pawnChains(Position position, Piece piece) {
    int count = 0;
    Position pos1 = new Position(position.getRankIndex() - 1, position.getFileIndex() - 1);
    Position pos2 = new Position(position.getRankIndex() - 1, position.getFileIndex() + 1);
    Position pos3 = new Position(position.getRankIndex() + 1, position.getFileIndex() - 1);
    Position pos4 = new Position(position.getRankIndex() + 1, position.getFileIndex() + 1);

    if (board.pieceAt(pos1).orElse(piece.getOpposite()) == piece) {
      count++;
    }

    if (board.pieceAt(pos2).orElse(piece.getOpposite()) == piece) {
      count++;
    }

    if (board.pieceAt(pos3).orElse(piece.getOpposite()) == piece) {
      count++;
    }

    if (board.pieceAt(pos4).orElse(piece.getOpposite()) == piece) {
      count++;
    }

    return count;
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
