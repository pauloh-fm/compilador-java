# Documentação do Compilador Java

**Projeto de Compiladores**  
**Data:** 18 de maio de 2026

---

## Índice

1. [Objetivo](#objetivo)
2. [Etapa 1 – Análise Léxica](#etapa-1--análise-léxica)
3. [Etapa 2 – Análise Sintática](#etapa-2--análise-sintática)
4. [Próximos passos](#próximos-passos)

---

## Objetivo

Este documento registra a evolução do projeto por etapas, com o status de cada item, as justificativas técnicas necessárias e referências diretas para a implementação no código-fonte.

O objetivo é manter uma documentação viva, fácil de exportar para PDF e simples de atualizar à medida que o compilador evolui.

---

## Etapa 1 – Análise Léxica

### Checklist

- [x] Implementar o analisador léxico conforme o modelo do livro.
- [ ] Criar, a partir da gramática fornecida, uma relação dos tokens da linguagem.
- [ ] Obter uma gramática léxica para este conjunto.
- [ ] Obter uma expressão regular para este conjunto.
- [ ] Testar e documentar o analisador léxico.

### Relação de tokens

Os tokens implementados cobrem palavras-chave, identificadores, literais, operadores e delimitadores. O conjunto principal é:

**Palavras-chave:**
- `program`, `var`, `integer`, `boolean`, `begin`, `end`, `if`, `then`, `else`, `while`, `do`, `true`, `false`, `or`, `and`.

**Identificadores:**
- Sequências iniciadas por letra ou sublinhado, seguidas por letras, dígitos ou sublinhado.

**Literais:**
- Inteiros e reais simples, incluindo formas como `2.` e `.5`.

**Operadores:**
- `+`, `-`, `*`, `/`, `:=`, `<`, `>`, `=`.

**Delimitadores:**
- `,`, `;`, `:`, `(`, `)`, `.`.

### Gramática léxica

```
program       → palavra-chave reservada
var           → palavra-chave reservada
identifier    → [a-zA-Z_][a-zA-Z0-9_]*
integer-lit   → [0-9]+
float-lit     → ([0-9]+\.[0-9]*|\.[0-9]+)
whitespace    → [\t\r\n ]+
comment-line  → //.*
comment-block → /\*.*?\*/
```

### Expressões regulares

Representação dos principais padrões:

| Padrão | Regex |
|--------|-------|
| Identificador | `[A-Za-z_][A-Za-z0-9_]*` |
| Inteiro | `[0-9]+` |
| Float | `([0-9]+\.[0-9]*\|\.[0-9]+)` |
| Espaço em branco | `[\t\r\n ]+` |
| Comentário de linha | `//.*` |
| Comentário de bloco | `/\*.*?\*/` |

### Implementação

O lexer está implementado em:
- **`src/main/java/br/edu/compiladorjava/lexer/Lexer.java`** – Núcleo da análise léxica

Ele percorre a fonte com controle de linha e coluna, reconhece palavras reservadas antes de classificar identificadores e ignora espaços e comentários.

Estruturas relacionadas:
- **`TokenType.java`** – Enumeração de tipos de token
- **`Token.java`** – Registro com tipo, lexema, linha e coluna
- **`LexerException.java`** – Exceção léxica com localização

### Testes

Os testes automatizados cobrem:

1. **Programa simples** – Sequência básica de tokens
2. **Comentários e floats** – Ignora comentários, reconhece `.5` e `2.`
3. **Erro léxico** – Rejeita caractere inválido

Localização: **`src/test/java/br/edu/compiladorjava/lexer/LexerTest.java`**

---

## Etapa 2 – Análise Sintática

### Checklist

- [x] Implementar, através do método recursivo descendente, um analisador léxico para a linguagem.
- [x] Implementar, através do método recursivo descendente, um analisador sintático para a linguagem.
- [x] Integrar os analisadores léxico e sintático.
- [x] Projetar e implementar uma interface com o usuário (linha de comando ou janela).
- [x] Desenvolver os casos de teste.
- [ ] Verificar se a gramática da linguagem é LL(1). Justificar a sua resposta.
- [ ] Obter uma gramática equivalente que seja LL(1).
- [ ] Demonstrar, através do cálculo dos conjuntos first e follow, que a nova gramática é LL(1).
- [ ] Obter, a partir da nova gramática, uma gramática sintática para a linguagem.
- [ ] Documentar o trabalho completo (linguagem-fonte, estrutura léxica, sintática, exemplos, transformações, técnicas, algoritmos, interface, erros, testes, manuais).

### Verificação LL(1)

A gramática original não é LL(1) por dois motivos principais:

1. **Recursão à esquerda**: Produções como `<lista-de-comandos> ::= <lista-de-comandos> <comando> ;` causam loop infinito no parser.
2. **Prefixos comuns**: Alternativas que começam com o mesmo símbolo não-terminal (ex. `<expressão-simples>`) requerem lookahead infinito.

**Status:** ✓ Foi adotada uma gramática equivalente sem recursão à esquerda e com fatoração à esquerda, adequada para análise preditiva recursiva descendente.

### Gramática equivalente

Forma resumida utilizada pelo parser:

```
programa            ::= 'program' id ';' corpo '.'
corpo               ::= declarações comando-composto

declarações         ::= 'var' lista-ids ':' tipo ';' declarações | ε
lista-ids           ::= id (',' id)*

comando             ::= atribuição | condicional | iterativo | comando-composto
atribuição          ::= id ':=' expressão
condicional         ::= 'if' expressão 'then' comando ('else' comando)?
iterativo           ::= 'while' expressão 'do' comando
comando-composto    ::= 'begin' lista-comandos 'end'
lista-comandos      ::= comando (';' comando)* | ε

expressão           ::= expressão-simples (op-rel expressão-simples)?
expressão-simples   ::= termo ((op-ad) termo)*
termo               ::= fator ((op-mul) fator)*
fator               ::= id | literal | '(' expressão ')'
```

### Justificativa com FIRST/FOLLOW

Os conjuntos foram usados para validar as produções com alternativas e ε. Em cada caso, os conjuntos FIRST das alternativas são disjuntos; quando há produção vazia, a interseção entre FIRST da alternativa não vazia e FOLLOW do não-terminal é vazia.

**Casos principais:**

| Não-terminal | FIRST | FOLLOW | Status |
|--------------|-------|--------|--------|
| `declarações` | {var} ∪ {ε} | {begin} | ✓ Disjuntos |
| `lista-comandos` | {id, if, while, begin} ∪ {ε} | {end} | ✓ Disjuntos |
| `expressão` | FIRST(expr-simples) | {;, then, do, )} | ✓ LL(1) |
| `resto-expressão` | {<, >, =} ∪ {ε} | {;, then, do, )} | ✓ Disjuntos |

**Conclusão:** A gramática satisfaz LL(1) e permite análise preditiva com lookahead de 1 token.

### Implementação

O parser recursivo descendente está em:
- **`src/main/java/br/edu/compiladorjava/parser/Parser.java`** – Núcleo do parser

Ele consome a sequência de tokens produzida pelo lexer e valida a estrutura do programa, declarações, comandos e expressões.

Integração:
- **`src/main/java/br/edu/compiladorjava/cli/CompilerCli.java`** – Interface de linha de comando
- **`src/main/java/br/edu/compiladorjava/App.java`** – Ponto de entrada com suporte a arquivo ou string
- **`ParserException.java`** – Exceção sintática com localização

### Interface com o usuário (CLI)

A aplicação aceita:

```bash
# Exibe mensagem de uso
java -jar compilador.jar

# Lê de arquivo
java -jar compilador.jar programa.txt

# Lê string de comando
java -jar compilador.jar "program test; begin x := 1 end."
```

**Saída esperada:**
```
PROGRAM('program')@1:1
IDENTIFIER('test')@1:9
SEMICOLON(';')@1:13
BEGIN('begin')@1:15
...
Analise lexica e sintatica concluida com sucesso.
```

### Testes

Os testes de parser cobrem:

1. **Programa válido** – Declarações, atribuições, condicionais e laços
2. **Programa inválido** – Erro sintático de encerramento

Localização: **`src/test/java/br/edu/compiladorjava/parser/ParserTest.java`**

**Exemplo de teste válido:**
```java
program teste;
var x: integer;
var flag: boolean;
begin
  x := 1;
  if true then
    flag := false
  else
    flag := true;
  while x < 10 do
    x := x + 1
end.
```

---

## Próximos passos

1. **Completar justificativa formal de LL(1)** com cálculos matemáticos detalhados de FIRST/FOLLOW.
2. **Expandir documentação** com exemplos de entrada e saída reais do compilador.
3. **Adicionar seções futuras**:
   - Etapa 3: Montagem e Visualização da AST
   - Etapa 4: Análise de Contexto
   - Etapa 5: Geração de Código

---

**Gerado em:** 18 de maio de 2026  
**Versão:** 1.0
