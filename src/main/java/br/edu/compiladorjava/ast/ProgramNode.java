package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class ProgramNode extends AstNode {
    public DeclarationNode declarations;
    public CommandNode commands;

    @Override
    public void visit(Visitor v) {
        v.visitProgramNode(this);
    }
}