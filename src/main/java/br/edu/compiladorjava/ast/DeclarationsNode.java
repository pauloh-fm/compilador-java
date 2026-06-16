package br.edu.compiladorjava.ast;

import java.util.List;

public class DeclarationsNode extends AstNode {
    private List<VariableDeclNode> declarations;

    public DeclarationsNode(List<VariableDeclNode> declarations, int line, int column) {
        super(line, column);
        this.declarations = declarations;
    }

    public List<VariableDeclNode> getDeclarations() {
        return declarations;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDeclarations(this);
    }
}
