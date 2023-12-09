package love.cxy020700.chatglm;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import love.cxy020700.chatglm.model.*;
import love.cxy020700.chatglm.session.Configuration;
import love.cxy020700.chatglm.session.OpenAiSession;
import love.cxy020700.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @description: 在官网申请 ApiSecretKey <a href="https://open.bigmodel.cn/usercenter/apikeys">ApiSecretKey</a>
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
@Slf4j
public class ApiTest {

    private OpenAiSession openAiSession;

    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("4cc938c69a448e31babe006889ae434f.vP9n6LTcTOlJtnVf");
        configuration.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 2. 会话工厂
        DefaultOpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    @Test
    public void test_completions() throws JsonProcessingException, InterruptedException {
        // 入参：模型、请求信息
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO);
        request.setIncremental(false);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            private static final long serialVersionUID = -7988151926241837899L;

            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("2+3")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("Okay")
                        .build());

                /* system 和 user 为一组出现。如果有参数类型为 system 则 system + user 一组一起传递。*/
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.system.getCode())
                        .content("10+10")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("Okay")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("99-57")
                        .build());

            }
        });

        // 请求
        openAiSession.completions(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                log.info("测试结果 onEvent：{}", response.getData());
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.finish.getCode().equals(type)) {
                    ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
                    log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
                }

            }
            @Override
            public void onClosed(EventSource eventSource) {
                log.info("对话结束");
            }
        });

        // 等待
        new CountDownLatch(1).await();
    }
}
