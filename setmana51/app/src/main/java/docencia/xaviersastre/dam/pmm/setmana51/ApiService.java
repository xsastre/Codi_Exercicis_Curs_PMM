package docencia.xaviersastre.dam.pmm.setmana51;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("posts")
    Call<PostResponse> createPost(@Body PostRequest post);
}