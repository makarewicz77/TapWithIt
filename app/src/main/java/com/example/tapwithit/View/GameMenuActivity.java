package com.example.tapwithit.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tapwithit.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import lombok.NonNull;

import static com.example.tapwithit.View.LoginRegisterActivity.Login;

public class GameMenuActivity extends AppCompatActivity {

    public static final int CLICKER_GAME_ACTIVITY_REQUEST_CODE = 1;
    public static final int SENSOR_CLICKER_GAME_ACTIVITY_REQUEST_CODE = 2;
    String passUsr, sensorMode = "SensorMode", simpleMode = "SimpleMode";
    GoogleSignInClient mGoogleSignInClient;
    boolean gSignIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Bundle bundle = getIntent().getExtras();

        Button clickerButton = findViewById(R.id.game_menu_clicker);
        Button sensorClickerButton = findViewById(R.id.game_menu_sensor_clicker);
        Button scoreboardButton = findViewById(R.id.game_menu_scoreboard);
        Button logoutButton = findViewById(R.id.game_menu_logout);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if((int)bundle.get(Login) == 1){
            passUsr = bundle.get("UsernameForScoreboard").toString();
        } else if((int)bundle.get(Login) == 2) {
            if (acct != null) {
                passUsr = acct.getDisplayName();
                gSignIn = true;
            }
        } else {
            passUsr = "tempUser";
        }

        clickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMenuActivity.this, PregameActivity.class);
                intent.putExtra("UsernameForScoreboard", passUsr);
                intent.putExtra("Mode", simpleMode);
                startActivityForResult(intent, CLICKER_GAME_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sensorClickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMenuActivity.this, PregameActivity.class);
                intent.putExtra("UsernameForScoreboard", passUsr);
                intent.putExtra("Mode", sensorMode);
                startActivityForResult(intent, SENSOR_CLICKER_GAME_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMenuActivity.this, ScoreBoardActivity.class);
                intent.putExtra("UsernameForScoreboard", passUsr);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gSignIn == true){
                    signOut();
                } else {
                    finish();
                }

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Toast.makeText(GameMenuActivity.this, R.string.succ_log_out, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(GameMenuActivity.this, LoginRegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        gSignIn = false;
                        finish();
                    }
                });
    }
}
