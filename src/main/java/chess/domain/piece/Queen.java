package chess.domain.piece;

import chess.domain.board.Position;

public class Queen extends Piece {

    public Queen(final Color color) {
        super(color, Type.QUEEN);
    }

    @Override
    public boolean isMovable(Position source, Position target) {
        return true; // 일단 임시로 허용!
    }

}
