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

package com.memoripass;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.memoripass.auth.AuthenticationManager;
import com.memoripass.databinding.ActivityMainBinding;

/**
 * メインアクティビティ
 *
 * <p>アプリのエントリーポイント。
 * セキュリティのため、FLAG_SECUREを設定し、
 * スクリーンショットと画面録画を防止する。</p>
 *
 * <p>認証機能:</p>
 * <ul>
 *   <li>起動時に生体認証を要求</li>
 *   <li>バックグラウンド移行時にオートロックタイマー開始</li>
 *   <li>フォアグラウンド復帰時に再認証</li>
 * </ul>
 *
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private AuthenticationManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // スクリーンショット・画面録画を防止
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 認証マネージャーの初期化
        authManager = new AuthenticationManager(this);

        // 初回起動時の認証
        if (!authManager.isAuthenticated()) {
            showLockScreen();
            requestAuthentication();
        } else {
            showMainContent();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // フォアグラウンド復帰時、未認証なら再認証
        if (!authManager.isAuthenticated()) {
            showLockScreen();
            requestAuthentication();
        } else {
            // オートロックタイマーを停止
            authManager.stopAutoLock();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // バックグラウンド移行時、オートロックタイマー開始
        if (authManager.isAuthenticated()) {
            authManager.startAutoLock();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // リソースのクリーンアップ
        if (authManager != null) {
            authManager.cleanup();
        }

        binding = null;
    }

    /**
     * 認証をリクエスト
     */
    private void requestAuthentication() {
        if (!authManager.isBiometricAvailable()) {
            Toast.makeText(
                    this,
                    "生体認証が利用できません。デバイスの設定を確認してください。",
                    Toast.LENGTH_LONG
            ).show();
            finish();
            return;
        }

        authManager.authenticate(this, new AuthenticationManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                Log.d(TAG, "Authentication successful");
                runOnUiThread(() -> {
                    showMainContent();
                    Toast.makeText(
                            MainActivity.this,
                            "認証に成功しました",
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }

            @Override
            public void onAuthenticationFailed() {
                Log.w(TAG, "Authentication failed");
                runOnUiThread(() -> {
                    Toast.makeText(
                            MainActivity.this,
                            "認証に失敗しました。もう一度お試しください。",
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }

            @Override
            public void onAuthenticationError(String errorMessage) {
                Log.e(TAG, "Authentication error: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(
                            MainActivity.this,
                            "認証エラー: " + errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                    finish();
                });
            }

            @Override
            public void onAuthenticationCancelled() {
                Log.d(TAG, "Authentication cancelled");
                runOnUiThread(() -> {
                    Toast.makeText(
                            MainActivity.this,
                            "認証がキャンセルされました",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                });
            }
        });
    }

    /**
     * ロック画面を表示
     */
    private void showLockScreen() {
        // TODO: 実際のロック画面UIを実装
        // 現在は仮実装として、メインコンテンツを非表示
        if (binding != null && binding.getRoot() != null) {
            binding.getRoot().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * メインコンテンツを表示
     */
    private void showMainContent() {
        // TODO: 実際のメインコンテンツUIを実装
        // 現在は仮実装として、メインコンテンツを表示
        if (binding != null && binding.getRoot() != null) {
            binding.getRoot().setVisibility(View.VISIBLE);
        }
    }
}