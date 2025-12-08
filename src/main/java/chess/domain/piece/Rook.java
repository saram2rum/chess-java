package chess.domain.piece;

import chess.domain.board.Position;

public class Rook extends Piece {

    public Rook(final Color color) {
        super(color, Type.ROOK);
    }

    @Override
    public boolean isMovable(Position source, Position target) {
        return true; // 일단 임시로 허용!
    }

}
