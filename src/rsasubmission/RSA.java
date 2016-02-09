/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rsasubmission;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author j42p557
 */
public class RSA {

    static int p = 0,
               q = 0,
               e = 0,
               m,
               c;
    static String text = "";
    
    public static void main(String[] args) {
        boolean encryption = true;
        try{
            encryption = loadText();
        } catch (FileNotFoundException e){
            System.out.println("No file found");
            System.exit(1);
        }
        //for ascii value to alphanumeric conversion
        text = text.toUpperCase();
        
        //choose which algorithm
        if (encryption)
            encryptMessage();
        else
            decryptMessage();
        
        System.out.println();
    }
    
    private static void encryptMessage(){
        
        int mod = p * q; //mod value
        
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            m = (int) letter - 64; //convert character to alphanumeric value
            
            c = (int) Math.pow(m, e) % mod;
            
            System.out.print(c + " ");
        }
    }
    
    private static void decryptMessage() {
        
        String decryption = "";
        int size;
        int[] mods;
        String[] numbers = text.split(" ");
        for (int i = 0; i < numbers.length; i++) {
            
            int cast = Integer.parseInt(numbers[i]);
            int d = posInverse();
            
            ArrayList<Integer> array = splitExponent(d);
            
            //grab modded power of 2's
            mods = new int[array.get(0) + 1];
            mods[0] = cast % (p*q);
            for (int k = 1; k < mods.length; k++) {
                mods[k] = (int) Math.pow(mods[k-1],2) % (p*q);
            }
            
            int finalModding = 1;
            for (int l = 0; l < array.size(); l++) {
                finalModding *= mods[array.get(l)];
            }
            
            int decrypt = finalModding % (p*q);
            decryption += Character.toString((char)(decrypt + 64));
        }
        System.out.println(decryption);
    }
    
    private static int posInverse() {
        int condition;
        int inverse;
        int check;
        
        int mod2 = (p-1) * (q-1);
        int x = 40;
        
        //changed the algorithm to work faster on computers than
        //what the book asks to do
        int k = 1;
        boolean done = false;
        try {
        while (!done) {
            condition = x * k;
            inverse = condition / e;
            check = e * (inverse + 1) - condition;
            
            if (check == 1) {
                return inverse + 1;
            } else {
                k++;
            }
        }
        } catch (StackOverflowError e){
            System.out.println("Could not find value");
        }
        return 0;
        
    }
    
    public static boolean loadText() throws FileNotFoundException{
   
        boolean ENC;
        
        FileReader file = new FileReader("Input3.txt");
        //Scanner checker = new Scanner(file);
        Scanner inFile = new Scanner(file);
        inFile.useDelimiter("\n");
        String in = "";


        //in case load file is empty
        if (!inFile.hasNextLine())
            throw new FileNotFoundException();

        text = inFile.nextLine().trim();
        //Trim the array down

        in = inFile.nextLine();
        String[] line = in.split(" ");

        //check for encryption vs decryption
        if (line[0].compareTo("ENC") == 0){
            ENC = true;
        } else {
            ENC = false;
        }

        //store values to their corresponding variables
        p = Integer.parseInt(line[1]);
        q = Integer.parseInt(line[2]);
        e = Integer.parseInt(line[3]);


        inFile.close();

        return ENC;
    }

    private static ArrayList splitExponent(int d) {
        ArrayList<Integer> powers = new ArrayList<>(); //assuming we don't find a massive exponential value
        
        int j = 0;
        while (d > 0){
            int i = 1;
            while ((int) Math.pow(2,i) < d){
                i++;
            }
            i--;
            powers.add(i);
            d = d - (int) Math.pow(2,i);
            j++;
        }
        return powers;
    }

    
    
}
