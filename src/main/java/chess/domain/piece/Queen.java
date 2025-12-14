package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color, Type.QUEEN);
    }

    @Override
    public boolean isSliding() {
        return true; // 🚧 경로 검사 필요
    }

    @Override
    public boolean isMovable(Position source, Position target, Piece targetPiece) {
        Direction direction = Direction.of(source, target);

        // 직선이거나 대각선이면 OK
        return direction.isLinear() || direction.isDiagonal();
    }
}