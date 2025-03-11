import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.LinkedList;
import org.junit.Test;
import ASTNodes.*;

public class InterpreterTest {

    @Test
    public void testData() throws Exception{
        Lexer lexer = new Lexer("Test.txt");
        Parser parser = new Parser(lexer.lex());
        StatementsNode statementsNode = parser.parse();
        Interpreter interpreter = new Interpreter(statementsNode);
        LinkedList<Node> data = interpreter.getData();

        assertEquals(12, ((IntegerNode)data.get(0)).getNumber());
        assertEquals(43, ((IntegerNode)data.get(1)).getNumber());
        assertEquals(23, ((IntegerNode)data.get(2)).getNumber());
        assertEquals(12, ((IntegerNode)data.get(3)).getNumber());
        assertEquals("string", ((StringNode)data.get(4)).getString());
        assertEquals(12.45f, ((FloatNode)data.get(5)).getNumber(), .01);
    }

    @Test
    public void testBuiltInFunctions(){
        Interpreter interpreter = new Interpreter(new StatementsNode(new LinkedList<>()));

        // test RANDOM()
        int rand = interpreter.RANDOM();
        assertTrue(rand <= Integer.MAX_VALUE && rand >= Integer.MIN_VALUE);

        // test LEFT$(string, int)
        assertEquals("Hello W", interpreter.LEFT("Hello World", 7));

        // test RIGHT$(string, int)
        assertEquals("rld", interpreter.RIGHT("Hello World", 3));

        // test MID$(string, int, int)
        assertEquals(" Wor", interpreter.MID("Hello World", 5, 4));

        // test NUM$(int)
        assertEquals("192837", interpreter.NUM(192837));

        // test NUM$(float)
        assertEquals("1234.987", interpreter.NUM(1234.987f));

        // test VAL(string)
        assertEquals(9000, interpreter.VAL("9000"));

        // test VALFloat(string)
        assertEquals(17562.222f, interpreter.VALFloat("17562.222"), .001);
    }
}