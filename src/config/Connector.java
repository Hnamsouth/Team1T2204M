package config;

import java.sql.*;
import java.util.ArrayList;

public class Connector {
//    set private để ko thể
    private Connection cnn;
    private static Connector instance;
    String strmysql="jdbc:mysql://localhost:3306/javaproject",user="root",pw="";
//    private String strmysql="jdbc:mysql://pokapi.tech:3306/javaproject",user="south",pw="AFMhn17397";
    public Connector(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.cnn = DriverManager.getConnection(strmysql,user,pw);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connector getInstance(){
         return instance==null?new Connector():instance;
    }
    public Statement createSTM() throws SQLException {
        return cnn.createStatement();
    }

    public ResultSet queryRS(String sql){
        try {
            return createSTM().executeQuery(sql);
        }catch (Exception e) {
            return null;
        }
    }
    public boolean queryBL(String sql) throws SQLException {
            return createSTM().execute(sql);
    }

    public  PreparedStatement CreatePPSTM(String sql) throws SQLException {
        return cnn.prepareStatement(sql);
    }
    public ResultSet queryRS(String sql, ArrayList arr) throws SQLException {
        try {
            //        parameterIndex tinh tu 1
            PreparedStatement stm=CreatePPSTM(sql);
            for(int i=0;i<arr.size();i++){
                if(arr.get(i) instanceof Integer){
                    stm.setInt( i+1 , (Integer) arr.get(i));
                }else if(arr.get(i) instanceof String){
                    stm.setString( i+1 , (String) arr.get(i));
                }else if(arr.get(i) instanceof Double){
                    stm.setDouble( i+1 , (Double) arr.get(i));
                }else if(arr.get(i) instanceof Date){
                    stm.setDate( i+1 , (Date) arr.get(i));
                }
            }
            return stm.executeQuery();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean queryBL(String sql, ArrayList arr) throws SQLException {
        try {
            //        parameterIndex tinh tu 1
            PreparedStatement stm=CreatePPSTM(sql);
            for(int i=0;i<arr.size();i++){
                if(arr.get(i) instanceof Integer){
                    stm.setInt( i+1 , (Integer) arr.get(i));
                }else if(arr.get(i) instanceof String){
                    stm.setString( i+1 , (String) arr.get(i));
                }else if(arr.get(i) instanceof Double){
                    stm.setDouble( i+1 , (Double) arr.get(i));
                }else if(arr.get(i) instanceof Date){
                    stm.setDate( i+1 , (Date) arr.get(i));
                }
            }
            stm.execute();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }







}
