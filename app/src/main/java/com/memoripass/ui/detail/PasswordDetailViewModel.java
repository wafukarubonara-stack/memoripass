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

package com.memoripass.ui.detail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.model.Password;
import com.memoripass.domain.usecase.GetPasswordUseCase;
import com.memoripass.ui.common.BaseViewModel;

/**
 * パスワード詳細ViewModel
 *
 * <p>パスワード詳細画面のUI状態とビジネスロジックを管理する。</p>
 *
 * @since 1.0
 */
public class PasswordDetailViewModel extends BaseViewModel {

    private static final String TAG = "PasswordDetailViewModel";

    private final GetPasswordUseCase getPasswordUseCase;
    private LiveData<Password> password;

    /**
     * コンストラクタ
     *
     * @param application アプリケーション
     */
    public PasswordDetailViewModel(@NonNull Application application) {
        super(application);

        try {
            PasswordRepository repository = new PasswordRepository(application);
            this.getPasswordUseCase = new GetPasswordUseCase(repository);

            Log.d(TAG, "PasswordDetailViewModel initialized successfully");

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to initialize PasswordDetailViewModel", e);
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
}
