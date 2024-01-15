package tests;

import game_adm.Table;
import org.junit.jupiter.api.Test;
import pieces.Position;

import javax.swing.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void getLegalMoves() {
        Table t;
        try
        {
            FileInputStream f = new FileInputStream("castling.txt");
            ObjectInputStream in = new ObjectInputStream(f);
            t = (Table)in.readObject();
            in.close();

            assertEquals(3,t.getBlackKing().getLegalMoves().size());
            t.ILikeToMoveItMoveIt(new Position(4,0),new Position(6,0));
            assertEquals(3,t.getWhiteKing().getLegalMoves().size());
            t.ILikeToMoveItMoveIt(new Position(4,7),new Position(2,7));

            assertEquals(1,t.getBlackKing().getLegalMoves().size());
        }
        catch(IOException | ClassNotFoundException ex){fail("file not found");}


    }

    @Test
    void isInCheck() {
        Table t;
        try
        {
            FileInputStream f = new FileInputStream("check.txt");
            ObjectInputStream in = new ObjectInputStream(f);
            t = (Table)in.readObject();
            in.close();

            assertFalse(t.getBlackKing().isInCheck());
            t.ILikeToMoveItMoveIt(new Position(3,7),new Position(7,3));
            assertTrue(t.getBlackKing().isInCheck());
            assertEquals(0,t.getBlackKing().getLegalMoves().size());
        }
        catch(IOException | ClassNotFoundException ex){fail("file not found");}

    }

    @Test
    void canMoveTo() {
        Table t;
        try
        {
            FileInputStream f = new FileInputStream("check.txt");
            ObjectInputStream in = new ObjectInputStream(f);
            t = (Table)in.readObject();
            in.close();

            t.ILikeToMoveItMoveIt(new Position(3,7),new Position(7,3));
            assertFalse(t.getBlackKing().canMoveTo(5,1));
            assertFalse(t.getBlackKing().canMoveTo(3,0));
            assertFalse(t.getBlackKing().canMoveTo(4,1));
        }
        catch(IOException | ClassNotFoundException ex){fail("file not found");}
    }
}