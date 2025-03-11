package ASTNodes;

public class ForNode extends StatementNode {
    
    // Class Instance Variables
    private final VariableNode variable;
    private final int startValue;
    private final int endValue;
    private final int increment;
    private StatementNode next;
    private StatementNode prev;
    
    /**
     * Constructor given the increment value
     * @param variable
     * @param startValue
     * @param endValue
     * @param increment
     */
    public ForNode(VariableNode variable, int startValue, int endValue, int increment) {
        this.variable = variable;
        this.startValue = startValue;
        this.endValue = endValue;
        this.increment = increment;
    }

    /**
     * Constructor not given increment
     * default increment to 1
     * @param variable
     * @param startValue
     * @param endValue
     */
    public ForNode(VariableNode variable, int startValue, int endValue) {
        this.variable = variable;
        this.startValue = startValue;
        this.endValue = endValue;
        this.increment = 1;
    }

    ///////////////
    // Accessors //
    ///////////////
    public VariableNode getVariable(){
        return variable;
    }

    public int getStartValue() {
        return startValue;
    }

    public int getEndValue() {
        return endValue;
    }
    
    public int getIncrement() {
        return increment;
    }
    
    /**
     * Overridden toString Method
     */
    @Override
    public String toString() {
        return "ForNode(" + variable + " = " + startValue + " -> " + endValue + ", by " + increment + ")";
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