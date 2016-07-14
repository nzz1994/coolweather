package com.nzz.coolweather.activity;

import com.nzz.coolweather.R;
import com.nzz.coolweather.util.HttpCallbackListener;
import com.nzz.coolweather.util.HttpUtil;
import com.nzz.coolweather.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{	
	private LinearLayout weatherInfoLayout;	
	private TextView cityNameText;	
	private TextView publishText;	
	private TextView weatherDespText;	
	private TextView temp1Text;	
	private TextView temp2Text;	
	private TextView currentDateText;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyName = getIntent().getStringExtra("county_name");
		if (!TextUtils.isEmpty(countyName)) {
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherInfo(countyName);
		} else {
			showWeather();
		}
	}
	
	
	
	//查询天气代号所对应的天气
		private void queryWeatherInfo(String countyName) {
			// TODO Auto-generated method stub
			String address = "https://api.thinkpage.cn/v3/weather/daily.json?key=8grk8uebycktak6s&location="+
					countyName+"&language=zh-Hans&unit=c&start=0&days=5" ;
			queryFromSever(address);
		}
	
	
	
	private void queryFromSever(final String address) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
					Utility.handleWeatherResponse(WeatherActivity.this, response);					
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
							
						}
					});				
			}			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {					
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}
				});
			}
		});
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", "n"));
		temp1Text.setText(prefs.getString("temp1", "n")+"℃");
		temp2Text.setText(prefs.getString("temp2", "n")+"℃");

		weatherDespText.setText(prefs.getString("weather_desp", "n"));
		publishText.setText(prefs.getString("publish_time", "n")+"发布");
		currentDateText.setText(prefs.getString("current_date", "n"));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
}
