import java.io.*;
import java.util.ArrayList;
/***
 * @author esteban acosta
 */
import java.util.Scanner;
public class Assembler {
	/** Definitions of opcodes. LSB is at array index 0, so the opcodes are
	 * written right to left (reverse of reading in English) */
	private static final boolean[] OP_ADD = {false,false,false,true,true,false,true,false,false,false,true};
	private static final boolean[] OP_SUB = {false,false,false,true,true,false,true,false,false,true,true};
	private static final boolean[] OP_AND = {false,false,false,false,true,false,true,false,false,false,true};
	private static final boolean[] OP_ORR = {false,false,false,false,true,false,true,false,true,false,true};
	private static final boolean[] OP_LDR = {false,true,false,false,false,false,true,true,true,true,true};
	private static final boolean[] OP_STR = {false,false,false,false,false,false,true,true,true,true,true};
	private static final boolean[] OP_CBZ = {false,false,true,false,true,true,false,true};
	private static final boolean[] OP_B = {true,false,true,false,false,false};
	private static final boolean[] OP_HLT = {false,true,false,false,false,true,false,true,false,true,true};

	//will be used to store all the numbers in the data segment
	private static ArrayList<Integer> bin= new ArrayList<Integer>();

	//will be used to keep track of how many bytes are in the data segment
	private static int dataCount = 0;

	//will be used to keep track of how many bytes are in the code segment
	private static int codeCount = 0;

	//stores the value register
	private static String value ;

	//stores the base register
	private static String base ;

	//stores the offset number
	private static String offS ;


	/**
	 * Assembles the code file. When this method is finished, the dataFile and
	 * codeFile contain the assembled data segment and code segment, respectively.
	 *
	 * @param inFile The pathname to the assembly language file to be assembled.
	 * @param dataFile The pathname where the data segment file should be written.
	 * @param codeFile The pathname where the code segment file should be written.
	 */
	public static void assemble(String inFile, String dataFile, String codeFile)
			throws FileNotFoundException, IOException {

		// do not make any changes to this method

		ArrayList<LabelOffset> labels = pass1(inFile, dataFile, codeFile);
		pass2(inFile, dataFile, codeFile, labels);
	}

	/**
	 * First pass of the assembler. Writes the number of bytes in the data segment
	 * and code segment to their respective output files. Returns a list of
	 * code segment labels and their relative offsets.
	 *
	 * @param inFile The pathname of the file containing assembly language code.
	 * @param dataFile The pathname for the data segment binary file.
	 * @param codeFile The pathname for the code segment binary file.
	 * @return List of the code segment labels and relative offsets.
	 * @exception RuntimeException if the assembly code file does not have the
	 * correct format, or another error while processing the assembly code file.
	 */
	private static ArrayList<LabelOffset> pass1(String inFile, String dataFile, String codeFile)
			throws FileNotFoundException {

		Scanner input = new Scanner(new File(inFile));

		ArrayList<LabelOffset> label = new ArrayList<LabelOffset>();

		//keeps track of how many bytes are in the data segment
		dataCount = 0;

		//keeps track of how many bytes are in the code segment
		codeCount = 0;

		//this loop figures out how many bytes are in the data segment of the file
		while (input.hasNextLine()) {

			//takes each line in the text file
			String str = input.nextLine();

			//trims any white space before or after the string
			str = str.trim();

			//if the line contains .data or .align skip to the next iteration
			if(str.contains(".data") || str.contains(".align")){
				continue;
			}
			//if the line contains .text, break out of the loop
			else if(str.contains(".text")){
				break;
			}

			// removes the ".word" from each line
			String [] array = str.split(".word");

			/*then takes the second element of the array and splits it
			by the commas that are in the string*/

			/*What's currently stored in the array "a" is now all the numbers that's
			on that line
			 */
			array[1] = array[1].trim();
			String [] a = array[1].split(",");

			// since we are keeping track of how many bytes we have
			//we should add the length of the array after every iteration
			//(which is the # of numbers in the array) to dataCount.
			dataCount += a.length;

			/*Take each string value in the array and convert it into an int and add it to the 
			 * array list "bin"
			 */

			for(int i = 0; i < a.length; i++){
				bin.add(Integer.parseInt(a[i]));
			}
		}

		//counts the offset
		int off = 0;

		//this loop figures out how many bytes are in the code segment of the file
		while(input.hasNextLine()){
			//takes each line of the file and store it into a string
			String line = input.nextLine();


			// skip these lines if the lines are in the data segment of the text file
			if(line.contains(".word") || line.contains(".global") || line.contains(".text") || line.contains(".align")){
				continue;
			}

			//if we are at the end of the text file, break out of the loop
			else if(line.contains(".end")){
				//increase offset by 4
				off+=4;
				//and add one more to codeCount(since .end is also an instruction, we add 1 to codeCount)
				codeCount+=1;
				break;
			}

			//if the line contains a colon (since all labels have a colon after it)
			else if(line.contains(":")){
				//create a new labelOffSet object after every label we encounter in the file
				LabelOffset l = new LabelOffset();

				//set the offset to the current offset
				l.offset = off;

				//first remove the colon that's after the label
				String [] split = line.split(":");

				//and set the current object's label to the label that's on the line
				l.label = split[0];

				//add LabelOffSet object to the array list
				label.add(l);

				//after that, skip to the next iteration
				continue;
			}

			// if none of the other conditions are fulfilled, that means that that line contains an instruction
			else{ 
				//add 1 to the # of instructions that are in the program
				codeCount+=1;

				//Add 4 to the offset every time we encounter an instruction
				off+=4;
			}

		}
		input.close();
		//Since they count the number of numbers that are in the data segment
		//and the number of instructions that are in the code segment respectively, we need
		//to multiply by 4 to obtain the # of bytes that are needed to store
		//the # of number and the # of instructions
		codeCount*=4;
		dataCount*=4;

		/*
		 * Writes codeCount to codeFile and dataCount to dataFile
		 */

		PrintWriter pw1 = new PrintWriter(codeFile);
		pw1.print(codeCount);
		pw1.close();
		
		PrintWriter pw2 = new PrintWriter(dataFile);
		pw2.print(dataCount);
		pw2.close();

		return label;
	} // end of pass1

	/**
	 * Second pass of the assembler. Writes the binary data and code files.
	 * @param inFile The pathname of the file containing assembly language code.
	 * @param dataFile The pathname for the data segment binary file.
	 * @param codeFile The pathname for the code segment binary file.
	 * @param labels List of the code segment labels and relative offsets.
	 * @exception RuntimeException if there is an error when processing the assembly
	 * code file.
	 */
	public static void pass2(String inFile, String dataFile, String codeFile,
			ArrayList<LabelOffset> labels) throws FileNotFoundException, IOException {

		//create a binary object in order to use some of the methods that are in the binary class

		Binary binary = new Binary();

		//creates a boolean array with array length of 32 bits
		boolean [] b = new boolean[Binary.BINARY_LENGTH];

		Scanner input = new Scanner(new File(inFile)); 

		//Since we have written something on this file before, we need to append the data the next time we open it
		FileWriter f = new FileWriter(new File(dataFile), true);
		PrintWriter pw = new PrintWriter(f);
		//Print everything on the next line
		pw.println();



		//loop through each value in the array list
		for(int i : bin){
			//and convert each decimal number to binary and store it into the boolean array b
			b = binary.sDecToBin(i);

			//keep track of how many bytes that have been printed on the line
			int count = 0;

			//go through the boolean array
			for(int j = 0; j < b.length; j++){
				//and print each bit
				pw.print(b[j] + " ");
				//add one to counter
				count++;

				//once we've printed 8 bits (1 byte) on one line
				if(count == 8){
					//we reset counter to 0 and continue looping
					count = 0;
					//print everything on the next line
					pw.println();
				}
			}
		}

		//Since an array list adds numbers every single time we go through this program, it would be best to clear 
		//the numbers that are currently in the array list, so the next time we use this program, we will have an empty arraylist
		bin.clear();
		pw.close();

		/**
		 * Since we don't want to erase the data from what's currently being stored in codeFile,
		 * we create FileWrite object that will allows us to append all the data we need to the 
		 * file
		 */
		FileWriter fw = new FileWriter(new File(codeFile), true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		//Since we don't want to add the appended data to the first line, we need
		//to print everything after the first line
		out.println();

		// keep track of how many bytes we need put on one line
		int count = 0;

		boolean [] result = new boolean[Binary.BINARY_LENGTH];
		/**
		 * Now we are going to try to determine the bits of each instruction
		 */
		
		
		//keep track of how 4 bytes are there on each line
		int lineCount = 0;
		//this helps us keep track of how many times we have iterated 
		int numOfIterat = 0;
		while(input.hasNextLine()){
			String line = input.nextLine();

			if(line.contains(".word") || line.contains(".global") || line.contains(".text") || line.contains(".align") || line.contains(".data")){
				continue;
			}

			else if(line.contains(":")){
				continue;
			}
			//if none of the other conditions are fulfilled, that means that we are focusing on just the instructions
			else{
				numOfIterat+=1;
				
				lineCount += 4;
				//If the #of iterations is equal to 1
				if(numOfIterat == 1){
					//the first line is 0 bytes
					lineCount = 0;
				}
				
				if(line.contains("ADD")){
					//this array is for the destination register
					boolean[] temp = new boolean [binary.BINARY_LENGTH];
					//this array is for register 1
					boolean[] temp1 = new boolean [binary.BINARY_LENGTH];
					// this array is for register 2
					boolean[] temp2 = new boolean [binary.BINARY_LENGTH];
					line = line.trim();
					//split the line by the commas 
					String []s = line.split(",");

					//trim the white spaces for each of the strings
					s[0] = s[0].trim();
					s[1] = s[1].trim();
					s[2] = s[2].trim();

					//split each by the letter R, so we'll be left with just the register number
					String [] destRegister = s[0].split("ADD R");
					String [] register1= s[1].split("R");
					String [] register2 = s[2].split("R");

					//convert each register numbers into an int and convert the decimal number
					//into binary, store each one into an array ...
					temp = binary.uDecToBin(Integer.parseInt(destRegister[1]));

					temp1 = binary.uDecToBin(Integer.parseInt(register1[1]));

					temp2 = binary.uDecToBin(Integer.parseInt(register2[1]));

					//and call the makeBigArray method which will combine all three binary arrays plus the instruction opcode into one big array
					result = makeBigArray(temp, temp1, temp2, "add");

				}


				else if(line.contains("SUB")){
					boolean[] temp = new boolean [binary.BINARY_LENGTH];
					boolean[] temp1 = new boolean [binary.BINARY_LENGTH];
					boolean[] temp2 = new boolean [binary.BINARY_LENGTH];
					line = line.trim();
					//split the line by the commas 
					String []s = line.split(",");

					//trim the white spaces for each of the strings
					s[0] = s[0].trim();
					s[1] = s[1].trim();
					s[2] = s[2].trim();

					//split each by the letter R, so we'll be left with just the register number
					String [] destRegister = s[0].split("SUB R");
					String [] register2= s[1].split("R");
					String [] register3 = s[2].split("R");

					//convert each register numbers into an int and convert the decimal number
					//into binary 


					/**
					 *  Take the binary array and put it into its respective array
					 */
					temp = binary.uDecToBin(Integer.parseInt(destRegister[1]));

					temp1 = binary.uDecToBin(Integer.parseInt(register2[1]));

					temp2 = binary.uDecToBin(Integer.parseInt(register3[1]));

					//This method is going to put the boolean values into one big array of 32 bits
					result = makeBigArray(temp, temp1, temp2, "SUB");
				}


				else if(line.contains("AND")){
					boolean[] temp = new boolean [binary.BINARY_LENGTH];
					boolean[] temp1 = new boolean [binary.BINARY_LENGTH];
					boolean[] temp2 = new boolean [binary.BINARY_LENGTH];
					line = line.trim();
					//split the line by the commas 
					String []s = line.split(",");

					//trim the white spaces for each of the strings
					s[0] = s[0].trim();
					s[1] = s[1].trim();
					s[2] = s[2].trim();

					//split each by the letter R, so we'll be left with just the register number
					String [] destRegister = s[0].split("AND R");
					String [] register2= s[1].split("R");
					String [] register3 = s[2].split("R");

					//convert each register numbers into an int and convert the decimal number
					//into binary 
					//when done, print the register number onto the file
					temp = binary.uDecToBin(Integer.parseInt(destRegister[1]));

					temp1 = binary.uDecToBin(Integer.parseInt(register2[1]));

					temp2 = binary.uDecToBin(Integer.parseInt(register3[1]));

					//This method is going to put the boolean values into one big array of 32 bits
					result = makeBigArray(temp, temp1, temp2, "AND");
				}


				else if(line.contains("ORR")){
					boolean[] temp = new boolean [binary.BINARY_LENGTH];
					boolean[] temp1 = new boolean [binary.BINARY_LENGTH];
					boolean[] temp2 = new boolean [binary.BINARY_LENGTH];
					line = line.trim();
					//split the line by the commas 
					String []s = line.split(",");

					//trim the white spaces of each of the strings
					s[0] = s[0].trim();
					s[1] = s[1].trim();
					s[2] = s[2].trim();

					//split each by the letter R, so we'll be left with just the register number
					String [] destRegister = s[0].split("ORR R");
					String [] register2= s[1].split("R");
					String [] register3 = s[2].split("R");

					//convert each register numbers into an int and convert the decimal number
					//into binary 

					temp = binary.uDecToBin(Integer.parseInt(destRegister[1]));

					temp1 = binary.uDecToBin(Integer.parseInt(register2[1]));

					temp2 = binary.uDecToBin(Integer.parseInt(register3[1]));

					//This method is going to put the boolean values into one big array of 32 bits
					result = makeBigArray(temp, temp1, temp2, "ORR");
				}


				else if(line.contains("LDR")){
					//Will split it until we are left with just the register numbers
					splitIt(line);

					//we need an array for the base register, the value register and the offset 
					boolean[] baseReg = new boolean[binary.BINARY_LENGTH] ;
					boolean[] valueReg = new boolean[binary.BINARY_LENGTH];
					boolean [] offSet =  new boolean[binary.BINARY_LENGTH];

					baseReg = binary.sDecToBin(Integer.parseInt(base));
					valueReg =  binary.sDecToBin(Integer.parseInt(value));
					offSet = binary.sDecToBin(Integer.parseInt(offS));

					result = makeInstructions(baseReg,valueReg,"LDR",offSet);
				}

				else if(line.contains("STR")){
					//Will split it until we are left with just the register numbers
					splitIt(line);

					//we need an array for the base register, the value register and the offset 
					boolean[] baseReg = new boolean[binary.BINARY_LENGTH] ;
					boolean[] valueReg = new boolean[binary.BINARY_LENGTH];
					boolean [] offSet =  new boolean[binary.BINARY_LENGTH];

					baseReg = binary.uDecToBin(Integer.parseInt(base));
					valueReg =  binary.uDecToBin(Integer.parseInt(value));
					offSet = binary.uDecToBin(Integer.parseInt(offS));

					result = makeInstructions(baseReg,valueReg,"STR",offSet);
				}

				else if(line.contains("CBZ")){

					//Keeps track of what position we are in the result array
					int position = 0;

					boolean [] temp = new boolean[binary.BINARY_LENGTH];

					//split on every comma
					String [] split = line.split(",");

					split[0] = split[0].trim();

					//find the first occurrence of the letter R 
					int start = split[0].indexOf("R");

					//then take everything that's past the letter R and store it into the string
					//what we will be left is the register number
					split[0] = split[0].substring(start + 1, split[0].length());

					//Convert the register number to binary and place it into temp
					temp = binary.uDecToBin(Integer.parseInt(split[0]));

					//place the register number into the result array
					for(int i = 0 ; i < 5; i++){
						result[position] = temp[i];
						position++;
					}

					int offset = 0;

					split[1] = split[1].trim();
					//if the label that is on this line is in the array list
					for(int index = 0; index < labels.size(); index++){
						//store the offset number
						if(split[1].equals(labels.get(index).label)){
							offset = labels.get(index).offset;
						}
					}
					//subtract the line count by the offset
					offset-= lineCount;

					//and then convert offset to binary
					temp = binary.sDecToBin(offset);

					//store the offset value into result
					for(int i = 0; i < 19 ; i++){
						result[position] = temp[i];
						position++;
					}

					//then store the OPCODE into result
					for(int i = 0; i < OP_CBZ.length;i++){
						result[position] = OP_CBZ[i];
						position++;
					}
				}

				else if(line.contains("B")){
					boolean [] temp = new boolean[binary.BINARY_LENGTH];

					String [] split = line.split("B");

					int offset = 0;

					split[1] = split[1].trim();

					//if the label that is on this line is in the array list
					for(int index = 0; index < labels.size(); index++){
						//store the offset number
						if(split[1].equals(labels.get(index).label)){
							offset = labels.get(index).offset;
						}
					}

					//subtract the line count by the offset
					offset-= lineCount;

					//Convert offset to binary
					temp = binary.sDecToBin(offset);

					//Keep track of where we are in the result array
					int position = 0;

					//store the offset value into result
					for(int i = 0; i < 26 ; i++){
						result[position] =  temp[i];
						position++;
					}

					//store the B opcode into result
					for(int i = 0; i < OP_B.length; i++){
						result[position] = OP_B[i];
						position++;
					}
				}

				else if(line.contains(".end")){
					//this keeps track of what position we are in in the result array
					int position = 0;

					//this is supposed to hold the current position of the HLT array
					int currentP = 0;

					//print 20 false values and store them in the first 20 places in the array result
					for(int i = 0; i < 21 ; i++){
						result[i] = false ;
						position = i;
					}

					//since we want to start one position after the last position we were at, we add one
					//to current position
					position += 1;

					//loop through the rest of the result...
					for(int i = position; position < result.length; i++ ){
						//and insert each element from the HLT array into the remaining spots in the array result
						result[i] = OP_HLT[currentP];

						//if we've reached the last element of the array, break out of the loop
						if(currentP == OP_HLT.length- 1){
							break;
						}
						currentP++;
					}
				}

				//loop through result
				for(int j = 0; j < result.length; j++){
					//and print each bit
					out.print(result[j] + " ");
					//add one to counter
					count++;

					//once we've printed 8 bits (1 byte) on one line
					if(count == 8){
						//we reset counter to 0 and continue looping
						count = 0;
						//print everything on the next line
						out.println();
					}
				}
			} 
		}

		out.close();
	}

	/**
	 * Takes the base register, the value register and the instruction op code and puts them into one big array of 32 bits
	 * This is specifically for four instructions: AND, ADD, SUB, ORR
	 * @param destReg
	 * @param reg1
	 * @param reg2
	 * @param instruction
	 * @return
	 */
	private static boolean[] makeBigArray(boolean [] destReg, boolean [] reg1, boolean [] reg2, String instruction){

		//every instruction is 4 bytes so the instruction array has the length of 4 bytes (32 bits)
		boolean [] result = new boolean[Binary.BINARY_LENGTH];

		//keep track of where we are in the instruction array
		int position = 0;


		// put the last five binary values of the destination register into the instruction array
		for(int i = 0; i <  5 ; i++){
			result[position] = destReg[i];
			position++;
		}

		// put the last five binary values of the first register into the instruction array
		for(int i = 0 ; i < 5; i++){
			result[position] = reg1[i];
			position++;
		}

		//put the six shift bits into the instruction array
		for(int i = 0 ; i < 6; i++){
			result[position] = false;
			position++;
		}

		// put the last five binary values of the second register into the instruction array
		for(int i = 0; i < 5; i++){
			result[position] = reg2[i];
			position++;
		}

		//put the opcode in the remaining bits of the instruction array
		if(instruction.toLowerCase().equals("add")){
			for(int i = 0; i< OP_ADD.length; i++){
				result[position] = OP_ADD[i];
				position++;
			}
		}

		else if(instruction.toLowerCase().equals("sub")){
			for(int i = 0; i< OP_SUB.length; i++){
				result[position] = OP_SUB[i];
				position++;
			}
		}
		
		else if(instruction.toLowerCase().equals("orr")){
			for(int i = 0; i< OP_ORR.length; i++){
				result[position] = OP_ORR[i];
				position++;
			}
		}

		else if(instruction.toLowerCase().equals("and")){
			for(int i = 0; i< OP_AND.length; i++){
				result[position] = OP_AND[i];
				position++;
			}
		}
		return result;
	}

	/**
	 * Takes the base register, the value register and the instruction op code and puts them into one big array of 32 bits
	 * This is specifically for two instructions: LDR and STR
	 * @param baseReg
	 * @param valueReg
	 * @param instruction
	 * @param offSet
	 * @return
	 */
	private static boolean [] makeInstructions(boolean [] baseReg , boolean[] valueReg, String instruction, boolean [] offSet){

		boolean [] result = new boolean[Binary.BINARY_LENGTH];

		//keeps track of the result position
		int position = 0;

		//loop through the value register and add it to result
		for(int i = 0; i < 5; i++){
			result[position] =  valueReg[i];
			position++;
		}

		//loop through the base register and add it to result
		for(int j = 0 ; j < 5; j++){
			result[position] = baseReg[j];
			position++;
		}

		//add the shift bits to result
		for(int i =0; i < 2; i++){
			result[position] = false;
			position++;
		}

		//add the offset value to result
		for(int h = 0; h <9 ; h++){
			result[position] = offSet[h];
			position++;
		}

		//if the instruction is LDR, then we fill in the remaining spots with the LDR OPCODE
		if(instruction.toLowerCase().equals("ldr")){
			for(int index = 0; index < OP_LDR.length; index++){
				result[position] = OP_LDR[index];
				position++;
			}
		}

		//if the instruction is STR, then we fill in the remaining spots with the STR OPCODE
		else if (instruction.toLowerCase().equals("str")){
			for(int index = 0; index < OP_STR.length; index++){
				result[position] =  OP_STR[index];
				position++;
			}
		}

		return result;
	}

	/**
	 * This method splits the LDR AND STR instructions
	 * @param str (The input line)
	 */
	private static void splitIt(String str){
		//split the instruction by the commas
		String [] split = str.split(",");

		//take each string that's in the array and trim it
		split[0] = split[0].trim();
		split[1] = split[1].trim();
		split[2] = split[2].trim();

		//Find the last occurrence of the letter R
		int end = split[0].lastIndexOf("R");

		//and store everything past R into a new string
		value = split[0].substring(end + 1,split[0].length());

		//Find the last occurrence of the letter R
		end = split[1].lastIndexOf("R");

		//And store everything past the letter R into a new string
		base = split[1].substring(end + 1, split[1].length()) ;


		//Since we only care about the offset, we need to split it on two things
		//we need to find the # sign and the square bracket

		int start = split[2].indexOf("#");
		end = split[2].indexOf("]");

		//Once we have the positions of both the # sign and the square bracket,
		//store everything that's in between the two things into a new string
		offS = split[2].substring(start + 1, end) ;

		offS = offS.trim();

		value = value.trim();

		base = base.trim();
	}
}

// end of pass2
