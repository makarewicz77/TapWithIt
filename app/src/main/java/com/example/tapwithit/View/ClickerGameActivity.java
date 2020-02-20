package com.example.tapwithit.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tapwithit.R;
import com.example.tapwithit.ScoreDatabase.Score;
import com.example.tapwithit.ScoreDatabase.ScoreDatabase;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;

public class ClickerGameActivity extends AppCompatActivity implements SensorEventListener{

    private static final String KEY_SCORE = "score";
    private static final String KEY_LIFES = "lifes";
    private static final String KEY_TIME_LEFT = "time_left";
    private static final String KEY_IS_IN_GAME = "isInGame";

    public final static String Login = "LOGIN_NORMAL";
    public static final int NORMAL_LOGIN = 1;

    private TextView clickerTimer, goodBadTap, score, lifesText, finalscore, pregameTimerTextView;
    private ImageView[] stars;
    private CountDownTimer countDownTimer, gameOverTimer, gameOverTimer2, pregameTimer;
    private Button playAgainBtn, exitBtn;
    private long timeLeftInMilisecounds, delay1s = 1200, gameOverDelay = 3000, timeLeft = 5500;
    private float scoreCounter = 0;
    private double scoreHandler;
    private boolean timerRunning, blink = false, modeSensor = false, isInGame = false;
    private int starToPress, starPressed = -1, blinkCounter = 0, lifes = 3;
    private LinearLayout clickerInGameLayout;

    String passingUsername, mode, sensorMode = "SensorMode", simpleMode = "SimpleMode";
    ScoreDatabase scoreDatabase;

    private SensorManager sensorManager;
    private Sensor sensorLight, sensorOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(getIntent().hasExtra("bundle") && savedInstanceState==null){
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker_game);

        passingUsername = getIntent().getStringExtra("UsernameForScoreboard");

        mode = getIntent().getStringExtra("Mode");
        if(mode.equals("SensorMode")) {
            modeSensor = true;
        } else if(mode.equals("SimpleMode")) {
            modeSensor = false;
        }

        scoreDatabase = Room.databaseBuilder(getApplicationContext(), ScoreDatabase.class, "score_db").allowMainThreadQueries().build();

        if(savedInstanceState != null) {
            scoreCounter = savedInstanceState.getFloat(KEY_SCORE);
            lifes = savedInstanceState.getInt(KEY_LIFES);
            timeLeft = savedInstanceState.getLong(KEY_TIME_LEFT);
            isInGame = savedInstanceState.getBoolean(KEY_IS_IN_GAME);
        }

        timeLeftInMilisecounds = timeLeft;

        stars = new ImageView[9];
        starToPress = (int)(Math.random() * 9);
        final boolean[] tap = {false};

        clickerTimer = findViewById(R.id.clicker_timer);
        goodBadTap = findViewById(R.id.good_bad_tap);
        score = findViewById(R.id.clicker_score);
        lifesText = findViewById(R.id.lifes_text_view);
        playAgainBtn = findViewById(R.id.play_again_clicker_button);
        exitBtn = findViewById(R.id.exit_clicker_button);
        finalscore = findViewById(R.id.final_score);
        clickerInGameLayout = findViewById(R.id.clicker_in_game);
        pregameTimerTextView = findViewById(R.id.pregameTimer);

        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);
        stars[5] = findViewById(R.id.star6);
        stars[6] = findViewById(R.id.star7);
        stars[7] = findViewById(R.id.star8);
        stars[8] = findViewById(R.id.star9);

        if(modeSensor == true) {

            for(int i=0; i<9; i++) {
                int x = i;
                if(i == 0 || i == 2 || i == 6 || i == 8) {
                    stars[i].setImageResource(R.drawable.ic_star_not_pressed);
                    stars[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(tap[0]) {
                                stars[x].setImageResource(R.drawable.ic_star_pressed);
                                tap[0] = false;
                            } else {
                                stars[x].setImageResource(R.drawable.ic_star_not_pressed);
                                tap[0] = true;
                            }
                            starPressed = x;
                            startStop();
                            Tapped(0);
                        }
                    });
                }
                else {
                    stars[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            starPressed = -1;
                            startStop();
                            Tapped(0);
                        }
                    });
                }
            }

            stars[1].setImageResource(R.drawable.ic_arrowup_not_pressed);
            stars[3].setImageResource(R.drawable.ic_arrowleft_not_pressed);
            stars[5].setImageResource(R.drawable.ic_arrowright_not_pressed);
            stars[7].setImageResource(R.drawable.ic_arrowdown_not_pressed);
            stars[4].setImageResource(R.drawable.ic_lightbulb_not_pressed);

        } else {

            for(int i=0; i<9; i++) {
                int x = i;
                stars[i].setImageResource(R.drawable.ic_star_not_pressed);
                stars[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(tap[0]) {
                            stars[x].setImageResource(R.drawable.ic_star_pressed);
                            tap[0] = false;
                        } else {
                            stars[x].setImageResource(R.drawable.ic_star_not_pressed);
                            tap[0] = true;
                        }
                        starPressed = x;
                        startStop();
                        Tapped(0);
                    }
                });
            }

        }

        if(modeSensor == true) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorOrientation = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
            sensorLight = sensorManager.getDefaultSensor(TYPE_LIGHT);
        }

        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClickerGameActivity.this, PregameActivity.class);
                intent.putExtra("UsernameForScoreboard", passingUsername);
                if(modeSensor == true) {
                    intent.putExtra("Mode", sensorMode);
                } else {
                    intent.putExtra("Mode", simpleMode);
                }
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClickerGameActivity.this, GameMenuActivity.class);
                intent.putExtra("UsernameForScoreboard", passingUsername);
                intent.putExtra(Login, NORMAL_LOGIN);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        playAgainBtn.setVisibility(View.GONE);
        exitBtn.setVisibility(View.GONE);
        finalscore.setVisibility(View.GONE);

        if(isInGame) {
            randomToTap();
            updateTimer();
            startStop();
        } else
            pregameView();
    }

    public void startStop() {
        if(timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilisecounds, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilisecounds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                starPressed = -1;
                Tapped(1);
            }
        }.start();

        timerRunning = true;
    }

    public void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    public void updateTimer() {
        int sec = (int) timeLeftInMilisecounds / 1000;
        int tsec = (int) timeLeftInMilisecounds / 100 % 10;

        String timeLeftText;

        timeLeftText = "" + sec + "," + tsec + "s";

        clickerTimer.setText(timeLeftText);
    }

    public void randomToTap() {

        if (modeSensor == true){
            if(starToPress == 1)
                stars[starToPress].setImageResource(R.drawable.ic_arrowup_pressed);
            else if(starToPress == 3)
                stars[starToPress].setImageResource(R.drawable.ic_arrowleft_pressed);
            else if(starToPress == 4)
                stars[starToPress].setImageResource(R.drawable.ic_lightbulb_pressed);
            else if(starToPress == 5)
                stars[starToPress].setImageResource(R.drawable.ic_arrowright_pressed);
            else if(starToPress == 7)
                stars[starToPress].setImageResource(R.drawable.ic_arrowdown_pressed);
            else
                stars[starToPress].setImageResource(R.drawable.ic_star_pressed);
        } else {
            stars[starToPress].setImageResource(R.drawable.ic_star_pressed);
        }

        score.setText(getResources().getString(R.string.clicker_score) + scoreCounter);
        String lf = "" + lifes;
        lifesText.setText(lf);
    }

    public void Tapped(int state) {

        for(int i=0; i<9; i++)
            stars[i].setOnClickListener(null);

        if (modeSensor == true)
            sensorManager.unregisterListener(this);

        if(starToPress == starPressed) {

            if (modeSensor == true){
                if(starPressed == 1)
                    stars[starPressed].setImageResource(R.drawable.ic_arrowup_not_pressed);
                else if(starPressed == 3)
                    stars[starPressed].setImageResource(R.drawable.ic_arrowleft_not_pressed);
                else if(starPressed == 4)
                    stars[starPressed].setImageResource(R.drawable.ic_lightbulb_not_pressed);
                else if(starPressed == 5)
                    stars[starPressed].setImageResource(R.drawable.ic_arrowright_not_pressed);
                else if(starPressed == 7)
                    stars[starPressed].setImageResource(R.drawable.ic_arrowdown_not_pressed);
                else
                    stars[starPressed].setImageResource(R.drawable.ic_star_not_pressed);
            } else {
                stars[starPressed].setImageResource(R.drawable.ic_star_not_pressed);
            }

            goodBadTap.setText(R.string.bravo);

            // system punktow dodajacy pozostaly czas
//            scoreCounter += timeLeftInMilisecounds / 1000 + (double)(timeLeftInMilisecounds / 100 % 10)/10;

            // system punktow dodajacy punkty wyliczone z proporcji
            scoreCounter += (double)(10.0 * (timeLeftInMilisecounds / 1000.0 + (timeLeftInMilisecounds / 100.0 % 10.0)/10.0) / (timeLeft / 1000.0));
            scoreHandler = Math.floor(scoreCounter * 10.0) / 10.0;
            scoreCounter = (float)scoreHandler;
            delayRestart(0);
        }
        else {
            if(state != 0)
                goodBadTap.setText(R.string.out_of_time);
            else
                goodBadTap.setText(R.string.you_missed);

            lifes--;
            String lf;
            if(lifes <= 0)
                lf = "0";
            else
                lf = "" + lifes;
            lifesText.setText(lf);
            if(lifes >= 0)
                delayRestart(1);
            else
                gameOver();
        }

    }
    
    public void delayRestart(int lose) {

        score.setText(getResources().getString(R.string.clicker_score) + scoreCounter);

        countDownTimer = new CountDownTimer(delay1s, 150) {

            @Override
            public void onTick(long millisUntilFinished) {
                delay1s = millisUntilFinished;
                if(lose == 1 && blinkCounter <= 6) {
                    if(blink) {
                        for(int i=0; i<9; i++) {
                            if (modeSensor == true){
                                if(i == 1)
                                    stars[i].setImageResource(R.drawable.ic_arrowup_pressed);
                                else if(i == 3)
                                    stars[i].setImageResource(R.drawable.ic_arrowleft_pressed);
                                else if(i == 4)
                                    stars[i].setImageResource(R.drawable.ic_lightbulb_pressed);
                                else if(i == 5)
                                    stars[i].setImageResource(R.drawable.ic_arrowright_pressed);
                                else if(i == 7)
                                    stars[i].setImageResource(R.drawable.ic_arrowdown_pressed);
                                else
                                    stars[i].setImageResource(R.drawable.ic_star_pressed);
                            } else {
                                stars[i].setImageResource(R.drawable.ic_star_pressed);
                            }
                        }

                        blink = false;
                        blinkCounter++;
                    }
                    else {
                        for(int i=0; i<9; i++) {
                            if (modeSensor == true){
                                if(i == 1)
                                    stars[i].setImageResource(R.drawable.ic_arrowup_not_pressed);
                                else if(i == 3)
                                    stars[i].setImageResource(R.drawable.ic_arrowleft_not_pressed);
                                else if(i == 4)
                                    stars[i].setImageResource(R.drawable.ic_lightbulb_not_pressed);
                                else if(i == 5)
                                    stars[i].setImageResource(R.drawable.ic_arrowright_not_pressed);
                                else if(i == 7)
                                    stars[i].setImageResource(R.drawable.ic_arrowdown_not_pressed);
                                else
                                    stars[i].setImageResource(R.drawable.ic_star_not_pressed);
                            } else {
                                stars[i].setImageResource(R.drawable.ic_star_not_pressed);
                            }
                        }
                        blink = true;
                        blinkCounter++;
                    }
                }
            }

            @Override
            public void onFinish() {
                goodBadTap.setText("");

                // mnożnik zmiejszania czasu
                if(timeLeft >= 4500 && timeLeft <= 5500)
                    timeLeft *= 0.95;
                else if(timeLeft >= 3500 && timeLeft < 4500)
                    timeLeft *= 0.96;
                else if(timeLeft >= 2500 && timeLeft < 3500)
                    timeLeft *= 0.97;
                else if(timeLeft >= 1500 && timeLeft < 2500)
                    timeLeft *= 0.98;
                else
                    timeLeft *= 0.99;

                //  restart gry i przekazanie punktów
                Bundle temp_bundle = new Bundle();
                if(temp_bundle != null)
                    onSaveInstanceState(temp_bundle);
                Intent intent = new Intent(getIntent());
                intent.putExtra("bundle", temp_bundle);
                startActivity(intent);
                overridePendingTransition( 0, 0);
                finish();
                overridePendingTransition( 0, 0);
            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(KEY_SCORE, scoreCounter);
        outState.putInt(KEY_LIFES, lifes);
        outState.putLong(KEY_TIME_LEFT, timeLeft);
        outState.putBoolean(KEY_IS_IN_GAME, isInGame);
    }

    public void gameOver() {

        gameOverTimer = new CountDownTimer(delay1s, 150) {

            @Override
            public void onTick(long millisUntilFinished) {
                delay1s = millisUntilFinished;
                if(blinkCounter <= 6) {
                    if(blink) {
                        for(int i=0; i<9; i++) {
                            if (modeSensor == true){
                                if(i == 1)
                                    stars[i].setImageResource(R.drawable.ic_arrowup_pressed);
                                else if(i == 3)
                                    stars[i].setImageResource(R.drawable.ic_arrowleft_pressed);
                                else if(i == 4)
                                    stars[i].setImageResource(R.drawable.ic_lightbulb_pressed);
                                else if(i == 5)
                                    stars[i].setImageResource(R.drawable.ic_arrowright_pressed);
                                else if(i == 7)
                                    stars[i].setImageResource(R.drawable.ic_arrowdown_pressed);
                                else
                                    stars[i].setImageResource(R.drawable.ic_star_pressed);
                            } else {
                                stars[i].setImageResource(R.drawable.ic_star_pressed);
                            }
                        }
                        blink = false;
                        blinkCounter++;
                    }
                    else {
                        for(int i=0; i<9; i++) {
                            if (modeSensor == true){
                                if(i == 1)
                                    stars[i].setImageResource(R.drawable.ic_arrowup_not_pressed);
                                else if(i == 3)
                                    stars[i].setImageResource(R.drawable.ic_arrowleft_not_pressed);
                                else if(i == 4)
                                    stars[i].setImageResource(R.drawable.ic_lightbulb_not_pressed);
                                else if(i == 5)
                                    stars[i].setImageResource(R.drawable.ic_arrowright_not_pressed);
                                else if(i == 7)
                                    stars[i].setImageResource(R.drawable.ic_arrowdown_not_pressed);
                                else
                                    stars[i].setImageResource(R.drawable.ic_star_not_pressed);
                            } else {
                                stars[i].setImageResource(R.drawable.ic_star_not_pressed);
                            }
                        }
                        blink = true;
                        blinkCounter++;
                    }
                }
            }

            @Override
            public void onFinish() {
                goodBadTap.setText(getResources().getString(R.string.game_over));
                gameOverTimer.cancel();
            }
        }.start();


        gameOverTimer2 = new CountDownTimer(gameOverDelay, 200) {

            @Override
            public void onTick(long millisUntilFinished) {
                gameOverDelay = millisUntilFinished;
            }

            @Override
            public void onFinish() {

                Score score = new Score();
                score.setScore(scoreCounter);
                score.setUsername(passingUsername);
                if(modeSensor == true)
                    score.setMode(getResources().getString(R.string.game_menu_sensor_clicker));
                else
                    score.setMode(getResources().getString(R.string.game_menu_clicker));

                scoreDatabase.scoreDao().insert(score);

                AlphaAnimation alpha = new AlphaAnimation(0F, 0F);
                alpha.setDuration(0);
                alpha.setFillAfter(true);
                clickerInGameLayout.startAnimation(alpha);

                finalscore.setText(getResources().getString(R.string.final_score) + scoreCounter);

                playAgainBtn.setVisibility(View.VISIBLE);
                exitBtn.setVisibility(View.VISIBLE);
                finalscore.setVisibility(View.VISIBLE);

                gameOverTimer2.cancel();
            }
        }.start();
    }

    public void pregameView() {

        AlphaAnimation alpha = new AlphaAnimation(0F, 0F);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        clickerInGameLayout.startAnimation(alpha);

        pregameTimer = new CountDownTimer(gameOverDelay, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                gameOverDelay = millisUntilFinished;

                int sec = (int) gameOverDelay / 1000 + 1;

                String timeLeftText;

                timeLeftText = "" + sec;

                pregameTimerTextView.setText(timeLeftText);
            }

            @Override
            public void onFinish() {

                isInGame = true;

                gameOverDelay = 3000;
                pregameTimer.cancel();

                clickerInGameLayout.clearAnimation();
                pregameTimerTextView.setVisibility(View.GONE);

                randomToTap();
                updateTimer();
                startStop();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (modeSensor == true) {
            float accVal[], lightVal[];

            if (event.sensor.getType() == TYPE_ACCELEROMETER) {
                accVal = event.values;
                if((starToPress == 3 && accVal[0] > 5)){
                    starPressed = 3;
                    startStop();
                    Tapped(0);
                    sensorManager.unregisterListener(this);
                }
                if((starToPress == 5 && accVal[0] < -5)) {
                    starPressed = 5;
                    startStop();
                    Tapped(0);
                    sensorManager.unregisterListener(this);
                }
                if (starToPress == 1 && accVal[2] > 9 && accVal[1] > -2 && accVal[1] < 2) {
                    starPressed = 1;
                    startStop();
                    Tapped(0);
                    sensorManager.unregisterListener(this);
                }
                if (starToPress == 7 && accVal[2] < -4) {
                    starPressed = 7;
                    startStop();
                    Tapped(0);
                    sensorManager.unregisterListener(this);
                }
            }
            if (event.sensor.getType() == TYPE_LIGHT) {
                lightVal = event.values;
                if (starToPress == 4 && lightVal[0] == 0) {
                    starPressed = 4;
                    startStop();
                    Tapped(0);
                    sensorManager.unregisterListener(this);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sensorLight != null && modeSensor == true) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(sensorOrientation != null && modeSensor == true) {
            sensorManager.registerListener(this, sensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
