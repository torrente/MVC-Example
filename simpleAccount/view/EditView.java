package assignment3.simpleAccount.view;

import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import assignment3.simpleAccount.controller.Controller;
import assignment3.simpleAccount.model.Account;

/** 
 * A class which defines views for specific accounts. These views are not unique and multiple copies can be opened.
 * This view is used to deposit or withdraw funds from an account.
 * 
 * @author dTorrente
 */
public class EditView extends JFrame{

	
	private static final long serialVersionUID = 5653683117109503921L;
	private Account accountToEdit;
	private Controller controller;
	private JLabel avabFundsLabel;
	private JLabel enterFundsLabel;
	private JButton[] editButtons;
	private JTextField currentFunds;
	private JTextField editAmount;
	private JPanel buttonPanel;
	private JPanel editPanel;
	private JPanel currentFundsPanel;
	private DecimalFormat decimalFormat;
	
	private float fundMultiplier;
	
	public ButtonHandler buttonHandler;
	
	/**
	* Constructor to create a view used for editing accounts.
	* 
	* @param accountToEdit The account (model) which this view is to be tied to.
	* @param controller The controller which this view is created from.
	* @param editMethod The type of method that was used to create the editView. This is used to properly configure 
	* the view based on what type of funds should be displayed as well as the title.
	* 
	*/
	public EditView(Account accountToEdit, Controller controller, String editMethod)
	{
		super(accountToEdit.generateStringForEditView() + " " + editMethod);
		this.setSize(400, 68);
		this.setResizable(false);
	
		decimalFormat = new DecimalFormat("#.00");
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
		
		this.accountToEdit = accountToEdit;
		accountToEdit.addEditModelListener(this);
		this.controller = controller;
		
		buttonHandler = new ButtonHandler();
		setButtons();
		setFundDisplay();
		if(editMethod.equals("Edit in USD"))
		{
			enterFundsLabel.setText("Enter amount in USD: ");
			setFundMultiplier(1.00f);
		}
		else if(editMethod.equals("Edit in Euros"))
		{
			enterFundsLabel.setText("Enter amount in Euros: ");
			setFundMultiplier(0.94f);
		}
		else if(editMethod.equals("Edit in Yuan"))
		{
			enterFundsLabel.setText("Enter amount in Yuan: ");
			setFundMultiplier(6.91f);
		}
		
		updateAvabFundDisplay();
		
		this.setVisible(true);
		
	}
	
	/** 
	* Set up buttons for the editView GUI. 
	*/
	private void setButtons()
	{
		editButtons = new JButton[3];
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout( 1, editButtons.length));
		
		editButtons[0] = new JButton ("Deposit");
		editButtons[1] = new JButton ("Withdraw");
		editButtons[2] = new JButton ("Dismiss");
		
		for(int iterator = 0; iterator < editButtons.length; iterator++)
		{
			editButtons[iterator].addActionListener(buttonHandler);
			buttonPanel.add(editButtons[iterator]);
		}
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/** 
	* Set up additional display elements such as the total available funds as well as an editable text field for input. 
	*/
	private void setFundDisplay()
	{
		editPanel = new JPanel();
		editPanel.setLayout(new GridLayout(1, 2));
		
		currentFundsPanel = new JPanel();
		currentFundsPanel.setLayout(new GridLayout(1, 2));
		
		avabFundsLabel = new JLabel();
		avabFundsLabel.setText("Funds available:");
		enterFundsLabel = new JLabel();
		
		editAmount = new JTextField();
		editAmount.setText("0.00");
		
		currentFunds = new JTextField();
		currentFunds.setEditable(false);
		currentFunds.setText(Float.toString(accountToEdit.getFunds() * fundMultiplier ));
		
		editPanel.add(enterFundsLabel);
		editPanel.add(editAmount);
		
		currentFundsPanel.add(avabFundsLabel);
		currentFundsPanel.add(currentFunds);
		
		this.add(editPanel, BorderLayout.CENTER);
		this.add(currentFundsPanel, BorderLayout.NORTH);
	}
	
	/** 
	* Set the fund multiplier to properly display funds. Note that it does not actually modify the funds.
	* @param value The multiplier to use in order to properly display funds. 
	*/
	private void setFundMultiplier(float value)
	{
		this.fundMultiplier = value;
	}
	
	/** 
	* Reset the editable text field. 
	*/
	public void resetEditAmountDisplay()
	{
		editAmount.setText("0.00");
	}
	
	/** 
	* Updates the value of current funds. Called in response to the model being updated. 
	*/
	public void updateAvabFundDisplay()
	{
		String valueToDisplay;
		valueToDisplay = decimalFormat.format(accountToEdit.getFunds() * fundMultiplier);
		currentFunds.setText(valueToDisplay);
		resetEditAmountDisplay();
	}
	
	/** 
	* Gets the account (model) that this view is listening to.
	* @return The account (model) that this view is listening to. 
	*/
	public Account getListenTarget()
	{
		return this.accountToEdit;
	}
	
	/** 
	* Gets this view.
	* @return This view. 
	*/
	public EditView getEditView()
	{
		return this;
	}
	
	/** 
	* Gets the fund multiplier.
	* @return The fund multiplier. 
	*/
	public float getFundMultiplier()
	{
		return this.fundMultiplier;
	}
	
	/** 
	* Inner class which handles actions from buttons on this view.
	*  
	*/
	class ButtonHandler implements ActionListener
	{
		/** 
		* Handles an event. This method passes the event to the controller to handle.
		* @param event The event that was initiated. 
		*/
		public void actionPerformed (ActionEvent event)
		{
			String action = ((JButton)event.getSource()).getText();
			controller.editViewOperations(action, editAmount.getText(), getEditView());
		}
	}
	
	
	
}
