package intervall_timer.frontend.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.R;
import intervall_timer.frontend.Service.Response.ServiceResponse;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterVH>
{
    private List<Exercise> exerciseList;
    private Context context;

    public ExerciseAdapter() {

    }

    public void setData(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ExerciseAdapter.ExerciseAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_exercise,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapterVH holder, int position) {
        Exercise exercise = exerciseList.get(position);
        String exerciseName = exercise.getName();
        holder.exerciseName.setText(exerciseName);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ExerciseAdapterVH extends RecyclerView.ViewHolder {
        TextView exerciseName;
        Button deleteExercise;
        Button editExercise;


        public ExerciseAdapterVH(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            deleteExercise = itemView.findViewById(R.id.deleteExercise);
            editExercise = itemView.findViewById(R.id.editExercise);

        }
    }
}
