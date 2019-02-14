package com.example.user.memory_game;

import java.security.InvalidParameterException;

public class WinInGame {
    public static final String DELIMITER = "&&&";
    private int score;
    private int day;
    private int month;
    private int year;
    private int hours;
    private int minute;
    private String user;

    public WinInGame(int score, int day, int month, int year, int hours, int minute, String user) {
        this.score = score;
        setDay(day);
        setMonth(month);
        setYear(year);
        setHours(hours);
        setMinute(minute);
        this.user = user;
    }


    public WinInGame (String winAsString){
        String[] parts = winAsString.split(DELIMITER);
        if (parts.length != 7)
            throw new InvalidParameterException("Must be seven parts to wisAsString");
        try {
            //parts[0] = score
            setScore(Integer.valueOf(parts[0]));
        } catch (Exception e){
            throw new InvalidParameterException("score mast be number");
        }
        try{

            //parts[1] = day
            setDay(Integer.valueOf(parts[1]));

            //parts[2] = month
            setMonth(Integer.valueOf(parts[2]));

            //parts[3] = year
            setYear(Integer.valueOf(parts[3]));

            //parts[4] = hours
            setHours(Integer.valueOf(parts[4]));

            //parts[5] = seconds
            setMinute(Integer.valueOf(parts[5]));

        }catch (Exception e){
            throw new InvalidParameterException("The Parameters of number must be Integer");
        }

        //parts[6] = user
        user = parts[6];
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day < 1 || day > 32)
            throw new IndexOutOfBoundsException("Day must be between 1 to 32");
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
        throw new IndexOutOfBoundsException("Month must be between 1 to 12");
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 2019 || year > 2200)
            throw new IndexOutOfBoundsException("Year must be between 2019 to 2200");
        this.year = year;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if (hours < 0 || hours > 23)
            throw new IndexOutOfBoundsException("Hours must be between 0 to 23");
        this.hours = hours;
    }

    public int getSeconds() {
        return minute;
    }

    public void setMinute(int seconds) {
        if (seconds < 0 || seconds > 59)
            throw new IndexOutOfBoundsException("Seconds must be between 0 to 59");
        this.minute = seconds;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return score + DELIMITER
                + day + DELIMITER
                + month + DELIMITER
                + year + DELIMITER
                + hours + DELIMITER
                + minute + DELIMITER
                + user;
    }
    public static WinInGame getEmptyWin(){
        return new WinInGame(0,1,1,2020,0,0,"Non");
    }
    public String getDate(){
        return day + "/"
                + month + "/"
                + year;
    }
    public String getHour(){
        return (hours > 9 ? hours : "0" + hours) + ":" + (minute > 9 ? minute : "0" + minute);
    }

    public static void sortArrayWines(WinInGame[] scores, int from, int to){
        if(from >= to)
            return;

        int middle = from + (to-from)/2;
        sortArrayWines(scores, from, middle);
        sortArrayWines(scores, middle+1, to);
        merge(scores,from,middle,to);

    }

    public static void merge(WinInGame[] arr, int l, int m, int r){
        int n1 = m - l + 1;
        int n2 = r - m;

        //create temp arrays:
        WinInGame[] L = new WinInGame[n1];
        WinInGame[] R = new WinInGame[n2];

        //copy all the elements from arr to the temp arrays:
        for (int i = 0; i < n1; i++)
            L[i] = arr[l + i];  //L[0] = arr[l]
        for (int j = 0; j < n2; j++) {
            R[j] = arr[m + 1 + j];
        }

        int i=0,j=0;
        int k = l;
        while (i < n1 && j < n2){
            if(L[i].score >= R[j].score){
                arr[k] = L[i];
                i++;
            }else{
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1){
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2){
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}
