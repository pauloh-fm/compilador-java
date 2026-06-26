# Relatório de resultado da execução do projeto

Este documento resume a análise do estado atual do projeto, os testes realizados no menu da aplicação e as principais lacunas em relação ao enunciado das etapas.

## Resumo executivo

O projeto compila com sucesso e o menu da aplicação executa corretamente as opções 1 a 5 com o programa de entrada atual. A etapa léxica está implementada, o parser constrói uma AST parcial, a análise de contexto valida o programa de exemplo e a geração de código salva um arquivo `saida.txt`.

Apesar disso, o projeto ainda não cobre integralmente todos os requisitos do enunciado. Existem lacunas relevantes na modelagem da AST, na análise de contexto completa e na geração de código para estruturas de controle.

## Verificações executadas

### Build

- Comando: `mvn -DskipTests package`
- Resultado: sucesso
- Saída relevante: geração do artefato `target/compilador-java-0.1.0-SNAPSHOT.jar`

### Testes automatizados

- Comando: `mvn test`
- Resultado: falha em um teste
- Falha observada em `br.edu.compiladorjava.AppTest`
- Motivo: a asserção espera `Compilador Java UNIVASF`, mas a aplicação retorna `Compilador Java UNIVASF - Modo Sintático Direto`

### Execução do menu

Foram testadas as opções 1 a 5 da interface em linha de comando usando `mvn exec:java -Dexec.mainClass=br.edu.compiladorjava.App`.

- Opção 1: executa a análise léxica e exibe a tabela de tokens
- Opção 2: executa a análise léxica e confirma que o programa é sintaticamente correto
- Opção 3: além das etapas anteriores, imprime a AST
- Opção 4: além das etapas anteriores, executa a análise de contexto sem erro no programa de exemplo
- Opção 5: além das etapas anteriores, gera o arquivo `saida.txt`

### Saída gerada na etapa 5

O arquivo `saida.txt` foi criado com as instruções:

```text
LOADL 10
STORE 0
LOADL 5
STORE 1
LOAD 0
LOAD 1
LOADL 2
CALL MULT
CALL ADD
STORE 0
HALT
```

## Análise por etapa

### Etapa 1 - Análise léxica

Implementada e funcional para o programa de exemplo.

Evidências no código:

- `src/main/java/br/edu/compiladorjava/lexer/Scanner.java`
- `src/main/java/br/edu/compiladorjava/lexer/Token.java`
- `src/main/java/br/edu/compiladorjava/lexer/Kind.java`

Observação:

- O léxico reconhece identificadores, inteiros, palavras reservadas, operadores aritméticos, relacionais e símbolos de pontuação.
- Comentários com `!` são ignorados.

### Etapa 2 - Análise sintática

Implementada e funcional para o programa de exemplo.

Evidências no código:

- `src/main/java/br/edu/compiladorjava/parser/Parser.java`
- `src/main/java/br/edu/compiladorjava/parser/ParserException.java`

Observação:

- O parser valida a estrutura do programa e produz mensagens de erro sintático com linha e coluna.
- A implementação cobre declaração, atribuição, expressões, comandos compostos, `if` e `while`.

### Etapa 3 - AST

Parcialmente implementada.

Evidências no código:

- `src/main/java/br/edu/compiladorjava/ast/ProgramNode.java`
- `src/main/java/br/edu/compiladorjava/ast/AssignmentNode.java`
- `src/main/java/br/edu/compiladorjava/ast/BinaryExpressionNode.java`
- `src/main/java/br/edu/compiladorjava/ast/AstPrinter.java`

Observações:

- A árvore existe e pode ser impressa.
- A AST atual representa bem declarações, atribuições e expressões.
- Os comandos `if`, `while` e seus blocos não aparecem como nós específicos na árvore impressa, o que indica cobertura incompleta em relação ao enunciado.

### Etapa 4 - Análise de contexto

Parcialmente implementada.

Evidências no código:

- `src/main/java/br/edu/compiladorjava/semantic/ContextAnalyzer.java`
- `src/main/java/br/edu/compiladorjava/semantic/SymbolTable.java`
- `src/main/java/br/edu/compiladorjava/semantic/Symbol.java`
- `src/main/java/br/edu/compiladorjava/semantic/Type.java`

Observações:

- Há verificação de declaração prévia, duplicidade no escopo corrente e compatibilidade básica de tipos.
- O programa de exemplo passa na análise semântica.
- A gestão de escopos existe na tabela de símbolos, mas o fluxo atual não mostra abertura e fechamento de escopos por bloco/comando composto.
- Não foi encontrada uma cobertura explícita para todas as dependências de contexto descritas no enunciado.

### Etapa 5 - Geração de código

Parcialmente implementada.

Evidências no código:

- `src/main/java/br/edu/compiladorjava/codegen/CodeGenerator.java`
- `src/main/java/br/edu/compiladorjava/codegen/LabelGenerator.java`
- `src/main/java/br/edu/compiladorjava/codegen/TamInstruction.java`

Observações:

- O gerador produz código para atribuições e expressões aritméticas/lógicas simples.
- O arquivo `saida.txt` é salvo corretamente.
- Não há geração de código para estruturas de controle como `if` e `while`.
- O uso de rótulos existe como classe auxiliar, mas não foi observado no fluxo atual de geração.

## Lacunas identificadas

As seguintes pendências ainda impedem dizer que o projeto está completo em todas as etapas:

- A mensagem inicial do aplicativo diverge da expectativa do teste `AppTest`.
- A AST ainda não modela todos os comandos da linguagem de forma explícita.
- A análise de contexto não evidencia escopos completos por bloco.
- A geração de código ainda não cobre todos os comandos e estruturas mencionados no enunciado.
- O arquivo `src/main/java/br/edu/compiladorjava/cli/CompilerCli.java` ainda está como TODO.

## Conclusão

O projeto está funcional como protótipo de compilador para o programa de exemplo e já executa as cinco opções do menu. Entretanto, para afirmar aderência total ao roteiro do trabalho, ainda são necessárias evoluções na AST, na análise semântica e, principalmente, na geração de código para controle de fluxo e demais construções da linguagem.

## Referências úteis

- `README.md`
- `pom.xml`
- `entrada.txt`
- `saida.txt`
- `src/main/java/br/edu/compiladorjava/App.java`