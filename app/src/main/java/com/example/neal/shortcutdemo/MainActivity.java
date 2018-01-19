package com.example.neal.shortcutdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

@TargetApi(26)
public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();

    }
    private void setupView(){
        findViewById(R.id.create_older_shortcut).setOnClickListener(this);
        findViewById(R.id.set_dynamic).setOnClickListener(this);
        findViewById(R.id.add_dynamic).setOnClickListener(this);
        findViewById(R.id.update_dynamic).setOnClickListener(this);
        findViewById(R.id.remove_dynamic).setOnClickListener(this);
        findViewById(R.id.remove_all_dynamic).setOnClickListener(this);
        findViewById(R.id.pin_shortcut).setOnClickListener(this);
        findViewById(R.id.adapter_bitmap).setOnClickListener(this);
    }

    private void createIconToTable() {
            // 创建意图
            Intent intent = new Intent();
            // 指定意图行为，在桌面添加快捷图标
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            // 指定快捷方式的名字
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "BTC.COM");
            // 指定快捷方式的图标
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_home));
            // 指定快捷方式的用途
            Intent value = new Intent();
            value.setAction("android.intent.action.VIEW");
            value.addCategory("android.intent.category.DEFAULT");
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, value);
            // 发送广播,8.0直接断子绝孙，只要是创建快捷方式的ACTION，就直接不传递，所以无论是动态注册的广播接受者还是直接发送显示广播
            sendBroadcast(intent);
            sendBroadcast(new Intent("6666"));

    }
    private void getPermissionInfo(String permission){
        try {
            PermissionInfo info =getPackageManager().getPermissionInfo(permission,PackageManager.GET_META_DATA);
            Log.d("neal","name:"+info.name+","+
                    "group:"+info.group+","+
                    "package:"+info.packageName+","+
            "protectLevel:"+info.protectionLevel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void createPinShortCut(Context context){
        ShortcutManager mShortcutManager =
                context.getSystemService(ShortcutManager.class);
        LauncherApps launcherApps = (LauncherApps) getSystemService(LAUNCHER_APPS_SERVICE);
        if (launcherApps.hasShortcutHostPermission()){
            Log.d("neal","host shortcut");
        } else {
            Log.d("neal","oops host");
        }
        //检测默认Launcher是否支持pinShortcut，主要根据默认Launcher是否含有那2个intent action
        if (mShortcutManager.isRequestPinShortcutSupported()) {
            Log.d("neal","surrport shortcut");
            // Assumes there's already a shortcut with the ID "my-shortcut".
            // The shortcut must be enabled.
            ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Web site")
                    .setLongLabel("Open the web site")
                    .setIcon(Icon.createWithResource(this,R.mipmap.ic_launcher_home))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();

            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the shortcut to be pinned. Note that, if the
            // pinning operation fails, your app isn't notified. We assume here that the
            // app has implemented a method called createShortcutResultIntent() that
            // returns a broadcast intent.
            Intent pinnedShortcutCallbackIntent =
                    mShortcutManager.createShortcutResultIntent(pinShortcutInfo);
            pinnedShortcutCallbackIntent.setAction("pin.feedback");
            pinnedShortcutCallbackIntent.setClass(this,PinFeedbackReceiver.class);

            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully.
            PendingIntent successCallback = PendingIntent.getBroadcast(context, 0,
                    pinnedShortcutCallbackIntent, 0);

            mShortcutManager.requestPinShortcut(pinShortcutInfo,
                    successCallback.getIntentSender());
        } else {
            Log.d("neal","oops surrpot");
        }
    }
    private void setDynamic(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                .setShortLabel("SetDynamic")
                .setLongLabel("SetDynamic Test")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                .setIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));

    }
    private void addDynamic(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id2")
                .setShortLabel("AddDynamic")
                .setLongLabel("AddDynamic Test")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                .setIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
    }
    private void updateDynamic(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id2")
                .setShortLabel("UpdateDynamic")
                .setLongLabel("UpdateDynamic Test")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                .setIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
    }

    /**
     * 使用26引入的AdaptiveDrawable作为资源创建Pin Shortcut
     */
    private void pinShortcutWithAdaptiveDrawable(){
        ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);
        LauncherApps launcherApps = (LauncherApps) getSystemService(LAUNCHER_APPS_SERVICE);
        if (launcherApps.hasShortcutHostPermission()){
            Log.d("neal","host shortcut");
        } else {
            Log.d("neal","oops host");
        }
        //检测默认Launcher是否支持pinShortcut，主要根据默认Launcher是否含有那2个intent action
        if (mShortcutManager.isRequestPinShortcutSupported()) {
            Log.d("neal","surrport shortcut");
            // Assumes there's already a shortcut with the ID "my-shortcut".
            // The shortcut must be enabled.
            ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "id3")
                    .setShortLabel("AdaptiveDrawable Test")
                    .setLongLabel("Open the web site")
                    .setIcon(Icon.createWithResource(this,R.drawable.ic_launcher_home))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();

            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the shortcut to be pinned. Note that, if the
            // pinning operation fails, your app isn't notified. We assume here that the
            // app has implemented a method called createShortcutResultIntent() that
            // returns a broadcast intent.
            Intent pinnedShortcutCallbackIntent =
                    mShortcutManager.createShortcutResultIntent(pinShortcutInfo);
            pinnedShortcutCallbackIntent.setAction("pin.feedback");
            pinnedShortcutCallbackIntent.setClass(this,PinFeedbackReceiver.class);

            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully.
            PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                    pinnedShortcutCallbackIntent, 0);

            mShortcutManager.requestPinShortcut(pinShortcutInfo,
                    successCallback.getIntentSender());
        } else {
            Log.d("neal","oops surrpot");
        }
    }

    private void removeDynamic(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        shortcutManager.removeDynamicShortcuts(Arrays.asList("id2"));
    }

    private void removeAllDynamic(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        shortcutManager.removeAllDynamicShortcuts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_older_shortcut:
                createIconToTable();
                break;
            case R.id.set_dynamic:
                setDynamic();
                break;
            case R.id.add_dynamic:
                addDynamic();
                break;
            case R.id.update_dynamic:
                updateDynamic();
                break;
            case R.id.remove_dynamic:
                removeDynamic();
                break;
            case R.id.remove_all_dynamic:
                removeAllDynamic();
                break;
            case R.id.pin_shortcut:
                createPinShortCut(this);
                break;
            case R.id.adapter_bitmap:
                pinShortcutWithAdaptiveDrawable();
            default:
        }
    }
}
