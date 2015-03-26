package com.nicodelee.beautyarticle.http;

import com.fasterxml.jackson.databind.JavaType;
import com.nicodelee.utils.StringUtils;

import java.io.IOException;

/**
 * Created by Nicodelee on 15/3/26.
 */
public class JsonUtil {

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     * @see #jsonToMod(String, JavaType)
     */
    public static  <T> T jsonToMod(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return JsonMapper
                    .getInstance()
                    .getJsonMapper().readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToMod(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) JsonMapper
                    .getInstance()
                    .getJsonMapper().readValue(jsonString,javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
