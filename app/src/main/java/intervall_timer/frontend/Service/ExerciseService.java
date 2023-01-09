package intervall_timer.frontend.Service;


import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.Service.Response.ServiceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ExerciseService {
    static final String baseServiceURL = "exercise/";
    static final String getAllURL = baseServiceURL + "getAll";
    static final String saveExerciseURL = baseServiceURL + "newExercise";

    @GET(getAllURL)
    Call<ServiceResponse<Exercise>> getAll();

    @POST(saveExerciseURL)
    Call<ServiceResponse<Exercise>> save(@Body Exercise exercise);
}
