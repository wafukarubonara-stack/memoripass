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

package com.memoripass.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.local.AppDatabase;
import com.memoripass.data.local.dao.PasswordEntryDao;
import com.memoripass.data.model.PasswordEntry;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * パスワードリポジトリ
 *
 * <p>パスワードデータへのアクセスを抽象化する。
 * データソース（Room）と暗号化処理を統合し、
 * ドメイン層に対して単一のインターフェースを提供する。</p>
 *
 * <p>責務:</p>
 * <ul>
 *   <li>データの取得・保存・更新・削除</li>
 *   <li>パスワードの暗号化・復号</li>
 *   <li>バックグラウンドスレッドでの処理実行</li>
 * </ul>
 *
 * @since 1.0
 */
public class PasswordRepository {

    private static final String TAG = "PasswordRepository";

    private final PasswordEntryDao passwordDao;
    private final CryptoManager cryptoManager;
    private final Executor executor;
    private final LiveData<List<PasswordEntry>> allPasswords;

    /**
     * コンストラクタ
     *
     * @param context アプリケーションコンテキスト
     * @throws CryptoManager.CryptoException 暗号化マネージャーの初期化に失敗
     */
    public PasswordRepository(@NonNull Context context) throws CryptoManager.CryptoException {
        AppDatabase database = AppDatabase.getInstance(context);
        this.passwordDao = database.passwordEntryDao();
        this.cryptoManager = new CryptoManager();
        this.executor = Executors.newSingleThreadExecutor();
        this.allPasswords = passwordDao.getAllPasswords();

        Log.d(TAG, "PasswordRepository initialized");
    }

    // ==================== 読み取り操作 ====================

    /**
     * すべてのパスワードを取得
     *
     * @return パスワードエントリのLiveDataリスト
     */
    public LiveData<List<PasswordEntry>> getAllPasswords() {
        return allPasswords;
    }

    /**
     * IDでパスワードを取得
     *
     * @param id エントリID
     * @return パスワードエントリのLiveData
     */
    public LiveData<PasswordEntry> getPasswordById(@NonNull String id) {
        return passwordDao.getPasswordById(id);
    }

    /**
     * カテゴリでパスワードを取得
     *
     * @param category カテゴリ名
     * @return パスワードエントリのLiveDataリスト
     */
    public LiveData<List<PasswordEntry>> getPasswordsByCategory(@NonNull String category) {
        return passwordDao.getPasswordsByCategory(category);
    }

    /**
     * パスワードを検索
     *
     * @param query 検索クエリ
     * @return パスワードエントリのLiveDataリスト
     */
    public LiveData<List<PasswordEntry>> searchPasswords(@NonNull String query) {
        return passwordDao.searchPasswords(query);
    }

    // ==================== 書き込み操作 ====================

    /**
     * パスワードを挿入
     *
     * @param entry パスワードエントリ
     */
    public void insert(@NonNull PasswordEntry entry) {
        executor.execute(() -> {
            passwordDao.insert(entry);
            Log.d(TAG, "Password inserted: " + entry.getId());
        });
    }

    /**
     * パスワードを更新
     *
     * @param entry パスワードエントリ
     */
    public void update(@NonNull PasswordEntry entry) {
        executor.execute(() -> {
            entry.updateTimestamp();
            passwordDao.update(entry);
            Log.d(TAG, "Password updated: " + entry.getId());
        });
    }

    /**
     * パスワードを削除
     *
     * @param entry パスワードエントリ
     */
    public void delete(@NonNull PasswordEntry entry) {
        executor.execute(() -> {
            passwordDao.delete(entry);
            Log.d(TAG, "Password deleted: " + entry.getId());
        });
    }

    /**
     * IDでパスワードを削除
     *
     * @param id エントリID
     */
    public void deleteById(@NonNull String id) {
        executor.execute(() -> {
            passwordDao.deleteById(id);
            Log.d(TAG, "Password deleted by ID: " + id);
        });
    }

    // ==================== 暗号化ヘルパー ====================

    /**
     * パスワードを暗号化
     *
     * @param plainPassword 平文パスワード
     * @return Base64エンコードされた暗号化パスワード
     * @throws CryptoManager.CryptoException 暗号化に失敗
     */
    @NonNull
    public String encryptPassword(@NonNull String plainPassword) throws CryptoManager.CryptoException {
        return cryptoManager.encrypt(plainPassword);
    }

    /**
     * パスワードを復号
     *
     * @param encryptedPassword 暗号化パスワード
     * @return 平文パスワード
     * @throws CryptoManager.CryptoException 復号に失敗
     */
    @NonNull
    public String decryptPassword(@NonNull String encryptedPassword) throws CryptoManager.CryptoException {
        return cryptoManager.decrypt(encryptedPassword);
    }
}
