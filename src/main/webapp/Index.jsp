<%-- src/main/webapp/index.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.gomoku.GomokuLogic" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>五目並べ</title>
    <style>
        body { font-family: sans-serif; text-align: center; }
        .board-container { display: inline-block; border: 1px solid black; }
        .board-row { display: flex; }
        .cell {
            width: 40px; height: 40px;
            border: 1px solid #ccc;
            display: flex; justify-content: center; align-items: center;
            font-size: 1.5em; font-weight: bold;
        }
        .cell.x { color: blue; }
        .cell.o { color: red; }
        .message { margin-top: 10px; font-weight: bold; }
        .error { color: red; }
        form { margin-top: 20px; }
    </style>
</head>
<body>
    <h1>五目並べ</h1>

    <%
        GomokuLogic game = (GomokuLogic) request.getAttribute("game");
        if (game == null) {
            // セッションからゲームオブジェクトを取得できなかった場合のフォールバック（通常はサーブレットで処理されるはず）
            response.sendRedirect(request.getContextPath() + "/gomoku?reset=true");
            return;
        }
    %>

    <div class="message">
        <% if (game.isGameOver()) { %>
            <% if ("Draw".equals(game.getWinner())) { %>
                ゲーム終了: 引き分けです！
            <% } else { %>
                ゲーム終了: プレイヤー <%= game.getWinner() %> の勝利です！
            <% } %>
        <% } else { %>
            プレイヤー <%= game.getCurrentPlayer() %> の番です。
        <% } %>
    </div>

    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="message error"><%= errorMessage %></div>
    <% } %>

    <%-- index.jsp の該当部分 --%>
	<div class="board-container">
	    <% for (int r = 0; r < GomokuLogic.getBoardSize(); r++) { %>
	        <div class="board-row">
	            <% for (int c = 0; c < GomokuLogic.getBoardSize(); c++) { %>
	                <div class="cell">
	                    <% if (game.getBoard()[r][c] == 'X') { %>
	                        <span class="x">X</span>
	                    <% } else if (game.getBoard()[r][c] == 'O') { %>
	                        <span class="o">O</span>
	                    <% } else { %>
	                        <form action="<%= request.getContextPath() %>/gomoku" method="post" style="margin:0;">
	                            <input type="hidden" name="row" value="<%= r %>">
	                            <input type="hidden" name="col" value="<%= c %>">
	                            <% if (!game.isGameOver()) { %>
	                                <button type="submit" style="width: 100%; height: 100%; background: none; border: none; cursor: pointer;"></button>
	                            <% } else { %>
	                                <!-- ゲーム終了後はボタンを無効化 -->
	                                <button type="button" disabled style="width: 100%; height: 100%; background: none; border: none;"></button>
	                            <% } %>
	                        </form>
	                    <% } %>
	                </div>
	            <% } %>
	        </div>
	    <% } %>
	</div>

    <form action="<%= request.getContextPath() %>/gomoku" method="get" style="margin-top: 20px;">
        <input type="hidden" name="reset" value="true">
        <button type="submit">ゲームをリセット</button>
    </form>

</body>
</html>