package analyzer.ast;

public class SyntaxTreeBuilder {
    protected int recordIndex = 0;
    public Stack<Node> nodeStack = new Stack<Node>();
    public Node root;
    public SyntaxTreeBuilder() {
        String sqlExpression =
                "DROP TABLE IF EXISTS sk_nodes;" +
                        "CREATE TABLE IF NOT EXISTS sk_nodes (" +
                        "record_number integer PRIMARY KEY," + // the unique record number
                        "node_type varchar(32)," + // the name of the nonterminal
                        "alternative integer," + // the grammar alternative
                        "line_number integer," + // the source line number
                        "col_number integer," + // the source column number
                        "value0 varchar(32)," + // )
                        "value1 varchar(32)," + // ) up to 3 terminal values
                        "value2 varchar(32)," + // )
                        "child0 integer," + // )
                        "child1 integer," + // ) up to 5 children, either
                        "child2 integer," + // ) static or heads of
                        "child3 integer," + // ) iteration lists
                        "child4 integer);"; // )
        try {
            dbmgr.update(sqlExpression);
        } catch (SQLException sqle) {
            log.append("SyntaxTreeBuilder(1): " + sqle.getMessage() + "\n");
        }
    }
    public Node getRootNode() { return root; }
    public void pushNode(Node n) {
        if (nodeStack.size() == 0) root = n; // preserve the root node.
        nodeStack.push(n);
    }
    public Node popNode() {
        if (nodeStack.isEmpty()) {
            System.err.println("SyntaxTreeBuilder(1): Stack is empty!");
            return null;
        }
        return nodeStack.pop();
    }
    public void openNodeScope(Node n) {
        n.setRecordNumber(++recordIndex);
        n.setNodeName(n.getClass().getSimpleName());
        pushNode(n);
    }
    public void closeNodeScope() {
        Node n = popNode();
        Node parent = null;
        if (!nodeStack.isEmpty()) parent = nodeStack.peek();
        n.setParent(parent);
        if (parent != null) parent.addChild(n);
    }
    public void startIteration(Object nodeClass, int iterationNumber) {
        Node tos = nodeStack.peek();
        int noOfChildren = tos.children.size();
        int noOfCorrespondingChild =
                thereIsAChildOfThisType(tos, nodeClass, iterationNumber);
        if (noOfChildren > 0 && noOfCorrespondingChild >= 0) {
            Node lastChild = tos.children.get(noOfCorrespondingChild);
            Node previous = nodeStack.set(nodeStack.size()-1, lastChild);
        } else {
        }
    }
    private int thereIsAChildOfThisType(Node node, Object nodeClass, int iterationNumber) {
        int found = -1; // assume no child of the correct type
        if (node.children != null && node.children.size() > 0) {
            for (int i = 0; i < node.children.size(); i++) {
                if (node.children.get(i).getClass() == nodeClass) {
                    if (iterationNumber > 1) {
                        iterationNumber--;
                    } else {
                        found = i;
                    }
                }
            }
        }
        return found;
    }
}