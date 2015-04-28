package com.weigo.sales;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.app.framework.fs.DirectoryManager;
import com.weigo.base.utils.DirType;
import com.weigo.base.utils.ServiceContext;
import com.weigo.base.utils.ServiceName;

public class SHContext extends ServiceContext {
	
	private final static String GC_ROOT_FOLDER = "weigosales";
	
	public static boolean initInstance (Context context)
	{
		if (_instance == null)
		{
			SHContext gcContext = new SHContext(context);
			
			_instance = gcContext;
			return gcContext.init();
		}
		
		return true;
	}
	
	private Map<String, Object> objsMap;
	DirectoryManager mDirectoryManager = null;
	
	public SHContext(Context context) {
		super(context);
		objsMap = new HashMap<String, Object>();
	}
	
	private boolean init()
	{		
		DirectoryManager dm = new DirectoryManager(new SHDirectoryContext(getApplicationContext(), GC_ROOT_FOLDER));
		boolean ret = dm.buildAndClean();
		if (!ret) {
			return false;
		}
		
		registerSystemObject(ServiceName.DIR_MANAGER, dm);
		mDirectoryManager = dm;
		
		return ret;
	}
	
	public static DirectoryManager getDirectoryManager()
	{
		if (_instance == null)
			return null;
		
		return ((SHContext)_instance).mDirectoryManager;
	}
	
	public static  File getDirectory(DirType type) {
		DirectoryManager manager = getDirectoryManager();
		if (manager == null)
			return null;
		
		return manager.getDir(type.value());
	}
	
	public static String getDirectoryPath(DirType type) {
		File file = getDirectory(type);
		if (file == null) 
			return null;
		
		return file.getAbsolutePath();
	}

	@Override
	public void registerSystemObject(String name, Object obj) {
		objsMap.put(name, obj);
	}

	@Override
	public Object getSystemObject(String name) {
		return objsMap.get(name);
	}

}
