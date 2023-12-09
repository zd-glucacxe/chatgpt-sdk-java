package love.cxy020700.chatglm.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import love.cxy020700.chatglm.model.ChatCompletionRequest;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * @description: 会话服务接口
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
public interface OpenAiSession {

    EventSource completions(ChatCompletionRequest chatCompletionRequest,
                            EventSourceListener eventSourceListener) throws JsonProcessingException;

    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

}
