package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**使用jackson的工具包
 * 高可复用的jackson的Util包
 * Created by txk on 2018/7/6.
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper=new ObjectMapper();
    //准备工作设置一下
    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认转化timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //所有日期格式统一为一下的格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARDFORMAT));
        //忽略在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误。
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    }

    /**
     * 封装的一个把对象转换成jeson字符串的方法
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String?(String) obj:objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error.",e);
            return null;
        }
    }

    /**
     *封装一个格式化好的把对象转换成jeson字符串的方法
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String?(String) obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error.",e);
            return null;
        }
    }

    /**
     * 封装了字符串转对象的方法
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str,Class clazz){
        if(StringUtils.isEmpty(str) || clazz==null){
            return null;
        }
        try {
            return clazz.equals(String.class)?(T)str:(T)objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse String to Objiec error",e);
            return null;
        }
    }

    /**
     *封装了字符串转对象的方法（用于list的情况）
     * @param str
     * @param tTypeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> tTypeReference){
        if(StringUtils.isEmpty(str) || tTypeReference==null){
            return null;
        }
        try {
            return (T)(tTypeReference.getType().equals(String.class)?str:objectMapper.readValue(str,tTypeReference));
        } catch (Exception e) {
            log.warn("Parse String to Objiec error",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elementClasses ){
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to Objiec error",e);
            return null;
        }
    }


    public static void main(String[] args) {
        User u1=new User();
        User u2=new User();
        u1.setId(1);
        u1.setEmail("1231");

        String u1Json=JsonUtil.obj2String(u1);
        String u1JsonPretty=JsonUtil.obj2StringPretty(u1);

        log.info("user1Json:{}",u1Json);
        log.info("user1JsonPretty:{}",u1JsonPretty);
        User user=JsonUtil.string2Obj(u1Json,User.class);

        List<User> userList= Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);
        String userListStr=obj2String(userList);
        log.info("=====");
        log.info("userListStr:{}",userListStr);

        List<User> userListObj=string2Obj(userListStr, new TypeReference<List<User>>() {
        });//一个坑不能变为useList<User> userListObjr对象

        List<User> userListObj2=string2Obj(userListStr,List.class,User.class);

        System.out.println("end");
    }

}
