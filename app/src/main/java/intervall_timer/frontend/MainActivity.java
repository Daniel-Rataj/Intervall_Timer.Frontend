package intervall_timer.frontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.Service.ApiClient;
import intervall_timer.frontend.Service.Response.ServiceResponse;
import intervall_timer.frontend.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  ExerciseAdapter.ClickedItem, CreateExerciseDialog.CreateExerciseDialogListener {

    private final String EXERCISEDETAILKEY = "DATA";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseAdapter = new ExerciseAdapter(this::ClickedExercise);
        getAllExercises();


        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.createExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        CreateExerciseDialog exerciseDialog = new CreateExerciseDialog();
        exerciseDialog.show(getSupportFragmentManager(), "Create new Exercise");
    }

    public void getAllExercises() {
        Call<ServiceResponse<Exercise>> exerciseList = ApiClient.getExerciseService().getAll();
        exerciseList.enqueue(new Callback<ServiceResponse<Exercise>>() {
            @Override
            public void onResponse(Call<ServiceResponse<Exercise>> call, Response<ServiceResponse<Exercise>> response) {
                if(response.isSuccessful()) {
                    List<Exercise> exerciseList;
                    if(response.body().listResponse != null) {
                        exerciseList = response.body().listResponse;
                        exerciseAdapter.setData(exerciseList);
                        recyclerView.setAdapter(exerciseAdapter);
                    } else {
                        exerciseList = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse<Exercise>> call, Throwable t) {
                Log.e("failure", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void ClickedExercise(Exercise exercise) {
        String toastMsg = "Übung " + exercise.getName() + " wird geöffnet";
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        Intent exerciseDetailsIntent = new Intent(this, ExerciseDetailsActivity.class)
                .putExtra(EXERCISEDETAILKEY, exercise);
        startActivity(exerciseDetailsIntent);
    }

    @Override
    public void saveExercise(String name, int repCount, int lapTime, int lapBreaktime, int startCountdown) {
        Exercise createdExercise = new Exercise(name, repCount, lapTime, lapBreaktime, startCountdown);
        Call<ServiceResponse<Exercise>> save = ApiClient.getExerciseService().save(createdExercise);
        save.enqueue(new Callback<ServiceResponse<Exercise>>() {
            @Override
            public void onResponse(Call<ServiceResponse<Exercise>> call, Response<ServiceResponse<Exercise>> response) {
                if(response.isSuccessful()) {
                    getAllExercises();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse<Exercise>> call, Throwable t) {
                Log.e("failure", t.getLocalizedMessage());
            }
        });
    }
}