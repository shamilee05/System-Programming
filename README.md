# System-Programming
Java programs to implement system programs like assembler and macro pre-processor.

### 1. Two Pass Assembler:
* An assembler is basically a translator that converts an assembly-level language program into a machine-level language program.
* A Two Pass assembler specifically scans the entire program twice- 
  * During the first pass, it stores addresses of all the symbolic labels.
  * During the second pass, it actually translates the opcode and operands into machine-level language code.
  
An instance of how the program works is as shown below:
