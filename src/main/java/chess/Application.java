package chess;

import chess.domain.game.ChessGame;
import chess.domain.board.Position;

public class Application {

    public static void main(String[] args) {
        // 1. 게임 시작 (여기서 Board 생성, 기물 초기화, 턴 설정 다 됨)
        ChessGame game = new ChessGame();

        System.out.println("=== 체크 로직 테스트 시작 ===");

        // 불가능한 움직임
        move(game, "f2", "f5");
        // 상대 기물
        move(game, "f7", "f6");
        // 제자리 걸음
        move(game, "f2", "f2");

        move(game, "f2", "f3");
        move(game, "e7", "e6");
        move(game, "a2", "a3");
        move(game, "d8", "h4");

    }

    // 🛠️ 도우미 메서드: 이제 Board가 아니라 Game을 받습니다!
    public static void move(ChessGame game, String source, String target) {
        try {
            System.out.print("이동 시도: " + source + " -> " + target + " ... ");

            // game.move 안에서 1.턴 검사 -> 2.기물 검사 -> 3.이동 -> 4.턴 넘기기 다 함
            game.move(new Position(source), new Position(target));

            System.out.println("✅ 성공!");
            // (선택) 눈으로 보고 싶다면: OutputView.printBoard(game.getBoard());
        } catch (Exception e) {
            System.out.println("❌ 실패: " + e.getMessage());
        }
    }
}