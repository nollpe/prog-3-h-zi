package pieces;

import game_adm.Table;
import pieces.Empty;
import pieces.Piece;
import pieces.Position;

import java.util.LinkedList;

public class King extends Piece
{

    public King(int x1, int y1, boolean iw, Table t1)
    {
        super(x1,y1,iw,iw?"white_king.png":"black_king.png",t1);
        valueOfPiece=15;
    }

    public char getchar()
    {return this.isWhite ?'K':'k';}

    public boolean givesCheckOn(int x1,int y1)
    {
        if(Math.abs(this.pos.positionX-x1)<=1 &&Math.abs(this.pos.positionY-y1)<=1)
            return true;
        return false;
    }

    public LinkedList<Position> getLegalMoves()
    {
        LinkedList<Position> ret = new LinkedList<Position>();
        for(int x=-1;x<=1;x++)
        {
            for(int y=-1;y<=1;y++)
            {
                int x1=this.pos.positionX+x,y1=this.pos.positionY+y;
                //I became the very thing i swore to destroy
                if((x1>=0 && x1<=7 && y1>=0 && y1<=7) && (this.isWhite ==t.getTurnOfWhite() && ((t.getPieceOn(x1,y1).isEmpty || t.getPieceOn(x1,y1).isWhite !=this.isWhite))))
                {
                    //empty a kiraly helyere
                    t.setPieceOn(this.pos.positionX,this.pos.positionY,new Empty(this.pos.positionX,this.pos.positionY,this.t));
                    //elmentjuk a leutott babut
                    Piece saveTaken=this.t.getPieceOn(x1,y1);
                    //a leutott babu helyere ureset rakunk
                    t.setPieceOn(x1,y1,this);
                    if(!this.isInCheckOn(x1,y1))
                    {
                        ret.add(new Position(x1,y1));
                    }
                    t.setPieceOn(this.pos.positionX,this.pos.positionY,this);
                    t.setPieceOn(x1,y1,saveTaken);
                }
            }
        }

        //checking if the king can castle on the
        if((!this.isInCheck()) && this.canCastle && t.getPieceOn(0,this.pos.positionY).canCastle)
        {
            boolean l=true;
            for(int i=1;i<=3;i++)
            {
                if(!(t.getPieceOn(i,this.pos.positionY).isEmpty && !this.isInCheckOn(i,this.pos.positionY)))
                {
                    l=false;
                }
            }
            if(l)ret.add(new Position(2,this.pos.positionY));
        }

        if((!this.isInCheck()) && this.canCastle && t.getPieceOn(7,this.pos.positionY).canCastle)
        {
            boolean l=true;
            for(int i=5;i<=6;i++)
            {
                if(!(t.getPieceOn(i,this.pos.positionY).isEmpty && !this.isInCheckOn(i,this.pos.positionY)))
                {
                    l=false;
                }
            }
            if(l)ret.add(new Position(6,this.pos.positionY));
        }

        legalMoves=ret;

        return ret;
    }

    public boolean isInCheckOn(int x1,int y1)
    {
        LinkedList<Piece> opponentsPieces;
        if (this.isWhite) opponentsPieces = t.getBlackPeces();
        else opponentsPieces = t.getWhitePieces();

        for(int i=0;i<opponentsPieces.size();i++)
        {
            if(opponentsPieces.get(i).givesCheckOn(x1,y1))
            {
                return true;
            }
        }
        //minden lepes utan meg kene hivni?
        return false;
    }
    public boolean isInCheck()
    {
        return this.isInCheckOn(this.pos.positionX,this.pos.positionY);
    }


    public void moveTo(int x1,int y1) throws cannotMoveThere
    {
        boolean cc=this.canCastle?true:false;
        super.moveTo(x1,y1);
        //sancolas nem szep megoldas
        if(cc && this.pos.positionX==2)
        {
            t.setPieceOn(3,this.pos.positionY,t.getPieceOn(0,this.pos.positionY));
            t.setPieceOn(0,this.pos.positionY,new Empty(0,this.pos.positionY,t));
            t.getPieceOn(3,this.pos.positionY).pos=new Position(3,this.pos.positionY);
            t.getPieceOn(3,this.pos.positionY).setBounds(3*80,y1*80,80,80);
        }
        if(cc && this.pos.positionX==6)
        {
            t.setPieceOn(5,this.pos.positionY,t.getPieceOn(7,this.pos.positionY));
            t.setPieceOn(7,this.pos.positionY,new Empty(7,this.pos.positionY,t));
            t.getPieceOn(5,this.pos.positionY).pos=new Position(5,this.pos.positionY);
            t.getPieceOn(5,this.pos.positionY).setBounds(5*80,y1*80,80,80);
        }
    }
}
