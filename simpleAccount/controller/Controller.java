package assignment3.simpleAccount.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

import assignment3.simpleAccount.model.Account;
import assignment3.simpleAccount.util.AccountComparator;
import assignment3.simpleAccount.util.AccountDataException;
import assignment3.simpleAccount.view.EditView;
import assignment3.simpleAccount.view.MainView;

/** 
 * A class which controls the main functionality of the program. This class is the controller in an MVC model. It 
 * acts as the main entry point of the program as the instantiation of this class initializes the model and view.
 *  
 * @author dTorrente
 */
public class Controller {
	
	private Vector<Account> account;
	private MainView mainView;
	private Vector<EditView> editView;
	
	/**
	* Default constructor for the controller.  
	*/
	public Controller()
	{	
		mainView = new MainView(this);
		account = new Vector<Account>();
		editView = new Vector<EditView>();
	}
	
	/**
	* Loads accounts from a data file.
	* 
	* @param accountFile The account file to load data from. 
	* 
	*/
	private void loadAccounts(File accountFile)
	{
		Vector<Account> accounts = new Vector<Account>();
		clearAccounts();
		Scanner input = null;
		try
		{
			input = new Scanner (accountFile);
			
			while (input.hasNext())
			{
				accounts.add(new Account(confirmedName(input.next()), confirmedName(input.next()), input.nextInt(), input.nextFloat(), this.mainView));
			}
			
			if (organizeAccounts(accounts) || (accounts.size() == 0))
			{
				throw new Exception();
			}
			
			this.account = accounts;
		}
		catch (FileNotFoundException fnfException)
		{
			mainView.displayNotification("File not found. Please check the file.");
			resetModelAndView();
		}
		catch (InputMismatchException imException)
		{
			mainView.displayNotification("Account file is not properly formatted. Make sure the ID contains only numbers and that the user name contains only letters.");
			resetModelAndView();
		}
		catch (Exception exception)
		{
			mainView.displayNotification("Account file has errors. Check for duplicates");
			resetModelAndView();
		}
		finally
		{
			input.close();
		}
	}
	
	/**
	* Used to reset the account list (model) as well as the views. Called when a new account file is loaded.
	* 
	* @param 
	* 
	*/
	private void resetModelAndView()
	{
		clearAccounts();
		mainView.resetComboBox();
	}
	
	/**
	* Sorts a list of accounts based on the account ID.
	*
	* @param accounts The list of accounts to sort.
	* @return If a duplicate account ID was found. This can later be used to prevent duplicate accounts. 
	*/
	public boolean organizeAccounts(Vector<Account> accounts)
	{
		Collections.sort(accounts, new AccountComparator());
		for(int iterator = 0; iterator < accounts.size() - 1; iterator++ )
		{
			if (accounts.elementAt(iterator).getAccountID() == accounts.elementAt(iterator + 1).getAccountID())
				return true;
		}
		return false;
	}
	
	/**
	* Checks if a string contains only letters by checking each letter one at a time..
	* 
	* @param name The string to check for letters only.
	* @return If all of the characters were letters. 
	* 
	*/
	private boolean isAlpha(String name) 
	{
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	* Confirms if a name contains only the allowed characters.
	* 
	* @param name The name to confirm.
	* @return The original name passed in if the name was confirmed to not contain illegal characters.
	* @throws InputMismatchException If the name contains illegal characters.
	*/
	private String confirmedName(String name) throws InputMismatchException
	{
		if (!isAlpha(name))
			throw new InputMismatchException();
		return name;
	}
	
	/**
	* Saves the current accounts to a file.
	* 
	* @param file The file to save the accounts to.
	* @param account The list of accounts to save to a file.
	* 
	*/
	private void saveAccounts(File file, Vector<Account> account)
	{	
		//FileWriter is wrapper with a print writer to catch IO Exceptions
		//PrintWriter is used to facilitate ease of formatting.
		FileWriter fWriter = null;
		PrintWriter pWriter = null;
		
		try
		{
			fWriter = new FileWriter(file);
			pWriter = new PrintWriter(fWriter);
			
			for(int iterator = 0; iterator < account.size(); iterator++)
			{
				pWriter.println(account.get(iterator).generateString());
			}
			
			fWriter.close();
		}
		catch (FileNotFoundException fnfException)
		{
			mainView.displayNotification("File not found. Please check the file.");
		}
		catch (SecurityException sException)
		{
			mainView.displayNotification("You do not have permission to write this file.");
		}
		catch (IOException ioException)
		{
			mainView.displayNotification("There was an error writing this file. Please try again.");
		}
		finally
		{
			pWriter.close();
		}		
	}
	
	/**
	* Handles operations declared by editViews.
	* 
	* @param action The name of the button which called the action.
	* @param editValue The value to edit an account by. Parsed to a float within this method.
	* @param editView The specific editView which the action was called from. 
	* 
	*/
	public void editViewOperations(String action, String editValue, EditView editView)
	{
		try
		{
			float editV = Float.parseFloat(editValue);
			editV = editV / editView.getFundMultiplier();
			if(action.equals("Deposit"))
			{
				editView.getListenTarget().depositFunds(editV);
				//editView.getListenTarget().notifyEditView();
			}
			
			else if(action.equals("Withdraw"))
			{
				editView.getListenTarget().withdrawFunds(editV, editView.getFundMultiplier());
				//editView.getListenTarget().notifyEditView();
			}
			else if(action.equals("Dismiss"))
			{
				editView.setVisible(false);
				editView.getListenTarget().removeEditModelListener(editView);
				removeView(editView);
			}
		}
		catch (AccountDataException adException)
		{
			mainView.displayNotification(adException.getMessage());
		}
		catch (NumberFormatException nfException)
		{
			mainView.displayNotification("Please enter a valid number.");
		}
		finally
		{
			if(editView != null)
			{
				editView.resetEditAmountDisplay();
			}
		}
		
	}
	
	/**
	* 
	* 
	* @param action The name of the button which called the action. Also used to determine which specific editView to
	* instantiate.
	* @param activeAccount The specific account that was currently active in the drop down menu.
	* 
	*/
	public void mainViewButtonOperations(String action, Account activeAccount)
	{	
			if(activeAccount.getAccountID() == 0)
			{
				mainView.displayNotification("Please load an active account file before attempting to adjust funds.");
			}
			else
			{
				this.editView.addElement(new EditView(activeAccount, this, action));
			}
	}
	
	/**
	* Close an editView down. It is visibly closed and removed from the list of editViews.
	* 
	* @param editView The view to close down. Prior to this, the view is decoupled from the model. 
	* 
	*/
	private void removeView(EditView editView)
	{
		boolean found = false;
		
		for(int iterator = 0; iterator < this.editView.size() && !found; iterator++)
		{
			if (editView == this.editView.elementAt(iterator))
			{
				found = true;
				this.editView.remove(iterator);
			}
		}
	}
	
	/**
	* 
	*  Clears all accounts from the account list. Often used when loading new accounts.
	*  All views associated with the accounts are also closed in this method.
	* 
	*/
	private void clearAccounts()
	{
		while(!editView.isEmpty())
		{
			editView.firstElement().setVisible(false);
			editView.firstElement().getListenTarget().removeEditModelListener(editView.firstElement());
			removeView(editView.firstElement());
		}
		
		//remove all listeners to the mainModel
		for(int iterator = 0; iterator < account.size(); iterator++)
		{
			account.elementAt(iterator).removeMainModelListener();
		}
		
		//clear all accounts
		account.clear();
	}
	
	/**
	* Handles actions taken in the main view file menu system. Primarily used for loading and saving accounts, as well as exiting 
	* the program. 
	* 
	* @param action The specific selection made in the file menu.  
	* 
	*/
	public void mainViewMenuOperations(String action)
	{
		File file = null;
		if(action.equals("Save Account File"))
		{
			file = mainView.selectFile(action);
			if(file != null)
			saveAccounts(file, account);
			
		}
		else if(action.equals("Load Account File"))
		{
			if(mainView.displayConfirmation("You are about to load new accounts. This will close all of your current windows. Proceed?"))
			{
				file = mainView.selectFile(action);
				if(file != null)
					loadAccounts(file);
			}
		}
		else if(action.equals("Exit Program"))
		{
			mainView.displayNotification("Please make sure to save before exiting. If you choose not to, you will lose any updates you have made.");
			file = mainView.selectFile(action);
			if(file != null)
			saveAccounts(file, account);
			System.exit(0);
		}
	}
	
}
