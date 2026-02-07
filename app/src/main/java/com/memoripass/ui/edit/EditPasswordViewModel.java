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

package com.memoripass.ui.edit;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.model.Password;
import com.memoripass.domain.usecase.GetPasswordUseCase;
import com.memoripass.domain.usecase.UpdatePasswordUseCase;
import com.memoripass.ui.common.BaseViewModel;

/**
 * パスワード編集ViewModel
 *
 * <p>パスワード編集画面のUI状態とビジネスロジックを管理する。</p>
 *
 * @since 1.0
 */
public class EditPasswordViewModel extends BaseViewModel {

    private static final String TAG = "EditPasswordViewModel";

    private final GetPasswordUseCase getPasswordUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private LiveData<Password> password;

    /**
     * コンストラクタ
     *
     * @param application アプリケーション
     */
    public EditPasswordViewModel(@NonNull Application application) {
        super(application);

        try {
            PasswordRepository repository = new PasswordRepository(application);
            this.getPasswordUseCase = new GetPasswordUseCase(repository);
            this.updatePasswordUseCase = new UpdatePasswordUseCase(repository);

            Log.d(TAG, "EditPasswordViewModel initialized successfully");

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to initialize EditPasswordViewModel", e);
            throw new RuntimeException("Failed to initialize ViewModel", e);
        }
    }

    /**
     * パスワードを読み込む
     *
     * @param passwordId パスワードID
     */
    public void loadPassword(@NonNull String passwordId) {
        Log.d(TAG, "Loading password: " + passwordId);
        setLoading();

        try {
            password = getPasswordUseCase.execute(passwordId);
            setSuccess();

        } catch (Exception e) {
            Log.e(TAG, "Failed to load password", e);
            setError("パスワードの読み込みに失敗しました");
        }
    }

    /**
     * パスワードを取得
     *
     * @return パスワードのLiveData
     */
    public LiveData<Password> getPassword() {
        return password;
    }

    /**
     * 更新成功イベントを取得
     *
     * @return 更新成功のLiveData
     */
    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    /**
     * パスワードを更新
     *
     * @param id パスワードID
     * @param title タイトル
     * @param username ユーザー名
     * @param password パスワード
     * @param url URL
     * @param notes メモ
     * @param category カテゴリ
     * @param createdAt 作成日時
     */
    public void updatePassword(
            @NonNull String id,
            @NonNull String title,
            String username,
            @NonNull String password,
            String url,
            String notes,
            String category,
            long createdAt
    ) {
        Log.d(TAG, "Updating password: " + title);
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
                .id(id)
                .title(title.trim())
                .username(username)
                .password(password)
                .url(url)
                .notes(notes)
                .category(category)
                .createdAt(createdAt)
                .updatedAt(System.currentTimeMillis())
                .build();

        // バックグラウンドで更新
        new Thread(() -> {
            try {
                updatePasswordUseCase.execute(passwordModel);
                
                Log.i(TAG, "Password updated successfully: " + title);
                
                // 成功を通知
                updateSuccess.postValue(true);
                setSuccess();

            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Validation error: " + e.getMessage());
                setError(e.getMessage());
                
            } catch (CryptoManager.CryptoException e) {
                Log.e(TAG, "Encryption error", e);
                setError("暗号化に失敗しました: " + e.getMessage());
                
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error", e);
                setError("更新に失敗しました: " + e.getMessage());
            }
        }).start();
    }
}
