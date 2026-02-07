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

package com.memoripass.ui.add;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.model.Password;
import com.memoripass.domain.usecase.AddPasswordUseCase;
import com.memoripass.ui.common.BaseViewModel;

/**
 * パスワード追加ViewModel
 *
 * <p>パスワード追加画面のUI状態とビジネスロジックを管理する。</p>
 *
 * @since 1.0
 */
public class AddPasswordViewModel extends BaseViewModel {

    private static final String TAG = "AddPasswordViewModel";

    private final AddPasswordUseCase addPasswordUseCase;
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    /**
     * コンストラクタ
     *
     * @param application アプリケーション
     */
    public AddPasswordViewModel(@NonNull Application application) {
        super(application);

        try {
            PasswordRepository repository = new PasswordRepository(application);
            this.addPasswordUseCase = new AddPasswordUseCase(repository);

            Log.d(TAG, "AddPasswordViewModel initialized successfully");

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to initialize AddPasswordViewModel", e);
            throw new RuntimeException("Failed to initialize ViewModel", e);
        }
    }

    /**
     * 保存成功イベントを取得
     *
     * @return 保存成功のLiveData
     */
    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    /**
     * パスワードを保存
     *
     * @param title タイトル
     * @param username ユーザー名
     * @param password パスワード
     * @param url URL
     * @param notes メモ
     * @param category カテゴリ
     */
    public void savePassword(
            @NonNull String title,
            String username,
            @NonNull String password,
            String url,
            String notes,
            String category
    ) {
        Log.d(TAG, "Saving password: " + title);
        setLoading();

        // バリデーション
        if (title.trim().isEmpty()) {
            setError("タイトルを入力してください");
            return;
        }

        if (password.isEmpty()) {
            setError("パスワードを入力してください");
            return;
        }

        // Passwordドメインモデルを構築
        Password passwordModel = new Password.Builder()
                .title(title.trim())
                .username(username)
                .password(password)
                .url(url)
                .notes(notes)
                .category(category)
                .build();

        // バックグラウンドで保存
        new Thread(() -> {
            try {
                addPasswordUseCase.execute(passwordModel);
                
                Log.i(TAG, "Password saved successfully: " + title);
                
                // 成功を通知
                saveSuccess.postValue(true);
                setSuccess();

            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Validation error: " + e.getMessage());
                setError(e.getMessage());
                
            } catch (CryptoManager.CryptoException e) {
                Log.e(TAG, "Encryption error", e);
                setError("暗号化に失敗しました: " + e.getMessage());
                
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error", e);
                setError("保存に失敗しました: " + e.getMessage());
            }
        }).start();
    }
}
