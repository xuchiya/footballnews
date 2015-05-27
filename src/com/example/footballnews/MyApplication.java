package com.example.footballnews;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
	private CacheData mCache;
	@Override
	public void onCreate() {
		super.onCreate();
        mCache = new CacheData();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();



		// ��Ϊ��û�������������������Ĭ��ͼƬ��ͼƬΪ��ʱ������ʧ��ʱ�������С�
		ImageLoader.getInstance().init(config);
	}
	
	public CacheData getCacheData(){
		
		return mCache;
	}
	
}
