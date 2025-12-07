package chess;

import chess.domain.board.Board;
import chess.domain.piece.Piece;

public class Application {
    public static void main(String[] args) {
        Board board = new Board();
        board.initialize();

        System.out.println("--- 이동 전 ---");
        System.out.println("a2 위치: " + board.findPiece("a2")); // 흰색 폰

        // 🚀 이동 명령! (흰색 폰을 a2 -> a3로)
        System.out.println("\n🚀 a2 폰을 a3로 이동!");
        board.move("a2", "a3");

        System.out.println("\n--- 이동 후 ---");
        System.out.println("a2 위치: " + board.findPiece("a2")); // null이어야 함
        System.out.println("a3 위치: " + board.findPiece("a3")); // 흰색 폰이어야 함
    }
}