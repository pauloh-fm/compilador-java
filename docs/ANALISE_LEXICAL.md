# Análise Léxica - Mini-Triangle

## 1. Relação de Tokens

### Tokens da Linguagem

| Token | Tipo | Exemplos |
|-------|------|----------|
| Identifier | Identificadores | `x`, `count`, `variable_name` |
| Integer-Literal | Literais inteiros | `0`, `42`, `123456` |
| Operator | Operadores aritméticos | `+`, `-`, `*`, `/` |
| Operator | Operadores relacionais | `<`, `>`, `=` |
| Operator | Operadores diversos | `\` |
| Keyword | Palavras-chave | `begin`, `const`, `do`, `else`, `end` |
| Keyword | Palavras-chave | `if`, `in`, `let`, `then`, `var`, `while` |
| Separator | Separadores | `;`, `:`, `:=`, `~` |
| Delimiter | Delimitadores | `(`, `)` |
| EOT | Fim de arquivo | `<EOF>` |

---

## 2. Gramática Léxica

```
Token          ::= Identifier | Integer-Literal | Operator
                 | begin | const | do | else | end | if | in
                 | let | then | var | while
                 | ; | : | := | ~ | ( | ) | eot

Identifier     ::= Letter (Letter | Digit)*

Integer-Literal ::= Digit Digit*

Operator       ::= + | - | * | / | < | > | = | \

Letter         ::= a | b | ... | z | A | B | ... | Z

Digit          ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

Graphic        ::= Qualquer caractere visível (imprimível)

Separator      ::= Comment | space | eol

Comment        ::= ! Graphic* eol
```

---

## 3. Expressões Regulares

### Expressões Regulares para Tokens

```regex
# Identificadores
Identifier = [a-zA-Z][a-zA-Z0-9]*

# Literais inteiros
Integer = [0-9]+

# Operadores
Operator = [+\-*/<>=\\]

# Palavras-chave (case-sensitive)
Keyword = begin|const|do|else|end|if|in|let|then|var|while

# Separadores
Semicolon = ;
Colon = :
Becomes = :=
Is = ~
LParen = \(
RParen = \)

# Comentários (devem ser ignorados pelo lexer)
Comment = ![^\n]*\n

# Espaços em branco (devem ser ignorados)
Whitespace = [ \t\r\n]+

# Token final
EOT = \0
```

### Expressão Regular Unificada (DFA)

```regex
Token = (Keyword|Identifier|Integer|Operator|Comment|Whitespace|Separator|EOT)

Especificamente:
Token = (begin|const|do|else|end|if|in|let|then|var|while)
      | [a-zA-Z][a-zA-Z0-9]*
      | [0-9]+
      | :=
      | [+\-*/<>=\\;:~()]
      | ![^\n]*\n
      | [ \t\r\n]+
      | \0
```

### Reconhecimento de Padrões em Ordem de Prioridade

1. **Comentários**: `![^\n]*\n` - Começam com `!` e vão até o fim da linha
2. **Espaços em branco**: `[ \t\r\n]+` - Um ou mais espaços, tabs, quebras de linha
3. **Palavras-chave**: Exatamente as 11 palavras reservadas
4. **Operador especial**: `:=` (atribuição)
5. **Identificadores**: Começam com letra, seguidos de letras ou dígitos
6. **Literais inteiros**: Um ou mais dígitos
7. **Operadores**: Caracteres únicos `+ - * / < > = \`
8. **Separadores**: `;`, `:`, `~`, `(`, `)`
9. **EOT**: Fim de arquivo

---

## 4. Implementação do Analisador Léxico

### Estrutura de Classes

#### TokenType (enum)
Define todos os tipos de token reconhecidos pela linguagem.

```java
public enum TokenType {
    IDENTIFIER, INTLITERAL, OPERATOR,
    PLUS, MINUS, MULTIPLY, DIVIDE,
    LESS_THAN, GREATER_THAN, EQUALS, BACKSLASH,
    BEGIN, CONST, DO, ELSE, END, IF, IN, LET, THEN, VAR, WHILE,
    SEMICOLON, COLON, BECOMES, IS, LPAREN, RPAREN,
    EOT
}
```

#### Token
Representa um token individual com tipo, lexema e posição.

```java
public class Token {
    private final TokenType kind;
    private final String spelling;
    private final int line;
    private final int column;
    
    // Construtores e getters
}
```

#### Lexer (Scanner)
Implementa o algoritmo de scanning conforme apresentado em "Programming Language Processors in Java" (Watt & Brown).

**Funcionalidades principais:**
- Rastreamento de linha e coluna para mensagens de erro
- Pula comentários e espaços em branco
- Reconhecimento de identificadores e palavras-chave
- Reconhecimento de literais inteiros
- Reconhecimento de operadores e símbolos especiais
- Tratamento de erros léxicos com exceções

**Métodos principais:**
- `scan()` - Obtém o próximo token
- `peek()` - Previsuraliza o próximo token sem avançar
- `getPosition()` - Retorna a posição atual (para diagnósticos)

#### LexerException
Exceção personalizada para erros durante a análise léxica.

---

## 5. Testes e Documentação

### Cobertura de Testes

O arquivo `LexerTest.java` contém 24 testes que cobrem:

✅ **Identificadores**
- Identificadores simples
- Identificadores com dígitos
- Identificadores maiúsculos

✅ **Literais Inteiros**
- Literais simples
- Literais grandes

✅ **Palavras-chave**
- Todas as 11 palavras-chave
- Verificação que substrings de keywords não são reconhecidas como keywords

✅ **Operadores**
- Todos os 8 operadores individuais (`+`, `-`, `*`, `/`, `<`, `>`, `=`, `\`)

✅ **Separadores**
- Semicolon, Colon, LPAREN, RPAREN, IS
- Operador especial `:=`

✅ **Tratamento de Espaços e Comentários**
- Pulo de espaços em branco
- Pulo de comentários
- Rastreamento correto de linhas com comentários

✅ **Rastreamento de Posição**
- Rastreamento de número de linhas
- Rastreamento de colunas
- Informações corretas em exceções

✅ **Casos de Erro**
- Caracteres inválidos lançam exceção
- Exceções contêm informações de posição

✅ **Sequências de Tokens**
- Processamento de múltiplos tokens consecutivos
- Programa Mini-Triangle completo
- Múltiplos comentários em um programa

### Executar Testes

```bash
# Executar todos os testes do lexer
mvn test -Dtest=LexerTest

# Executar testes específicos
mvn test -Dtest=LexerTest#testSimpleIdentifier
```

---

## 6. Exemplos de Uso

### Exemplo 1: Escanear um identificador

```java
Lexer lexer = new Lexer("myVariable");
Token token = lexer.scan();

System.out.println(token);
// Saída: Token [type=IDENTIFIER, spelling='myVariable', line=1, column=1]
```

### Exemplo 2: Escanear um programa simples

```java
String program = "let x := 10;";
Lexer lexer = new Lexer(program);

Token token1 = lexer.scan(); // let
Token token2 = lexer.scan(); // x
Token token3 = lexer.scan(); // :=
Token token4 = lexer.scan(); // 10
Token token5 = lexer.scan(); // ;
Token token6 = lexer.scan(); // EOT
```

### Exemplo 3: Tratamento de erros

```java
try {
    Lexer lexer = new Lexer("x @ y");
    lexer.scan(); // x
    lexer.scan(); // Lança LexerException
} catch (LexerException e) {
    System.err.println(e.getMessage());
    // Saída: Caractere inválido: '@' at line 1, column 3
}
```

### Exemplo 4: Escanear programa Mini-Triangle

```java
String program = """
    let x := 5;
    var y;
    begin
      y := x + 3
    end
    """;

Lexer lexer = new Lexer(program);
Token token;

do {
    token = lexer.scan();
    System.out.println(token);
} while (token.getKind() != TokenType.EOT);
```

---

## 7. Algoritmo de Scanning

O analisador léxico funciona da seguinte forma:

1. **Inicialização**: Carrega o código-fonte e posiciona no primeiro caractere
2. **Pulo de separadores**: Remove espaços, tabs, quebras de linha e comentários
3. **Verificação de fim de arquivo**: Se encontrar `\0`, retorna EOT
4. **Classificação do caractere atual**:
   - Se for letra → escaneia identificador/palavra-chave
   - Se for dígito → escaneia literal inteiro
   - Se for operador/símbolo → escaneia operador ou símbolo especial
   - Caso contrário → lança exceção
5. **Retorno do token**: Retorna token com tipo, lexema e posição

---

## 8. Melhorias Implementadas

Comparado ao exemplo do livro Watt & Brown, a implementação inclui:

✅ **Rastreamento de posição**: Linha e coluna para cada token
✅ **Mensagens de erro detalhadas**: Exceções com posição do erro
✅ **Separação de tipos de operador**: Cada operador tem seu próprio tipo
✅ **Tratamento robusto de comentários**: Suporta comentários até fim da linha
✅ **Testes abrangentes**: 24 testes cobrindo todos os casos
✅ **Documentação completa**: JavaDoc em todas as classes e métodos
✅ **Expressões regulares formais**: Documentação das regex usadas

---

## Referências

- WATT, David A.; BROWN, Lorna A. Programming Language Processors in Java. Harlow: Addison-Wesley, 2000.
- Mini-Triangle Language Specification (fornecido pelo professor)
