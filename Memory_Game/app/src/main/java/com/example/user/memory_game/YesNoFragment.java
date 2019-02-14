package com.example.user.memory_game;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class YesNoFragment extends DialogFragment {


    private String question;
    private String btnYesText;
    private String btnNoText;
    private YesNoFragmentListener yesNoFragmentListener;
    private boolean isProgressBar;


    public void setQuestion(String question){
        this.question = question;
    }
    public void setBtnYes(String btnYesText){
        this.btnYesText = btnYesText;
    }
    public void setBtnNo(String btnNoText){
        this.btnNoText = btnNoText;
    }
    public void setIsProgressBar(){
        isProgressBar = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yesno, container, false);

        if(question != null){
            TextView lblQuestion = view.findViewById(R.id.lblQuestion);
            lblQuestion.setText(question);
            lblQuestion.setVisibility(View.VISIBLE);
        }
        if (isProgressBar){
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        if (btnNoText != null && !btnNoText.isEmpty()){
            Button btnNo = view.findViewById(R.id.btnNo);
            btnNo.setVisibility(View.VISIBLE);
            btnNo.setText(btnNoText);
            btnNo.setOnClickListener(listener);

        }


        if (btnYesText != null && !btnYesText.isEmpty()){
            Button btnYes = view.findViewById(R.id.btnYes);
            btnYes.setText(btnYesText);
            btnYes.setVisibility(View.VISIBLE);
            btnYes.setOnClickListener(listener);
        }


        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isYes = false;
            switch (view.getId()) {
                case R.id.btnYes:
                    isYes = true;
                    break;
                case R.id.btnNo:
                    isYes = false;
                    break;
            }
            if(yesNoFragmentListener != null) {
                yesNoFragmentListener.onChoose(isYes);
                dismiss();
            }else
                dismiss();
        }
    };

    public void setYesNoFragmentListener(YesNoFragmentListener yesNoFragmentListener) {
        this.yesNoFragmentListener = yesNoFragmentListener;
    }

    public interface YesNoFragmentListener{
        void onChoose(boolean isYes);
    }
}
