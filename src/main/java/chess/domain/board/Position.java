package chess.domain.board;

import java.util.Objects;

public class Position {

    private final int x;
    private final int y;

    public Position(final String uiPosition) {
        this.x = uiPosition.charAt(0) - 'a';
        this.y = uiPosition.charAt(1) - '1';
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // Position.java

    // ... 기존 코드 아래에 추가 ...

    // x좌표(가로) 차이 계산
    public int xDiff(Position other) {
        return other.x - this.x;
    }

    // y좌표(세로) 차이 계산
    public int yDiff(Position other) {
        return other.y - this.y;
    }

    // (선택) 절대값으로 거리 구하기 (나중에 룩, 비숍 때 유용함)
    public boolean isVertical(Position other) {
        return this.x == other.x && this.y != other.y;
    }

}

