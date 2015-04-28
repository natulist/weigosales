package com.weigo.sales;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.app.framework.fs.Directory;
import com.app.framework.fs.DirectroyContext;
import com.app.framework.util.TimeConstants;
import com.weigo.base.utils.DirType;



public class SHDirectoryContext extends DirectroyContext {
	
	Context mContext;
	
	public SHDirectoryContext(Context context, String appName)
	{
		this.mContext = context;
		initContext(appName);
	}	

	@Override
	public void initContext(String root)
	{
		if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			File fileDir = mContext.getFilesDir();
			
			String rootPath = fileDir.getAbsolutePath() + 
					File.separator + root;
					
			super.initContext(rootPath);
		}else {	
			String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + 
					File.separator + root;			
			super.initContext(rootPath);
		}
	}

	@Override
	protected Collection<Directory> initDirectories()
	{
		List<Directory> children = new ArrayList<Directory>();
		
		Directory dir = newDirectory(DirType.log);
		children.add(dir);
		dir = newDirectory(DirType.image);
		children.add(dir);		
		dir = newDirectory(DirType.crash);		
		children.add(dir);
		dir = newDirectory(DirType.cache);		
		children.add(dir);
		return children;
	}
	
	private Directory newDirectory(DirType type) {
		Directory child = new Directory(type.toString(), null);
		child.setType(type.value());
		if (type.equals(DirType.cache))
		{
			child.setForCache(true);
			child.setExpiredTime(TimeConstants.ONE_DAY_MS);
		}
		
		return child;
	}
}
