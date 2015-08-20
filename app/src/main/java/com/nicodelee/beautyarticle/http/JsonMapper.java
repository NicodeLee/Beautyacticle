package com.nicodelee.beautyarticle.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson简单封装
 */
public class JsonMapper {
  private JsonMapper() {
  }

  public static JsonMapper instance;
  private static ObjectMapper jsonMapper = new ObjectMapper();

  @SuppressWarnings("deprecation") public static JsonMapper getInstance() {
    if (instance == null) {
      instance = new JsonMapper();
      // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性 2.x
      jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      //1.9x
      //			jsonMapper
      //					.getDeserializationConfig()
      //					.set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
      //							false);
    }
    return instance;
  }

  public ObjectMapper getJsonMapper() {
    return jsonMapper;
  }

  /**
   * 把JavaBean转换为json字符串
   * 普通对象转换：toJson(Student)
   * List转换：toJson(List)
   * Map转换:toJson(Map)
   * 我们发现不管什么类型，都可以直接传入这个方法
   *
   * @param object JavaBean对象
   * @return json字符串
   */
  public static String toJSon(Object object) {
    if (jsonMapper == null) {
      jsonMapper = new ObjectMapper();
    }
    try {
      return jsonMapper.writeValueAsString(object);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
