package com.hoon.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class GameActivity extends Activity{
    private static final String TAG = "GameActivity";
    private String name;
    private int stage, score, points;
    private int preScore, totalScore;
    private int numberOfCards;
    private int time = 70;
    private int row;

    private Integer[] resourceImages = {
            R.drawable.ddolgie, R.drawable.ddungee, R.drawable.hochi,
            R.drawable.shaechomi, R.drawable.drago, R.drawable.yorongee,
            R.drawable.macho, R.drawable.mimi, R.drawable.mongchi,
            R.drawable.kiki, R.drawable.kangdari, R.drawable.jjingjjingee};
    private int [] frontimages;

    private LinearLayout mainLayout;
    private TextView tvScore, tvTime, tvName, tvInfo;
    private Card [] cards;
    private Card selectedCard1 = null, selectedCard2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        stage = intent.getIntExtra("stage", 0);
        preScore = intent.getIntExtra("prescore", 0);

        mainLayout = (LinearLayout) findViewById(R.id.CardsLayout);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvName = (TextView) findViewById(R.id.tvName);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        initGame(stage);
    }

    private void initGame(int stage) {
        switch (stage) {
            case 0:
                numberOfCards = 6;
                points = 10;
                row = 2;
                break;
            case 1:
                numberOfCards = 12;
                points = 20;
                row = 3;
                break;
            case 2:
                numberOfCards = 24;
                points = 30;
                row = 4;
                break;
        }

        initRandomImages();
        initCards();
        setName();
        setTime(60);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void initCards() {
        int col = numberOfCards / row;
        LinearLayout [] horizontalLayouts = new LinearLayout[row];
        cards = new Card[numberOfCards];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        for (int i = 0 ; i < numberOfCards; ++ i) {
            cards[i] = new Card(getBaseContext());
            cards[i].setFrontImage(frontimages[i]);
            cards[i].setOnClickListener(new CardClickListener());
            cards[i].setTag(i);
            cards[i].setIsClickAble(false);
        }

        for (int i = 0; i < row; ++i) {
            horizontalLayouts[i] = new LinearLayout(getBaseContext());
            horizontalLayouts[i].setLayoutParams(params);
            horizontalLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < col; ++ j)
                horizontalLayouts[i].addView(cards[j + (col * i)]);

            mainLayout.addView(horizontalLayouts[i]);
        }
    }

    private void initRandomImages(){
        int cntImage = numberOfCards / 2;                              // 필요한 카드개수 1단계 3, 2단계 6, 3단계 12
        ArrayList<Integer> allImages = new ArrayList<Integer>();
        Collections.addAll(allImages, resourceImages);
        Collections.shuffle(allImages);                      // 이미지 섞기

        resourceImages = allImages.toArray(new Integer[12]);

        ArrayList<Integer> imageIndex = new ArrayList<Integer>();                 // index 랜덤 생성
        for (int i = 0; i < numberOfCards; ++i)
            imageIndex.add(i);
        Collections.shuffle(imageIndex);

        frontimages = new int[numberOfCards];
        for (int i = 0; i < numberOfCards; ++i)                           // frontimage 생성
            frontimages[(int)imageIndex.get(i)] = resourceImages[i % cntImage];

        allImages.clear();
        imageIndex.clear();
    }

    private void startGame() {
        for (int i = 0; i < numberOfCards; ++i) {
            cards[i].flipCard();
            cards[i].setIsClickAble(true);
        }
        setInfo("게임 시작!");
    }

    private void setName() {
        tvName.setText(name + " (" + stage+1 + ")");
    }

    private void setScore() {
        tvScore.setText("점수 : " + score);
    }

    private void setTime(int time) {
        tvTime.setText("남은시간 : " + time);
    }

    private void setInfo(String msg) {
        tvInfo.setText(msg);
    }

    private void compareTwoCards() {
        selectedCard1.flipCard();
        selectedCard2.flipCard();

        if (selectedCard1.getFrontImage() == selectedCard2.getFrontImage()) {
            selectSameCards();
        } else {
            setClickAble(false);
            handler.sendEmptyMessageDelayed(2, 1000);
        }
    }

    private void selectSameCards() {
        selectedCard1.setIsClickAble(false);
        selectedCard1.setIsCorrect(true);
        selectedCard2.setIsClickAble(false);
        selectedCard2.setIsCorrect(true);
        setInfo("정답입니다!");
        score += points;
        setScore();

        if (isGameClear()) {
            score += time;
            totalScore = preScore + score;
            stopTime();
            AlertDialog dialog = new AlertDialog(this, name, stage, score, totalScore, handler);
            dialog.show();
        }
        resetSelectedCards();
    }

    private boolean isGameClear() {
        if (score == (numberOfCards / 2 * points))
            return true;
        else
            return false;
    }

    private void setClickAble(boolean isClickAble) {
        if (isClickAble) {
            for (int i = 0; i < numberOfCards; ++i) {
                if (!cards[i].isCorrect())
                    cards[i].setIsClickAble(true);
            }
        } else {
            for (int i = 0; i < numberOfCards; ++i) {
                cards[i].setIsClickAble(false);
            }
        }
    }

    private void resetSelectedCards(){
        selectedCard1.clickedBackImage();
        selectedCard2.clickedBackImage();

        selectedCard1 = null;
        selectedCard2 = null;
    }

    private void stopTime() {
        handler.removeMessages(0);
    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                --time;
                if (time > 60)
                    setInfo(time - 60 + "초 뒤에 게임이 시작됩니다.");

                if (time < 60)
                    setTime(time);
                else if (time == 60)
                    startGame();

                if (time > 0)
                    handler.sendEmptyMessageDelayed(0, 1000);
            } else if (msg.what == 1){
                if (selectedCard1 == null)
                    selectedCard1 = cards[msg.arg1];
                else if (selectedCard2 == null) {
                    selectedCard2 = cards[msg.arg1];
                    compareTwoCards();
                }
            } else if (msg.what == 2) {
                selectedCard1.flipCard();
                selectedCard2.flipCard();
                setInfo("틀렸습니다.");
                setClickAble(true);
                resetSelectedCards();
            } else if (msg.what == 3) {
                Intent intent = new Intent();
                intent.putExtra("stage", stage);
                intent.putExtra("prescore", totalScore);
                setResult(RESULT_OK, intent);
                finish();
            }
            return false;
        }
    });

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
        stopTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed");
        //AlertDialog dialog = new AlertDialog(this, name, score, handler);
       // dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        handler.removeMessages(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Card card = (Card) v;
            if (card.isClickAble()) {
                card.clickedBackImage();
                if (card.isBackImageClicked()) {
                    Message msg = handler.obtainMessage(1);
                    msg.arg1 = (int) card.getTag();
                    handler.sendMessage(msg);
                } else {
                    selectedCard1 = null;
                }
            }
        }
    }
}
