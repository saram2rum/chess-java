package chess;

import chess.domain.game.ChessGame;
import chess.domain.board.Position;

public class Application {

    public static void main(String[] args) {
        // 1. 게임 시작 (여기서 Board 생성, 기물 초기화, 턴 설정 다 됨)
        ChessGame game = new ChessGame();

        System.out.println("=== ♟️ 폰 로직 테스트 시작 ===");

        // [상황 1] 백색 폰(a2) 직진 테스트
        move(game, "a2", "a3"); // 성공해야 함 (빈칸 직진)
        move(game, "a3", "a4"); // 성공해야 함 (빈칸 직진)

        // [상황 2] 흑색 폰(a7) 직진 테스트 (턴이 바뀌었으므로 흑 차례여야 함)
        // 위에서 백이 2번 뒀으므로 턴이 꼬였을 수 있음.
        // 테스트 편의를 위해 번갈아 두겠습니다.

        System.out.println("\n--- 게임 리셋 후 시뮬레이션 ---");
        game = new ChessGame(); // 게임 초기화

        // 1. 백: a2 -> a3 (이동)
        move(game, "a2", "a3");

        // 2. 흑: b7 -> b6 (이동) -> 이제 b6에 흑 폰이 있음
        move(game, "b7", "b6");

        // 3. 백: a3 -> a4 (이동)
        move(game, "a3", "a4");

        // 4. 흑: b6 -> b5 (이동) -> 이제 a4(백) 옆에 b5(흑)이 옴
        move(game, "b6", "b5");

        // 5. 🚨 백의 공격 테스트: a4 -> b5 (대각선 공격!)
        // b5에 흑 폰이 있으므로 성공해야 함!
        System.out.println("\n[Test] 대각선 공격 시도 (a4 -> b5)");
        move(game, "a4", "b5");

        // 6. 🚨 백의 잘못된 공격 테스트: a2 -> b3 (빈 땅 공격)
        // b3는 비어있으므로 실패해야 함!
        System.out.println("\n[Test] 빈 땅 대각선 이동 시도 (실패해야 함)");
        // 테스트를 위해 새 게임
        ChessGame failTestGame = new ChessGame();
        move(failTestGame, "a2", "b3");
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