package com.example.user.memory_game;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HighScoreActivity extends Activity {


    public static final String SCORE_FILE = "score_file";
    public static final String SCORES_SET = "scores_set";
    public static final String USER_FILE = "userFile";
    public static final String USER_KEY = "userKey";
    public static final String GITIT = "gitit";
    public static final String TAG = "gitit";

    private ViewPager viewPager;
    private int[] layouts = new int[]{R.layout.high_score_fragment,R.layout.high_score_fragment};
    private HighScoresAdapter highScoresAdapter;
    private List<WinInGame> privateWins;
    private List<WinInGame> publicWins;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        privateWins = new ArrayList<>();
        publicWins = new ArrayList<>();

        Set<String> privateWinsAsString = new HashSet<>();


        //pull set of scores from sharedPreferences
        SharedPreferences pref = getSharedPreferences(SCORE_FILE, MODE_PRIVATE);
        if (pref.contains(SCORES_SET)) {
            privateWinsAsString = pref.getStringSet(SCORES_SET, null);
        }


        WinInGame[] array = new WinInGame[privateWinsAsString.size()];
        int i = 0;
        for (String win : privateWinsAsString) {
            array[i] = new WinInGame(win);
            i++;
        }
        WinInGame.sortArrayWines(array, 0, array.length-1);

        privateWins.addAll(Arrays.asList(array));


        /*
        String userAsString = getSharedPreferences(USER_FILE,MODE_PRIVATE).getString(USER_KEY, null);
        if (userAsString != null){
            User user = new User(userAsString);
            Set<String> allWins = user.getAllWins();
            for (String win : allWins) {
                if (win == null){
                    Log.d(TAG, "onCreate: null");
                }
                privateWins.add(new WinInGame(win));
            }
        }
        */
        viewPager = findViewById(R.id.pager);
        highScoresAdapter = new HighScoresAdapter(layouts, this);
        viewPager.setAdapter(highScoresAdapter);


    }

    class HighScoresAdapter extends PagerAdapter{

        private int[] layouts;
        private LayoutInflater inflater;
        private Context context;
        private ListScoresAdapter listScoresAdapter;
        private ListView listViewPrivate;
        private TextView lblTitle;



        public HighScoresAdapter(int[] layouts, Context context){
            this.layouts = layouts;
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = inflater.inflate(layouts[position],container,false);
            lblTitle = view.findViewById(R.id.lblTitle);

            switch (position){
                case 0:
                    setListViewAdapter("Your high score", view, privateWins);
                break;
                case 1:
                    setListViewAdapter("Public high score", view, publicWins);
                    break;
            }
            container.addView(view);
            return view;
        }

        private void setListViewAdapter(String title, View view, List<WinInGame> list){
            lblTitle.setText(title);
            listViewPrivate = view.findViewById(R.id.listView);
            listScoresAdapter = new ListScoresAdapter((Activity) view.getContext(), list);
            listViewPrivate.setAdapter(listScoresAdapter);
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    class ListScoresAdapter extends ArrayAdapter<WinInGame>{


                private Activity activity;
                private List<WinInGame> winInGames;

                public ListScoresAdapter(Activity activity, List<WinInGame> winInGames){
                    super(activity, R.layout.item_win, winInGames);
                    this.activity = activity;
                    this.winInGames = winInGames;
                }

                class ViewContainer{
                    TextView lblScore, lblUserName, lblDate, lblHour;

                }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            ViewContainer viewContainer;
            if (view == null) {
                view = activity.getLayoutInflater().inflate(R.layout.item_win, parent, false);
                viewContainer = new ViewContainer();
                viewContainer.lblScore = view.findViewById(R.id.lblScore);
                viewContainer.lblUserName = view.findViewById(R.id.lblUserName);
                viewContainer.lblDate = view.findViewById(R.id.lblDate);
                viewContainer.lblHour = view.findViewById(R.id.lblHour);
                view.setTag(viewContainer);
            }else {
                viewContainer = (ViewContainer) view.getTag();
            }
            WinInGame win = winInGames.get(position);
            viewContainer.lblScore.setText(String.valueOf(win.getScore()));
            viewContainer.lblUserName.setText(win.getUser());
            viewContainer.lblDate.setText(win.getDate());
            viewContainer.lblHour.setText(win.getHour());
            return view;

        }
    }


}
