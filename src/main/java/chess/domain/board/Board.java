package chess.domain.board;

import chess.domain.piece.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    // ... 기존 코드 아래에 추가 ...

    public void move(Position source, Position target, Color currentTurn, Type promotionType) {

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

        if (sourcePiece.isSliding() && isPathBlocked(source, target)) {
            throw new IllegalArgumentException("경로가 막혀있습니다!");
        }

        // 3. 🚨 [추가] "거기로 가면 우리 왕이 위험해지나요?" (자살 금지)
        if (!isMoveSafe(source, target)) {
            throw new IllegalArgumentException("왕이 체크 상태에 빠지게 되는 수는 둘 수 없습니다! 🛡️");
        }

        pieces.put(target, sourcePiece);
        pieces.remove(source);

        if (sourcePiece.is(Type.KING, Color.WHITE)) {
            whiteKingPosition = target;
        }

        if (sourcePiece.is(Type.KING, Color.BLACK)) {
            blackKingPosition = target;
        }

        // 👑 [프로모션 로직 추가]
        // "지금 도착한 기물이 폰이고 + 끝까지 갔다면?"
        if (sourcePiece.is(Type.PAWN)) {
            if (canPromote(target, sourcePiece.getColor())) {
                // 변신할 타입이 없으면? -> 룰상 에러지만, 일단 퀸으로 자동 변신 or 에러 처리
                if (promotionType == null) {
                    // 제대로 하려면 여기서 에러를 내야 맞습니다.
                    // throw new IllegalArgumentException("프로모션할 기물을 선택해야 합니다!");

                    // 하지만 편의상 일단 퀸으로 해둡시다.
                    promotionType = Type.QUEEN;
                }

                // 기물 교체 (변신!)
                Piece promotedPiece = createPromotedPiece(promotionType, sourcePiece.getColor());
                pieces.put(target, promotedPiece);
            }
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

    // "기물 규칙상 갈 수 있고 && 장애물도 없는지" 확인하는 통합 메서드
// (public으로 열어서 ChessGame에서도 쓰면 좋습니다)
    public boolean isValidMove(Position source, Position target) {
        Piece piece = pieces.get(source);
        if (piece == null) return false; // 기물이 없으면 이동 불가
        if (source.equals(target)) return false;

        Piece targetPiece = pieces.get(target);

        // 1. 아군 팀킬 방지
        if (targetPiece != null && piece.isSameColor(targetPiece)) {
            return false;
        }

        // 2. 기물 자체의 이동 규칙 검사 (방향, 거리 등)
        if (!piece.isMovable(source, target, targetPiece)) {
            return false;
        }

        // 3. 장애물 검사 (슬라이딩 기물 OR "폰이 2칸 이동할 때") 🚨 수정됨!
        // (폰인지 확인하기 위해 instanceof 사용)
        boolean isPawnTwoStep = (piece instanceof Pawn) && Math.abs(source.getY() - target.getY()) == 2;

        if ((piece.isSliding() || isPawnTwoStep) && isPathBlocked(source, target)) {
            return false; // 중간에 누구 있으면 이동 불가
        }

        return true;
    }

    // "거기로 움직이면 우리 왕이 안전한가?" (가상 이동 시뮬레이션)
    // source -> target으로 이동했을 때, 내 왕이 체크 상태가 아니면 true
    private boolean isMoveSafe(Position source, Position target) {
        Piece piece = pieces.get(source);
        Piece capturedPiece = pieces.get(target);
        Color myColor = piece.getColor();

        // 1. 기물 이동 (가상)
        pieces.put(target, piece);
        pieces.remove(source);

        // 왕 위치 업데이트 (필요시)
        Position originalKingPos = null;
        if (piece.isKing()) {
            if (myColor.isWhite()) {
                originalKingPos = whiteKingPosition;
                whiteKingPosition = target;
            } else {
                originalKingPos = blackKingPosition;
                blackKingPosition = target;
            }
        }

        // 2. 안전한지 확인
        boolean isSafe = !isChecked(myColor); // 내가 자살수를 둔 게 아닌지 확인

        // 3. 원상복구 (Rollback)
        pieces.put(source, piece);
        if (capturedPiece != null) {
            pieces.put(target, capturedPiece);
        } else {
            pieces.remove(target);
        }

        if (originalKingPos != null) {
            if (myColor.isWhite()) whiteKingPosition = originalKingPos;
            else blackKingPosition = originalKingPos;
        }

        return isSafe;
    }

    // 프론트엔드용: "이 기물, 어디 어디 갈 수 있어?"
    public List<Position> calculateMovablePositions(Position source) {
        List<Position> movablePositions = new ArrayList<>();
        Piece piece = pieces.get(source);

        if (piece == null) return movablePositions; // 빈칸 클릭하면 빈 리스트

        // 체스판 전체를 훑으면서 갈 수 있는지 확인
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Position target = new Position(x, y);
                if (source.equals(target)) continue;

                // 1. 규칙상 갈 수 있고 (isValidMove)
                // 2. 가서 체크당하지 않는다면 (isMoveSafe) -> OK!
                if (isValidMove(source, target) && isMoveSafe(source, target)) {
                    movablePositions.add(target);
                }
            }
        }

        return movablePositions;
    }

    // "우리 팀 기물 중에 어디로든 움직여서 살 수 있는 수가 하나라도 있니?"
    public boolean hasAnySafeMove(Color color) {

        // 🚨 [수정 포인트] 에러 방지를 위해 keySet을 새로운 List로 복사해서 사용!
        List<Position> piecePositions = new ArrayList<>(pieces.keySet());

        for (Position source : piecePositions) {
            // 주의: 복사본에는 있는데, 그 사이에 잡혀서 사라진 기물일 수도 있으니 null 체크 필수
            Piece piece = pieces.get(source);
            if (piece == null || piece.getColor() != color) continue;

            // 방금 만든 메서드 활용!
            // "이 기물이 갈 수 있는 곳이 하나라도 있으면 생존"
            if (!calculateMovablePositions(source).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 1. 체크메이트: 체크 상태인데 && 살길이 없음
    public boolean isCheckMate(Color color) {
        return isChecked(color) && !hasAnySafeMove(color);
    }

    // 2. 스테일메이트: 체크 아닌데 && 살길이 없음 (무승부)
    public boolean isStaleMate(Color color) {
        return !isChecked(color) && !hasAnySafeMove(color);
    }

    // 프로모션 자격 확인 (맨 끝 줄인가?)
    private boolean canPromote(Position target, Color color) {
        int y = target.getY();
        // 백색은 y=7(8랭크), 흑색은 y=0(1랭크) 도달 시
        if (color.isWhite()) return y == 7;
        else return y == 0;
    }

    // 기물 생성 공장 (Factory 패턴의 간단 버전)
    private Piece createPromotedPiece(Type type, Color color) {
        switch (type) {
            case QUEEN: return new Queen(color);
            case ROOK: return new Rook(color);
            case BISHOP: return new Bishop(color);
            case KNIGHT: return new Knight(color);
            default: throw new IllegalArgumentException("폰은 킹이나 폰으로 변신할 수 없습니다.");
        }
    }
}