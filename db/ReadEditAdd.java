/*
 * class ReadEditAdd creates the ReadEdit form, which is the second of the two
 * main forms of this application.  With 'nextRecordButton' user can iterate 
 * through the student records. 'UpdateButton' allows the record to be updated.
 * 'deleteButton' allows the record to be deleted. 'addNewButton' allows for a
 * new record to be added to the class file. 'closeButton' brings user back to
 * main menu form.
 */
package student.records.db;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static student.records.db.StudentRecordsDB.parentFrame;

/**
 *
 * @author mairead
 */
public class ReadEditAdd extends JInternalFrame implements MouseListener{
    
    private ObjectInputStream input;
    private final JPanel parentCenteringPane;
    private final JPanel btnPane;
    private final JPanel parentPane;
    private final RecordUI recordPane;
    private JButton nextRecordBtn, openBtn, updateBtn, closeBtn, addNewBtn, deleteBtn;
    private final ArrayList<StudentRecord>recordsArr=new ArrayList<>();
    private boolean saveChangesBln;
    private boolean addNewBln;
    private ObjectOutputStream output;
    private File fileName;
    private String setBtnName;
    private StudentRecord studentRec;
    private final StudentRecord endRecord=new StudentRecord("finalRecord");  //EOF marker
   
     //public RecordUICreate() {}
    public ReadEditAdd() {
        
        super("Read, Edit, Add, Delete Records");    //change this to 'Read Records'.
        getContentPane().setLayout(new BorderLayout());
        parentPane=new JPanel(new BorderLayout());
        recordPane=new RecordUI();
         recordPane.setFieldsUneditable();
        
        //create the input ButtonPanel1, ReadUpdateDelete.
        btnPane=new JPanel();
        
        btnPane.setLayout(new FlowLayout());
        setBtnPane();
        
       //From class StudentRecordsDB, Static member,'parentFrame', is an
        //object of StudentRecordsDB (the only JFrame). Initialized in main().
        parentFrame.addWindowListener(
                        new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (input != null) {
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
        
        //put parentPane into a larger JPanel to centre the central form and buttons.
        parentCenteringPane=new JPanel();
        parentCenteringPane.setLayout(new BorderLayout());
        JPanel northPanel=new JPanel();
        northPanel.add(new JLabel(" "));
        JPanel southPanel=new JPanel();
        southPanel.add(new JLabel(" "));
        parentCenteringPane.add(northPanel, BorderLayout.NORTH);
        parentCenteringPane.add(parentPane, BorderLayout.CENTER);
        parentCenteringPane.add(southPanel, BorderLayout.SOUTH);
        add(parentCenteringPane);
        

        //open the JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        //user clicked Cancel button ondialog
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        fileName = fileChooser.getSelectedFile();

        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                input = new ObjectInputStream(
                        new FileInputStream(fileName));
                
                nextRecordBtn.setEnabled(true);
                nextRecordBtn.setText("next record");
                recordPane.setFieldsEditable();
                
                nextRecord();
                
            }//end try
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, "error Opening File", "Error", JOptionPane.ERROR_MESSAGE);
            }//end catch
        }//end else
    }//end constructor()
    
    //initialize and add 5 buttons to the ButtonPane.
    private void setBtnPane(){
       
        //add 'update' button
        updateBtn = new JButton("update");
        updateBtn.setEnabled(false);
        updateBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        update();
                    }//end actionPerformed()
                }//end anonymous inner class
        ); //end addActionListener()
        btnPane.add(updateBtn);
        
        //show nextRecord button
        nextRecordBtn = new JButton("next record");
        nextRecordBtn.setEnabled(false);
        nextRecordBtn.addActionListener((ActionEvent e) -> {
            nextRecord();
        }); //end addActionListener()
        btnPane.add(nextRecordBtn);
        
        deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        deleteRecord();
                    }//end actionPerformed()
                }//end anonymouse Inner class
        ); //end addActionListener()
        btnPane.add(deleteBtn);
        
        addNewBtn = new JButton("new record");
        addNewBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(!addNewBln) addNew();
                        else if(recordPane.checkInputFields())
                            saveRecord();
                            else JOptionPane.showMessageDialog(null,"Enter Student 'first name' and "
                                + " 'last name' fields\n before Save Record.");
                        
                    }//end actionPerformed()
                }//end anonymouse Inner class
        ); //end addActionListener()
        btnPane.add(addNewBtn);
        
        closeBtn = new JButton("close");
        closeBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(saveChangesBln) writeRecords();
                        else closeFile();
                    }//end actionPerformed()
                }//end anonymous inner class
        ); //end addActionListener()
        btnPane.add(closeBtn); 
    }//setBtnPane()
    
   
    
    private void openFile() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        //user clicked Cancel button ondialog
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        fileName = fileChooser.getSelectedFile();

        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                input = new ObjectInputStream(
                        new FileInputStream(fileName));
                nextRecordBtn.setEnabled(true);
                recordPane.setFieldsEditable();
                
                nextRecord();
                
            }//end try
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, "error Opening File", "Error", JOptionPane.ERROR_MESSAGE);

            }//end catch
        }//end else
    }//end openBtnFile()

    
private void closeFile() {
        try {
                if(input!=null){
                    input.close();  
                }//if
                
                if(output!=null){
                    output.close();
                } //if
                dispose();
        }//end try
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "RecordUIRead,closeFile(), error closing Input \nor error closing output.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }//end catch
    }//end closeFile()
    
    
   
    public void nextRecord() {
        
        try {
                studentRec=(StudentRecord)input.readObject();
                
                if(!(studentRec.equals(endRecord)))
                {
                    recordsArr.add(studentRec);
                    String values[]={
                        studentRec.getStClass(), 
                        studentRec.getTeacher(),
                        studentRec.getFirstName(), 
                        studentRec.getLastName(),
                        studentRec.getMother(),
                        studentRec.getMotherMob(),
                        studentRec.getFather(),
                        studentRec.getFatherMob(),
                        studentRec.getLandline(),
                        studentRec.getEmergCnt1(),
                        studentRec.getEmergCnt1Ph(),
                        studentRec.getEmergCnt2(),
                        studentRec.getEmergCnt2Ph(),
                        studentRec.getInfo()
                                
                        }; //end String values[]

                    //use student name on the SaveChangesBtn
                    if(values[2]!=null &&values[3]!=null){
                        char [] charArr=values[2].toCharArray();
                        setBtnName=charArr[0]+"\u005F"+values[3];
                        updateBtn.setText(setBtnName+": Update");
                    }//if
                    else updateBtn.setText("update");
                    
                    updateBtn.setEnabled(true);   
                    recordPane.setFieldValues(values);
                    addNewBln=false;
                    
                }//if
                else {
                    recordPane.clearFields();
                    nextRecordBtn.setEnabled(false);
                    updateBtn.setText("update");
                    updateBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                    recordPane.setFieldsUneditable();
                }//else
            }//end try
            catch (EOFException eofex) {
                JOptionPane.showMessageDialog(this, "No more records", "End of File", JOptionPane.ERROR_MESSAGE
                );
            }//end EndOfFileException
            catch (ClassNotFoundException nfe) {
                JOptionPane.showMessageDialog(this, "Unable to create object", "Class Not Found", JOptionPane.ERROR_MESSAGE
                );
            }//end NumberFormatException
            catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error during read from file", "Read Error", JOptionPane.ERROR_MESSAGE);
                openBtn.setEnabled(true);
            }//end IOException catch
       
            
    }//end nextRecord()
    
    //complete reading from the inputStream before writeRecords().
    private void completeInputStream(){
        try{
            while(!(studentRec.equals(endRecord)))
                {
                    studentRec=(StudentRecord)input.readObject();
                    recordsArr.add(studentRec);
                }//while
                
            }//end try
            catch (EOFException eofex) {
                JOptionPane.showMessageDialog(this, "No more records", "End of File", JOptionPane.ERROR_MESSAGE
                );
            }//end EndOfFileException
            catch (ClassNotFoundException nfe) {
                JOptionPane.showMessageDialog(this, "Unable to create object", "Class Not Found", JOptionPane.ERROR_MESSAGE
                );
            }//end NumberFormatException
            catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error during read from file", "Read Error", JOptionPane.ERROR_MESSAGE);
                
            }//end IOException catch
    }//completeInputStream
    
    
    //createStudentRec() takes the values from the recordPane and uses 
    //them to update the current record, or delete the current record, 
    //or in adding a new record (to recordsArr).//
    private void createStudentRec(){
        //set flag saveChangesBln to true, which is checked by  closeBtn.
        saveChangesBln=true;
       String fieldValues[]=recordPane.getFieldValues();
        
            studentRec= new StudentRecord (fieldValues[0], fieldValues[1], 
                    fieldValues[2], fieldValues[3], fieldValues[4], fieldValues[5],
                    fieldValues[6], fieldValues[7], fieldValues[8],
                    fieldValues[9], fieldValues[10], fieldValues[11], fieldValues[12],
                    fieldValues[13]
                    );
            
    }//createStudentRec()
    
    //update() writes the edited records into ArrayList studentsArr
    public void update(){
            createStudentRec();
            
            //replace the last record added to the ArrayList with this modified record.
            int s=recordsArr.size();
            s=s-1;
            recordsArr.remove(s);
            recordsArr.add(studentRec);
            saveChangesBln=true;  //so that the file is freshly written.
            updateBtn.setText(setBtnName+": Updated");
            
    
    } //saveChanges()
    
    //delete the record from the file.
    void deleteRecord(){
        createStudentRec();

        //remove the last record added to the ArrayList.
        int s=recordsArr.size();
        s=s-1;
        recordsArr.remove(s);
        saveChangesBln=true;  //so that the file is freshly written.
        
        //display next record if there is one. If not, clear fields, and 
        //disable nextRecordBtn.
        
        
        try {
                studentRec=(StudentRecord)input.readObject();
                
                if(!(studentRec.equals(endRecord)))
                {
                    recordsArr.add(studentRec);
                    String values[]={
                        studentRec.getStClass(), 
                        studentRec.getTeacher(),
                        studentRec.getFirstName(), 
                        studentRec.getLastName(),
                        studentRec.getMother(),
                        studentRec.getMotherMob(),
                        studentRec.getFather(),
                        studentRec.getFatherMob(),
                        studentRec.getLandline(),
                        studentRec.getEmergCnt1(),
                        studentRec.getEmergCnt1Ph(),
                        studentRec.getEmergCnt2(),
                        studentRec.getEmergCnt2Ph(),
                        studentRec.getInfo()
                                
                        }; //end String values[]

                    //use student name on the SaveChangesBtn
                    char [] charArr=values[2].toCharArray();
                    setBtnName=charArr[0]+"\u005F"+values[3];
                    updateBtn.setText(setBtnName+": Update");
                    updateBtn.setEnabled(true);   
                    recordPane.setFieldValues(values);
                    addNewBln=false;
                    
                    
                }//if
                else {
                    recordPane.clearFields();
                    recordPane.setFieldsUneditable();
                    updateBtn.setText("update");
                    nextRecordBtn.setEnabled(false);
                    updateBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                }//else
            }//end try
            catch (EOFException eofex) {
                JOptionPane.showMessageDialog(this, "No more records", "End of File", JOptionPane.ERROR_MESSAGE
                );
            }//end EndOfFileException
            catch (ClassNotFoundException nfe) {
                JOptionPane.showMessageDialog(this, "Unable to create object", "Class Not Found", JOptionPane.ERROR_MESSAGE
                );
            }//end NumberFormatException
            catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error during read from file", "Read Error", JOptionPane.ERROR_MESSAGE);
                openBtn.setEnabled(true);
            }//end IOException catch
          
   
    }//deleteRecord()
    
  //To save a new record, first clear the fields, Then change the button name.
    void addNew(){
        //if addNewBln is true, then 'addNew' is changed to 'save record'
        //and the 'updateBtn' is disabled.
        addNewBln=true;
        
        updateBtn.setEnabled(false);
        recordPane.clearFields();
        recordPane.setFieldsEditable();
        addNewBtn.setText("save record");
    }//addNew()
    
    //To save a new student record (on ReadEditAdd form).
    void saveRecord(){
        //addNewBln is set to false, so that 'addNew'ActionPerformed calls
        //addNew()
        //the button displays 'new record'.
       addNewBln=false;
       saveChangesBln=true;  //necessary so that the file is new written.
       
        createStudentRec();
        //add this record to the Arraylist.
        recordsArr.add(studentRec); 
        recordPane.clearFields();
        addNewBtn.setText("new record");
    }//saveRecord()
    
//write ArrayList recordsArr to output
    void writeRecords(){
        try {
                output = new ObjectOutputStream(
                        new FileOutputStream(fileName));
                
                //I did not write the following lambda stream. Inserted by NetBeans.mc.`
                try{
                        recordsArr.stream().forEach(new Consumer<StudentRecord>() {

                            @Override
                            public void accept(StudentRecord sr) {
                                try {
                                    output.writeObject(sr);

                                } catch (IOException ex) {
                                    Logger.getLogger(ReadEditAdd.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        output.writeObject(endRecord);
                    }//try
                catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(this,"NumberFormatException thrown, RecordUICreate, l174\n"+
                        "problem writing the record to file");
                 }//end NumberFormatException

            }//end try
             
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, "error Opening File", "Error", JOptionPane.ERROR_MESSAGE);
           
            }//end catch
        closeFile();
    }//writeRecords()
    
    
    //when setMaximum is called, the form will fit into the JDesktopPane,
    //it cannot then be moved outside of the JDesktopPane area.
    void checkMaximized(){
    if(!isMaximum())
        try {
            setMaximum(true);
         } 
        catch (PropertyVetoException ex) {
            Logger.getLogger(ReadEditAdd.class.getName()).log(Level.SEVERE, null, ex);
        }//catch
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

}

