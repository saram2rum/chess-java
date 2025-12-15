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
        Direction direction;

        try {
            // 🚨 방향부터 구하다가 터지는 중! -> 예외 처리로 감싸기
            direction = Direction.of(source, target);
        } catch (IllegalArgumentException e) {
            return false; // 방향이 이상하면 폰은 절대 못 감
        }

        // 🚨 [추가] 거리 계산: 폰은 무조건 1칸(대각선 포함)만 움직일 수 있음!
        // (처음 2칸 움직이는 룰은 나중에 추가하더라도 일단 기본은 1칸)
        int xDiff = Math.abs(source.getX() - target.getX());
        int yDiff = Math.abs(source.getY() - target.getY());

        if (xDiff > 1 || yDiff > 1) {
            return false; // 1칸 넘게 차이나면 폰은 절대 못 감 (레이저 발사 금지 🙅‍♂️)
        }

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