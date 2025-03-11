import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;
import ASTNodes.*;

public class ParserTest {
    @Test
    void testParse() throws Exception {
        Lexer lexer = new Lexer("Test.txt");
        Parser parser = new Parser(lexer.lex());
        StatementsNode nodes = parser.parse();

        LinkedList<StatementNode> nodesList = nodes.getStatements();

        assertEquals("AssignmentNode(x = MathOpNode(MathOpNode(MathOpNode(5 , add , 5) , add , MathOpNode(9 , multiply , 0)) , subtract , 2))"
                    , nodesList.get(0).toString());
        
        assertEquals("AssignmentNode(y = MathOpNode(MathOpNode(MathOpNode(10 , divide , 5) , add , MathOpNode(x , multiply , -80)) , subtract , MathOpNode(100.5 , add , -0.5)))"
                    , nodesList.get(1).toString());
        
        assertEquals("AssignmentNode(word = 120)"
                    , nodesList.get(2).toString());
        
        assertEquals("PrintNode(\n" +
                        "x\n" +
                        ")"
                    , nodesList.get(3).toString());

        assertEquals("PrintNode(\n" +
                        "word\n" +
                        "y\n" +
                        "MathOpNode(5 , add , 4)\n" +
                        ")"
                    , nodesList.get(4).toString());

        assertEquals("DataNode(\n" +
                        "hello\n" +
                        "12\n" +
                        "-3.5\n" +
                        "World\n" +
                        ")"
                    , nodesList.get(5).toString());

        assertEquals("ReadNode(\n" +
                        "stringVar$\n" +
                        "num\n" +
                        "float%\n" +
                        "string$\n" +
                        ")"
                    , nodesList.get(6).toString());

        assertEquals("InputNode(\n" +
                        "Input numbers:\n" +
                        "num1\n" +
                        "num2\n" +
                        "num3\n" +
                        ")", nodesList.get(7).toString());

        assertEquals("LabeledStatementNode(label)"
                    , nodesList.get(8).toString());
        
        assertEquals("AssignmentNode(m = MathOpNode(5 , add , 2))"
                    , nodesList.get(9).toString());
        
        assertEquals("FunctionNode(RANDOM([]))"
                    , nodesList.get(10).toString());
        
        assertEquals("ForNode(A = 0 -> 10, by 2)"
                    , nodesList.get(11).toString());

        assertEquals("NextNode(A)"
                    , nodesList.get(12).toString());
        
        assertEquals("GoSubNode(label)"
                    , nodesList.get(13).toString());
        
        assertEquals("IfNode(BooleanExpNode(x , LessThan , 5) , xIsSmall)"
                    , nodesList.get(14).toString());
        
        assertEquals("WhileNode(BooleanExpNode(y , GreaterThan , MathOpNode(8 , subtract , 3)) , endWhileLabel)"
                    , nodesList.get(15).toString());

        assertEquals("AssignmentNode(y = MathOpNode(y , add , 1))"
                    , nodesList.get(16).toString());

        assertEquals("LabeledStatementNode(endWhileLabel)"
                    , nodesList.get(17).toString());

        assertEquals("FunctionNode(LEFT$([HELLO, MathOpNode(6 , subtract , 1)]))"
                    , nodesList.get(18).toString());

        assertEquals("FunctionNode(RIGHT$([WORLD, y]))"
                    , nodesList.get(19).toString());

        assertEquals("FunctionNode(MID$([Your Name, MathOpNode(8 , multiply , 7), x]))"
                    , nodesList.get(20).toString());

        assertEquals("FunctionNode(NUM$([5.502]))"
                    , nodesList.get(21).toString());

        assertEquals("FunctionNode(VAL([abc]))"
                    , nodesList.get(22).toString());

        assertEquals("FunctionNode(VALPERCENT([efg]))"
                    , nodesList.get(23).toString());

        assertEquals("LabeledStatementNode(xIsSmall)"
                    , nodesList.get(24).toString());

        assertEquals("EndNode()"
                    , nodesList.get(25).toString());
    }
}