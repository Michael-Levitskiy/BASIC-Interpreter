package ASTNodes;

import java.util.LinkedList;

public class InputNode extends StatementNode{
    
    // Class Instance Variables
    private final LinkedList<Node> inputs;
    private StatementNode next;
    private StatementNode prev;

    /**
     * Constructor
     * @param inputs
     */
    public InputNode(LinkedList<Node> inputs) {
        this.inputs = inputs;
    }

    /**
     * Accessor
     * @return the LinkedList
     */
    public LinkedList<Node> getInputs() {
        return this.inputs;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        String toReturn = "InputNode(\n";

        for (int i = 0; i < this.inputs.size(); i++){
            toReturn += this.inputs.get(i) + "\n";
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