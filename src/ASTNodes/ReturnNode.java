package ASTNodes;

public class ReturnNode extends StatementNode{
    
    // Class Instance Variable
    private StatementNode next;
    private StatementNode prev;

    /**
     * Null Constructor
     */
    public ReturnNode(){}

    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "ReturnNode()";
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