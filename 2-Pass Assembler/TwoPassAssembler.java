/*input to pass 1: filename: assembler_input_pass1.txt		output: input_pass2
input to pass2: input_pass2.txt		output:output_pass2.txt*/
import java.util.*;
import java.io.*;

class MOT
{
	String mnemonic,type,bin_opcode;
	int length;
}

class POT
{
	String pseudo;	
}

class Base_Table
{
	int reg_no,value;
}

class Symbol
{
	String symbol_name;
	int value,length;
	char A_R;	
}

class Literal
{
	String literal_name;
	int value,length;
	char A_R;
	
	//To check if the value of the literal has been assigned or not
	boolean value_allotted=false;
}

class TwoPassAssembler
{
	public static void main(String args[]) throws Exception
	{
		int i,j,k,count,ind_1,ind_2,L,LC=0,sym_id,lit_no,count_literals=0;
		boolean label_present=false,ltorg_appeared=false;
		String line,file_name;
		String input_pass2="input_pass2.txt";
		String output_pass2="output_pass2.txt";
		
		Vector<POT> pot=new Vector<POT>();
		Vector<MOT> mot=new Vector<MOT>();
		Vector<Symbol> sym=new Vector<Symbol>();
		Vector<Literal> lit=new Vector<Literal>();
		
		POT p1=new POT();	p1.pseudo="START";	pot.add(p1);
		POT p2=new POT();	p2.pseudo="END";	pot.add(p2);
		POT p3=new POT();	p3.pseudo="USING";	pot.add(p3);
		POT p4=new POT();	p4.pseudo="LTORG";	pot.add(p4);
		POT p5=new POT();	p5.pseudo="DROP";	pot.add(p5);
		POT p6=new POT();	p6.pseudo="EQU";	pot.add(p6);
		POT p7=new POT();	p7.pseudo="DC";		pot.add(p7);
		POT p8=new POT();	p8.pseudo="DS";		pot.add(p8);
		
		MOT m1=new MOT();	m1.mnemonic="LA";	m1.type="RX";	m1.length=4;	m1.bin_opcode="01";	mot.add(m1);
		MOT m2=new MOT();	m2.mnemonic="L";	m2.type="RX";	m2.length=4;	m2.bin_opcode="58";		mot.add(m2);
		MOT m3=new MOT();	m3.mnemonic="SR";	m3.type="RR";	m3.length=2;	m3.bin_opcode="1B";		mot.add(m3);
		MOT m4=new MOT();	m4.mnemonic="AR";	m4.type="RR";	m4.length=2;	m4.bin_opcode="1A";		mot.add(m4);
		MOT m5=new MOT();	m5.mnemonic="A";	m5.type="RX";	m5.length=4;	m5.bin_opcode="5A";		mot.add(m5);
		MOT m6=new MOT();	m6.mnemonic="LR";	m6.type="RR";	m6.length=2;	m6.bin_opcode="18";		mot.add(m6);
		MOT m7=new MOT();	m7.mnemonic="BR";	m7.type="RR";	m7.length=2;    m7.bin_opcode="07";		mot.add(m7);
		MOT m8=new MOT();	m8.mnemonic="C";	m8.type="RX";	m8.length=4;    m8.bin_opcode="59";		mot.add(m8);
		MOT m9=new MOT();	m9.mnemonic="BNE";	m9.type="RX";	m9.length=4;    m9.bin_opcode="47";		mot.add(m9);
		MOT m10=new MOT();	m10.mnemonic="ST";	m10.type="RX";	m10.length=4;   m10.bin_opcode="50";	mot.add(m10);
		
		//Bubble sort to sort POT
		for(i=0;i<(pot.size());i++)
		{
    		for(j=0;j<(pot.size()-1);j++) 
    	  	{
    	  		POT ptemp1=pot.elementAt(i); 
    	  		POT ptemp2=pot.elementAt(j); 
    	  		POT ptemp=new POT();
    	  		if(((ptemp1.pseudo).compareTo(ptemp2.pseudo))<0) 
    	  		{
    	  			ptemp.pseudo=ptemp1.pseudo;
    	  			ptemp1.pseudo=ptemp2.pseudo;
    	  			ptemp2.pseudo=ptemp.pseudo;
    	  		}
      		}
    	}
    	
    	//Bubble sort to sort MOT
		for(i=0;i<(mot.size());i++)
		{
    		for(j=0;j<(mot.size()-1);j++) 
    	  	{
    	  		MOT mtemp1=mot.elementAt(i); 
    	  		MOT mtemp2=mot.elementAt(j); 
    	  		MOT mtemp=new MOT();
    	  		if(((mtemp1.mnemonic).compareTo(mtemp2.mnemonic))<0) 
    	  		{
    	  			mtemp.mnemonic=mtemp1.mnemonic;       mtemp1.mnemonic=mtemp2.mnemonic;      mtemp2.mnemonic=mtemp.mnemonic;
    	  			mtemp.type=mtemp1.type;               mtemp1.type=mtemp2.type;	            mtemp2.type=mtemp.type;
    	  			mtemp.bin_opcode=mtemp1.bin_opcode;	  mtemp1.bin_opcode=mtemp2.bin_opcode;  mtemp2.bin_opcode=mtemp.bin_opcode;
    	  			mtemp.length=mtemp1.length;		      mtemp1.length=mtemp2.length;		    mtemp2.length=mtemp.length;
    	  		}
    	  	}
    	}	
		
		//Displaying POT
		System.out.println("POT:\nPseudo-assembler directive");
		for(i=0;i<pot.size();i++)
		{
			POT ptemp=pot.elementAt(i);
			System.out.println(ptemp.pseudo);
		}
		System.out.println();
	
		//Displaying MOT
		System.out.println("MOT:\nMnemonic   Type      Length   Binary Opcode");
		for(i=0;i<mot.size();i++)
		{
			MOT mtemp=mot.elementAt(i);
			System.out.println(mtemp.mnemonic+"\t    "+mtemp.type+"\t\t"+mtemp.length+"\t "+mtemp.bin_opcode);
		}
		System.out.println();
		
		System.out.println("Pass 1:");

		//Reading the name of the input file from the user
		System.out.println("Enter the name of the input file:");
		
		InputStreamReader isr=new InputStreamReader(System.in);
		BufferedReader br=new BufferedReader(isr);
		
		file_name=br.readLine();

		File new_file=new File(file_name); 
        FileInputStream fileStream=new FileInputStream(new_file); 
        InputStreamReader input=new InputStreamReader(fileStream); 
        BufferedReader reader=new BufferedReader(input);
        
        //Reading the input file line-by-line
        while((line=reader.readLine())!=null) 
        {
        	i=0;
        	StringTokenizer st1=new StringTokenizer(line," ",false);
        	String tokens[]=new String[st1.countTokens()];
        	String opcode="";	label_present=false;
        	
        	//Checking if there is a label present or not
        	while(st1.hasMoreTokens())
        	{
        		tokens[i]=st1.nextToken();
        		i++;
        	}
        	
        	//Checking for different kinds of instructions
        	if(tokens.length==1)
        	{
        		opcode=tokens[0];
        	}
        	else if(tokens.length==3)
        	{
        		label_present=true;
        		opcode=tokens[1];
        	}
        	else if(tokens.length==2)
        	{
        		opcode=tokens[0];
        	}

        	//Checking if the opcode is present in the POT or not
        	ind_1=bin_search_POT(pot,0,pot.size()-1,opcode);
         	if(ind_1==-1)
         	{
         		//Opcode has not been found in POT, next check in MOT
         		ind_2=bin_search_MOT(mot,0,mot.size()-1,opcode);
         		
         		//Opcode has been found in MOT, retrieve it from MOT
 				MOT mtemp=mot.elementAt(ind_2);
 						
 				//The length of the instruction is assigned to 'L'
 				L=mtemp.length;
 						
 				//Add a new entry to the literal table with just the name,type and length (leaving the value)
 				StringTokenizer st2;
 				if(label_present==true)
 				{
 					st2=new StringTokenizer(tokens[2],",",false);
 				}
 				else
 				{
 					st2=new StringTokenizer(tokens[1],",",false);
 				}
 				while(st2.hasMoreTokens())
 				{
 					String check_for_literal=st2.nextToken();
 					char check[]=check_for_literal.toCharArray();
 				
 					if(check[0]=='=')
 					{
 						Literal new_lit=new Literal();
 						new_lit.literal_name=check_for_literal.substring(1);
 						new_lit.length=L;
 						new_lit.A_R='R';
 						lit.add(new_lit);
 					}
 				}
 						
 				//Add a new entry to the symbol table, if any symbols present
 				if(label_present==true)
 				{
 					Symbol new_sym=new Symbol();
         			new_sym.symbol_name=tokens[0];	new_sym.value=LC;	new_sym.length=L;	new_sym.A_R='R';
         			sym.add(new_sym);
 				}
 						
 				LC=LC+L;
         	}
         		
         	//If the opcode has been found in the POT
         	else
         	{
         		//If it is "START", just initialise the LC with the corresponding value 
         		if(opcode.equals("START"))
         		{
         			LC=0;
         			if(label_present==true)
         			{
         				Symbol new_sym=new Symbol();
         				new_sym.symbol_name=tokens[0];	new_sym.value=LC;	new_sym.length=1;	new_sym.A_R='R';
         				sym.add(new_sym);
         			}
         		}	
         		
         		//If it is "DS" or "DC", find out the length of the data field, assign it to L, increment LC by L
         		else if(opcode.equals("DS")||opcode.equals("DC"))
         		{	
         			String data_field_length;
         			while(LC%4!=0)
         			{
         				LC++;
         			}
         			if(label_present==true)
         			{
         				Symbol new_sym=new Symbol();
         				new_sym.symbol_name=tokens[0];	new_sym.value=LC;	new_sym.length=4;	new_sym.A_R='R';
         				sym.add(new_sym);
         				data_field_length=tokens[2];
         			}
         			else
         			{
         				data_field_length=tokens[1];
         			}
         			
         			count=0;
         			if(data_field_length.charAt(data_field_length.length()-1)=='F')
         			{
         				count=Integer.parseInt(data_field_length.substring(0,data_field_length.length()-1));
         			}
         			else
         			{
         				StringTokenizer st3=new StringTokenizer(data_field_length,",",false);
         				count=st3.countTokens();	
         			}
         			L=count*4;
         			LC=LC+L;
         		}
         		
         		//If it is "EQU", check for symbols
         		else if(opcode.equals("EQU"))
         		{
         			//Add a new entry to symbol table
         			Symbol new_sym=new Symbol();
         			new_sym.symbol_name=tokens[0];	new_sym.length=1;	new_sym.A_R='A';
         			if(tokens[2]=="*")
         			{
         				new_sym.value=LC;
         			}
         			else
         			{
         				new_sym.value=Integer.parseInt(tokens[2]);
         			}
         			sym.add(new_sym);
         		}
         		
         		//If its is "LTORG", assign values to literals
         		else if(opcode.equals("LTORG"))
         		{
         			while(LC%8!=0)
         			{
         				LC++;
         			}
         			for(k=0;k<lit.size();k++)
         			{
         				Literal lit_temp=lit.elementAt(k);
         				lit_temp.value=LC;
         				lit_temp.value_allotted=true;
         				LC=LC+lit_temp.length;
         				count_literals++;
         			}
         			ltorg_appeared=true;
         		}
         		
         		//Assign values to literals when "END" appears
         		else if(opcode.equals("END"))
         		{
         			//If "LTORG" has not appeared at all
         			if(ltorg_appeared==false)
         			{
         				while(LC%8!=0)
         				{
         					LC++;
         				}
         				for(k=0;k<lit.size();k++)
         				{
         					Literal lit_temp=lit.elementAt(k);
         					lit_temp.value=LC;
         					LC=LC+lit_temp.length;
         				}
         			}
         			
         			//If "LTORG" appears but not all literals have been assigned values
         			else if(count_literals!=lit.size())
         			{
         				while(LC%8!=0)
         				{
         					LC++;
         				}
         				for(k=0;k<lit.size();k++)
         				{
         					Literal lit_temp=lit.elementAt(k);
         					if(lit_temp.value_allotted==false)
         					{
         						lit_temp.value=LC;
         						LC=LC+lit_temp.length;
         					}
         				}
         			}
         		}
         	}
        }
        
        //Generating the intermediate code
        char delimiters[]={',',' '};
        String delim=new String(delimiters);
        File new_file1=new File(file_name); 
        FileInputStream fileStream1=new FileInputStream(new_file1); 
        InputStreamReader input1=new InputStreamReader(fileStream1); 
        BufferedReader reader1=new BufferedReader(input1);
        while((line=reader1.readLine())!=null) 
        {	
        	StringTokenizer st4=new StringTokenizer(line," ",false);
        	label_present=false;
        	String new_line="";
        	if(st4.countTokens()==3)
        	{
        		label_present=true;		String label_not_imp=st4.nextToken();
        	}
        	String without_label="";
        	while(st4.countTokens()!=0)
        	{
        		without_label=without_label+st4.nextToken()+" ";
        	}
        	StringTokenizer st5=new StringTokenizer(without_label,delim,true);
        	while(st5.hasMoreTokens())
        	{
        		String check_tables=st5.nextToken();	
        			
        		if((sym_id=symbol_search(sym,check_tables))!=-1)
        		{
        			new_line=new_line+"ID#"+Integer.toString(sym_id);
        		}
        		else if((lit_no=literal_search(lit,check_tables.substring(1)))!=-1)
        		{
        			new_line=new_line+"LT#"+Integer.toString(lit_no);
        		}
        		else
        		{
        			new_line=new_line+check_tables;
        		}
        	}
        	if(!new_line.contains("EQU") && !new_line.contains("LTORG"))
        	{
        		writeToFile(input_pass2,new_line);
        	}
        }
    
        //Displaying the Symbol Table
     	System.out.println("Symbol Table:\nName      Value       Length   A/R");
		for(i=0;i<sym.size();i++)
		{
			Symbol stemp=sym.elementAt(i);
			System.out.println(stemp.symbol_name+"\t    "+stemp.value+"\t\t"+stemp.length+"\t"+stemp.A_R);
		}
		System.out.println();
		
		//Displaying the Literal Table
     	System.out.println("Literal Table:\nName      Value        Length   A/R");
		for(i=0;i<lit.size();i++)
		{
			Literal ltemp=lit.elementAt(i);
			System.out.println(ltemp.literal_name+"\t    "+ltemp.value+"\t\t"+ltemp.length+"\t"+ltemp.A_R);
		}
		System.out.println();
		
		System.out.println("Pass 2:");
		
		//Initialising LC with value '0'
		LC=0;
		String mnem,mnem_type,bin_op,opcode;
		Vector<Base_Table> base=new Vector<Base_Table>();

		file_name=input_pass2;

		File new_file2=new File(file_name); 
        FileInputStream fileStream2=new FileInputStream(new_file2); 
        InputStreamReader input2=new InputStreamReader(fileStream2); 
        BufferedReader reader2=new BufferedReader(input2);
        
        //Reading the input file line-by-line
        while((line=reader2.readLine())!=null) 
        {
        	i=0;	String new_line1="";
        	StringTokenizer st1=new StringTokenizer(line," ",false);
        	String tokens2[]=new String[st1.countTokens()];
        	while(st1.hasMoreTokens())
        	{
        		tokens2[i]=st1.nextToken();
        		i++;
        	}
        	
        	//Retrieve the opcode from the instruction
        	opcode=tokens2[0];
        	
        	//Checking if the opcode is present in the POT or not
        	ind_1=bin_search_POT(pot,0,pot.size()-1,opcode);
         	if(ind_1==-1)
         	{
         		//Opcode has not been found in POT, next check in MOT
         		ind_2=bin_search_MOT(mot,0,mot.size()-1,opcode);
         		
         		//Opcode has been found in MOT, retrieve it from MOT
 				MOT mtemp=mot.elementAt(ind_2);
 				
 				mnem_type=mtemp.type;		bin_op=mtemp.bin_opcode;		L=mtemp.length;		mnem=mtemp.mnemonic;
 				if(mnem_type.equals("RR"))
 				{
 					//If it is "BR", replace with "BCR"
 					if(mnem.equals("BR"))
 					{
 						new_line1="BCR 15,";
 					}
 					else
 					{
 						new_line1=tokens2[0]+" ";
 					}
 					StringTokenizer st2=new StringTokenizer(tokens2[1],",",true);
 					while(st2.hasMoreTokens())
 					{
 						//Find out the values of the registers for "RR"
 						String register=st2.nextToken();	
 						if(register.charAt(0)=='I')
 						{
 							int pos=Character.getNumericValue(register.charAt(3));	
 							Symbol stemp1=sym.elementAt(pos-1);
 							new_line1=new_line1+stemp1.value;
 						}
 						else
 						{
 							new_line1=new_line1+register;
 						}
 					}
 					writeToFile(output_pass2,new_line1);
 					
 					LC=LC+L;
 				}
 				
 				else if(mnem_type.equals("RX"))
 				{
 					//If it is "BNE", replace with "BC"
 					if(mnem.equals("BNE"))
 					{
 						new_line1="BC 7,";
 					}
 					else
 					{
 						new_line1=tokens2[0]+" ";
 					}
 					StringTokenizer st2=new StringTokenizer(tokens2[1],",",true);
 					while(st2.hasMoreTokens())
 					{
 						//Find out the effective address, displacement and display disp(value,register no.)
 						String operand=st2.nextToken();
 						Base_Table breg=base.elementAt(0);
 						if(operand.charAt(0)=='I')
 						{
 							int pos=Character.getNumericValue(operand.charAt(3));
 							Symbol stemp1=sym.elementAt(pos-1);
 							int EA=stemp1.value;
 							int disp=EA-breg.value;
 							new_line1=new_line1+Integer.toString(disp)+"("+Integer.toString(breg.value)+","+Integer.toString(breg.reg_no)+")";
 						}
 						else if(operand.charAt(0)=='L')
 						{
 							int pos=Character.getNumericValue(operand.charAt(3));
 							Literal ltemp1=lit.elementAt(pos-1);
 							int EA=ltemp1.value;
 							int disp=EA-breg.value;
 							new_line1=new_line1+Integer.toString(disp)+"("+Integer.toString(breg.value)+","+Integer.toString(breg.reg_no)+")";
 						}
 						else
 						{
 							new_line1=new_line1+operand;
 						}
 					}
 					writeToFile(output_pass2,new_line1);
 					
 					LC=LC+L;
 				}
         	}
         	
         	//Opcode has been found in the POT
         	else
         	{
         		if(opcode.equals("DS")||opcode.equals("DC"))
         		{	
         			String data_field_length=tokens2[1];
         			while(LC%4!=0)
         			{
         				LC++;
         			}
         			
         			//If it is "DC", insert respective constants in code
         			if(opcode.equals("DC"))
         			{
         				int constant[]=new int[8];	i=7;
         				int num=Integer.parseInt(data_field_length.substring(2,(data_field_length.length()-1)));
         				while(num!=0)
         				{
         					constant[i]=num%10;
         					num=num/10;
         					i--;
         				}
         				new_line1="X'";	i=0;
         				while(i<=7)
         				{
         					new_line1=new_line1+Integer.toString(constant[i]);
         					i++;
         				}
         				new_line1=new_line1+"'";
         				writeToFile(output_pass2,new_line1);
         			}
         			
         			count=0;
         			if(data_field_length.charAt(data_field_length.length()-1)=='F')
         			{
         				count=Integer.parseInt(data_field_length.substring(0,data_field_length.length()-1));
         			}
         			else
         			{
         				StringTokenizer st3=new StringTokenizer(data_field_length,",",false);
         				count=st3.countTokens();	
         			}
         			L=count*4;
         			LC=LC+L;
         		}
         		
         		//If it is "USING", make a new entry in the Base Table
         		else if(opcode.equals("USING"))
         		{
         			Base_Table bt=new Base_Table();
         			if(tokens2[1].charAt(0)=='*')
         			{
         				bt.value=LC;
         				bt.reg_no=Integer.parseInt(tokens2[1].substring(2));
         				base.add(bt);
         			}
         			else
         			{
         				StringTokenizer st3=new StringTokenizer(tokens2[1],",",false);
         				bt.value=Integer.parseInt(st3.nextToken());
         				bt.reg_no=Integer.parseInt(st3.nextToken());
         				base.add(bt);
         			}
         		}
         		
         		//If it is "END", insert literals from literal table into the code
         		else if(opcode.equals("END"))
         		{
         			for(i=0;i<lit.size();i++)
         			{
         				int constant[]=new int[8];	j=7;
         				Literal ltemp=lit.elementAt(i);
         				int num=Integer.parseInt((ltemp.literal_name).substring(2,(ltemp.literal_name).length()-1));
         				while(num!=0)
         				{
         					constant[j]=num%10;
         					num=num/10;
         					j--;
         				}
         				new_line1="X'";	i=0;
         				while(i<=7)
         				{
         					new_line1=new_line1+Integer.toString(constant[i]);
         					i++;
         				}
         				new_line1=new_line1+"'";
         				writeToFile(output_pass2,new_line1);
         			}
         		}
         	}
        }
        
        System.out.println("Base Table:\nRegister No.         Value");
		for(i=0;i<base.size();i++)
		{
			Base_Table btemp=base.elementAt(i);
			System.out.println(btemp.reg_no+"\t\t\t"+btemp.value);
		}
		System.out.println();
    }
    
    //Recursive binary search function to search the token in POT
	public static int bin_search_POT(Vector<POT> pot,int first,int last,String token)
	{
        if(last>=first) 
        { 
            int mid=first+(last-first)/2;
            POT ptemp=pot.elementAt(mid);
  
            if((ptemp.pseudo).equals(token))
            { 
                return mid;
  			}
  			
            if(token.compareTo(ptemp.pseudo)<0) 
            {
                return bin_search_POT(pot,first,mid-1,token);
            }
            
            return bin_search_POT(pot,mid+1,last,token); 
        }
        return -1;
	}
	
	//Recursive binary search function to search the token in MOT
	public static int bin_search_MOT(Vector<MOT> mot,int first,int last,String token)
	{
        if(last>=first) 
        { 
            int mid=first+(last-first)/2;
            MOT mtemp=mot.elementAt(mid);
  
            if((mtemp.mnemonic).equals(token))
            { 
                return mid;
  			}
  			
            if(token.compareTo(mtemp.mnemonic)<0)
            {
                return bin_search_MOT(mot,first,mid-1,token);
            }
  
            return bin_search_MOT(mot,mid+1,last,token); 
        } 
        return -1;
	}
	
	public static int symbol_search(Vector<Symbol> symbol,String token)
	{
		for(int i=0;i<symbol.size();i++)
		{
			Symbol stemp=symbol.elementAt(i);
			if((stemp.symbol_name).equals(token))
			{
				return (symbol.indexOf(stemp)+1);
			}
		}
		
		return -1;
	}
	
	public static int literal_search(Vector<Literal> literal,String token)
	{
		for(int i=0;i<literal.size();i++)
		{
			Literal ltemp=literal.elementAt(i);
			if((ltemp.literal_name).equals(token))
			{
				return (literal.indexOf(ltemp)+1);
			}
		}
		
		return -1;
	}
	
	public static void writeToFile(String file,String text) 
  	{
    	try 
    	{
     		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(file),true));
      		bw.write(text);
      		bw.newLine();
      		bw.close();
    	} 
    	catch(Exception e){}
  	}
}