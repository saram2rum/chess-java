package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Color;

public class ChessGame {
    private final Board board;
    private Color currentTurn; // ⏱️ 핵심: 현재 턴을 기억하는 변수

    public ChessGame() {
        this.board = new Board();
        this.board.initialize(); // 게임 시작 시 기물 배치
        this.currentTurn = Color.WHITE; // 체스는 항상 흰색 먼저 시작
    }

    public void move(Position source, Position target) {
        // 1. Board에게 이동 명령 내리기 (현재 턴 정보를 같이 줍니다!)
        //    -> 만약 내 턴이 아닌 말을 건드리면 Board가 에러를 뱉겠죠?
        board.move(source, target, currentTurn);

        // 2. 이동이 성공적으로 끝났다면? 턴을 넘깁니다.
        nextTurn();
    }

    // 턴을 스위칭하는 간단한 로직
    private void nextTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }
}