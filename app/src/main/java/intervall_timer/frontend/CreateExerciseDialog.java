package intervall_timer.frontend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateExerciseDialog extends AppCompatDialogFragment {
    private EditText editTextNewExerciseName;
    private EditText editTextNewExerciseRepCount;
    private EditText editTextNewExerciseLapTime;
    private EditText editTextNewExerciseLapBreakTime;
    private EditText editTextNewExerciseStartCountdown;
    private CreateExerciseDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Neue Übung erstellen")
                .setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("speichern", new DialogInterface.OnClickListener() {
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

                    }
                });
        editTextNewExerciseName = view.findViewById(R.id.newExerciseName);
        editTextNewExerciseRepCount = view.findViewById(R.id.newExerciseRepCount);
        editTextNewExerciseLapTime = view.findViewById(R.id.newExerciseLapTime);
        editTextNewExerciseLapBreakTime = view.findViewById(R.id.newExerciseLapBreakTime);
        editTextNewExerciseStartCountdown = view.findViewById(R.id.newExerciseStartCountdown);
        return builder.create();
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
        void saveExercise(String name, int repCount, int lapTime, int lapBreaktime, int startCountdown);
    }
}
