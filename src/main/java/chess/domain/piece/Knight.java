package chess.domain.piece;

import chess.domain.board.Position;

public class Knight extends Piece {

    public Knight(final Color color) {
        super(color, Type.KNIGHT);
    }

    @Override
    public boolean isMovable(Position source, Position target) {
        return true; // 일단 임시로 허용!
    }

}
