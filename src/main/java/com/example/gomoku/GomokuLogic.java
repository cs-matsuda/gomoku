package com.example.gomoku;

import java.io.Serializable;

public class GomokuLogic implements Serializable {
 private static final long serialVersionUID = 1L;
 private static final int BOARD_SIZE = 9;
 private static final int WIN_CONDITION = 5;
 private char[][] board;
 private char currentPlayer;
 private boolean gameOver;
 private String winner; // 勝利プレイヤー ('X', 'O', 'Draw')

 public GomokuLogic() {
     initializeGame();
 }

 public void initializeGame() {
     board = new char[BOARD_SIZE][BOARD_SIZE];
     for (int i = 0; i < BOARD_SIZE; i++) {
         for (int j = 0; j < BOARD_SIZE; j++) {
             board[i][j] = '-';
         }
     }
     currentPlayer = 'X';
     gameOver = false;
     winner = null;
 }

 public char[][] getBoard() {
     return board;
 }

 public char getCurrentPlayer() {
     return currentPlayer;
 }

 public boolean isGameOver() {
     return gameOver;
 }

 public String getWinner() {
     return winner;
 }

 public boolean isValidMove(int row, int col) {
     return row >= 0 && row < BOARD_SIZE &&
            col >= 0 && col < BOARD_SIZE &&
            board[row][col] == '-';
 }

 public void makeMove(int row, int col) {
     if (!gameOver && isValidMove(row, col)) {
         board[row][col] = currentPlayer;
         if (checkWin()) {
             gameOver = true;
             winner = String.valueOf(currentPlayer);
         } else if (checkDraw()) {
             gameOver = true;
             winner = "Draw";
         } else {
             switchPlayer();
         }
     }
 }

 private void switchPlayer() {
     currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
 }

 private boolean checkWin() {
     return checkDirection(0, 1) || // 水平
            checkDirection(1, 0) || // 垂直
            checkDirection(1, 1) || // 右下がり斜め
            checkDirection(1, -1); // 右上がり斜め
 }

 private boolean checkDirection(int rowDelta, int colDelta) {
     for (int r = 0; r < BOARD_SIZE; r++) {
         for (int c = 0; c < BOARD_SIZE; c++) {
             if (board[r][c] == currentPlayer) {
                 int count = 1;
                 for (int k = 1; k < WIN_CONDITION; k++) {
                     int newR = r + k * rowDelta;
                     int newC = c + k * colDelta;

                     if (newR >= 0 && newR < BOARD_SIZE &&
                         newC >= 0 && newC < BOARD_SIZE &&
                         board[newR][newC] == currentPlayer) {
                         count++;
                     } else {
                         break;
                     }
                 }
                 if (count == WIN_CONDITION) {
                     return true;
                 }
             }
         }
     }
     return false;
 }

 private boolean checkDraw() {
     for (int i = 0; i < BOARD_SIZE; i++) {
         for (int j = 0; j < BOARD_SIZE; j++) {
             if (board[i][j] == '-') {
                 return false;
             }
         }
     }
     return true;
 }

 // 必要に応じてBOARD_SIZEなどを取得するgetterを追加
 public static int getBoardSize() {
     return BOARD_SIZE;
 }
}
