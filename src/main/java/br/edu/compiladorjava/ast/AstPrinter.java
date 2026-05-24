package br.edu.compiladorjava.ast;
import br.edu.compiladorjava.visitor.Visitor;

public class AstPrinter implements Visitor {

    private int level = 0;

    private void indent() {
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
    }

    public void print(ProgramNode p) {

        System.out.println("\n---> Iniciando impressao da arvore\n");

        p.visit(this);
    }

    @Override
    public void visitProgramNode(ProgramNode p) {

        if (p == null) {
            return;
        }

        System.out.println("PROGRAM");

        level++;

        if (p.declarations != null) {
            p.declarations.visit(this);
        }

        if (p.commands != null) {
            p.commands.visit(this);
        }

        level--;
    }

    @Override
    public void visitDeclarationNode(DeclarationNode d) {

        if (d == null) {
            return;
        }

        indent();
        System.out.println("DECL " + d.name);

        if (d.next != null) {
            d.next.visit(this);
        }
    }

    @Override
    public void visitAssignmentNode(AssignmentNode c) {

        if (c == null) {
            return;
        }

        indent();
        System.out.println("ASSIGN " + c.variable);

        level++;

        if (c.expression != null) {
            c.expression.visit(this);
        }

        level--;

        if (c.next != null) {
            c.next.visit(this);
        }
    }

    @Override
    public void visitBinaryExpressionNode(BinaryExpressionNode e) {

        indent();
        System.out.println(e.operator);

        level++;

        e.left.visit(this);
        e.right.visit(this);

        level--;
    }

    @Override
    public void visitIdentifierNode(IdentifierNode e) {

        indent();
        System.out.println(e.name);
    }
}