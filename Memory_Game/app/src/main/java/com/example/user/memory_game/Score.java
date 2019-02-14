package com.example.user.memory_game;

import java.security.InvalidParameterException;

public class Score {
    public static final String DELIMITER = ",,,";
    private int points;
    private int successesSequentially;
    private int failuresInSequence;
    private int numberOfSuccess;



    public Score(){}
    
    //new game after win, save just the points
    public Score(int points){
        this.points = points;
    }

    public Score(String scoreAsString){
        String[] parts = scoreAsString.split(DELIMITER);
        if (parts.length != 4)
            throw new InvalidParameterException("Score must have 4 parts");
        try {


            //parts[0] = points
            points = Integer.valueOf(parts[0]);

            //parts[1] = successesSequentially
            successesSequentially = Integer.valueOf(parts[1]);

            //parts[2] = failuresInSequence
            failuresInSequence  = Integer.valueOf(parts[2]);

            //parts[3] = numberOfSuccess
            numberOfSuccess  = Integer.valueOf(parts[3]);

        }catch (Exception e){
            throw new InvalidParameterException("All the 4 parts must have numbers");
        }
    }

    public void failed(){
        successesSequentially = 0;
        points -= failuresInSequence*10;
        if (points < 0)
            points = 0;
        failuresInSequence++;

    }
    public void succeeded(){
        failuresInSequence = 0;
        successesSequentially++;
        points += 50*successesSequentially;
        numberOfSuccess += 2;
    }

    public int getScore() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSuccessesSequentially() {
        return successesSequentially;
    }

    public void setSuccessesSequentially(int successesSequentially) {
        this.successesSequentially = successesSequentially;
    }

    public int getFailuresInSequence() {
        return failuresInSequence;
    }

    public void setFailuresInSequence(int failuresInSequence) {
        this.failuresInSequence = failuresInSequence;
    }

    public int getNumberOfSuccess() {
        return numberOfSuccess;
    }

    @Override
    public String toString() {
        return points + DELIMITER
                + successesSequentially + DELIMITER
                + failuresInSequence + DELIMITER
                + numberOfSuccess;
    }



   
}
