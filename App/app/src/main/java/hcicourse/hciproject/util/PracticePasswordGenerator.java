package hcicourse.hciproject.util;

import java.util.Random;

import hcicourse.hciproject.data_structures.Password;

/**
 * Created by alanwu on 2015-11-16.
 */
public class PracticePasswordGenerator {

    private static final String [] pinPasswords = {
            "2,3,5,9",

            "0,8,6,2",

            "3,6,2,4",

            "1,7,8,5,2",

            "4,1,2,8,9",

            "7,8,2,3,6",

            "7,3,6,8,5,1",

            "4,1,9,8,6,2",

            "0,7,3,6,2,1"
    };

    private static final String [] patternPasswords = {
            "1,2,5,4",

            "6,3,4,1",

            "7,4,1,0",

            "5,4,3,0,1",

            "8,7,4,3,0",

            "7,6,3,4,1",

            "8,4,2,1,0,3",

            "0,1,5,7,6,3",

            "8,4,0,1,5,2"
    };

    private static final String [] dialPasswords = {
            "4,5,2,3",

            "7,9,8,0",

            "6,5,6,3",

            "5,3,6,4",

            "1,7,9,8",

            "2,3,9,0",

            "2,4,8,0",

            "6,3,8,6",

            "1,4,0,3"
    };


    private Random rand;

    public PracticePasswordGenerator(){
        rand = new Random();
    }

    public Password GetRandomPasswordPin(){
        int index = rand.nextInt(pinPasswords.length);

        Password pass = new Password(pinPasswords[index]);
        return pass;
    }

    public Password GetRandomPasswordPattern(){
        int index = rand.nextInt(patternPasswords.length);

        Password pass = new Password(patternPasswords[index]);
        return pass;
    }

    public Password GetRandomPasswordDial(){
        int index = rand.nextInt(dialPasswords.length);

        Password pass = new Password(dialPasswords[index]);
        return pass;
    }
}
