package org.dama;

import org.dama.core.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testConstructorAndFields() {
        Position pos = new Position(6, 0);
        assertEquals(6, pos.row, "Nesedí row.");
        assertEquals(0, pos.col, "Nesedí col.");
    }

    @Test
    public void testEqualsAndHashCode() {
        Position p1 = new Position(6, 0);
        Position p2 = new Position(6, 0);
        Position p3 = new Position(5, 1);

        assertTrue(p1.equals(p2), "p1 a p2 by mali byť rovnaké.");
        assertEquals(p1.hashCode(), p2.hashCode(), "Hash kódy p1 a p2 by mali byť rovnaké.");
        assertFalse(p1.equals(p3), "p1 a p3 by nemali byť rovnaké.");
    }

    @Test
    public void testToString() {
        // row=6, col=0 => 8 - 6 = 2 => "A2"
        Position p = new Position(6, 0);
        String s = p.toString();
        assertEquals("A2", s, "Očakávaný reťazec 'A2' pre row=6, col=0.");
    }
}
