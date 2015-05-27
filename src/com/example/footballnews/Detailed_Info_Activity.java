package com.example.footballnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Detailed_Info_Activity extends Activity{
	private WebView mWebview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_info);
        mWebview = (WebView) findViewById(R.id.webView1);
        mWebview.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        mWebview.loadUrl(url);
	
	
	
	}
	

}
