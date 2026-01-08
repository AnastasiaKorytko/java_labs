package org.example;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader("input.txt"));
            String str = br.readLine();
            String sep = br.readLine();
            StringProcess proc = new StringProcess(str, sep);
            String result = proc.process();
            File file = new File("output.txt");
            PrintWriter pw = new PrintWriter(file);
            pw.println(result);
            pw.close();
        } catch (IOException e){
            System.out.println("Error: " + e);
        }
        finally {
            try{
                br.close();
            }
            catch (IOException e){
                System.out.println("Error: " + e);
            }
        }
    }
}