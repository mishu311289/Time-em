package com.time_em.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import com.time_em.authentication.LoginActivity;
import com.time_em.authentication.PinAuthentication;
import com.time_em.utils.GcmUtils;
import com.time_em.utils.Utils;


public class SplashActivity extends Activity {
	public DependencyResolver resolver;
	private BroadcastReceiver receiver;
	private Context mContext;
	//todo variables
	private boolean isRegistered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
		mContext = getApplicationContext();

		resolver = new DependencyResolver(mContext);

        new Handler().postDelayed(new Runnable() {

			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				/*if(Utils.getSharedPrefs(SplashActivity.this, "loginId").equals("")){
					Intent i = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(i);
				}else{
					Intent i = new Intent(SplashActivity.this, PinAuthentication.class);
					startActivity(i);
				}*/
				if(Utils.isNetworkAvailable(getApplicationContext())) {
					getRegisterId();
					//goNext();
				}else{
					//Utils.alertMessage(SplashActivity.this,Utils.network_error);
					goNext();
				}
				// close this activity

			}
		}, 2000);
    }
	private void getRegisterId()
	{
		String regId = GcmUtils.getRegistrationId(this);
		Log.e("splash", regId);
		if (regId.isEmpty()) {

			Log.e("splash", "regId = empty");
			GcmUtils.registerInBackground(this, new GcmUtils.RegisterListener() {
				@Override
			public void onRegistered(String regId) {
					Log.e("splash", "regId = " + regId);

					goNext();
				}
				@Override
			public void onError(Exception e) {
					Log.e("splash", "regId = " + e.toString());
					goNext();
				}
			});

		} else
			goNext();

	}
	/*private void initReceivers() {

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
			if (isRegistered) {
					unregisterReceiver(receiver);
					isRegistered = false;
					goNext();
				}
				else{
					goNext();
				}

			}
		};
		isRegistered = true;
	}*/

	private void goNext()
	{
		if(Utils.getSharedPrefs(SplashActivity.this, "loginId").equals("")){
			Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(mIntent);
		}else{
			Intent mIntent = new Intent(SplashActivity.this, PinAuthentication.class);
			startActivity(mIntent);
		}
		finish();
	}
}
