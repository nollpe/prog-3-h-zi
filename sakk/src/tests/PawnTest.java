package tests;

import game_adm.Table;
import org.junit.jupiter.api.Test;
import pieces.Position;
import pieces.cannotMoveThere;

import javax.swing.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {


    @Test
    void getLegalMoves() {
        Table t;
        try
        {
            FileInputStream f = new FileInputStream("enpassant.txt");
            ObjectInputStream in = new ObjectInputStream(f);
            t = (Table)in.readObject();
            in.close();

            assertEquals(2,t.getPieceOn(3,1).getLegalMoves().size());
            t.ILikeToMoveItMoveIt(new Position(3,1),new Position(3,3));
            assertEquals(2,t.getPieceOn(4,3).getLegalMoves().size());
            assertTrue(t.getPieceOn(4,3).canMoveTo(3,2));//it can enpassant

        }
        catch(IOException | ClassNotFoundException ex){fail("file not found");}

    }

    @Test
    void givesCheckOn() {
        Table t;
        try
        {
            FileInputStream f = new FileInputStream("enpassant.txt");
            ObjectInputStream in = new ObjectInputStream(f);
            t = (Table)in.readObject();
            in.close();

            t.ILikeToMoveItMoveIt(new Position(3,1),new Position(3,3));
            assertTrue(t.getPieceOn(4,3).givesCheckOn(5,2));
            assertFalse(t.getPieceOn(4,3).canMoveTo(5,2));


        }
        catch(IOException | ClassNotFoundException ex){fail("file not found");}
    }

    @Test
    void moveTo()
    {
        Table t;
        try
        {
            FileInputStream f = new FileInputStream("enpassant.txt");
            ObjectInputStream in = new ObjectInputStream(f);
            t = (Table)in.readObject();
            in.close();

            assertThrows(cannotMoveThere.class,()->t.getPieceOn(3,1).moveTo(6,6));

        }
        catch(IOException | ClassNotFoundException ex){fail("file not found");}

    }
}