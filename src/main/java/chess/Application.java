package chess;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Color;

public class Application {

    // main에서 쓰려면 board도 static이거나 main 안에 있어야 해요.
    static Board board = new Board();

    public static void main(String[] args) {
        // [1] 보드 초기화 (기물 배치)
        board.initialize();

        // [2] 테스트 시작: 이제 객체 생성 없이 편하게 String으로 명령하세요!
        System.out.println("=== 테스트 시작 ===");

        move("a2", "a3", Color.WHITE); // 폰 이동
        move("a3", "a4", Color.WHITE); // 한 칸 더

        // 일부러 틀린 것도 넣어보세요
        move("a1", "a5", Color.WHITE); // 룩이 점프? (에러 나야 함)
    }

    // 💡 [도우미 메서드] 문자열을 받아서 객체로 변환 후 Board에게 전달
    public static void move(String source, String target, Color color) {
        try {
            System.out.println("이동 시도: " + source + " -> " + target);

            // 여기서 변환 작업(노가다)을 대신 처리합니다.
            board.move(new Position(source), new Position(target), color);

            System.out.println("✅ 이동 성공!");
        } catch (Exception e) {
            // 에러가 나면 프로그램이 죽지 않고 메시지만 출력하게 예외 처리
            System.out.println("❌ 이동 실패: " + e.getMessage());
        }
        System.out.println("----------------------");
    }
}