package intervall_timer.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import intervall_timer.frontend.Model.Exercise;

public class ExerciseDetailsActivity extends AppCompatActivity {

    // Constants
    private final String EXERCISEDETAILKEY = "DATA";
    private final String REPCOUNT = "Aktuelle Runde ";
    private final long COUNTDOWNINTERVAL = 1000;
    private final String PAUSE = "Pause";
    private final String START = "Start";

    // UI
    private TextView textViewName, textViewRepCount, textViewCountDown;
    private Exercise currentExercise;
    private Button buttonStartPause;
    private Button buttonReset;
    private RelativeLayout layout;


    // Logic
    private int currentRepetition = 1;
    private boolean isBreakMode = false;
    private boolean isPauseMode = false;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long lapTimeLeftInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        // TextViews
        textViewName = findViewById(R.id.textViewName);
        textViewRepCount = findViewById(R.id.textViewRepCount);
        textViewCountDown = findViewById(R.id.textViewCountDown);

        // Layout
        layout = findViewById(R.id.timer_layout);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            currentExercise = (Exercise) intent.getSerializableExtra(EXERCISEDETAILKEY);

            // Assign values
            // Exercise Name
            String exerciseName = currentExercise.getName();
            textViewName.setText(exerciseName);

            // Lap Time / Timer Display
            lapTimeLeftInMillis = convertSecToMillis(currentExercise.getLapTime());

            // Initial Value for Repetition Count
            textViewRepCount.setText(REPCOUNT + "1");
        }

        //Buttons
        buttonStartPause = findViewById(R.id.button_start_pause);
        buttonReset = findViewById(R.id.button_reset);

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning) {
                    pauseTimer();
                } else {
                    // Lap Settings
                    startTimer();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }

    private long convertSecToMillis(int lapTime) {
        return (lapTime * 1000);
    }

    private void startTimer() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        int timeToRun = 0;

        //Breakmode Handling
        if(isBreakMode) {
            layout.setBackgroundResource(R.drawable.gradient_background_red);
            timeToRun = currentExercise.getLapBreakTime();
        } else {
            layout.setBackgroundResource(R.drawable.gradient_background_green);
            timeToRun = currentExercise.getLapTime();
        }

        //Pausemode Handling
        if(isPauseMode) {
            // LapTimeLefInMillis needs no adjustment
            isPauseMode = false;
        } else {
            lapTimeLeftInMillis = convertSecToMillis(timeToRun);
        }

        countDownTimer = new CountDownTimer(lapTimeLeftInMillis, COUNTDOWNINTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                lapTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                // Repetition is only counting up after breaks
                currentRepetition = isBreakMode ? currentRepetition+1 : currentRepetition;

                // Adjusting the isBreakMode Flag
                isBreakMode = !isBreakMode;

                if(currentRepetition <= currentExercise.getRepCount()) {
                    // Adjusting the RepetitionCount Text
                    String repCount = REPCOUNT + Integer.toString(currentRepetition);
                    textViewRepCount.setText(repCount);
                    timerRunning = false;
                    startTimer();
                } else {
                    buttonStartPause.setText(START);
                    buttonStartPause.setVisibility(View.INVISIBLE);
                    buttonReset.setVisibility(View.VISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        }.start();
        timerRunning = true;
        buttonStartPause.setText(PAUSE);
        buttonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isPauseMode = true;
        layout.setBackgroundResource(R.drawable.gradient_background_yellow);
        countDownTimer.cancel();
        timerRunning = false;
        buttonStartPause.setText(START);
        buttonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        // Resetting Logic
        lapTimeLeftInMillis = convertSecToMillis(currentExercise.getLapTime());
        isBreakMode = false;
        isPauseMode = false;
        currentRepetition = 1;
        // Resetting UI
        updateCountDownText();
        buttonReset.setVisibility(View.INVISIBLE);
        buttonStartPause.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.drawable.gradient_background_green);
        textViewRepCount.setText(REPCOUNT + Integer.toString(currentRepetition));
    }

    private void updateCountDownText() {
        int minutes = (int) (lapTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (lapTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeLeftFormatted);
    }
}