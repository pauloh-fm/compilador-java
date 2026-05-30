package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.visitor.Visitor;

public class AstPrinter implements Visitor {

    private int level = 0;

    private void indent() {
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
    }

    public void print(ProgramNode program) {
        program.visit(this);
    }

    @Override
    public void visitProgramNode(ProgramNode p) {

        System.out.println("PROGRAM");

        level++;

        if (p.declarations != null)
            p.declarations.visit(this);

        if (p.commands != null)
            p.commands.visit(this);

        level--;
    }

    @Override
    public void visitDeclarationNode(DeclarationNode d) {

        DeclarationNode current = d;

        while (current != null) {

            indent();
            System.out.println(
                    "DECL " + current.name + " : " + current.type);

            current = current.next;
        }
    }

    @Override
    public void visitAssignmentNode(AssignmentNode a) {
        AssignmentNode current = a;
        while (current != null) {
            indent();
            System.out.println("ASSIGN " + current.variable);
            level++;
            current.expression.visit(this);
            level--;
            current = (AssignmentNode) current.next;
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
    public void visitIdentifierNode(IdentifierNode i) {

        indent();
        System.out.println(i.name);
    }

    @Override
    public void visitLiteralNode(LiteralNode l) {

        indent();
        System.out.println(l.value);
    }
}