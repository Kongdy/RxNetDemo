package com.kongdy.rxnetdemo.net;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * @author kongdy
 *         on 2016/8/18
 * 适配json遇到空字符串的问题
 */
public class IntegerJsonSerializer implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        try {
            if(json.getAsString().trim().equals("") || json.getAsString().trim().equals("null")) {
                return 0;
            }
            if(json.getAsString().trim().contains(".")) {
                float temp = json.getAsFloat();
                return (int)temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return json.getAsInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return (src == null || src.toString().equals("null")) ? new JsonPrimitive(""):new JsonPrimitive(src.toString());
    }
}
