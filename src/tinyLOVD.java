import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.Action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPasswordField;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class tinyLOVD {

	private JFrame frmTinylovd;
	private JTable ErgebnisTabelle;
	private JTextField txtHost;
	private JTextField txtUser;
	private JTextField txtPort;
	private JTextField txtDatabase;
	private final Action connectaction = new Connect();
	private JPasswordField txtPassword;
	public ResultSet lastResult;
	private JButton btnQueryGo;
	private final Action goaction = new Go();
	private JTextField textStatus;
	private JTextField textSELECT;
	private JTextField textFROM;
	private JTextField textWHERE;
	private JTextField textGROUPBY;
	private JButton btnSaveasCSV;
	private JButton btnAbout;
	private JButton btnClear;
	private JButton btnLoadExample;
	
	public String hostname = "";
	public String portname = "";
	public String dbname = "";
	public String username = "";
	public String password = "";
	
	public Connection sqlConnection;
	public DefaultTableModel tabModel;
	public Statement queryStatement;
	
	public String queryBauen = "";
	public int reihenZahl = 0;
	public int spaltenZahl = 0;
	public String[] spaltenTitel;
	private JPanel titelPanel;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					tinyLOVD window = new tinyLOVD();
					window.frmTinylovd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
		            Class.forName("com.mysql.jdbc.Driver").newInstance();
		        } catch (Exception e) {
		        	e.printStackTrace();
 		        }

			}
		});
	}

	/**
	 * Create the application.
	 */
	public tinyLOVD() {
		initialize();
	}

	public void spalte_entfernen(JTable table, int col_index) {
		  TableColumn tcol = table.getColumnModel().getColumn(col_index);
		  table.removeColumn(tcol);
	}
	
	public void saveTableasCSV(JTable table, File file) {
		try 
		{
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("Query: " + queryBauen);
			bw.newLine();
			bw.write("Result: ");
			bw.newLine();
       	for(int k = 0; k < spaltenZahl; k++) 
        	{
                bw.write(spaltenTitel[k]);
                bw.write(";");
            }
			bw.newLine();
			 		
            for(int i = 0; i < reihenZahl; i++) 
            {
            	for(int j = 0; j < spaltenZahl; j++) 
            	{
            		String value = tabModel.getValueAt(i, j).toString();
	                bw.write(value);
	                bw.write(";");
	            }
	            bw.newLine();
	        }
	 
	        bw.flush();
	        bw.close();
	 
	        } 
		catch (IOException e) 
		{
			e.printStackTrace();
	    }
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTinylovd = new JFrame();
		frmTinylovd.setTitle("tinyLOVD");
		frmTinylovd.getContentPane().setBackground(new Color(255, 204, 204));
		frmTinylovd.setBounds(100, 100, 600, 550);
		frmTinylovd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinylovd.getContentPane().setLayout(null);
		
		JLabel TitelLabel = new JLabel("tinyLOVD: LOVD query and export");
		TitelLabel.setBounds(10, 11, 572, 20);
		TitelLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		TitelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frmTinylovd.getContentPane().add(TitelLabel);
		
		tabModel = new DefaultTableModel(new Object[][] {}, new String[] {});
		ErgebnisTabelle = new JTable(tabModel);
		ErgebnisTabelle.setColumnSelectionAllowed(true);
		ErgebnisTabelle.setCellSelectionEnabled(true);
		ErgebnisTabelle.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ErgebnisTabelle.setBackground(new Color(255, 255, 204));
		ErgebnisTabelle.setFillsViewportHeight(true);
		JScrollPane scrollErgebnis = new JScrollPane(ErgebnisTabelle);
		scrollErgebnis.setVerticalScrollBarPolicy(scrollErgebnis.VERTICAL_SCROLLBAR_ALWAYS);
		scrollErgebnis.setBounds(10, 238, 572, 200);
		frmTinylovd.getContentPane().add(scrollErgebnis);
	
		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(10, 42, 86, 14);
		frmTinylovd.getContentPane().add(lblHost);
		
		txtHost = new JTextField();
		txtHost.setText("db11.net-server.de");
		txtHost.setBounds(10, 61, 86, 20);
		frmTinylovd.getContentPane().add(txtHost);
		txtHost.setColumns(10);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(201, 42, 86, 14);
		frmTinylovd.getContentPane().add(lblUser);
		
		txtUser = new JTextField();
		txtUser.setText("netsh10373");
		txtUser.setBounds(201, 61, 86, 20);
		frmTinylovd.getContentPane().add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(297, 42, 86, 14);
		frmTinylovd.getContentPane().add(lblPassword);
		
		JLabel lblDatabase = new JLabel("Database:");
		lblDatabase.setBounds(393, 42, 86, 14);
		frmTinylovd.getContentPane().add(lblDatabase);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(106, 42, 85, 14);
		frmTinylovd.getContentPane().add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setText("3306");
		txtPort.setBounds(106, 61, 86, 20);
		frmTinylovd.getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		txtDatabase = new JTextField();
		txtDatabase.setText("gnome");
		txtDatabase.setBounds(393, 61, 86, 20);
		frmTinylovd.getContentPane().add(txtDatabase);
		txtDatabase.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setAction(connectaction);
		btnConnect.setBounds(493, 60, 89, 23);
		frmTinylovd.getContentPane().add(btnConnect);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(297, 61, 86, 20);
		frmTinylovd.getContentPane().add(txtPassword);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 488, 46, 14);
		frmTinylovd.getContentPane().add(lblStatus);
		
		textStatus = new JTextField();
		textStatus.setBounds(66, 485, 413, 20);
		frmTinylovd.getContentPane().add(textStatus);
		textStatus.setColumns(10);
		
		textSELECT = new JTextField("");
		textSELECT.setBounds(10, 123, 181, 20);
		frmTinylovd.getContentPane().add(textSELECT);
		textSELECT.setColumns(10);
		
		JLabel lblSelect = new JLabel("SELECT");
		lblSelect.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelect.setBounds(10, 98, 46, 14);
		frmTinylovd.getContentPane().add(lblSelect);
		
		JLabel lblFrom = new JLabel("FROM");
		lblFrom.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFrom.setBounds(201, 98, 46, 14);
		frmTinylovd.getContentPane().add(lblFrom);
		
		textFROM = new JTextField("");
		textFROM.setBounds(201, 123, 182, 20);
		frmTinylovd.getContentPane().add(textFROM);
		textFROM.setColumns(10);
		
		JLabel lblWhereoptional = new JLabel("WHERE (optional)");
		lblWhereoptional.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblWhereoptional.setBounds(10, 154, 189, 14);
		frmTinylovd.getContentPane().add(lblWhereoptional);
		
		textWHERE = new JTextField("");
		textWHERE.setBounds(10, 179, 181, 20);
		frmTinylovd.getContentPane().add(textWHERE);
		textWHERE.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(5, 92, 582, 117);
		frmTinylovd.getContentPane().add(panel);
		panel.setLayout(null);
		
		textGROUPBY = new JTextField("");
		textGROUPBY.setBounds(196, 86, 182, 20);
		panel.add(textGROUPBY);
		textGROUPBY.setColumns(10);
		
		JLabel lblGroupby = new JLabel("GROUP BY (optional)");
		lblGroupby.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblGroupby.setBounds(196, 61, 117, 14);
		panel.add(lblGroupby);
		
		btnLoadExample = new JButton("Load example query");
		btnLoadExample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textSELECT.setText("`Variant/Exon`, count(`Variant/Exon`)");
				textFROM.setText("lovd_RB1_variants");
				textWHERE.setText("");
				textGROUPBY.setText("`Variant/Exon`");
			}
		});
		btnLoadExample.setEnabled(false);
		btnLoadExample.setBounds(388, 85, 189, 23);
		panel.add(btnLoadExample);
		
		btnQueryGo = new JButton("Go");
		btnQueryGo.setBounds(388, 30, 86, 23);
		panel.add(btnQueryGo);
		btnQueryGo.setEnabled(false);
		
		btnClear = new JButton("Clear");
		btnClear.setBounds(488, 30, 89, 23);
		panel.add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textSELECT.setText("");
				textFROM.setText("");
				textWHERE.setText("");
				textGROUPBY.setText("");
			}
		});
		btnClear.setEnabled(false);
		btnQueryGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lblResult = new JLabel("Result:");
		lblResult.setBounds(10, 219, 46, 14);
		frmTinylovd.getContentPane().add(lblResult);
		
		btnSaveasCSV = new JButton("Save results as CSV");
		btnSaveasCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File speicherDatei = new File("*.csv");
				JFileChooser fc = new JFileChooser(speicherDatei);
				fc.setSelectedFile(speicherDatei);
			    fc.setFileFilter(new FileFilter()
			    {
			    	@Override public boolean accept( File f )
			    	{
			    		return f.isDirectory() || f.getName().toLowerCase().endsWith( ".csv" );
			    	}
			    @Override public String getDescription()
			    {
			    	return "Comma-separated values";
			    }
			    } );
				if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
				{
					File selectedFile = fc.getSelectedFile();
				    saveTableasCSV(ErgebnisTabelle, selectedFile);
				}
			}
		});
		btnSaveasCSV.setEnabled(false);
		btnSaveasCSV.setBounds(7, 449, 575, 23);
		frmTinylovd.getContentPane().add(btnSaveasCSV);
		
		btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "tinyLOVD: (c) 2012 christina.czeschik@uni-due.de", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnAbout.setFont(new Font("Tahoma", Font.ITALIC, 11));
		btnAbout.setForeground(Color.BLUE);
		btnAbout.setBounds(493, 484, 89, 23);
		frmTinylovd.getContentPane().add(btnAbout);
		
		titelPanel = new JPanel();
		titelPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		titelPanel.setBounds(155, 11, 282, 20);
		frmTinylovd.getContentPane().add(titelPanel);
		
	}
	private class Connect extends AbstractAction {
		public Connect() {
			putValue(NAME, "Connect");
			putValue(SHORT_DESCRIPTION, "Connect to LOVD");
		}
		public void actionPerformed(ActionEvent e) {
			hostname = "";
			portname = "";
			dbname = "";
			username = "";
			password = "";
			try {
	            hostname = txtHost.getText();
	            portname = txtPort.getText();
	            dbname = txtDatabase.getText();
	            username = txtUser.getText();
	            password = new String(txtPassword.getPassword());
	            
	        } catch (Exception ex) {
				textStatus.setText("An error has occurred. Please check connection settings.");
		        }
			try {
				
				sqlConnection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + portname + "/" + dbname, username, password);
				Statement stmt = sqlConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet connTextRS = stmt.executeQuery("SELECT `symbol` FROM `lovd_genes`");
				while (connTextRS.next() == true)
				{
					textStatus.setText("Successfully connected to " + connTextRS.getString("symbol") + " database.");
					btnQueryGo.setEnabled(true);
					btnQueryGo.setAction(goaction);
					btnClear.setEnabled(true);
					btnLoadExample.setEnabled(true);

				}
			}
				catch (Exception ex) {
					textStatus.setText("An error has occurred. Connection to database could not be established.");
	    	}
		}
	}

	private class Go extends AbstractAction {
		public Go() {
			putValue(NAME, "Go");
			putValue(SHORT_DESCRIPTION, "Start Query");
		}
		public void actionPerformed(ActionEvent e) {

			try {
				queryStatement = sqlConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				queryBauen = "SELECT " + textSELECT.getText() + " FROM " + textFROM.getText();
				if(textWHERE.getText().equals("") == false) {
					queryBauen = queryBauen + " WHERE " + textWHERE.getText();
				}
				if(textGROUPBY.getText().equals("") == false) {
					queryBauen = queryBauen + " GROUP BY " + textGROUPBY.getText();
				}

				tabModel = new DefaultTableModel(new Object[][] {}, new String[] {});
				ErgebnisTabelle.setModel(tabModel);
				// JOptionPane.showMessageDialog(null, queryBauen);
				lastResult = queryStatement.executeQuery(queryBauen);
				ResultSetMetaData lastMetaData = lastResult.getMetaData();
				spaltenZahl = lastMetaData.getColumnCount();
				spaltenTitel = new String[spaltenZahl];
				for(int h = 1; h <= spaltenZahl; h++) {
					spaltenTitel[h - 1] = lastMetaData.getColumnName(h); 
			    }
				// JOptionPane.showMessageDialog(null, "" + spaltenZahl);
				for(int h = 1; h <= spaltenZahl; h++) {
					tabModel.addColumn(spaltenTitel[h - 1]);
			    }

			while(lastResult.next() == true) {
				Object[] reihe = new Object[spaltenZahl];
				for(int i = 0; i < spaltenZahl; i++) {
					reihe[i] = lastResult.getString(i+1);
				}
				tabModel.addRow(reihe);
			}
			lastResult.last();
			reihenZahl = lastResult.getRow();
			textStatus.setText("" + reihenZahl + " rows retrieved");
			btnSaveasCSV.setEnabled(true);
			
			}
				catch (Exception ex) {
					textStatus.setText("An error has occurred while processing your query.");
	    	}
		}
	}
}
