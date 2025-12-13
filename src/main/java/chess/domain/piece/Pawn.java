package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;

public class Pawn extends Piece {

    // 1. 생성자 수정: 부모에게 "저는 폰입니다"라고 알려줌
    public Pawn(Color color) {
        super(color, Type.PAWN);
    }

    @Override
    public boolean isMovable(Position source, Position target, Piece targetPiece) {
        Direction direction = Direction.of(source, target);

        // 2. 방향 결정 (부모의 isWhite() 재사용)
        Direction forward = isWhite() ? Direction.NORTH : Direction.SOUTH;

        // 공격 방향 (백: 북서/북동, 흑: 남서/남동)
        // (Direction Enum 순서나 정의에 따라 다를 수 있으니 확인 필요)
        // 일단 개념적으로 '공격용 대각선'인지 확인
        boolean isAttackMove = isAttackDirection(direction);

        // [상황 A] 직진 (앞에 아무도 없어야 함)
        if (direction == forward) {
            return targetPiece == null;
        }

        // [상황 B] 대각선 공격 (적이 있어야 함)
        if (isAttackMove) {
            // 적이 있고 + 우리 편이 아니어야 함 (isSameColor 활용!)
            return targetPiece != null && !isSameColor(targetPiece);
        }

        return false;
    }

    // 대각선 방향인지 확인하는 도우미
    private boolean isAttackDirection(Direction direction) {
        if (isWhite()) {
            return direction == Direction.NORTHWEST || direction == Direction.NORTHEAST;
        }
        return direction == Direction.SOUTHWEST || direction == Direction.SOUTHEAST;
    }
}