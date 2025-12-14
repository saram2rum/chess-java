package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;

public class Rook extends Piece {

    public Rook(final Color color) {
        super(color, Type.ROOK);
    }

    @Override
    public boolean isSliding() {
        return true; // 경로 장애물 검사 필요
    }

    @Override
    public boolean isMovable(Position source, Position target, Piece targetPiece) {
        // 1. 방향 계산 (직선/대각선이 아니거나 제자리니면 여기서 에러가 나서 걸러짐)
        Direction direction = Direction.of(source, target);

        // 2. "직선이니?" 라고 물어보면 끝!
        return direction.isLinear();
    }
}