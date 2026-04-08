# Java Maven Setup Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Create a clean Java 21 Maven project scaffold for the compiler course team.

**Architecture:** Use a standard Maven layout with a single application module, JUnit 5 for tests, and a package structure that separates compiler concerns early. Keep the initial code minimal so the team can add lexer, parser, AST, semantic analysis, and code generation incrementally.

**Tech Stack:** Java 21, Maven, JUnit 5.

---

### Task 1: Add Maven project metadata

**Files:**

- Create: `pom.xml`

**Step 1: Write the project descriptor**

Use Java 21 as the compiler target, set a clear group/artifact name, and add JUnit 5 dependencies.

**Step 2: Verify the descriptor is valid**

Run: `mvn -q test`
Expected: Maven resolves the project successfully, even if no tests exist yet.

### Task 2: Create the source layout

**Files:**

- Create: `src/main/java/br/edu/compiladorjava/App.java`
- Create: `src/test/java/br/edu/compiladorjava/AppTest.java`
- Create: `src/main/java/br/edu/compiladorjava/lexer/.gitkeep`
- Create: `src/main/java/br/edu/compiladorjava/parser/.gitkeep`
- Create: `src/main/java/br/edu/compiladorjava/ast/.gitkeep`
- Create: `src/main/java/br/edu/compiladorjava/semantic/.gitkeep`
- Create: `src/main/java/br/edu/compiladorjava/codegen/.gitkeep`
- Create: `src/main/java/br/edu/compiladorjava/util/.gitkeep`

**Step 1: Add a minimal runnable entry point**

Print a short startup message so the project has a smoke-testable main class.

**Step 2: Add a basic test**

Write a JUnit test that validates the starter behavior.

**Step 3: Verify the test passes**

Run: `mvn test`
Expected: All tests pass.

### Task 3: Add repository hygiene and documentation

**Files:**

- Create: `.gitignore`
- Update: `README.md`

**Step 1: Ignore build artifacts and IDE files**

Add Maven output, Java class files, and common IDE folders to `.gitignore`.

**Step 2: Document setup and commands**

Explain how to run tests, build, and execute the starter app.

**Step 3: Verify the README matches the scaffold**

Confirm the documented commands work with the new Maven project.
