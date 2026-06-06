package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class AssignmentNode extends CommandNode {

    public String variable;
    public ExpressionNode expression;

    public AssignmentNode(String variable, ExpressionNode expression, int line, int column) {
        this.variable = variable;
        this.expression = expression;
        this.line = line;
        this.column = column;
    }

    @Override
    public void visit(Visitor v) {
        v.visitAssignmentNode(this);
    }
}