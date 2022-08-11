# PawnRace

## Introdution
PawnRace is a game that involves a chess board with only pawns on it. The aim of the game is to be the first player to get one of your pawns to the other side of the board. In this specific version of PawnRace you play against the AI which uses the Minimax algorithm along with Alpha-Beta pruning to search the game tree.

Pull requests welcome to correct any spotted bugs.

## How To Play

* The AI will play with the white pawns and start first. You will play as the black pawns.

* The columns are labelled as letters from a-h and rows labelled as numbers from 1-8. 

* If the square in front is empty, you can move the pawn 1 space forward. You make this move by simply referring to the column and row of the square you want to move to. For example, to move from square a5 to square a4 you simply type in "a4".

* If the pawn is on its starting position you can also move it 2 spaces forward given both squares in front of it are empty. You make this move by simply referring to the column and row of the square you want to move to as above.

* The pawn can move diagonally forward by 1 square if and only if the target square contains a pawn of opposite colour in it. This move is known as a capture. The opposite-coloured pawn is removed from the board and game. To perform a capture you type the column of the original square followed by x followed by the column and row of the destination square. For example, to go from b5 to a4 you would type "bxa4".

* If a pawn of the opposite colour moves forward by 2 positions it can be captured on the square it passed through. This is a combination of the 2 rules above and can only happen on the move immediately after the opposite-coloured pawn has moved forward 2 places. This is known as an En Passant capture. You make this move in the same way as you would make a normal capture move as mentioned above.


* A game is won in 2 ways:
  * One of the 2 players gets one of their pawns to the opposite side of the board first.
  * One of the 2 players has no more valid moves left to play. 

* A game can result in a stalemate if both players have no more valid moves left to play. This happens when all the pawns of both players are blocked from moving.

## AI Details

The AI was implemented using the minimax algorithm with alpha-beta pruning. The alpha-beta pruning in particular is incredibly useful to allow the AI to search deep within the game tree efficiently. The AI searchs up to 7 moves ahead in the game and uses an evaluation function which employs a wide variety of heuristics to output a current score that the algorithm then uses to find the best possible move.





