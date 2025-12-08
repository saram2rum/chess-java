package chess.domain.board;

import chess.domain.piece.*;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Position, Piece> pieces = new HashMap<>();

    public void initialize() {
        pieces.clear(); // 맵을 싹 비우고 시작
        addBlackPieces();
        addWhitePieces();
    }

    private void addBlackPieces() {
        // 검은색 폰 (y=6, 위에서 두 번째 줄)
        for (int i = 0; i < 8; i++) {
            pieces.put(new Position(i, 6), new Pawn(Color.BLACK));
        }

        // 검은색 주요 기물 (y=7, 맨 윗줄)
        pieces.put(new Position(0, 7), new Rook(Color.BLACK));
        pieces.put(new Position(1, 7), new Knight(Color.BLACK));
        pieces.put(new Position(2, 7), new Bishop(Color.BLACK));
        pieces.put(new Position(3, 7), new Queen(Color.BLACK));
        pieces.put(new Position(4, 7), new King(Color.BLACK));
        pieces.put(new Position(5, 7), new Bishop(Color.BLACK));
        pieces.put(new Position(6, 7), new Knight(Color.BLACK));
        pieces.put(new Position(7, 7), new Rook(Color.BLACK));
    }

    private void addWhitePieces() {
        // 흰색 폰 (y=1, 아래서 두 번째 줄)
        for (int i = 0; i < 8; i++) {
            pieces.put(new Position(i, 1), new Pawn(Color.WHITE));
        }

        // 흰색 주요 기물 (y=0, 맨 아랫줄)
        pieces.put(new Position(0, 0), new Rook(Color.WHITE));
        pieces.put(new Position(1, 0), new Knight(Color.WHITE));
        pieces.put(new Position(2, 0), new Bishop(Color.WHITE));
        pieces.put(new Position(3, 0), new Queen(Color.WHITE));
        pieces.put(new Position(4, 0), new King(Color.WHITE));
        pieces.put(new Position(5, 0), new Bishop(Color.WHITE));
        pieces.put(new Position(6, 0), new Knight(Color.WHITE));
        pieces.put(new Position(7, 0), new Rook(Color.WHITE));
    }

    // 테스트용: 기물이 제대로 들어갔는지 확인하는 메서드
    public Piece findPiece(String position) {
        return pieces.get(new Position(position));
    }

    // ... 기존 코드 아래에 추가 ...

    public void move(String sourceValue, String targetValue) {
        Position source = new Position(sourceValue);
        Position target = new Position(targetValue);

        // 1. 출발지에 기물이 있는지 확인 (없으면 에러!)
        Piece piece = pieces.get(source);
        if (piece == null) {
            throw new IllegalArgumentException("출발지에 기물이 없습니다! 귀신을 옮길 순 없어요 👻");
        }

        // --- [NEW] 전략 패턴 적용: 기물별 이동 규칙 검사 ---
        if (!piece.isMovable(source, target)) {
            throw new IllegalArgumentException("그 기물은 거기로 갈 수 없습니다! 규칙 위반 삐-! 🚨");
        }

        // 2. 내 기물인지 확인 (상대방 말을 움직이면 안 되니까)
        // (이 부분은 나중에 '현재 누구 턴인지' 관리할 때 추가합시다. 일단 패스!)

        // 3. 같은 팀이 있는 자리로 이동 불가 (팀킬 방지)
        Piece targetPiece = pieces.get(target);
        if (targetPiece != null && targetPiece.getColor() == piece.getColor()) {
            throw new IllegalArgumentException("같은 팀 기물이 있는 곳으로는 이동할 수 없습니다! 🚫");
        }

        // 4. 실제 이동 (Map 갱신)
        pieces.put(target, piece); // 도착지에 기물 놓기 (만약 적이 있으면 덮어씌워짐 -> 잡은 것!)
        pieces.remove(source);     // 출발지 비우기
    }
}