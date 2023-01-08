package intervall_timer.frontend.Service;


import intervall_timer.frontend.Model.Exercise;
import intervall_timer.frontend.Service.Response.ServiceResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExerciseService {
    static final String getAllURL = "exercise/getAll";

    @GET(getAllURL)
    Call<ServiceResponse<Exercise>> getAll();
}
