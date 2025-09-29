package com.example.gomoku;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/gomoku") 
public class GomokuServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;

 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     HttpSession session = request.getSession();
     GomokuLogic game = (GomokuLogic) session.getAttribute("gomokuGame");

     // ゲームがセッションに存在しない場合、またはリセット要求があった場合、新しいゲームを開始
     if (game == null || request.getParameter("reset") != null) {
         game = new GomokuLogic();
         session.setAttribute("gomokuGame", game);
     }

     request.setAttribute("game", game); // JSPにゲームの状態を渡す
     request.getRequestDispatcher("/index.jsp").forward(request, response);
 }

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     HttpSession session = request.getSession();
     GomokuLogic game = (GomokuLogic) session.getAttribute("gomokuGame");

     if (game == null) { // ここでnullチェックしている
         // セッション切れなどでゲームがない場合は、新しく開始させる
         response.sendRedirect(request.getContextPath() + "/gomoku?reset=true");
         return;
     }

     if (!game.isGameOver()) { // ゲームが終了していない場合のみ処理
         try {
             int row = Integer.parseInt(request.getParameter("row"));
             int col = Integer.parseInt(request.getParameter("col"));

             if (game.isValidMove(row, col)) {
                 game.makeMove(row, col);
             } else {
                 request.setAttribute("errorMessage", "そのマスには置けません。");
             }
         } catch (NumberFormatException e) {
             request.setAttribute("errorMessage", "無効な入力です。");
         }
     } else {
         request.setAttribute("errorMessage", "ゲームは終了しています。リセットしてください。");
     }

     request.setAttribute("game", game); // JSPにゲームの状態を渡す
     request.getRequestDispatcher("/index.jsp").forward(request, response);
 }
}