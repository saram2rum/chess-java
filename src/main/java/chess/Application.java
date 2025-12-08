package chess;

import chess.domain.board.Board;
import chess.domain.piece.Piece;

public class Application {
    public static void main(String[] args) {
        Board board = new Board();
        board.initialize();

// 테스트 1: 정상 이동 (성공해야 함)
        System.out.println("🚀 1. 폰 정상 이동 (a2 -> a3)");
        board.move("a2", "a3");
        System.out.println("✅ 성공!");

// 테스트 2: 뒤로 가기 (실패해야 함)
        try {
            System.out.println("\n🚀 2. 폰 뒤로 가기 (a3 -> a2)");
            board.move("a3", "a2"); // 폰은 후진 불가!
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 실패: " + e.getMessage()); // "그 기물은 거기로 갈 수 없습니다..." 출력 예상
        }

// 테스트 3: 옆으로 가기 (실패해야 함)
        try {
            System.out.println("\n🚀 3. 폰 옆으로 가기 (a3 -> b3)");
            board.move("a3", "b3");
        } catch (Exception e) {
            System.out.println("❌ 실패: " + e.getMessage());
        }
    }
}