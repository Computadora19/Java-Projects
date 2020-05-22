

/**
 * Methods for converting between binary and decimal.
 *
 * @author Esteban Acostas
 */

public class Binary {

	/** Class constant defines the length of binary numbers. */
	public static final int BINARY_LENGTH = 32;

	/**
	 * Converts a two's complement binary number to signed decimal
	 *
	 * @param b The two's complement binary number
	 * @return The equivalent decimal value
	 * @exception IllegalArgumentException Parameter array length is not BINARY_LENGTH.
	 */
	public static long binToSDec(boolean[] b) throws IllegalArgumentException{
		//throws IllegalArgumentException if parameter length is not equal to BINARY_LENGTH
		if(b.length != BINARY_LENGTH){
			throw new IllegalArgumentException ("Parameter array length is not BINARY_LENGTH");
		}

		//converts from boolean array to integer array 
		int[] zerosOnesRep = booleanToIntConversion(b); 

		int signedDec = 0;
		//So now we have a string of 0s and 1s we need to convert to a signed decimal using two's complement
		//now we have an integer representation of 32 bits 

		//keeps track of bit position
		int current = 1;

		for (int i = zerosOnesRep.length - 1; i >= 0; i--) {
			//If we reach the most significant bit ...
			if(i == 0){

				//do the same thing as before but this time, multiply the bit value "current"
				//by negative 1. Since the most significant bit represents the sign of the
				//the number, then every time we hit the the last digit(MSB) in
				//the array, we always want to multiply it by the negative bit value
				signedDec = signedDec + (zerosOnesRep[i] * (-1*current));

				//Break out of the loop. If this break isn't included then it will perform
				//the operations below this if statement and give us the wrong decimal representation
				break;
			}
			// multiply the bit by each position in the array and add it to signedDec
			signedDec = signedDec + (zerosOnesRep[i] * current);
			// update the bit position
			current *= 2;
		}
		return signedDec;
	}

	/**
	 * Converts boolean array to an array of integers to represent a binary string 
	 * @param boolean array
	 * @return array of integers 
	 * 
	 */
	private static int[] booleanToIntConversion(boolean[] b){
		//keeps track of position in the integer array
		int posit = 0; 
		//stores the digits of the binary number
		int[] bool = new int [Binary.BINARY_LENGTH];
		for(int i = b.length - 1; i > 0; i--){
			// if the value is true, insert 1 into the position
			if (b[i] == true) {
				bool[posit] = 1;
			}
			// if the value is false, insert 0 into the position
			else {
				bool[posit] = 0;
			}
			// increment counter
			posit++;
		}
		//the integer array to be returned 
		return bool; 
	}
	/**
	 * Converts an unsigned binary number to unsigned decimal
	 *
	 * @param b The unsigned binary number
	 * @return The equivalent decimal value
	 * @exception IllegalArgumentException
	 *                Parameter array length is not BINARY_LENGTH.
	 */
	public static long binToUDec(boolean[] b) throws IllegalArgumentException {
		if(b.length != Binary.BINARY_LENGTH){
			throw new IllegalArgumentException("Array shouldn't be longer than consant Binary length");
		}
		long dec = 0;
		//calls a method to convert from boolean array to integer array to represent binary
		int[] bool = booleanToIntConversion(b); 

		// keeps track of the bit position
		long current = 1;
		for (int i = bool.length - 1; i >= 0; i--) {
			// multiply the bit by each position in the array and add it to dec
			dec = dec + (bool[i] * current);
			// update the bit position
			current *= 2;
		}

		return dec;
	}

	/**
	 *  Finds the largest signed decimal number
	 * @return largest signed decimal number
	 */
	private static long largestSDec(){
		boolean [] bool =  new boolean[Binary.BINARY_LENGTH];
		bool[bool.length-1] = false;
		//fills the entire array with true values (1's) except the most significant bit
		for(int i = bool.length - 2; i >= 0 ; i-- ){
			bool[i] = true;
		}

		//convert the binary array to decimal
		long large = binToSDec(bool);
		return large ;
	}

	/**
	 * Finds the smallest unsigned decimal number
	 * @return smallest signed decimal number
	 */
	private static long smallestSDec(){
		boolean [] bool =  new boolean[Binary.BINARY_LENGTH];
		bool[bool.length - 1] = true;
		//fills all the array with false values (0's) except the most significant bit
		for(int i = bool.length - 2; i >= 0 ; i-- ){
			bool[i] = false;
		}
		//convert the binary array to decimal
		long smallest = binToSDec(bool);
		return smallest ;

	}


	/**
	 * Converts a signed decimal number to two's complement binary
	 *
	 * @param d The decimal value
	 * @return The equivalent two's complement binary representation
	 * @exception IllegalArgumentException Parameter is outside valid range.
	 */
	public static boolean[] sDecToBin(long d) throws IllegalArgumentException {
		//finds the smallest signed decimal
		long smallest = smallestSDec() ; 
		//finds the largest signed decimal
		long largest = largestSDec(); 
		//stores the binary values
		boolean [] bin = new boolean[Binary.BINARY_LENGTH];


		int position = 0;

		if(d > largest){
			throw new IllegalArgumentException(" Number shouldn't be bigger than " + largest);
		}
		else if(d < smallest){
			throw new IllegalArgumentException("Number shouldn't be smaller than" + smallest);
		}

		//if the number is negative
		if(d < 0){

			//find the binary representation of the positive number
			bin = uDecToBin(Math.abs(d));

			//loop through the binary representation of the positive number until 
			//we hit a "1".
			for(int i = bin.length - 1; i >= 0 ; i--){
				//once we hit a 1 (in this case represented by the boolean
				//value "true") in the array
				if(bin[i] == true){
					//store the position of where we are in the array
					position = i;
					//and leave the for loop
					break;
				}
			}

			//Now starting from one plus the position we were at in the array
			for(int i = position - 1; i >= 0 ; i--){
				//if the boolean value is true invert the bit from true to false 
				if(bin[i] == true){
					bin[i] = false;
				}
				//if the boolean value is false invert the bit from false to true
				else{
					bin[i] = true;
				}
			}
			return bin;
		}

		//if the number that's passed to this number is positive, call the uDecToBin method
		else{
			bin = uDecToBin(d);
		}

		return bin;
	}

	/**
	 * Finds the largest unsigned decimal number
	 * @return largest unsigned decimal number
	 */
	private static long largestUDec(){
		boolean [] bool =  new boolean[Binary.BINARY_LENGTH];
		//fills entire array with true values
		for(int i = bool.length - 1; i >= 0 ; i-- ){
			bool[i] = true;
		}

		//converts the boolean array to a decimal number
		long large =  binToUDec(bool);

		return  large ;
	}


	/**
	 * Converts an unsigned decimal number to binary
	 *
	 * @param d The decimal value
	 * @return The equivalent binary representation
	 * @exception IllegalArgumentException Parameter is outside valid range.
	 */
	public static boolean[] uDecToBin(long d) throws IllegalArgumentException{
		//finds the largest unsigned decimal number
		long largest = largestUDec();

		if(d <  0){
			throw new IllegalArgumentException(" Pass a positive number ");
		}

		else if(d > largest){
			throw new IllegalArgumentException(" The number that's passed has to be smaller than the"
					+ " the largest unsigned integer ");
		}

		boolean [] bin = new boolean [Binary.BINARY_LENGTH];
		long quot = d;
		long remain = 0;
		int i = bin.length - 1;

		//continue looping until the quotient is 0
		while(quot != 0){

			//Find the remainder of the number divided by two
			remain = quot%2;

			//if the remainder is equal to 1, put true
			if(remain == 1){
				bin[i] = true;
			}		

			//if the remainder is equal to 0, put false
			else if( remain == 0){
				bin[i] = false;
			}

			//Divide the value by two and put it into quot
			quot = quot/2;

			//update the current position of the boolean array
			i--;

		}

		return bin;
	}

	/**
	 * Returns a string representation of the binary number. Uses an underscore
	 * to separate each group of 4 bits.
	 *
	 * @param b The binary number
	 * @return The string representation of the binary number.
	 * @exception IllegalArgumentException Parameter array length is not BINARY_LENGTH.
	 */
	public static String toString(boolean[] b) throws IllegalArgumentException {
		String binary = "";
		if(b.length != Binary.BINARY_LENGTH){
			throw new IllegalArgumentException(" The array shouldn't be longer than binary length ");
		}

		//this counter keeps track of how many strings have been added to the string Binary
		int i = 1;

		for(int j = 0; j < b.length; j++){
			//if the value is true, replace true with one and add it to the string
			if(b[j] == true){
				binary+="1";
			}
			//if the value is false, replace false with zero and add it to the string
			else if(b[j] == false){
				binary+="0";
			}

			//if the counter is divisible by 4 add an underscore at the end of the first 4 characters
			//however don't add an underscore if the counter is equal to the length of the array
			if(i %4 == 0 && i != b.length){
				binary+="_";
			}
			// updates counter
			i++;

		}

		return binary;
	}

	/**
	 * Returns a hexadecimal representation of the unsigned binary number. Uses
	 * an underscore to separate each group of 4 characters.
	 *
	 * @param b The binary number
	 * @return The hexadecimal representation of the binary number.
	 * @exception IllegalArgumentException Parameter array length is not BINARY_LENGTH.
	 */
	public static String toHexString(boolean[] b) throws IllegalArgumentException{
		String hex = "";

		if(b.length!= Binary.BINARY_LENGTH){
			throw new IllegalArgumentException("Parameter is outside of valid range");
		}

		//Will be used to store each four values that are from the boolean array b
		boolean [] bool2 =  new boolean[4];

		//Since the bool2 array can only store 4 values, we need to keep track of which position the 
		//the bool2 array is at
		int track = 0;

		// Will be used to find decimal representation of each four binary values
		long dec = 0;

		//This will store the hexadecimal representation of the number
		String letter;

		//Keeps track of the number of characters that have been added to the string "hex"
		int underScore = 0;

		//Starts from the most significant value and loops until the least significant value is reached
		for(int i = b.length - 1; i >= 0; i--){

			//Every four boolean values from the array " b" are stored into bool2
			bool2[track] = b[i];

			//if the boolean array "bool2" is already full (we've reached the final position in the array)
			if(track == 3){

				//take the values that are stored inside of the array and convert them to decimal
				dec = convertToDec(bool2);

				//since we are going to store every four values every time we are moving through the array "b"
				//we need to reset the position of the array "bool2". By resetting the position, we will be able
				//to store every four boolean values and we won't get an IndexOutOfBound error
				track = 0;

				//Now that we have the decimal representation of the four binary values, we now want
				//to find what the hexadecimal representation of that value is
				letter = findHex(dec);

				//After we have found the hexadecimal representation, we add it to the string "hex"
				hex += letter;

				//Underscore keeps track of where we are in the string "hex".Every time we add
				//a letter to the string, we add one to the counter. 
				underScore++;

				//If the counter is equal to 4 (which means we have 4 characters in the string
				//already) and as long as we haven't reached the end of the boolean array "b"...
				if(underScore % 4 ==0 && i != 0){
					//...add an underscore after the four characters
					hex+="_";
					//and then set underScore to 0.  Since we want to add an underscore 
					//after every 4 characters in the string, we need to set it to 0, otherwise
					//we are going to an underscore in the wrong place in the string
					underScore = 0;
				}


				//if this continue isn't added, the variable "track" will get updated right
				// after this if statement thus every time we iterate 
				//through the array "b", we will always store the values in positions 1 - 3 of the 
				// bool array instead of positions 0 -3 of the bool array
				continue;
			}

			//updates position in the array "bool2"
			track++;
		}

		return hex;
	}
	/**
	 *  Maps the decimal number to its hexadecimal representation
	 * @param num == decimal representation
	 * @return found == hexadecimal representation
	 */ 
	private static String findHex(long num){
		//This array contains the list of hexadecimal representations of binary digits
		String [] symbol = {"0", "1","2", "3", "4", "5","6","7", "8", "9", "A", "B",
				"C", "D" , "E" , "F"};

		String found = "";

		//Loops through the symbol array
		for(int i = 0; i < symbol.length; i++){
			//if the position is equal to the parameter that has been passed to this method..
			if(i == num){
				//store the value in that position into the variable "found"...
				found = symbol[i];
			}
		}

		//and return it
		return found;
	}

	/**
	 * This method converts the  boolean values to decimal
	 * @param b == four boolean values
	 * @return dec == decimal representation of the four boolean values
	 */
	private static int convertToDec(boolean [] b){
		//bit position
		int current = 1;

		//decimal representation
		int dec = 0;

		int posit = 0;

		int [] bool =  new int[4];


		for(int i = 0; i < b.length; i++){
			//if the value is true, insert 1 into the position
			if(b[i] == true){
				bool[posit] = 1;
			}
			//if the value is false, insert 0 into the position
			else{
				bool[posit] = 0;
			}
			//increment counter
			posit++;
		}


		for(int i = bool.length - 1; i >= 0 ; i--){
			//multiplies the bit by each position in the array and adds it to dec
			dec = dec + ( bool[i] * current);
			//updates the bit position
			current*=2;
		}

		return dec;
	}
}
