package org.dama;

import org.dama.core.Move;
import org.dama.core.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    public void testConstructorNoCapture() {
        Position from = new Position(6, 0);
        Position to = new Position(5, 1);
        Move move = new Move(from, to);

        assertEquals(from, move.getFrom(), "Odchádzajúca pozícia by mala sedieť.");
        assertEquals(to, move.getTo(), "Cieľová pozícia by mala sedieť.");
        assertFalse(move.isCapture(), "Nemal by to byť ťah s vyhodením (capture).");
        assertNull(move.getCapturedPosition(), "CapturedPosition by mal byť null.");
    }

    @Test
    public void testConstructorWithCapture() {
        Position from = new Position(6, 0);
        Position to = new Position(4, 2);
        Position captured = new Position(5, 1);
        Move move = new Move(from, to, captured);

        assertEquals(from, move.getFrom());
        assertEquals(to, move.getTo());
        assertTrue(move.isCapture(), "Očakávame ťah s vyhodením (capture).");
        assertEquals(captured, move.getCapturedPosition(), "Nezhoduje sa vyhodená pozícia.");
    }

    @Test
    public void testToString() {
        Move move1 = new Move(new Position(6,0), new Position(5,1));
        String s1 = move1.toString();
        assertTrue(s1.contains("->"), "toString by mal obsahovať '->'.");
        assertFalse(s1.contains("vyhodená:"), "Nemal by obsahovať 'vyhodená', lebo nie je capture.");

        Move move2 = new Move(new Position(6,0), new Position(4,2), new Position(5,1));
        String s2 = move2.toString();
        assertTrue(s2.contains("vyhodená"), "Pre ťah s vyhodením má obsahovať 'vyhodená:'.");
    }
}
