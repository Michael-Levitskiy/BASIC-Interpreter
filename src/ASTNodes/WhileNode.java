package ASTNodes;

public class WhileNode extends StatementNode{

    // Class Instance Variables
    private final BooleanExpNode booleanExp;
    private final String labelName;
    private StatementNode next;
    private StatementNode prev;
    
    /**
     * Constructor
     * @param booleanExp
     * @param labelName
     */
    public WhileNode(BooleanExpNode booleanExp, String labelName) {
        this.booleanExp = booleanExp;
        this.labelName = labelName;
    }

    ///////////////
    // Accessors //
    ///////////////
    public BooleanExpNode getBooleanExp() {
        return booleanExp;
    }

    public String getLabelName() {
        return labelName;
    }
    
    /**
     * Overridden toStringMethod
     */
    @Override
    public String toString() {
        return "WhileNode(" + booleanExp + " , " + labelName + ")";
    }

    @Override
    public StatementNode getNext() {
        return next;
    }

    @Override
    public void setNext(StatementNode next) {
        this.next = next;
    }

    @Override
    public StatementNode getPrev() {
        return prev;
    }

    @Override
    public void setPrev(StatementNode prev) {
        this.prev = prev;
    }
}