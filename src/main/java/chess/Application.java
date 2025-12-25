package chess;

import chess.domain.game.ChessGame;
import chess.domain.board.Position;

public class Application {

    public static void main(String[] args) {

        ChessGame game = new ChessGame();
        System.out.println("=== ♟️ 폰 2칸 이동 규칙 테스트 (정밀) ♟️ ===");

        // 1. [White] 초기 위치 2칸 전진 (성공해야 함)
        move(game, "a2", "a4");

        // 2. [Black] 흑색도 아무거나 하나 둬서 턴을 넘겨줌 (h7 -> h6)
        // (그래야 다시 백색 차례가 옴)
        move(game, "h7", "h6");

        // 3. [White] 한 번 움직였던 폰이 또 2칸 가려고 함 (a4 -> a6) -> 여기서 실패해야 함!
        // 기대 메시지: "그 기물은 거기로 갈 수 없습니다!"
        move(game, "a4", "a6");

        // 4. [White] (실패했으니 여전히 백 턴) 이번엔 1칸만 가봄 (a4 -> a5) -> 성공해야 함
        move(game, "a4", "a5");

        // 5. [Black] b7 -> b5 (흑 폰 2칸 테스트)
        move(game, "b7", "b5");
        System.out.println("--- 흑 폰 테스트 완료 ---");

    }

    // 🛠️ 도우미 메서드: 이제 Board가 아니라 Game을 받습니다!
    public static void move(ChessGame game, String source, String target) {
        try {
            System.out.print("이동 시도: " + source + " -> " + target + " ... ");

            // game.move 안에서 1.턴 검사 -> 2.기물 검사 -> 3.이동 -> 4.턴 넘기기 다 함
            game.move(source, target);

            System.out.println("✅ 성공!");
            // (선택) 눈으로 보고 싶다면: OutputView.printBoard(game.getBoard());
        } catch (Exception e) {
            System.out.println("❌ 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}