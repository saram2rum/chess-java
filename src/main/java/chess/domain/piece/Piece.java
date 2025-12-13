package chess.domain.piece;

import chess.domain.board.Position;

import java.util.Objects;

public abstract class Piece {

    private final Color color;
    private final Type type;

    // 생성자: 기물을 만들 때 색상과 종류를 무조건 정해야 함
    public Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    public boolean isWhite() {
        return color.isWhite();
    }

    public boolean isBlack() {
        return color.isBlack();
    }

    public boolean isSliding() {
        return false;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    // Piece.java

    // ... 기존 코드 아래에 추가 ...

    // "source에서 target으로 움직이는 게 너의 규칙(전략)에 맞니?" 라고 묻는 메서드
    public abstract boolean isMovable(Position source, Position target);
}