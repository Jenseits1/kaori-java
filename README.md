# 🎻 Kaori - Programming Language

**Kaori** is a statically typed, object-oriented programming language built with **Java 17**
It aims to offer a clear syntax, expressive semantics, and a powerful interpreter architecture for building modern applications and learning language design


## ✨ Features ✨

- **Statically Typed 🔒**  
  Enforces type safety for predictable, robust programs

- **Detailed Error Messages 🎯**  
  Developer-friendly diagnostics for syntax and runtime issues

- **Implemented Language Features ✅**

  - [x] Variable declarations (`make x = 10;`)
  - [x] Logical operators (`and`, `or`, `!`)
  - [x] Arithmetic operators (`+`, `-`, `*`, `/`)
  - [x] Comparison operators (`==`, `!=`, `<`, `>`, `<=`, `>=`)
  - [x] `if / else` statements
  - [x] `for` loops
  - [x] `while` loops
  - [x] Block statements for scope (`{ ... }`)
  - [x] Output with `print` statements
  - [ ] Functions
  - [ ] Classes and inheritance
  - [ ] Native data structures (e.g., lists, maps)
  - [ ] Pattern matching


## 🛠️ Technologies Used 🛠️

- **Java 17 ☕** — The entire interpreter is written in modern Java
- **Maven / Gradle** — Dependency and build management


## 📜 Grammar

```text
program                  -> statement* EOF

statement                -> expr_stmt
                         | print_stmt
                         | block
                         | if_stmt
                         | while_stmt
                         | for_stmt
                         | return_stmt
                         | variable_stmt                

variable_stmt            -> "make" IDENTIFIER ( "=" expression )? ";"

block                    -> "{" declaration* "}"

expr_stmt                -> expression ";"

print_stmt               -> "print" expression ";"

if_stmt                  -> "if" "(" expression ")" block_stmt ("else" (if_stmt | block_stmt))?

while_stmt               -> "while" "(" expression ")" statement

for_stmt                 -> "for" "(" var_decl expression ";" expression ")" block_stmt

expression               -> assignment

assignment               -> IDENTIFIER "=" assignment | logic_or

logic_or                 -> logic_and ("or" logic_and)*

logic_and                -> equality ("and" equality)*

equality                 -> comparison (("!=" | "==") comparison)*

comparison               -> term ((">" | ">=" | "<" | "<=") term)*

term                     -> factor (("+" | "-") factor)*

factor                   -> unary (("*" | "/") unary)*

unary                    -> ("!" | "-") unary | primary

primary                  -> NUMBER | STRING | "true" | "false"
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


## 📄 License 📄

Kaori is released under the **MIT License**.  
See the [`LICENSE`](LICENSE) file for more details.

---

