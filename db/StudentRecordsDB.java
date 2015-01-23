/*
 * class StudentRecordsDB creates the JFrame which contains the JDesktopPane.
 * The opening form 'MainMenu' is created here and displayed.
 */
package student.records.db;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

/**
 *
 * @author mairead
 */
public class StudentRecordsDB extends JFrame{
    public static JDesktopPane theDesktop;
    public static MainMenu mainMenu;
    public static OpeningInfo openingInfo;
    public static StudentRecordsDB parentFrame;
    public static SelectClassTeacher selectForm;
    public static CreateNewClassFile createClassFile;
    public static final Dimension frameDim=new Dimension(780, 530);
    
    StudentRecordsDB(){
    
    theDesktop=new JDesktopPane();
    add(theDesktop);
    
    //create WelcomeJIntFrame and add.
     mainMenu=new MainMenu();
     mainMenu.setSize(frameDim);
     mainMenu.setVisible(true);
     theDesktop.add(mainMenu); 
      
        setUndecorated(true);   //no title bar showing.
        //I would set the back pane as transparent if I could.(setOpaque() doesn't work).
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameDim);
        int[] coordinates=place();
        int x=coordinates[0];
        int y=coordinates[1];
        setLocation(x,y);
        setVisible(true);
     
     
    }//constructor()
    
    //place() places the JFrame (application) centrally on screen.
    int[] place(){
        Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
        int w=(int)dim.getSize().width;
        int h=(int)dim.getSize().height;
        int x=(w-(int)getSize().width)/2; //(getContentPane().getSize().width);
        int y=(h-(int)getSize().height)/2; //(getContentPane().getSize().height);
        
        int [] coordinates=new int[2];
        coordinates[0]=x;
        coordinates[1]=y;
        return(coordinates);
    }  //place()  
  
    
    public static void main(String[]args){
        parentFrame=new StudentRecordsDB();
        
    }
}

