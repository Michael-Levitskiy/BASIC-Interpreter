import java.util.HashMap;
import java.util.LinkedList;


public class Lexer {

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private CodeHandler codeHandler;
    private int lineNumber = 1;
    private int position = 0;
    private HashMap<String, Token.TokenType> knownWords;
    private HashMap<String, Token.TokenType> oneCharSymbols;
    private HashMap<String, Token.TokenType> twoCharSymbols;


    /////////////////
    // Constructor //
    /////////////////
    /**
     * @param fileName (String) to be passed into the CodeHandler variable
     */
    public Lexer(String fileName) {
        this.codeHandler = new CodeHandler(fileName);
        this.fillHashMaps();
    }


    ///////////////////
    // Public Method //
    ///////////////////
    /**
     * Given a file name, use code handler to break down the contents of the file
     *      into separate tokens and store the tokens in a linked list
     * @return a LinkedList of Tokens made from the file
     * @throws Exception 
     */
    public LinkedList<Token> lex() throws Exception{
        LinkedList<Token> toReturn = new LinkedList<>();
        
        while(!this.codeHandler.IsDone()){
            char currentChar = this.codeHandler.Peek(0);
            
            // If space or tab, move past it and increment position
            if(currentChar == ' ' || currentChar == '\t'){
                this.codeHandler.Swallow(1);
                this.position++;
            }
            // If linefeed, create new EndOfLine Token, add to Linked list, increment lineNumber as set position to 0
            else if(currentChar == '\n'){
                toReturn.add(new Token(Token.TokenType.ENDOFLINE, this.lineNumber, this.position));
                this.lineNumber++;
                this.position = 0;
                this.codeHandler.Swallow(1);
            }
            // If carriage return, ignore it
            else if(currentChar == '\r'){
                this.codeHandler.Swallow(1);
            }
            // If quote, create a string token
            else if(currentChar == '"'){
                toReturn.add(this.processStringLiteral());
            }
            // If letter, call helper method to create token and add to LinkedList
            else if(Character.isLetter(currentChar)){
                toReturn.add(this.processWord());
            }
            // If digit, call helper method to create token and add to LinkedList
            else if(Character.isDigit(currentChar)){
                toReturn.add(this.processNumber());
            }
            // Use peekString() to check if char is the start of a 2 char string
            else if (twoCharSymbols.containsKey(this.codeHandler.PeekString(2))){
                    toReturn.add(new Token(twoCharSymbols.get(this.codeHandler.PeekString(2)), this.lineNumber, this.position));
                    this.position += 2;
                    this.codeHandler.Swallow(2);
                }
            // If char is in oneCharSymbol hashmap, create and add token
            else if(oneCharSymbols.containsKey(currentChar+"")){
                toReturn.add(new Token(oneCharSymbols.get(currentChar+""), this.lineNumber, this.position));
                this.position++;
                this.codeHandler.Swallow(1);
            }
            // Throw an exception if the char is not listed above
            else{
                throw new Exception("Unknown Character on Line: " + this.lineNumber + ", Position: " + this.position);
            }
        }
        // If the file doesn't end with a ENDOFLINE token, add token
        if (toReturn.getLast().getType() != Token.TokenType.ENDOFLINE){
            toReturn.add(new Token(Token.TokenType.ENDOFLINE, this.lineNumber, this.position));
        }
        return toReturn;
    }


    /////////////////////
    // Private Methods //
    /////////////////////
    /**
     * Create a WORD token
     * @return the Token
     */
    private Token processWord(){
        String value = "" + this.codeHandler.GetChar();     // initialize value of the token
        int startPosition = this.position++;                // hold the value of the starting position, post-increment to preserve position
        char currentChar = this.codeHandler.Peek(0);      // get the current char  

        // Loop continues as long as the currentChar is a letter, digit, '_', '$', or '%'
        while(Character.isLetter(currentChar) || Character.isDigit(currentChar) ||
                currentChar == '_' || currentChar == '$' || currentChar == '%' || currentChar == ':'){
            
            value += this.codeHandler.GetChar();
            this.position++;

            // if the char is '$' or '%', that is the end of the word
            if (currentChar == '$' || currentChar == '%' || currentChar == ':'){break;}
            
            // check if file ended before peeking another char
            if (this.codeHandler.IsDone()){break;}
            currentChar = this.codeHandler.Peek(0);
        }
        // check if value is a known word or if it's a label
        if (knownWords.containsKey(value)){
            return new Token(knownWords.get(value), this.lineNumber, startPosition);
        }
        else if (value.charAt(this.position-startPosition-1) == ':'){
            return new Token(Token.TokenType.LABEL, value, this.lineNumber, startPosition);
        }
        else{
            return new Token(Token.TokenType.WORD, value, this.lineNumber, startPosition);
        }
    }

    /**
     * Create a NUMBER token
     * @return the Token
     */
    private Token processNumber(){
        String value = "" + codeHandler.GetChar();  // initialize value of the token
        int startPosition = this.position++;        // store the value of the starting position, post-increment to preserve position
        int decimalCount = 0;                       // ensure a maximum of 1 decimal exists
        char currentChar = codeHandler.Peek(0);   // get the current char

        // loop continues as long as the char is a digit or decimal
        while(Character.isDigit(currentChar) || currentChar == '.'){
            // if char is the second decimal, break
            if (currentChar == '.' && decimalCount == 1){break;}

            value += this.codeHandler.GetChar();
            this.position++;

            // if decimal, increment decimal count
            if (currentChar == '.'){decimalCount++;}

            // check if the file ended before peeking another char
            if (this.codeHandler.IsDone()){break;}
            currentChar = codeHandler.Peek(0);
        }
        // create token
        Token toReturn = new Token(Token.TokenType.NUMBER, value, this.lineNumber, startPosition);
        return toReturn;
    }

    /**
     * Private helper method to process a string Token
     * @return the Token
     */
    private Token processStringLiteral(){
        String value = "";                              // initialize value with starting '"'
        this.codeHandler.Swallow(1);                  // move past the '"' in codeHandler
        int startPosition = this.position++;            // store value of the starting position, post-increment to preserve position
        int startLineNumber = this.lineNumber;          // store value of the starting line number in case linefeed exists
        char currentChar = this.codeHandler.Peek(0);  // get the current char

        // loop until I hit another quotation
        while(currentChar != '"'){
            // if I come across a '\', check if '"' follows and don't end the string literal
            if (currentChar == '\\'){
                String nextPair = this.codeHandler.PeekString(2);
                if (nextPair.equals("\\\"")){
                    value += "\"";
                    this.position += 2;
                    this.codeHandler.Swallow(2);
                    currentChar = this.codeHandler.Peek(0);
                    continue;
                }
            }
            value += this.codeHandler.GetChar();
            // if the char was a linefeed, increment linenumber and set position to 0
            if (currentChar == '\n'){
                this.lineNumber++;
                this.position = 0;
            }
            else{   // otherwise, increment position
                this.position++;
            }
            currentChar = this.codeHandler.Peek(0); // peek the next char
        }
        // increment position and swallow to account for the last '"' we came across
        this.position++;
        this.codeHandler.Swallow(1);

        return new Token(Token.TokenType.STRINGLITERAL, value, startLineNumber, startPosition);
    }

    /**
     * Private method to fill the hash map of known words
     */
    private void fillHashMaps(){
        // create and fill knownWords
        this.knownWords = new HashMap<>();
        this.knownWords.put("PRINT", Token.TokenType.PRINT);
        this.knownWords.put("READ", Token.TokenType.READ);
        this.knownWords.put("INPUT", Token.TokenType.INPUT);
        this.knownWords.put("DATA", Token.TokenType.DATA);
        this.knownWords.put("GOSUB", Token.TokenType.GOSUB);
        this.knownWords.put("GOTO", Token.TokenType.GOTO);
        this.knownWords.put("FOR", Token.TokenType.FOR);
        this.knownWords.put("TO", Token.TokenType.TO);
        this.knownWords.put("STEP", Token.TokenType.STEP);
        this.knownWords.put("NEXT", Token.TokenType.NEXT);
        this.knownWords.put("RETURN", Token.TokenType.RETURN);
        this.knownWords.put("IF", Token.TokenType.IF);
        this.knownWords.put("THEN", Token.TokenType.THEN);
        this.knownWords.put("FUNCTION", Token.TokenType.FUNCTION);
        this.knownWords.put("WHILE", Token.TokenType.WHILE);
        this.knownWords.put("END", Token.TokenType.END);
        this.knownWords.put("RANDOM", Token.TokenType.RANDOM);
        this.knownWords.put("LEFT$", Token.TokenType.LEFT$);
        this.knownWords.put("RIGHT$", Token.TokenType.RIGHT$);
        this.knownWords.put("MID$", Token.TokenType.MID$);
        this.knownWords.put("NUM$", Token.TokenType.NUM$);
        this.knownWords.put("VAL", Token.TokenType.VAL);
        this.knownWords.put("VAL%", Token.TokenType.VALPERCENT);

        // create and fill oneCharSymbols
        this.oneCharSymbols = new HashMap<>();
        this.oneCharSymbols.put("=", Token.TokenType.EQUALS);
        this.oneCharSymbols.put("<", Token.TokenType.LESSTHAN);
        this.oneCharSymbols.put(">", Token.TokenType.GREATERTHAN);
        this.oneCharSymbols.put("(", Token.TokenType.LPAREN);
        this.oneCharSymbols.put(")", Token.TokenType.RPAREN);
        this.oneCharSymbols.put("+", Token.TokenType.PLUS);
        this.oneCharSymbols.put("-", Token.TokenType.MINUS);
        this.oneCharSymbols.put("*", Token.TokenType.MULTIPLY);
        this.oneCharSymbols.put("/", Token.TokenType.DIVIDE);
        this.oneCharSymbols.put(",", Token.TokenType.COMMA);

        // create and fill twoCharSymbols
        this.twoCharSymbols = new HashMap<>();
        this.twoCharSymbols.put("<=", Token.TokenType.LESSTHANEQUAL);
        this.twoCharSymbols.put(">=", Token.TokenType.GREATERTHANEQUAL);
        this.twoCharSymbols.put("<>", Token.TokenType.NOTEQUAL);
    }
}