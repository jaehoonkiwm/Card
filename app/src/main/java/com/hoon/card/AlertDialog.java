package com.hoon.card;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Hoon on 2015-06-30.
 */
public class AlertDialog extends Dialog {

    String name;
    int stage, score, total;
    Handler handler;

    public AlertDialog(Context context, String name, int stage,int score, int totalScore,Handler handler) {
        super(context);
        this.name = name;
        this.stage = stage;
        this.score = score;
        this.total = totalScore;
        this.handler = handler;
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    private String nextMessage() {
        return name + "님\n"
                + stage + "단계를 " + score + "점으로 클리어하셨습니다.\n"
                + "총 점수는 " + total +"입니다.\n";
    }

    private String clearMessage() {
       // return name + "님 축하합니다!" +
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alert);

        TextView tvDialog = (TextView) findViewById(R.id.tvDialog);
        tvDialog.setText(nextMessage());

        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(3);
                dismiss();
            }
        });
    }
}
