package com.weigo.base.utils;

import java.util.ArrayList;
import java.util.List;

import com.app.framework.util.CollectionUtils;

/** 
 * @Description: 
 * @author wenbiao.xie 
 * @date 2014年11月14日 下午2:17:47 
 * @copyright TCL-MIE
 */

public class ListUtils {

	public static <E> List<E> same(List<E> l, List<E> r) {
		if (CollectionUtils.isEmpty(l) || CollectionUtils.isEmpty(r) )
			return null;
		
		List<E> s = new ArrayList<E>(l);
		s.removeAll(r);
		List<E> k = new ArrayList<E>(l);		
		k.removeAll(s);
		return k;
	}

}
