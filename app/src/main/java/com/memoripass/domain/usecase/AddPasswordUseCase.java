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

package com.memoripass.domain.usecase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.model.Password;

/**
 * パスワード追加ユースケース
 *
 * <p>新しいパスワードをデータベースに追加する。
 * ビジネスバリデーションと暗号化を行う。</p>
 *
 * <p>処理フロー:</p>
 * <ol>
 *   <li>入力バリデーション（タイトル・パスワード必須）</li>
 *   <li>パスワードの暗号化</li>
 *   <li>PasswordEntryエンティティに変換</li>
 *   <li>データベースに保存</li>
 * </ol>
 *
 * @since 1.0
 */
public class AddPasswordUseCase {

    private static final String TAG = "AddPasswordUseCase";

    private final PasswordRepository repository;

    /**
     * コンストラクタ
     *
     * @param repository パスワードリポジトリ
     */
    public AddPasswordUseCase(@NonNull PasswordRepository repository) {
        this.repository = repository;
    }

    /**
     * パスワードを追加
     *
     * @param password パスワードドメインモデル
     * @throws IllegalArgumentException バリデーションエラー
     * @throws CryptoManager.CryptoException 暗号化エラー
     */
    public void execute(@NonNull Password password) throws CryptoManager.CryptoException {
        Log.d(TAG, "Executing AddPasswordUseCase for: " + password.getTitle());

        // ビジネスバリデーション
        validatePassword(password);

        // パスワードを暗号化
        String encryptedPassword = repository.encryptPassword(password.getPassword());
        Log.d(TAG, "Password encrypted successfully");

        // PasswordEntryエンティティに変換
        PasswordEntry entry = new PasswordEntry(
                password.getId(),
                password.getTitle(),
                encryptedPassword
        );
        entry.setUsername(password.getUsername());
        entry.setUrl(password.getUrl());
        entry.setNotes(password.getNotes());
        entry.setCategory(password.getCategory());
        entry.setCreatedAt(password.getCreatedAt());
        entry.setUpdatedAt(password.getUpdatedAt());

        // データベースに保存
        repository.insert(entry);
        Log.i(TAG, "Password added successfully: " + password.getId());
    }

    /**
     * パスワードのバリデーション
     *
     * @param password パスワード
     * @throws IllegalArgumentException バリデーションエラー
     */
    private void validatePassword(@NonNull Password password) {
        if (password.getTitle() == null || password.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }

        if (password.getTitle().length() > 100) {
            throw new IllegalArgumentException("タイトルは100文字以内にしてください");
        }

        if (password.getPassword() == null || password.getPassword().isEmpty()) {
            throw new IllegalArgumentException("パスワードは必須です");
        }

        if (password.getPassword().length() > 500) {
            throw new IllegalArgumentException("パスワードは500文字以内にしてください");
        }

        if (password.getUsername() != null && password.getUsername().length() > 200) {
            throw new IllegalArgumentException("ユーザー名は200文字以内にしてください");
        }

        Log.d(TAG, "Validation passed");
    }
}
