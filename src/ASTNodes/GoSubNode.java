package ASTNodes;

public class GoSubNode extends StatementNode {
    
    // Class Instance Variable
    private final String identifier;
    private StatementNode next;
    private StatementNode prev;
    
    /**
     * Constructor
     */
    public GoSubNode(String identifier){
        this.identifier = identifier;
    }

    /**
     * Accessor
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "GoSubNode(" + this.identifier + ")";
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