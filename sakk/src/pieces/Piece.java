package pieces;

import game_adm.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.LinkedList;

public abstract class Piece extends JLabel implements Serializable{
    /*mivel a tabla tartalmazza a szinuket ezert ennek nem kell csak a poziciot,
     amit azert hogy egyszeruen le lehessen kerni egy kulon classba tettem

     */
    protected Position pos;//the position of the piece
    protected boolean canCastle;//if the piece can castle
    protected boolean isWhite;//it's colour
    protected boolean isEmpty;//if it isn't actually a piece
    protected double valueOfPiece=0;//this would only be needed for an advenced bot
    public String ImageName;// the name of the image displayed on the screen
    public Table t;// the table on which the piece is
    LinkedList<Position> legalMoves=new LinkedList<Position>();//the positions the piece can legally move to
    JavaMouseDeer a;

    public boolean isWhite(){return isWhite;}

    public boolean isEmpty(){return isEmpty;}

    public boolean canCastle(){return canCastle;}

    public void setCanCastle(boolean s){canCastle=s;}

    public char getchar()
    {return ' ';}

    //makes a move with the piece
    public void moveTo(int x1,int y1) throws cannotMoveThere
    {
        if(!this.canMoveTo(x1, y1))
        {
            throw new cannotMoveThere();
        }
        //remove the piece from the array of its colour
        if(this.isWhite && t.getBlackPeces().contains(t.getPieceOn(x1,y1)))
        {
            t.getBlackPeces().remove(t.getPieceOn(x1,y1));
            t.pieceCaptured();
        }
        else if(!this.isWhite && t.getWhitePieces().contains(t.getPieceOn(x1,y1)))
        {
            t.getWhitePieces().remove(t.getPieceOn(x1,y1));
            t.pieceCaptured();
        }

        t.setPieceOn(x1,y1,this);
        t.setPieceOn(this.pos.positionX,this.pos.positionY,new Empty(this.pos.positionX,this.pos.positionY,t));
        this.pos.positionX=x1;
        this.pos.positionY=y1;
        setBounds(x1*80,y1*80,80,80);
        canCastle=false;

    }

    //on which square the piece gives check on (not necessarily the same as it can move to)
    public boolean givesCheckOn(int x1,int y1)
    {
        return this.canMoveTo(x1,y1);
    }

    //only called when looking at legal moves of a piece
    public boolean KingWontBeInCheck(Position takenPiece)
    {
        if(t.getTurnOfWhite()!=this.isWhite)
        {
            return true;
        }
        boolean returnValue;
        //making the move on the board
        Piece saveTaken=t.getPieceOn(takenPiece.positionX,takenPiece.positionY);//save the piece from where we move
        t.setPieceOn(takenPiece.positionX,takenPiece.positionY,this);//we make the move on the board
        t.setPieceOn(this.pos.positionX,this.pos.positionY,new Empty(this.pos.positionX,this.pos.positionY,this.t));//make a new empty piece from where we move
        //remove the taken piece so it cant give check
        if(saveTaken.isWhite && !(saveTaken.isEmpty)){t.getWhitePieces().remove(saveTaken);}
        if(!saveTaken.isWhite && !(saveTaken.isEmpty)){t.getBlackPeces().remove(saveTaken);}


        King myKing;
        if (this.isWhite) myKing = t.getWhiteKing();
        else myKing = t.getBlackKing();
        returnValue = !myKing.isInCheck();

        t.setPieceOn(takenPiece.positionX,takenPiece.positionY,saveTaken);//put the taken piece back
        t.setPieceOn(this.pos.positionX,this.pos.positionY,this);//put our piecec back
        if(saveTaken.isWhite && !(saveTaken.isEmpty)){t.getWhitePieces().add(saveTaken);}//but it back into the arrays
        if(!saveTaken.isWhite && !(saveTaken.isEmpty)){t.getBlackPeces().add(saveTaken);}

        return returnValue;
    }

    //determines if the piece can move to a certain position
    public boolean canMoveTo(int x1,int y1)
    {
        legalMoves=this.getLegalMoves();
        Position to=new Position(x1,y1);
        for (Position legalMove : legalMoves)
        {
            if (to.equals(legalMove))
            {
                return true;
            }
        }
        return false;
    }

    //never checks if the king is in check
    public LinkedList<Position> getLegalMoves()
    {
        return new LinkedList<Position>();
    }

    //constructor
    public Piece(int x1, int y1, boolean iw, String kepnev, Table t1)
    {
        canCastle=false;
        pos=new Position(x1,y1);
        isWhite =iw;
        isEmpty=false;
        ImageName=kepnev;
        t=t1;
        setBounds(x1*80,y1*80,80,80);
        a=new JavaMouseDeer();
        addMouseListener(a);

        Image image = Toolkit.getDefaultToolkit().getImage(ImageName);
        image = image.getScaledInstance(80,80,Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        this.setIcon(icon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public Position getPos() {
        return pos;
    }

    class JavaMouseDeer implements MouseListener,Serializable
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            t.wasclicked(Piece.this);
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e){}
        @Override
        public void mouseExited(MouseEvent e){}
        @Override
        public void mouseReleased(MouseEvent e){}
    }
}

