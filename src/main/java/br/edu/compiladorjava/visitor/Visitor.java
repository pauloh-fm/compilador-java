package br.edu.compiladorjava.visitor;

import br.edu.compiladorjava.ast.*;

public interface Visitor {

    void visitProgramNode(ProgramNode p);

    void visitDeclarationNode(DeclarationNode d);

    void visitAssignmentNode(AssignmentNode a);

    void visitBinaryExpressionNode(BinaryExpressionNode e);

    void visitIdentifierNode(IdentifierNode i);

    void visitLiteralNode(LiteralNode l);
}