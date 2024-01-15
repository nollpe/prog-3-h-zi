package pieces;

import game_adm.Table;
import pieces.Piece;
import pieces.Position;

import java.util.LinkedList;

public class Queen extends Piece
{
    public Queen(int x1, int y1, boolean iw, Table t1)
    {
        super(x1,y1,iw,iw?"white_queen.png":"black_queen.png",t1);
        valueOfPiece=9;
    }

    public char getchar()
    {return this.isWhite ?'Q':'q';}

    public LinkedList<Position> getLegalMoves()
    {
        int x=this.pos.positionX,y=this.pos.positionY;
        LinkedList<Position> ret=new LinkedList<Position>();
        /*since the queen can move to any direction
        * there are 2 for s 1 for the x, 1 for the y value, they both go from -1 to 1 to cover any direction
        * than these are added to the queens position until it gets stopped by another piece or reaches the end of the board
        * */
        for(int tox=-1;tox<=1;tox++)
        {
            for(int toy=-1;toy<=1;toy++)
            {
                if(toy==0 && tox==0)
                {
                    x=8;//kurvaszar megoldás #439
                        //it's a really bad break really
                }
                x+=tox;y+=toy;
                while(x>=0 && x<=7 && y>=0 && y<=7)
                {
                    Position temp=new Position(x,y);
                    if(t.getPieceOn(x,y).isEmpty)
                    {
                        if(this.KingWontBeInCheck(temp))
                        {
                            ret.add(temp);
                        }
                    }
                    else if(t.getPieceOn(x,y).isWhite !=this.isWhite)
                    {
                        if(this.KingWontBeInCheck(temp))
                        {
                            ret.add(temp);
                        }x=9;//ez csak egy fura break
                    }
                    else
                    {
                        x=9;//meg mindig csak egy fura break;
                    }
                    x+=tox;y+=toy;

                }
                x=this.pos.positionX;y=this.pos.positionY;
            }
        }
        legalMoves=ret;
        return ret;
    }

}
