
package com.weigo.sales.activity.base;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * 
 * 所有Activity的基类，负责管理Activity生命周期
 *
 */
public class BaseActivity extends FragmentActivity {

	
	private static class ActivityReference extends WeakReference<Activity>
	{
		public ActivityReference(Activity r, ReferenceQueue<? super Activity> q)
		{
			super(r, q);
		}
	}
	
	/** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
	static ReferenceQueue<Activity>	sReferenceQueue = new ReferenceQueue<Activity>();;
	static LinkedList<ActivityReference> sStack = new LinkedList<ActivityReference>();
	
	private boolean mVisible;
	private ActivityReference mRef;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mRef = new ActivityReference(this, sReferenceQueue);
		sStack.push(mRef);
	}
	
	private static void clean()
	{
		ActivityReference ref = null;
		while ((ref = (ActivityReference) sReferenceQueue.poll()) != null)
		{
			sStack.remove(ref);
		}
	}
	
	public static void finishActivityStack() {
		clean();
		Iterator<ActivityReference> it = sStack.iterator();
		while (it.hasNext()) {
			ActivityReference ref = it.next();
			if (ref != null && ref.get() != null) {
				Activity activity = ref.get();
				if (!activity.isFinishing())
					activity.finish();
			}
		}
		
		sStack.clear();
	}
	
	public static Activity topActivity() {
		Activity activity = null;
		ActivityReference ref = null;
		
		do {
			
			ref = sStack.peek();
			if (ref == null)
				break;
			if ( ref.get() != null) {
				activity = ref.get();
				break;
			} else {
				sStack.pop();
			}
			
		} while(true);
		
		return activity;
	}
	
	protected static void finishActivityStackExcept(Activity a) {
		clean();
		Iterator<ActivityReference> it = sStack.iterator();
		while (it.hasNext()) {
			ActivityReference ref = it.next();
			if (ref != null && ref.get() != null) {
				Activity activity = ref.get();
				if (activity == a)
					continue;
				
				if (!activity.isFinishing())
					activity.finish();
			}
		}
		
		sStack.clear();
	}
	
	protected static void finishActivityStackExcept(List<Class<? extends Activity>> clzList) {
		clean();
		Iterator<ActivityReference> it = sStack.iterator();
		while (it.hasNext()) {
			ActivityReference ref = it.next();
			if (ref != null && ref.get() != null) {
				Activity activity = ref.get();
				Class<? extends Activity> clz = activity.getClass();
				if (clzList.contains(clz))
					continue;
				
				if (!activity.isFinishing())
					activity.finish();
			}
		}
		
		sStack.clear();
	}
		
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		sStack.remove(mRef);
	}

	protected boolean isVisible() {
		return mVisible;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mVisible = true;
	}	
		
	@Override
	protected void onPause() {
		super.onPause();
		mVisible = false;
	}

	
	/**
	 * 只显示栈底的activity
	 */
	public static void gotoBottomActivity(){
		if (sStack.size() <= 1){
			return;
		}
		clean();
		while (sStack.size() != 1) {
			ActivityReference ref = sStack.getFirst();
			if (ref != null && ref.get() != null) {
				Activity activity = ref.get();
				if (!activity.isFinishing()){
					activity.finish();
					sStack.remove(ref);
				}
			}
		}
		
	}

}
