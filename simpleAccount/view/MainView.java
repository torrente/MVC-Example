package assignment3.simpleAccount.view;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.io.File;
import java.util.Vector;

import javax.swing.JPanel;

import assignment3.simpleAccount.controller.Controller;
import assignment3.simpleAccount.model.Account;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.BorderLayout;

/** 
 * A class which defines a view for all accounts to use. This view is used to save accounts, load accounts, and modify them through 
 * additional windows.
 * @author dTorrente
 */
public class MainView extends JFrame implements WindowListener{

	private static final long serialVersionUID = 434756746508015718L;
	private JComboBox<Account> accountList;
	private JButton[] button;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	
	private Controller controller;
	private Vector<Account> accounts;
	
	private JMenuItem saveFile;
	private JMenuItem loadFile;
	private JMenuItem exit;
	
	private JPanel buttonPanel;
	
	private ButtonHandler buttonHandler;
	private MenuHandler menuHandler;
	
	private DefaultComboBoxModel<Account> comboModel; 
	
	/**
	* Constructor to create a view used to select accounts as well as how to edit them.
	* @param controller The controller used to create this account.
	* 
	*/
	public MainView(Controller controller)
	{
		super("Assignment 3");
		
		this.setSize(500, 200);
		this.setResizable(false);
		this.controller = controller;
		
		buttonHandler = new ButtonHandler();
		menuHandler = new MenuHandler();
	
		setFileMenu();
		setMenuBar();
		setButtons();
		setComboBox();
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		this.setVisible(true);
	}
	
	/** 
	* Set up the comboBox with a list of accounts. Initially has a default account listed. 
	*/
	private void setComboBox()
	{
		accounts = new Vector<Account>();
		accounts.add(new Account(this));
		comboModel = new DefaultComboBoxModel<Account>(accounts);
		accountList = new JComboBox<Account>( comboModel );
		
		this.add(accountList, BorderLayout.NORTH);
	}
	
	/** 
	* Get the comboBox and the underlying model.
	* @return The comboBox. 
	*/
	public JComboBox<Account> getAccountComboBox()
	{
		return this.accountList;
	}
	
	/** 
	* Set up buttons for the mainView GUI. 
	*/
	private void setButtons()
	{
		button = new JButton[3];
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout( 1, button.length));
		
		button[0] = new JButton ("Edit in USD");
		button[1] = new JButton ("Edit in Euros");
		button[2] = new JButton ("Edit in Yuan");
		
		for(int iterator = 0; iterator < button.length; iterator++)
		{
			button[iterator].addActionListener(buttonHandler);
			buttonPanel.add(button[iterator]);
		}
		
		
		this.add( buttonPanel, BorderLayout.SOUTH);
	}
	
	/** 
	* Set up the file menu for the mainView GUI. 
	*/
	private void setFileMenu()
	{
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
	
		saveFile = new JMenuItem("Save Account File");
		saveFile.setMnemonic('S');
		saveFile.addActionListener(menuHandler);
		loadFile = new JMenuItem("Load Account File");
		loadFile.setMnemonic('L');
		loadFile.addActionListener(menuHandler);
		exit = new JMenuItem("Exit Program");
		exit.setMnemonic('x');
		exit.addActionListener(menuHandler);
		fileMenu.add(loadFile);
		fileMenu.add(saveFile);
		fileMenu.add(exit);
	}
	
	/** 
	* Set up the menu bar for the mainView GUI. 
	*/
	private void setMenuBar()
	{
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
	}
		
	/** 
	* Displays a window with pertinent information. Used to notify the user.
	* @param notification The string message to display. 
	*/
	public void displayNotification(String notification)
	{
		JOptionPane.showMessageDialog(null, notification);
	}
	
	/** 
	* Displays a window with pertinent information. Used to notify the user and allow the user to confirm or cancel an option.
	* @param notification The string message to display. 
	* @return The option selected.
	*/
	public boolean displayConfirmation(String notification)
	{
		return JOptionPane.showConfirmDialog(null, notification, "Please Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}
	
	/** 
	* Called when the menu has changed. 
	* @param account The list of currently loaded accounts. Used to re-populate the comboBox model.
	*/
	public void menuChanged(Account account)
	{
		if(accountList.getItemCount()==1)
		{
			if(accountList.getItemAt(0).getAccountID() == 0)
				accountList.removeItemAt(0);
		}
		accounts.add(account);
		controller.organizeAccounts(accounts);
		accountList.setModel( new DefaultComboBoxModel<Account>(accounts));
		accountList.setSelectedIndex(0);
	}
	
	/** 
	* Inner class used to handle buttons on the mainView.
	*/
	private class ButtonHandler implements ActionListener
	{
		/** 
		* Handles an event called from the mainView buttons. This passes the event on to the controller for handling.
		* @param event The event to handle.
		*/
		public void actionPerformed (ActionEvent event)
		{
			String action = ((JButton)event.getSource()).getText();
			controller.mainViewButtonOperations(action, (Account)accountList.getSelectedItem());
		}
		
	}
	
	/** 
	* Inner class used to handle the menu on the mainView.
	*/
	private class MenuHandler implements ActionListener
	{
		/** 
		* Handles an event called from the mainView menu. This passes the event on to the controller for handling. Note that
		* prior to this the mainView manages the event through a GUI called from within this class. This is done as the view
		* remains separated from the controller. However, portions of the menu require an additional GUI.
		* @param event The event to handle.
		*/
		public void actionPerformed (ActionEvent event)
		{
			String action = ((JMenuItem)event.getSource()).getText();
			controller.mainViewMenuOperations(action);
		}
	}
	
	/** 
	* Handles an event called from the mainView menu for selecting files
	* @param action The type of action selected to handle.
	* @return The file selected.
	*/
	public File selectFile(String action)
	{
		JFileChooser fileChooser = new JFileChooser();
		int selection;
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		if(action.equals("Load Account File"))
		{
			selection = fileChooser.showOpenDialog(null);
		}
		else
		{
			selection = fileChooser.showSaveDialog(null);
		}

	   if (selection == JFileChooser.APPROVE_OPTION) 
	   { 
	      return fileChooser.getSelectedFile();
	   }
	   return null;   
	}
	
	/** 
	* Used to clear accounts from the comboBox account list. Called when the model is rebuilt.
	*/
	public void notifyOfRemoval()
	{
		accounts.clear();
	}

	/** 
	* Handles all window closing operations. Allows for an additional option when the user attempts to close the mainView in 
	* various ways. Note that this will not be called on a force close.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowClosing(WindowEvent e) 
	{
		controller.mainViewMenuOperations("Exit Program");
	}

	/** 
	* Used to clear the comboBox by completely resetting it. 
	*/
	public void resetComboBox()
	{
		accounts.clear();
		accounts.add(new Account(this));
		controller.organizeAccounts(accounts);
		accountList.setModel( new DefaultComboBoxModel<Account>(accounts));
		accountList.setSelectedIndex(0);
	}
	
	/** 
	* Operation is not used in this implementation. Required to fully instantiate an interface.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowClosed(WindowEvent e){}

	/** 
	* Operation is not used in this implementation. Required to fully instantiate an interface.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowOpened(WindowEvent e){}

	/** 
	* Operation is not used in this implementation. Required to fully instantiate an interface.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowIconified(WindowEvent e){}

	/** 
	* Operation is not used in this implementation. Required to fully instantiate an interface.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowDeiconified(WindowEvent e){}

	/** 
	* Operation is not used in this implementation. Required to fully instantiate an interface.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowActivated(WindowEvent e){}

	/** 
	* Operation is not used in this implementation. Required to fully instantiate an interface.
	* @param e Not used in this specific instance. Required for implementation of an interface.
	*/
	public void windowDeactivated(WindowEvent e){}
	
}
