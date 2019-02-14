package com.example.user.memory_game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SettingsActivity extends Activity {

    public static final String SETTINGS_FILE = "settings_file";
    public static final String LEVEL_KEY = "level_key";
    public static final String LIST = "list";

    public static final String IS_BACK_CARDS = "is back cards";
    private ImageButton btnLevel1;
    private ImageButton btnLevel2;
    private ImageButton btnLevel3;
    private int level;
    private Button btnLogout;
    private LinearLayout bottomLayout;
    public static final String USER_FILE = "userFile";
    public static final String USER_KEY = "userKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnLevel1 = findViewById(R.id.btnLevel1);
        btnLevel2 = findViewById(R.id.btnLevel2);
        btnLevel3 = findViewById(R.id.btnLevel3);
        SharedPreferences pref = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        if (pref.contains(LEVEL_KEY)) {
            level = pref.getInt(LEVEL_KEY,1);
        }
        else
            level = 1;
        setLevel(level);
        bottomLayout = findViewById(R.id.bottomLayout);
        btnLogout = findViewById(R.id.btnLogout);
        String userAsString = getSharedPreferences(USER_FILE,MODE_PRIVATE).getString(USER_KEY, null);
        if (userAsString != null) {
            btnLogout.setVisibility(View.VISIBLE);
        }else {

        }


    }


    private void setLevel(int level) {
        switch (this.level){
            case 1:
                btnLevel1.setBackground(getDrawable(R.drawable.string_1_blue));
                break;
            case 2:
                btnLevel2.setBackground(getDrawable(R.drawable.string_2_blue));
                break;
            case 3:
                btnLevel3.setBackground(getDrawable(R.drawable.string_3_blue));
                break;
        }
        this.level = level;
        switch (level){
            case 1:
                btnLevel1.setBackground(getDrawable(R.drawable.string_1_red));
                break;
            case 2:
                btnLevel2.setBackground(getDrawable(R.drawable.string_2_red));
                break;
            case 3:
                btnLevel3.setBackground(getDrawable(R.drawable.string_3_red));
                break;
        }
        getSharedPreferences(SETTINGS_FILE,MODE_PRIVATE).edit().putInt(LEVEL_KEY, this.level).apply();
        getSharedPreferences(GameActivity.SAVE_GAME,MODE_PRIVATE).edit().remove(GameActivity.MY_GAME).apply();
    }

    public void btnBack(View view) {
        finish();
    }

    public void btnLevel(View view) {
        String tag = (String) view.getTag();
        setLevel(Integer.valueOf(tag));
    }

    public void btnCardsView(View view) {
        String tag = (String) view.getTag();
        Intent intent = new Intent(this,CardsViewActivity.class);
        boolean isBackCards = tag.equals("backCardView");
        intent.putExtra(IS_BACK_CARDS, isBackCards);
        startActivity(intent);
        finish();
    }

    public void btnLogout(View view) {
        YesNoFragment fragment = new YesNoFragment();
        fragment.setQuestion("Are you sure you want to logout?");
        fragment.setBtnNo("No");
        fragment.setBtnYes("Yes");
        fragment.setYesNoFragmentListener(new YesNoFragment.YesNoFragmentListener() {
            @Override
            public void onChoose(boolean isYes) {
                if (isYes){
                    getSharedPreferences(USER_FILE, MODE_PRIVATE).edit().remove(USER_KEY).apply();
                    btnLogout.setVisibility(View.GONE);
                }
            }
        });
        fragment.show(getFragmentManager(), "");
    }
}
