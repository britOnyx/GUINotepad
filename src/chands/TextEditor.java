package chands;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;

public class TextEditor extends JFrame {

	private JPanel contentPane;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextEditor frame = new TextEditor("Text Editor");
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param strNoteText - gets the list text when editing
	 * @param listPosition - get the position of the text in the array
	 * @param frameTitle - title of the frame
	 */
	public TextEditor(String frameTitle) {
		JTextArea textArea = new JTextArea();
		
		if(frameTitle.contains("Edit Note")) {
			//textArea.setText(singleton.getTextListElement(singleton.getSelectectedUIIndex()).toString());
			//textArea.setText(Singleton.getInstance().getTextListElement(Singleton.getInstance().getSelectectedUIIndex()).toString());
			textArea.setText(Singleton.getInstance().getList().get(Singleton.getInstance().getSelectectedUIIndex()).toString());
			
			System.out.println("editing... ");
		}else {
			System.out.println("creating...");
		}
		
		setTitle(frameTitle);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 506, 825);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnSaveNote = new JButton("Save Note");
		btnSaveNote.setBackground(new Color(152, 251, 152));
		btnSaveNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				if(frameTitle.contains("Edit Note")) {
					
					System.out.println("Editing Note....");

					if(!textArea.getText().equals(""))
					{
						System.out.println("Editing Note....");
						Singleton.getInstance().editTextListElement(Singleton.getInstance().getSelectectedUIIndex(), new StringBuilder(textArea.getText()));
						
						Singleton.getInstance().getTempMainFrame().setVisible(true);
						dispose();
					}else
					{
						frameERROR err = new frameERROR();
						err.setVisible(true);
						err.setAlwaysOnTop(true);
					}
					
				}else //else its save note
				{
					if(!textArea.getText().equals("")) {
						try {
							Singleton.getInstance().setTextListElement(new StringBuilder(textArea.getText()));
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
						System.out.println("Saving Text: " + textArea.getText());
						
						Singleton.getInstance().getJListNotePad().updateUI();
						
						Singleton.getInstance().getTempMainFrame().setVisible(true);
						
						Singleton.getInstance().setTempMainFrame(null); // clear the variable before I close the editor
						
						//close JFrame Editor
						dispose();
					}else {
						
						frameERROR err = new frameERROR();
						err.setVisible(true);
						err.setAlwaysOnTop(true);
					}
				}
				

				
				
				
				
			}
		});
		
		
		btnSaveNote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBackground(new Color(255, 228, 196));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
					.addComponent(btnSaveNote, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
						.addComponent(btnSaveNote, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		contentPane.add(textArea, BorderLayout.CENTER);
		
		this.addWindowListener(new WindowListener() {
			

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				
				// This will set the visibility of the MainFrame
				Singleton.getInstance().getTempMainFrame().setVisible(true);
			}
		});

	}
}
