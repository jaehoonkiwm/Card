package com.hoon.card;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 1000;
    private EditText etName;
    private String name;

    private Database db;

    public void OnButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.btnGameStart:
                name = etName.getText().toString();
                if (name.equals(""))
                    Toast.makeText(getBaseContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra("name", name);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.btnShowScore:
                ContentValues[] values = db.readDate();
                if (values != null) {
                    Toast.makeText(getBaseContext(), values.length + " " + values[0].getAsString(Database.ScoreTable.NAME) + " " +
                    values[0].getAsString(Database.ScoreTable.SCORE), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.editText);
        db = new Database(getBaseContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int stage = data.getIntExtra("stage", 0);
                int score = data.getIntExtra("prescore", 0);

                if (stage < 2) {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("stage", ++stage);
                    intent.putExtra("prescore", score);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(getBaseContext(), "점수 : " + score, Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(Database.ScoreTable.NAME, name);
                    values.put(Database.ScoreTable.SCORE, score);
                    db.insertData(values);
                }
            } else {
                Toast.makeText(getBaseContext(), "게임취소", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
