package com.example.tapwithit.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tapwithit.R;
import com.example.tapwithit.UserDatabase.User;
import com.example.tapwithit.UserDatabase.UserDatabase;
import com.example.tapwithit.UserDatabase.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText regUsrEditText;
    EditText regPassEditText;
    EditText regRepPassEditText;
    Button createAccountBtn;

    UserViewModel userViewModel;
    static UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDatabase = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "user_db").allowMainThreadQueries().build();

        regUsrEditText = findViewById(R.id.reg_usr_edit_text);
        regPassEditText = findViewById(R.id.reg_pass_edit_text);
        regRepPassEditText = findViewById(R.id.reg_rep_pass_edit_text);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.findAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

            }
        });

        createAccountBtn = findViewById(R.id.create_account);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = regUsrEditText.getText().toString();
                String pass = regPassEditText.getText().toString();
                String repPass = regRepPassEditText.getText().toString();

                boolean checkUser = userDatabase.userDao().checkUserInDatabase(usr);

                if(!checkUser) {
                    if(pass.length() >= 4 && usr.length() > 0) {
                        if(pass.equals(repPass)) {
                            User user = new User();

                            user.setUsername(usr);
                            user.setPassword(pass);

                            RegisterActivity.userDatabase.userDao().insert(user);

                            Toast.makeText(RegisterActivity.this, R.string.succ_create_acc, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginRegisterActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.wrong_rep_pass, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.wrong_pass_input, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.wrong_usr_input, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(RegisterActivity.this, LoginRegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
