package chess.domain.piece;

import chess.domain.board.Position;

public class King extends Piece {

    public King(final Color color) {
        super(color, Type.KING);
    }

    @Override
    public boolean isMovable(Position source, Position target) {
        return true; // 일단 임시로 허용!
    }

}
