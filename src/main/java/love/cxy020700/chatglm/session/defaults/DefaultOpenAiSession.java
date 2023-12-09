package love.cxy020700.chatglm.session.defaults;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import love.cxy020700.chatglm.IOpenAiApi;
import love.cxy020700.chatglm.model.ChatCompletionRequest;
import love.cxy020700.chatglm.model.ChatCompletionResponse;
import love.cxy020700.chatglm.model.EventType;
import love.cxy020700.chatglm.session.Configuration;
import love.cxy020700.chatglm.session.OpenAiSession;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @description: 会话服务
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
@Slf4j
public class DefaultOpenAiSession implements OpenAiSession {

    /**
     * OpenAi 接口
     */
    private final Configuration configuration;
    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
    }

    /**
     * 方法实现了对话完成的功能
     * @param chatCompletionRequest
     * @param eventSourceListener
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        // 构建请求信息
        Request request = new Request.Builder()
                // 拼接请求地址
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getCode()))
                // 设置请求体参数
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();

        return factory.newEventSource(request, eventSourceListener);
    }

    /**
     *
     * @param chatCompletionRequest
     * @return
     * @throws InterruptedException
     */
    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        // 用于执行异步任务并获取结果
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();

        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();

        // 异步响应请求
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.add.getCode().equals(type)) {
                    dataBuffer.append(response.getData());
                } else if (EventType.finish.getCode().equals(type)) {
                    future.complete(dataBuffer.toString());
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });

        return future;

    }
}
