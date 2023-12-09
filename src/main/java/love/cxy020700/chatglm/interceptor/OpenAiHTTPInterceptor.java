package love.cxy020700.chatglm.interceptor;

import love.cxy020700.chatglm.session.Configuration;
import love.cxy020700.chatglm.utils.BearerTokenUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @description: 接口拦截器
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
public class OpenAiHTTPInterceptor implements Interceptor {

    private final Configuration configuration;

    public OpenAiHTTPInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        // 1. 获取原始请求
        Request original = chain.request();

        // 2. 构建新的请求
        Request request = original.newBuilder()
                .url(original.url())
                // 设置请求头参数
                .header("Authorization", "Bearer " + BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                // 设置参数类型
                .header("Content-Type", Configuration.JSON_CONTENT_TYPE)
                // 设置授权信息
                .header("User-Agent", Configuration.DEFAULT_USER_AGENT)
                // 设置内容类型
                .header("Accept", Configuration.SSE_CONTENT_TYPE)
                .method(original.method(), original.body())
                .build();

        // 3. 返回执行结果
        return chain.proceed(request);

    }
}
