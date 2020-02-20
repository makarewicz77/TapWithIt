package com.example.tapwithit.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tapwithit.R;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_REGISTER_ACTIVITY_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button log_reg = findViewById(R.id.login_or_register_button);
        TextView basic_info = findViewById(R.id.basic_info_text_view);
        basic_info.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        log_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivityForResult(intent, LOGIN_REGISTER_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
