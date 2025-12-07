package chess.domain.piece;

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

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    // 나중에 구현할 움직임 로직 (추상 메서드)
    // public abstract boolean canMove(Position source, Position target);
}