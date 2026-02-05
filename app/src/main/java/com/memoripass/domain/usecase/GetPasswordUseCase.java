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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.model.Password;

/**
 * パスワード取得ユースケース
 *
 * <p>IDでパスワードを取得し、復号する。</p>
 *
 * <p>処理フロー:</p>
 * <ol>
 *   <li>データベースからPasswordEntryを取得</li>
 *   <li>暗号化されたパスワードを復号</li>
 *   <li>Passwordドメインモデルに変換</li>
 * </ol>
 *
 * @since 1.0
 */
public class GetPasswordUseCase {

    private static final String TAG = "GetPasswordUseCase";

    private final PasswordRepository repository;

    /**
     * コンストラクタ
     *
     * @param repository パスワードリポジトリ
     */
    public GetPasswordUseCase(@NonNull PasswordRepository repository) {
        this.repository = repository;
    }

    /**
     * パスワードを取得（LiveData）
     *
     * <p>LiveDataを返すため、ViewModelから監視可能。
     * データベース変更時に自動的に更新される。</p>
     *
     * @param passwordId パスワードID
     * @return パスワードドメインモデルのLiveData
     */
    public LiveData<Password> execute(@NonNull String passwordId) {
        Log.d(TAG, "Executing GetPasswordUseCase for: " + passwordId);

        // バリデーション
        if (passwordId == null || passwordId.isEmpty()) {
            throw new IllegalArgumentException("パスワードIDは必須です");
        }

        // PasswordEntryをPasswordに変換（LiveData変換）
        return Transformations.map(
                repository.getPasswordById(passwordId),
                this::convertToPassword
        );
    }

    /**
     * PasswordEntryをPasswordドメインモデルに変換
     *
     * @param entry PasswordEntry
     * @return Passwordドメインモデル（復号済み）
     */
    private Password convertToPassword(PasswordEntry entry) {
        if (entry == null) {
            return null;
        }

        try {
            // パスワードを復号
            String decryptedPassword = repository.decryptPassword(entry.getEncryptedPassword());
            Log.d(TAG, "Password decrypted successfully");

            // Passwordドメインモデルに変換
            return new Password.Builder()
                    .id(entry.getId())
                    .title(entry.getTitle())
                    .username(entry.getUsername())
                    .password(decryptedPassword)
                    .url(entry.getUrl())
                    .notes(entry.getNotes())
                    .category(entry.getCategory())
                    .createdAt(entry.getCreatedAt())
                    .updatedAt(entry.getUpdatedAt())
                    .build();

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to decrypt password", e);
            return null;
        }
    }
}
