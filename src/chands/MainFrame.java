package chands;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.beans.PropertyChangeEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import java.awt.Color;

public class MainFrame {

	private JFrame frmMyNotepad;
	private JPanel panel;
	private static JButton btnDeleteNote;
	private JButton btnCreateNote;
	
	private static ListModel<StringBuilder> listModel;
	
	private static JButton btnEditNote;
	
	private static Singleton singleton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					singleton = Singleton.getInstance();
					
					MainFrame window = new MainFrame();
					window.frmMyNotepad.setVisible(true);
					
					//Add Static contents to the list
					/*singleton.setTextListElement(new StringBuilder("Test"));
					singleton.setTextListElement(new StringBuilder("Fell"));
					singleton.setTextListElement(new StringBuilder("Suresh"));
					singleton.setTextListElement(new StringBuilder("Chand"));*/
										 
					
					//method to set the state of the button if certain rules are met
					setStateOfDeleteButton();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMyNotepad = new JFrame();
		frmMyNotepad.setTitle("My Notepad");
		frmMyNotepad.setBounds(100, 100, 624, 641);
		frmMyNotepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMyNotepad.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		frmMyNotepad.getContentPane().add(panel, BorderLayout.SOUTH);
		
		btnDeleteNote = new JButton("Delete Note");
		btnDeleteNote.setBackground(new Color(255, 228, 196));

		btnDeleteNote.setToolTipText("Click to delete selected note");
		btnDeleteNote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		btnCreateNote = new JButton("Create Note");
		btnCreateNote.setBackground(new Color(152, 251, 152));
		btnCreateNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TextEditor te = new TextEditor("Create Note");
				te.setVisible(true);
				
				//set a temporary variable to store MainFrame make sure we can call back the frame
				singleton.setTempMainFrame(frmMyNotepad);
				
				//hide the MainFrame
				frmMyNotepad.setVisible(false);
				
			}
		});
		btnCreateNote.setToolTipText("Click to create a new note");
		btnCreateNote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		btnEditNote = new JButton("Edit Note");
		btnEditNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//this is used to temporarily set the selected element in the list
				singleton.setSelectectedUIIndex(singleton.getJListNotePad().getSelectedIndex());
				
				//set a temporary variable to store MainFrame make sure we can call back the frame
				singleton.setTempMainFrame(frmMyNotepad);
				
				TextEditor te = new TextEditor("Edit Note");
				
				//sets the visibility of text editor
				te.setVisible(true);
				
				//hide the MainFrame
				frmMyNotepad.setVisible(false);
				
				
			}
		});
		btnEditNote.setToolTipText("Click to delete selected note");
		btnEditNote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(31)
					.addComponent(btnDeleteNote)
					.addGap(49)
					.addComponent(btnEditNote, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
					.addGap(52)
					.addComponent(btnCreateNote)
					.addGap(59))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(18, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEditNote, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCreateNote, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDeleteNote, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		JLabel lblAppTitle = new JLabel("My Notepad");
		lblAppTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAppTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		frmMyNotepad.getContentPane().add(lblAppTitle, BorderLayout.NORTH);
		
		
				
		//listener has been added to check the state of the list
		singleton.getJListNotePad().addListSelectionListener(new ListSelectionListener() {
			
			//if an item has been selected, it will enable the delete button
			public void valueChanged(ListSelectionEvent e) {
				
				btnDeleteNote.setEnabled(true);
				setStateOfDeleteButton();
			}
		});
		
		singleton.getJListNotePad().setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null)); //changes the style of the menu
		
		/*
		 * data model that provides a List with its contents. 
		 */
		listModel = new AbstractListModel<StringBuilder>() {
			/**
			 * GENERATED: This is serial runtime version, which is used to force the class version (AbstractListModel) to one specified. This avoids any compatibility issues with the users compiler??
			 * 
			 */
			private static final long serialVersionUID = 2573967083497276870L;
			public int getSize() {
				return singleton.getList().size();
			}
			
			public StringBuilder getElementAt(int index) {
				return singleton.getList().get(index);
			}
			
		};
		
		//Creates a list model which is a child of JMenu
		
		singleton.getJListNotePad().setModel(listModel);
		
		frmMyNotepad.getContentPane().add(singleton.getJListNotePad(), BorderLayout.CENTER); 
		
		
		btnDeleteNote.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//remove item from list if the button is pressed
				try {
					singleton.removeElementInList(singleton.getJListNotePad().getSelectedIndex());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//updateUI
				singleton.getJListNotePad().updateUI();
				
				//call method to change state of button
				setStateOfDeleteButton();
			}
		});
			

		
		//can only select one item at a time
		singleton.getJListNotePad().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
	}

	protected static void setStateOfDeleteButton() {
		// TODO Auto-generated method stub
		//disables the button if the list is empty OR if an item in the list hasnt been selected OR if the last item in the list has been deleted
		if(listModel.getSize() < 1 || singleton.getJListNotePad().getSelectedIndex() == -1 || singleton.getJListNotePad().getSelectedIndex() == singleton.getList().size())
		{
			
			btnDeleteNote.setEnabled(false);
			btnEditNote.setEnabled(false);
		}
		else
		{
			btnDeleteNote.setEnabled(true);
			btnEditNote.setEnabled(true);
		}
		
		System.out.println(singleton.getJListNotePad().getSelectedIndex());
	}
	
}
