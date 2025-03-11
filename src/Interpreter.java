import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import ASTNodes.*;
import ASTNodes.FunctionNode.Function;

public class Interpreter{

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final StatementsNode program;
    private LinkedList<Node> data = new LinkedList<>();
    private HashMap<String, LabeledStatementNode> labels = new HashMap<>();
    private HashMap<String, Integer> intVars = new HashMap<>();
    private HashMap<String, Float> floatVars = new HashMap<>();
    private HashMap<String, String> stringVars = new HashMap<>();
    private Boolean contLoop = true;
    private StatementNode currentStatement;
    private StatementNode nextStatement;
    private Stack<StatementNode> stack = new Stack<>();


    /**
     * Constructor
     * @param program (StatementsNode)
     */
    public Interpreter(StatementsNode program){
        this.program = program;
        this.fillData();
        this.fillLabels();
        this.createLinkedList();
        currentStatement = this.program.getStatements().getFirst();
    }


    ///////////////
    // Accessors //
    ///////////////
    public LinkedList<Node> getData() {
        return data;
    }

    public HashMap<String, LabeledStatementNode> getLabels() {
        return labels;
    }


    /**
     * Public method to interpret and execute the StatementsNode
     * @throws Exception
     */
    public void interpret() throws Exception{
        while(contLoop){
            nextStatement = currentStatement.getNext();
            interpret(currentStatement);
            currentStatement = nextStatement;
        }
    }


    /////////////////////
    // Private Methods //
    /////////////////////
    /**
     * 
     * @param statementNode
     * @throws Exception 
     */
    private void interpret(StatementNode statementNode) throws Exception{
        if (statementNode instanceof ReadNode){
            // get list of VariableNodes to read into and iterate
            LinkedList<VariableNode> variables = ((ReadNode)statementNode).getVariables();
            for (VariableNode variable : variables){
                // get the variable name and remove node from data list
                String variableName = variable.getVariableName();
                Node data = this.data.removeFirst();
                // determine type of variable name and add data to it accordingly
                if (variableName.endsWith("$")){
                    if (!(data instanceof StringNode)){throw new Exception("Tried to read non-string into a string variable");}
                    stringVars.put(variableName, ((StringNode)data).getString());
                }
                else if (variableName.endsWith("%")){
                    if (!(data instanceof FloatNode)){throw new Exception("Tried to read non-float into a float variable");}
                    floatVars.put(variableName, ((FloatNode)data).getNumber());
                }
                else{
                    if (!(data instanceof IntegerNode)){throw new Exception("Tried to read non-integer into an integer variable");}
                    intVars.put(variableName, ((IntegerNode)data).getNumber());
                }
            }
        }
        else if (statementNode instanceof AssignmentNode){
            // get the variable name (left) and expression (right)
            String variableName = ((AssignmentNode)statementNode).getVariable().getVariableName();
            Node expression = ((AssignmentNode)statementNode).getValue();
            // if VariableNode, get value and assign to this variable
            if (expression instanceof VariableNode){
                VariableNode variable = (VariableNode)expression;
                if (stringVars.containsKey(variable.getVariableName())){
                    if (!variableName.endsWith("$")){throw new Exception("Tried to assign string into a non-string variable");}
                    String string = stringVars.get(variable.getVariableName());
                    stringVars.put(variableName, string);
                }
                else if (floatVars.containsKey(variable.getVariableName())){
                    if (!variableName.endsWith("%")){throw new Exception("Tried to assign float into a non-float variable");}
                    float num = floatVars.get(variable.getVariableName());
                    floatVars.put(variableName, num);
                }
                else if (intVars.containsKey(variable.getVariableName())){
                    if (variableName.endsWith("%") || variableName.endsWith("$")){throw new Exception("Tried to assign int into a non-int variable");}
                    int num = intVars.get(variable.getVariableName());
                    intVars.put(variableName, num);
                }
                else{throw new Exception("Unknown Variable in Assignment");}
            }
            // if integer, assign
            if (expression instanceof IntegerNode){
                intVars.put(variableName, ((IntegerNode)expression).getNumber());
            }
            if (expression instanceof FloatNode){
                floatVars.put(variableName, ((FloatNode)expression).getNumber());
            }
            if (expression instanceof MathOpNode){
                java.lang.Integer intNum = this.evaluateInt(expression);
                java.lang.Float floatNum = this.evaluateFloat(expression);
                if(intNum == null && floatNum == null){
                    throw new Exception("Invalid Math operation in assignment");
                }
                else if(intNum == null){
                    floatVars.put(variableName, floatNum);
                }
                else{
                    intVars.put(variableName, intNum);
                }
            }
        }
        else if (statementNode instanceof InputNode){
            // get list of input
            LinkedList<Node> list = ((InputNode)statementNode).getInputs();
            // if first Node is a String, print it
            if (list.getFirst() instanceof StringNode){
                String toPrint = ((StringNode)list.removeFirst()).getString();
                System.out.println(toPrint);
            }
            // for every node (VariableNode), check name and get appropriate input
            for (Node node : list){
                Scanner scn = new Scanner(System.in);
                VariableNode variableNode = (VariableNode)node;
                String varName = variableNode.getVariableName();
                // determine type of variable name and add data to it accordingly
                if (varName.endsWith("$")){
                    String input = scn.next();
                    stringVars.put(varName, input);
                }
                else if (varName.endsWith("%")){
                    float input = scn.nextFloat();
                    floatVars.put(varName, input);
                }
                else{
                    int input = scn.nextInt();
                    intVars.put(varName, input);
                }
                scn.close();
            }
        }
        else if (statementNode instanceof PrintNode){
            // get the list of nodes to print and iterate through the list
            LinkedList<Node> list = ((PrintNode)statementNode).getPrint();
            for (Node node : list){
                // if StringNode, print the string
                if (node instanceof StringNode){
                    System.out.print(((StringNode)node).getString());
                }
                // if VariableNode, get the value and print it
                else if(node instanceof VariableNode){
                    String varName = ((VariableNode)node).getVariableName();
                    if (stringVars.containsKey(varName)){
                        String string = stringVars.get(varName);
                        System.out.print(string);
                    }
                    else if (floatVars.containsKey(varName)){
                        float num = floatVars.get(varName);
                        System.out.print(num);
                    }
                    else if (intVars.containsKey(varName)){
                        int num = intVars.get(varName);
                        System.out.print(num);
                    }
                    else{throw new Exception("Unknown Variable in Assignment");}
                }
                // if MathOpNode, evaluate it and print it
                else if (node instanceof MathOpNode){
                    java.lang.Integer intNum = this.evaluateInt(node);
                    java.lang.Float floatNum = this.evaluateFloat(node);
                    if(intNum == null && floatNum == null){
                        throw new Exception("Invalid Math operation in PRINT");
                    }
                    else if(intNum == null){
                        System.out.print(floatNum);
                    }
                    else{
                        System.out.print(floatNum);
                    }
                }
            }
            System.out.println();
        }
        else if (statementNode instanceof IfNode){
            if (this.evaluateBool(((IfNode)statementNode).getBooleanExp())){
                LabeledStatementNode label = this.labels.get(((IfNode)statementNode).getLabel());
                this.currentStatement = label;
            }
        }
        else if (statementNode instanceof GoSubNode){
            this.stack.push(statementNode.getNext());
            nextStatement = this.labels.get(((GoSubNode)statementNode).getIdentifier());
        }
        else if (statementNode instanceof GOTONode){
            nextStatement = this.labels.get(((GOTONode)statementNode).getIdentifier());
        }
        else if (statementNode instanceof ReturnNode || statementNode instanceof NextNode){
            nextStatement = this.stack.pop();
        }
        else if (statementNode instanceof ForNode){
            Integer var = this.intVars.get(((ForNode)statementNode).getVariable().getVariableName());
            if (var == null){
                this.intVars.put(((ForNode)statementNode).getVariable().getVariableName(), ((ForNode)statementNode).getStartValue());
                this.stack.push(statementNode);
            }
            else{
                var += ((ForNode)statementNode).getIncrement();
                this.intVars.put(((ForNode)statementNode).getVariable().getVariableName(), var);
                if (var >= ((ForNode)statementNode).getEndValue()){
                    do{
                        statementNode = statementNode.getNext();
                    }while(!(statementNode instanceof NextNode));
                    this.currentStatement = statementNode;
                    this.nextStatement = this.currentStatement.getNext();
                }
                else{
                    this.stack.push(statementNode);
                }
            }
        }
        else if (statementNode instanceof FunctionNode){
            FunctionNode.Function function = ((FunctionNode)statementNode).getFunction();
            
            if(function == Function.RANDOM){}
            
        }
        else if (statementNode instanceof EndNode){
            this.contLoop = false;
        }
    }
    

    /**
     * Given a node, evaluate it to return an int
     * Return null if can't return an int
     */
    private java.lang.Integer evaluateInt(Node node){
        if (node instanceof VariableNode){
            if (intVars.containsKey(((VariableNode)node).getVariableName())){
                return intVars.get(((VariableNode)node).getVariableName());
            }
            else{
                return null;
            }
        }
        else if (node instanceof IntegerNode){
            return ((IntegerNode)node).getNumber();
        }
        else if (node instanceof MathOpNode){
            MathOpNode.Operation op = ((MathOpNode)node).getOp();
            java.lang.Integer left = evaluateInt(((MathOpNode)node).getLeft());
            java.lang.Integer right = evaluateInt(((MathOpNode)node).getRight());
            
            if (left == null || right == null){return null;}
            
            if (op == MathOpNode.Operation.add){return left + right;}
            else if (op == MathOpNode.Operation.subtract){return left - right;}
            else if (op == MathOpNode.Operation.multiply){return left * right;}
            else{return left / right;}

        }
        return null;
    }

    /**
     * Given a node, evaluate it to return a float
     * Return null if can't return a float
     */
    private java.lang.Float evaluateFloat(Node node){
        if (node instanceof VariableNode){
            if (intVars.containsKey(((VariableNode)node).getVariableName())){
                return floatVars.get(((VariableNode)node).getVariableName());
            }
            else{
                return null;
            }
        }
        else if (node instanceof FloatNode){
            return ((FloatNode)node).getNumber();
        }
        else if (node instanceof MathOpNode){
            MathOpNode.Operation op = ((MathOpNode)node).getOp();
            java.lang.Float left = evaluateFloat(((MathOpNode)node).getLeft());
            java.lang.Float right = evaluateFloat(((MathOpNode)node).getRight());
            
            if (left == null || right == null){return null;}
            
            if (op == MathOpNode.Operation.add){return left + right;}
            else if (op == MathOpNode.Operation.subtract){return left - right;}
            else if (op == MathOpNode.Operation.multiply){return left * right;}
            else{return left / right;}

        }
        return null;
    }

    /**
     * Given a BooleanExpNode, return whether the expression is true or false
     * @param node
     * @return true or false
     * @throws Exception 
     */
    private Boolean evaluateBool(BooleanExpNode node) throws Exception{
        // get the boolean operation
        BooleanExpNode.Operation op = node.getOp();
        // evaluate the left and right side
        java.lang.Integer leftIntNum = this.evaluateInt(node.getLeft());
        java.lang.Float leftFloatNum = this.evaluateFloat(node.getLeft());
        java.lang.Integer rightIntNum = this.evaluateInt(node.getRight());
        java.lang.Float rightFloatNum = this.evaluateFloat(node.getRight());

        if(leftIntNum == null && leftFloatNum == null && rightFloatNum == null && rightIntNum == null){
            throw new Exception("Invalid Math operation in PRINT");
        }
        else if(leftIntNum == null){
            if(rightIntNum == null){
                if(op == BooleanExpNode.Operation.Equals){return leftFloatNum == rightFloatNum;}
                else if(op == BooleanExpNode.Operation.GreaterThan){return leftFloatNum > rightFloatNum;}
                else if(op == BooleanExpNode.Operation.GreaterThanEqual){return leftFloatNum >= rightFloatNum;}
                else if(op == BooleanExpNode.Operation.LessThan){return leftFloatNum < rightFloatNum;}
                else if(op == BooleanExpNode.Operation.LessThanEqual){return leftFloatNum <= rightFloatNum;}
                else if(op == BooleanExpNode.Operation.NotEqual){return leftFloatNum != rightFloatNum;}
                else{
                    throw new Exception("Invalid Boolean Operation in BooleanExpNode");
                }
            }
            else{
                if(op == BooleanExpNode.Operation.Equals){throw new Exception("Can't perform Equals between a float and an int");}
                else if(op == BooleanExpNode.Operation.GreaterThan){return leftFloatNum > rightFloatNum;}
                else if(op == BooleanExpNode.Operation.GreaterThanEqual){return leftFloatNum >= rightIntNum;}
                else if(op == BooleanExpNode.Operation.LessThan){return leftFloatNum < rightIntNum;}
                else if(op == BooleanExpNode.Operation.LessThanEqual){return leftFloatNum <= rightIntNum;}
                else if(op == BooleanExpNode.Operation.NotEqual){throw new Exception("Can't perform NotEquals between a float and an int");}
                else{
                    throw new Exception("Invalid Boolean Operation in BooleanExpNode");
                }
            }
        }
        else{
            if(rightIntNum == null){
                if(op == BooleanExpNode.Operation.Equals){throw new Exception("Can't perform Equals between an int and a float");}
                else if(op == BooleanExpNode.Operation.GreaterThan){return leftIntNum > rightFloatNum;}
                else if(op == BooleanExpNode.Operation.GreaterThanEqual){return leftIntNum >= rightFloatNum;}
                else if(op == BooleanExpNode.Operation.LessThan){return leftIntNum < rightFloatNum;}
                else if(op == BooleanExpNode.Operation.LessThanEqual){return leftIntNum <= rightFloatNum;}
                else if(op == BooleanExpNode.Operation.NotEqual){throw new Exception("Can't perform NotEquals between an int and a float");}
                else{
                    throw new Exception("Invalid Boolean Operation in BooleanExpNode");
                }
            }
            else{
                if(op == BooleanExpNode.Operation.Equals){return leftIntNum == rightIntNum;}
                else if(op == BooleanExpNode.Operation.GreaterThan){return leftIntNum > rightIntNum;}
                else if(op == BooleanExpNode.Operation.GreaterThanEqual){return leftIntNum >= rightIntNum;}
                else if(op == BooleanExpNode.Operation.LessThan){return leftIntNum < rightIntNum;}
                else if(op == BooleanExpNode.Operation.LessThanEqual){return leftIntNum <= rightIntNum;}
                else if(op == BooleanExpNode.Operation.NotEqual){return leftIntNum != rightIntNum;}
                else{
                    throw new Exception("Invalid Boolean Operation in BooleanExpNode");
                }
            }
        }
    }


    /**
     * Private method to find all DATA statements
     * then fill LinkedList of Node to hold all DATA inputs
     */
    private void fillData(){
        LinkedList<StatementNode> programList = program.getStatements();
        for (StatementNode statement : programList){
            
            if (statement instanceof DataNode){
                LinkedList<Node> dataList = ((DataNode)statement).getVariables();
                for (Node node : dataList){
                    this.data.add(node);
                }
            }
        }
    }

    /**
     * Private method to find all labels
     * then add to hashmap all of the LabeledStatementNodes
     */
    private void fillLabels(){
        LinkedList<StatementNode> programList = program.getStatements();
        for (StatementNode statement : programList){
            if (statement instanceof LabeledStatementNode){
                String labelName = ((LabeledStatementNode) statement).getLabelName();
                this.labels.put(labelName, (LabeledStatementNode)statement);
            }
        }
    }

    /**
     * Private method to use StatementsNode
     * and create linked list by having each node point to the next node
     */
    private void createLinkedList(){
        LinkedList<StatementNode> statements = program.getStatements();
        StatementNode current = statements.get(0);
        for (int i = 0; i < statements.size(); i++){
            
            if(i == 0){
                current.setPrev(null);
            }
            if(i == statements.size()-1){
                current.setNext(null);
                return;
            }
            StatementNode nextNode = statements.get(i+1);
            current.setNext(nextNode);
            nextNode.setPrev(current);
            current = nextNode;
        }
    }


    /////////////////////////////
    // Public Function Methods //
    /////////////////////////////
    /**
     * @return a random integer
     */
    public int RANDOM(){
        Random rand = new Random();
        return rand.nextInt(Integer.MAX_VALUE);
    }

    /**
     * @param string
     * @param i
     * @return the leftmost i characters from the string
     */
    public String LEFT(String string, int i){
        return string.substring(0, i);
    }

    /**
     * @param string
     * @param i
     * @return the rightmost i characters from the string
     */
    public String RIGHT(String string, int i){
        return string.substring(string.length()-i);
    }

    /**
     * 
     * @param string
     * @param start
     * @param count
     * @return the characters of the string, starting from 'start' and for 'count' characters
     */
    public String MID(String string, int start, int count){
        return string.substring(start, start+count);
    }

    /**
     * @param num
     * @return the int as a String
     */
    public String NUM(int num){
        return Integer.toString(num);
    }

    /**
     * @param num
     * @return the float as a String
     */
    public String NUM(float num){
        return Float.toString(num);
    }

    /**
     * @param string
     * @return the string as an int
     */
    public int VAL(String string){
        return Integer.parseInt(string);
    }

    /**
     * @param string
     * @return the string as a float
     */
    public float VALFloat(String string){
        return Float.parseFloat(string);
    }
}