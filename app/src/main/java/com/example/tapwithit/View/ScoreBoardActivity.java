package com.example.tapwithit.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapwithit.R;
import com.example.tapwithit.ScoreDatabase.Score;
import com.example.tapwithit.ScoreDatabase.ScoreDatabase;

import java.util.Comparator;
import java.util.List;

import lombok.NonNull;

public class ScoreBoardActivity extends AppCompatActivity {

    public final static String Login = "LOGIN_NORMAL";
    public static final int NORMAL_LOGIN = 1;

    private RecyclerView recyclerView;
    static ScoreDatabase scoreDatabase;

    Button clearScoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        scoreDatabase = Room.databaseBuilder(getApplicationContext(), ScoreDatabase.class, "score_db").allowMainThreadQueries().build();

        recyclerView = findViewById(R.id.score_recycler_view);
        final ScoreAdapter adapter = new ScoreAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreBoardActivity.this));

        scoreDatabase.scoreDao().findAll().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scores) {
                adapter.setScores(scores);
            }
        });

        clearScoreboard = findViewById(R.id.clear_scoreboard);
        clearScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ScoreBoardActivity.this, R.style.MyTheme));
                builder.setCancelable(true);
                builder.setTitle(getResources().getString(R.string.alert_title_sb_clear));
                builder.setMessage(getResources().getString(R.string.alert_message_sb_clear));
                builder.setPositiveButton(getResources().getString(R.string.alert_confirm_sb_clear),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scoreDatabase.scoreDao().deleteAll();
                                Toast.makeText(ScoreBoardActivity.this, getResources().getString(R.string.succ_all_score_del), Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setNegativeButton(getResources().getString(R.string.alert_cancel_sb_clear), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private class ScoreHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView usernameTextView, scoreTextView, modeTextView;
        Score score;

        public ScoreHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.board_item_score, parent, false));

            usernameTextView = itemView.findViewById(R.id.score_board_username);
            scoreTextView = itemView.findViewById(R.id.score_board_value);
            modeTextView = itemView.findViewById(R.id.score_board_mode);

            itemView.setOnLongClickListener(this);
        }

        public void bind(Score score) {
            this.score = score;

            usernameTextView.setText(score.getUsername());
            scoreTextView.setText(String.valueOf(score.getScore()));
            modeTextView.setText(score.getMode());
        }

        @Override
        public boolean onLongClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ScoreBoardActivity.this, R.style.MyTheme));
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.alert_title_s_clear));
            builder.setMessage(getResources().getString(R.string.alert_message_s_clear));
            builder.setPositiveButton(getResources().getString(R.string.alert_confirm_sb_clear),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scoreDatabase.scoreDao().delete(score);
                            Toast.makeText(ScoreBoardActivity.this, getResources().getString(R.string.succ_score_del), Toast.LENGTH_LONG).show();
                        }
                    });
            builder.setNegativeButton(getResources().getString(R.string.alert_cancel_sb_clear), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private List<Score> scores;

        @NonNull
        @Override
        public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ScoreHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
            if (scores != null) {
                scores.sort(Comparator.comparing(Score::getScore).reversed());
                Score score = scores.get(position);
                holder.bind(score);
            } else {
                Log.d("ScoreBoardActivity", "No Scores");
            }
        }

        @Override
        public int getItemCount() {
            if (scores != null) {
                return scores.size();
            } else {
                return 0;
            }
        }

        void setScores(List<Score> scores) {
            this.scores = scores;
            notifyDataSetChanged();
        }
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(ScoreBoardActivity.this, GameMenuActivity.class);
        intent.putExtra("UsernameForScoreboard", getIntent().getStringExtra("UsernameForScoreboard"));
        intent.putExtra(Login, NORMAL_LOGIN);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
