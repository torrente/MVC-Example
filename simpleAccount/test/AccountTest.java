/**
 * 
 */
package assignment3.simpleAccount.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import assignment3.simpleAccount.model.Account;
import assignment3.simpleAccount.util.AccountDataException;

/**
 * 
 * JUnit tests designed to test the account class.
 * @author dTorrente
 *
 */
public class AccountTest {

	
	Account account;
	
	/**
	 * Test method for confirming account is properly assigned to variables. It confirms that the funds are properly set,
	 * the first name is set, and that the last name is set.
	 * 
	 */
	@Test
	public void testAccountConstruction() 
	{
		account = new Account ("testFirstName", "testLastName", 5, 10);
		assertEquals("initial funds must be 10", 10, account.getFunds(), 0.00);
		assertEquals("first name must be testFirstName", "testFirstName", account.getFirstName());
		assertEquals("first name must be testFirstName", "testLastName", account.getLastName());
	}

	/**
	 * Test method for withdrawing funds from the account. Confirms that the withdraw method is functioning properly.
	 */
	@Test
	public void testWithdrawFunds() 
	{
		account = new Account ("testFirstName", "testLastName", 5, 10);
		try
		{
			account.withdrawFunds(5, 1, true);
			assertEquals("10 minus 5 must equal 5", 5, account.getFunds(), 0.00);
		}
		catch(AccountDataException adException)
		{
			
		}
	}
	
	/**
	 * Test method for attempt to withdraw negative fund value. Confirms that the AccountDataException is being properly thrown and
	 * passed to the calling method.
	 */
	@Test
	public void testWithdrawFundsException() 
	{
		account = new Account ("testFirstName", "testLastName", 5, 10);
		try
		{
			account.withdrawFunds(-5, 1, true);
			fail("expected AccountDateException due to witdrawing negative funds");
		}
		catch(AccountDataException adException)
		{
			
		}
	}
	

}
