package assignment3.simpleAccount.util;

/**
 * 
 * A custom exception class used to display information when an account is not properly configured.
 * @author dTorrente
 *
 */
public class AccountDataException extends Exception{
	
	private static final long serialVersionUID = -1391171911359140163L;

	/**
	* Creates a meaningful exception which can be displayed as a string.
	* 
	* @param exceptionType A string which better describes the exception thrown.
	* 
	*/
	public AccountDataException (String exceptionType)
	{
		super (exceptionType);
	}

}
