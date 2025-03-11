import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CodeHandlerTest {
    /**
     * tests peek(int) method
     * @throws Exception 
     */
    @Test
    void testPeek() throws Exception {
        CodeHandler codeHandler = new CodeHandler("Test.txt");
        
        char x = codeHandler.Peek(0);
        assertEquals('x', x);

        x = codeHandler.Peek(6);
        assertEquals('+', x);
        
        x = codeHandler.Peek(9);
        assertEquals(' ', x);

        x = codeHandler.Peek(41);
        assertEquals('8', x);

        x = codeHandler.Peek(61);
        assertEquals('\n', x);

        x = codeHandler.Peek(115);
        assertEquals('l', x);

        x = codeHandler.Peek(131);
        assertEquals('\"', x);

        x = codeHandler.Peek(2000);
        assertEquals('\0', x);
    }
    
    /**
     * tests peekString(int) method
     */
    @Test
    void testPeekString() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        String nextChars = codeHandler.PeekString(5);
        assertEquals("x = 5", nextChars);

        codeHandler.Swallow(30);
        nextChars = codeHandler.PeekString(10);
        assertEquals("/ 5 + x * ", nextChars);

        codeHandler.Swallow(30);
        nextChars = codeHandler.PeekString(7);
        assertEquals("\r\nword ", nextChars);

        codeHandler.Swallow(200);
        nextChars = codeHandler.PeekString(15);
        assertEquals("A = 0 TO 10 STE", nextChars);

        codeHandler.Swallow(200);
        nextChars = codeHandler.PeekString(20);
        assertEquals("$(5.502)\r\n" +
                        "VAL(\"abc\")", nextChars);
    }
    
    /**
     * tests getChar() method
     */
    @Test
    void testGetChar() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        char c = codeHandler.GetChar();
        int i = codeHandler.getIndex();
        assertEquals('x', c);
        assertEquals(1, i);

        c = codeHandler.GetChar();
        i = codeHandler.getIndex();
        assertEquals(' ', c);
        assertEquals(2, i);

        codeHandler.Swallow(110);
        c = codeHandler.GetChar();
        i = codeHandler.getIndex();
        assertEquals('\"', c);
        assertEquals(113, i);
    }

    /**
     * tests swallow(int) and getIndex() methods
     */
    @Test
    void testSwallow() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        int x = codeHandler.getIndex();
        assertEquals(0, x);

        codeHandler.Swallow(10);
        x = codeHandler.getIndex();
        assertEquals(10, x);

        codeHandler.Swallow(5);
        x = codeHandler.getIndex();
        assertEquals(15, x);

        codeHandler.Swallow(90);
        x = codeHandler.getIndex();
        assertEquals(105, x);

        codeHandler.Swallow(0);
        x = codeHandler.getIndex();
        assertEquals(105, x);

        codeHandler.Swallow(1);
        x = codeHandler.getIndex();
        assertEquals(106, x);
    }

    /**
     * tests isDone() method
     */
    @Test
    void testIsDone() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        assertFalse(codeHandler.IsDone());

        codeHandler.Swallow(100);
        assertFalse(codeHandler.IsDone());

        codeHandler.Swallow(415);
        assertTrue(codeHandler.IsDone());
    }

    /**
     * tests remainder() method
     */
    @Test
    void testRemainder() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        String remainder = codeHandler.Remainder();
        assertEquals("x = 5 + 5 + 9 * 0 - 2\r\n" +
                        "y = 10 / 5 + x * -80 - (100.5 + -0.5)\r\n" +
                        "word = 120\r\n" +
                        "\r\n" +
                        "PRINT x\r\n" +
                        "PRINT word, y, 5+4\r\n" +
                        "\r\n" +
                        "DATA \"hello\", 12, -3.5, \"World\"\r\n" +
                        "READ stringVar$, num, float%, string$\r\n" +
                        "INPUT \"Input numbers:\", num1, num2, num3\r\n" +
                        "\r\n" +
                        "label:\r\n" +
                        "m = 5 + 2\r\n" +
                        "\r\n" +
                        "RANDOM()\r\n" +
                        "\r\n" +
                        "FOR A = 0 TO 10 STEP 2\r\n" +
                        "NEXT A\r\n" +
                        "\r\n" +
                        "GOSUB label\r\n" +
                        "\r\n" +
                        "IF x<5 THEN xIsSmall\r\n" +
                        "\r\n" +
                        "WHILE y > 8-3 endWhileLabel\r\n" +
                        "y = y + 1\r\n" +
                        "endWhileLabel:\r\n" +
                        "\r\n" +
                        "LEFT$(\"HELLO\", 6-1)\r\n" +
                        "RIGHT$(\"WORLD\", y)\r\n" +
                        "MID$(\"Your Name\", 8*7, x)\r\n" +
                        "\r\n" +
                        "NUM$(5.502)\r\n" +
                        "VAL(\"abc\")\r\n" +
                        "VAL%(\"efg\")\r\n" +
                        "\r\n" +
                        "\r\n" +
                        "xIsSmall:\r\n" +
                        "\r\n" +
                        "END",   remainder);

        codeHandler.Swallow(50);
        remainder = codeHandler.Remainder();
        assertEquals(".5 + -0.5)\r\n" +
                        "word = 120\r\n" +
                        "\r\n" +
                        "PRINT x\r\n" +
                        "PRINT word, y, 5+4\r\n" +
                        "\r\n" +
                        "DATA \"hello\", 12, -3.5, \"World\"\r\n" +
                        "READ stringVar$, num, float%, string$\r\n" +
                        "INPUT \"Input numbers:\", num1, num2, num3\r\n" +
                        "\r\n" +
                        "label:\r\n" +
                        "m = 5 + 2\r\n" +
                        "\r\n" +
                        "RANDOM()\r\n" +
                        "\r\n" +
                        "FOR A = 0 TO 10 STEP 2\r\n" +
                        "NEXT A\r\n" +
                        "\r\n" +
                        "GOSUB label\r\n" +
                        "\r\n" +
                        "IF x<5 THEN xIsSmall\r\n" +
                        "\r\n" +
                        "WHILE y > 8-3 endWhileLabel\r\n" +
                        "y = y + 1\r\n" +
                        "endWhileLabel:\r\n" +
                        "\r\n" +
                        "LEFT$(\"HELLO\", 6-1)\r\n" +
                        "RIGHT$(\"WORLD\", y)\r\n" +
                        "MID$(\"Your Name\", 8*7, x)\r\n" +
                        "\r\n" +
                        "NUM$(5.502)\r\n" +
                        "VAL(\"abc\")\r\n" +
                        "VAL%(\"efg\")\r\n" +
                        "\r\n" +
                        "\r\n" +
                        "xIsSmall:\r\n" +
                        "\r\n" +
                        "END",   remainder);

        codeHandler.Swallow(300);
        remainder = codeHandler.Remainder();
        assertEquals("eLabel\r\n" +
                        "y = y + 1\r\n" +
                        "endWhileLabel:\r\n" +
                        "\r\n" +
                        "LEFT$(\"HELLO\", 6-1)\r\n" +
                        "RIGHT$(\"WORLD\", y)\r\n" +
                        "MID$(\"Your Name\", 8*7, x)\r\n" +
                        "\r\n" +
                        "NUM$(5.502)\r\n" +
                        "VAL(\"abc\")\r\n" +
                        "VAL%(\"efg\")\r\n" +
                        "\r\n" +
                        "\r\n" +
                        "xIsSmall:\r\n" +
                        "\r\n" +
                        "END", remainder);

        codeHandler.Swallow(150);
        remainder = codeHandler.Remainder();
        assertEquals("IsSmall:\r\n" +
                        "\r\n" +
                        "END",   remainder);
    }
}