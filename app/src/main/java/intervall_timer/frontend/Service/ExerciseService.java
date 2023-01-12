package intervall_timer.frontend.Service;


import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.Service.Response.ServiceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ExerciseService {
    static String baseServiceURL = "exercise/";
    static String getAllURL = baseServiceURL + "getAll";
    static String saveExerciseURL = baseServiceURL + "newExercise";
    static String deleteExerciseURL = baseServiceURL + "delete";

    @GET(getAllURL)
    Call<ServiceResponse<Exercise>> getAll();

    @POST(saveExerciseURL)
    Call<ServiceResponse<Exercise>> save(@Body Exercise exercise);

    @POST(deleteExerciseURL)
    Call<ServiceResponse<Exercise>> delete(@Body long id);
}
