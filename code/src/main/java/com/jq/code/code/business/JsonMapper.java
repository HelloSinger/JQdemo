package com.jq.code.code.business;

import com.jq.code.model.BGlucoseEntity;
import com.jq.code.model.BPressEntity;
import com.jq.code.model.DataType;
import com.jq.code.model.WeightEntity;
import com.jq.code.model.json.JsonDataEnyity;
import com.jq.code.model.sport.SubmitFoodEntity;
import com.jq.code.model.sport.SubmitSportEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/8/3.
 */
public class JsonMapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private JsonMapper() {
    }

    public static ObjectMapper getInstance() {
        return mapper;
    }

    public static String toJson(Object data) {
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        LogUtil.i("JsonMapper", "writer:" + writer);
        return writer.toString();
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
//        LogUtil.i("JsonMapper", "json:" + json);
        T t = null;
        try {
            t = mapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T fromJson(Object json, Class<T> tClass) {
//        LogUtil.i("JsonMapper", "Object:" + json);
        return fromJson(toJson(json), tClass);
    }

    public static <T> T fromJson(String json, TypeReference valueTypeRef) {
//        LogUtil.i("JsonMapper", "json:" + json);
        T t = null;
        try {
            t = mapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T fromJson(Object json, TypeReference valueTypeRef) {
//        LogUtil.i("JsonMapper", "Object:" + json);
        T t = null;
        try {
            t = mapper.readValue(toJson(json), valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 解析测量数据
     *
     * @param json
     * @return
     */
    public static JsonDataEnyity fromDataJson(Object json) {
        String toJson = toJson(json);
        JsonDataEnyity dataEnyity = new JsonDataEnyity();
        try {
            JsonNode jsonNode = mapper.readTree(toJson);
            dataEnyity.setMtype(jsonNode.get("mtype").asText());
            dataEnyity.setLastsync(jsonNode.get("lastsync").asLong());
            dataEnyity.setRole_id(jsonNode.get("role_id").asLong());
            JsonNode putbaseNode = jsonNode.get("mdata");
            Iterator<JsonNode> elements = putbaseNode.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                String mtype = next.get("mtype").asText();
                if (mtype.equals(DataType.WEIGHT.getType())) {
                    dataEnyity.getMdata().add(fromJson(next, WeightEntity.class));
                } else if (mtype.equals(DataType.BSL.getType())) {
                    dataEnyity.getMdata().add(fromJson(next, BGlucoseEntity.class));
                } else if (mtype.equals(DataType.BP.getType())) {
                    dataEnyity.getMdata().add(fromJson(next, BPressEntity.class));
                } else if (mtype.equals(DataType.EXERCISE.getType())) {
                    dataEnyity.getMdata().add(fromJson(next, SubmitSportEntity.class));
                } else if (mtype.equals(DataType.FOOD.getType())) {
                    dataEnyity.getMdata().add(fromJson(next, SubmitFoodEntity.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataEnyity;
    }
}
