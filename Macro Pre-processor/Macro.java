/*input for Pass 1:file_name, output of Pass1:input_pass2
input for Pass 2:input_pass2, output of Pass2:output_pass2*/
import java.util.*;
import java.io.*;

class MNT_Contents
{
  int index,mdt_index;
  String macro_name;
}

class Macro
{
  public static void main(String[] args) throws Exception
  {
    String file_name,line;
    int i=0,j,mntc=0,mdtc=0,count_args=0,arg_pos,total_tokens,macro_count=0;
    
    //For default arguments; storing the no. of args in an array to later compare with the macro call
    //Also storing whether an argument has a default value or not
    int no_of_args[]=new int[100];
    boolean has_default[][]=new boolean[100][100];
    
    char delimiters[]={' ',','};
    String delim=new String(delimiters);
    
    ArrayList<String> MDT=new ArrayList<String>();
    Vector<MNT_Contents> MNT=new Vector<MNT_Contents>();
    String ALA[][]=new String[100][];
    
    System.out.println("PASS 1:");
    String input_pass2="input_pass_2.txt";
    System.out.println("Enter the name of the input file:");
    
    InputStreamReader isr=new InputStreamReader(System.in);
    BufferedReader br=new BufferedReader(isr);
    
    file_name=br.readLine();
    File new_file=new File(file_name); 
    FileInputStream fileStream=new FileInputStream(new_file); 
    InputStreamReader input=new InputStreamReader(fileStream); 
    BufferedReader reader=new BufferedReader(input);

    while((line=reader.readLine())!=null)
    {
      if(line.equals("MACRO"))
      {
      
        //Read the line containing the name of the macro
        line=reader.readLine();
        MNT_Contents m1=new MNT_Contents();
        
        //Check if there is a label present
        int label_case=0;
        StringTokenizer st1=new StringTokenizer(line,delim,false);
        total_tokens=st1.countTokens();
        String check_label=st1.nextToken();
        if(check_label.charAt(0)=='&')
        {
          label_case=1;
          count_args=total_tokens-2; //No. of tokens-2 (one will be label and the other would be the name of the macro)
          m1.macro_name=st1.nextToken();
        }
        if(label_case==0)
        {
          count_args=total_tokens-1; //No. of tokens-1 (the name of the macro)
          m1.macro_name=check_label;
        }
        
        //Make an entry in MNT
        m1.index=mntc;  m1.mdt_index=mdtc;  MNT.add(mntc,m1);  mntc++;
        
        //Preparation of ALA 
        ALA[i]=new String[count_args+1];
        ALA[i][0]="bbbb";
        j=1;
        
        if(label_case==1) //If there is a label present
        {
          ALA[i][0]=check_label;
        }
        
        while(st1.hasMoreTokens())
        {
          //Check for default arguments, if present, just select the argument, DO NOT STORE THE DEFAULT ARGUMENT VALUE ANYWHERE IN PASS 1
          String check_for_default=st1.nextToken();
          int k=0;
          while(k<check_for_default.length())
          {
          	if(check_for_default.charAt(k)=='=')
          	{
            	break;
            }
            k++;
          }
          ALA[i][j]=check_for_default.substring(0,k);
          j++;
        }
        
        //Adding macro name line in MDT
        MDT.add(mdtc,line);  mdtc++;
        
        //Replacing arguments with positional indicators
        do
        {
          if((line=reader.readLine())!=null)
          {
            if(label_case==0 && line.charAt(0)=='&')
            {
              label_case=2; //There is a label, but it will be passed in the arguments list only
            }
          	String new_line_args_replaced="";
          	StringTokenizer st2=new StringTokenizer(line,delim,true);
          	while(st2.hasMoreTokens())
          	{
          	  String to_replace=st2.nextToken();
          	  if(to_replace.charAt(0)=='&')
          	  {
          	    if(label_case==2)
          	    {
          	      //Include label in ALA, count_args was initially counting label as well so decrement it
          	      ALA[i][0]=to_replace;
          	      ALA[i][count_args]=null;
          	      count_args--;
          	      label_case=1;
          	    }
          	    
          	    //Calling function to search through ALA for that argument
          	    arg_pos=linear_search_ALA(ALA[i],count_args+1,to_replace);
          	    String arg_replaced="#"+Integer.toString(arg_pos);
          	    new_line_args_replaced+=arg_replaced;
          	  }
          	  else
          	  {
          	    //If not an argument, no replacement needed
          	  	new_line_args_replaced+=to_replace;
          	  }
          	}
          	
          	//Add the new line with all replaced arguments in MDT
          	MDT.add(mdtc,new_line_args_replaced);  mdtc++;
          }
        }
        while(!line.equals("MEND"));
        
        //Storing the no. of arguments for default arguments
        no_of_args[i]=count_args;
        
        //End of macro definition
        i++;
        macro_count=i;
      }
      else
      {
        writeToFile(input_pass2,line);
        if(line=="END")
        {
          break;
        }
      }
    }
    
    System.out.println();
    System.out.println("Contents of MNT:");
    System.out.println("Index  Macro_Name  MDT_Index");
    for(i=0;i<MNT.size();i++)
    {
      MNT_Contents mtemp=MNT.elementAt(i);
      System.out.println("  "+mtemp.index+"      "+mtemp.macro_name+"      "+mtemp.mdt_index);
    }
    System.out.println();
    
    System.out.println("Contents of MDT:");
    System.out.println("Index        Source_Card");
    for(i=0;i<MDT.size();i++)
    {
      System.out.println("  "+i+"       "+MDT.get(i));
    }
    System.out.println();
    
    System.out.println("Contents of ALA:");
    for(i=0;i<macro_count;i++)
    {
      System.out.println("ALA for the macro "+(MNT.elementAt(i)).macro_name+":");
       for(j=0;j<ALA[i].length;j++)
       {
         if(ALA[i][j]!=null)
         {
          System.out.println(j+"  "+ALA[i][j]);
         }
       }
       System.out.println();
     }

    System.out.println("PASS 2:");
    int mdtp,mnt_for_ref_ala=-1;
    String temp,output_pass2="final_output.txt";
    
    File new_file_1=new File(input_pass2);
    FileInputStream fileStream_1=new FileInputStream(new_file_1); 
    InputStreamReader input_1=new InputStreamReader(fileStream_1); 
    BufferedReader reader_1=new BufferedReader(input_1);
    
    while((line=reader_1.readLine())!=null)
    {
      mdtp=-1;
      StringTokenizer st3=new StringTokenizer(line,delim,false);
      search_for_macro:
      while(st3.hasMoreTokens())
      {
        String check_if_macro=st3.nextToken();
        for(i=0;i<MNT.size();i++)
        {
          MNT_Contents mtemp=MNT.elementAt(i);
          
          //Check if it matches any of the macro names
          if((mtemp.macro_name).equals(check_if_macro))
          {
            //Initialise mdtp with mdtc from corresponding MNT entry
            mdtp=mtemp.mdt_index;  mnt_for_ref_ala=mtemp.index;
            break search_for_macro; //labelled break will have the loop stop when a macro has been found
          }
        }
      }
      
      /*No macro found in this line*/
      if(mdtp==-1)
      {
        writeToFile(output_pass2,line);
        if(line.equals("END"))
        {
          break;
        }
        else
        {
          continue;
        }
      }
      
      //Setting up ALA for the macro
      else
      {
        i=0;
        StringTokenizer st4=new StringTokenizer(line," ",false);
        
        //There is a label present before the macro name
        if(st4.countTokens()==3)
        {
    	  ALA[mnt_for_ref_ala][i]=st4.nextToken();
    	  i++;
    	  
    	  //Store the macro name in some temporary variable, it is not actually required
    	  temp=st4.nextToken();
    	  
    	  StringTokenizer st5=new StringTokenizer(st4.nextToken(),",",false);
    	  {
    	  	j=i;
    	  	
    	  	//Setting up of default arguments
    	  	StringTokenizer str1=new StringTokenizer(MDT.get(mdtp),delim,false);
    	  	String label=str1.nextToken();		String macro_name1=str1.nextToken();

    	  	while(str1.hasMoreTokens())
    	  	{
    	  		String arg=str1.nextToken();
    	  		if(arg.contains("="))
    	  		{
    	  			ALA[mnt_for_ref_ala][i]=arg.substring((arg.indexOf("=")+1));
    	  			has_default[mnt_for_ref_ala][i]=true;
    	  		}
    	  		i++;
    	  	}
    	  	
    	  	i=j;
    	  	
    	    while(st5.hasMoreTokens())
    	    {
    	      String if_keyword_arg=st5.nextToken();
    	      if(has_default[mnt_for_ref_ala][i]==false)
    	      {
    	      	//Checking for keyword arguments
    	      	if(if_keyword_arg.contains("="))
    	      	{
    	      		String value_key=if_keyword_arg.substring(if_keyword_arg.lastIndexOf("=")+1);
    	      		int count_key_args=0;	int k=0;
    	      		while(k<if_keyword_arg.length())
    	      		{
    	      			if(if_keyword_arg.charAt(k)=='=')
    	      			{
    	      				count_key_args++;
    	      			}
    	      			k++;
    	      		}
    	      		
    	      		//Works for multiple arguments being assigned the same value
    	      		while(count_key_args!=0)
    	      		{
    	      			String k_arg=if_keyword_arg.substring(0,if_keyword_arg.indexOf("="));
    	      			int posi=linear_search_ALA(ALA[mnt_for_ref_ala],no_of_args[mnt_for_ref_ala]+1,k_arg);
    	      			ALA[mnt_for_ref_ala][posi]=value_key;
    	      			if_keyword_arg=if_keyword_arg.substring(if_keyword_arg.indexOf("=")+1);
    	      			count_key_args--;
    	      		}
    	      	}
    	      	ALA[mnt_for_ref_ala][i]=if_keyword_arg;
    	      }
    	      
    	      i++;
    	    }
    	  }
        }
        
        //There is a label in the arguments list or maybe not
        else if(st4.countTokens()==2)
        {
          i=1;
          StringTokenizer st6=new StringTokenizer(line,delim,false);
          
          //Store the macro name in the temporary variable, it is not actually required
          temp=st6.nextToken();
          
          j=i;
    	  
    	  //Setting up of default arguments
    	  StringTokenizer str1=new StringTokenizer(MDT.get(mdtp),delim,false);
    	  String macro_name1=str1.nextToken();
    	  		
    	  while(str1.hasMoreTokens())
    	  {
			String arg_or_label=str1.nextToken();
    	    if(arg_or_label.contains("="))
    	  	{
    	  		ALA[mnt_for_ref_ala][i]=arg_or_label.substring((arg_or_label.indexOf('=')+1));
    	  	    has_default[mnt_for_ref_ala][i]=true;
    	  	}
    	  	i++;
    	  }
    	  	
    	  i=j;
          
          while(st6.hasMoreTokens())
          {
          	String if_keyword_arg=st6.nextToken();
          	if(has_default[mnt_for_ref_ala][i]==false)
          	{
          		//Checking for keyword arguments
    	      	if(if_keyword_arg.contains("="))
    	      	{
    	      		String value_key=if_keyword_arg.substring(if_keyword_arg.lastIndexOf("=")+1);
    	      		int count_key_args=0;	int k=0;
    	      		while(k<if_keyword_arg.length())
    	      		{
    	      			if(if_keyword_arg.charAt(k)=='=')
    	      			{
    	      				count_key_args++;
    	      			}
    	      			k++;
    	      		}
    	      		
    	      		//Works for multiple arguments being assigned the same value
    	      		while(count_key_args!=0) 
    	      		{
    	      			String k_arg=if_keyword_arg.substring(0,if_keyword_arg.indexOf("="));
    	      			int posi=linear_search_ALA(ALA[mnt_for_ref_ala],no_of_args[mnt_for_ref_ala]+1,k_arg);
    	      			ALA[mnt_for_ref_ala][posi]=value_key;
    	      			if_keyword_arg=if_keyword_arg.substring(if_keyword_arg.indexOf("=")+1);
    	      			count_key_args--;
    	      		}
    	      	}
    			if(st6.countTokens()>=1 && ALA[mnt_for_ref_ala][0]!="bbbb")
    			{
            		ALA[mnt_for_ref_ala][i]=if_keyword_arg;
            	}
            }
            
            i++;
            
            //The last argument will be the label if there is any
            if(st6.countTokens()==1 && ALA[mnt_for_ref_ala][0]!="bbbb")
            {
              ALA[mnt_for_ref_ala][0]=st6.nextToken();
            }
          }
        }
      
        String new_from_mdt="";
        do
        {
          //Increment the value of mdtp
          mdtp++;
        
          //Read the line pointed out by mdtp from the MDT
          String from_mdt=MDT.get(mdtp);
        
          //Replace the arguments with the values from the macro call
          StringTokenizer st7=new StringTokenizer(from_mdt,delim,true);
          new_from_mdt="";
          
          while(st7.hasMoreTokens())
          {
            temp=st7.nextToken();
            if(temp.charAt(0)=='#')
            {
              new_from_mdt+=ALA[mnt_for_ref_ala][Integer.parseInt(temp.substring(1,temp.length()))];
            }
            else
            {
              new_from_mdt+=temp;
            }
          }
          if(!new_from_mdt.equals("MEND"))
          {
            writeToFile(output_pass2,new_from_mdt);
          }
        }
        while(!new_from_mdt.equals("MEND"));
      }
    }
    
    System.out.println("Contents of ALA:");
    for(i=0;i<macro_count;i++)
    {
      System.out.println("ALA for the macro "+(MNT.elementAt(i)).macro_name+":");
       for(j=0;j<ALA[i].length;j++)
       {
         if(ALA[i][j]!=null)
         {
          System.out.println(j+"  "+ALA[i][j]);
         }
       }
       System.out.println();
     }
  }
  
  //Function to write to file for input to pass 2
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
  
  //Search through the ALA to find position of arguments
  public static int linear_search_ALA(String arr[],int n,String arg)
  {
    int k;
    for(k=0;k<n;k++)
    {
      if(arr[k].equals(arg))
      {
        return k;
      }
    }
    return -1;
  }
}
/*For label case: 0 means no label present,
1 means label present as &label macro_name arg1,arg2
2 means label present as macro_name arg1,arg2,&label-as soon as labels are substituted, case is changed to 1*/