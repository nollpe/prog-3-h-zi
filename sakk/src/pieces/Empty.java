package pieces;

import game_adm.Table;
import pieces.Piece;

public class Empty extends Piece
{
    public Empty(int x1, int y1, Table t1)
    {
        super(x1,y1,true,"eskujolesz.png",t1);

        isEmpty=true;
    }

    public boolean canMoveTo(int x1,int y2, Table t)
    {
        return false;
    }

    void moveTo(int x1, int y1, Table t) throws cannotMoveThere {
        throw new cannotMoveThere();
    }
}
//ez am nem kell