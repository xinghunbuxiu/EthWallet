package com.lixh.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.lixh.rxhttp.Observable;

public class LocalAppInfo extends Observable {
    private final static String TAG = "LocalAppInfo";
    private String appName;
    private String packageName;
    private String versionName;
    private String imei;
    private int versionCode;
    String labelRes;
    private static LocalAppInfo localAppInfo;

    @SuppressLint("MissingPermission")
    public static void init(Context mContext) {
        localAppInfo = new LocalAppInfo();
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                localAppInfo.setAppName(pm.getApplicationLabel(appInfo).toString());
                localAppInfo.setPackageName(info.packageName);
                localAppInfo.setVersionCode(info.versionCode);
                localAppInfo.setVersionName(info.versionName);
                localAppInfo.setLabelRes(mContext.getResources().getString(info.applicationInfo.labelRes));
            }
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            localAppInfo.setIMei(tm.getDeviceId());
            ULog.d(TAG, "about: LocalAppInfo = " + localAppInfo.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static LocalAppInfo getLocalAppInfo() {
        if (null == localAppInfo) {
            throw new RuntimeException("LocalAppInfo is not init.");
        }
        return localAppInfo;
    }

    public String getLabelRes() {
        return this.labelRes;
    }

    public void setLabelRes(String labelRes) {
        this.labelRes = labelRes;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getSdkINT(Context context) {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getIMei() {
        return imei;
    }

    public void setIMei(String imei) {
        this.imei = imei;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "LocalAppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", imei='" + imei + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}