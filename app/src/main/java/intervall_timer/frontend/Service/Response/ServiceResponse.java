package intervall_timer.frontend.Service.Response;

import java.util.List;

public class ServiceResponse<T> {
    public ResponseType responseType = null;
    public String message = null;
    public T singleResponse = null;
    public List<T> listResponse = null;
}


