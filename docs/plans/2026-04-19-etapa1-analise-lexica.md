# Etapa 1 - Analise Lexica

## 1. Relacao de Tokens

### 1.1 Palavras reservadas
- `program`
- `var`
- `begin`
- `end`
- `if`
- `then`
- `else`
- `while`
- `do`
- `integer`
- `boolean`
- `true`
- `false`
- `and`
- `or`

### 1.2 Identificadores e literais
- `IDENTIFIER`
- `NUMBER` (inteiro e ponto flutuante)
- `STRING`

### 1.3 Operadores
- Relacionais: `<`, `>`, `=`
- Aditivos: `+`, `-`, `or`
- Multiplicativos: `*`, `/`, `and`
- Atribuicao: `:=`

### 1.4 Separadores
- `;`
- `,`
- `:`
- `(`
- `)`
- `.`

### 1.5 Token de fim de arquivo
- `EOF`

## 2. Gramatica Lexica

```text
LETRA           ::= [A-Za-z_]
DIGITO          ::= [0-9]
ID              ::= LETRA (LETRA | DIGITO)*
INT_LIT         ::= DIGITO+
FLOAT_LIT       ::= DIGITO+ '.' DIGITO* | '.' DIGITO+
NUMBER          ::= FLOAT_LIT | INT_LIT
STRING          ::= '"' (qualquer_caractere_menos_aspas_ou_quebra_linha)* '"'

ASSIGN          ::= ':='
COLON           ::= ':'
SEMICOLON       ::= ';'
COMMA           ::= ','
DOT             ::= '.'
LEFT_PAREN      ::= '('
RIGHT_PAREN     ::= ')'
PLUS            ::= '+'
MINUS           ::= '-'
STAR            ::= '*'
SLASH           ::= '/'
LESS            ::= '<'
GREATER         ::= '>'
EQUAL           ::= '='

WS              ::= (' ' | '\t' | '\r' | '\n')+
```

## 3. Expressoes Regulares

- `ID`: `^[A-Za-z_][A-Za-z0-9_]*$`
- `INT_LIT`: `^[0-9]+$`
- `FLOAT_LIT`: `^([0-9]+\.[0-9]*|\.[0-9]+)$`
- `NUMBER`: `^([0-9]+|[0-9]+\.[0-9]*|\.[0-9]+)$`
- `ASSIGN`: `^:=$`
- `REL_OP`: `^(<|>|=)$`
- `ADD_OP`: `^(\+|-|or)$`
- `MUL_OP`: `^(\*|/|and)$`

## 4. Implementacao (Modelo do Livro)

O analisador lexico foi implementado no estilo scanner incremental:
- percorre a entrada caractere a caractere;
- identifica lexemas com `advance`, `peek`, `peekNext` e `match`;
- cria tokens por categoria (reservadas, operadores, separadores, identificadores, literais);
- acumula linha para mensagens de erro;
- finaliza com token `EOF`.

Arquivos principais:
- `src/main/java/br/edu/compiladorjava/lexer/TokenType.java`
- `src/main/java/br/edu/compiladorjava/lexer/Lexer.java`

## 5. Testes e Evidencias

Casos automatizados:
- `LexerTest`: fluxo basico de tokenizacao.
- `LexerGrammarTest#shouldScanLanguageGrammarTokens`: programa com `program`, `var`, `begin/end`, `:` e `:=`.
- `LexerGrammarTest#shouldScanRelationalAdditiveAndMultiplicativeOperators`: operadores relacionais, aditivos e multiplicativos.

Comando de validacao:

```bash
mvn test
```

Resultado esperado:
- Build com sucesso.
- Todos os testes do lexer aprovados.