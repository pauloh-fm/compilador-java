# Etapas do Projeto

Este arquivo organiza as atividades do trabalho com base na gramática em `linguagem.txt`.

## Etapa 1 - Análise Léxica

- [x] Criar, a partir da gramática fornecida, uma relação dos tokens da linguagem.
- [ ] Obter uma gramática léxica para este conjunto.
- [ ] Obter uma expressão regular para este conjunto.
- [x] Implementar o analisador léxico conforme o modelo do livro.
- [ ] Testar e documentar o analisador léxico.

## Etapa 2 - Análise Sintática

- [ ] Verificar se a gramática da linguagem é LL(1) e justificar a resposta.
- [x] Obter uma gramática equivalente que seja LL(1).
- [ ] Demonstrar, por meio dos conjuntos `first` e `follow`, que a nova gramática é LL(1).
- [ ] Obter, a partir da nova gramática, uma gramática sintática para a linguagem.
- [x] Implementar, por método recursivo descendente, um analisador léxico para a linguagem.
- [x] Implementar, por método recursivo descendente, um analisador sintático para a linguagem.
- [x] Integrar os analisadores léxico e sintático.
- [ ] Projetar e implementar uma interface com o usuário, em linha de comando ou janela.
- [ ] Desenvolver os casos de teste.
- [ ] Documentar o trabalho: sintaxe da linguagem-fonte, estrutura léxica, estrutura sintática, exemplos de programas-fonte, transformações gramaticais efetuadas, técnicas de análise empregadas, estruturas de dados e algoritmos utilizados, descrição da interface, mensagens de erro, exemplos de entradas e saídas, testes, manual de instalação e manual de operação.

## Etapa 3 - Montagem e Visualização da AST

- [x] Construir uma estrutura de dados que represente a estrutura sintática do programa-fonte.
- [x] Especificar as classes abstratas e concretas para representar os nós da AST.
- [x] Adaptar os métodos de análise sintática para construir a árvore durante o processamento do programa-fonte.
- [x] Prever uma opção que permita ao usuário visualizar a árvore depois de montada.
- [x] Utilizar o padrão de projeto Visitor.

## Etapa 4 - Análise de Contexto

- [ ] Descrever, em detalhes e com exemplos, todas as dependências de contexto da linguagem.
- [ ] Definir e documentar casos omissos da linguagem.
- [ ] Implementar o analisador de contexto.
- [ ] Implementar a tabela de símbolos.
- [ ] Implementar os métodos de identificação e verificação de tipos.
- [ ] Observar as regras de escopo e as regras de tipo da linguagem.
- [ ] Utilizar o padrão de projeto Visitor.
- [ ] Emitir mensagens de erro quando as dependências de contexto forem violadas.

## Etapa 5 - Geração de Código

- [ ] Implementar a geração de código para todos os comandos, funções e procedimentos da linguagem.
- [ ] Usar os padrões de código apropriados.
- [ ] Implementar e utilizar um gerador de rótulos.
- [ ] Criar um arquivo TXT contendo as instruções da máquina-objeto.
- [ ] Referenciar variáveis pelos seus nomes.
- [ ] Usar a máquina TAM, definida no livro-texto, como máquina-objeto.
- [ ] Considerar o modelo de máquina de pilha.
- [ ] Utilizar o padrão de projeto Visitor.

## Itens opcionais

- [ ] Gerar endereço para variáveis e parâmetros, com deslocamento e registrador.
- [ ] Gerar código para acessar vetores e matrizes.
- [ ] Gerar link estático para chamada de procedimentos e funções.
- [ ] Gerar código para declarações de variáveis.

## Estrutura criada para execução

- `docs/etapas/`: planejamento detalhado por etapa.
- `src/main/java/br/edu/compiladorjava/lexer/`: implementação da etapa 1.
- `src/main/java/br/edu/compiladorjava/parser/`: implementação da etapa 2.
- `src/main/java/br/edu/compiladorjava/ast/`: implementação da etapa 3.
- `src/main/java/br/edu/compiladorjava/semantic/`: implementação da etapa 4.
- `src/main/java/br/edu/compiladorjava/codegen/`: implementação da etapa 5.
- `src/main/java/br/edu/compiladorjava/cli/`: interface com usuário (CLI).
- `src/test/java/br/edu/compiladorjava/`: testes por etapa.
