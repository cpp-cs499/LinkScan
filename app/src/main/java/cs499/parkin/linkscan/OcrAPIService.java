package cs499.parkin.linkscan;

import com.squareup.okhttp.RequestBody;

import cs499.parkin.linkscan.data.ImageData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by parkin on 2/6/2016.
 */
public interface OcrAPIService {


    @POST("/parse/image")
    Call<ImageData> postImageData(
         @Body RequestBody image);
}
