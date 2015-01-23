/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.records.db;

import java.io.Serializable;

/**
 *
 * @author mairead
 */
public class StudentRecord implements Serializable{
    
    //14 data members
    private String studentClass;
    private String teacher;  
    private String firstName;
    private String lastName;
    private String mother;
    private String father;
    private String emergCnt1;
    private String emergCnt2;
    private String motherMob;
    private String fatherMob;
    private String landline;
    private String emergCnt1Ph;
    private String emergCnt2Ph;
    private String info;
    
    
    //set StudentClass to "finalRecord", for EOF marker
    public StudentRecord(String s){
        this(s,"","","","","","","","","","","","","");
    }//constructor(String s)
    
    public StudentRecord(String stClass, String teacher, String first, String last, String mother, 
                String motherMob,String father, String fatherMob, String  landline,
                String emergCnt1, String emergCnt1Ph, String emergCnt2, 
                String emergCnt2Ph, String info){
        studentClass=stClass;
        this.teacher=teacher;
        firstName=first;
        lastName=last;
        this.mother=mother;
        this.father=father;
        this.emergCnt1=emergCnt1;
        this.emergCnt2=emergCnt2;
        this.motherMob=motherMob;
        this.fatherMob=fatherMob;
        this.emergCnt1Ph=emergCnt1Ph;
        this.emergCnt2Ph=emergCnt2Ph;
        this.landline=landline;
        this.info=info;
        
        
    }//constructor
    
    
    
    public String getStClass()      {return studentClass;}
    public String getTeacher()      {return teacher;}
    public String getFirstName()    {return firstName;}
    public String getLastName()     {return lastName;}
    public String getMother()       {return mother; }
    public String getMotherMob()       {return motherMob; }
    public String getFather()       {return father; }
    public String getFatherMob()       {return fatherMob;}
    public String getLandline()        {return landline; }
    public String getEmergCnt1()    {return emergCnt1; }
    public String getEmergCnt1Ph()     {return emergCnt1Ph; }
    public String getEmergCnt2()    {return emergCnt2; }
    public String getEmergCnt2Ph()     {return emergCnt2Ph; }
    public String getInfo()         {return info; }
    
    @Override
    public boolean equals(Object sr){
        StudentRecord stRec=(StudentRecord)sr;
        
       return (this.studentClass.equals(stRec.studentClass))  ;
        
    }//equals
}