package ASTNodes;

public class IntegerNode extends Node{

    // Class Instance Variable
    private final int number;

    /**
     * Constructor
     * @param number
     */
    public IntegerNode(int number) {
        this.number = number;
    }

    /**
     * Accessor
     * @return the number (int)
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        return Integer.toString(this.number);
    }
}