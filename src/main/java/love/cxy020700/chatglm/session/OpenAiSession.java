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

    /**
     * 该方法使用EventSource类来进行异步请求和接收回复。它接受两个参数，chatCompletionRequest是聊天请求对象，
     * 包含了聊天的上下文和问题等信息；eventSourceListener是一个事件监听器，用于处理接收到的回复信息。该方法
     * 返回一个EventSource对象，可以通过该对象进行订阅和取消订阅事件源。在订阅后，可以通过监听器处理接收到的回复信息
     * @param chatCompletionRequest
     * @param eventSourceListener
     * @return
     * @throws JsonProcessingException
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest,
                            EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 该方法使用CompletableFuture来进行异步请求和获取回复。它接受一个chatCompletionRequest参数，表示聊天请求
     * 对象。该方法返回一个CompletableFuture对象，可以通过该对象进行异步操作并获取回复信息。可以通过调用get()方
     * 法来等待并获取异步操作的结果，该结果为生成的回复字符串。
     * @param chatCompletionRequest
     * @return
     * @throws InterruptedException
     */
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

}
