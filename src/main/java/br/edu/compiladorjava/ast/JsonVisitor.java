package br.edu.compiladorjava.ast;

public class JsonVisitor implements AstVisitor<String> {
    private StringBuilder json;

    public String toJson(AstNode node) {
        json = new StringBuilder();
        json.append(node.accept(this));
        return json.toString();
    }

    @Override
    public String visitProgram(ProgramNode node) {
        return "{\"type\":\"Program\",\"name\":\"" + node.getName() +
                "\",\"corpo\":" + node.getCorpo().accept(this) + "}";
    }

    @Override
    public String visitCorpo(CorpoNode node) {
        return "{\"type\":\"Corpo\",\"declarations\":" +
                (node.getDeclarations() != null ? node.getDeclarations().accept(this) : "null") +
                ",\"commands\":" + node.getCommandBlock().accept(this) + "}";
    }

    @Override
    public String visitDeclarations(DeclarationsNode node) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < node.getDeclarations().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(node.getDeclarations().get(i).accept(this));
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String visitVariableDecl(VariableDeclNode node) {
        return "{\"type\":\"VarDecl\",\"name\":\"" + node.getName() +
                "\",\"varType\":\"" + node.getType() + "\"}";
    }

    @Override
    public String visitCommandBlock(CommandBlockNode node) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < node.getCommands().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(node.getCommands().get(i).accept(this));
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String visitAssignment(AssignmentNode node) {
        return "{\"type\":\"Assignment\",\"variable\":\"" + node.getVariable() +
                "\",\"value\":" + node.getExpression().accept(this) + "}";
    }

    @Override
    public String visitConditional(ConditionalNode node) {
        String result = "{\"type\":\"Conditional\",\"condition\":" +
                node.getCondition().accept(this) +
                ",\"then\":" + node.getThenCommand().accept(this);
        if (node.hasElse()) {
            result += ",\"else\":" + node.getElseCommand().accept(this);
        }
        result += "}";
        return result;
    }

    @Override
    public String visitLoop(LoopNode node) {
        return "{\"type\":\"Loop\",\"condition\":" + node.getCondition().accept(this) +
                ",\"command\":" + node.getCommand().accept(this) + "}";
    }

    @Override
    public String visitExpression(ExpressionNode node) {
        return node.getLeft().accept(this);
    }

    @Override
    public String visitBinaryOp(BinaryOpNode node) {
        return "{\"type\":\"BinaryOp\",\"op\":\"" + node.getOperator() +
                "\",\"left\":" + node.getLeft().accept(this) +
                ",\"right\":" + node.getRight().accept(this) + "}";
    }

    @Override
    public String visitUnaryOp(UnaryOpNode node) {
        return "{\"type\":\"UnaryOp\",\"op\":\"" + node.getOperator() +
                "\",\"operand\":" + node.getOperand().accept(this) + "}";
    }

    @Override
    public String visitVariable(VariableNode node) {
        return "{\"type\":\"Variable\",\"name\":\"" + node.getName() + "\"}";
    }

    @Override
    public String visitLiteral(LiteralNode node) {
        return "{\"type\":\"Literal\",\"value\":\"" + node.getValue() +
                "\",\"valueType\":\"" + node.getType() + "\"}";
    }

    @Override
    public String visitIdentifier(IdentifierNode node) {
        return "{\"type\":\"Identifier\",\"name\":\"" + node.getName() + "\"}";
    }
}
