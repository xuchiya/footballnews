package com.example.footballnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class splashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ImageView image = (ImageView) findViewById(R.id.image);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(splashActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();

			}
		}, 2000);

	}
}
