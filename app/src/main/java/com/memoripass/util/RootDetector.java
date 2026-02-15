package com.memoripass.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * root化端末検知クラス
 *
 * <p>複数の手法を組み合わせてroot化を検知する。
 * root化端末ではAndroid KeyStoreが迂回される可能性があるため、
 * セキュリティリスクとして警告する。</p>
 *
 * <p>検知手法:</p>
 * <ul>
 *   <li>suバイナリの存在確認</li>
 *   <li>既知のrootアプリの確認</li>
 *   <li>ビルドタグの確認</li>
 *   <li>書き込み可能パスの確認</li>
 *   <li>デバッガー接続の確認</li>
 * </ul>
 *
 * @since 1.0.1
 */
public class RootDetector {

    private static final String TAG = "RootDetector";

    // suバイナリの既知パス
    private static final String[] SU_PATHS = {
        "/system/bin/su",
        "/system/xbin/su",
        "/sbin/su",
        "/system/su",
        "/system/bin/.ext/.su",
        "/system/usr/we-need-root/su-backup",
        "/system/xbin/mu"
    };

    // 既知のrootアプリのパッケージ名
    private static final String[] ROOT_PACKAGES = {
        "com.noshufou.android.su",
        "com.noshufou.android.su.elite",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.thirdparty.superuser",
        "com.yellowes.su",
        "com.topjohnwu.magisk",
        "com.kingroot.kinguser",
        "com.kingo.root"
    };

    // 書き込み可能な危険パス
    private static final String[] DANGEROUS_PATHS = {
        "/system",
        "/system/bin",
        "/system/sbin",
        "/system/xbin",
        "/vendor/bin",
        "/sys",
        "/sbin",
        "/etc"
    };

    private final Context context;

    public RootDetector(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * root化を総合的に判定
     *
     * @return true: root化の可能性あり
     */
    public boolean isRooted() {
        boolean suFound = checkSuBinary();
        boolean rootApp = checkRootApps();
        boolean buildTag = checkBuildTags();
        boolean writablePath = checkWritablePaths();
        boolean debugger = checkDebuggerConnected();

        Log.w(TAG, "Root check - su:" + suFound
                + " rootApp:" + rootApp
                + " buildTag:" + buildTag
                + " writablePath:" + writablePath
                + " debugger:" + debugger);

        return suFound || rootApp || buildTag || writablePath || debugger;
    }

    /**
     * suバイナリの存在確認
     */
    private boolean checkSuBinary() {
        for (String path : SU_PATHS) {
            if (new File(path).exists()) {
                Log.w(TAG, "su binary found: " + path);
                return true;
            }
        }
        return false;
    }

    /**
     * 既知のrootアプリの確認
     */
    private boolean checkRootApps() {
        PackageManager pm = context.getPackageManager();
        for (String pkg : ROOT_PACKAGES) {
            try {
                pm.getPackageInfo(pkg, 0);
                Log.w(TAG, "Root app found: " + pkg);
                return true;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return false;
    }

    /**
     * ビルドタグの確認
     * テストビルドやデバッグビルドはroot化リスクが高い
     */
    private boolean checkBuildTags() {
        String buildTags = Build.TAGS;
        boolean isTestKey = buildTags != null && buildTags.contains("test-keys");
        if (isTestKey) {
            Log.w(TAG, "Test-keys build detected");
        }
        return isTestKey;
    }

    /**
     * 書き込み可能な危険パスの確認
     */
    private boolean checkWritablePaths() {
        for (String path : DANGEROUS_PATHS) {
            File file = new File(path);
            if (file.exists() && file.canWrite()) {
                Log.w(TAG, "Writable dangerous path: " + path);
                return true;
            }
        }
        return false;
    }

    /**
     * デバッガー接続の確認
     */
    private boolean checkDebuggerConnected() {
        boolean debuggerConnected = android.os.Debug.isDebuggerConnected();
        if (debuggerConnected) {
            Log.w(TAG, "Debugger connected");
        }
        return debuggerConnected;
    }

    /**
     * 検知結果の詳細を返す
     */
    public RootCheckResult getDetailedResult() {
        return new RootCheckResult(
            checkSuBinary(),
            checkRootApps(),
            checkBuildTags(),
            checkWritablePaths(),
            checkDebuggerConnected()
        );
    }

    /**
     * 検知結果クラス
     */
    public static class RootCheckResult {
        public final boolean suBinaryFound;
        public final boolean rootAppFound;
        public final boolean testKeysBuild;
        public final boolean writablePathFound;
        public final boolean debuggerConnected;
        public final boolean isRooted;

        public RootCheckResult(boolean su, boolean app, boolean keys,
                               boolean path, boolean debugger) {
            this.suBinaryFound = su;
            this.rootAppFound = app;
            this.testKeysBuild = keys;
            this.writablePathFound = path;
            this.debuggerConnected = debugger;
            this.isRooted = su || app || keys || path || debugger;
        }
    }
}
