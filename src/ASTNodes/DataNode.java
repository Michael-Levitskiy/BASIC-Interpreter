package ASTNodes;

import java.util.LinkedList;

public class DataNode extends StatementNode{
    
    // Class Instance Variables
    private final LinkedList<Node> variables;
    private StatementNode next;
    private StatementNode prev;

    /**
     * Constructor
     * @param variables
     */
    public DataNode(LinkedList<Node> variables) {
        this.variables = variables;
    }

    /**
     * Accessor
     * @return the LinkedList
     */
    public LinkedList<Node> getVariables() {
        return this.variables;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        String toReturn = "DataNode(\n";

        for (int i = 0; i < this.variables.size(); i++){
            toReturn += this.variables.get(i) + "\n";
        }
        toReturn += ")";
        return toReturn;
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