package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;

public class Bishop extends Piece {

    public Bishop(final Color color) {
        super(color, Type.BISHOP);
    }

    @Override
    public boolean isSliding() {
        return true;
    }

    @Override
    public boolean isMovable(Position source, Position target, Piece targetPiece) {
        Direction direction = Direction.of(source, target);

        // 대각선 방향(북동, 북서, 남동, 남서) 중 하나인지 확인
        return direction.isDiagonal();
    }
}
