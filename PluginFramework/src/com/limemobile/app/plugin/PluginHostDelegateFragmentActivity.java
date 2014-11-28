/*
 * Copyright (C) 2014 achellies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.limemobile.app.plugin;

import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

import com.limemobile.app.plugin.internal.PluginClientManager;
import com.limemobile.app.plugin.internal.PluginDelegateActivityImpl;
import com.limemobile.app.plugin.internal.IPluginActivityDelegate;

/**
 * PluginHost中用于代理PluginClient中FragmentActivity的生命周期管理FragmentActivity，
 * PluginClient中所有的FragmentActivity调用都是通过反射实例化PluginClient中相应的FragmentActivity
 * ，然后启动PluginHostProxyActivity，通过他代理去调用PluginClient中的FragmentActivity的生命周期方法
 * 
 * @author achellies
 * 
 */
public class PluginHostDelegateFragmentActivity extends FragmentActivity
		implements IPluginActivityDelegate {

	protected IPluginActivity mDelegatedActivity;
	protected PluginDelegateActivityImpl mProxyImpl = new PluginDelegateActivityImpl(
			this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mProxyImpl.onCreate(getIntent());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void attach(IPluginActivity delegatedActivity) {
		mDelegatedActivity = delegatedActivity;
	}

	@Override
	public AssetManager getAssets() {
		return mProxyImpl.getAssets() == null ? super.getAssets() : mProxyImpl
				.getAssets();
	}

	@Override
	public Resources getResources() {
		return mProxyImpl.getResources() == null ? super.getResources()
				: mProxyImpl.getResources();
	}

	@Override
	public Theme getTheme() {
		return mProxyImpl.getTheme() == null ? super.getTheme() : mProxyImpl
				.getTheme();
	}

	@Override
	public ClassLoader getClassLoader() {
		return mProxyImpl.getClassLoader();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mDelegatedActivity.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		mDelegatedActivity.onStart();
		super.onStart();
	}

	@Override
	protected void onRestart() {
		mDelegatedActivity.onRestart();
		super.onRestart();
	}

	@Override
	protected void onResume() {
		mDelegatedActivity.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mDelegatedActivity.onPause();
		super.onPause();
	}

	@Override
	protected void onStop() {
		mDelegatedActivity.onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mDelegatedActivity.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mDelegatedActivity.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mDelegatedActivity.onRestoreInstanceState(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		mDelegatedActivity.onNewIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	public void onBackPressed() {
		mDelegatedActivity.onBackPressed();
		super.onBackPressed();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return mDelegatedActivity.onTouchEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);
		return mDelegatedActivity.onKeyUp(keyCode, event);
	}

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		mDelegatedActivity.onWindowAttributesChanged(params);
		super.onWindowAttributesChanged(params);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		mDelegatedActivity.onWindowFocusChanged(hasFocus);
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mDelegatedActivity.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mDelegatedActivity.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			intent.setPackage(mDelegatedActivity.getPackageName());
		} else {
			super.startActivityForResult(intent, requestCode);
			return;
		}
		PluginClientManager.sharedInstance(this).startActivityForResult(this,
				intent, requestCode);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode,
			Bundle options) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			intent.setPackage(mDelegatedActivity.getPackageName());
		} else {
			super.startActivityForResult(intent, requestCode, options);
			return;
		}
		PluginClientManager.sharedInstance(this).startActivityForResult(this,
				intent, requestCode, options);
	}

	@Override
	public void startActivity(Intent intent) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			intent.setPackage(mDelegatedActivity.getPackageName());
		} else {
			super.startActivity(intent);
			return;
		}
		PluginClientManager.sharedInstance(this).startActivity(this, intent);
	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			intent.setPackage(mDelegatedActivity.getPackageName());
		} else {
			super.startActivity(intent, options);
			return;
		}
		PluginClientManager.sharedInstance(this).startActivity(this, intent,
				options);
	}

	@Override
	public ComponentName startService(Intent service) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentServices(service, PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			service.setPackage(mDelegatedActivity.getPackageName());
		} else {
			return super.startService(service);
		}
		return PluginClientManager.sharedInstance(this).startService(this,
				service);
	}

	@Override
	public boolean stopService(Intent service) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentServices(service, PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			service.setPackage(mDelegatedActivity.getPackageName());
		} else {
			return super.stopService(service);
		}
		return PluginClientManager.sharedInstance(this).stopService(this,
				service);
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		List<ResolveInfo> resolveInfos = getPackageManager()
				.queryIntentServices(service, PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfos == null || resolveInfos.isEmpty()) {
			service.setPackage(mDelegatedActivity.getPackageName());
		} else {
			return super.bindService(service, conn, flags);
		}
		return PluginClientManager.sharedInstance(this).bindService(this,
				service, conn, flags);
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		mDelegatedActivity.unbindService(conn);
		super.unbindService(conn);
	}
}
