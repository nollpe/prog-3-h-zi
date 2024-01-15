package pieces;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    public int positionX;
    public int positionY;

    public Position(int x,int y)
    {
        positionX=x;
        positionY=y;
    }

    public Position(Position pos) {
        positionX=pos.positionX;
        positionY=pos.positionY;
    }

    @Override//én csak meg akartam írni erre IDEfossa ezt
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return positionX == position.positionX && positionY == position.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }
}
