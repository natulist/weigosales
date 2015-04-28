package com.weigo.base.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/** 
 * @Description: 
 * @author wenbiao.xie 
 * @date 2014年10月31日 下午3:04:22 
 * @copyright TCL-MIE
 */

public class StringKVPair extends KeyValuePair<String> implements Comparable<StringKVPair>{
	
	public StringKVPair(String k, String v) {
		super(k, v);
	}
	
	public boolean isNull() {
		return TextUtils.isEmpty(key) && TextUtils.isEmpty(value);
	}
	
	@Override
	public int compareTo(StringKVPair another) {
		if (TextUtils.isEmpty(key) && TextUtils.isEmpty(another.key))
			return 0;
		
		else if (TextUtils.isEmpty(key))
			return -1;
		
		else if (TextUtils.isEmpty(another.key))
			return 1;		
		
		return key.compareTo(another.key);
	}
	
	public static StringKVPair fromJSON(JSONObject j) {
		String key = j.optString("key");
		String value = j.optString("value");
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		StringKVPair p = new StringKVPair(key, value);
		return p;
	}
	
	public static List<StringKVPair> fromJSONArray(JSONArray array) throws JSONException {
		if (array == null || array.length() == 0)
			return null;
		int size = array.length();
		List<StringKVPair> list = new ArrayList<StringKVPair>(size);
		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			if (jsonObject != null ) {
				StringKVPair p = fromJSON(jsonObject);
				if (p != null)
					list.add(p);
			}
		}
		
		return list;
	}

	public JSONObject toJSON() throws JSONException {
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		
		JSONObject json = new JSONObject();
		json.put("key", key);
		if (TextUtils.isEmpty(value))
			json.put("value", "");
		else 
			json.put("value", value);
		
		return json;
	}
}
