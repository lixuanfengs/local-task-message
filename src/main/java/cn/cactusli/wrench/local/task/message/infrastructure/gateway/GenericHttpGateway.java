package cn.cactusli.wrench.local.task.message.infrastructure.gateway;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

import java.util.Map;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/3 17:34
 * @github https://github.com/lixuanfengs
 */
public interface GenericHttpGateway {

    @POST
    Call<ResponseBody> post(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Body RequestBody body
    );

}
