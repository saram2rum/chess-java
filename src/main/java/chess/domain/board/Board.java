package chess.domain.board;

import chess.domain.piece.*;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Position, Piece> pieces = new HashMap<>();

    private Position whiteKingPosition;
    private Position blackKingPosition;

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
        Position pos = new Position(4, 7);
        pieces.put(pos, new King(Color.BLACK));
        blackKingPosition = pos;
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
        Position pos = new Position(4, 0);
        pieces.put(pos, new King(Color.WHITE));
        whiteKingPosition = pos;
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

        if (!sourcePiece.isMovable(source, target, targetPiece)) {
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

        if (sourcePiece.is(Type.KING, Color.WHITE)) {
            whiteKingPosition = target;
        }

        if (sourcePiece.is(Type.KING, Color.BLACK)) {
            blackKingPosition = target;
        }
    }

    public boolean isChecked(Color kingColor) {
        Position kingPosition = kingColor.isWhite() ? whiteKingPosition : blackKingPosition;
        Piece king = pieces.get(kingPosition);

        for (Position source : pieces.keySet()) {
            Piece attacker = pieces.get(source);

            // 1. 아군은 패스
            if (attacker.isSameColor(king)) continue;

            // 2. 기본 이동 규칙 검사 (방향, 거리 등)
            if (!attacker.isMovable(source, kingPosition, king)) continue;

            // 3. 🚨 [추가] 슬라이딩 기물(룩, 비숍, 퀸)은 장애물 검사 필수!
            // 나이트는 점프하니까 검사 안 함. 폰은 바로 앞이니 검사 안 함(혹은 1칸이라 루프 안 돎).
            if (attacker.isSliding()) {
                if (isPathBlocked(source, kingPosition)) {
                    continue; // 벽에 막혔으니 체크 아님 -> 다음 놈 검사
                }
            }

            // 여기까지 통과하면 진짜 체크!
            return true;
        }
        return false;
    }

    // 장애물이 있으면 true, 뻥 뚫려 있으면 false
    private boolean isPathBlocked(Position source, Position target) {
        Direction direction = Direction.of(source, target);
        Position current = source;

        while (true) {
            int nextX = current.getX() + direction.getXDegree();
            int nextY = current.getY() + direction.getYDegree();

            // 🚨 [필수] 체스판 밖으로 나가면 즉시 종료! (무한 루프 방지)
            if (nextX < 0 || nextX > 7 || nextY < 0 || nextY > 7) {
                return false;
            }

            current = new Position(nextX, nextY);

            // 1. 목적지(왕)에 도착했으면 "장애물 없음" (통과)
            if (current.equals(target)) {
                return false;
            }

            // 2. 가는 길에 다른 기물이 있으면 "장애물 있음" (차단)
            if (pieces.containsKey(current)) {
                return true;
            }
        }
    }


}