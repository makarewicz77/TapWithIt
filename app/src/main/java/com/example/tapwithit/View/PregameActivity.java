package com.example.tapwithit.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tapwithit.R;

import static com.example.tapwithit.View.GameMenuActivity.CLICKER_GAME_ACTIVITY_REQUEST_CODE;
import static com.example.tapwithit.View.GameMenuActivity.SENSOR_CLICKER_GAME_ACTIVITY_REQUEST_CODE;

public class PregameActivity extends AppCompatActivity {

    public final static String Login = "LOGIN_NORMAL";
    public static final int NORMAL_LOGIN = 1;

    private String mode, sensorMode = "SensorMode", simpleMode = "SimpleMode";;
    private Button startClicker;
    private TextView preInstruct;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregame_layout);

        preInstruct = findViewById(R.id.pregame_instruction);

        if(getIntent().hasExtra("Mode")) {
            mode = getIntent().getStringExtra("Mode");
            if(mode.equals("SensorMode")) {
                preInstruct.setText(getResources().getString(R.string.pregame_sensor_clicker));
            } else if(mode.equals("SimpleMode")) {
                preInstruct.setText(getResources().getString(R.string.pregame_clicker));
            }
        } else {
            preInstruct.setText("Error");
        }

        preInstruct.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        startClicker = findViewById(R.id.start_clicker);
        startClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("SensorMode")) {
                    Intent intent = new Intent(PregameActivity.this, ClickerGameActivity.class);
                    intent.putExtra("UsernameForScoreboard", getIntent().getStringExtra("UsernameForScoreboard"));
                    intent.putExtra("Mode", sensorMode);
                    startActivityForResult(intent, SENSOR_CLICKER_GAME_ACTIVITY_REQUEST_CODE);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if(mode.equals("SimpleMode")) {
                    Intent intent = new Intent(PregameActivity.this, ClickerGameActivity.class);
                    intent.putExtra("UsernameForScoreboard", getIntent().getStringExtra("UsernameForScoreboard"));
                    intent.putExtra("Mode", simpleMode);
                    startActivityForResult(intent, CLICKER_GAME_ACTIVITY_REQUEST_CODE);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(PregameActivity.this, GameMenuActivity.class);
        intent.putExtra("UsernameForScoreboard", getIntent().getStringExtra("UsernameForScoreboard"));
        intent.putExtra(Login, NORMAL_LOGIN);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
