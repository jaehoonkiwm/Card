package com.hoon.card;

import android.app.Dialog;
import android.content.Context;
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
    int score;
    Handler handler;

    public AlertDialog(Context context, int score, Handler handler) {
        super(context);
        this.score = score;
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alert);

        TextView tvDialog = (TextView) findViewById(R.id.tvDialog);
        tvDialog.setText("님 " + "단계를 " + score + "점으로 클리어하셨습니다.\n");
        tvDialog.append("총 점수는 입니다.\n");
        tvDialog.append("다음 단계로 넘어갑니다.\n");

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
