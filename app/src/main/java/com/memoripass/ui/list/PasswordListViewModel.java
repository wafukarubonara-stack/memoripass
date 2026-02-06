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

package com.memoripass.ui.list;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.usecase.DeletePasswordUseCase;
import com.memoripass.domain.usecase.GetAllPasswordsUseCase;
import com.memoripass.ui.common.BaseViewModel;

import java.util.List;

/**
 * パスワード一覧ViewModel
 *
 * <p>パスワード一覧画面のUI状態とビジネスロジックを管理する。</p>
 *
 * <p>機能:</p>
 * <ul>
 *   <li>パスワード一覧の取得</li>
 *   <li>検索</li>
 *   <li>カテゴリフィルタ</li>
 *   <li>パスワード削除</li>
 * </ul>
 *
 * @since 1.0
 */
public class PasswordListViewModel extends BaseViewModel {

    private static final String TAG = "PasswordListViewModel";

    private final GetAllPasswordsUseCase getAllPasswordsUseCase;
    private final DeletePasswordUseCase deletePasswordUseCase;

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final LiveData<List<PasswordEntry>> passwords;

    /**
     * コンストラクタ
     *
     * @param application アプリケーション
     */
    public PasswordListViewModel(@NonNull Application application) {
        super(application);

        try {
            // Repositoryとユースケースの初期化
            PasswordRepository repository = new PasswordRepository(application);
            this.getAllPasswordsUseCase = new GetAllPasswordsUseCase(repository);
            this.deletePasswordUseCase = new DeletePasswordUseCase(repository);

            // 検索クエリに応じてパスワードを取得
            this.passwords = Transformations.switchMap(searchQuery, query -> {
                if (query == null || query.isEmpty()) {
                    return getAllPasswordsUseCase.execute();
                } else {
                    return getAllPasswordsUseCase.executeSearch(query);
                }
            });

            // 初期状態として全件取得
            searchQuery.setValue("");

            Log.d(TAG, "PasswordListViewModel initialized successfully");

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to initialize PasswordListViewModel", e);
            throw new RuntimeException("Failed to initialize ViewModel", e);
        }
    }

    /**
     * パスワード一覧を取得
     *
     * @return パスワードエントリのLiveDataリスト
     */
    public LiveData<List<PasswordEntry>> getPasswords() {
        return passwords;
    }

    /**
     * パスワードを検索
     *
     * @param query 検索クエリ
     */
    public void searchPasswords(@NonNull String query) {
        Log.d(TAG, "Searching passwords with query: " + query);
        searchQuery.setValue(query);
    }

    /**
     * 検索をクリア（全件表示）
     */
    public void clearSearch() {
        Log.d(TAG, "Clearing search");
        searchQuery.setValue("");
    }

    /**
     * カテゴリでフィルタ
     *
     * @param category カテゴリ名
     */
    public void filterByCategory(@NonNull String category) {
        Log.d(TAG, "Filtering by category: " + category);
        // TODO: カテゴリフィルタの実装
        // 現時点では検索機能で代用
    }

    /**
     * パスワードを削除
     *
     * @param passwordEntry 削除するパスワードエントリ
     */
    public void deletePassword(@NonNull PasswordEntry passwordEntry) {
        Log.d(TAG, "Deleting password: " + passwordEntry.getId());

        try {
            deletePasswordUseCase.execute(passwordEntry.getId());
            setSuccess();
            Log.i(TAG, "Password deleted successfully");
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * パスワード一覧が読み込まれた時の処理
     *
     * @param passwords パスワードリスト
     */
    public void onPasswordsLoaded(List<PasswordEntry> passwords) {
        if (passwords == null || passwords.isEmpty()) {
            setEmpty();
        } else {
            setSuccess();
        }
    }
}
