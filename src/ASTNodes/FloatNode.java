package ASTNodes;

public class FloatNode extends Node {
    
    // Class Instance Variable
    private final float number;

    /**
     * Constructor
     * @param number
     */
    public FloatNode(float number) {
        this.number = number;
    }

    /**
     * Accessor
     * @return the number (float)
     */
    public float getNumber() {
        return this.number;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        return Float.toString(this.number);
    }   
}