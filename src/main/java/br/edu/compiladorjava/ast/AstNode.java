package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public abstract class AstNode {
    public int line;
    public int column;

    public abstract void visit(Visitor v);
}