package com.hoon.card;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Hoon on 2015-06-28.
 */
public class Card extends FrameLayout{
    private ImageView frontImageView;
    private  ImageView backImageView;

    private AnimatorSet setRightOut;
    private AnimatorSet setLeftIn;

    private int frontimageID;
    private boolean isFrontImageVisible = true;
    private boolean isBackImageClicked = false;
    private boolean isClickAble = true;
    private boolean isCorrect = false;

    public Card(Context context) {
        super(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        //params.setMargins(5, 5, 5, 5);
        this.setLayoutParams(params);

        params.weight = 1;

        frontImageView = new ImageView(context);
        frontImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        backImageView = new ImageView(context);
        backImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        backImageView.setImageResource(R.drawable.backimg);

        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);

        this.addView(backImageView);
        this.addView(frontImageView);
    }

    public void setFrontImage(int imageID) {
        frontimageID = imageID;
        frontImageView.setImageResource(frontimageID);
    }

    public int getFrontImage() {
        return frontimageID;
    }

    public void flipCard() {
        if (isFrontImageVisible)
            flipToBack();
        else
            flipToFront();
    }

    private void flipToFront() {
        setRightOut.setTarget(backImageView);
        setLeftIn.setTarget(frontImageView);
        setRightOut.start();
        setLeftIn.start();
        isFrontImageVisible = true;
    }

    private void flipToBack() {
        setRightOut.setTarget(frontImageView);
        setLeftIn.setTarget(backImageView);
        setRightOut.start();
        setLeftIn.start();
        isFrontImageVisible = false;
    }

    public void clickedBackImage() {
        if (isBackImageClicked) {
            backImageView.setImageResource(R.drawable.backimg);
            isBackImageClicked = false;
        } else {
            backImageView.setImageResource(R.drawable.clickedbackimg);
            isBackImageClicked = true;
        }
    }

    public boolean isBackImageClicked() {
        return isBackImageClicked;
    }

    public boolean isClickAble() {
        return isClickAble;
    }

    public void setIsClickAble(boolean isClickAble) {
        this.isClickAble = isClickAble;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
