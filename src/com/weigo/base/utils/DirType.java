package com.weigo.base.utils;

public enum DirType {
	root,
	log,
	image,
	cache,
	crash,
	sync;
	
	public int value()
	{
		return ordinal() + 1;
	}
}
