package chess;

import chess.domain.game.ChessGame;
import chess.domain.board.Position;
import chess.domain.piece.Type;

public class Application {

    public static void main(String[] args) {

        ChessGame game = new ChessGame();
        System.out.println("=== 👑 프로모션(승급) 테스트 👑 ===");

        // [전략]
        // 1. 백(b2)이 b4로 나감
        // 2. 흑(a7)이 a5로 나옴
        // 3. 백(b4)이 a5를 잡으면서(Capture) a열로 차선 변경!
        //    -> 이러면 a6, a7, a8이 텅텅 빔! (고속도로 개통 🛣️)

        move(game, "b2", "b4"); // 1. 백 전진
        move(game, "a7", "a5"); // 2. 흑 전진 (먹잇감 등장)

        // 3. 흑 폰을 잡으면서 a5로 이동!
        move(game, "b4", "a5");

        move(game, "h7", "h6"); // 흑은 구석에서 턴 낭비 중...

        move(game, "a5", "a6"); // 4. 백 전진 (앞이 뻥 뚫림)
        move(game, "h6", "h5");

        move(game, "a6", "a7"); // 5. 백 도착 직전! (a7)
        move(game, "h5", "h4");

        System.out.println(">>> 운명의 순간! 폰이 끝(b8)에 도착합니다! (나이트로 변신 시도)");

        // 👑 a7 -> a8 로 가면서 KNIGHT로 승급!
        movePromote(game, "a7", "b8", Type.KNIGHT);

    }

    // 🛠️ 도우미 메서드: 이제 Board가 아니라 Game을 받습니다!
    // 1. 일반 이동 (기존 코드 호환용)
    public static void move(ChessGame game, String source, String target) {
        move(game, source, target, null); // 아래 녀석에게 null을 넘겨서 처리
    }

    // 2. 프로모션 이동 (진짜 일하는 녀석)
    public static void move(ChessGame game, String source, String target, Type promotionType) {
        try {
            // 로그 메시지도 프로모션이면 좀 다르게 출력
            String message = "이동 시도: " + source + " -> " + target;
            if (promotionType != null) {
                message += " (승급: " + promotionType + ")";
            }
            System.out.print(message + " ... ");

            // game.move에게 type까지 전달!
            game.move(source, target, promotionType);

            System.out.println(" ✅ 성공!");
        } catch (Exception e) {
            System.out.println("❌ 실패: " + e.getMessage());
            // e.printStackTrace(); // 에러 위치 찾을 때 주석 해제
        }
    }

    // Application 내의 도우미 메서드 추가
    public static void movePromote(ChessGame game, String source, String target, Type type) {
        try {
            System.out.print("승급 이동 시도: " + source + " -> " + target + " (" + type + ") ... ");
            game.move(source, target, type); // 3개짜리 호출
            // 성공 메시지는 ChessGame에서 출력
        } catch (Exception e) {
            System.out.println("❌ 실패: " + e.getMessage());
        }
    }
}