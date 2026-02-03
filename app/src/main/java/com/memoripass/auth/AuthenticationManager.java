/*
 * Copyright 2026 wafukarubonara-stack
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.memoripass.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

/**
 * 認証管理クラス
 *
 * <p>生体認証とデバイス認証情報（PIN/パターン/パスワード）を管理する。
 * アプリのロック/アンロック状態を制御し、オートロック機能を提供する。</p>
 *
 * <p>セキュリティ機能:</p>
 * <ul>
 *   <li>生体認証（指紋・顔認証）</li>
 *   <li>デバイス認証情報へのフォールバック</li>
 *   <li>30秒間のオートロック</li>
 *   <li>認証状態の管理</li>
 * </ul>
 *
 * @since 1.0
 */
public class AuthenticationManager {

    private static final String TAG = "AuthenticationManager";

    // オートロックまでの時間（ミリ秒）
    private static final long AUTO_LOCK_DELAY_MS = 30_000; // 30秒

    private final Context context;
    private final BiometricManager biometricManager;
    private final Handler autoLockHandler;
    private final Runnable autoLockRunnable;

    private boolean isAuthenticated = false;
    private BiometricPrompt biometricPrompt;

    /**
     * コンストラクタ
     *
     * @param context アプリケーションコンテキスト
     */
    public AuthenticationManager(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.biometricManager = BiometricManager.from(this.context);
        this.autoLockHandler = new Handler(Looper.getMainLooper());
        this.autoLockRunnable = this::lock;
    }

    /**
     * 生体認証が利用可能かチェック
     *
     * @return true: 利用可能, false: 利用不可
     */
    public boolean isBiometricAvailable() {
        int result = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
        );

        switch (result) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(TAG, "Biometric authentication is available");
                return true;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(TAG, "No biometric hardware available");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(TAG, "Biometric hardware currently unavailable");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(TAG, "No biometric credentials enrolled");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Log.e(TAG, "Security update required");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Log.e(TAG, "Biometric authentication unsupported");
                return false;

            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Log.e(TAG, "Biometric status unknown");
                return false;

            default:
                Log.e(TAG, "Unknown biometric status: " + result);
                return false;
        }
    }

    /**
     * 認証を実行
     *
     * @param activity FragmentActivity
     * @param callback 認証結果のコールバック
     */
    public void authenticate(
            @NonNull FragmentActivity activity,
            @NonNull AuthenticationCallback callback
    ) {
        if (!isBiometricAvailable()) {
            callback.onAuthenticationError("生体認証が利用できません");
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(context);

        biometricPrompt = new BiometricPrompt(
                activity,
                executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result
                    ) {
                        super.onAuthenticationSucceeded(result);
                        Log.d(TAG, "Authentication succeeded");
                        isAuthenticated = true;
                        stopAutoLock();
                        callback.onAuthenticationSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.w(TAG, "Authentication failed");
                        callback.onAuthenticationFailed();
                    }

                    @Override
                    public void onAuthenticationError(
                            int errorCode,
                            @NonNull CharSequence errString
                    ) {
                        super.onAuthenticationError(errorCode, errString);
                        Log.e(TAG, "Authentication error: " + errString);

                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                                errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                            callback.onAuthenticationCancelled();
                        } else {
                            callback.onAuthenticationError(errString.toString());
                        }
                    }
                }
        );

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Memoripass 認証")
                .setSubtitle("パスワードにアクセスするには認証してください")
                .setDescription("生体認証またはデバイス認証情報を使用")
                .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG |
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    /**
     * アプリをロック
     */
    public void lock() {
        Log.d(TAG, "Locking application");
        isAuthenticated = false;
        stopAutoLock();
    }

    /**
     * オートロックタイマーを開始
     */
    public void startAutoLock() {
        Log.d(TAG, "Starting auto-lock timer (30 seconds)");
        stopAutoLock();
        autoLockHandler.postDelayed(autoLockRunnable, AUTO_LOCK_DELAY_MS);
    }

    /**
     * オートロックタイマーを停止
     */
    public void stopAutoLock() {
        autoLockHandler.removeCallbacks(autoLockRunnable);
    }

    /**
     * 認証状態を取得
     *
     * @return true: 認証済み, false: 未認証
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * リソースをクリーンアップ
     */
    public void cleanup() {
        stopAutoLock();
        if (biometricPrompt != null) {
            biometricPrompt.cancelAuthentication();
            biometricPrompt = null;
        }
    }

    /**
     * 認証結果のコールバックインターフェース
     */
    public interface AuthenticationCallback {
        /**
         * 認証成功時
         */
        void onAuthenticationSuccess();

        /**
         * 認証失敗時（生体情報が一致しない）
         */
        void onAuthenticationFailed();

        /**
         * 認証エラー時（ハードウェアエラー等）
         *
         * @param errorMessage エラーメッセージ
         */
        void onAuthenticationError(String errorMessage);

        /**
         * 認証キャンセル時
         */
        void onAuthenticationCancelled();
    }
}