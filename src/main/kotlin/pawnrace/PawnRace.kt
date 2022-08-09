package pawnrace

import Board
import Player
import Game
import utils.*
import utils.File
import java.io.PrintWriter
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.Scanner


// You should not add any more member values or member functions to this class
// (or change its name!). The autorunner will load it in via reflection and it
// will be safer for you to just call your code from within the playGame member
// function, without any unexpected surprises!
class PawnRace {
  // Don't edit the type or the name of this method
  // The colour can take one of two values: 'W' or 'B', this indicates your player colour
  fun playGame(colour: Char, output: PrintWriter, input: BufferedReader) {
    // You should call your code from within here
    // Step 1: If you are the black player, you should send a string containing the gaps
    // It should be of the form "wb" with the white gap first and then the black gap: i.e. "AH"
    // TODO: Send gaps with output.println()

    // Regardless of your colour, you should now receive the gaps verified by the autorunner
    // (or by the human if you are using your own main function below), these are provided
    // in the same form as above ("wb"), for example: "AH"
    // TODO: receive the confirmed gaps with input.readLine()

    // Now you may construct your initial board
    // TODO: Initialise the board state
    // If you are the white player, you are now allowed to move
    // you may send your move, once you have decided what it will be, with output.println(move)
    // for example: output.println("axb4")
    // TODO: White player should decide what move to make and send it

    // After point, you may create a loop which waits to receive the other players move
    // (via input.readLine()), updates the state, checks for game over and, if not, decides
    // on a new move and again send that with output.println(move). You should check if the
    // game is over after every move.
    /* TODO: Create the "game loop", which:
          * gets the opponents move
          * updates board
          * checks game over, if not then
          * choose a move
          * send this move
          * update the state
          * check game over
          * rinse, and repeat.
    */

    // Once the loop is over, the game has finished and you may wish to print who has won
    // If your advanced AI has used any files, make sure you close them now!
    // TODO: tidy up resources, if any
  }
}

// When running the command, provide an argument either W or B, this indicates your player colour
fun main(args: Array<String>) {
  //PawnRace().playGame(args[0][0], PrintWriter(System.out, true), BufferedReader(InputStreamReader(System.`in`)))

  val scanner = Scanner(System.`in`)

  var whiteGap : String
  var blackGap : String
  do {
    print("Enter white gap.. between a to h inclusive: ")
    whiteGap = scanner.nextLine()
  } while (Character.toLowerCase(whiteGap.first()) < 'a' || Character.toLowerCase(whiteGap.first()) > 'h')


  do {
    print("Enter black gap.. between a to h inclusive: ")
    blackGap = scanner.nextLine()
  } while (Character.toLowerCase(blackGap.first()) < 'a' || Character.toLowerCase(blackGap.first()) > 'h')

  val board = Board(File(whiteGap.first()), File(blackGap.first()))
  val game = Game(board, null)
  val whitePlayer = Player(Piece.WHITE, null, board, game)
  val blackPlayer = Player(Piece.BLACK, whitePlayer, board, game)
  whitePlayer.opponent = blackPlayer
  game.currentPlayer = whitePlayer


  var text : Char
  do {
    println("Is AIPlayer white or black? (W/B)")
    text = Character.toUpperCase(scanner.nextLine().first())
  } while (text != 'W' && text != 'B')
  val aiPlayer = if (text == 'W') whitePlayer else blackPlayer




  do {
    println(board)

    if (game.currentPlayer != aiPlayer) {
      var move : Move?
      do {
        print("Enter your next move: ")
        val san = scanner.nextLine()
        move = game.parseMove(san)
        if (move != null) {
          game.applyMove(move)
          println("User played the move: " + move)
        }
      } while (move == null)
    } else {
      aiPlayer.makeMove(game)
    }
  } while (!game.gameOver())

  println(board)

  val winningPlayer = game.winner()
  if (winningPlayer == null) {
    println("It's a stalemate.")
  } else if (winningPlayer == whitePlayer) {
    println("White wins!")
  } else {
    println("Black wins!")
  }
}
