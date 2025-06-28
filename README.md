# 🐉 Kaori - Programming Language

**Kaori** is a statically typed, object-oriented programming language built with **Java 17**
It aims to offer a clear syntax, expressive semantics, and a powerful interpreter architecture for building modern applications and learning language design

---

## ✨ Features ✨

- **Statically Typed 🔒**  
  Enforces type safety at compile time for predictable, robust programs

- **Object-Oriented 🧱**  
  Everything is an object — including primitives — with support for classes and inheritance

- **Clean Java-like Syntax ✍️**  
  Designed to feel familiar to Java/C-style language users, but simpler and more concise

- **Detailed Error Messages 🎯**  
  Developer-friendly diagnostics for syntax and runtime issues

---

## 🛠️ Technologies Used 🛠️

- **Java 17 ☕** — The entire interpreter is written in modern Java
- **Maven / Gradle** — Dependency and build management

---

## 📜 Grammar

```text
program                  -> declaration* EOF

declaration              -> class_decl
                         | var_decl
                         | fun_decl
                         | statement

class_decl               -> "class" IDENTIFIER "{" function* "}"

fun_decl                 -> "fun" IDENTIFIER "(" parameters? ")" block

var_decl                 -> "var" IDENTIFIER ( "=" expression )? ";"

statement                -> expr_stmt
                         | print_stmt
                         | block
                         | if_stmt
                         | while_stmt
                         | for_stmt
                         | return_stmt

block                    -> "{" declaration* "}"

expr_stmt                -> expression ";"

print_stmt               -> "print" expression ";"

if_stmt                  -> "if" "(" expression ")" statement ("else" statement)?

while_stmt               -> "while" "(" expression ")" statement

for_stmt                 -> "for" "(" var_decl? expression? ";" expression? ")" statement

return_stmt              -> "return" expression? ";"

expression               -> assignment

assignment               -> IDENTIFIER "=" assignment | logic_or

logic_or                 -> logic_and ("or" logic_and)*

logic_and                -> equality ("and" equality)*

equality                 -> comparison (("!=" | "==") comparison)*

comparison               -> term ((">" | ">=" | "<" | "<=") term)*

term                     -> factor (("+" | "-") factor)*

factor                   -> unary (("*" | "/") unary)*

unary                    -> ("!" | "-") unary | primary

primary                  -> NUMBER | STRING | "true" | "false" | "nil"
                         | IDENTIFIER | "(" expression ")"
```

## 🚀 Getting Started 🚀

### 📋 Prerequisites

- Java 17+
- Maven or Gradle (recommended for building)

### ⬇️ Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/kaori-lang.git
    cd kaori-lang
    ```

2. Build the project with Maven:

    ```bash
    mvn package
    ```

    Or with Gradle:

    ```bash
    ./gradlew build
    ```

3. Run the REPL:

    ```bash
    java -jar target/kaori.jar
    ```

---

## 🧪 Running Scripts

To run a `.kaori` script:

```bash
java -jar target/kaori.jar path/to/script.kaori
```

To enter the interactive mode (REPL):

```bash
java -jar target/kaori.jar
```

---

## 🤝 Contributing 🤝

Kaori is a passion project, and contributions are warmly welcome!

### Ways to Contribute:

- 🚨 Report bugs  
- ✨ Propose new features or syntax ideas  
- 🧪 Add new test cases  
- 📚 Improve the documentation  

### Steps:

1. Fork the repo  
2. Create a new branch:

    ```bash
    git checkout -b feature/my-feature
    ```

3. Make your changes and commit:

    ```bash
    git commit -m 'feat: add my feature'
    ```

4. Push and open a PR

---

## 📄 License 📄

Kaori is released under the **MIT License**.  
See the [`LICENSE`](LICENSE) file for more details.

---

