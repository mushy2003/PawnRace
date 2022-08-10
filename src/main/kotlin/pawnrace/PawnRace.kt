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

  val aiPlayer = whitePlayer;

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
      //game.currentPlayer.makeRandomMove(game);
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
