package ASTNodes;

public class MathOpNode extends Node{

    public enum Operation{add, subtract, multiply, divide}

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final Operation op;
    private final Node left;
    private final Node right;
    

    /**
     * Constructor
     * @param op
     * @param left
     * @param right
     */
    public MathOpNode(MathOpNode.Operation op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }


    ///////////////
    // Accessors //
    ///////////////
    public Operation getOp() {
        return this.op;
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }


    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        return "MathOpNode(" + this.left + " , " + this.op + " , " + this.right + ")";
    }
}