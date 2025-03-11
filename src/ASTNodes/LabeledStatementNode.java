package ASTNodes;

public class LabeledStatementNode extends StatementNode {
    
    // Class Instance Variables
    private final String labelName;
    private StatementNode next;
    private StatementNode prev;
    
    /**
     * Constructor
     * @param labelName
     * @param statementNode
     */
    public LabeledStatementNode(String labelName) {
        this.labelName = labelName;
    }

    ///////////////
    // Accessors //
    ///////////////
    public String getLabelName() {
        return labelName;
    }

    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "LabeledStatementNode(" + labelName + ")";
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