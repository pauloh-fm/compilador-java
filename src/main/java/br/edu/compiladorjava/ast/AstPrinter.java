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

        if (p.declarations != null) p.declarations.visit(this);

        if (p.commands != null) p.commands.visit(this);

        level--;
    }

    @Override
    public void visitDeclarationNode(DeclarationNode d) {
        DeclarationNode current = d;
        while (current != null) {
            indent();
            System.out.println("DECL " + current.name + " : " + current.type);
            current = current.next;
        }
    }

    @Override
    public void visitAssignmentNode(AssignmentNode a) {
        indent();
        System.out.println("ASSIGN " + a.variable);
        level++;

        a.expression.visit(this);
        level--;
        if (a.next != null) {
            a.next.visit(this);
        }
    }

    @Override
    public void visitIfNode(IfNode node) {
        indent();
        System.out.println("IF");
        level++;
        indent();
        System.out.println("CONDITION");
        level++;
        node.condition.visit(this);
        level--;
        indent();
        System.out.println("THEN");
        level++;
        node.thenBranch.visit(this);
        level--;
        if (node.elseBranch != null) {
            indent();
            System.out.println("ELSE");
            level++;
            node.elseBranch.visit(this);
            level--;
        }
        level--;
        if (node.next != null) {
            node.next.visit(this);
        }
    }

    @Override
    public void visitWhileNode(WhileNode node) {
        indent();
        System.out.println("WHILE");
        level++;
        indent();
        System.out.println("CONDITION");
        level++;
        node.condition.visit(this);
        level--;
        indent();
        System.out.println("BODY");
        level++;
        node.body.visit(this);
        level--;
        level--;
        if (node.next != null) {
            node.next.visit(this);
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