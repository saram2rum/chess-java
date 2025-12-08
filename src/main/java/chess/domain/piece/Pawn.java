package chess.domain.piece;

import chess.domain.board.Position;

public class Pawn extends Piece {

    public Pawn(final Color color) {
        super(color, Type.PAWN);
    }

    @Override
    public boolean isMovable(Position source, Position target) {
        // 1. 방향 결정 (흰색은 +1, 검은색은 -1)
        int direction = isWhite() ? 1 : -1;

        // 2. 가로(x) 위치는 같아야 함 (직진이니까)
        if (source.xDiff(target) != 0) {
            return false;
        }

        // 3. 세로(y) 위치가 정확히 '방향'만큼 차이나야 함 (한 칸 전진)
        return source.yDiff(target) == direction;
    }
}