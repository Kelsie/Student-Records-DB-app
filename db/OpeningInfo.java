/*
 * class OpeningInfo makes visible the form 'OpeningInfo' which 
 * displays an information message to user.  This screen is accessed by
 * a button on form 'MainMenu' and returns to form 'MainMenu'.
 */

package student.records.db;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import static student.records.db.StudentRecordsDB.*;

/**
 *
 * @author mairead
 */
class OpeningInfo extends JInternalFrame implements MouseListener{
    
    JTextArea mainBodyArea;
    JPanel textAreaPane;
    JPanel buttonPane;
    FormPanel formPane;
    JCheckBox messageCheck;
    JButton contBtn;
    JLabel headerLbl=new JLabel();
    
    String headerStr;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    
    
   
    public OpeningInfo(){
        super("Information", false, false, false, false);
        
        //haaderLabel displays the header of the information, using
        //the correct School Name entered in the previous screen.
        //headerLbl=new JLabel();
        headerStr="Student Records Application";
        headerLbl.setText(headerStr);
        headerLbl.setHorizontalAlignment(JLabel.CENTER);
        headerLbl.setHorizontalTextPosition(JLabel.CENTER);
        headerLbl.setFont(new Font("Verdana", Font.BOLD, 16));
        
        //create a TextArea to display the information and add the checkbox to
        //this Panel also.
        mainBodyArea=new JTextArea();
        //Dimension dimBA=new Dimension(410, 410);
        //mainBodyArea.setPreferredSize(dimBA);
        mainBodyArea.setEditable(false);
        mainBodyArea.setOpaque(false);
        mainBodyArea.setFont(new Font("Verdana", Font.BOLD, 14));
        mainBodyArea.setLineWrap(true);
        mainBodyArea.setWrapStyleWord(true);
        mainBodyArea.setText(
                "All students are grouped according to their class.  To save a "+
                "student record it is necessary to first select the class and name "+
                "the teacher for this class.  To save the class file, navigate "+
                "through the file system and create a folder in which to save all "+
                "student records files.  The filename is automatically inserted.  "+
                "One or more student records can be entered, and other student "+
                "records can be entered at a later time. A record can be deleted "+
                "from a class file.  "+
                "\n\nTo start entering Student records choose 'Create New Class File'\n\n"+
                "To read ,edit and delete existing records and to add new records, "+
                "choose 'Read, Edit, Add Records.\n\nAt the end of the school year, when "+
                "students progress to the next class year, make changes to the filename "+
                "by renaming with the new class name and teacher name in the file system "+
                "explorer window (eg. Windows Explorer)."
        );
        
        textAreaPane=new JPanel();
        Dimension areaDim=new Dimension(460,360);
        textAreaPane.setPreferredSize(areaDim);
        textAreaPane.setLayout(new BorderLayout());
        textAreaPane.add(mainBodyArea,BorderLayout.CENTER);
        
        
        contBtn=new JButton("continue");
        contBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // OpeningInfo form close. 
                openingInfo.dispose();
            }
        });//addActionListener()
        
        
        buttonPane=new JPanel(new FlowLayout());
        buttonPane.add(contBtn);
        
       
        //put the 3 panes into a single Panel, which will be the central panel
        // in the JInternalFrame. Layout with GridBagLayout.
        formPane=new FormPanel();
        formPane.layoutCentrePane();
        
        //layout the JInternalFrame with GridBagLayout.
        layoutForm();
        
        
        addMouseListener(this);
        
    }//constructor(String)
    
    
    
    
    
    //layoutForm(): layout the JInternalFrame with GridBagLayout
        //from Deitel&Deitel, Java SE8, Page 646
    private void layoutForm() {
        
        layout=new GridBagLayout();
        setLayout(layout);
        constraints=new GridBagConstraints();
        
        //create 8 labels to fill the grid
        JLabel label1=new JLabel();
        //label1.setSize(150,25);
        JLabel label2=new JLabel();
        //label2.setSize(440,25);
        JLabel label3=new JLabel();
        //label3.setSize(150,25);
        JLabel label4=new JLabel();
        label4.setSize(150,395);
        JLabel label6=new JLabel();
        label6.setSize(150,395);
        JLabel label7= new JLabel();
        //label7.setSize(150,65);
        JLabel label8=new JLabel();
        //label8.setSize(440,65);
        JLabel label9=new JLabel();
        //label9.setSize(150,335);
        
        //add the labels and the formPane
        //constraints.fill=GridBagConstraints.NONE;
        constraints.weightx=1;
        constraints.weighty=1;
        constraints.fill=GridBagConstraints.BOTH;
        constraints.gridwidth=1;
        addComponent(label1);
        constraints.gridwidth=GridBagConstraints.RELATIVE;
        addComponent(label2);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        addComponent(label3);
        //row 2
        constraints.gridwidth=1;
        addComponent(label4);
        constraints.gridwidth=GridBagConstraints.RELATIVE;
        addComponent(formPane);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        addComponent(label6);
        //row 3
        constraints.gridwidth=1;
        addComponent(label7);
        constraints.gridwidth=GridBagConstraints.RELATIVE;
        addComponent(label8);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        addComponent(label9);
    }

    private void addComponent(Component component) {
        layout.setConstraints(component, constraints);
        add(component);
    }//addComponent
    
    @Override
    public void mouseClicked(MouseEvent e) {
            try {
                setMaximum(true);
            } 
            catch (PropertyVetoException ex) {
                Logger.getLogger(OpeningInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    @Override
    public void mousePressed(MouseEvent e) {   }

    @Override
    public void mouseReleased(MouseEvent e) {   }

    @Override
    public void mouseEntered(MouseEvent e) {   }

    @Override
    public void mouseExited(MouseEvent e) { }
    
    //extend JPanel to create a method which lays out the formFane in GridBagLayout.
    //create inner class FormPanel.
private class FormPanel extends JPanel{
        
        private GridBagLayout layout1;
        private GridBagConstraints constraints1;

        //layoutCentrePane() lays out the central pane of the JInternalFrame, formPane.
        void layoutCentrePane() {

            layout1=new GridBagLayout();
            setLayout(layout1);
            constraints1=new GridBagConstraints();

            //create 4 labels to fill the grid
            JLabel label4=new JLabel();
            JLabel label6=new JLabel();
            label6.setSize(150,335);
            JLabel label7= new JLabel();
            label7.setSize(150,65);
            JLabel label9=new JLabel();
            
            //add the labels and the formPane
            //constraints.fill=GridBagConstraints.NONE;
            constraints1.weightx=1;
            constraints1.weighty=1;
            constraints1.fill=GridBagConstraints.BOTH;
            constraints1.gridwidth=GridBagConstraints.REMAINDER;
            addComponent(headerLbl);
            
            //row 2
            constraints1.gridwidth=1;
            addComponent(label4);
            constraints1.gridwidth=GridBagConstraints.RELATIVE;
            addComponent(textAreaPane);
            constraints1.gridwidth=GridBagConstraints.REMAINDER;
            addComponent(label6);
            //row 3
            constraints1.gridwidth=1;
            addComponent(label7);
            constraints1.gridwidth=GridBagConstraints.RELATIVE;
            addComponent(buttonPane);
            constraints1.gridwidth=GridBagConstraints.REMAINDER;
            addComponent(label9);
        }
        private void addComponent(Component component) {
            layout1.setConstraints(component, constraints1);
            add(component);
        }//addComponent
    }//class FormPanel
}//class OpeningInfo