package pieces;

import game_adm.Table;
import pieces.Piece;
import pieces.Position;

import java.util.LinkedList;

public class Pawn extends Piece
{
    public boolean startingPosition;

    public Pawn(int x1, int y1, boolean iw, Table t1)
    {
        super(x1,y1,iw,iw?"white_pawn.png":"black_pawn.png",t1);
        valueOfPiece=1;
        if(isWhite &&pos.positionY==6)startingPosition=true;
        if(!isWhite &&pos.positionY==1)startingPosition=true;
    }

    public char getchar()
    {return this.isWhite ?'P':'p';}

    public LinkedList<Position> getLegalMoves()
    {
        LinkedList<Position> ret=new LinkedList<Position>();
        int multiplyer = this.isWhite ?-1:1;
        Position temp;
        // move forvard
        temp=new Position(pos.positionX,pos.positionY+1*multiplyer);
        if(t.getPieceOn(pos.positionX,pos.positionY+1*multiplyer).isEmpty)
        {
            if(this.KingWontBeInCheck(temp))
                ret.add(temp);
        }
        //takes a piece on the left of it
        temp=new Position(pos.positionX-1,pos.positionY+1*multiplyer);
        if(pos.positionX-1>=0 && (temp.equals(t.getEnPassant()) || (t.getPieceOn(pos.positionX-1,pos.positionY+1*multiplyer).isWhite !=this.isWhite && !t.getPieceOn(pos.positionX-1,pos.positionY+1*multiplyer).isEmpty)))
        {
            if(this.KingWontBeInCheck(temp))
                ret.add(temp);
        }
        //takes a piece on the right of it
        temp=new Position(pos.positionX+1,pos.positionY+1*multiplyer);
        if(pos.positionX+1<=7 && (temp.equals(t.getEnPassant()) || (t.getPieceOn(pos.positionX+1,pos.positionY+1*multiplyer).isWhite !=this.isWhite && !t.getPieceOn(pos.positionX+1,pos.positionY+1*multiplyer).isEmpty)))
        {
            if(this.KingWontBeInCheck(temp))
                ret.add(temp);
        }
        //moves 2 squares forward
        temp=new Position(pos.positionX,pos.positionY+2*multiplyer);
        if(this.startingPosition && t.getPieceOn(pos.positionX,pos.positionY+1*multiplyer).isEmpty && t.getPieceOn(pos.positionX,pos.positionY+2*multiplyer).isEmpty)
        {
            if(this.KingWontBeInCheck(temp))
                ret.add(temp);
        }
        legalMoves=ret;
        return ret;
    }

    //determines if the pawn gives check on a certain position
    public boolean givesCheckOn(int x1,int y1)
    {
        int multiplyer = this.isWhite ?-1:1;
        if(y1==pos.positionY+1*multiplyer && (x1==pos.positionX-1 || x1==pos.positionX+1))
        {return true;}
        return false;
    }

    //moves to the given position if it's legal
    public void moveTo(int x1,int y1) throws cannotMoveThere
    {
        super.moveTo(x1,y1);

        //beert a masik oldalon
        if(this.isWhite && y1==0 || (!(this.isWhite) && y1==7) )
        {
            Piece temp=new Queen(x1,y1,this.isWhite,t);
            if(this.isWhite)
            {
                t.getWhitePieces().remove(this);
                t.getWhitePieces().add(temp);
            }
            else
            {
                t.getBlackPeces().remove(this);
                t.getBlackPeces().add(temp);
            }
            t.setPieceOn(x1,y1,temp);
        }
        if(t.getEnPassant()!=null && t.getEnPassant().equals(new Position(x1,y1)))
        {
            //remove the piece from the array of its colour
            if(this.isWhite && t.getBlackPeces().contains(t.getPieceOn(x1,y1+1)))
            {
                t.getBlackPeces().remove(t.getPieceOn(x1,y1+1));
                t.setPieceOn(x1,y1+1,new Empty(x1,y1+1,t));
            }
            else if(!this.isWhite && t.getWhitePieces().contains(t.getPieceOn(x1,y1-1)))
            {
                t.getWhitePieces().remove(t.getPieceOn(x1,y1-1));
                t.setPieceOn(x1,y1-1,new Empty(x1,y1-1,t));
            }
        }
        if(startingPosition)
        {
            if(this.pos.positionY==4)
                t.setEnPassant(new Position(pos.positionX,5));
            if(this.pos.positionY==3)
                t.setEnPassant(new Position(pos.positionX,2));

            t.setEnPassant();
        }
        t.pawnMoved();
        startingPosition=false;
    }

}
