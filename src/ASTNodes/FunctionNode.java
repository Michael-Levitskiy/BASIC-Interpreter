package ASTNodes;

import java.util.LinkedList;

public class FunctionNode extends StatementNode{
    
    public enum Function{RANDOM, LEFT$, RIGHT$, MID$, NUM$, VAL, VALPERCENT}

    // Class Instance Variables
    private final Function function;
    private final LinkedList<Node> parameters;
    private StatementNode next;
    private StatementNode prev;
    
    /**
     * Constructor
     * @param function
     * @param parameters
     */
    public FunctionNode(FunctionNode.Function function, LinkedList<Node> parameters) {
        this.function = function;
        this.parameters = parameters;
    }

    ///////////////
    // Accessors //
    ///////////////
    public Function getFunction() {
        return function;
    }

    public LinkedList<Node> getParameters() {
        return parameters;
    }

    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "FunctionNode(" + function + "(" + parameters + "))";
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