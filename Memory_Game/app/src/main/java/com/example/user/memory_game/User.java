package com.example.user.memory_game;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class User {

    public static final String DELIMITER = "@@@";
    public static final String STR = "%%%";

    private String userName;
    private String password;
    private WinInGame myHighScore;
    private Set<String> allWins;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public User(String user){
        if (user == null)
            throw new InvalidParameterException();
        String[] parts = user.split(DELIMITER);
        if (parts.length != 4)
            throw new InvalidParameterException("User have to be 4 parts");
        userName = parts[0];
        password = parts[1];
        myHighScore = new WinInGame(parts[2]);
        if (myHighScore.getScore() == 0){
            myHighScore = null;
        }else {
            setAllWins(parts[3]);
        }
    }

    public WinInGame getMyHighScore() {
        return myHighScore;
    }

    public void setMyHighScore(WinInGame myHighScore) {
        this.myHighScore = myHighScore;
    }

    public Set<String> getAllWins() {
        return allWins;
    }
    public String getAllWinsAsString(){
        StringBuilder stringBuilder = new StringBuilder();
        boolean putSTR = false;
        for (String win : allWins) {
            if (putSTR){
                stringBuilder.append(STR);
                putSTR = true;
            }
            stringBuilder.append(allWins);

        }
        return stringBuilder.toString();
    }
    public void setAllWins(String allWinsString) {
        allWins = new HashSet<String>();
        allWins.addAll(Arrays.asList(allWinsString.split(STR)));

    }
    public void setAllWins(Set<String> allWins) {
        this.allWins = allWins;
    }
    public boolean addWinIsHighScore(WinInGame newWin){
        if (allWins == null){
            allWins = new HashSet<>();
        }
        allWins.add(newWin.toString());
        if (newWin.getScore() > myHighScore.getScore()){
            myHighScore = newWin;
            return true;
        }
        return false;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        if (myHighScore == null){
            myHighScore = WinInGame.getEmptyWin();
        }
        if (allWins == null){
            addWinIsHighScore(myHighScore);
        }
        return userName + DELIMITER
                + password + DELIMITER
                + myHighScore.toString() + DELIMITER
                + getAllWinsAsString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj instanceof User){
            User user = (User) obj;
            return userName.equals(user.userName) && password.equals(user.password);
        }
        return false;
    }
}
