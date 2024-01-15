package pieces;

import game_adm.Table;
import pieces.Piece;
import pieces.Position;

import java.util.LinkedList;

public class Rook extends Piece
{
    public Rook(int x1, int y1, boolean iw, Table t1)
    {
        super(x1,y1,iw,iw?"white_rook.png":"black_rook.png",t1);
        valueOfPiece=5;
    }

    public char getchar()
    {return this.isWhite ?'R':'r';}

    public LinkedList<Position> getLegalMoves()
    {
        int x=this.pos.positionX,y=this.pos.positionY;
        LinkedList<Position> ret=new LinkedList<Position>();
        /*since the rook paralell to the edges of the board
         * there are 2 for s 1 for the x, 1 for the y value, they take the values -1:0 , 1:0 , 0:1 , 0:-1 to cover every direction a rook can go
         * than these are added to the rooks position until it gets stopped by another piece or reaches the end of the board
         * */
        for(int tox=-1;tox<=1;tox++)
        {
            for(int toy=-1+Math.abs(tox);toy<=1;toy+=2)
            {
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
