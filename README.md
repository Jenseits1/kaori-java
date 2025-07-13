# 🎻 Kaori - Programming Language

**Kaori** is an interpreted statically typed programming language built with **Java 17**

## Features

-   **Statically Typed**  
    Enforces type safety for predictable, and faster runtimes

-   **Detailed Error Messages**  
    Developer-friendly diagnostics for syntax and runtime issues

-   **Implemented Language Features**

    -   [x] Variable declaration (`x: f64 = 10;`, `x: bool = true;`, `x: str = "Hello world";`)
    -   [x] Assign operators (`x = 5;`)
    -   [x] Logical operators (`&&`, `||`, `!`)
    -   [x] Arithmetic operators (`+`, `-`, `*`, `/`)
    -   [x] Comparison operators (`==`, `!=`, `<`, `>`, `<=`, `>=`)
    -   [x] Postfix operators (`++`, `--`)
    -   [x] `if / else` statements
    -   [x] `for` loops
    -   [x] `while` loops
    -   [x] Block statements for scope (`{ ... }`)
    -   [x] Output with `print` statements
    -   [ ] Functions
    -   [ ] Native data structures (e.g., lists, maps)
    -   [ ] Bytecode generation
    -   [ ] Classes and inheritance

## Technologies Used

-   **Java 17** — The entire interpreter is written in modern Java
-   **Maven / Gradle** — Dependency and build management

## Grammar

```text
program                  -> statement* EOF

statement                -> expr_stmt
                         | print_stmt
                         | block_stmt
                         | if_stmt
                         | while_stmt
                         | for_stmt
                         | return_stmt
                         | variable_stmt

function_stmt            -> "def" identifier "(" we ";"

expr_stmt                -> expression ";"

block_stmt               -> "{" statement* "}"

variable_stmt            -> identifier ":" type = expression ";"

print_stmt               -> "print" "(" expression ")" ";"

if_stmt                  -> "if" expression block_stmt ("else" (if_stmt | block_stmt))?

while_stmt               -> "while" expression block_stmt

for_stmt                 -> "for" variable_stmt ";" expression ";" expression block_stmt

expression               -> assign | or ";"

assign                   -> identifier "=" assignment

logic_or                 -> logic_and ("||" logic_and)*

logic_and                -> equality ("&&" equality)*

equality                 -> comparison (("!=" | "==") comparison)*

comparison               -> term ((">" | ">=" | "<" | "<=") term)*

term                     -> factor (("+" | "-") factor)*

factor                   -> unary (("*" | "/") unary)*

prefix_unary             -> ("!" | "-") unary | primary

primary                  -> number | string | boolean | postfix_unary | "(" expression ")"

postfix_unary            -> identifier ("++" | "--")?
```

## Getting Started

### Prerequisites

-   Java 17+
-   Maven or Gradle (recommended for building)

### Installation

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

## Running Scripts

To run a `.kaori` script:

```bash
java -jar target/kaori.jar path/to/script.kaori
```

To enter the interactive mode (REPL):

```bash
java -jar target/kaori.jar
```

## Contributing

Contributions are warmly welcome!

### Ways to Contribute:

-   🚨 Report bugs
-   ✨ Propose new features or syntax ideas
-   🧪 Add new test cases
-   📚 Improve the documentation

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

## 💖 Name Inspiration

The name Kaori is inspired by the character Kaori Miyazono from the anime "Your Lie in April". She represents inspiration, motivation, and the desire to create something different from the standard — the same spirit behind creating this language

## License

Kaori is released under the **MIT License**.  
See the [`LICENSE`](LICENSE) file for more details
