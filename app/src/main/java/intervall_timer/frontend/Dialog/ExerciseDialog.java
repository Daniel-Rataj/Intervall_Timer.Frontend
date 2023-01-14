package intervall_timer.frontend.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.R;

public class ExerciseDialog extends AppCompatDialogFragment {
    private TextInputLayout textInputNewExerciseName;
    private TextInputLayout textInputNewExerciseRepCount;
    private TextInputLayout textInputNewExerciseLapTime;
    private TextInputLayout textInputNewExerciseLapBreakTime;
    private TextInputLayout textInputNewExerciseStartCountdown;

    private CreateExerciseDialogListener listener;

    private String dialogTitle;

    //EditMode
    private boolean isEditMode = false;
    private Exercise currentExercise;

    public ExerciseDialog() {}

    public ExerciseDialog(Exercise exercise) {
        currentExercise = exercise;
        isEditMode = true;
    }


    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        // EditText
        textInputNewExerciseName = view.findViewById(R.id.newExerciseName);
        textInputNewExerciseRepCount = view.findViewById(R.id.newExerciseRepCount);
        textInputNewExerciseLapTime = view.findViewById(R.id.newExerciseLapTime);
        textInputNewExerciseLapBreakTime = view.findViewById(R.id.newExerciseLapBreakTime);
        textInputNewExerciseStartCountdown = view.findViewById(R.id.newExerciseStartCountdown);

        // Setting configurations after isEditMode
        if(isEditMode) {
            FillTextViews();
        }

        builder.setView(view)
                .setTitle(dialogTitle);

        return builder.create();
    }

    private void saveExerciseInCreationMode() {
        dialogTitle = "Neue Übung erstellen";

        String name = textInputNewExerciseName.getEditText().getText().toString();

        String repCountString = textInputNewExerciseRepCount.getEditText().getText().toString();
        int repCount = Integer.parseInt(repCountString);

        String lapTimeString = textInputNewExerciseLapTime.getEditText().getText().toString();
        int lapTime = Integer.parseInt(lapTimeString);

        String lapBreakTimeString = textInputNewExerciseLapBreakTime.getEditText().getText().toString();
        int lapBreakTime = Integer.parseInt(lapBreakTimeString);

        String startCountdownString = textInputNewExerciseStartCountdown.getEditText().getText().toString();
        int startCountdown = Integer.parseInt(startCountdownString);

        listener.saveExercise(name, repCount, lapTime, lapBreakTime, startCountdown);
    }

    private void saveExerciseInEditMode() {
        dialogTitle = "Übung bearbeiten";

        String name = textInputNewExerciseName.getEditText().getText().toString();
        currentExercise.setName(name);

        String repCountString = textInputNewExerciseRepCount.getEditText().getText().toString();
        int repCount = Integer.parseInt(repCountString);
        currentExercise.setRepCount(repCount);

        String lapTimeString = textInputNewExerciseLapTime.getEditText().getText().toString();
        int lapTime = Integer.parseInt(lapTimeString);
        currentExercise.setLapTime(lapTime);

        String lapBreakTimeString = textInputNewExerciseLapBreakTime.getEditText().getText().toString();
        int lapBreakTime = Integer.parseInt(lapBreakTimeString);
        currentExercise.setLapBreakTime(lapBreakTime);

        String startCountdownString = textInputNewExerciseStartCountdown.getEditText().getText().toString();
        int startCountdown = Integer.parseInt(startCountdownString);
        currentExercise.setStartCountdown(startCountdown);

        listener.editExercise(currentExercise);
    }

    private void FillTextViews() {
        if(currentExercise != null) {
            textInputNewExerciseName.getEditText().setText(currentExercise.getName());
            textInputNewExerciseRepCount.getEditText().setText(Integer.toString(currentExercise.getRepCount()));
            textInputNewExerciseLapTime.getEditText().setText(Integer.toString(currentExercise.getLapTime()));
            textInputNewExerciseLapBreakTime.getEditText().setText(Integer.toString(currentExercise.getLapBreakTime()));
            textInputNewExerciseStartCountdown.getEditText().setText(Integer.toString(currentExercise.getStartCountdown()));
        }
    }

    private boolean validateInputs() {
        boolean result = false;
        result = (!validateName()
                | !validateRepCount()
                | !validateLapTime()
                | !validateLapBreakTime()
                | !validateStartCountdown());
        return !result;
    }

    private boolean validateName() {
        String name = textInputNewExerciseName.getEditText().getText().toString().trim();

        if(name.isEmpty()) {
            textInputNewExerciseName.setError("Feld darf nicht leer sein");
            return false;
        } else {
            textInputNewExerciseName.setError(null);
            return true;
        }
    }

    private boolean validateRepCount() {
        String repCount = textInputNewExerciseRepCount.getEditText().getText().toString().trim();

        if(repCount.isEmpty()) {
            textInputNewExerciseRepCount.setError("Feld darf nicht leer sein");
            return false;
        } else if(Integer.parseInt(repCount) > 100) {
            textInputNewExerciseRepCount.setError("Bruder übertreib nicht!");
            return false;
        } else {
            textInputNewExerciseRepCount.setError(null);
            return true;
        }
    }

    private boolean validateLapTime() {
        String lapTime = textInputNewExerciseLapTime.getEditText().getText().toString().trim();

        if(lapTime.isEmpty()) {
            textInputNewExerciseLapTime.setError("Feld darf nicht leer sein");
            return false;
        } else if(Integer.parseInt(lapTime) > 3600) {
            textInputNewExerciseLapTime.setError("Eine Stunde reicht doch digga!");
            return false;
        } else {
            textInputNewExerciseLapTime.setError(null);
            return true;
        }
    }

    private boolean validateLapBreakTime() {
        String lapBreakTime = textInputNewExerciseLapBreakTime.getEditText().getText().toString().trim();

        if(lapBreakTime.isEmpty()) {
            textInputNewExerciseLapBreakTime.setError("Feld darf nicht leer sein");
            return false;
        } else if(Integer.parseInt(lapBreakTime) > 3600) {
            textInputNewExerciseLapBreakTime.setError("Eine Stunde reicht doch digga!");
            return false;
        } else {
            textInputNewExerciseLapBreakTime.setError(null);
            return true;
        }
    }

    private boolean validateStartCountdown() {
        String lapTime = textInputNewExerciseStartCountdown.getEditText().getText().toString().trim();

        if(lapTime.isEmpty()) {
            textInputNewExerciseStartCountdown.setError("Feld darf nicht leer sein");
            return false;
        } else if(Integer.parseInt(lapTime) > 10) {
            textInputNewExerciseStartCountdown.setError("Willst du überhaupt trainieren?");
            return false;
        } else {
            textInputNewExerciseStartCountdown.setError(null);
            return true;
        }
    }

    public void cancel() {
        getDialog().cancel();
    }

    public void save() {
        if(validateInputs()) {
            if (isEditMode) {
                saveExerciseInEditMode();

            } else {
                saveExerciseInCreationMode();
            }
            getDialog().dismiss();
            return;
        } else {
            Toast.makeText(getContext(), "Überprüfen Sie die eingegebenen Daten", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CreateExerciseDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CreateExerciseDialogListener");
        }
    }

    public interface CreateExerciseDialogListener {
        void saveExercise(String name, int repCount, int lapTime, int lapBreakTime, int startCountdown);
        void editExercise(Exercise exercise);
    }
}
