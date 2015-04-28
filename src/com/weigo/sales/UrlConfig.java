package com.weigo.sales;

import com.app.framework.network.HostNameResolver;

public class UrlConfig {

	private static final String PROTOCOL_HOST = "http://120.24.55.91/";
	private static final String ROOT_API = "service_drp/";

	private static final String SHARE_URI = "goods.jsp";
	// 上传新增的图片
	private static final String ALBUMS_UPLOAD_PIC = "albumsync/uploadpic";
	private static final String LOGIN = "member.jsp";

	
	private static String getAbsoluteURI(String uri) {
		StringBuffer sb = new StringBuffer(PROTOCOL_HOST);
		sb.append(ROOT_API);
		sb.append(uri);
//		sb.append("/");
		return HostNameResolver.resovleURL(sb.toString());
	}

	public static String getShareListURL() {
		return getAbsoluteURI(SHARE_URI);
	}

	public static String getUploadPicUrl() {
		return getAbsoluteURI(ALBUMS_UPLOAD_PIC);
	}
	
	public static String getLoginUrl() {
		return getAbsoluteURI(LOGIN);
	}
}
