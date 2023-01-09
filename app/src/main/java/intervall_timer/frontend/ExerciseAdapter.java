package intervall_timer.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import intervall_timer.frontend.Model.Exercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterVH>
{
    private List<Exercise> exerciseList;
    private Context context;
    private ClickedItem clickedItem;

    public ExerciseAdapter(ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }

    public void setData(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_exercise,parent, false);
        return new ExerciseAdapterVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapterVH holder, int position) {
        Exercise exercise = exerciseList.get(position);
        String exerciseName = exercise.getName();
        holder.exerciseName.setText(exerciseName);
        holder.exerciseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem.ClickedExercise(exercise);
            }
        });
    }

    public interface ClickedItem {
        public void ClickedExercise(Exercise exercise);
    }


    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ExerciseAdapterVH extends RecyclerView.ViewHolder {
        TextView exerciseName;
        Button deleteExercise;
        Button editExercise;
        private ExerciseAdapter exerciseAdapter;


        public ExerciseAdapterVH(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            deleteExercise = itemView.findViewById(R.id.deleteExercise);
            deleteExercise.setOnClickListener(view -> {
                exerciseAdapter.exerciseList.remove(getBindingAdapterPosition());
                exerciseAdapter.notifyItemRemoved(getBindingAdapterPosition());
            });
            editExercise = itemView.findViewById(R.id.editExercise);

        }

        public ExerciseAdapterVH linkAdapter(ExerciseAdapter exerciseAdapter) {
            this.exerciseAdapter = exerciseAdapter;
            return this;
        }
    }
}
