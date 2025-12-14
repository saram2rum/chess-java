package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;

public class King extends Piece {

    public King(Color color) {
        super(color, Type.KING);
    }

    @Override
    public boolean isKing() {
        return true; // 게임 종료 조건 체크용
    }

    @Override
    public boolean isMovable(Position source, Position target, Piece targetPiece) {
        int xDiff = Math.abs(source.xDiff(target));
        int yDiff = Math.abs(source.yDiff(target));

        // 8방향으로 1칸만 이동 가능 (0칸 이동은 Board에서 막아주니 제외)
        // x차이 + y차이가 0이면 안 되고, x, y 둘 다 1 이하여야 함.
        return (xDiff <= 1 && yDiff <= 1) && (xDiff + yDiff > 0);
    }
}