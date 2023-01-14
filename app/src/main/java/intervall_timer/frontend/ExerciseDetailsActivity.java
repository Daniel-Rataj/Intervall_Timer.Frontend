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
    private TextView textViewName, textViewRepCount, textViewCountDown, textViewUserNotification;
    private Exercise currentExercise;
    private Button buttonStartPause;
    private Button buttonReset;
    private RelativeLayout layout;


    // Logic
    private int currentRepetition = 1;
    private boolean isBreakMode = false;
    private boolean isPauseMode = false;
    private boolean isStartCountdownFinished = false;

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
        textViewUserNotification = findViewById(R.id.userNotification);


        // Layout
        layout = findViewById(R.id.timer_layout);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            currentExercise = (Exercise) intent.getSerializableExtra(EXERCISEDETAILKEY);

            // Assign values
            // Exercise Name
            String exerciseName = currentExercise.getName();
            textViewName.setText(exerciseName);

            // Start Countdown / Timer Display
            lapTimeLeftInMillis = convertSecToMillis(currentExercise.getStartCountdown());

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

    /**
     * Recursive-Method to control the configurations of the timer
     * Consists 3 timer conditions
     * 1. Start-Countdown
     * 2. Exercise-Timer
     * 3. Break-Timer
     * Finishes after all repetitions are done
     * */
    private void startTimer() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // isStartCountdownFinished is by default false
        if(isStartCountdownFinished) {
            //Break Mode Handling
            lapTimeLeftInMillis = handleBreakMode();
        } else {
            // startCountdown was assigned to lapTimeLeftInMillis at the onCreate() Method
            // UI
            layout.setBackgroundResource(R.drawable.gradient_background_blue);
        }

        // Pause-Mode is only being toggled if currently in Pause-Mode
        if(isPauseMode) {
            togglePauseMode();
        }

        countDownTimer = new CountDownTimer(lapTimeLeftInMillis, COUNTDOWNINTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountDownText();
                lapTimeLeftInMillis = millisUntilFinished;
            }
            @Override
            public void onFinish() {
                if(isStartCountdownFinished) {
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
                } else {
                    textViewUserNotification.setVisibility(View.INVISIBLE);
                    isStartCountdownFinished = true;
                    startTimer();
                }
                updateCountDownText();
            }
        }.start();
        timerRunning = true;
        buttonStartPause.setText(PAUSE);
        buttonReset.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets the configurations for the Break-mode*/
    private long handleBreakMode() {
        long timeInMillis;
        if(isBreakMode) {
            layout.setBackgroundResource(R.drawable.gradient_background_red);
            timeInMillis = isPauseMode ? lapTimeLeftInMillis : convertSecToMillis(currentExercise.getLapBreakTime());
            textViewUserNotification.setText("PAUSE!");
            textViewUserNotification.setVisibility(View.VISIBLE);

        } else {
            layout.setBackgroundResource(R.drawable.gradient_background_green);
            textViewUserNotification.setVisibility(View.INVISIBLE);
            timeInMillis = isPauseMode ? lapTimeLeftInMillis : convertSecToMillis(currentExercise.getLapTime());

        }
        return timeInMillis;
    }

    /**
     * Toggles the Pause-Mode if Pause-Mode is active
     *
     * */
    private void togglePauseMode() {
        isPauseMode = (!isPauseMode);
    }

    /**
     * Sets the configuration for the Pause-mode
     * */
    private void pauseTimer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isPauseMode = true;
        layout.setBackgroundResource(R.drawable.gradient_background_yellow);
        countDownTimer.cancel();
        timerRunning = false;
        buttonStartPause.setText(START);
        buttonReset.setVisibility(View.VISIBLE);
    }

    /**
     * Resets the timer configurations
     * */
    private void resetTimer() {
        // Resetting Logic
        lapTimeLeftInMillis = convertSecToMillis(currentExercise.getStartCountdown());
        isBreakMode = false;
        isPauseMode = false;
        isStartCountdownFinished = false;
        currentRepetition = 1;
        // Resetting UI
        updateCountDownText();
        buttonReset.setVisibility(View.INVISIBLE);
        buttonStartPause.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.drawable.gradient_background_blue);
        textViewUserNotification.setText("Machen Sie sich bereit!");
        textViewUserNotification.setVisibility(View.VISIBLE);
        textViewRepCount.setText(REPCOUNT + Integer.toString(currentRepetition));
    }

    /**
     * Sets the textview text for the timer
     * */
    private void updateCountDownText() {
        int minutes = (int) (lapTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (lapTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeLeftFormatted);
    }
}