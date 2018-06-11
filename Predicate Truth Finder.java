/*
 * Lillie Atkins
 * Programming Assignment 1
 * Worked with Nicole Hong
 * Time spent on assignment: 13 hours
 * 
 * Sample output: 
 * 
 * Input predicate using capital letters, and the operators ‘and’, ‘or’, and ‘not’, as well as parentheses: 
	“(F and G) and (H and K)”, “(F and G) or (H and K)”
		F G H K 
		--------
		T T T T 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ProgrammingAssignment1 {
	
	public static int[][] truth_table;
	public static int count = 0, pred_num;
	public static ArrayList<Character> variables = new ArrayList<Character>();
	public static String[] predicates_array;

	public static void main(String[] args) throws ScriptException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Input predicate using capital letters, and the operators ‘and’, ‘or’, and ‘not’, as well as parentheses: ");
		String predicates = scan.nextLine();
		predicates_array = predicates.split(","); //creates an array where each element is a predicate
		
		for(int c = 0; c < predicates.length(); c++) { //c for character
			if(Character.isUpperCase(predicates.charAt(c)) && !variables.contains(predicates.charAt(c))) {
				variables.add(count, predicates.charAt(c)); //creates a list of the variables
				count++; //counts the number of variables
			}
		}
		
		for(int var = 0; var<variables.size(); var++) { //prints out the variables
			System.out.print(variables.get(var) + " ");
		}
		System.out.println();
		for(int x = 0; x < count*2; x++) { //print out dashes that are proportional to the number of variables
			System.out.print("-");
		}
		System.out.println();
		
		truth_table = new int[(int) Math.pow(2, count)][count + predicates_array.length]; //create a truth table with rows 
		//equal to the number of possible options (which is always 2^number of variables) and columns equal to the number of
		//variables plus the number of predicates (so that we have room to put in all the options for the variables and so that
		//we can put in whether each predicate is true or false for that option)
		
		for(int row = 0; row < Math.pow(2, count); row++) { //fill in truth table with possible options for every variable
			String number = Integer.toBinaryString(row); //Integer.toBinaryString() turns a decimal number into a binary number. 
			//We want this number to increase by 1 for every row so we base it off of the row. This is because counting in binary 
			//was a easy way to assign all the variables the different truth table options.
			for(int col = 0; col < count; col++) {
				while(number.length() != count) {
					number = "0" + number; //do this so that the number has the correct number of digits as the number of variables
				}
				truth_table[row][col] = number.charAt(col)-48; //-48 because in ASCII 0 is 48 and 1 is 49
				//above we are assigning one character of this number to each column of the truth table, so that each option is is considered for the combination of variables 
			}	
		}
		
		
		for(pred_num = 0; pred_num < predicates_array.length; pred_num++) { //pred_num is the number of predicates
			evaluatePred(predicates_array[pred_num]); //calls evalPred for every predicate
		}
		
		//evaluates what options of the truth table are true for all predicates and prints what each variable is in these options
		int t;
		for(int row = 0; row<truth_table.length; row++) {
			t = 0;
			for(int col = count; col<truth_table[row].length; col++) { //check if each predicate is true for those options
				if(truth_table[row][col] == 1) {
					t++; //if true then increase true count
				}
				else {
					break; //if one is false go to the next row
				}
			}
			if(t == predicates_array.length) { //if true count is equal to the number of predicates then it was true for every predicate
				for(int var_col = 0; var_col<variables.size(); var_col++) { //print out what each variable was for this true option
					if(truth_table[row][var_col] == 0) {
						System.out.print("F "); //if 0 in the truth table that means false
					}
					else {
						System.out.print("T "); //if 1 in the truth table that means true
					}
				}
				System.out.println(); //next line
			}
		}
		
		scan.close();
		
	}
	
	public static void evaluatePred(String predicates) throws ScriptException {
		
		String math_predicate = "";
		
		for(int letter = 0; letter < predicates.length(); letter++) { //turns the word predicate into a string with variables and math symbols (a.k.a. substituting or to ||)
			if(predicates.charAt(letter) == '(' || predicates.charAt(letter) == ')')
				math_predicate += predicates.charAt(letter);
			if(predicates.charAt(letter) == 'a') {
				letter+=3;
				math_predicate += "&&";
			}
			if(predicates.charAt(letter) == 'n') {
				letter+=3;
				math_predicate += "!";
			}
			if(predicates.charAt(letter) == 'o' ) {
				letter+=2;
				math_predicate += "||";
			}
			if(Character.isUpperCase(predicates.charAt(letter))) {
				math_predicate += predicates.charAt(letter);
			}
		}
		
		
		ScriptEngineManager manager = new ScriptEngineManager(); //these two lines of code allow us to evaluate a string as a boolean
		ScriptEngine engine = manager.getEngineByName("js");
		
		String final_predicate;
		int value;
		for(int row = 0; row<truth_table.length; row++) { //loop through and evaluate the predicate created above for each of the options for every variable in the truth table
			final_predicate = "";
			for(int c = 0; c<math_predicate.length(); c++) { //c for character
				if(Character.isUpperCase(math_predicate.charAt(c))) { //allows us to only substitute for the variables in the predicate created above
					value = truth_table[row][variables.indexOf(math_predicate.charAt(c))]; //get what an option for each variable is
					final_predicate += "1==" + Integer.toString(value); //have the 1== so that the 0 and 1 in the truth table can be evaluated as booleans by checking if 1==1 or if 1==0
				}
				else {
					final_predicate += math_predicate.charAt(c); 
				}
			}
			if(engine.eval(final_predicate).equals(true)) { //if the option on a certain row makes the predicate true then add a 1 for this predicate's column at that row of options in the truth table
				truth_table[row][pred_num+count] = 1; 
			}
			else {
				truth_table[row][pred_num+count] = 0; //if not add a 0
			}
		}
	}

}
