import java.util.LinkedList;
import ASTNodes.*;

public class Basic {
    public static void main(String[] args) throws Exception {
        
        // Ensures that there is only 1 argument provided
        if(args.length != 1){
            System.err.println("Provide 1 argument");
            System.exit(0);
        }
        
        Lexer lexer = new Lexer(args[0]);                   // initialize lexer
        LinkedList<Token> tokens = lexer.lex();             // call lex() on lexer

        Parser parser = new Parser(tokens);                 // initialize parser
        StatementsNode program = parser.parse();            // call parse() of parser
        
        Interpreter interpreter = new Interpreter(program); // initialize interpreter
        interpreter.interpret();                            // call interpret() on interpreter
    }
}