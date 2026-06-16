package br.edu.compiladorjava.ast;

public interface AstVisitor<T> {
    T visitProgram(ProgramNode node);
    T visitCorpo(CorpoNode node);
    T visitDeclarations(DeclarationsNode node);
    T visitVariableDecl(VariableDeclNode node);
    T visitCommandBlock(CommandBlockNode node);
    T visitAssignment(AssignmentNode node);
    T visitConditional(ConditionalNode node);
    T visitLoop(LoopNode node);
    T visitExpression(ExpressionNode node);
    T visitBinaryOp(BinaryOpNode node);
    T visitUnaryOp(UnaryOpNode node);
    T visitVariable(VariableNode node);
    T visitLiteral(LiteralNode node);
    T visitIdentifier(IdentifierNode node);
}
