package chess.domain.board;

import java.util.Arrays;

public enum Direction {
    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1);

    public int getXDegree() {
        return xDegree;
    }

    public int getYDegree() {
        return yDegree;
    }

    private int xDegree;
    private int yDegree;

    Direction(int xDegree, int yDegree) {
        this.xDegree = xDegree;
        this.yDegree = yDegree;
    }

    public static Direction of(Position source, Position target) {
        int xDiff = source.xDiff(target);
        int yDiff = source.yDiff(target);

        int xStep = Integer.compare(xDiff, 0);
        int yStep = Integer.compare(yDiff, 0);

        return Arrays.stream(values())
                .filter(dir -> dir.xDegree == xStep && dir.yDegree == yStep)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("직선/대각선 방향이 아닙니다."));
    }
}
