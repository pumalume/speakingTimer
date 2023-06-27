package com.ingilizceevi.pickthepicture;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EntryControl extends AsyncTask<String, String, List<StudentInfo>> {

    Activity activity;
    StudentInfo studentInfo;


    List<StudentInfo> studentList = new ArrayList<>();
    String user ="pumal";
    String password = "Maylee08";
    String database = "EnglishHouse";
    String ipaddress = "192.168.1.83";
    Boolean putString = false;
    EntryControl(Activity activity){
        this.activity = activity;
        this.putString = false;
    }
    EntryControl(Activity activity, StudentInfo studentInfo){
        this.activity = activity;
        this.studentInfo = studentInfo;
        this.putString=true;
    }
    Connection connection = null;
    String ConnectURL = null;

    @Override
    protected List<StudentInfo> doInBackground(String... strings) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String selectNameQuery = "SELECT StudentID,FirstName,LastName FROM dbo.Students ORDER BY FirstName;";
        //String query = String.format("UPDATE dbo.Students SET Points = 5 WHERE StudentID = '%s'", id);
        String myIP = strings[0];


            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                //ConnectURL = "jdbc:jtds:sqlserver://" + ipaddress + "/" + database + ";user=" + user + ";password=" + password + ";";
                ConnectURL = "jdbc:jtds:sqlserver://192.168.1.77:1434;instanceName=SQL02;databaseName=EnglishHouse;user=pumal;password=Maylee08;";

                connection = DriverManager.getConnection(ConnectURL);
                String s = "";
                try {
                    if (putString) {
                        String query = String.format(Locale.getDefault(),
                                "INSERT INTO Chapters (StudentID, ChapterNum, ChapterTime, ChapterClicks) VALUES ('%s', '%d', '%d', '%d');",
                                studentInfo.studentID,
                                studentInfo.studentChapter,
                                studentInfo.studentTime,
                                studentInfo.studentClicks
                        );
                        Statement stmnt = connection.createStatement();
                        try {
                            stmnt.executeUpdate(query);
                            System.out.println("preparing update");
                            stmnt.close();
                            connection.close();
                        } catch (Exception e) {
                            Log.e("Couldn't make query: ", e.getMessage());
                            System.out.println("didn't work");
                            connection.close();
                        }

                    } else {
                        Statement stmnt = connection.createStatement();
                        ResultSet rs = stmnt.executeQuery(selectNameQuery);
                        while (rs.next()) {
                            String tempFirstName = rs.getString("FirstName");
                            String tempLastName = rs.getString("LastName");
                            Integer studentID = rs.getInt("StudentID");
                            String studentName = tempFirstName + " " + tempLastName;
                            studentList.add(new StudentInfo(studentName, studentID));
                        }
                        stmnt.close();
                        System.out.println("sql closed");
                        connection.close();
                        System.out.println("connection closed");
                    }
                } catch (Exception e) {

                    Log.e("Couldn't make query: ", e.getMessage());
                    return null;
                }
            } catch (Exception e) {
                Log.e("Network error here  :  ", e.getMessage());
                return null;
            }
        return studentList;
    }
    @Override
    protected void onPostExecute(List<StudentInfo> studentMap)
    {
        if(putString)return;
        if(studentMap!=null){
            FillTheView fillView = new FillTheView(activity);
            fillView.addChapterList(studentMap);
            LinearLayout l = activity.findViewById(R.id.myNamesList);
            int count = l.getChildCount();
            for (int i = 0; i < count; i++) {
                TextView t = (TextView) l.getChildAt(i);
                t.setOnClickListener(v -> {
                    TextView text = ((TextView) v);
                    String tempName = (String) text.getText();
                    Intent intent = new Intent(activity, MenuActivity.class);
                    intent.putExtra("studentName", tempName);
                    intent.putExtra("studentID", findID(studentMap, tempName));
                    activity.startActivity(intent);
                });
            }
        }
        else{
            List<StudentInfo> emptyList = new ArrayList<>();
            emptyList.add(new StudentInfo("No connection", 0));
            emptyList.add(new StudentInfo("Press SKIP or try again", 1));
            FillTheView fillView = new FillTheView(activity);
            fillView.addChapterList(emptyList);
        //    Intent intent = new Intent(activity, MenuActivity.class);
        //    intent.putExtra("studentName", "Spencer");
        //    intent.putExtra("studentID", 0);
        //    activity.startActivity(intent);

        }

    }
    public int findID(List<StudentInfo> studentMap, String myString){
        for(int i = 0; i<studentMap.size(); i++){
            if(studentMap.get(i).studentName==myString)return studentMap.get(i).studentID;
        }
        return -1;
    }
}