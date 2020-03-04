package com.company;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    private static HashMap<Character, Long> freq = new HashMap<>();

    public static void main(String[] args) throws IOException {
        freq.put('A', 0L);
        freq.put('B', 0L);
        freq.put('R', 0L);
        freq.put('N', 0L);
        freq.put('D', 0L);
        freq.put('C', 0L);
        freq.put('E', 0L);
        freq.put('Q', 0L);
        freq.put('G', 0L);
        freq.put('H', 0L);
        freq.put('I', 0L);
        freq.put('L', 0L);
        freq.put('K', 0L);
        freq.put('M', 0L);
        freq.put('F', 0L);
        freq.put('P', 0L);
        freq.put('S', 0L);
        freq.put('T', 0L);
        freq.put('W', 0L);
        freq.put('Y', 0L);
        freq.put('V', 0L);
        freq.put('*', 0L);


//  U=0; C=1; A=2; G=3;
        char[][][] mapping = new char[4][4][4];
        mapping[0][0][0] = 'F';
        mapping[0][0][1] = 'F';
        mapping[0][0][2] = 'L';
        mapping[0][0][3] = 'L';
        mapping[0][1][0] = 'S';
        mapping[0][1][1] = 'S';
        mapping[0][1][2] = 'S';
        mapping[0][1][3] = 'S';
        mapping[0][2][0] = 'Y';
        mapping[0][2][1] = 'Y';
        mapping[0][2][2] = '*';
        mapping[0][2][3] = '*';
        mapping[0][3][0] = 'C';
        mapping[0][3][1] = 'C';
        mapping[0][3][2] = '*';
        mapping[0][3][3] = 'W';
        mapping[1][0][0] = 'L';
        mapping[1][0][1] = 'L';
        mapping[1][0][2] = 'L';
        mapping[1][0][3] = 'L';
        mapping[1][1][0] = 'P';
        mapping[1][1][1] = 'P';
        mapping[1][1][2] = 'P';
        mapping[1][1][3] = 'P';
        mapping[1][2][0] = 'H';
        mapping[1][2][1] = 'H';
        mapping[1][2][2] = 'G';
        mapping[1][2][3] = 'G';
        mapping[1][3][0] = 'R';
        mapping[1][3][1] = 'R';
        mapping[1][3][2] = 'R';
        mapping[1][3][3] = 'R';
        mapping[2][0][0] = 'I';
        mapping[2][0][1] = 'I';
        mapping[2][0][2] = 'I';
        mapping[2][0][3] = 'M';
        mapping[2][1][0] = 'T';
        mapping[2][1][1] = 'T';
        mapping[2][1][2] = 'T';
        mapping[2][1][3] = 'T';
        mapping[2][2][0] = 'N';
        mapping[2][2][1] = 'N';
        mapping[2][2][2] = 'K';
        mapping[2][2][3] = 'K';
        mapping[2][3][0] = 'S';
        mapping[2][3][1] = 'S';
        mapping[2][3][2] = 'R';
        mapping[2][3][3] = 'R';
        mapping[3][0][0] = 'V';
        mapping[3][0][1] = 'V';
        mapping[3][0][2] = 'V';
        mapping[3][0][3] = 'V';
        mapping[3][1][0] = 'A';
        mapping[3][1][1] = 'A';
        mapping[3][1][2] = 'A';
        mapping[3][1][3] = 'A';
        mapping[3][2][0] = 'B';
        mapping[3][2][1] = 'B';
        mapping[3][2][2] = 'Q';
        mapping[3][2][3] = 'Q';
        mapping[3][3][0] = 'G';
        mapping[3][3][1] = 'G';
        mapping[3][3][2] = 'G';
        mapping[3][3][3] = 'G';

       File file = new File("/home/saurabh/Documents/6th sem/SE/Genetic-Code-and-Codon-Degeneracy/Ecol_K12_MG1655_.ena");
//        File file = new File("/home/saurabh/Documents/6th sem/SE/Genetic-Code-and-Codon-Degeneracy/test.ena");
        BufferedReader br = new BufferedReader(new FileReader(file));

        long geneLen = 0;
        String geneName = "";
        String AAseq = "";
        String readLine = "";
        String remark = "ALL GOOD";
        boolean valid = true;

        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '>') {
                if (!geneName.equals("")) {
                    //TODO store all this in DB
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection("jdbc:sqlite:/home/saurabh/Documents/6th sem/SE/Genetic-Code-and-Codon-Degeneracy/geneInfo.db");
                        Statement statement = connection.createStatement();
                        statement.execute("CREATE TABLE IF NOT EXISTS gene" +
                                "(Gene_name VARCHAR, Gene_seq VARCHAR,Length INTEGER(6),Remarks VARCHAR )");
                        // now adding data to the database
                        String sql = "INSERT INTO gene(Gene_name,Gene_seq,Length,Remarks)" +
                                "VALUES(?,?,?,'Valid')";
                        // currently setting by default Valid for Remarks!
                        PreparedStatement pstatement = connection.prepareStatement(sql);
                        pstatement.setString(1, geneName);
                        pstatement.setString(2, AAseq);
                        pstatement.setLong(3, geneLen);
                        pstatement.executeUpdate();

                    } catch (SQLException e) {
                        System.out.println("Something went wrong!");
                        e.printStackTrace();
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Gene Name:" + geneName + "\ngene Len:" + geneLen + "\nAAseq Generated:" + AAseq + "\n\n");
                    // analyzing the amino acid seq:
                  //  analyse(AAseq);
                    //restoring the defaults
                    geneLen = 0;
                    AAseq = "";
                    valid = true;
                    remark = "ALL GOOD";
                }
                geneName = readLine;
            }
//      else if(readLine.charAt(0)!='>'){
//          remark = "Erroroneous sequence";
//          System.out.println(remark);
//      }
            else {
                geneLen += readLine.length();
                if (valid) {

                    for (int i = 0; i < readLine.length(); i += 3) {

                        int x, y, z;
                        try {
                            x = binding(readLine.charAt(i));
                            y = binding(readLine.charAt(i + 1));
                            z = binding(readLine.charAt(i + 2));
                        } catch (IndexOutOfBoundsException e) {
                            x = -999;
                            y = -111;
                            z = -999;
                        }
                        //debug:: System.out.println("seq:"+readLine.charAt(i)+readLine.charAt(i+1)+readLine.charAt(i+2)+":");
                        if (x == -999 || y == -999 || z == -999) {

                            AAseq = "INVALID";
                            if (y != -111)
                                remark = "INVALID GENE CODE near index:" + i + "-" + (i + 2);
                            else
                                remark = "INVALID LENGTH";
                            valid = false;
                            break;
                        } else {
                            AAseq += mapping[x][y][z];
                            Long newval=0L;

                            // Calculating amino acids for stats
                            try {
                                newval = freq.get(mapping[x][y][z])+1;

                            }catch (Exception e){
                                System.out.println("Hash Map Freq missed:"+mapping[x][y][z]);
                            }

                            freq.replace(mapping[x][y][z],newval);
                        }
                    }
                }
            }
        }
        System.out.println("Gene Name:" + geneName + "\ngene Len:" + geneLen + "\nAAseq Generated:" + AAseq + "\n\n");

        // handling stats
        System.out.println(freq.toString());


    }

    private static int binding(char c) {
        if (c == 'a' || c == 'A')
            return 2;
        else if (c == 'u' || c == 'U' || c == 't' || c == 'T')
            return 0;
        else if (c == 'c' || c == 'C')
            return 1;
        else if (c == 'g' || c == 'G')
            return 3;
        else
            return -999;
    }

    private static void analyse(String amino) {
        for (int i = 0; i < amino.length(); i++) {
            char current = amino.charAt(i);
            if (freq.containsKey(current)) {
                long temp = freq.get(current);   // get the old value
                freq.replace(current, temp, temp + 1);   // replace it with new value
            }
        }

    }


}


