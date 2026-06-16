package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.lexer.Lexer;
import br.edu.compiladorjava.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AstTest {

    @Test
    public void testProgramNodeCreation() {
        String source = "program test; var x : integer; begin x := 1; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        assertNotNull(program);
        assertEquals("test", program.getName());
        assertNotNull(program.getCorpo());
    }

    @Test
    public void testAstPrinter() {
        String source = "program exemplo; var x : integer; begin x := 10; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        AstPrinter printer = new AstPrinter();
        String output = printer.print(program);

        assertNotNull(output);
        assertTrue(output.contains("ProgramNode"));
        assertTrue(output.contains("exemplo"));
        assertTrue(output.contains("CorpoNode"));
    }

    @Test
    public void testJsonExport() {
        String source = "program test; var x : integer; begin x := 5; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        JsonVisitor visitor = new JsonVisitor();
        String json = visitor.toJson(program);

        assertNotNull(json);
        assertTrue(json.contains("\"type\":\"Program\""));
        assertTrue(json.contains("\"name\":\"test\""));
    }

    @Test
    public void testDeclarationsNode() {
        String source = "program test; var x : integer; var y : boolean; begin end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        DeclarationsNode decls = program.getCorpo().getDeclarations();
        assertEquals(2, decls.getDeclarations().size());

        assertEquals("x", decls.getDeclarations().get(0).getName());
        assertEquals("integer", decls.getDeclarations().get(0).getType());

        assertEquals("y", decls.getDeclarations().get(1).getName());
        assertEquals("boolean", decls.getDeclarations().get(1).getType());
    }

    @Test
    public void testAssignmentNode() {
        String source = "program test; begin x := 42; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        CommandBlockNode commands = program.getCorpo().getCommandBlock();
        AssignmentNode assignment = (AssignmentNode) commands.getCommands().get(0);

        assertEquals("x", assignment.getVariable());
        assertNotNull(assignment.getExpression());
    }

    @Test
    public void testConditionalNode() {
        String source = "program test; begin if x > 5 then y := 1; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        CommandBlockNode commands = program.getCorpo().getCommandBlock();
        ConditionalNode conditional = (ConditionalNode) commands.getCommands().get(0);

        assertNotNull(conditional.getCondition());
        assertNotNull(conditional.getThenCommand());
        assertFalse(conditional.hasElse());
    }

    @Test
    public void testLoopNode() {
        String source = "program test; begin while x > 0 do x := x - 1; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        CommandBlockNode commands = program.getCorpo().getCommandBlock();
        LoopNode loop = (LoopNode) commands.getCommands().get(0);

        assertNotNull(loop.getCondition());
        assertNotNull(loop.getCommand());
    }

    @Test
    public void testBinaryOpNode() {
        String source = "program test; begin x := 1 + 2; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        CommandBlockNode commands = program.getCorpo().getCommandBlock();
        AssignmentNode assignment = (AssignmentNode) commands.getCommands().get(0);

        BinaryOpNode binOp = (BinaryOpNode) assignment.getExpression();
        assertEquals("+", binOp.getOperator());
        assertNotNull(binOp.getLeft());
        assertNotNull(binOp.getRight());
    }

    @Test
    public void testLiteralNode() {
        String source = "program test; begin x := 100; end";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        ProgramNode program = parser.parseProgram();

        CommandBlockNode commands = program.getCorpo().getCommandBlock();
        AssignmentNode assignment = (AssignmentNode) commands.getCommands().get(0);

        LiteralNode literal = (LiteralNode) assignment.getExpression();
        assertEquals("100", literal.getValue());
        assertEquals("int", literal.getType());
    }
}
