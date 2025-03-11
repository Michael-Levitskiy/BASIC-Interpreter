package ASTNodes;

public abstract class StatementNode extends Node{

    public abstract StatementNode getPrev();
    public abstract void setPrev(StatementNode prev);
    public abstract StatementNode getNext();
    public abstract void setNext(StatementNode next);
    @Override
    public abstract String toString();
}