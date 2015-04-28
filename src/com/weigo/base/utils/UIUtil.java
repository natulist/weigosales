package com.weigo.base.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.framework.util.DeviceManager;
import com.app.framework.util.TemplateRunnable;
import com.weigo.sales.R;

public class UIUtil {

	/**
	 * 删除当前应用的桌面快捷方式
	 * 
	 * @param cx
	 */
	public static void delShortcut(Context cx) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(cx.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}
		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		Intent shortcutIntent = cx.getPackageManager()
				.getLaunchIntentForPackage(cx.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		cx.sendBroadcast(shortcut);
	}

	/**
	 * 判断桌面是否已添加快捷方式
	 * 
	 * @param cx
	 * @param titleName
	 *            快捷方式名称
	 * @return
	 */
	public static boolean hasShortcut(Context cx) {
		boolean result = false;
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(cx.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}

		final String uriStr;
		if (android.os.Build.VERSION.SDK_INT < 8) {
			uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final Uri CONTENT_URI = Uri.parse(uriStr);
		final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,
				"title=?", new String[] { title }, null);
		if (c != null && c.getCount() > 0) {
			result = true;
		}
		/* System.out.println("判断" + result); */
		return result;
	}
	
	public static void vibrate (Context context, long milliseconds)
	{
		Vibrator vb = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vb.vibrate(milliseconds);
	}

	public static void hideActivity (Context context)
	{
		//对联想OS2.0设备做特殊处理
		try
		{
			Intent homeIntent = new Intent(Intent.ACTION_MAIN, null);
			homeIntent.putExtra("GOHOME", "GOHOME");
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
					Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			homeIntent.setClassName("com.android.launcher",
					"com.android.launcher.HomeScreen");
			context.startActivity(homeIntent);
		}
		catch (ActivityNotFoundException e)
		{
			Intent homeIntent = new Intent(Intent.ACTION_MAIN);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			homeIntent.addCategory(Intent.CATEGORY_HOME);
			context.startActivity(homeIntent);
		}
	}
	
	/**
	 * 隐藏键盘
	 *
	 * @param context 上下文
	 * @param binder  键盘绑定者
	 */
	public static void hideKeyboard (Context context, IBinder binder)
	{
		InputMethodManager m = (InputMethodManager)
				context.getSystemService(Context.INPUT_METHOD_SERVICE);
		IBinder localIBinder = binder;
		m.hideSoftInputFromWindow(localIBinder, 0);
	}	
	
	public static void hideKeyboard (Context context)
	{
		if(context instanceof Activity)
		{
			Activity activity = (Activity) context;
			InputMethodManager imm= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
			if(activity.getCurrentFocus() !=  null )
			{  
				if(activity.getCurrentFocus().getWindowToken() !=  null )
				{  
					imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),  
							InputMethodManager.HIDE_NOT_ALWAYS);  
				}  
			}  
		}
	}
	
	public static void hideToast() {
		if (app_toast != null) {
			app_toast.cancel();
			app_toast = null;
		}
	}
	
	public static  boolean              enable_toast_visible = true;
	private static android.widget.Toast app_toast            = null;
	
	private static void showToastInner(Context context, int iconId, CharSequence text, boolean longToast)
	{
		if (!enable_toast_visible)
		{
			return;
		}
		int dp = DeviceManager.dip2px(context, 20);
		if (app_toast != null && app_toast.getView() != null)
		{
			TextView textView = (TextView) app_toast.getView().findViewById(R.id.tv_toast_msg);
			textView.setText(text);
			ImageView iv = (ImageView) app_toast.getView().findViewById(R.id.iv_toast_icon);
			if(iconId != 0)
			{
				iv.setVisibility(View.VISIBLE);
				iv.setImageResource(iconId);
			}else{
				iv.setVisibility(View.GONE);
			}
			app_toast.setDuration(longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
			
			app_toast.setGravity(Gravity.BOTTOM, 0, dp);
			app_toast.show();
			return;
		}

		app_toast = null;
		Toast toast = new Toast(context);
		LayoutInflater inflate = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.common_toast, null);
		TextView tv = (TextView) v.findViewById(R.id.tv_toast_msg);
		tv.setText(text);
		ImageView iv = (ImageView) v.findViewById(R.id.iv_toast_icon);
		if(iconId != 0)
		{
			iv.setVisibility(View.VISIBLE);
			iv.setImageResource(iconId);
		}else{
			iv.setVisibility(View.GONE);
		}
		
		toast.setView(v);
		toast.setGravity(Gravity.BOTTOM, 0, dp);
		toast.setDuration(longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		app_toast = toast;
		toast.show();
	}
	
	static class ToastTask {
		public Context context;
		public int iconId;
		public CharSequence text;
		public boolean longToast;
		
		public ToastTask(Context context, int iconId, CharSequence text, boolean longToast) {
			this.context = context;
			this.iconId = iconId;
			this.text = text;
			this.longToast = longToast;
		}
	}
	
	public static void showToast (Context context, int iconId, CharSequence text, boolean longToast)
	{
		if (!enable_toast_visible || context == null)
		{
			return;
		}
		
		if (ThreadUtil.isMainThread()) {
			showToastInner(context, 0, text, longToast);
		}
		
		else if (context instanceof Activity) {
			
			final Activity activity = (Activity) context;
			activity.runOnUiThread(new TemplateRunnable<ToastTask>(new ToastTask(context, iconId, text, longToast)) {

				@Override
				protected void doRun(ToastTask e) {
					showToastInner(e.context, e.iconId, e.text, e.longToast);
				}
				
			});
		}		
		
		else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new TemplateRunnable<ToastTask>(new ToastTask(context, iconId, text, longToast)) {

				@Override
				protected void doRun(ToastTask e) {
					showToastInner(e.context, e.iconId, e.text, e.longToast);
				}
				
			});
		}
	}
	
	/**
	 * 默认样式的提示
	 *
	 * @param context
	 * @param text      显示的内容
	 * @param longToast 长时间显示还是短时间显示
	 */
	public static void showToast (Context context, CharSequence text, boolean longToast)
	{
		showToast(context, 0, text, longToast);
	}

	/**
	 * 自定义样式的提示
	 *
	 * @param context
	 * @param textId    资源id
	 * @param longToast 长时间显示还是短时间显示
	 */
	public static void showToast (Context context, int textId, boolean longToast)
	{
		if (context == null)
			return;
		CharSequence text = context.getResources().getText(textId);
		showToast(context, text, longToast);
	}
	
	public static int colorWithRGB (int r, int g, int b)
	{
		int color = Color.rgb(r, g, b);
		return color;
	}

	public static int colorWithARGB (int a, int r, int g, int b)
	{
		int color = Color.argb(a, r, g, b);
		return color;
	}
	
	public static Drawable tilefy (Context context, Drawable drawable, boolean clip)
	{
		Bitmap mSampleTile = null;
		if (drawable instanceof LayerDrawable)
		{
			LayerDrawable background = (LayerDrawable) drawable;
			final int N = background.getNumberOfLayers();
			Drawable[] outDrawables = new Drawable[N];

			for (int i = 0; i < N; i++)
			{
				int id = background.getId(i);
				outDrawables[i] = tilefy(context, background.getDrawable(i),
						(id == android.R.id.progress || id == android.R.id.secondaryProgress));
			}

			LayerDrawable newBg = new LayerDrawable(outDrawables);

			for (int i = 0; i < N; i++)
			{
				newBg.setId(i, background.getId(i));
			}

			return newBg;

		}
		else if (drawable instanceof BitmapDrawable)
		{
			((BitmapDrawable) drawable).setTargetDensity(context.getResources().getDisplayMetrics());
			final Bitmap tileBitmap = ((BitmapDrawable) drawable).getBitmap();
			if (mSampleTile == null)
			{
				mSampleTile = tileBitmap;
			}
			final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
			final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
					Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
			shapeDrawable.getPaint().setShader(bitmapShader);

			return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
					ClipDrawable.HORIZONTAL) : shapeDrawable;
		}
		return drawable;
	}
	
	static Shape getDrawableShape ()
	{
		final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		return new RoundRectShape(roundedCorners, null, null);
	}
	
	public static StateListDrawable getBackground (Drawable normal, Drawable pressed)
	{
		return MyView.getBackground(normal, pressed, null);
	}

	private static class MyView extends View
	{
		public MyView (Context context)
		{
			super(context);
		}

		public static StateListDrawable getBackground (Drawable normal, Drawable pressed, Drawable disabled)
		{
			StateListDrawable stateListDrawable = new StateListDrawable();
			stateListDrawable.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
			stateListDrawable.addState(View.ENABLED_FOCUSED_STATE_SET, pressed);
			stateListDrawable.addState(View.ENABLED_STATE_SET, normal);
			stateListDrawable.addState(View.FOCUSED_STATE_SET, pressed);
			stateListDrawable.addState(View.EMPTY_STATE_SET, normal);
			return stateListDrawable;
		}
	}
	
	public static void keepFontScale(Context context) {
		Resources res = context.getResources();
		Configuration c = res.getConfiguration();
		float old = c.fontScale;
		if (old != 1.0f) {
			c.fontScale = 1.0f;
			res.updateConfiguration(c, res.getDisplayMetrics());
		}
	}
	
}
