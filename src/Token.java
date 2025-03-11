public class Token {

    enum TokenType {WORD, NUMBER, ENDOFLINE, PRINT, READ, INPUT, DATA, 
        GOSUB, GOTO, FOR, TO, STEP, NEXT, RETURN, IF, THEN, FUNCTION, WHILE, END, STRINGLITERAL,
        LESSTHANEQUAL, GREATERTHANEQUAL, NOTEQUAL, EQUALS, LESSTHAN, GREATERTHAN, LPAREN,
        RPAREN, PLUS, MINUS, MULTIPLY, DIVIDE, LABEL, COMMA,
        RANDOM, LEFT$, RIGHT$, MID$, NUM$, VAL, VALPERCENT}

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private TokenType tokenType;
    private String value = new String();
    private int lineNumber;
    private int position;
    

    //////////////////
    // Constructors //
    //////////////////
    /**
     * Constructor given every variable instance
     * @param tokenType
     * @param value
     * @param lineNumber
     * @param position
     */
    public Token(TokenType tokenType, String value, int lineNumber, int position) {
        this.tokenType = tokenType;
        this.value = value;
        this.lineNumber = lineNumber;
        this.position = position;
    }

    /**
     * Constructor given every variable instance except 'value'
     * @param tokenType
     * @param lineNumber
     * @param position
     */
    public Token(TokenType tokenType, int lineNumber, int position) {
        this.tokenType = tokenType;
        this.lineNumber = lineNumber;
        this.position = position;
    }
    

    ///////////////
    // Accessors //
    ///////////////
    /**
     * @return the TokenType
     */
    public TokenType getType(){
        return this.tokenType;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Overridden method to return class variable as a string
     */
    @Override
    public String toString() {
        return this.tokenType + "(" + this.value + ") " + this.lineNumber + "-" + this.position;
    }
}