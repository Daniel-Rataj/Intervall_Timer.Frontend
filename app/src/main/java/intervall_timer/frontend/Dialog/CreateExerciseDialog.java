package intervall_timer.frontend.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.R;

public class CreateExerciseDialog extends AppCompatDialogFragment {
    private EditText editTextNewExerciseName;
    private EditText editTextNewExerciseRepCount;
    private EditText editTextNewExerciseLapTime;
    private EditText editTextNewExerciseLapBreakTime;
    private EditText editTextNewExerciseStartCountdown;
    private CreateExerciseDialogListener listener;

    private String dialogTitle;

    //EditMode
    private boolean isEditMode = false;
    private Exercise currentExercise;

    public CreateExerciseDialog() {}

    public CreateExerciseDialog(Exercise exercise) {
        currentExercise = exercise;
        isEditMode = true;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        // EditText
        editTextNewExerciseName = view.findViewById(R.id.newExerciseName);
        editTextNewExerciseRepCount = view.findViewById(R.id.newExerciseRepCount);
        editTextNewExerciseLapTime = view.findViewById(R.id.newExerciseLapTime);
        editTextNewExerciseLapBreakTime = view.findViewById(R.id.newExerciseLapBreakTime);
        editTextNewExerciseStartCountdown = view.findViewById(R.id.newExerciseStartCountdown);

        // Setting configurations after isEditMode
        DialogInterface.OnClickListener onSaveDialogClickListener;
        if(isEditMode) {
            FillTextViews();
            onSaveDialogClickListener = createEditListener();
        } else {
            onSaveDialogClickListener = createSaveListener();
        }

        builder.setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("abbrechen", (dialog, which) -> dialog.cancel())
                .setPositiveButton("speichern", onSaveDialogClickListener);


        return builder.create();
    }

    private void FillTextViews() {
        if(currentExercise != null) {
            editTextNewExerciseName.setText(currentExercise.getName());
            editTextNewExerciseRepCount.setText(Integer.toString(currentExercise.getRepCount()));
            editTextNewExerciseLapTime.setText(Integer.toString(currentExercise.getLapTime()));
            editTextNewExerciseLapBreakTime.setText(Integer.toString(currentExercise.getLapBreakTime()));
            editTextNewExerciseStartCountdown.setText(Integer.toString(currentExercise.getStartCountdown()));
        }
    }

    private DialogInterface.OnClickListener createEditListener() {
        dialogTitle = "Übung bearbeiten";
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextNewExerciseName.getText().toString();
                currentExercise.setName(name);

                String repCountString = editTextNewExerciseRepCount.getText().toString();
                int repCount = Integer.parseInt(repCountString);
                currentExercise.setRepCount(repCount);

                String lapTimeString = editTextNewExerciseLapTime.getText().toString();
                int lapTime = Integer.parseInt(lapTimeString);
                currentExercise.setLapTime(lapTime);

                String lapBreakTimeString = editTextNewExerciseLapBreakTime.getText().toString();
                int lapBreakTime = Integer.parseInt(lapBreakTimeString);
                currentExercise.setLapBreakTime(lapBreakTime);

                String startCountdownString = editTextNewExerciseStartCountdown.getText().toString();
                int startCountdown = Integer.parseInt(startCountdownString);
                currentExercise.setStartCountdown(startCountdown);

                listener.editExercise(currentExercise);
            }};
    }

    private DialogInterface.OnClickListener createSaveListener() {
        dialogTitle = "Neue Übung erstellen";
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextNewExerciseName.getText().toString();
                String repCountString = editTextNewExerciseRepCount.getText().toString();
                int repCount = Integer.parseInt(repCountString);
                String lapTimeString = editTextNewExerciseLapTime.getText().toString();
                int lapTime = Integer.parseInt(lapTimeString);
                String lapBreakTimeString = editTextNewExerciseLapBreakTime.getText().toString();
                int lapBreakTime = Integer.parseInt(lapBreakTimeString);
                String startCountdownString = editTextNewExerciseStartCountdown.getText().toString();
                int startCountdown = Integer.parseInt(startCountdownString);
                listener.saveExercise(name, repCount, lapTime, lapBreakTime, startCountdown);
            }};
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
