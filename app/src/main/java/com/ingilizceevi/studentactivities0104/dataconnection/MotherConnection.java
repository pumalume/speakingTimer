package com.ingilizceevi.studentactivities0104.dataconnection;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import com.ingilizceevi.studentactivities0104.gameModel;
import com.ingilizceevi.studentactivities0104.StudentInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MotherConnection extends AsyncTask<String, String, List<StudentInfo>> {
    Boolean myFlag = true;
    gameModel gameBrain;
    Connection connection = null;
    String ConnectURL = null;
    List<StudentInfo> studentList = new ArrayList<>();
    String user = "pumal";
    String password = "Maylee08";
    String database = "EnglishHouse";
    String ipaddress = "192.168.1.4";
    String selectNameQuery =
            "SELECT StudentID,FirstName,LastName FROM dbo.Students ORDER BY FirstName;";

    public MotherConnection(gameModel gb) {
        this.gameBrain = gb;
    }


    @Override
    protected List<StudentInfo> doInBackground(String... strings) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        List<StudentInfo> listOfStudents = new ArrayList<>();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectURL = "jdbc:jtds:sqlserver://192.168.1.67:1434;instanceName=SQL02;databaseName=EnglishHouse;user=pumal;password=Maylee08;";
            //ConnectURL = "jdbc:jtds:sqlserver://" + ipaddress + "/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectURL);
            studentList=makeSQLQuery();
            connection.close();
        } catch (Exception e) {
            Log.e("connection error:", e.getMessage());
            myFlag = false;
            return null;
        }
        return studentList;
    }
    @Override
    protected void onPostExecute(List<StudentInfo> listOfStudents) {
        Boolean studentListIsFull = (listOfStudents!=null);
        if(myFlag)gameBrain.loadMyStudentList(listOfStudents);
        gameBrain.flagIsRaisedForLoadedStudentList(myFlag);
    }

    private  List<StudentInfo> makeSQLQuery() {
        List<StudentInfo> studentList;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(selectNameQuery);
            studentList =getNamesFromResultSet(rs);
            statement.close();
        } catch (Exception e) {
            Log.e("Query failed: ", e.getMessage());
            studentList = null;
            myFlag = false;
            return null;
        }
        return studentList;
    }
    private  List<StudentInfo> getNamesFromResultSet(ResultSet rs) throws SQLException {
        List<StudentInfo> studentList = new ArrayList<>(0);
        while (rs.next()) {
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            int studentID = rs.getInt("StudentID");
            String s = String.valueOf(studentID);
            StudentInfo student = new StudentInfo(s);
            student.setStudentFirstName(firstName);
            student.setStudentLastName(lastName);
            studentList.add(student);
        }
        return studentList;
    }
}
