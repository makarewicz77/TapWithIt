package com.example.tapwithit.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapwithit.R;
import com.example.tapwithit.UserDatabase.UserDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class LoginRegisterActivity extends AppCompatActivity {

    UserDatabase userDatabase;
    GoogleSignInClient mGoogleSignInClient;

    public final static String Login = "LOGIN_NORMAL";
    public static final int NORMAL_LOGIN = 1;
    public static final int GOOGLE_SIGN_IN = 2;
    private final static String TAG = "LoginActivityGoogle";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        Button youtubeButton = findViewById(R.id.youtube_button);

        EditText usernameEditText = findViewById(R.id.username_edit_text);
        EditText passwordEditText = findViewById(R.id.password_edit_text);

        TextView googleInfo = findViewById(R.id.google_sign_in_info);
        googleInfo.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        userDatabase = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "user_db").allowMainThreadQueries().build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usr = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                int check = userDatabase.userDao().checkUser(usr, pass);

                if(check == 1) {
                    Toast.makeText(LoginRegisterActivity.this, R.string.succ_log_in, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginRegisterActivity.this, GameMenuActivity.class);
                    intent.putExtra("UsernameForScoreboard", usr);
                    intent.putExtra(Login, NORMAL_LOGIN);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else {
                    Toast.makeText(LoginRegisterActivity.this, R.string.wrong_usr_or_pass, Toast.LENGTH_LONG).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        findViewById(R.id.sign_in_google_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_google_button:
                        signIn();
                        break;
                }
            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube/watch?v=t-yZUqthDMM&feature=youtu.be&fbclid=IwAR167qXCXmETxkP1Q_ZvDKN02YfjSqmwodhe-ZvRdlKQxJ3MqXcR-1NwLIg"));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=t-yZUqthDMM&feature=youtu.be&fbclid=IwAR167qXCXmETxkP1Q_ZvDKN02YfjSqmwodhe-ZvRdlKQxJ3MqXcR-1NwLIg"));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInIntent.putExtra(Login, GOOGLE_SIGN_IN);
        startActivityForResult(signInIntent, NORMAL_LOGIN);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void  updateUI(GoogleSignInAccount account){
        if(account != null){
            Toast.makeText(this,R.string.succ_log_in, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, GameMenuActivity.class).putExtra(Login, GOOGLE_SIGN_IN));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else {
            Toast.makeText(this,R.string.fail_log_in, Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NORMAL_LOGIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
