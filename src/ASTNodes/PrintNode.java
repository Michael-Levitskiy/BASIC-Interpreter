package ASTNodes;

import java.util.LinkedList;

public class PrintNode extends StatementNode{
    
    // Class Instance Variable
    private final LinkedList<Node> print;
    private StatementNode next;
    private StatementNode prev;

    /**
     * Constructor given the list of Node's
     * @param print
     */
    public PrintNode(LinkedList<Node> print) {
        this.print = print;
    }

    /**
     * Accessor
     * @return
     */
    public LinkedList<Node> getPrint() {
        return this.print;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        String toReturn = "PrintNode(\n";

        for (int i = 0; i < this.print.size(); i++){
            toReturn += this.print.get(i) + "\n";
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