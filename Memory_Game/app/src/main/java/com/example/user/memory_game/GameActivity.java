package com.example.user.memory_game;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class GameActivity extends Activity {


    public static final String USER_FILE = "userFile";
    public static final String USER_KEY = "userKey";
    public static final String SETTINGS_FILE = "settings_file";
    public static final String LEVEL_KEY = "level_key";
    public static final String SCORE_FILE = "score_file";
    public static final String SCORES_SET = "scores_set";
    public static final String FRONT_CARDS = "front_cards";
    public static final String BACK_CARDS = "back_cards";
    public static final String MY_GAME = "My_game";
    public static final String SAVE_GAME = "save_game";
    public static final String DELIMITER = "delimiter";
    public static final String DRAWABLE = "drawable";
    public static final String PHOTO = "photo";


    private MemoryGame game;
    private int level;
    private LinearLayout cardsLayout;

    private Card cardOpen1;
    private Card cardOpen2;
    private int[] picturesOfCardsArray = new int[8];
    private int backOfCards;
    //private String pathBackOfCardsBitmap;
    private Bitmap bitmap;
    private int numberOfOpenCards;
    private TextView lblScore;
    private int orient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_game);
        cardsLayout = findViewById(R.id.cardsLayout);
        lblScore = findViewById(R.id.lblScore);


        loadingGame(savedInstanceState);
    }
    private void loadingGame(Bundle savedInstanceState){


        orientation();
        getSavedInformation(savedInstanceState);
        if (game == null)
            game = new MemoryGame(level);
        startGame();

    }
    private void orientation(){
        orient = getResources().getConfiguration().orientation;
        if(orient == Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout mainLayout = findViewById(R.id.mainLayout);
            mainLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout titleLayout = findViewById(R.id.titleLayout);
            titleLayout.setOrientation(LinearLayout.VERTICAL);
            titleLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            findViewById(R.id.lblLine).setLayoutParams(new LinearLayout.LayoutParams(10, ViewGroup.LayoutParams.MATCH_PARENT));
            cardsLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));
            mainLayout.addView(new TextView(this), new LinearLayout.LayoutParams
                    (0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            findViewById(R.id.btnBack).setLayoutParams(new LinearLayout.LayoutParams
                    (200, 0, 0.5f));
            findViewById(R.id.lblScore).setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, 0, 3));
            findViewById(R.id.btnNewGame).setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        }
    }
    private void getSavedInformation(Bundle savedInstanceState){
        //settings in sharedPreferences
        SharedPreferences pref = getSharedPreferences(SETTINGS_FILE,MODE_PRIVATE);
        level = pref.getInt(LEVEL_KEY, 1);
        String typeOfCards = pref.getString(FRONT_CARDS, "disney");
        updatePicturesOfCardsArray(typeOfCards);
        typeOfCards = pref.getString(BACK_CARDS, "1");
        updateBackOfCards(typeOfCards);

        //check if its a saving game in savedInstanceState
        if (savedInstanceState != null) {
            String myGame = savedInstanceState.getString(MY_GAME);
            if (myGame != null) {
                game = new MemoryGame(myGame);
                //lblScore.setText("Score: " + game.getScore());
            }
        }
    }
    public void updatePicturesOfCardsArray(String typeOfFrontCards) {
        switch (typeOfFrontCards){
            case "disney":

                picturesOfCardsArray[0] = R.drawable.disney1;
                picturesOfCardsArray[1] = R.drawable.disney2;
                picturesOfCardsArray[2] = R.drawable.disney3;
                picturesOfCardsArray[3] = R.drawable.disney4;
                picturesOfCardsArray[4] = R.drawable.disney5;
                picturesOfCardsArray[5] = R.drawable.disney6;
                picturesOfCardsArray[6] = R.drawable.disney7;
                picturesOfCardsArray[7] = R.drawable.disney8;
                break;
            case "area":
                picturesOfCardsArray[0] = R.drawable.area1;
                picturesOfCardsArray[1] = R.drawable.area2;
                picturesOfCardsArray[2] = R.drawable.area3;
                picturesOfCardsArray[3] = R.drawable.area4;
                picturesOfCardsArray[4] = R.drawable.area5;
                picturesOfCardsArray[5] = R.drawable.area6;
                picturesOfCardsArray[6] = R.drawable.area7;
                picturesOfCardsArray[7] = R.drawable.area8;
                break;
        }
    }
    private void updateBackOfCards(String typeOfBackCards) {
        String[] strings = typeOfBackCards.split(DELIMITER);
        if (strings.length != 2) {
            backOfCards = R.drawable.background_1;
            return;
        }
        switch (strings[0]){
            case DRAWABLE:
                switch (strings[1]){
                    case "1":
                        backOfCards = R.drawable.background_1;
                        break;
                    case "2":
                        backOfCards = R.drawable.background_2;
                        break;
                    case "3":
                        backOfCards = R.drawable.background_3;
                        break;
                }
                break;
            case PHOTO:
                backOfCards = 0;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(strings[1], bitmapOptions);
                int photoW = bitmapOptions.outWidth;
                int photoH = bitmapOptions.outHeight;
                int[] widthAndHeight = getWidthAndHeight();
                int cardW = widthAndHeight[0];
                int cardH = widthAndHeight[1];
                int scaleFactor = Math.max(photoW/cardW, photoH/cardH);
                bitmapOptions.inJustDecodeBounds = false;
                bitmapOptions.inSampleSize = scaleFactor;
                bitmap = BitmapFactory.decodeFile(strings[1],bitmapOptions);
                bitmap = getRoundedCornerBitmap(bitmap, 35);
                break;

        }

    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        Bitmap roundedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return roundedBitmap;
    }
    private int[] getWidthAndHeight(){
        int[] widthAndHeight = new int[2];
        //widthAndHeight[0] = width.        widthAndHeight[1] = height
        if (orient == Configuration.ORIENTATION_PORTRAIT){
            switch (level){
                case 1:
                    widthAndHeight[0] = 520;
                    widthAndHeight[1] = 442;
                    break;
                case 2:
                    widthAndHeight[0] = 340;
                    widthAndHeight[1] = 327;
                    break;
                case 3:
                    widthAndHeight[0] = 250;
                    widthAndHeight[1] = 327;
                    break;
            }
        }else {
            switch (level){
                case 1:
                    widthAndHeight[0] = 426;
                    widthAndHeight[1] = 319;
                    break;
                case 2:
                    widthAndHeight[0] = 277;
                    widthAndHeight[1] = 234;
                    break;
                case 3:
                    widthAndHeight[0] = 203;
                    widthAndHeight[1] = 234;
                    break;
            }
        }

        return widthAndHeight;
    }
    private void startGame(){
        game.setOnWinListener(new MemoryGame.OnWinListener() {
            @Override
            public void childIsWin() {
                new GameActivity.OnWinTask().execute();
            }
        });
        numberOfOpenCards = 0;
        createCardsOnTheBord();
        if (game.isOneCardOpen()){
            game.getCardsArray()[game.getIndexOfCardOpen1()].showFrontCard();
            numberOfOpenCards ++;
        }
    }
    private void createCardsOnTheBord(){
        int row = game.getRow(), column = game.getColumn(), index=0;
        boolean isBitmapBack = bitmap != null;
        for (int i = 0; i < row; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            cardsLayout.addView(rowLayout, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));

            for (int j = 0; j < column; j++) {

                Card card = new Card(this, game.getTagOfCardByIndex(index), index, isBitmapBack);
                card.setListener(listener);
                card.addToView(rowLayout);
                if (game.getTagOfCardByIndex(index) != -1) {
                    if (backOfCards != 0) {
                        card.setImages(picturesOfCardsArray[game.getTagOfCardByIndex(index)], backOfCards);
                    } else {
                        card.setImages(picturesOfCardsArray[game.getTagOfCardByIndex(index)], bitmap);
                    }
                }else {
                    card.getLinearLayout().setVisibility(View.INVISIBLE);
                }
                game.addCardToArray(card, index);
                index++;
            }
        }

    }
    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (numberOfOpenCards < 2) {
                view.setEnabled(false);
                new CardOpenTask((int) view.getTag()).execute();
                numberOfOpenCards++;
            }
        }};

    private class CardOpenTask extends AsyncTask<Void, Void, Integer > {
        private int index;

        public CardOpenTask(int index){
            this.index = index;

        }

        @Override
        protected void onPreExecute() {

            game.getCardsArray()[index].showCard();

        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int action = game.cardClicked(index);
            switch (action){
                case 1:
                    cardOpen1 = game.getCardByTag(index);
                    break;
                case 2:
                    cardOpen2 = game.getCardByTag(index);
                    publishProgress();
                    break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (action == 2){
                return game.closeOrRemoveOpenCards();
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            game.setEnabledCards(false);
        }

        @Override
        protected void onPostExecute(Integer action) {
            if (action == 0){
                return;
            }
            game.removeOrCloseCouple(action);
            cardOpen1 = null;
            cardOpen2 = null;
            lblScore.setText("Score: " + game.getScore());
            game.setEnabledCards(true);
            numberOfOpenCards = 0;
        }
    }
    private class OnWinTask extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String userAsString = getSharedPreferences(USER_FILE,MODE_PRIVATE).getString(USER_KEY, null);
            WinInGame win;
            if (userAsString == null){
                win = new WinInGame(game.getScore()
                        ,day,month,year,hour,minute,"No name");
            }else {
                User user = new User(userAsString);
                win = new WinInGame(game.getScore()
                        ,day,month,year,hour,minute,user.getUserName());
            }
            SharedPreferences pref = getSharedPreferences(SCORE_FILE,MODE_PRIVATE);
            Set<String> wins = pref.getStringSet(SCORES_SET, null);
            if (wins == null)
                wins = new HashSet<>();
            wins.add(win.toString());
            SharedPreferences.Editor editor = getSharedPreferences(SCORE_FILE, MODE_PRIVATE).edit();
            editor.clear();
            editor.putStringSet(SCORES_SET, wins);
            editor.apply();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startNewGame(game.getScore());
            Toast.makeText(GameActivity.this, "You win!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void btnBack(View view) {
        YesNoFragment yesNoFragment = new YesNoFragment();
        yesNoFragment.setQuestion("Are you sure you want to live this game?");
        yesNoFragment.setBtnYes("Yes");
        yesNoFragment.setBtnNo("No");
        yesNoFragment.setYesNoFragmentListener(new YesNoFragment.YesNoFragmentListener() {
            @Override
            public void onChoose(boolean isYes) {
                if (isYes)
                    finish();
            }
        });
        yesNoFragment.show(getFragmentManager(), "");
    }
    private void startNewGame(int score){
        cardOpen1 = null;
        cardOpen2 = null;
        game = new MemoryGame(level, score);
        cardsLayout.removeAllViews();
        startGame();
    }
    public void btnNewGame(View view) {
        YesNoFragment newGameFragment = new YesNoFragment();
        newGameFragment.setQuestion("are you sure you want to start a new game?");
        newGameFragment.setBtnYes("Yes");
        newGameFragment.setBtnNo("No");
        newGameFragment.setYesNoFragmentListener(new YesNoFragment.YesNoFragmentListener() {
            @Override
            public void onChoose(boolean isYes) {
                if (isYes){
                    startNewGame(0);
                    lblScore.setText("Score: 0");
                }
            }
        });
        newGameFragment.show(getFragmentManager(),"");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MY_GAME, game.toString());
    }

}
