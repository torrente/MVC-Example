package assignment3;

import assignment3.simpleAccount.controller.Controller;


/** 
 *
 *Main entry to an MVC bank account. This gets the program running.
 * 
 *@author dTorrente
 */
public class Assignment3 {

	/** 
	*Creates an instance of the controller class which begins the program.
	*@param args Arguments to be passed in to the main method.
	*/
	public static void main(String[] args) 
	{
	
		//Note that the following warning can be ignored.
		//An anonymous class could have been used, but was not necessary.
		//Warning was not suppressed.
		Controller controller = new Controller();

	}
		
}
