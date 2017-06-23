package assignment3.simpleAccount.model;

import java.util.ArrayList;

import assignment3.simpleAccount.util.AccountDataException;
import assignment3.simpleAccount.view.EditView;
import assignment3.simpleAccount.view.MainView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/** 
 * A class which defines accounts for the bank as a model. Note that the account ID value of 0 specifies a special case
 * in which no accounts have been loaded. It is not to be used as an actual account value. This class
 * is the model in MVC.
 * 
 * @author dTorrente
 */
public class Account {

	private String firstName;
	private String lastName;
	private int accountID;
	private float funds;
	private DecimalFormat decimalFormat;
	
	private MainView mainMenuListener;
	private ArrayList<EditView> editMenuListener;
	
	/**
	* Default constructor for an account. This is a special case constructor which is used only when the account menu
	* is empty. This is not to be used when loading accounts.
	* 
	* @param mainView The main view which listens to all accounts. Notices changes made to the account list in particular.
	* 
	*/
	public Account (MainView mainView)
	{
		this.firstName = "No accounts";
		this.lastName = "loaded. Please load an account file.";
		this.accountID = 0;
		this.funds = 0.0f;
		this.mainMenuListener = mainView;	
		
		decimalFormat = new DecimalFormat("#.00");
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

	}
	
	/**
	* Constructor for an account. This constructor is to be used when an actual account is added.
	* @param firstName The first name of the account holder.
	* @param lastName The last name of the account holder.
	* @param accountID The unique ID of an account.
	* @param funds The current amount of funds in the account. Stored in USD.  
	* @param mainView The main view which listens to all accounts. Notices changes made to the account list in particular.
	*/
	public Account (String firstName, String lastName, int accountID, float funds, MainView mainView)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountID = accountID;
		this.funds = funds;
		this.editMenuListener = new ArrayList<EditView>();
		
		decimalFormat = new DecimalFormat("#.00");
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
		
		addMainModelListener(mainView);
		notifyMainView();
	}
	
	/**
	* Constructor for an account. This constructor is to be used when a JUnit test is to be ran. Additional integration features are 
	* removed from this method that are typically ran in the original constructor.
	* @param firstName The first name of the account holder.
	* @param lastName The last name of the account holder.
	* @param accountID The unique ID of an account.
	* @param funds The current amount of funds in the account. Stored in USD.  
	*/
	public Account (String firstName, String lastName, int accountID, float funds)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountID = accountID;
		this.funds = funds;
		this.editMenuListener = new ArrayList<EditView>();
		
		decimalFormat = new DecimalFormat("#.00");
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
	}
	
	/** 
	* Get the first name of the account holder.
	* @return The first name of the account holder.
	*/
	public String getFirstName()
	{
		return firstName;
	}
	
	/** 
	* Get the last name of the account holder.
	* @return The last name of the account holder.
	*/
	public String getLastName()
	{
		return lastName;
	}
	
	/** 
	* Get the unique account ID of the account.
	* @return The unique account ID of the account.
	*/
	public int getAccountID()
	{
		return accountID;
	}
	
	/** 
	* Get the current amount of funds in the account.
	* @return The current amount of funds in the account.
	*/
	public float getFunds()
	{
		return funds;
	}
	
	/** 
	* Set the first name of the account holder.
	* @param firstName The value to set for the first name of the account holder.
	*/
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	/** 
	* Set the last name of the account holder.
	* @param lastName The value to set for the first name of the account holder.
	*/
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	/** 
	* Set the account ID of the account. This is a unique value.
	* @param accountID The value to set for the account ID.
	*/
	public void setAccountID(int accountID)
	{
		this.accountID = accountID;
	}
	
	/** 
	* Set the funds of the account.
	* @param funds The value to set for the account funds.
	*/
	public void setFunds(float funds)
	{
		this.funds = funds;
	}

	/** 
	* Adjust the current funds in the account.
	* @param amountToAdjust The amount to adjust the funds by. A negative value reduces funds while a positive one increases funds.
	* @throws AccountDataException Exception is thrown if the amount is negative.
	*/
	public void depositFunds(float amountToAdjust) throws AccountDataException
	{
		if (amountToAdjust < 0)
			throw new AccountDataException("Please enter in a positive value");
		
		this.funds = this.funds + amountToAdjust;
		notifyEditView();
	}
	
	/** 
	* Adjust the current funds in the account.
	* @param amountToAdjust The amount to adjust the funds by. A negative value reduces funds while a positive one increases funds.
	* @param fundMultiplier The multiplier to properly display the funds should the user attempt to withdraw more funds than they have available.
	* @throws AccountDataException Exception is thrown if the amount is negative.
	*/
	public void withdrawFunds(float amountToAdjust, float fundMultiplier) throws AccountDataException
	{
		float difference = amountToAdjust - this.funds;
		
		if (amountToAdjust < 0)
			throw new AccountDataException("Please enter in a positive value");
		else if(difference > 0)
			throw new AccountDataException("Insufficient funds: amount to withdraw is " + decimalFormat.format(difference * fundMultiplier ) + " greater than available funds " + decimalFormat.format(this.funds * fundMultiplier ) + ".");
		
		this.funds = this.funds - amountToAdjust;
		notifyEditView();	
	}
	
	/** 
	* Adjust the current funds in the account. This method is used only for unit testing.
	* @param amountToAdjust The amount to adjust the funds by. A negative value reduces funds while a positive one increases funds.
	* @param fundMultiplier The multiplier to properly display the funds should the user attempt to withdraw more funds than they have available.
	* @param testing A dummy value to identify that this method is being called from a test. 
	* @throws AccountDataException Exception is thrown if the amount is negative.
	*/
	public void withdrawFunds(float amountToAdjust, float fundMultiplier, boolean testing) throws AccountDataException
	{
		float difference = amountToAdjust - this.funds;
		
		if (amountToAdjust < 0)
			throw new AccountDataException("Please enter in a positive value");
		else if(difference > 0)
			throw new AccountDataException("Insufficient funds: amount to withdraw is " + decimalFormat.format(difference * fundMultiplier ) + " greater than available funds " + decimalFormat.format(this.funds * fundMultiplier ) + ".");
		
		this.funds = this.funds - amountToAdjust;
	}
		
	/** 
	* Inform the main view (view) that the account (model) has changed.
	* This is primarily used when the list of accounts has changed
	* rather than an individual account itself.
	* 
	*/
	public void notifyMainView()
	{
		//make sure an actual account is loaded and not just the default one.
		//close all editWindows as they will no longer be valid.
		mainMenuListener.menuChanged(this);
	}
	
	/** 
	* Notify all editViews (views) of an account (model) change. This is used to update all edit view
	* windows tied to a specific account.
	*/
	public void notifyEditView()
	{
		for(int iterator = 0; iterator < editMenuListener.size(); iterator++)
		{
			editMenuListener.get(iterator).updateAvabFundDisplay();
		}
	}
	
	/** 
	* Add a listener to the main view. The main view will listen to all
	* accounts. As such, this is called in the constructor of each real
	* account.
	* @param mainView The main view to notify.
	*/
	public void addMainModelListener(MainView mainView)
	{
		this.mainMenuListener = mainView;
	}
	
	/** 
	* Add a listener to an edit view. Each edit view can listen to one
	* account. An account can have multiple listeners. This list will
	* be dynamic as the program itself runs.
	* @param editView The edit view to notify.
	*/
	public void addEditModelListener(EditView editView)
	{
		this.editMenuListener.add(editView);
	}
	
	/** 
	* Remove a listener from this account. This is called
	* when an edit view is closed.
	* @param editView The edit view to notify.
	*/
	public void removeEditModelListener(EditView editView)
	{
		this.editMenuListener.remove(editView);
	}
	
	/** 
	* Remove the main listener from this account. This is used primarily when the accounts are changed
	* and this account is no longer included in the current list of accounts.
	*/
	public void removeMainModelListener()
	{
		mainMenuListener.notifyOfRemoval();
		mainMenuListener = null;
	}
	
	
	@Override
	/** 
	* Display the account and name of the account. This method overrides toString() in order to properly display within a combo box.
	* @return A string which contains the account number and the name of the account holder
	* */
	public String toString()
	{
		return getAccountID() + " (" + getFirstName() + " " + getLastName() + ")";
	}
	
	/** 
	* Generates a full string of all information within an account. Primarily used to write to a file when an account is saved out.
	* @return A string which contains the account number and the name of the account holder as well as the current funds in USD. Note
	* that the funds returned will not be formatted to two decimal places in order to accommodate for special rates.
	* */
	public String generateString()
	{
		return getFirstName() + " " + getLastName() + " " + getAccountID() + " " + getFunds();
	}
	
	/** 
	* Generates a string to be used for the title of an account in an edit view window.
	* @return A string which contains the account number and the name of the account holder.
	* */
	public String generateStringForEditView()
	{
		return getFirstName() + " " + getLastName() + " " + getAccountID();
	}
}
