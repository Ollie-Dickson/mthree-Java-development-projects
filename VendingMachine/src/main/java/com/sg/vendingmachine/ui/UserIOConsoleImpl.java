package com.sg.vendingmachine.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserIOConsoleImpl implements UserIO{

    Scanner sc = new Scanner(System.in);

    @Override
    public void print(String s){
        System.out.println(s);
    }

    @Override
    public String readString(String prompt){
        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt){
        boolean validInput = false;
        int input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Integer.parseInt(sc.nextLine());
                validInput = true;
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "an integer.");
            }
        }
        return input;
    }

    @Override
    public int readInt(String prompt, int min, int max){
        boolean validInput = false;
        int input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Integer.parseInt(sc.nextLine());
                validInput = true;
                if(input < min || input > max){
                    System.out.println("input does not fall in given range "
                            + "("+ min +" - "+ max +").");
                    validInput = false;
                }
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "an integer.");
            }
        }
        return input;
    }

    @Override
    public double readDouble(String prompt){
        boolean validInput = false;
        double input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Double.parseDouble(sc.nextLine());
                validInput = true;
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "a double.");
            }
        }
        return input;
    }

    @Override
    public double readDouble(String prompt, double min, double max){
        boolean validInput = false;
        double input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Double.parseDouble(sc.nextLine());
                validInput = true;
                if(input < min || input > max){
                    System.out.println("input does not fall in given range "
                            + "("+ min +" - "+ max +").");
                    validInput = false;
                }
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "a double.");
            }
        }
        return input;
    }

    @Override
    public float readFloat(String prompt){
        boolean validInput = false;
        float input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Float.parseFloat(sc.nextLine());
                validInput = true;
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "a float.");
            }
        }
        return input;
    }

    @Override
    public float readFloat(String prompt, float min, float max){
        boolean validInput = false;
        float input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Float.parseFloat(sc.nextLine());
                validInput = true;
                if(input < min || input > max){
                    System.out.println("input does not fall in given range "
                            + "("+ min +" - "+ max +").");
                    validInput = false;
                }
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "a float.");
            }
        }
        return input;
    }

    @Override
    public long readLong(String prompt){
        boolean validInput = false;
        long input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Long.parseLong(sc.nextLine());
                validInput = true;
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "a long.");
            }
        }
        return input;
    }

    @Override
    public long readLong(String prompt, long min, long max){
        boolean validInput = false;
        long input = 0;
        while(!validInput){
            try{
                System.out.println(prompt);
                input = Long.parseLong(sc.nextLine());
                validInput = true;
                if(input < min || input > max){
                    System.out.println("input does not fall in given range "
                            + "("+ min +" - "+ max +").");
                    validInput = false;
                }
            } catch(NumberFormatException ex) {
                System.out.println("Input could not be parsed into "
                        + "a long.");
            }
        }
        return input;
    }
}
