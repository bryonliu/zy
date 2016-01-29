package com.tencent.zy.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.tencent.zy.app.ZyApplication;

public class ApkUtil {
    
    public static boolean isAppInstalledFromSystem(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
             PackageInfo info = getInstalledPackageInfo(packageName,0);
             if(null == info) {
                 return false;
             }
            return true;
        }
        return false;
    }
    
    /**
     * 获得已安装的APK包信息
     * @param pkgName apk包名
     * @param flag 需要签名等其它信息的要指定flag, 如果只想获取安装包信息的flag建议置为0 这样可以提高访问速度
     * @return
     */
    public static PackageInfo getInstalledPackageInfo(String pkgName, int flag) {
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }

        PackageInfo pkgInfo = null;
        PackageManager pm = ZyApplication.self().getPackageManager();
        if (pm != null) {
            try {
                pkgInfo = (PackageInfo) pm.getPackageInfo(pkgName, flag); // flag为0提高访问速度
            } catch (NameNotFoundException e) {
//              e.printStackTrace();
            } catch (RuntimeException e) {
//              e.printStackTrace();
            }
        }
        return pkgInfo;
    }
}
