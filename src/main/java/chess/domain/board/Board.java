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

    // 안전한 findPiece 메서드
    private Piece findPiece(Position position) {
        Piece piece = pieces.get(position);

        // 검문: "야, 여기 아무것도 없는데?"
        if (piece == null) {
            throw new IllegalArgumentException("해당 위치에는 기물이 없습니다.");
        }

        // 여기까지 왔다면, 무조건 '살아있는 기물'임이 보장됨
        return piece;
    }

    private void validatePathIsEmpty(Position source, Position target) {
        // 1. 방향 구하기 (a1 -> a5면 NORTH)
        Direction direction = Direction.of(source, target);

        int currentX = source.getX();
        int currentY = source.getY();

        // 2. 목적지에 닿을 때까지 반복 (출발지 바로 다음 칸부터 검사)
        while (true) {
            currentX += direction.getXDegree();
            currentY += direction.getYDegree();

            Position current = new Position(currentX, currentY);

            // 도착지에 왔으면 멈춤 (도착지에 적이 있는 건 잡으면 되니까 OK)
            if (current.equals(target)) {
                break;
            }

            // 3. 가는 길목에 누가 있다? -> 에러!! 쾅!!
            if (pieces.containsKey(current)) {
                throw new IllegalArgumentException("이동 경로가 막혀있습니다! 🚧");
            }
        }
    }

    // ... 기존 코드 아래에 추가 ...

    public void move(Position source, Position target, Color currentTurn) {

        Piece sourcePiece = findPiece(source);
        Piece targetPiece = pieces.get(target);

        if (sourcePiece.getColor() != currentTurn) {
            throw new IllegalArgumentException("상대방의 기물은 건드릴 수 없습니다!");
        }

        if (!sourcePiece.isMovable(source, target)) {
            throw new IllegalArgumentException("그 기물은 거기로 갈 수 없습니다! 규칙 위반 삐-! 🚨");
        }

        if (targetPiece != null && targetPiece.getColor() == currentTurn) {
            throw new IllegalArgumentException("같은 팀 기물이 있는 곳으로는 이동할 수 없습니다! 🚫");
        }

        if (sourcePiece.isSliding()) {
            validatePathIsEmpty(source, target);
        }

        pieces.put(target, sourcePiece);
        pieces.remove(source);
    }
}