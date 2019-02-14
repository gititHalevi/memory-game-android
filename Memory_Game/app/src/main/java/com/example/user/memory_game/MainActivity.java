package com.example.user.memory_game;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {

    public static final String LEVEL = "level";
    public static final String TAG = "gitit";
    public static final String USER_FILE = "userFile";
    public static final String USER_KEY = "userKey";
    private LinearLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        titleLayout = findViewById(R.id.titleLayout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String user = getSharedPreferences(USER_FILE,MODE_PRIVATE).getString(USER_KEY, null);
        if (user != null){
            showUserName(new User(user));
        }else {
            showLoginRegisterButtons();
        }
    }

    private void showLoginRegisterButtons(){
        titleLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0
                , ViewGroup.LayoutParams.MATCH_PARENT, 1);

        LinearLayout linearLayoutLogin = new LinearLayout(this);
        LinearLayout linearLayoutRegister = new LinearLayout(this);
        linearLayoutLogin.setGravity(Gravity.CENTER);
        linearLayoutRegister.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParamsButtons = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        titleLayout.addView(linearLayoutLogin, layoutParams);
        titleLayout.addView(linearLayoutRegister, layoutParams);


        ImageButton login = new ImageButton(this);
        ImageButton register = new ImageButton(this);

        login.setBackground(getDrawable(R.drawable.login_button));
        login.setTag("login");
        login.setOnClickListener(listenerLoginRegister);
        register.setBackground(getDrawable(R.drawable.register_button));
        register.setTag("register");
        register.setOnClickListener(listenerLoginRegister);
        linearLayoutLogin.addView(login,layoutParamsButtons);
        linearLayoutRegister.addView(register, layoutParamsButtons);

    }
    private void showUserName(User user){
        titleLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (0,ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = new TextView(this);
        textView.setText("Welcome " + user.getUserName());
        textView.setTextColor(getColor(R.color.bordo));
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleLayout.addView(textView, params);
    }
    private View.OnClickListener listenerLoginRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tag = (String) view.getTag();
            openDialogLoginRegister(tag);
        }
    };
    public void startGame(View view) {

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(LEVEL, 3);
        startActivity(intent);
    }
    public void btnSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void btnHighScore(View view) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }
    private void openDialogLoginRegister(final String action){
        LoginRegisterDialog dialog = new LoginRegisterDialog();
        dialog.setAction(action);
        dialog.setListener(new LoginRegisterDialog.LoginOrRegisterListener() {
            @Override
            public void onOk(String userName, String password) {
                User user = new User(userName,password);
                User justCode = new User(action, "");
                new LoginOrRegisterTask().execute(user, justCode);

            }
        });
        dialog.show(getFragmentManager(), "LoginRegisterDialog");
    }

    private class LoginOrRegisterTask extends AsyncTask<User,String,User> {

        private YesNoFragment fragment;

        @Override
        protected void onPreExecute() {
            fragment = new YesNoFragment();
            fragment.setQuestion("Please waite...");
            fragment.setIsProgressBar();
            fragment.setBtnNo("Cancel");
            fragment.setYesNoFragmentListener(new YesNoFragment.YesNoFragmentListener() {
                @Override
                public void onChoose(boolean isYes) {
                    cancel(true);
                }
            });
            fragment.show(getFragmentManager(),"");
        }

        @Override
        protected User doInBackground(User... users) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            URL url = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;
            try {
                url = new URL("http://10.0.2.2:8080/MemoryGameServer_war_exploded/MemoryGameServlet");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.connect();
                outputStream = connection.getOutputStream();
                String action = users[1].getUserName();
                String s = action + ";;;" + users[0].toString();
                outputStream.write(s.getBytes());
                inputStream = connection.getInputStream();
                int responseCode = inputStream.read();

                switch (responseCode) {
                    case 0:
                        return null;
                    case 1:
                        byte[] buffer = new byte[1024];
                        StringBuilder builder = new StringBuilder();
                        int actuallyRead;
                        while ((actuallyRead = inputStream.read(buffer)) != -1) {
                            builder.append(new String(buffer, 0, actuallyRead));
                        }
                        User userFromServer = new User(builder.toString());
                        getSharedPreferences(USER_FILE, MODE_PRIVATE)
                                .edit()
                                .putString(USER_KEY, userFromServer.toString())
                                .apply();
                        return userFromServer;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            fragment.dismiss();
            if (user == null) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                showUserName(user);
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}

