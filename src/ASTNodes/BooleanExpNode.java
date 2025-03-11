package ASTNodes;

public class BooleanExpNode extends Node {

    public enum Operation{GreaterThan, GreaterThanEqual, LessThan, LessThanEqual, NotEqual, Equals}

    // Class Instance Variables
    private final Operation op;
    private final Node left;
    private final Node right;
    
    /**
     * Constructor
     * @param op
     * @param left
     * @param right
     */
    public BooleanExpNode(BooleanExpNode.Operation op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    ///////////////
    // Accessors //
    ///////////////
    public Operation getOp() {
        return op;
    }

    public Node getLeft() {
        return left;
    }
    
    public Node getRight() {
        return right;
    }
    
    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "BooleanExpNode(" + left + " , " + op + " , " + right + ")";
    }

    
}