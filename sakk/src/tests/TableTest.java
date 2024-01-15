package tests;

import game_adm.Table;
import org.junit.jupiter.api.Test;
import pieces.Position;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void getTurnOfWhite() {
        Table t=new Table(new JFrame(),false);
        t.ILikeToMoveItMoveIt(new Position(4,6),new Position(4,4));
        assertFalse(t.getTurnOfWhite());
        t.ILikeToMoveItMoveIt(new Position(4,1),new Position(4,3));
        assertTrue(t.getTurnOfWhite());
    }

    @Test
    void setState() {
        Table t=new Table(new JFrame(),false);
        t.setState("r2qk2r/ppp1bppp/2np1n2/4p3/2B1P1b1/2NPBN2/PPP2PPP/R2QK2R w KQkq - 3 7");
        assertTrue(t.ILikeToMoveItMoveIt(new Position(2,5),new Position(3,3)));

    }

    @Test
    void getState() {
        Table t=new Table(new JFrame(),false);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",t.getState());
        t.ILikeToMoveItMoveIt(new Position(4,6),new Position(4,4));
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",t.getState());
    }

    @Test
    void ILikeToMoveItMoveIt() {
        Table t=new Table(new JFrame(),false);
        assertTrue(t.ILikeToMoveItMoveIt(new Position(4,6),new Position(4,4)));
        assertFalse(t.ILikeToMoveItMoveIt(new Position(4,4),new Position(4,3)));
        assertTrue(t.ILikeToMoveItMoveIt(new Position(4,1),new Position(4,3)));
    }
}