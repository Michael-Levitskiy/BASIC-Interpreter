package ASTNodes;

public class StringNode extends Node{

    // Class Instance Variable
    private final String string;

    /**
     * Constructor
     * @param string
     */
    public StringNode(String string){
        this.string = string;
    }

    /**
     * Accessor
     * @return the string
     */
    public String getString() {
        return this.string;
    }

    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return this.string;
    }
}