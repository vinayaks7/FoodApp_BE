package com.tutorial.FoodApp.utils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FoodAppUtils {

	private FoodAppUtils() {

	}

	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
		return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
	}

	public static String getUUID() {
		Date date = new Date();
		//It returns the number of milliseconds since January 1, 1970, 00:00:00 GMT.
		long time = date.getTime();
		return "BILL-" + time;
	}

	public static JSONArray getJsonArrayFromString(String data) throws JSONException {
		JSONArray jsonArray = new JSONArray(data);
		return jsonArray;
	}

	public static Map<String, Object> getMapFromJson(String data) {
		if (!Strings.isNullOrEmpty(data))
			return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
			}.getType());
		return new HashMap<>();
	}
	
	public static Boolean doesFileExist(String path) {
		try {
			File file = new File(path);
			return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
