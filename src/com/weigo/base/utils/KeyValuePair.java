package com.weigo.base.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/** 
 * @Description: 
 * @author wenbiao.xie 
 * @date 2014年10月31日 下午3:41:16 
 * @copyright TCL-MIE
 */

public class KeyValuePair<VT> {
	
	public String key;
	public VT value;
	
	public KeyValuePair(String k, VT v) {
		this.key = k;
		this.value = v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		KeyValuePair other = (KeyValuePair) obj;
		
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		
		return true;
	}
	
	public JSONObject toJSON() throws JSONException {
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		
		JSONObject json = new JSONObject();
		json.put("key", key);
		
		if (value != null)
			json.put("value", value);
		
		return json;
	}

}
