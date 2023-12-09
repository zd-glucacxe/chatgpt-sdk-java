package love.cxy020700.chatglm;

import io.reactivex.Single;
import love.cxy020700.chatglm.model.ChatCompletionRequest;
import love.cxy020700.chatglm.model.ChatCompletionResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @description: OpenAi 接口，用于扩展通用类服务
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
public interface IOpenAiApi {

    String v3_completions = "api/paas/v3/model-api/{model}/sse-invoke";

    @POST(v3_completions)
    Single<ChatCompletionResponse> completions(@Path("model") String model,
                                               @Body ChatCompletionRequest chatCompletionRequest);

}
