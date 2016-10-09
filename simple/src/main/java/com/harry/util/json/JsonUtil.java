package com.harry.util.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.ClassMorpher;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

public class JsonUtil {
	
	private static String[] dateFormats = new String[] { "yyyy-MM-dd" };
	private static String dateFormat="yyyy-MM-dd";
	private static String timeFormat="yyyy-MM-dd HH:mm:ss";

    public static String obj2Json(Object obj) {
    	JsonConfig jsonConfig = new JsonConfig(); //建立配置文件
    	jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor(dateFormat));//json 日期转换
    	jsonConfig.registerJsonValueProcessor(Timestamp.class,new JsonDateValueProcessor(timeFormat));//json 时间转换
    	JSONObject jsonObject = JSONObject.fromObject(obj,jsonConfig); //加载配置文件
        return jsonObject.toString();
    }
    
    public static String list2Json(List list) {
    	JsonConfig jsonConfig = new JsonConfig(); //建立配置文件
    	jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor(dateFormat));//json 时间转换
    	jsonConfig.registerJsonValueProcessor(Timestamp.class,new JsonDateValueProcessor(timeFormat));//json 时间转换
//    	jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略
//    	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
//    	jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer"});//解决:hibernate延时加载 设置
    	JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig); //加载配置文件
        return jsonArray.toString();
    }
    
    public static <T> T json2Object(String jsonStr,Class<T> clazz) {
    	if (null==jsonStr) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(decode(jsonStr));
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
        return (T) JSONObject.toBean(jsonObj,clazz);
    }
    
//	public static <T> List<T> json2List(String jsonStr, Class<T> clazz) {
//		JSONArray jsonArray = JSONArray.fromObject(decode(jsonStr));
//
//		JSONUtils.getMorpherRegistry().registerMorpher(
//				new DateMorpher(dateFormats));
//		
//		Map<String, Class> classMap = new HashMap<String, Class>();
//		classMap.put("attrs", ItemAttr.class);
//		classMap.put("glass", ItemGlass.class);
//		
//		JsonConfig jsonConfig = new JsonConfig();
//		jsonConfig.setRootClass(clazz);
//		jsonConfig.setClassMap(classMap);
//
//		List<T> list = (List) JSONArray.toCollection(jsonArray, jsonConfig);
//		return list;
//	}
    
    public static String decode(String value){
    	try {
			return URLDecoder.decode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
