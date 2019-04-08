# System-Programming
Java programs to implement system programs like assembler and macro pre-processor.

### 1. Two Pass Assembler:
* An assembler is basically a translator that converts an assembly-level language program into a machine-level language program.
* A Two Pass assembler specifically scans the entire program twice- 
  * During the first pass, it stores addresses of all the symbolic labels.
  * During the second pass, it actually translates the opcode and operands into machine-level language code.
  
An instance of how the program works is as shown below:

The input program to the Two Pass Assembler is:

![image alt](https://github.com/shamilee05/System-Programming/blob/master/2-Pass%20Assembler/Input%20to%20Pass%201.png)

The output from the 1st Pass which will be fed as input for the 2nd Pass will be:

![image alt](https://github.com/shamilee05/System-Programming/blob/master/2-Pass%20Assembler/Input%20to%20Pass%202.png)

The final output from the 2nd Pass will be:

![image alt](https://github.com/shamilee05/System-Programming/blob/master/2-Pass%20Assembler/Output.png)

The various data structures used by the assembler include the MOT, POT, Symbol table, Literal table and the Base Table, all of 
which are as shown below:

![image alt](https://github.com/shamilee05/System-Programming/blob/master/2-Pass%20Assembler/Assembler.png)

*The program uses bubble sort to sort the MOT and POT, binary search to search through the MOT and POT and linear search to search through the Symbol table and Literal table.*

<br>

### 2. Macro Processor:
* A macro processor works in conjunction with the assembler and basically processes macros. It works in 2 passes wherein-
  * During the first pass, it **stores the definitions** of all the macros.
  * During the second pass, it **recognizes and replaces all the macro calls** with their **respective definitions**.
 
The program works for all types of arguments - **positional, keyword and default**. Instances of how it works in these different cases are as shown below *(Also shown are the contents of the various data structures used in the process like the MNT, MDT and the ALA for each of the macros):

 1. Positional arguments:
 
 ![image alt]( https://github.com/shamilee05/System-Programming/blob/master/Macro%20Pre-processor/Positional.png)
 
 <br>
 
 2. Keyword arguments:
 
 ![image alt]( https://github.com/shamilee05/System-Programming/blob/master/Macro%20Pre-processor/Keyword.png)
 
 <br>
 
 3. Default arguments:
 
 ![image alt]( https://github.com/shamilee05/System-Programming/blob/master/Macro%20Pre-processor/Default.png)

 
 
