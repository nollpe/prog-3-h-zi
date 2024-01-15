package pieces;

import game_adm.Table;

import java.util.LinkedList;

public class Knight extends Piece
{
    public Knight(int x1, int y1, boolean iw, Table t1)
    {
        super(x1,y1,iw,iw?"white_knight.png":"black_knight.png",t1);
        valueOfPiece=3;
    }

    public char getchar()
    {return this.isWhite ?'N':'n';}

    public LinkedList<Position> getLegalMoves()
    {
        LinkedList<Position> ret=new LinkedList<Position>();
        for(int x=1;x<=2;x++)
        {
            int y=3-x;
            for(int tox=-1;tox<=1;tox+=2)
            {   //itt már rosszul néz ki geci
                for (int toy = -1; toy <= 1; toy += 2)
                {
                    Position temp=new Position(this.pos.positionX+tox*x,this.pos.positionY+toy*y);
                    //a tablen belul van
                    if(temp.positionX<=7&&temp.positionY<=7&&temp.positionX>=0&&temp.positionY>=0)
                    {   //a mezo ures, vagy ellenfel babu all ott
                        if(t.getPieceOn(temp.positionX,temp.positionY).isEmpty || (!t.getPieceOn(temp.positionX,temp.positionY).isEmpty && t.getPieceOn(temp.positionX,temp.positionY).isWhite !=this.isWhite))
                        {
                            if(this.KingWontBeInCheck(temp))
                            {
                                ret.add(temp);
                            }

                        }

                    }
                }
            }
        }
        legalMoves=ret;
        return ret;
    }
}
