package love.cxy020700.chatglm.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 签名工具包；过期时间30分钟，缓存时间29分钟
 * @author: pengyanyu
 * @createDate: 2023/12/9
 * @version: 1.0
 */
@Slf4j
public class BearerTokenUtils {

    // 过期时间；默认30分钟
    private static final long expireMillis = 30 * 60 * 1000L;

    // 缓存服务
    public static Cache<String, String> cache = CacheBuilder.newBuilder()
            // 设定缓存项在给定时间内没有被写访问（创建或覆盖），则清除缓存。
            // 这里的有效时间是 expireMillis - 60秒，即 29.5 分钟
            .expireAfterWrite(expireMillis - (60 * 1000L), TimeUnit.MILLISECONDS)
            .build();

    /**
     * 对 ApiKey 进行签名
     *
     * @param apiKey    登录创建 ApiKey <a href="https://open.bigmodel.cn/usercenter/apikeys">apikeys</a>
     * @param apiSecret apiKey的后半部分 828902ec516c45307619708d3e780ae1.w5eKiLvhnLP8MtIf 取 w5eKiLvhnLP8MtIf 使用
     * @return Token
     */
    public static String getToken(String apiKey, String apiSecret) {
        // 先从缓存获取Token
        String token = cache.getIfPresent(apiKey);
        if (null != token) {
            return token;
        }

        // 创建算法对象
        Algorithm algorithm = Algorithm.HMAC256(apiSecret.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", apiKey);
        // 放入缓存过期时间
        payload.put("exp", System.currentTimeMillis() + expireMillis);
        // 放入当前时间戳
        payload.put("timestamp", Calendar.getInstance().getTimeInMillis());
        Map<String, Object> headerClaims = new HashMap<>();
        // 放入指定算法和签名类型
        headerClaims.put("alg", "HS256");
        headerClaims.put("sign_type", "SIGN");
        // 创建Token
        token = JWT.create().withPayload(payload).withHeader(headerClaims).sign(algorithm);
        // 放入缓存
        cache.put(apiKey, token);
        return token;
    }

}
