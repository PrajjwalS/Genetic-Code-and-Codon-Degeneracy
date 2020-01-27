package com.company;
import org.sqlite.SQLiteException;

import java.io.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {
//  U=0; C=1; A=2; G=3;
    char[][][] mapping = new char[4][4][4];
        mapping[0][0][0]='F';
        mapping[0][0][1]='F';
        mapping[0][0][2]='L';
        mapping[0][0][3]='L';
        mapping[0][1][0]='S';
        mapping[0][1][1]='S';
        mapping[0][1][2]='S';
        mapping[0][1][3]='S';
        mapping[0][2][0]='Y';
        mapping[0][2][1]='Y';
        mapping[0][2][2]='*';
        mapping[0][2][3]='*';
        mapping[0][3][0]='C';
        mapping[0][3][1]='C';
        mapping[0][3][2]='*';
        mapping[0][3][3]='W';
        mapping[1][0][0]='L';
        mapping[1][0][1]='L';
        mapping[1][0][2]='L';
        mapping[1][0][3]='L';
        mapping[1][1][0]='P';
        mapping[1][1][1]='P';
        mapping[1][1][2]='P';
        mapping[1][1][3]='P';
        mapping[1][2][0]='H';
        mapping[1][2][1]='H';
        mapping[1][2][2]='G';
        mapping[1][2][3]='G';
        mapping[1][3][0]='R';
        mapping[1][3][1]='R';
        mapping[1][3][2]='R';
        mapping[1][3][3]='R';
        mapping[2][0][0]='I';
        mapping[2][0][1]='I';
        mapping[2][0][2]='I';
        mapping[2][0][3]='M';
        mapping[2][1][0]='T';
        mapping[2][1][1]='T';
        mapping[2][1][2]='T';
        mapping[2][1][3]='T';
        mapping[2][2][0]='N';
        mapping[2][2][1]='N';
        mapping[2][2][2]='K';
        mapping[2][2][3]='K';
        mapping[2][3][0]='S';
        mapping[2][3][1]='S';
        mapping[2][3][2]='R';
        mapping[2][3][3]='R';
        mapping[3][0][0]='V';
        mapping[3][0][1]='V';
        mapping[3][0][2]='V';
        mapping[3][0][3]='V';
        mapping[3][1][0]='A';
        mapping[3][1][1]='A';
        mapping[3][1][2]='A';
        mapping[3][1][3]='A';
        mapping[3][2][0]='B';
        mapping[3][2][1]='B';
        mapping[3][2][2]='Q';
        mapping[3][2][3]='Q';
        mapping[3][3][0]='G';
        mapping[3][3][1]='G';
        mapping[3][3][2]='G';
        mapping[3][3][3]='G';

        File file = new File("/home/saurabh/Documents/6th sem/SE/Genetic-Code-and-Codon-Degeneracy/Ecol_K12_MG1655_.ena");
        BufferedReader br = new BufferedReader(new FileReader(file));

  long geneLen=0;
  String geneName="";
  String AAseq="";
  String readLine="";

  while((readLine=br.readLine())!=null){
      if(readLine.charAt(0)=='>'){
          if(!geneName.equals(""))
          {
              //TODO store all this in DB
              Connection connection = null;
              try{
                  connection = DriverManager.getConnection("jdbc:sqlite:/home/saurabh/Documents/6th sem/SE/Genetic-Code-and-Codon-Degeneracy/geneInfo.db");
                  Statement statement = connection.createStatement();
                  statement.execute("CREATE TABLE IF NOT EXISTS gene"+
                                        "(Gene_name VARCHAR, Gene_seq VARCHAR,Length INTEGER(6),Remarks VARCHAR )");
                  // now adding data to the database
                  String sql = "INSERT INTO gene(Gene_name,Gene_seq,Length,Remarks)"+
                          "VALUES(?,?,?,'Valid')";
                  // currently setting by default Valid for Remarks!
                  PreparedStatement pstatement = connection.prepareStatement(sql);
                  pstatement.setString(1,geneName);
                  pstatement.setString(2,AAseq);
                  pstatement.setLong(3,geneLen);
                  pstatement.executeUpdate();

                  System.out.println("It worked!");
              }catch (SQLException e){
                  System.out.println("Something went wrong!");
                  e.printStackTrace();
              }finally {
                  try {
                      connection.close();
                  }catch (SQLException e){
                      e.printStackTrace();
                  }
              }

              System.out.println("Gene Name:"+geneName+"\ngene Len:"+geneLen+"\nAAseq Generated:"+AAseq+"\n\n");
              //restoring the defaults
              geneLen=0;
              AAseq="";
          }
          geneName=readLine;
      }
      else{
          for(int i=0;i<readLine.length();i+=3){
              //TODO insert error check

              AAseq+=mapping[binding(readLine.charAt(i))][binding(readLine.charAt(i+1))][binding(readLine.charAt(i+2))];
              geneLen+=3;
          } 
      }
  }
        System.out.println("Gene Name:"+geneName+"\ngene Len:"+geneLen+"\nAAseq Generated:"+AAseq+"\n\n");


    }
   public static int binding(char c){
        if(c=='a'||c=='A')
            return 2;
        else if(c=='u'||c=='U' || c=='t' || c=='T')
            return 0;
        else if(c=='c'||c=='C')
            return 1;
        else if(c=='g'||c=='G')
            return 3;
        else
            return -999;
    }
}


