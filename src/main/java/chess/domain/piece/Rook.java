package chess.domain.piece;

import chess.domain.board.Position;

public class Rook extends Piece {

    public Rook(final Color color) { super(color, Type.ROOK); }

    @Override
    public boolean isSliding() {
        return true;
    }

    @Override
    public boolean isMovable(Position source, Position target) {

        // 같은 자리로 이동하며 안 됨
        if (source.equals(target)) {
            return false;
        }
        // 직진이어야함
        return source.getX() == target.getX() || source.getY() == target.getY();

    }

}
