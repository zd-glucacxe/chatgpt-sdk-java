package love.cxy020700.chatglm.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import love.cxy020700.chatglm.IOpenAiApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

/**
 * @description: 配置文件
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    // 智普Ai ChatGlM 请求地址
    @Getter
    @Setter
    private String apiHost = "https://open.bigmodel.cn/api/paas/";

    // 智普Ai https://open.bigmodel.cn/usercenter/apikeys - apiSecretKey = {apiKey}.{apiSecret}
    private String apiSecretKey;

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
        String[] arrStr = apiSecretKey.split("\\.");
        if (arrStr.length != 2) {
            throw new RuntimeException("invalid apiSecretKey");
        }
        this.apiKey = arrStr[0];
        this.apiSecret = arrStr[1];
    }

    @Getter
    private String apiKey;
    @Getter
    private String apiSecret;

    // Api 服务
    @Setter
    @Getter
    private IOpenAiApi openAiApi;

    @Getter
    @Setter
    private OkHttpClient okHttpClient;

    /**
     * 以便使用 EventSources.createFactory() 方法创建 EventSource 实例，
     * 并使用 OkHttpClient 对象来发起请求
     * @return EventSource.Factory 工厂对象
     */
    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }

    // OkHttp 配置信息
    @Setter
    @Getter
    private HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.HEADERS;
    @Setter
    @Getter
    private long connectTimeout = 450;
    @Setter
    @Getter
    private long writeTimeout = 450;
    @Setter
    @Getter
    private long readTimeout = 450;

    // http keywords
    public static final String SSE_CONTENT_TYPE = "text/event-stream";
    public static final String DEFAULT_USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    public static final String APPLICATION_JSON = "application/json";
    public static final String JSON_CONTENT_TYPE = APPLICATION_JSON + "; charset=utf-8";

}
