package game_adm;

import pieces.Piece;
import pieces.Position;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class BasicBot implements Serializable{

    Table t;

    public BasicBot(Table t1) {
        t = t1;
    }

    public void makeBasicMove() {
        LinkedList<Piece> myPieces = t.getTurnOfWhite() ? t.getWhitePieces() : t.getBlackPeces();
        int randomNum = ThreadLocalRandom.current().nextInt(0, myPieces.size());
        LinkedList<Position> lm = myPieces.get(randomNum).getLegalMoves();
        int max = 100;
        while (lm.size() == 0 && max > 0) {
            randomNum = ThreadLocalRandom.current().nextInt(0, myPieces.size());
            lm = myPieces.get(randomNum).getLegalMoves();
            max--;
        }
        if (max == 0) return;
        int rn = ThreadLocalRandom.current().nextInt(0, lm.size());
        t.ILikeToMoveItMoveIt(myPieces.get(randomNum).getPos(), lm.get(rn));

    }

    /*void attack()
    {
        LinkedList<pieces.Piece> myPieces = t.getTurnOfWhite() ? t.getWhitePieces() : t.getBlackPeces();
        pieces.Position bestFrom=null, bestTo=null;
        int bestDistance=9;
        pieces.King OpponentsKing=t.getTurnOfWhite() ? t.getBlackKing():t.getWhiteKing();
        for(pieces.Piece p:myPieces)
        {
            LinkedList<pieces.Position> lm=p.getLegalMoves();
            for(pieces.Position pos:lm)
            {
                int d=Math.abs(pos.positionY-OpponentsKing.pos.positionY)+Math.abs(pos.positionX-OpponentsKing.pos.positionX);
                if(d<bestDistance)
                {
                    bestDistance=d;
                    bestFrom=p.pos;
                    bestTo=pos;
                }
            }

        }
        if(bestFrom==null)return;
        t.ILikeToMoveItMoveIt(bestFrom,bestTo);
    }

    void defend()
    {
        LinkedList<pieces.Piece> myPieces = t.getTurnOfWhite() ? t.getWhitePieces() : t.getBlackPeces();
        pieces.Position bestFrom=null, bestTo=null;
        int bestDistance=9;
        pieces.King OpponentsKing=(!t.getTurnOfWhite()) ? t.getBlackKing():t.getWhiteKing();
        for(pieces.Piece p:myPieces)
        {
            LinkedList<pieces.Position> lm=p.getLegalMoves();
            if(p!=OpponentsKing)
            {
                for(pieces.Position pos:lm)
                {
                    int d=Math.abs(pos.positionY-OpponentsKing.pos.positionY)+Math.abs(pos.positionX-OpponentsKing.pos.positionX);
                    if(d<bestDistance)
                    {
                        bestDistance=d;
                        bestFrom=p.pos;
                        bestTo=pos;
                    }
                }
            }


        }
        if(bestFrom==null)return;
        t.ILikeToMoveItMoveIt(bestFrom,bestTo);
    }*/


    /*public double evalState()
    {
        double asd=0;
        LinkedList<pieces.Piece> myPieces = t.getTurnOfWhite() ? t.getWhitePieces() : t.getBlackPeces();
        LinkedList<pieces.Piece> OpPieces = !t.getTurnOfWhite() ? t.getWhitePieces() : t.getBlackPeces();
        for(pieces.Piece a:myPieces)
        {
            asd+=a.valueOfPiece;
        }
        for(pieces.Piece a:OpPieces)
        {
            asd-=a.valueOfPiece;
        }
        return asd;
    }*/
}