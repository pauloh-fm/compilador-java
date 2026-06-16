package br.edu.compiladorjava.ast;

public class AstPrinter implements AstVisitor<String> {
    private int indent = 0;

    public String print(AstNode node) {
        return node.accept(this);
    }

    private String indent() {
        return "  ".repeat(indent);
    }

    @Override
    public String visitProgram(ProgramNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ProgramNode: ").append(node.getName()).append("\n");
        indent++;
        sb.append(node.getCorpo().accept(this));
        indent--;
        return sb.toString();
    }

    @Override
    public String visitCorpo(CorpoNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("CorpoNode\n");
        indent++;
        if (node.getDeclarations() != null && !node.getDeclarations().getDeclarations().isEmpty()) {
            sb.append(node.getDeclarations().accept(this));
        }
        sb.append(node.getCommandBlock().accept(this));
        indent--;
        return sb.toString();
    }

    @Override
    public String visitDeclarations(DeclarationsNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("DeclarationsNode\n");
        indent++;
        for (VariableDeclNode decl : node.getDeclarations()) {
            sb.append(decl.accept(this));
        }
        indent--;
        return sb.toString();
    }

    @Override
    public String visitVariableDecl(VariableDeclNode node) {
        return indent() + "VariableDeclNode: " + node.getName() + " : " + node.getType() + "\n";
    }

    @Override
    public String visitCommandBlock(CommandBlockNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("CommandBlockNode\n");
        indent++;
        for (AstNode cmd : node.getCommands()) {
            sb.append(cmd.accept(this));
        }
        indent--;
        return sb.toString();
    }

    @Override
    public String visitAssignment(AssignmentNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("AssignmentNode: ").append(node.getVariable()).append(" :=\n");
        indent++;
        sb.append(node.getExpression().accept(this));
        indent--;
        return sb.toString();
    }

    @Override
    public String visitConditional(ConditionalNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ConditionalNode\n");
        indent++;
        sb.append(indent()).append("condition:\n");
        indent++;
        sb.append(node.getCondition().accept(this));
        indent--;
        sb.append(indent()).append("then:\n");
        indent++;
        sb.append(node.getThenCommand().accept(this));
        indent--;
        if (node.hasElse()) {
            sb.append(indent()).append("else:\n");
            indent++;
            sb.append(node.getElseCommand().accept(this));
            indent--;
        }
        indent--;
        return sb.toString();
    }

    @Override
    public String visitLoop(LoopNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("LoopNode (while)\n");
        indent++;
        sb.append(indent()).append("condition:\n");
        indent++;
        sb.append(node.getCondition().accept(this));
        indent--;
        sb.append(indent()).append("command:\n");
        indent++;
        sb.append(node.getCommand().accept(this));
        indent--;
        indent--;
        return sb.toString();
    }

    @Override
    public String visitExpression(ExpressionNode node) {
        return node.getLeft().accept(this);
    }

    @Override
    public String visitBinaryOp(BinaryOpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("BinaryOpNode: ").append(node.getOperator()).append("\n");
        indent++;
        sb.append(node.getLeft().accept(this));
        sb.append(node.getRight().accept(this));
        indent--;
        return sb.toString();
    }

    @Override
    public String visitUnaryOp(UnaryOpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("UnaryOpNode: ").append(node.getOperator()).append("\n");
        indent++;
        sb.append(node.getOperand().accept(this));
        indent--;
        return sb.toString();
    }

    @Override
    public String visitVariable(VariableNode node) {
        return indent() + "VariableNode: " + node.getName() + "\n";
    }

    @Override
    public String visitLiteral(LiteralNode node) {
        return indent() + "LiteralNode: " + node.getValue() + " (" + node.getType() + ")\n";
    }

    @Override
    public String visitIdentifier(IdentifierNode node) {
        return indent() + "IdentifierNode: " + node.getName() + "\n";
    }
}
