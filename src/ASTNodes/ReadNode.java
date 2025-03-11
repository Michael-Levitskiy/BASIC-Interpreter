package ASTNodes;

import java.util.LinkedList;

public class ReadNode extends StatementNode{

    // Class Instance Variables
    private final LinkedList<VariableNode> variables;
    private StatementNode next;
    private StatementNode prev;

    /**
     * Constructor
     * @param variables
     */
    public ReadNode(LinkedList<VariableNode> variables) {
        this.variables = variables;
    }

    /**
     * Accessor
     * @return the LinkedList
     */
    public LinkedList<VariableNode> getVariables() {
        return this.variables;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        String toReturn = "ReadNode(\n";

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