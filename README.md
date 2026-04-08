# compilador-java

Projeto open source da disciplina de Compiladores.

## Equipe

- Ariel Fernando
- Paulo Henrique de Farias Martins
- Nivaldo

## Stack do Projeto

- Linguagem: Java 21
- Build e dependências: Maven
- Testes: JUnit 5
- Execução local: terminal (CLI)

## Estrutura Base

- `src/main/java/br/edu/compiladorjava`: código-fonte principal
- `src/test/java/br/edu/compiladorjava`: testes automatizados
- `linguagem.txt`: gramática/base da linguagem
- `ETAPAS.md`: checklist de evolução por etapas

## Pré-requisitos

1. Java 21 instalado
2. Maven instalado

O projeto está configurado para Java 21 em `pom.xml` com `maven.compiler.release` igual a `21`.

Verificação rápida:

```bash
java -version
mvn -v
```

Se o Maven estiver usando Java diferente do 21, execute os comandos com:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn test
```

Ou defina no terminal para a sessao atual:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH="$JAVA_HOME/bin:$PATH"
```

## Como rodar o projeto

Executar testes:

```bash
mvn test
```

Gerar build:

```bash
mvn clean package
```

Executar aplicação base:

```bash
mvn -q exec:java
```

## Procedimento para construir uma nova feature (atividade)

Use este fluxo para qualquer nova atividade do compilador (lexer, parser, AST, contexto, geração de código).

1. Escolher a atividade no checklist

- Abrir `ETAPAS.md` e selecionar um item não concluído.

2. Criar branch da atividade

```bash
git checkout -b feat/nome-da-atividade
```

3. Implementar em camadas

- Criar/alterar código em `src/main/java/...`.
- Se necessário, incluir novas classes por responsabilidade (exemplo: `lexer`, `parser`, `ast`, `semantic`, `codegen`).

4. Criar ou atualizar testes

- Adicionar testes em `src/test/java/...` cobrindo casos válidos e inválidos.

5. Executar validações locais

```bash
mvn test
mvn clean package
```

6. Atualizar documentação

- Marcar progresso em `ETAPAS.md`.
- Atualizar `README.md` se mudar fluxo, comandos ou estrutura.

7. Commit e revisão

```bash
git add .
git commit -m "feat: descricao da atividade"
git push -u origin feat/nome-da-atividade
```

## Convenções recomendadas

- Commits curtos e descritivos.
- Sempre entregar com teste passando.
- Preferir uma atividade por branch para facilitar revisão.
