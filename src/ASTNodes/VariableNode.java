package ASTNodes;

public class VariableNode extends Node{
    
    // Class Instance Variable
    private final String variableName;

    /**
     * Constructor
     * @param variableName
     */
    public VariableNode(String variableName) {
        this.variableName = variableName;
    }

    /**
     * Accessor
     * @return the variable name
     */
    public String getVariableName() {
        return this.variableName;
    }

    /**
     * Overridden toString method
     */
    @Override
    public String toString() {
        return this.variableName;
    }
}