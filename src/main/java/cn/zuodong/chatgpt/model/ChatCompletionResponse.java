package cn.zuodong.chatgpt.model;

import lombok.Data;

/**
 * @description: 返回结果
 * @author zuodong
 * @createDate: 2023/12/9
 * @version: 1.0
 */
@Data
public class ChatCompletionResponse {

    /**
     * 对话结果
     */
    private String data;
    /**
     * 表示与结果相关的元数据
     */
    private String meta;

    @Data
    public static class Meta {
        // 任务的状态
        private String task_status;
        // 任务耗材量
        private Usage usage;
        // 任务id
        private String task_id;
        // 请求id
        private String request_id;
    }

    /**
     * 用量
     */
    @Data
    public static class Usage {
        // 完成的 token 数量
        private int completion_tokens;
        // 提示的 token 数量
        private int prompt_tokens;
        // 总 token 数量
        private int total_tokens;
    }

}
