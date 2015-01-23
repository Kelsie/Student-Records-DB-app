/*
 *  Class CreateNewClassFile calls class RecordUI (upper and middle pane)and add 
 *  the button panel, here with 3 buttons and adds functionality for buttons open,
 *  save record and close.
 *  This form is called from SelectClassTeacherForm 'createBtn'.
 *  This form is one of the two main forms of this application (creating the class 
 *  file and writing records to file).
 */
package student.records.db;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.JInternalFrame;
import static student.records.db.StudentRecordsDB.*;
import static student.records.db.SelectClassTeacher.*;

/**
 *
 * @author mairead cousins
 */
public class CreateNewClassFile extends JInternalFrame implements MouseListener{
    private ObjectOutputStream output;
    private JPanel btnPane;
    private JPanel parentPane;
    private RecordUI recordPane;
    private JButton saveRecordBtn, createBtn, closeBtn;
   
     //public RecordUICreate() {}
    public CreateNewClassFile() {
        
        super("Create student records and class file", false, false, false, false);
        getContentPane().setLayout(new BorderLayout());
        parentPane=new JPanel(new BorderLayout());
        recordPane=new RecordUI();
       
        //Set teacherName and className in the recordPane
        recordPane.classNameLbl.setText(cName);
        recordPane.teacherNameLbl.setText(tName);
        
        //create the input ButtonPanel
        btnPane=new JPanel();
        Dimension dimBtnPane=new Dimension(670, 65);
        btnPane.setPreferredSize(dimBtnPane);
        btnPane.setLayout(new FlowLayout());
        createBtn = new JButton("Create Class File");
        createBtn.addActionListener(
                new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        UIManager.put("OptionPane.messageFont",new FontUIResource(new Font("Verdana", Font.BOLD, 13 )));
                               
                        Object[] options= {"Confirm", "Cancel"};
                        String classFileName=recordPane.getFileName();
                        int n = JOptionPane.showOptionDialog(null,
                        "This creates a new file '"+classFileName+"'.\nIf '"+classFileName+
                        "' already exists in this folder\n its contents will be deleted and a new file created."+
                        "\nNavigate to a suitable folder (eg. My Documents) and create a new folder"+
                         "\nin which to store class files (eg. Student Records). "+        
                        "\nDo you wish to continue?",
                        "Confirm New File Dialog",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
                        if (n==0) openFile();
                        
                                
                    }  //end actionPerformed()
                }//end anonymouse Inner class
        ); //end addActionListener()
        btnPane.add(createBtn);
        
        saveRecordBtn = new JButton("Save Record");
        saveRecordBtn.setEnabled(false);
        saveRecordBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                         if(recordPane.checkInputFields())
                            saveRecord();
                        else JOptionPane.showMessageDialog(null,"Enter Student 'first name' and "
                                + " 'last name' fields\n before Save Record.");
                        
                    }//end actionPerformed()
                }//end anonymous inner class
        ); //end addActionListener()
        btnPane.add(saveRecordBtn);
        
        closeBtn = new JButton("close");
        closeBtn.addActionListener((ActionEvent e) -> {
            closeFile();
        } //end actionPerformed()
        //end anonymous inner class
        ); //end addActionListener()
        btnPane.add(closeBtn);
        
        //From class StudentRecordsDB, Static member,'parentFrame', is an
        //object of StudentRecordsDB (the only JFrame). Initialized in main().
        parentFrame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (output != null) {
                            closeFile();
                        } //end if
                        else {
                            System.exit(0);
                        }
                    } //end windowClosing()
                }//end anonymous innerclass WindowAdapter
        );  //end addWindowListener()

        parentPane.add(recordPane, BorderLayout.CENTER);
        parentPane.add(btnPane,BorderLayout.SOUTH);
        add(parentPane);
        addMouseListener(this);
        
    }//end constructor()
    
    //need to make this default access to allow class ClassFileDialog to access it.
    void openFile() {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //Suggest the Filename. (from Bruno Conde, StackOverflow.com)
        fileChooser.setSelectedFile(new File(recordPane.getFileName()));
        int result = fileChooser.showSaveDialog(this);
        
        //user clicked Cancel button ondialog
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        
        File fileName1 = fileChooser.getSelectedFile();

        if (fileName1 == null || fileName1.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                output = new ObjectOutputStream(
                        new FileOutputStream(fileName1));
                createBtn.setEnabled(false);
                saveRecordBtn.setEnabled(true);

            }//end try
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, "error Opening File", "Error", JOptionPane.ERROR_MESSAGE);

            }//end catch
        }//end else
    }//end createBtnFile()

    private void closeFile() {
        try {
            
            if(output!=null){
                
            StudentRecord finalRecord=new StudentRecord("finalRecord");
            output.writeObject(finalRecord);
            output.close();
            
            } //if
            dispose();
        }//end try
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error closing file", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }//end catch
    }//end closeFile()

    
    
   
    public void saveRecord() {
        
        StudentRecord studentRec;
        String fieldValues[]=new String[14];
        fieldValues=recordPane.getFieldValues();
        
            try{
                    studentRec= new StudentRecord (fieldValues[0], fieldValues[1], 
                            fieldValues[2], fieldValues[3], fieldValues[4], fieldValues[5],
                            fieldValues[6], fieldValues[7], fieldValues[8],
                            fieldValues[9], fieldValues[10], fieldValues[11], fieldValues[12],
                            fieldValues[13]
                    
                    );
                    output.writeObject(studentRec);
                    recordPane.clearFields();
                    recordPane.jTextField1.requestFocusInWindow();
            }//end try
            
            
            catch (IOException ioe){
                closeFile();
            }//end IOException catch
          
    }//end saveRecord()

     //when setMaximum is called, the form will fit into the JDesktopPane,
    //it cannot then be moved outside of the JDesktopPane area.
    //the JTextField gets the focus, the blinking cursor.
void checkMaximized(){
    if(!isMaximum())
        try {
            setMaximum(true);
            recordPane.jTextField1.requestFocusInWindow();
        } 
        catch (PropertyVetoException ex) {
            Logger.getLogger(CreateNewClassFile.class.getName()).log(Level.SEVERE, null, ex);
        }
}//checkMaximied()
  
@Override
    public void mouseClicked(MouseEvent e) {
            checkMaximized();
        }

    @Override
    public void mousePressed(MouseEvent e) {   }

    @Override
    public void mouseReleased(MouseEvent e) {   }

    @Override
    public void mouseEntered(MouseEvent e) {   }

    @Override
    public void mouseExited(MouseEvent e) { }
}//Class CreateNewClassFile
