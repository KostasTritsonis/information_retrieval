package view;

import src.WriteDocument;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.List;

public class ClientView implements ActionListener{
	private JFrame frame;
	private JTextField textField;
	private JButton SubmitButton,NextButton,PreviousButton,ClearButton;
	private JLabel lblNewLabel,lblNewLabel_2,lblNewLabel_3,lblNewLabel_4,lblNewLabel_5;
	private JComboBox<String> comboBox_1;
	private JTextPane textArea;
	private WriteDocument w;
	private JScrollPane scrollPane;
	private String text="";
	private String[] text1;
	private int counter,totalpage=0;
	private int page = 0;
	ArrayList<String> history;
	private List list_1;
	
	public  ClientView() {
		
		initialize();
		
	}
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(500, 400, 1006, 710);
		frame.setVisible(true);
		frame.setTitle("MovieSearchEngine");
		ImageIcon img = new ImageIcon("src\\movie.png");
		frame.setIconImage(img.getImage());
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 295, 968, 355);
        frame.getContentPane().add(scrollPane);
        frame.setFocusable(true);
        try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        textField = new JTextField();
        textField.setForeground(Color.BLACK);
        textField.setBounds(10, 31, 271, 35);
        textField.setBackground(Color.WHITE);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        SubmitButton = new JButton("Submit");
        SubmitButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        SubmitButton.setBounds(183, 71, 98, 29);
        SubmitButton.setBackground(Color.WHITE);
        frame.getContentPane().add(SubmitButton);
        
        lblNewLabel = new JLabel("Search");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setBounds(10, 11, 98, 22);
        frame.getContentPane().add(lblNewLabel);
        
        textArea = new JTextPane();
        textArea.setContentType("text/html");
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
        
        NextButton = new JButton("Next");
        NextButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        NextButton.setBackground(Color.WHITE);
        NextButton.setBounds(889, 261, 89, 23);
        frame.getContentPane().add(NextButton);

        
        PreviousButton = new JButton("Previous");
        PreviousButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        PreviousButton.setBackground(Color.WHITE);
        PreviousButton.setBounds(790, 261, 89, 23);
        frame.getContentPane().add(PreviousButton);
        
        lblNewLabel_2 = new JLabel("Sort by:");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setBounds(10, 265, 46, 14);
        frame.getContentPane().add(lblNewLabel_2);
        
        comboBox_1 = new JComboBox<>();
        comboBox_1.setBackground(Color.WHITE);
        comboBox_1.setBounds(55, 266, 61, 18);
        frame.getContentPane().add(comboBox_1);
        
        lblNewLabel_3 = new JLabel("Page: 0 of 0");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setBounds(891, 653, 99, 18);
        frame.getContentPane().add(lblNewLabel_3);
        
        list_1 = new List();
        list_1.setBounds(673, 36, 305, 148);
        frame.getContentPane().add(list_1);
        
        lblNewLabel_4 = new JLabel("History");
        lblNewLabel_4.setForeground(Color.WHITE);
        lblNewLabel_4.setBounds(808, 15, 46, 14);
        frame.getContentPane().add(lblNewLabel_4);
        
        lblNewLabel_5 = new JLabel("Found - hits");
        lblNewLabel_5.setForeground(Color.WHITE);
        lblNewLabel_5.setBounds(673, 270, 107, 14);
        frame.getContentPane().add(lblNewLabel_5);
        
        ClearButton = new JButton("Clear");
        ClearButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        ClearButton.setBounds(673, 188, 89, 23);
        frame.getContentPane().add(ClearButton);
    
        page++;
        
        comboBox_1.addItem("None");
        comboBox_1.addItem("Title");
        comboBox_1.addItem("Rating");
        
        
        SubmitButton.addActionListener(this);
        NextButton.addActionListener(this);
        PreviousButton.addActionListener(this);
        comboBox_1.addActionListener(this);
        ClearButton.addActionListener(this);
        
        list_1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
            	int index = list_1.getSelectedIndex();
    	        textField.setText(list_1.getItem(index));
  
            }
         });
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource()==SubmitButton) {
			if(textField.getText().equals("")) return;
			String s="";
			text="";
			int size = 10;
			counter = 0;
			page = 1;
			
			w = new WriteDocument(textField.getText(),comboBox_1.getSelectedItem().toString());
			
			try {
				s = w.search();
				w.writeFile(textField.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			text1 = s.split("<br><br>");
			this.lblNewLabel_5.setText("Found " + text1.length + " hits");
			
			if(text1.length%10==0) {
				totalpage=text1.length/10;
			}else {
				totalpage=(text1.length/10)+1;
			}
			
			this.lblNewLabel_3.setText("Page: "+page+" of "+totalpage);
			
			if(size>text1.length) {
				size=text1.length;
			}
			
			for(int i=0; i<size; i++) {
				text += text1[i]+"<br><br>";
				
			}
			list_1.clear();
			setHistory();
			textArea.setText(text);
		}
		
		else if(e.getSource()==NextButton) {
			counter++;
			String s="";
			int limit = 10*counter;
			if(text1 == null || limit >= text1.length) {
				counter--;
				return;
			}
			page++;
			this.lblNewLabel_3.setText("Page: "+page+" of "+totalpage);
			
			
			
			for(int i=limit; i<limit+10 && i<text1.length; i++) {
				s+=text1[i]+"<br><br>";
			}
			
			textArea.setText(s);
		}
		
		else if(e.getSource()==PreviousButton) {
			String s="";
			int limit =10*counter;
			if(counter==0 || text1==null) return;
			page--;
			this.lblNewLabel_3.setText("Page: "+page+" of "+totalpage);
			
			if(limit>text1.length) {
				limit = text1.length;
			}
			
			for(int i=limit-10; i<=limit-1; i++) {
				s+=text1[i]+"<br><br>";
			}
			counter--;
			textArea.setText(s);
		}
		
		else if(e.getSource()==ClearButton) {
			
			w.EmptyFile();
			list_1.clear();
		}
		
	}
	
	public void setHistory() {
		history = w.readFile();
		for(String line: history) {
			list_1.add(line);
		}
	}
	
	public static void main(String[] args) {
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						new ClientView();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
}
