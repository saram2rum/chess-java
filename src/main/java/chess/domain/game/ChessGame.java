package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Color;
import chess.domain.piece.Type;

public class ChessGame {
    private final Board board;
    private Color currentTurn; // ⏱️ 핵심: 현재 턴을 기억하는 변수

    // 🚩 게임 진행 중인지 확인하는 플래그
    private boolean isRunning = true;

    // 게임 종료 선언
    public void endGame() {
        this.isRunning = false;
    }

    // 외부에서 게임 끝났는지 확인할 때 사용
    public boolean isRunning() {
        return isRunning;
    }

    public ChessGame() {
        this.board = new Board();
        this.board.initialize(); // 게임 시작 시 기물 배치
        this.currentTurn = Color.WHITE; // 체스는 항상 흰색 먼저 시작
    }

    public void move(String sourceStr, String targetStr, Type promotionType) {
        if (!isRunning) {
            System.out.println("게임이 이미 종료되었습니다. 🚫");
            return;
        }

        try {
            // 🚨 여기서 통역사를 사용합니다!
            Position source = new Position(sourceStr);
            Position target = new Position(targetStr);

            // 3. Board에게 진짜 좌표로 이동 명령
            board.move(source, target, currentTurn, promotionType);

            // --- 성공했을 때만 아래 로직 실행 ---
            System.out.print("이동 성공: " + sourceStr + " -> " + targetStr);

            // 4. 승패/체크 판정 (아까 만든 로직)
            checkGameStatus();

            // 5. 턴 넘기기
            nextTurn();

        } catch (IllegalArgumentException e) {
            // Board나 toPosition에서 에러 던지면 여기서 잡아서 출력
            System.out.println("❌ 이동 실패: " + e.getMessage());
        }
    }

    private void checkGameStatus() {
        Color opponent = currentTurn.opponent();

        if (board.isChecked(opponent)) {
            if (board.isCheckMate(opponent)) {
                System.out.println("🎉 CHECKMATE! " + currentTurn + " 승리! 🎉");
                endGame();
            } else {
                System.out.println("🔥 CHECK! " + opponent + " 왕이 위험합니다! 🔥");
            }
        } else {
            if (board.isStaleMate(opponent)) {
                System.out.println("🤝 STALEMATE! 무승부입니다. 🤝");
                endGame();
            }
        }
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