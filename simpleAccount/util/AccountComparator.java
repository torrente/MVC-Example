package assignment3.simpleAccount.util;

import java.util.Comparator;
import assignment3.simpleAccount.model.Account;

/** 
 * A comparator class which compares two account numbers. It contains one public method used to
 * compare two objects of the Account class
 * 
 * @author dTorrente
 */
public class AccountComparator  implements Comparator<Account>{
	
	/**
	*Compares two accounts based on their ID. 
	*@param acct1 The first account to compare.
	*@param acct2 The second account to compare.
	*@return The value of comparison between the two accounts. If the first account has a higher account 
	*ID, it will return a positive value. If the second account has a higher account ID, it will return 
	*a negative value. If they share the same account number, it will return zero.
	*/
	public int compare(Account acct1, Account acct2)
	{
		return acct1.getAccountID() - acct2.getAccountID();
	}

}
