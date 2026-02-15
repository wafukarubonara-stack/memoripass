package com.memoripass.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

/**
 * 暗号化SharedPreferencesラッパークラス
 *
 * <p>通常のSharedPreferencesの代わりにEncryptedSharedPreferencesを使用し、
 * 設定値を安全に保存する。</p>
 *
 * <p>セキュリティ機能:</p>
 * <ul>
 *   <li>AES256-GCMによる値の暗号化</li>
 *   <li>AES256-SIVによるキーの暗号化</li>
 *   <li>Android KeyStoreによる鍵管理</li>
 * </ul>
 *
 * @since 1.0
 */
public class SecurePreferences {

    private static final String TAG = "SecurePreferences";
    private final SharedPreferences prefs;

    /**
     * コンストラクタ
     *
     * @param context アプリケーションコンテキスト
     * @throws Exception 初期化に失敗
     */
    public SecurePreferences(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        prefs = EncryptedSharedPreferences.create(
                context,
                Constants.PreferenceKeys.PREFERENCES_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        Log.d(TAG, "SecurePreferences initialized successfully");
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }
}
