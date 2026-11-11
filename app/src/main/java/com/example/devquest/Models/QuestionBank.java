package com.example.devquest.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class QuestionBank {

    private static ArrayList<QuestionModel> mcList;
    private static ArrayList<QuestionModel> idList;
    private static ArrayList<QuestionModel> fbList;
    private static final HashMap<String, String> HINTS = new HashMap<>();

    public static ArrayList<QuestionModel> load(String language, String type, int limit) {
        mcList = new ArrayList<>();
        idList = new ArrayList<>();
        fbList = new ArrayList<>();
        HINTS.clear();

        switch (language) {
            case "C++":        cpp();  break;
            case "Java":       java(); break;
            case "JavaScript": js();   break;
            default:           csharp();
        }

        ArrayList<QuestionModel> result;
        if (type.equals("fill")) result = fbList;
        else if (type.equals("identification")) result = idList;
        else result = mcList;

        Collections.shuffle(result);

        if (result.size() > limit) {
            return new ArrayList<>(result.subList(0, limit));
        }
        return result;
    }

    private static void mc(String q, String a, String b, String c, String d, String ans, String hint) {
        mcList.add(new QuestionModel(q, a, b, c, d, ans));
        HINTS.put(q, hint);
    }

    private static void id(String q, String ans, String hint) {
        idList.add(new QuestionModel(q, "", "", "", "", ans));
        HINTS.put(q, hint);
    }

    private static void fb(String q, String ans, String hint) {
        fbList.add(new QuestionModel(q, "", "", "", "", ans));
        HINTS.put(q, hint);
    }

    public static String getHint(String question) {
        return HINTS.get(question);
    }

    private static void cpp() {
        // ---- Multiple Choice ----
        mc("What will be the output?\nint age = 16;\nif (age >= 18 && age <= 25)\n    cout << \"Eligible\";\nelse\n    cout << \"Not Eligible\";",
                "Eligible", "Not Eligible", "Error", "No output", "Not Eligible", "The && operator needs both conditions to be true. Is 16 at least 18?");
        mc("What will be the output?\nint temperature = 35;\nif (temperature > 30 || temperature < 10)\n    cout << \"Extreme Weather\";\nelse\n    cout << \"Normal Weather\";",
                "Normal Weather", "Error", "Extreme Weather", "No output", "Extreme Weather", "The || operator needs only one condition to be true. Is 35 greater than 30?");
        mc("What will be the output?\nint score = 80;\nif (score >= 75 && score <= 100)\n    cout << \"Passed\";\nelse\n    cout << \"Failed\";",
                "Failed", "Passed", "Error", "No output", "Passed", "Is 80 between 75 and 100? Both ends are included.");
        mc("What will be the output?\nint number = 12;\nif (number < 5 || number > 20)\n    cout << \"Outside Range\";\nelse\n    cout << \"Within Range\";",
                "Outside Range", "Error", "No output", "Within Range", "Within Range", "Check each condition separately. Is 12 below 5, or above 20?");
        mc("What will be the output?\nint attendance = 90;\nchar permission = 'Y';\nif (attendance >= 80 && permission == 'Y')\n    cout << \"Allowed\";\nelse\n    cout << \"Not Allowed\";",
                "Not Allowed", "Error", "Allowed", "No output", "Allowed", "Both conditions must be true: attendance of at least 80 and permission equal to 'Y'.");
        mc("What code should replace the blank to check whether a number is positive?\n#include <iostream>\nusing namespace std;\nint main() {\n    int number = 8;\n    if (________) {\n        cout << \"Positive\";\n    }\n    return 0;\n}",
                "number < 0", "number == 0", "number > 0", "number != 8", "number > 0", "A positive number is greater than zero.");
        mc("What is the output of the following C++ code?\n#include <iostream>\nusing namespace std;\nint main() {\n    int x = 5;\n    int y = x++ + 2;\n    cout << x << \" \" << y;\n    return 0;\n}",
                "5 7", "6 7", "6 8", "7 6", "6 7", "x++ uses the old value of x in the expression first, then increases x.");
        mc("What code should replace the blank to print numbers from 1 to 5?\n#include <iostream>\nusing namespace std;\nint main() {\n    for (int i = 1; ________; i++) {\n        cout << i << \" \";\n    }\n    return 0;\n}",
                "i < 5", "i <= 5", "i == 5", "i = 5", "i <= 5", "The loop must still run when i is 5, so the condition has to include 5.");
        mc("What is the output of the following C++ code?\n#include <iostream>\nusing namespace std;\nint main() {\n    int sum = 0;\n    for (int i = 1; i <= 4; i++) {\n        sum += i;\n    }\n    cout << sum;\n    return 0;\n}",
                "4", "6", "10", "15", "10", "Add 1 + 2 + 3 + 4.");
        mc("What is the output of the following C++ code?\n#include <iostream>\nusing namespace std;\nint main() {\n    int x = 12;\n    if (x % 2 == 0 && x > 10) {\n        cout << \"A\";\n    } else {\n        cout << \"B\";\n    }\n    return 0;\n}",
                "A", "B", "12", "AB", "A", "12 is even and also greater than 10. Check which branch runs.");
        mc("What code should replace the blank to display Hello World!?\n#include <iostream>\nusing namespace std;\nint main() {\n________\nreturn 0;\n}",
                "cout >> \"Hello World!\";", "cout << \"Hello World!\";", "cin << \"Hello World!\";", "cin >> HelloWorld;", "cout << \"Hello World!\";", "Output in C++ uses cout together with the << operator.");
        mc("What is the output of the following C++ code?\n#include <iostream>\nusing namespace std;\nint main() {\n    for (int i = 1; i <= 3; i++) {\n        for (int j = 1; j <= 2; j++) {\n            cout << i;\n        }\n    }\n    return 0;\n}",
                "123", "112233", "121212", "11223344", "112233", "The inner loop runs twice for every value of i, so each i is printed twice in a row.");
        mc("What is the output of the following C++ code?\n#include <iostream>\nusing namespace std;\nint main() {\n    int numbers[] = {3, 6, 9, 12, 15};\n    cout << numbers[1] + numbers[3];\n    return 0;\n}",
                "15", "18", "21", "27", "18", "Array indexes start at 0, so index 1 is the second element and index 3 is the fourth.");
        mc("What code should replace the blank to calculate the sum of two numbers?\n#include <iostream>\nusing namespace std;\nint main() {\n    int a = 10, b = 5;\n    int sum = ________;\n    cout << sum;\n    return 0;\n}",
                "a - b", "a + b", "a * b", "a / b", "a + b", "Which operator adds two numbers?");
        mc("What code should replace the blank to access the third element of an array?\n#include <iostream>\nusing namespace std;\nint main() {\n    int numbers[] = {10, 20, 30, 40};\n    cout << numbers[________];\n    return 0;\n}",
                "1", "2", "3", "4", "2", "Array indexes start at 0, so the third element is not index 3.");
        mc("What is the output of the following C++ code?\n#include <iostream>\nusing namespace std;\nint main() {\n    string name = \"C++\";\n    cout << name.length();\n    return 0;\n}",
                "2", "3", "4", "Error", "3", "Count every character in the string, including both plus signs.");
        mc("Why are selection statements such as if and switch used in programming?",
                "To repeat instructions", "To perform mathematical operations", "To make decisions based on conditions", "To store values in arrays", "To make decisions based on conditions", "Think about what if and switch do after checking a condition.");
        mc("Which of the following best describes nested loops?",
                "A loop inside another loop", "Two loops written on separate lines", "A loop with no condition", "A loop that executes only once", "A loop inside another loop", "Picture a loop placed in the body of another loop.");
        mc("Which statement about the && (AND) operator is true?",
                "Returns true if at least one condition is true", "Returns true only if both conditions are true", "Returns false only if both conditions are false", "Compares two values for equality", "Returns true only if both conditions are true", "AND needs every condition to be true.");
        mc("What is the purpose of an array?",
                "To store multiple values of the same type in a single variable", "To display output on the screen", "To perform calculations only", "To receive user input only", "To store multiple values of the same type in a single variable", "An array holds a group of values under a single name.");

        // ---- Identification ----
        id("Identify the data type used to store decimal numbers.", "float|double", "Two types store decimals: one with single precision and one with double precision.");
        id("Identify the programming language feature that allows comments spanning multiple lines.", "Multi-line Comment", "It starts with /* and ends with */.");
        id("Identify the storage area that holds a program's variables while it is running.", "Memory", "Think of where the computer temporarily keeps data while a program is running.");
        id("Identify the operator used to obtain the memory address of a variable.", "Address-of Operator (&)|Address-of Operator|&", "It uses the ampersand symbol.");
        id("Identify the file that contains the source code written by the programmer.", "Source File", "It is the file with the .cpp extension that you write yourself.");
        id("Identify the value returned by a function when no return type is required.", "void", "This keyword means 'nothing' and is used where no value is returned.");
        id("Identify the process of fixing errors in a program.", "Debugging", "It is what you do when you hunt down and fix bugs.");
        id("Identify the programming technique of breaking a problem into smaller functions.", "Modular Programming", "Splitting a program into small, reusable pieces.");
        id("Identify the feature that allows multiple functions to have the same name but different parameters.", "Function Overloading", "Same function name, different parameter list.");
        id("Identify the blueprint used to create objects.", "Class", "It describes what its objects will have and do. Objects are created from it.");
        id("Identify the object-oriented principle of hiding internal data from direct access.", "Encapsulation", "Think of a capsule: data kept safely inside the class and hidden from outside.");
        id("Identify the function that serves as the entry point of a C++ program.", "main()|main", "Every C++ program starts running here.");
        id("Identify the type of error that occurs while the program is executing.", "Runtime Error", "It happens after the program compiles, while it is actually running.");
        id("Identify the file stream class commonly used to read data from files.", "ifstream", "It comes from <fstream>. The name means 'input file stream'.");
        id("Identify the OOP principle where a derived class acquires members from a base class.", "Inheritance", "A child class receives members from a parent class.");
        id("Identify the mechanism that allows functions to operate differently based on the object calling them.", "Polymorphism", "The word means 'many forms'.");
        id("Identify the type of function declared inside a class and shared by all objects of that class.", "Static Member Function", "It is declared with the static keyword and does not need an object to be called.");
        id("Identify the process of converting source code into machine code.", "Compilation", "A compiler does this to your source code.");
        id("Identify the error that occurs when the program's syntax violates language rules before execution.", "Syntax Error", "The compiler reports it before the program can even run.");
        id("Identify the OOP concept that focuses on exposing essential features while hiding implementation details.", "Abstraction", "Show what something does, hide how it does it.");

        // ---- Fill in the Blank ----
        fb("The C++ header file commonly used for input and output operations is ____.", "iostream", "Its name stands for input/output stream.");
        fb("The statement used to display output on the screen is ____.", "cout", "It starts with c and works with the << operator.");
        fb("The keyword used to define a constant value is ____.", "const", "Short for 'constant'.");
        fb("The data type used to store a single character is ____.", "char", "Short for 'character'.");
        fb("The statement used to receive input from the user is ____.", "cin", "It works with the >> operator.");
        fb("The symbol used to access the value stored at a memory address is ____.", "* (Asterisk)|*|Asterisk", "This symbol is the dereference operator. It is also used for multiplication.");
        fb("A named storage location in memory is called a ____.", "variable", "Its value can vary.");
        fb("A reusable block of code that performs a specific task is called a ____.", "function", "You define it once and call it as many times as you need.");
        fb("The keyword used to return a value from a function is ____.", "return", "It sends a value back to the caller.");
        fb("The process of combining data and functions into a single unit is called ____.", "encapsulation", "Bundling data and functions together inside a class.");
        fb("The keyword used to define a class in C++ is ____.", "class", "This keyword begins the blueprint for objects.");
        fb("A special function that is automatically called when an object is created is called a ____.", "constructor", "It has the same name as the class and no return type.");
        fb("The process of creating an object from a class is called ____.", "instantiation", "It is related to the word 'instance'.");
        fb("The keyword used to inherit from a base class is ____.", "public", "It is an access specifier, used like: class Dog : ____ Animal.");
        fb("The OOP principle that allows one interface to represent many forms is called ____.", "polymorphism", "It means 'many forms'.");
        fb("A function that calls itself is known as a ____ function.", "recursive", "It is related to the word 'recursion'.");
        fb("The keyword used to allocate memory dynamically is ____.", "new", "A three-letter keyword that pairs with delete.");
        fb("The keyword used to release dynamically allocated memory is ____.", "delete", "It pairs with new.");
        fb("A class that contains at least one pure virtual function is called an ____ class.", "abstract", "It contains a virtual function declared with = 0.");
        fb("The process of converting a derived-class object into a base-class type is called ____.", "upcasting", "Moving 'up' the inheritance hierarchy.");
    }

    private static void java() {
        // ---- Multiple Choice ----
        mc("Which data type is used to store whole numbers?", "String", "boolean", "int", "char", "int", "Think of integers. The type name is a short form of the word.");
        mc("Which symbol ends most Java statements?", "; (semicolon)", ": (colon)", ". (period)", ", (comma)", "; (semicolon)", "It is punctuation at the end of most lines of code.");
        mc("What is the correct way to print text to the console?",
                "print.console(\"Hello\");", "Console.WriteLine(\"Hello\");", "System.out.println(\"Hello\");", "echo(\"Hello\");", "System.out.println(\"Hello\");", "It begins with System.out.");
        mc("What is the output of System.out.println(5 + 3);", "53", "8", "5 + 3", "Error", "8", "Both values are numbers, so + does plain addition.");
        mc("Which operator checks whether two values are equal?", "!=", ">=", "=", "==", "==", "A single = assigns a value. Checking equality takes more than one character.");
        mc("Which statement is used to make a decision?", "class", "repeat", "if", "import", "if", "It is often used together with else.");
        mc("Which loop is commonly used when you know how many times to repeat?", "for loop", "if loop", "looping", "while loop", "for loop", "Think of counting from a start value to an end value.");
        mc("Which of these is a valid Java variable declaration?", "int age = 20;", "age int = 20;", "integer age = 20;", "var = age 20;", "int age = 20;", "Type first, then the name, then the value.");
        mc("What is the output?\nint[] numbers = {2, 4, 6, 8};\nSystem.out.println(numbers.length);", "3", "4", "5", "8", "4", "Count how many elements are inside the braces.");
        mc("What is the output?\nSystem.out.println(5 + 3 * 2);", "10", "11", "16", "13", "11", "Multiplication happens before addition.");
        mc("What is the index of the first element in a Java array?", "1", "2", "0", "-1", "0", "Java arrays are zero-based.");
        mc("Which operator represents logical AND?", "&&", "!", "||", "==", "&&", "Two symbols: the same character typed twice.");
        mc("What is the output?\nint x = 7;\nx++;\nSystem.out.println(x);", "6", "7", "8", "9", "8", "x++ adds 1 to x.");
        mc("What is the output?\nint x = 10;\nint y = 3;\nSystem.out.println(x / y);", "4", "1", "3.33", "3", "3", "Both numbers are int, so the result is an int with no decimal part.");
        mc("Which keyword is used to create a subclass?", "Implements", "Inherits", "extends", "super", "extends", "It is used like: class Dog ____ Animal.");
        mc("Which exception occurs when an integer is divided by zero?\nint result = 10 / 0;",
                "ArithmeticException", "IOException", "NumberFormatException", "NullPointerException", "ArithmeticException", "The exception's name comes from arithmetic.");
        mc("What is the output?\nint x = 5;\nSystem.out.println(x++ + ++x);", "10", "11", "12", "13", "12", "x++ uses the current value first. ++x increases first. Trace x step by step.");
        mc("Which statement about Java interfaces is correct?",
                "A class can implement only one interface.", "An interface cannot contain any methods.", "Interfaces can only be used with abstract classes.", "A class can implement multiple interfaces.", "A class can implement multiple interfaces.", "Think about how many interfaces a class may implement, unlike classes it can extend.");
        mc("What happens when this code runs?\nString s = null;\nSystem.out.println(s.length());",
                "Prints 0", "Throws NullPointerException", "Compilation error", "Prints null", "Throws NullPointerException", "Calling a method on null causes a problem.");
        mc("What is the output?\nstatic int calculate(int n) {\n    if (n <= 1) {\n        return 1;\n    }\n    return n * calculate(n - 1);\n}\nSystem.out.println(calculate(4));",
                "4", "10", "16", "24", "24", "This is a factorial: 4 x 3 x 2 x 1.");

        // ---- Identification ----
        id("A named storage location used to hold a value in a program.", "Variable", "It holds a value that can change.");
        id("A data type that stores either true or false.", "boolean", "It has only two possible values.");
        id("A repetition structure that executes a block of code while a condition is true.", "while loop|while", "It repeats for as long as its condition stays true. The keyword is a common English word.");
        id("A reusable block of code that performs a particular task.", "Method", "You call it by name, and it can take parameters and return a value.");
        id("A collection of elements of the same declared type that can be accessed using indexes.", "Array", "Its elements are accessed with an index inside square brackets.");
        id("The OOP principle that allows a class to acquire fields and methods from another class.", "Inheritance", "A subclass 'inherits' from a superclass.");
        id("The process of defining multiple methods with the same name but different parameter lists.", "Method overloading", "Same method name, different parameter list in the same class.");
        id("The special method-like class member that initializes an object when it is created.", "Constructor", "It has the same name as the class and no return type.");
        id("The OOP principle of bundling data and methods together while controlling access to internal state.", "Encapsulation", "Make the fields private and use getters and setters.");
        id("The mechanism that allows a subclass to provide its own implementation of an inherited method.", "Method overriding", "The subclass redefines a method that has the same signature.");
        id("The OOP concept in which the same superclass or interface reference can represent objects with different behaviors.", "Polymorphism", "It means 'many forms'.");
        id("A class that cannot be instantiated directly and may contain abstract methods.", "Abstract class", "It is declared with the abstract keyword.");
        id("A method declaration that has no implementation in the abstract class and must be implemented by a concrete subclass unless already implemented.", "Abstract method", "It has a signature but no body.");
        id("The runtime error thrown when a program attempts to access an array index outside its valid range.", "ArrayIndexOutOfBoundsException", "The name describes the problem: array, index, out of bounds.");
        id("The Java mechanism that allows exceptions to be handled using try, catch, and optionally finally.", "Exception handling", "Think about how a program deals with errors gracefully instead of crashing.");
        id("Identify the Java structure that selects one block of code from several possible cases based on an expression.", "switch statement|switch", "It uses case labels and break.");
        id("Identify the Java access modifier that allows a member to be accessed from anywhere, subject to normal package and module accessibility rules.", "public access modifier|public", "This is the most open access level.");
        id("Identify the OOP principle that hides implementation details and exposes only essential functionality.", "Abstraction", "Show the essentials and hide the details.");
        id("Identify the process of converting a primitive value into its corresponding wrapper object, such as converting an int to an Integer.", "Autoboxing", "It is an automatic conversion into a wrapper class.");
        id("Identify the Java mechanism in which a method calls itself to solve a smaller version of a problem.", "Recursion", "A method that calls itself.");

        // ---- Fill in the Blank ----
        fb("The Java keyword used to declare a class is ____.", "class", "It begins every class declaration.");
        fb("The statement used to display text in the console is System.out.____().", "println|println()", "It prints the text and then moves to a new line.");
        fb("The data type used to store whole numbers is ____.", "int", "Short for 'integer'.");
        fb("Every Java statement usually ends with a ____.", "Semicolon (;)|Semicolon|;", "It looks like ;");
        fb("The keyword used to create a new object is ____.", "new", "A three-letter keyword used to allocate an object.");
        fb("The keyword used to define a constant variable in Java is ____.", "final", "A variable declared with it cannot change.");
        fb("The operator used to check whether two values are not equal is ____.", "!=", "The ! means 'not'.");
        fb("The keyword used to inherit a class from another class is ____.", "Extends", "The subclass 'extends' the base class.");
        fb("To compare two primitive values for equality, use the operator ____.", "==", "Two equals signs.");
        fb("The Java keyword that sends a value back from a method is ____.", "return", "It sends a value back to the caller.");
        fb("The property used to get the number of elements in an array is ____.", "length", "For arrays this is a property, so there are no parentheses.");
        fb("The keyword used to prevent a method from being overridden is ____.", "final", "It is the same keyword that makes a variable constant.");
        fb("The Java keyword used to exit a loop immediately is ____.", "break", "It exits the loop (or switch) right away.");
        fb("The method used to compare the contents of two strings is ____.", "equals()|equals", "Do not use == to compare the contents of strings.");
        fb("The keyword used to refer to the current object is ____.", "this", "A four-letter keyword that refers to the current instance.");
        fb("A method that belongs to the class rather than an individual object is declared using ____.", "static", "It belongs to the class, not to an individual object.");
        fb("The keyword used to explicitly call a superclass constructor is ____.", "super", "It calls the parent class's constructor.");
        fb("The keyword used to implement an interface in a class declaration is ____.", "implements", "It is used together with interfaces.");
        fb("The keyword used to indicate that a method may throw an exception to its caller is ____.", "throws", "It goes in the method signature, after the parameter list.");
        fb("The keyword used to declare a method that belongs to an interface or class without requiring an object instance is ____.", "Static", "It lets you call the method without creating an object.");
    }

    private static void csharp() {
        // ---- Multiple Choice ----
        mc("What is the output?\nint age = 18;\nConsole.WriteLine(age);", "age", "\"18\"", "18", "Error", "18", "Console.WriteLine prints the value stored in the variable, not its name.");
        mc("Which Windows Forms control is used to allow users to type input?", "Label", "TextBox", "Button", "PictureBox", "TextBox", "It is the control that users type into.");
        mc("What is the output?\nConsole.WriteLine(10 % 4);", "2", "4", "6", "10", "2", "The % operator gives the remainder of a division.");
        mc("Which property changes the text displayed on a Button?", "Name", "Size", "Text", "Location", "Text", "It is the property that holds the label shown on the button.");
        mc("Which event occurs when a Button is pressed?", "Load", "Click", "Change", "Open", "Click", "It is the event raised when a user taps or clicks.");
        mc("Which data type stores True or False values?", "int", "string", "bool", "double", "bool", "Short for 'Boolean'.");
        mc("Why should buttons that perform similar actions have a consistent appearance?",
                "To reduce memory usage", "To improve usability", "To speed up compilation", "To reduce code", "To improve usability", "Think about how users learn and get used to an interface.");
        mc("What is the output?\nstring name = \"Raquel\";\nConsole.WriteLine(name.Length);", "5", "6", "7", "8", "6", "Count the letters in the name.");
        mc("Which OOP principle allows a class to inherit features from another class?", "Encapsulation", "Inheritance", "Polymorphism", "Abstraction", "Inheritance", "A child class inherits from a parent class.");
        mc("Which control is best for displaying an application's logo?", "Label", "TextBox", "PictureBox", "ComboBox", "PictureBox", "Which control is made for showing images?");
        mc("What is the output?\nfor(int i = 1; i <= 3; i++)\n{\n    Console.Write(i);\n}", "321", "123", "111", "0123", "123", "Console.Write does not add a new line.");
        mc("A login form should contain which controls?",
                "Two TextBoxes and a Button", "One Label only", "One PictureBox only", "One Panel only", "Two TextBoxes and a Button", "Think of username, password, and something to submit.");
        mc("What exception may occur here?\nint number = int.Parse(\"Hello\");",
                "IOException", "DivideByZeroException", "FormatException", "NullReferenceException", "FormatException", "The text cannot be converted because it is not in a valid number format.");
        mc("Why should validation be added to input fields?",
                "To increase program size", "To prevent invalid data from being entered", "To slow processing", "To remove controls", "To prevent invalid data from being entered", "Think about what happens when bad data gets in.");
        mc("What is the output?\nclass Animal\n{\n    public virtual void Sound()\n    {\n        Console.WriteLine(\"Animal\");\n    }\n}\nclass Dog : Animal\n{\n    public override void Sound()\n    {\n        Console.WriteLine(\"Dog\");\n    }\n}\nAnimal a = new Dog();\na.Sound();",
                "Animal", "Dog", "Animal Dog", "Error", "Dog", "The object is really a Dog, even though the variable's type is Animal. The method is overridden.");
        mc("Which interface design principle helps users find important actions quickly?",
                "Recursion", "Visual Hierarchy", "Inheritance", "Method Overloading", "Visual Hierarchy", "Arrange elements by how important they are.");
        mc("What is the output?\nList<string> items =\nnew List<string>()\n{\n    \"Burger\",\n    \"Fries\",\n    \"Drink\"\n};\nConsole.WriteLine(items.Count);",
                "2", "3", "4", "Error", "3", "Count the strings inside the list.");
        mc("In a food-ordering system, menu categories are placed on the left side while products appear on the right side. Which design principle is applied?",
                "Visual Hierarchy", "Polymorphism", "Encapsulation", "Recursion", "Visual Hierarchy", "Arrange elements by their importance and position on the screen.");
        mc("What is the best practice when designing a registration form?",
                "Save data without validation", "Validate inputs before saving", "Hide all error messages", "Use only one input field", "Validate inputs before saving", "Check the inputs first, then save.");
        mc("A \"Save\" button should be disabled until all required fields are completed. What design goal does this support?",
                "Preventing user errors", "Increasing memory usage", "Faster compilation", "Reducing inheritance", "Preventing user errors", "It stops users from submitting incomplete data.");
        mc("What is the purpose of the break statement in a loop?",
                "Restart the loop", "Exit the loop", "Skip the entire program", "Declare a variable", "Exit the loop", "It ends the loop immediately.");
        mc("Which keyword is used to create a new object in C#?", "break", "switch", "new", "while", "new", "It is the same keyword used before a constructor call.");
        mc("Which loop executes a block of code while a condition is true?", "for", "while", "foreach", "switch", "while", "The name of this loop describes its condition. It is not the counting loop.");
        mc("What is the correct way to write a single-line comment in C#?", "// comment", "<!-- comment -->", "** comment", "## comment", "// comment", "C# uses the same single-line comment style as Java and C++.");
        mc("Which keyword is used to declare an integer variable in C#?", "string", "int", "bool", "char", "int", "Short for 'integer'.");
        mc("Which method displays text in the console and moves to the next line?",
                "Console.ReadLine()", "Console.Input()", "Console.WriteLine()", "Console.Print()", "Console.WriteLine()", "Look for the method whose name ends with 'Line'.");
        mc("Which operator checks whether two values are equal?", "!=", "==", "=", ">=", "==", "A single = assigns a value. Checking equality takes more than one character.");
        mc("What is a correct syntax to output \"Hello World\" in C#?",
                "Console.WriteLine(\"Hello World\");", "System.out.println(\"Hello World\");", "cout << \"Hello World\";", "print(\"Hello World\");", "Console.WriteLine(\"Hello World\");", "In C#, the output method begins with Console.");
        mc("How do you create a variable with the numeric value 5?", "x = 5;", "num x = 5", "int x = 5;", "double x = 5;", "int x = 5;", "It is a whole number, so use the integer type.");
        mc("How do you create a variable with the floating number 2.8?", "byte x = 2.8", "double x = 2.8D;", "int x = 2.8;", "int x = 2.8D;", "double x = 2.8D;", "You need a type that can hold decimals.");
        mc("Which property can be used to find the length of a string?", "length", "Length", "length()", "getLength()", "Length", "It is a property, not a method, and C# properties start with a capital letter.");
        mc("To declare an array in C#, define the variable type with:", "{}", "[]", "()", "<>", "[]", "It is not curly braces or parentheses.");
        mc("How do you create a method in C#?", "My Method.", "MyMethod()", "myMethod[]", "(MyMethod)", "MyMethod()", "The name is followed by parentheses.");
        mc("Which keyword is used to create a class in C#?", "className", "class", "class()", "MyClass", "class", "It is just the word 'class'.");

        // ---- Identification ----
        id("Identify the control used to display non-editable text on a form.", "Label", "It only shows text. Users cannot type into it.");
        id("Identify the control that users click to perform an action.", "Button", "It raises a Click event.");
        id("Identify the data type used to store true or false values.", "bool|Boolean", "Short for 'Boolean'.");
        id("Identify the loop that executes at least once before checking its condition.", "do-while loop|do-while", "Its condition is checked at the end, after the body runs.");
        id("Identify the reusable block of code designed to perform a task.", "Method", "It is a function that lives inside a class.");
        id("Identify the collection that stores items using numerical indexes.", "Array", "Fixed size and a zero-based index.");
        id("Identify the window that serves as the main interface of a Windows Forms application.", "Form", "It is the window of a Windows Forms app.");
        id("Identify the OOP concept that allows one class to inherit from another.", "Inheritance", "A child class receives the parent's members.");
        id("Identify the process of ensuring entered data meets requirements before it is accepted.", "Input Validation", "Checking data before accepting it.");
        id("Identify the control commonly used to organize related controls into a section.", "Panel", "It is a container that groups controls together.");
        id("Identify the property that determines a control's position on a form.", "Location", "It holds x and y coordinates.");
        id("Identify the special method that runs automatically when an object is created.", "Constructor", "It has the same name as the class.");
        id("Identify the exception that occurs when a user attempts to access an object that is null.", "NullReferenceException", "The name says it: a reference that is null.");
        id("Identify the design principle that keeps an application's appearance and behavior uniform.", "Consistency", "Keep everything looking and behaving the same.");
        id("Identify the OOP principle in which a single reference can represent different object types.", "Polymorphism", "It means 'many forms'.");
        id("Identify the design principle that helps users focus on the most important information first.", "Visual Hierarchy", "The order of importance.");
        id("Identify the feature that allows operators such as + and == to have custom behavior.", "Operator Overloading", "Custom behavior for operators such as + and ==.");
        id("Identify the mechanism in which a method calls itself repeatedly to solve a problem.", "Recursion", "A method that calls itself.");
        id("Identify the class type that cannot be instantiated directly and may contain abstract methods.", "Abstract Class", "It is declared with the abstract keyword.");
        id("Identify the user-interface principle that focuses on making a system efficient, understandable, and easy to use.", "Usability", "How easy something is to use.");
        id("What do you call a named storage location in memory that holds a value?", "variable", "It holds a value that can change.");
        id("What symbol is used to start a single-line comment in C#?", "//", "It is the same symbol used in Java and C++.");
        id("What do you call a block of reusable code that performs a specific task?", "method|function", "It is a reusable block of code that you call by name.");
        id("What do you call the process of finding and fixing errors in a program?", "debugging", "It means finding and fixing bugs.");
        id("What do you call a collection of items of the same type stored under one name?", "array", "Same type, accessed by index.");
        id("What specific keyword is used in a C# switch statement or expression to represent the default or \"fall-through\" match when no other pattern matches?", "Default", "It is the catch-all case when nothing else matches.");
        id("What is the process of converting a value type to a reference type (by wrapping it inside an object instance on the heap) called?", "Boxing", "Think of wrapping something inside a box (an object).");
        id("Which keyword is applied to a class to prevent other classes from inheriting from it?", "sealed", "The class is 'sealed shut' against inheritance.");
        id("What architectural mechanism in .NET manages automatic memory allocation and deallocation by reclaiming objects that are no longer rooted?", "Garbage Collector (GC)|Garbage Collector|GC", "It collects what is no longer being used.");
        id("Introduced in C# 8, what modifier allows you to declare a struct that ensures its properties cannot be modified after initialization, helping the compiler optimize defensive copying?", "readonly", "After initialization, the struct can only be read.");
        id("Which keyword must be placed before a method's return type to allow the use of the await operator inside that method?", "async", "Short for 'asynchronous'.");
        id("What specific syntax structure (often written using a lambda expression =>) allows a method or property to be declared without an explicit block statement or return keyword?", "Expression-bodied member", "It uses => instead of braces.");
        id("What interface must an object implement so that it can be deterministically cleaned up using a using statement or block?", "IDisposable", "The interface name starts with I and has a Dispose method.");
        id("What language feature in C# combines object creation and property assignment into a single, readable statement (e.g., var p = new Person { Name = \"Alex\" };)?", "Object Initializer", "Properties are set inside curly braces right after new.");
        id("What special reference keyword allows an instance method to refer to the exact instance of the class it belongs to?", "this", "It refers to the current instance.");

        // ---- Fill in the Blank ----
        fb("The keyword used to define a class in C# is ____.", "Class", "It is the same word that begins every class declaration.");
        fb("The control commonly used to accept user input is a ____.", "TextBox", "It is a box where text is typed.");
        fb("The method used to display text in the console is Console.____().", "WriteLine", "It prints a line of text.");
        fb("The data type used to store decimal values is ____.", "double|decimal|float", "These types hold fractional numbers.");
        fb("The event that occurs when a button is pressed is the ____ event.", "Click", "It happens when a user clicks.");
        fb("The control used to display an image on a form is ____.", "PictureBox", "It is a box that holds a picture.");
        fb("Every C# statement typically ends with a ____.", "semicolon (;)|semicolon|;", "It looks like ;");
        fb("The property used to change the text displayed on a Button is ____.", "Text", "It holds the text shown on the button.");
        fb("The OOP principle that allows a class to acquire members from another class is ____.", "Inheritance", "The child gets the parent's members.");
        fb("A collection that automatically grows as items are added is called a ____.", "List", "It is a generic collection: List<T>.");
        fb("The process of checking user input before saving data is called ____.", "Validation", "Checking the input first.");
        fb("The keyword used to create an object instance is ____.", "new", "A three-letter keyword.");
        fb("The property used to identify a control in code is ____.", "Name", "It is the property you use to refer to the control in code.");
        fb("The keyword used to define a method that may be overridden is ____.", "virtual", "It allows a method to be overridden.");
        fb("The keyword used in a derived class to replace an inherited method implementation is ____.", "override", "It replaces the base class's method implementation.");
        fb("The exception thrown when invalid text is converted into a number is ____.", "FormatException", "The text has the wrong format.");
        fb("The design principle that arranges elements according to importance is called ____.", "Visual Hierarchy", "Order elements by importance.");
        fb("The OOP principle that hides implementation details is ____.", "Abstraction", "Hide the details, show the essentials.");
        fb("The keyword used to prevent a class from being inherited is ____.", "sealed", "It seals the class so nobody can inherit from it.");
        fb("The practice of maintaining similar layouts, styles, and behavior throughout an application is called ____.", "Consistency", "Uniformity across the application.");
        fb("The ____ keyword is used to declare an integer variable in C#.", "int", "Short for 'integer'.");
        fb("To print a line of text in C#, you use Console.____(\"Hello\");", "WriteLine", "It prints and then moves to the next line.");
        fb("The ____ statement is used to exit a loop immediately.", "break", "It exits the loop immediately.");
        fb("A ____ loop runs a block of code as long as its condition is true.", "while", "It is a condition-based loop, not the counting loop.");
        fb("The ____ data type stores true or false values in C#.", "bool", "Short for 'Boolean'.");
        fb("This data type is used to store whole numbers: ____", "Int", "Whole numbers. Short for 'integer'.");
        fb("This operator is used in an if-else statement to check if two values are not equal: ____", "!=", "The exclamation mark means 'not'.");
        fb("This data type is used to store a true or false value: ____", "Boolean|bool", "True or false.");
        fb("This operator is used to perform addition in C#: ____", "+", "The symbol used for adding.");
        fb("This data type is used to store a single character: ____", "Char", "Short for 'character'.");
        fb("This property indicates the name used in code to identify the object: ____", "(Name)|Name", "The property used to identify the control in code.");
        fb("Write the name of the property that determines the background color of the component ____", "BackColor", "Background color, written as one word.");
        fb("This property specifies the maximum number of characters that can be centered into the edit control: ____", "MaxLength", "It limits how many characters can be typed.");
        fb("This property indicates whether the component should draw right-to-left or RTL language: ____", "RightToLeft", "It is for right-to-left languages such as Arabic.");
        fb("This property indicates the text associated with the control: ____", "Text", "It is the caption shown on the control.");
    }

    private static void js() {
        // ---- Multiple Choice ----
        mc("What will be displayed in the console?\nlet name = \"JavaScript\";\nconsole.log(name);", "name", "JavaScript", "\"name\"", "Error", "JavaScript", "It prints the value stored in the variable.");
        mc("Which keyword is used to declare a variable that can be reassigned?", "const", "let", "static", "define", "let", "const cannot be reassigned. Which keyword can?");
        mc("What is the output?\nconsole.log(10 + 5);", "105", "15", "50", "Error", "15", "Both values are numbers, so + adds them.");
        mc("Which symbol is used for single-line comments in JavaScript?", "\" \"", "//", "#", "/*", "//", "It is the same style used in Java and C++.");
        mc("What is the output?\nlet age = 18;\nconsole.log(age >= 18);", "18", "0", "true", "false", "true", "A comparison gives back true or false.");
        mc("Which method is used to display output in the browser console?", "print()", "console.log()", "display()", "write()", "console.log()", "It begins with console.");
        mc("Which data type stores textual data?", "Number", "String", "Boolean", "Array", "String", "Text values are wrapped in quotes.");
        mc("What is the output?\nlet fruits = [\"Apple\", \"Banana\", \"Orange\"];\nconsole.log(fruits.length);", "2", "3", "4", "Error", "3", "Count the items in the array.");
        mc("What is the output?\nlet x = 7;\nif(x > 5){\n    console.log(\"High\");\n}\nelse{\n    console.log(\"Low\");\n}", "High", "Low", "7", "Error", "High", "Is 7 greater than 5?");
        mc("Which event occurs when a button is pressed?", "submit", "load", "click", "hover", "click", "It fires when the user clicks.");
        mc("What is the output?\nfor(let i = 1; i <= 3; i++){\n    console.log(i);\n}", "0 1 2", "1 2 3", "1 2", "Error", "1 2 3", "It starts at 1 and stops after 3.");
        mc("Which method adds an element to the end of an array?", "pop()", "shift()", "push()", "remove()", "push()", "It adds to the end of an array. pop() removes instead.");
        mc("What is the output?\nlet text = \"Hello\";\nconsole.log(text.toUpperCase());", "hello", "HELLO", "Hello", "Error", "HELLO", "It converts every letter to a capital.");
        mc("Which JavaScript structure stores key-value pairs?", "Array", "Object", "String", "Function", "Object", "It uses curly braces with properties.");
        mc("What is the output?\nlet person = {\n    name: \"Grace\",\n    age: 20\n};\nconsole.log(person.name);", "age", "Grace", "person", "Error", "Grace", "Dot notation reads a property's value.");
        mc("What is the output?\nfunction add(a, b){\n    return a + b;\n}\nconsole.log(add(3, 4));", "34", "7", "12", "Error", "7", "The numbers are added, not joined as text.");
        mc("What is the output?\nlet numbers = [2,4,6];\nnumbers.push(8);\nconsole.log(numbers.length);", "2", "3", "4", "8", "4", "Start with 3 items, then add one more.");
        mc("What is JSON commonly used for?", "Styling web pages", "Creating loops", "Exchanging and storing data", "Compiling code", "Exchanging and storing data", "Think of sending data between a server and a browser.");
        mc("Which JavaScript feature allows one function to be passed into another function?", "Encapsulation", "Inheritance", "Callback Function", "Constructor", "Callback Function", "It is a function passed as an argument to another function.");
        mc("What is the output?\nlet square = (x) => x * x;\nconsole.log(square(5));", "5", "10", "25", "Error", "25", "5 x 5.");

        // ---- Identification ----
        id("Identify the JavaScript data type used to store text.", "String", "Text values go in quotes.");
        id("Identify the structure used to store multiple values in a single variable using indexes.", "Array", "An ordered list whose index starts at 0.");
        id("Identify the browser object that represents the current web page.", "Document", "It represents the loaded web page.");
        id("Identify the event that occurs when a user presses a button.", "Click Event|Click", "It is triggered when something is pressed.");
        id("Identify the statement used to repeatedly execute code while a condition remains true.", "While Loop|While", "It repeats while a condition holds.");
        id("Identify the special value representing an intentional absence of a value.", "Null", "It means intentionally empty.");
        id("Identify the block of reusable code designed to perform a specific task.", "Function", "A reusable block of code.");
        id("Identify the JavaScript structure that stores data as key-value pairs.", "Object", "It uses curly braces with key: value pairs.");
        id("Identify the process of checking whether user input meets expected requirements.", "Validation", "Checking the input.");
        id("Identify the browser API used to interact with HTML elements.", "DOM|Document Object Model", "A tree representation of the HTML page. Three letters.");
        id("Identify the event that occurs when a webpage finishes loading.", "Load Event|Load", "It fires when the page is ready.");
        id("Identify the loop commonly used to repeat a block of code a specific number of times.", "for loop|for", "It has an initializer, a condition, and an increment.");
        id("Identify the method used to add an element to the end of an array.", "push()|push", "It adds an item to the end of an array.");
        id("Identify the function that displays a popup message box to the user.", "alert()|alert", "It shows a popup box.");
        id("Identify the JavaScript concept where functions can be treated as values.", "First-Class Functions", "Functions can be assigned to variables and passed around like any value.");
        id("Identify the programming concept in which a function remembers variables from its outer scope.", "Closure", "The function remembers its outer scope.");
        id("Identify the process of converting data into a format suitable for transmission or storage.", "Serialization", "Turning data into a storable or transmittable format. JSON.stringify does this.");
        id("Identify the JavaScript mechanism used to handle asynchronous operations through .then() and .catch().", "Promise", "It uses .then() and .catch().");
        id("Identify the object used to make network requests in modern JavaScript applications.", "Fetch API|Fetch", "A modern replacement for XMLHttpRequest.");
        id("Identify the JavaScript feature that allows one class to inherit properties and methods from another class.", "Inheritance", "Classes use the extends keyword for this.");

        // ---- Fill in the Blank ----
        fb("The keyword used to declare a variable that can be reassigned is ____.", "let", "It is block-scoped and can be reassigned.");
        fb("The method used to display output in the browser console is ____().", "console.log", "It prints to the console.");
        fb("The data type used to store true or false values is ____.", "Boolean", "True or false.");
        fb("The symbol used for single-line comments in JavaScript is ____.", "//", "It is the same style used in Java and C++.");
        fb("The keyword used to declare a constant variable is ____.", "const", "Short for 'constant'.");
        fb("A collection of multiple values stored in a single variable is called an ____.", "Array", "An ordered list.");
        fb("The HTML document loaded in a browser can be accessed through the ____ object.", "document", "It represents the web page.");
        fb("The keyword used to define a function is ____.", "function", "This keyword defines a function.");
        fb("The JavaScript event commonly triggered when a button is pressed is ____.", "click", "A mouse press event.");
        fb("The method used to convert a JSON string into a JavaScript object is ____().", "JSON.parse", "It turns a string into an object.");
        fb("The property used to access the number of elements in an array is ____.", "length", "It is a property, so there are no parentheses.");
        fb("An object is composed of properties and ____.", "methods", "Functions inside an object.");
        fb("The statement used to make decisions based on conditions is ____.", "if", "It is the conditional keyword.");
        fb("A function stored inside an object is called a ____.", "method", "A function that belongs to an object.");
        fb("The JavaScript feature that allows functions to be passed as arguments is called a ____ function.", "callback", "It is called back later.");
        fb("The concise syntax introduced in ES6 for writing functions is called an ____ function.", "arrow", "It uses =>.");
        fb("The method used to convert a JavaScript object into a JSON string is ____().", "JSON.stringify", "It turns an object into a string.");
        fb("The process of requesting data from a server without reloading the page is commonly known as ____.", "AJAX", "Asynchronous JavaScript and XML.");
        fb("The keyword used inside an object to refer to the current object is ____.", "this", "A four-letter keyword that refers to the current object.");
        fb("The mechanism that allows objects to inherit properties from other objects is called the ____ chain.", "prototype", "Objects link to each other through it.");
    }
}
