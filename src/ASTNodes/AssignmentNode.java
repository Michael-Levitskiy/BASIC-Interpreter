package ASTNodes;

public class AssignmentNode extends StatementNode{
    
    // Class Instance Variables
    private final VariableNode variable;
    private final Node value;
    private StatementNode next;
    private StatementNode prev;
    

    /**
     * Constructor
     * @param variable
     * @param value
     */
    public AssignmentNode(VariableNode variable, Node value) {
        this.variable = variable;
        this.value = value;
    }


    ///////////////
    // Accessors //
    ///////////////
    /**
     * @return the VariableNode
     */
    public VariableNode getVariable() {
        return this.variable;
    }

    /**
     * @return the Node containing the value
     */
    public Node getValue() {
        return this.value;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        return "AssignmentNode(" + variable + " = " + value + ")";
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