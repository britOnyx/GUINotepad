package chands;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;

public class Singleton {

	private static Singleton singleton;
	private static ArrayList<StringBuilder> txtList; //gets the list content
	private int selectedTemp; //used to get the selected item from the list
	private JFrame frmMyNotepad;
	private static JList<StringBuilder> notePadList; //Component which holds the ArrayList 'txtList'
	
	//DBM VARIABLES
	private Connection connect;
	private Statement statement;
	private DatabaseMetaData dbm; //interface which implements DBMS with JDBC driver
	
	private Singleton() throws ClassNotFoundException, SQLException
	{
		//initialise variables
		if(txtList == null)
		{
			txtList = new ArrayList<StringBuilder>();
		}
		
		if(notePadList == null)
		{
			notePadList = new JList<>();
		}
		
		//initialise database
		if(connect == null)
		{
			Class.forName("org.sqlite.JDBC");
			connect = DriverManager.getConnection("jdbc:sqlite:list.db");
			
			initialise();
		}
		
	}
	
    private void initialise() throws SQLException {

    	//initialises vairables
    	statement = connect.createStatement();
    	dbm = connect.getMetaData();
		
    	ResultSet table = dbm.getTables(null, null, "lists", null);
    	
    	//if the table doesnt exist
    	if(!table.next())
    	{
    		//create the table in the database
    		statement.execute("CREATE TABLE lists(noteID INTEGER PRIMARY KEY, " +
    						"contents varchar(6500) NOT NULL);");
    		
    		System.out.println("Created Lists Table!");
    	}else
    	{
    		
    		alterNoteID();
    		
    		ResultSet res = statement.executeQuery("SELECT * FROM lists");
    		
    		//get the data
    		while(res.next())
    		{
    			//int id = res.getInt("listItemID");
    			StringBuilder contents = new StringBuilder(res.getString("contents"));
    			txtList.add(contents);
    		}
    	}
    	
    	
	}

	public static Singleton getInstance() {
		if(singleton == null)
		{
			try {
				singleton = new Singleton();
				
				
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		}
		return singleton;
    } 
    
	//adds elements to database
	public void saveDataToDB(int posn, StringBuilder contents) throws SQLException
	{
		PreparedStatement prep = connect.prepareStatement("INSERT INTO lists values(?,?);");
		
		prep.setInt(1, posn);
		prep.setString(2, contents.toString());
		
		prep.execute();
		
	}
	
	public void getDataFromDB(int posn) throws SQLException
	{
		PreparedStatement prep = connect.prepareStatement("SELECT * FROM lists WHERE noteID = " + posn);
		
		prep.execute();
		
		ResultSet rs = prep.getResultSet();
				
		int notePosn;
		String noteContents;
		
		
		if(rs.next())
		{
			notePosn = rs.getInt(1);
			noteContents = rs.getString(2);
			
			System.out.println("getDataFromDB: Note Posn - " + notePosn + "\n Note Contents: " + noteContents);
			

			
		}else
		{
			System.out.println("Results exists! ");
		}
	}
	


	//gets the text element from the list
    public StringBuilder getTextListElement(int pos)
    {
    	return txtList.get(pos);
    }
    
    //adds an item to the list
    public void setTextListElement(StringBuilder contents) throws SQLException
    {
    	//add note to the list
    	txtList.add(contents);
    	
    	//then, add to the DATABASE
    	saveDataToDB(txtList.size()+1,contents);
    	System.out.println("list Posn " + txtList.size());
    }
	
    
    //replaces the contents in the list based on the position given
    public void editTextListElement(int pos, StringBuilder contents)
    {
    	txtList.set(pos,contents);
    }
    
    //gets the ArrayList
    public ArrayList<StringBuilder> getList()
    {
    	return txtList;
    }
    
    
    public int getSelectectedUIIndex()
    {
    	return selectedTemp;
    }
    
    public void setSelectectedUIIndex(int tempValue)
    {
    	selectedTemp = tempValue;
    }
    
    public JFrame getTempMainFrame()
    {
    	return frmMyNotepad;
    }
    
    public void setTempMainFrame(JFrame temp)
    {
    	frmMyNotepad = temp;
    }
    
    public void removeElementInList(int pos) throws SQLException
    {
    	int DBPosn = pos+1;
    	//remove item from list
    	txtList.remove(pos);
    	
    	System.out.println("removeElementInList DB: " + DBPosn);
    	
    	
    	getDataFromDB(DBPosn);
    	    	
    	//remove item from DB
    	removeElementFromDB(DBPosn);
    }

    public JList<StringBuilder> getJListNotePad()
    {
    	return notePadList;
    }
    
    //remove elements to database
  	public void removeElementFromDB(int posn) throws SQLException
  	{

  		//item deleted
  		PreparedStatement prep = connect.prepareStatement("DELETE FROM lists WHERE noteID = " + posn);
  		prep.execute();
  		
		//re-align the note Posn and alter the noteID
  		alterNoteID();

  	}

    private void alterNoteID() throws SQLException {
		// gets size of list
    	PreparedStatement prepSize = connect.prepareStatement("SELECT COUNT(*) FROM lists;");
    	prepSize.execute();
		ResultSet rsSize = prepSize.getResultSet();
		
		
		int posn=1;
		int size =rsSize.getInt(1);
		System.out.println("new Size: " + size);
		
		prepSize = connect.prepareStatement("SELECT * FROM lists;");
		prepSize.execute();
		ResultSet rs = prepSize.getResultSet();
		
		
		while(rs.next())
		{
			
			int oldNoteID = rs.getInt("noteID");
			
			System.out.println("noteID: " +oldNoteID);
			
			//if the noteID doesnt match
			if(oldNoteID != posn)
			{
				System.out.println("Does Not Match!");
				
				System.out.println("NoteID: " + oldNoteID);
				System.out.println("current Posn: " +posn);
				
				//alter noteID to current value in posn
				prepSize = connect.prepareStatement("UPDATE lists SET noteID = " + posn + " WHERE noteID = " +oldNoteID);
				prepSize.execute();
			}else {
				
				System.out.println("Matches!");
			}
			posn++;
		}
	}
}
