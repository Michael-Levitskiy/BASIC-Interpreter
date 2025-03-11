import java.util.LinkedList;
import java.util.Optional;
import ASTNodes.*;

public class Parser {
    
    // Class Instance Variable
    private TokenManager tokenManager;

    
    /**
     * Constructor
    */
    public Parser(LinkedList<Token> tokens) {
        this.tokenManager = new TokenManager(tokens);
    }


    /**
     * Public method to 'parse' the LinkedList of Tokens
     * Using TokenManager
     * @return the root, ProgramNode
     * @throws Exception 
     */
    public StatementsNode parse() throws Exception{
        LinkedList<StatementNode> statements = new LinkedList<>();
        
        // while there are more tokens
        while(tokenManager.MoreTokens()){
            statements.add(this.Statement());   // call statement and add to linked list
            this.AcceptSeparators();            // acceptor linefeed separators
        }
        return new StatementsNode(statements);
    }
    

    ////////////////////////////
    // Private Helper Methods //
    ////////////////////////////
    /**
     * Checks for linefeed, and continues checking if true
     * @return true if there was a linefeed and false if not
     */
    private boolean AcceptSeparators(){
        Optional<Token> next = tokenManager.Peek(0);    // peek the next token
        // return false if it's null or not an end of line
        if ((next.isEmpty()) || (next.get().getType() != Token.TokenType.ENDOFLINE)){
            return false;
        }
        // while loop that ends when we don't come across end of line in token manager
        while ((next.isPresent()) && (next.get().getType() == Token.TokenType.ENDOFLINE)){
            tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE);
            next = tokenManager.Peek(0);
        }
        return true;
    }

    /**
     * @return a StatementNode, which could be a PrintNode or AssignmentNode
     * @return null if node couldn't be made
     * @throws Exception 
     */
    private StatementNode Statement() throws Exception{
        // Check that we haven't reached the end of the list of tokens
        if (tokenManager.Peek(0).isPresent()){
            Token.TokenType type = tokenManager.Peek(0).get().getType();    // get the TokenType

            // first check if its a label
            if (type == Token.TokenType.LABEL){
                // get the value of the label
                String string = tokenManager.MatchAndRemove(type).get().getValue();
                return new LabeledStatementNode(string.substring(0, string.length()-1));
            }

            // if Type is WORD followed by an EQUALS, then AssignmentNode
            if (type == Token.TokenType.WORD){               
                if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getType() == Token.TokenType.EQUALS){
                    return this.Assignment();
                }
            }

            // if Type is PRINT, return a printNode
            if (type == Token.TokenType.PRINT){
                // call PrintList if there is a COMMA node 2 tokens after next
                if (tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getType() == Token.TokenType.COMMA){
                    return this.PrintList();
                }
                // otherwise, call PrintStatement
                return this.PrintStatement();
            }

            // if Type is a READ, return a ReadNode
            if (type == Token.TokenType.READ){return this.Read();}

            // if Type is DATA, return a DataNode
            if (type == Token.TokenType.DATA){return this.Data();}

            // if Type is INPUT, return an InputNode
            if (type == Token.TokenType.INPUT){return this.Input();}

            // if Type is GOSUB, return a GoSubNode
            if (type == Token.TokenType.GOSUB){
                // make sure that the next token is a word
                if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getType() == Token.TokenType.WORD){
                    tokenManager.MatchAndRemove(type);
                    return new GoSubNode(tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue());
                }
                throw new Exception("WORD expected after GOSUB");
            }

            // if Type is GOTO, return a GOTONode
            if (type == Token.TokenType.GOTO){
                // make sure that the next token is a word
                if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getType() == Token.TokenType.WORD){
                    tokenManager.MatchAndRemove(type);
                    return new GOTONode(tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue());
                }
                throw new Exception("WORD expected after GOTO");
            }

            // if Type is RETURN, return a ReturnNode
            if (type == Token.TokenType.RETURN){
                tokenManager.MatchAndRemove(type);
                return new ReturnNode();
            }

            // if Type is END, return an EndNode
            if (type == Token.TokenType.END){
                tokenManager.MatchAndRemove(type);
                return new EndNode();
            }

            // if Type is FOR, return a ForNode
            if (type == Token.TokenType.FOR){return this.For();}

            // if Type is NEXT, return a NextNode
            if (type == Token.TokenType.NEXT){
                tokenManager.MatchAndRemove(type);
                Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                if (token.isPresent()){
                    return new NextNode(new VariableNode(token.get().getValue()));
                }
                throw new Exception("Missing WORD after NEXT");
            }

            // if Type is IF, return an IfNode
            if (type == Token.TokenType.IF){return this.If();}

            // if Type is WHILE, return a WhileNode
            if (type == Token.TokenType.WHILE){return this.While();}

            // check if token is a function
            FunctionNode function = this.functionInvocation();
            if (function != null){
                return function;
            }
        }
        return null;
    }


    /**
     * @return the root node of the expression
     * @throws Exception 
     */
    private Node Expression() throws Exception{
        Node L = this.Term();                           // call term
        Optional<Token> next = tokenManager.Peek(0);  // peek the next token

        // while the next token is a '+' or '-'
        while((next.isPresent()) && 
                ((next.get().getType() == Token.TokenType.PLUS) || (next.get().getType() == Token.TokenType.MINUS))){
            
            next = tokenManager.MatchAndRemove(Token.TokenType.PLUS);
            // if the token is a plus, create a plus MathOpNode
            if (next.isPresent()){
                Node R = this.Term();
                MathOpNode mathOpNode = new MathOpNode(MathOpNode.Operation.add, L, R);
                L = mathOpNode;
            }
            else{   // else, create a subtract MathOpNode
                tokenManager.MatchAndRemove(Token.TokenType.MINUS);
                Node R = this.Term();
                MathOpNode mathOpNode = new MathOpNode(MathOpNode.Operation.subtract, L, R);
                L = mathOpNode;
            }
            // get the next token
            next = tokenManager.Peek(0);
        }
        return L;
    }


    /**
     * @return the root node of the term
     * @throws Exception 
     */
    private Node Term() throws Exception{
        Node L = this.Factor();                         // call factor
        Optional<Token> next = tokenManager.Peek(0);  // peek the next node

        // while the next token is a '*' or '/'
        while((next.isPresent()) && 
                ((next.get().getType() == Token.TokenType.MULTIPLY) || (next.get().getType() == Token.TokenType.DIVIDE))){
            
            next = tokenManager.MatchAndRemove(Token.TokenType.MULTIPLY);
            // if the token is multiply, create a multiply MathOpNode
            if (next.isPresent()){
                Node R = this.Factor();
                MathOpNode mathOpNode = new MathOpNode(MathOpNode.Operation.multiply, L, R);
                L = mathOpNode;
            }
            else{   // else, create a divide MathOpNode
                tokenManager.MatchAndRemove(Token.TokenType.DIVIDE);
                Node R = this.Factor();
                MathOpNode mathOpNode = new MathOpNode(MathOpNode.Operation.divide, L, R);
                L = mathOpNode;
            }
            // get the next token
            next = tokenManager.Peek(0);
        }
        return L;
    }


    /**
     * @return the root node of the factor
     * @throws Exception 
     */
    private Node Factor() throws Exception{
        Optional<Token> next = tokenManager.Peek(0);    // peek the next token and check if it's null
        if (next.isPresent()){
            // call private method to create an IntegerNode or FloatNode
            Node numberNode = this.intOrFloat();
            if (numberNode != null) {return numberNode;}
            
            // if token is a '(', except an expression between parenthesis
            next = tokenManager.MatchAndRemove(Token.TokenType.LPAREN);
            if (next.isPresent()){
                Node L = this.Expression();     // call expression for everything inside the parenthesis
                next = tokenManager.MatchAndRemove(Token.TokenType.RPAREN);

                // if the next token is a ')', return the node
                if (next.isPresent()){
                    return L;
                }
                else{throw new Exception("')' Expected after a '(<EXPRESSION>");}
            }

            // if token is a WORD, return a VariableNode
            next = tokenManager.MatchAndRemove(Token.TokenType.WORD);            
            if (next.isPresent()){
                return new VariableNode(next.get().getValue());
            }

            // check if token is a function
            FunctionNode function = this.functionInvocation();
            if (function != null){
                return function;
            }

            // throw an exception if we don't come across a number or a correct parenthesis
            throw new Exception("Could not find the appropriate Node in the Factor() method");
        }
        else{throw new Exception("No More Tokens");}
    }


    /**
     * Private method to create a NumberNode
     * A number node could be a positive or negative integer or float
     * @return the Node
     * @return null if we don't come across a minus or number
     * @throws Exception
     */
    private Node intOrFloat() throws Exception{
        Optional<Token> next = tokenManager.Peek(0);    // peek the next token and check if it's null
        if (next.isPresent()){

            // if token is a minus, expect a negative number and return either Integer or Float Node
            next = tokenManager.MatchAndRemove(Token.TokenType.MINUS);
            if (next.isPresent()){
                next = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                if (next.isPresent()){
                    String number = next.get().getValue();          // get the string value of the number to parse
                    // try parsing as an int and return IntegerNode
                    try{
                        int intNumber = Integer.parseInt(number);
                        return new IntegerNode(intNumber*-1);
                    }
                    // if error, then parse as a float and return FloatNode
                    catch(NumberFormatException e){
                        float floatNumber = Float.parseFloat(number);
                        return new FloatNode(floatNumber*-1);
                    }
                }
                else{throw new Exception("Expected NUMBER after a negative sign");}
            }

            // if token is a number, return either an Integer or Float Node
            next = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (next.isPresent()){
                String number = next.get().getValue();          // get the string value of the number to parse
                // try parsing as an int and return IntegerNode
                try{
                    int intNumber = Integer.parseInt(number);
                    return new IntegerNode(intNumber);
                }
                // if error, then parse as a float and return FloatNode
                catch(NumberFormatException e){
                    float floatNumber = Float.parseFloat(number);
                    return new FloatNode(floatNumber);
                }
            }
        }
        return null;
    }


    /**
     * @return an AssignmentNode, representing the form of "VARIABLE EQUALS Expression"
     * @return null if node couldn't be made
     * @throws Exception 
     */
    private AssignmentNode Assignment() throws Exception{
        // check that token was a word, representing the variable name
        Optional<Token> next = tokenManager.MatchAndRemove(Token.TokenType.WORD);
        if (next.isPresent()){
            // create a variableNode
            VariableNode variableNode = new VariableNode(next.get().getValue());

            // check that the next node is an equals
            if (tokenManager.MatchAndRemove(Token.TokenType.EQUALS).isPresent()){
                // return a new AssignmentNode, using variableNode and calling Expression method
                return new AssignmentNode(variableNode, this.Expression());
            }
        }
        return null;
    }


    /**
     * @return a PrintNode, for when we need to print a statement
     * @return  null if node could not be made
     * @throws Exception 
     */
    private PrintNode PrintStatement() throws Exception{
        // initialize the linkedList to be passed into the printNode
        LinkedList<Node> printStatement = new LinkedList<>();

        // confirm that the next token is a PRINT
        Optional<Token> next = tokenManager.MatchAndRemove(Token.TokenType.PRINT);
        if (next.isPresent()){
            // check if it is a string
            if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.STRINGLITERAL){
                printStatement.add(new StringNode(tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL).get().getValue()));
            }
            else{
                printStatement.add(this.Expression());  // call Expression and add to linkedList
            }
            return new PrintNode(printStatement);   // return a new PrintNode
        }
        return null;
    }


    /**
     * @return a PrintNode, for when we need to print multiple tokens
     * @return null if node couldn't be made
     * @throws Exception 
     */
    private PrintNode PrintList() throws Exception{
        // confirm that the next token is a PRINT
        Optional<Token> next = tokenManager.MatchAndRemove(Token.TokenType.PRINT);
        if (next.isPresent()){
            // create a linkedList of Node to add the nodes to print to
            LinkedList<Node> printStatements = new LinkedList<>();

            // while loop to continuously run until endOfLine or error
            while (true){
                // check if it is a string
                if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.STRINGLITERAL){
                    printStatements.add(new StringNode(tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL).get().getValue()));
                    next = tokenManager.MatchAndRemove(Token.TokenType.COMMA);
                    if(next.isEmpty()){
                        return new PrintNode(printStatements);
                    }
                }
                else{
                    printStatements.add(this.Expression());                             // call expression and add node to list
                    next = tokenManager.MatchAndRemove(Token.TokenType.COMMA);          // verify that the next node is a comma
                    if(next.isEmpty()){                                                 // if not a comma,
                        next = tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE);  // check if endOfLine
                        if(next.isPresent()){                                           // if yes,
                            return new PrintNode(printStatements);                      // return a new PrintNode of the linkedList
                        }
                        throw new Exception("Comma Expected in PRINT list statement");
                    }
                }
            }
        }
        return null;
    }


    /**
     * @return a ReadNode, adding the items after the READ token to the linkedList
     * @throws Exception 
     */
    private ReadNode Read() throws Exception{
        // check that token was a READ
        if (tokenManager.MatchAndRemove(Token.TokenType.READ).isPresent()){
            // create a linkedList to pass into ReadNode
            LinkedList<VariableNode> variables = new LinkedList<>();

            while(true){
                // check if a variable is the next token
                Optional<Token> next = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                if (next.isPresent()){
                    // if it is a variable, add new Variable node to linkedList
                    variables.add(new VariableNode(next.get().getValue()));

                    // check if a comma is the next token
                    if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isEmpty()){
                        // if it's not a comma, check if it's an ENDOFLINE
                        if (tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE).isPresent()){
                            // if so, return a readNode
                            return new ReadNode(variables);
                        }
                        // throw an exception if not ENDOFLINE
                        else{
                            throw new Exception("COMMA or ENDOFLINE is expected after variable in READ");
                        }
                    }
                }
                // throw an exception if a variable is not the next token
                else{
                    throw new Exception("Expected a 'Variable' after 'READ'");
                }
            }
        }
        return null;
    }


    /**
     * @return a DataNode, adding the items after the DATA token to the linkedList
     * @throws Exception 
     */
    private DataNode Data() throws Exception{
        // check that token was a DATA
        if (tokenManager.MatchAndRemove(Token.TokenType.DATA).isPresent()){
            // create a linkedList to pass into DataNode
            LinkedList<Node> variables = new LinkedList<>();

            while(true){
                // peak the next token as it can be a string, integer, or float
                if (tokenManager.Peek(0).isPresent()){
                    // get the TokenType
                    Token.TokenType type = tokenManager.Peek(0).get().getType();

                    // if type is a string, add StringNode to variables
                    if (type == Token.TokenType.STRINGLITERAL){
                        variables.add(new StringNode(tokenManager.MatchAndRemove(type).get().getValue()));
                    }
                    // if type is a minus or number, expect an IntegerNode or FloatNode and add to variables
                    else if (type == Token.TokenType.NUMBER || type == Token.TokenType.MINUS){
                        Node numberNode = this.intOrFloat();
                        if (numberNode != null) {variables.add(numberNode);}
                        else{throw new Exception("Invalid Number in DATA");}
                    }
                    // else, throw an exception
                    else{
                        throw new Exception("Incorrect Type in DATA");
                    }

                    // check that the next token is a comma
                    if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isEmpty()){
                        // if not a comma, check for endOfLine and return a DataNode if so
                        if (tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE).isPresent()){
                            return new DataNode(variables);
                        }
                        // else, throw and exception
                        else{
                            throw new Exception("Missing COMMA in DATA");
                        }
                    }
                }
                else{
                    throw new Exception("No more tokens to read after DATA");
                }
            }
        }
        return null;
    }


    /**
     * @return an InputNode, adding the items after the INPUT token to the linkedList
     * @throws Exception 
     */
    private InputNode Input() throws Exception{
        // check that the token is an INPUT
        if (tokenManager.MatchAndRemove(Token.TokenType.INPUT).isPresent()){
            LinkedList<Node> inputs = new LinkedList<>();

            // check if the first token is a string
            if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.STRINGLITERAL){
                inputs.add(new StringNode(tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL).get().getValue()));
                if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isEmpty()){
                    throw new Exception("Missing COMMA after initial STRING in INPUT");
                }
            }

            while(true){
                if (tokenManager.Peek(0).isPresent()){                    
                    // verify type is a word, add VariableNode to inputs
                    if (tokenManager.Peek(0).get().getType() == Token.TokenType.WORD){
                        inputs.add(new VariableNode(tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue()));
                    }
                    // else, throw an exception
                    else{
                        throw new Exception("Missing WORD in INPUT");
                    }

                    // check that the next token is a comma
                    if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isEmpty()){
                        // if not a comma, check for endOfLine and return a DataNode if so
                        if (tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE).isPresent()){
                            return new InputNode(inputs);
                        }
                        // else, throw and exception
                        else{
                            throw new Exception("Missing COMMA in INPUT");
                        }
                    }
                }
                else{
                    throw new Exception("No more tokens to read after INPUT");
                }
            }
        }
        return null;
    }


    /**
     * @return a ForNode
     * @throws Exception 
     */
    private ForNode For() throws Exception{
        // check that the token is a FOR
        if (tokenManager.MatchAndRemove(Token.TokenType.FOR).isPresent()){
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.WORD);
            // if next token is a WORD, create a variable node for the ForNode
            if (token.isPresent()){
                VariableNode variableNode = new VariableNode(token.get().getValue());
                // if next token is an EQUALS, continue with extracting FOR loop data
                if (tokenManager.MatchAndRemove(Token.TokenType.EQUALS).isPresent()){
                    // check that the next token is a number
                    token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                    if (token.isPresent()){
                        int startValue = Integer.parseInt(token.get().getValue());
                        // check that the next token is a TO
                        if (tokenManager.MatchAndRemove(Token.TokenType.TO).isPresent()){
                            // check that the next token is a number
                            token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                            if (token.isPresent()){
                                int endValue = Integer.parseInt(token.get().getValue());
                                // determine if next token is a STEP and create a ForNode from there
                                if (tokenManager.MatchAndRemove(Token.TokenType.STEP).isPresent()){
                                    // check that we are given the increment value
                                    token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                                    if (token.isPresent()){
                                        int increment = Integer.parseInt(token.get().getValue());
                                        return new ForNode(variableNode, startValue, endValue, increment);
                                    }
                                    else{
                                        throw new Exception("Missing Increment Value");
                                    }
                                }
                                else{
                                    return new ForNode(variableNode, startValue, endValue);
                                }
                            }
                            else{
                                throw new Exception("Missing Ending Number in FOR Statement");
                            }
                        }
                        else{
                            throw new Exception("Missing TO in FOR Statement");
                        }
                    }
                    else{
                        throw new Exception("Missing Starting Number in FOR Statement");
                    }
                }
                else{
                    throw new Exception("Missing Equals in FOR Statement");
                }
            }
            else{
                throw new Exception("Missing Variable in FOR Statement");
            }
        }
        return null;
    }

    /**
     * @return a BooleanExpNode
     * @throws Exception 
     */
    private BooleanExpNode BooleanExpression() throws Exception{
        Node left = this.Expression();      // call Expression to get the left side of Boolean Expression
        BooleanExpNode.Operation op;        // create variable to hold boolean operation
        
        // if statements to correctly label op
        if (tokenManager.MatchAndRemove(Token.TokenType.GREATERTHAN).isPresent()){op = BooleanExpNode.Operation.GreaterThan;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.GREATERTHANEQUAL).isPresent()){op = BooleanExpNode.Operation.GreaterThanEqual;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.LESSTHAN).isPresent()){op = BooleanExpNode.Operation.LessThan;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.LESSTHANEQUAL).isPresent()){op = BooleanExpNode.Operation.LessThanEqual;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.NOTEQUAL).isPresent()){op = BooleanExpNode.Operation.NotEqual;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.EQUALS).isPresent()){op = BooleanExpNode.Operation.Equals;}
        else{throw new Exception("Not Existent Boolean Operation");}
        
        Node right = this.Expression();     // call expression to ge the right side of Boolean Expression
        return new BooleanExpNode(op, left, right);
    }

    /**
     * @return an IfNode
     * @throws Exception 
     */
    private IfNode If() throws Exception{
        // check that the token is IF
        if (tokenManager.MatchAndRemove(Token.TokenType.IF).isPresent()){
            BooleanExpNode booleanExpression = this.BooleanExpression();
            // check that next token is a THEN
            if (tokenManager.MatchAndRemove(Token.TokenType.THEN).isPresent()){
                // check that next token is a WORD and use as LabelName
                Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                if (token.isPresent()){
                    return new IfNode(booleanExpression, token.get().getValue());
                }
                else{
                    throw new Exception("No LABEL given in IF Statement");
                }
            }
            else{
                throw new Exception("Missing THEN in IF statement");
            }
        }
        return null;
    }

    /**
     * @return a WhileNode
     * @throws Exception 
     */
    private WhileNode While() throws Exception{
        // check that the token is WHILE
        if (tokenManager.MatchAndRemove(Token.TokenType.WHILE).isPresent()){
            BooleanExpNode booleanExpression = this.BooleanExpression();
            // check that next token is WORD and use as LabelName
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.WORD);
            if (token.isPresent()){
                return new WhileNode(booleanExpression, token.get().getValue());
            }
            else{
                throw new Exception("No LABEL given in WHILE Statement");
            }
        }
        return null;
    }


    /**
     * @return a FunctionNode
     * @throws Exception
     */
    private FunctionNode functionInvocation() throws Exception{
        FunctionNode.Function function;     // variable to hold the function name
        
        // If statements to determine whether token is a function call
        if (tokenManager.MatchAndRemove(Token.TokenType.RANDOM).isPresent()){function = FunctionNode.Function.RANDOM;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.LEFT$).isPresent()){function = FunctionNode.Function.LEFT$;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.RIGHT$).isPresent()){function = FunctionNode.Function.RIGHT$;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.MID$).isPresent()){function = FunctionNode.Function.MID$;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.NUM$).isPresent()){function = FunctionNode.Function.NUM$;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.VAL).isPresent()){function = FunctionNode.Function.VAL;}
        else if (tokenManager.MatchAndRemove(Token.TokenType.VALPERCENT).isPresent()){function = FunctionNode.Function.VALPERCENT;}
        else {return null;}

        LinkedList<Node> parameters = new LinkedList<>();   // linkedList to be passed into FunctionNode

        // check for a left parenthesis
        if (tokenManager.MatchAndRemove(Token.TokenType.LPAREN).isPresent()){
            // check for an immediate right parenthesis before starting loop
            if (tokenManager.MatchAndRemove(Token.TokenType.RPAREN).isPresent()){
                return new FunctionNode(function, parameters);
            }
            // while loop to continue until we hit a right parenthesis
            while (true){
                // first check for a String and add to the linkedList if it exists
                Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL);
                if (token.isPresent()){
                    parameters.add(new StringNode(token.get().getValue()));
                }
                else{
                    parameters.add(this.Expression());
                }

                // check that next token is a comma before continuing loop
                if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                    continue;
                }
                else{
                    // if no comma, check if right parenthesis
                    if (tokenManager.MatchAndRemove(Token.TokenType.RPAREN).isPresent()){
                        return new FunctionNode(function, parameters);
                    }
                    else{
                        throw new Exception("Missing COMMA or RPAREN in function call");
                    }
                }
            }
        }
        else{
            throw new Exception("Missing Left Parenthesis in function call");
        }
    }
}