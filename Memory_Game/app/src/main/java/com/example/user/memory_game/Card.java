package com.example.user.memory_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Card {
    public static final String TAG = "gitit";
    private LinearLayout linearLayout;
    private LinearLayout backCardLayout;
    private ImageView backCard;
    private ImageView  frontCard;
    private boolean isOpen;
    private boolean isExistOnTheBord;
    private Context context;
    private int tag;
    private int index;
    private boolean isBitmapBack;
    public static final int REMOVE = 1;
    public static final int CLOSE = 2;

    public Card(Context context, int cardTag, int index, boolean isBitmapBack) {
        this.tag = cardTag;
        this.index = index;
        this.context = context;
        this.isBitmapBack = isBitmapBack;
        linearLayout = new LinearLayout(context);
        linearLayout.setTag(tag);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        setFrontCard();
        setBackCard(index);

        isExistOnTheBord = true;

    }
    public void setImages(int drawableOfFrontCards, int backOfCards) {
        frontCard.setBackground(context.getDrawable(drawableOfFrontCards));
        backCard.setBackground(context.getDrawable(backOfCards));

    }
    public void setImages(int drawableOfFrontCards, Bitmap bitmap){
        frontCard.setBackground(context.getDrawable(drawableOfFrontCards));
        /*
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        backCard.setLayoutParams(params);
*/

        backCardLayout.setBackground(context.getDrawable(R.drawable.shape));
        backCard.setImageBitmap(bitmap);

    }
    public void showFrontCard(){
        if (isBitmapBack){
            backCardLayout.setVisibility(View.GONE);
        }else {
            backCard.setVisibility(View.GONE);
        }
        frontCard.setVisibility(View.VISIBLE);
        isOpen = true;
    }
    public void showBackCard(){
        if (!isBitmapBack) {
            backCard.setVisibility(View.VISIBLE);
        }else {
            backCardLayout.setVisibility(View.VISIBLE);
        }
        frontCard.setVisibility(View.GONE);
        isOpen = false;
    }
    public static void removeOrCloseCouple(Card card1, Card card2, int action){
        switch (action){
            case CLOSE:
                card1.showBackCard();
                card2.showBackCard();
                break;
            case REMOVE:
                card1.linearLayout.setVisibility(View.INVISIBLE);
                card2.linearLayout.setVisibility(View.INVISIBLE);
                break;
        }

    }
    public void addToView(LinearLayout rowLayout){
        rowLayout.addView(linearLayout);
        linearLayout.addView(frontCard);
        if (isBitmapBack){
            linearLayout.addView(backCardLayout);
            backCardLayout.addView(backCard);
        }else {
            linearLayout.addView(backCard);
        }

    }
    public int getTag() {
        return tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public ImageView getBackCard() {
        return backCard;
    }

    public void setBackCard(ImageView backCard) {
        this.backCard = backCard;
    }
    public void setBackCard(int index){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        backCard = new ImageView(context);
        if (!isBitmapBack) {
            backCard.setLayoutParams(params);
        }else {
            backCardLayout = new LinearLayout(context);
            params.setMargins(30,15,30,45);
            backCardLayout.setLayoutParams(params);
            LinearLayout.LayoutParams backCardParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            backCardParams.setMargins(2,2,2,2);
            backCard.setLayoutParams(backCardParams);
        }
        backCard.setTag(index);

    }
    public void setFrontCard(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0,5,0,0);
        frontCard = new ImageView(context);
        frontCard.setVisibility(View.GONE);
        frontCard.setLayoutParams(params);
    }
    public void showCard(){

        View view;
        if (isBitmapBack){
            view = backCardLayout;
        }else {
            view = backCard;
        }
        Animation animation = new RotateAnimation(0,360
                ,view.getPivotX(),view.getPivotY());
        animation.setDuration(500);

        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                showFrontCard();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    public ImageView getFrontCard() {
        return frontCard;
    }

    public void setFrontCard(ImageView frontCard) {
        this.frontCard = frontCard;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isExistOnTheBord() {
        return isExistOnTheBord;
    }

    public void setExistOnTheBord(boolean existOnTheBord) {
        isExistOnTheBord = existOnTheBord;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setListener(View.OnClickListener listener){
        backCard.setOnClickListener(listener);
    }

    public static void setEnabledCards(boolean bool, Card... allCardsArray){
        for (Card card : allCardsArray) {
            if (card.isExistOnTheBord()) {
                card.backCard.setEnabled(bool);
            }
        }
    }
}
