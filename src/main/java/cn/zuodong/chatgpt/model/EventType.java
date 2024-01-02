package cn.zuodong.chatgpt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 消息类型 <a href="https://open.bigmodel.cn/dev/api#chatglm_lite">chatglm_lite</a>
 * @author zuodong
 * @createDate: 2023/12/9
 * @version: 1.0
 */
@Getter
@AllArgsConstructor
public enum EventType {

    add("add", "增量"),
    finish("finish", "结束"),
    error("error", "错误"),
    interrupted("interrupted", "中断"),

            ;
    private final String code;
    private final String info;

}
