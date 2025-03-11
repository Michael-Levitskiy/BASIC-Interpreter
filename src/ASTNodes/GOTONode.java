package ASTNodes;

public class GOTONode extends StatementNode{
    
    // Class Instance Variable
    private final String identifier;
    private StatementNode next;
    private StatementNode prev;
    
    /**
     * Constructor
     */
    public GOTONode(String identifier){
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
        return "GOTONode(" + this.identifier + ")";
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
