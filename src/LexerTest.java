import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

public class LexerTest {
    /**
     * tests lex(String) method
     * @throws Exception 
     */
    @Test
    void testLex() throws Exception {
        Lexer lexer = new Lexer("Test.txt");
        LinkedList<Token> tokens = lexer.lex();

        assertEquals("WORD(x) 1-0", tokens.get(0).toString());
        assertEquals("EQUALS() 1-2", tokens.get(1).toString());
        assertEquals("NUMBER(5) 1-4", tokens.get(2).toString());
        assertEquals("PLUS() 1-6", tokens.get(3).toString());
        assertEquals("LPAREN() 2-23", tokens.get(23).toString());
        assertEquals("PRINT() 5-0", tokens.get(35).toString());
        assertEquals("DATA() 8-0", tokens.get(48).toString());
        assertEquals("STRINGLITERAL(hello) 8-5", tokens.get(49).toString());
        assertEquals("LABEL(label:) 12-0", tokens.get(77).toString());
        assertEquals("RANDOM() 15-0", tokens.get(86).toString());
        assertEquals("FOR() 17-0", tokens.get(91).toString());
        assertEquals("TO() 17-10", tokens.get(95).toString());
        assertEquals("STEP() 17-16", tokens.get(97).toString());
        assertEquals("NEXT() 18-0", tokens.get(100).toString());
        assertEquals("GOSUB() 20-0", tokens.get(104).toString());
        assertEquals("IF() 22-0", tokens.get(108).toString());
        assertEquals("THEN() 22-7", tokens.get(112).toString());
        assertEquals("WHILE() 24-0", tokens.get(116).toString());
        assertEquals("LEFT$() 28-0", tokens.get(133).toString());
        assertEquals("RIGHT$() 29-0", tokens.get(142).toString());
        assertEquals("END() 39-0", tokens.get(181).toString());
    }
}