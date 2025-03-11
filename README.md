# BASIC-Interpreter
This project uses Java code to Lexically Analyze, Parse, and Interpret [BASIC](BASIC.pdf) code, allowing individuals to execute their own written [BASIC](BASIC.pdf) programs

## Purpose:
- To learn and understand what are the steps that an interpreter takes for executing different coding languages
- Practice writing JUnit Tests
- Practice Object Oriented Programming in Java

## How the Project Works:
- We begin the program by running Basic.java with 1 command line argument
    - Basic.java is where our Java main method is located
    - The command line argument is the name of the file with your BASIC code that you'd like to be executed
- First, we pass the file into the [Lexer](src/Lexer.java) and call the Lexer.lex() method
    - When we create the Lexer object, we use a [CodeHandler](src/CodeHandler.java) object. This Java class assists us in navigating the BASIC code file, treating it as a large string
    - The lex() method will iterate through the string, one character at a time, until we reach the end of the string. The end goal of this method is to return a linked list of ['Tokens'](src/Token.java) (lexemes) that represent the order of all of the different instructions, variables, and values in the BASIC program
    - During each iteration, there is conditional programming to create the correct token for the linked list
    - We ignore spaces, tabs, linefeed, and newline characters
    - The list of the different types of Tokens we're creating can be found in [Token.java](src/Token.java)
- Next, we pass the linked list of tokens into the [Parser](src/Parser.java) and call the Parser.parse() method
    - When we create the Parser object, we use a [TokenManager](src/TokenManager.java) object. This class is used to help use navigate the linked list by peeking into the list, checking if there are still tokens in the list, and removing specific tokens from the list.
    - As we iterate through the linked list, we are creating Nodes for our Abstract Syntax Tree (AST). The AST is how our interpreter will navigate the program and execute the correct operations in the correct order
    - All of the Nodes we're creating for our AST can be found in the [ASTNodes](src/ASTNodes/) folder
    - Nodes can be created from a single Token or from multiple Tokens. (e.g. a Number token can create an Integer Node and 2 number tokens with plus tokens can create a Math Operation Node)
    At the end of our parse() method, a single StatementsNode will be returned, which is the root node for our AST
- Finally, we pass a StatementsNode into the [Interpreter](src/Interpreter.java) and call the Interpreter.interpret() method
    - The interpret method will iterate through the provided StatementsNode, executing what each Node instructs
    - Conditional Programming is used during this iteration. This program will check the instance of the Node and once we determine what kind of Node we are visiting, this program will then execute the appropriate commands, whether that is storing a variable, printing to the terminal, or executing math operations
    - The Interpreter uses a linked list to store and retrieve data (made possible by the BASIC commands 'DATA' and 'READ')
    - The Interpreter also uses hashmaps so we can quickly look up the values for integer, float, or string variables
    - The Interpreter also uses a stack so that we can be able to move around our AST other than each Node in order. This makes jumping and loops possible in our BASIC code
- Once the EndNode is encountered in the Interpreter, the loop will end and the program will be completed


## Notable Document
- [BASIC Coding Language](BASIC.pdf): This document contains the rules for the version of BASIC that we are creating an interpreter for

## BASIC Code programs
- [ClassAverage.txt](src/ClassAverage.txt): This file uses the DATA and READ functionalities to store a student's name and the grades they've received. We then take this data and write to the terminal each student's average grade
- [CollatzConjecture.txt](src/CollatzConjecture.txt): The Collatz Conjecture is a mathematical problem. First, you provide a positive integer and if it is even, we divide it by 2 and if it's odd, we multiply it by 3 and add 1. We keep repeating this process until we reach the number 1 and display the number of steps it took to reach 1
- [FizzBuzz.txt](src/FizzBuzz.txt): The FizzBuzz program is a loop from 1 to 100, where we print each number with a few exceptions. If the number is divisible by 3, we write 'Fizz' instead. If the number is divisible by 5, we write 'Buzz' instead. Finally, if the number is divisible by both, then we write 'FizzBuzz' instead.

## TO-DO
- The Interpreter.java file stills needs to be debugged as it does not produce the correct outputs for CollatzConjecture.txt or FizzBuzz.txt files