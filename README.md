# 🎻 Kaori - Programming Language

Kaori is an interpreted, statically typed programming language originally built with Java 17.
🚧 Note: The Java version is now discontinued. Kaori is currently being rewritten in Rust

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
    -   [x] block statements for scope (`{ ... }`)
    -   [x] Output with `print` statements
    -   [x] Code comments (`/* this is a comment */`)
    -   [x] Functions
    -   [ ] Bytecode generation
    -   [ ] Virtual machine to interpret bytecode
    -   [ ] function and loop control flow (`break`, `continue`, `return`)
    -   [ ] Native data structures (e.g., lists, maps)
    -   [ ] Classes and inheritance

## Technologies Used

-   **Java 17** — The entire interpreter is written in modern Java
-   **Maven / Gradle** — Dependency and build management

## Grammar

```text
program                  -> declaration* EOF

type                     -> function_type | primitive_type
primitive_type           -> bool | f64 | str
function_type            -> ( [type [, type]*] ) -> type

declaration              -> variable_declaration
                         | function_declaration | statement

variable_declaration     -> identifier : type = expression ;

function_declaration     -> def identifier ( [variable_declaration*] ) : type block_statement

statement                -> expression_statement
                         | print_statement
                         | if_statement
                         | while_statement
                         | for_statement
                         | block_statement

expression_statement     -> expression ;

print_statement          -> print ( expression ) ;

if_statement             -> if expression block_statement [else [if_statement | block_statement]]?

while_statement          -> while expression block_statement

for_statement            -> for variable_declaration ; expression ; expression block_statement

block_statement          -> { declaration* }

expression               -> assignment | logic_or

assignment               -> identifier = expression

logic_or                 -> logic_and [|| logic_and]*

logic_and                -> equality [&& equality]*

equality                 -> comparison [[!= | ==] comparison]*

comparison               -> term [[> | >= | < | <=] term]*

term                     -> factor [[+ | -] factor]*

factor                   -> prefix_unary [[* | /] prefix_unary]*

prefix_unary             -> [! | -] unary | primary

primary                  -> number_literal
                         | string_literal
                         | boolean_literal
                         | postfix_unary
                         | ( expression )

postfix_unary            -> [identifier [++ | --]? | function_call]

function_call             -> callee [(expression [, expression]*)]*
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

## 💖 Name Inspiration

The name Kaori is inspired by the character Kaori Miyazono from the anime "Your Lie in April". She represents inspiration, motivation, and the desire to create something different from the standard — the same spirit behind creating this language

## License

Kaori is released under the **MIT License**.  
See the [`LICENSE`](LICENSE) file for more details
