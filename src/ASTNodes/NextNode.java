package ASTNodes;

public class NextNode extends StatementNode {
    
    // Class Instance Variable
    private final VariableNode variable;
    private StatementNode next;
    private StatementNode prev;

    /**
     * Constructor
     * @param variable
     */
    public NextNode(VariableNode variable) {
        this.variable = variable;
    }

    /**
     * Accessor
     */
    public VariableNode getVariable() {
        return variable;
    }

    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "NextNode(" + variable + ")";
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