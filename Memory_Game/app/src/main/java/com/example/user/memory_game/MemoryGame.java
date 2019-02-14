package com.example.user.memory_game;

import java.security.InvalidParameterException;
import java.util.Random;

public class MemoryGame {

    public static final int REMOVE = 1;
    public static final int CLOSE = 2;
    public static final String DELIMITER = "&&&";
    public static final String DELIMITER_TO_ARRAY = ",,,";
    public static final String TAG = "gitit";
    private int tagOfCardOpen1;
    private int tagOfCardOpen2;
    private int indexOfCardOpen1;
    private int indexOfCardOpen2;
    private boolean isOneCardOpen;
    private boolean isInAction;
    private int[] tagsArray;
    private Card[] cardsArray;
    private int level;
    private Score score;
    private OnWinListener OnWinListener;

    //start new game
    public MemoryGame(int level){
        this(level, 0);
    }
    //new game after win
    public MemoryGame(int level, int points){
        tagOfCardOpen1 = -1;
        tagOfCardOpen2 = -1;
        indexOfCardOpen1 = -1;
        indexOfCardOpen2 = -1;
        this.level = level;
        int length = cardsOnTheBord();
        tagsArray = new int[length];
        getMessArray(length);
        cardsArray = new Card[length];
        this.score = new Score(points);
    }

    //Start saved game in sharedPreferences or saveInstanceState
    public MemoryGame(String gameAsString){

        if (gameAsString == null)
            throw new InvalidParameterException();
        String[] parts = gameAsString.split(DELIMITER);
        if (parts.length != 9)
            throw new InvalidParameterException("Must have 8 parts");
        try {
            //part [0] = tagOfCardOpen1
            tagOfCardOpen1 = Integer.valueOf(parts[0]);

            //part [1] = tagOfCardOpen2
            tagOfCardOpen2 = Integer.valueOf(parts[1]);

        }catch (Exception e){
            throw new InvalidParameterException("Tag of open cards must be Integer");
        }

        try {
            //part [2] = isOneCardOpen
            isOneCardOpen = Boolean.valueOf(parts[2]);

            //part [3] = isInAction
            isInAction = Boolean.valueOf(parts[3]);

        }catch (Exception e){
            throw new InvalidParameterException("isOneCardOpen & isInAction must be Boolean");
        }

        //part [4] = tagsArrayAsSting
        String[] tagsParts = parts[4].split(DELIMITER_TO_ARRAY);
        if (tagsParts.length != 6 && tagsParts.length != 12 && tagsParts.length != 16)
            throw new InvalidParameterException("Must have 6, 12 or 16 parts");
        tagsArray = new int[tagsParts.length];
        for (int i=0; i < tagsParts.length; i++) {
            try{
                tagsArray[i] = Integer.valueOf(tagsParts[i]);
            } catch (Exception e){
                throw new InvalidParameterException();
            }
        }

        try {
            //part [5] = level
            level = Integer.valueOf(parts[5]);
        } catch (Exception e){
            throw new InvalidParameterException("Level must be Integer");
        }

        //part [6] = score
        score = new Score(parts[6]);


        try {
            //part [7] = placeOfTagOfCardOpen1
            indexOfCardOpen1 = Integer.valueOf(parts[7]);
            //part [7] = placeOfTagOfCardOpen2
            indexOfCardOpen2 = Integer.valueOf(parts[8]);
        }catch (Exception e){
            throw new InvalidParameterException("index must be Integer");
        }
        cardsArray = new Card[tagsArray.length];

    }
    public int getScore() {
        return score.getScore();
    }
    private void getMessArray(int length){
        for (int i = 0; i < length/2; i++) {
            tagsArray[i] = i;
            tagsArray[i + length/2] = i;
        }
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 2; j++) {
                int changePlace = r.nextInt(length);
                int temp = tagsArray[i];
                tagsArray[i] = tagsArray[changePlace];
                tagsArray[changePlace] = temp;
            }

        }
    }
    public void addCardToArray(Card card, int index){
        cardsArray[index] = card;

    }
    public void showFrontCard(int index){
        cardsArray[index].showFrontCard();
    }
    public void showBackCard(int tag){
        cardsArray[tag].showBackCard();
    }
    public Card getCardByTag(int tag){
        return cardsArray[tag];
    }
    public void setEnabledCards(boolean bool) {
        Card.setEnabledCards( bool, cardsArray);

    }
    public void removeOrCloseCouple(int action){
        Card.removeOrCloseCouple(cardsArray[indexOfCardOpen1], cardsArray[indexOfCardOpen2], action);
    }
    public Card[] getCardsArray() {
        return cardsArray;
    }
    public void setCardsArray(Card[] cardsArray) {
        this.cardsArray = cardsArray;
    }
    public int getTagOfCardOpen1() {
        return tagOfCardOpen1;
    }
    public void setTagOfCardOpen1(int tagOfCardOpen1) {
        this.tagOfCardOpen1 = tagOfCardOpen1;
    }
    public int getTagOfCardOpen2() {
        return tagOfCardOpen2;
    }
    public void setTagOfCardOpen2(int tagOfCardOpen2) {
        this.tagOfCardOpen2 = tagOfCardOpen2;
    }
    public int getIndexOfCardOpen1() {
        return indexOfCardOpen1;
    }
    public void setIndexOfCardOpen1(int indexOfCardOpen1) {
        this.indexOfCardOpen1 = indexOfCardOpen1;
    }
    public int getIndexOfCardOpen2() {
        return indexOfCardOpen2;
    }
    public void setIndexOfCardOpen2(int indexOfCardOpen2) {
        this.indexOfCardOpen2 = indexOfCardOpen2;
    }
    public boolean isOneCardOpen() {
        return isOneCardOpen;
    }
    public void setOneCardOpen(boolean oneCardOpen) {
        isOneCardOpen = oneCardOpen;
    }
    public boolean isInAction() {
        return isInAction;
    }
    public void setInAction(boolean inAction) {
        isInAction = inAction;
    }
    public int[] getTagsArray() {
        return tagsArray;
    }
    public int getTagOfCardByIndex(int index) {
        return tagsArray[index];
    }
    public void setTagsArray(int[] tagsArray) {
        this.tagsArray = tagsArray;
    }
    public int getLevel(){
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getRow(){
        if (level == 1)
            return 3;
        return 4;

    }
    public int getColumn(){
        if (level == 1)
            return 2;
        if (level == 2)
            return 3;
        return 4;

    }
    public void setOnWinListener(OnWinListener OnWinListener) {
        this.OnWinListener = OnWinListener;
    }
    public int cardClicked(int index) {
        if (!isOneCardOpen) {
            openCard1(index);
            return 1;
        } else {
            openCard2(index);
            return 2;

        }
    }
    private void openCard1(int index){
        indexOfCardOpen1 = index;
        tagOfCardOpen1 = cardsArray[index].getTag();
        isOneCardOpen = true;
    }
    private void openCard2(int index){
        indexOfCardOpen2 = index;
        tagOfCardOpen2 = cardsArray[index].getTag();
        isOneCardOpen = false;
    }
    public int closeOrRemoveOpenCards(){
        boolean isTheSame = tagOfCardOpen1 == tagOfCardOpen2;
        isInAction = false;
        if (isTheSame) {
            removeCardsFromArrayTagsCards();
            score.succeeded();
            if (isDoneAllCards()) {
                if (OnWinListener != null)
                    OnWinListener.childIsWin();
            }
            tagOfCardOpen1 = -1;
            tagOfCardOpen2 = -1;
            return REMOVE;
        }
        else {
            score.failed();
            tagOfCardOpen1 = -1;
            tagOfCardOpen2 = -1;
            return CLOSE;
        }

    }
    public boolean isDoneAllCards(){
        return score.getNumberOfSuccess() == cardsOnTheBord();
    }
    private void removeCardsFromArrayTagsCards(){
        for (int i = 0; i < tagsArray.length; i++) {
            if (tagsArray[i] == tagOfCardOpen1)
                tagsArray[i] = -1;

        }
    }
    public int cardsOnTheBord(){
        switch (level){
            case 1:
                return 6;
            case 2:
                return 12;
            case 3:
                return 16;
        }
        return 0;
    }
    @Override
    public String toString() {
        return tagOfCardOpen1 + DELIMITER 
                + tagOfCardOpen2 + DELIMITER
                + isOneCardOpen + DELIMITER
                + isInAction + DELIMITER
                + tagsArrayAsSting() + DELIMITER
                + level + DELIMITER
                + score.toString() + DELIMITER
                + indexOfCardOpen1 + DELIMITER
                + indexOfCardOpen2;
    }
    public String tagsArrayAsSting(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int atagsArray : tagsArray) {
            stringBuilder.append(atagsArray).append(DELIMITER_TO_ARRAY);
        }
        return stringBuilder.toString();
    }
    interface OnWinListener{
        void childIsWin();
    }
}
