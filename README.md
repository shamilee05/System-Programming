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
