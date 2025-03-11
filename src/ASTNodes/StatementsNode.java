package ASTNodes;

import java.util.LinkedList;

public class StatementsNode extends Node{
    
    // Class Instance Variable
    private final LinkedList<StatementNode> statements;

    /**
     * Constructor, given the LinkedList of StatementNode's
     * @param statements
     */
    public StatementsNode(LinkedList<StatementNode> statements) {
        this.statements = statements;
    }

    /**
     * Accessor
     * @return
     */
    public LinkedList<StatementNode> getStatements() {
        return statements;
    }

    /**
     * Overridden toString() method
     */
    @Override
    public String toString() {
        String toReturn = "StatementsNode(\n";

        for (int i = 0; i < this.statements.size(); i++){
            toReturn += this.statements.get(i) + "\n";
        }
        toReturn += ")";
        return toReturn;
    }
}