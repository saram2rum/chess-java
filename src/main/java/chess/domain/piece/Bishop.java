package chess.domain.piece;

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
    public boolean isMovable(Position source, Position target) {
        return true; // 일단 임시로 허용!
    }
}
