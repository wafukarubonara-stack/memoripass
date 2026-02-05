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

import com.memoripass.data.model.PasswordEntry;
import com.memoripass.data.repository.PasswordRepository;

import java.util.List;

/**
 * パスワード一覧取得ユースケース
 *
 * <p>すべてのパスワードを取得する。
 * セキュリティ上、一覧では暗号化されたままのPasswordEntryを返す。
 * 個別の詳細表示時にGetPasswordUseCaseで復号する。</p>
 *
 * <p>設計判断:</p>
 * <ul>
 *   <li>一覧表示ではパスワードを復号しない（パフォーマンスとセキュリティ）</li>
 *   <li>タイトルとユーザー名のみ表示</li>
 *   <li>パスワードは詳細画面で復号</li>
 * </ul>
 *
 * @since 1.0
 */
public class GetAllPasswordsUseCase {

    private static final String TAG = "GetAllPasswordsUseCase";

    private final PasswordRepository repository;

    /**
     * コンストラクタ
     *
     * @param repository パスワードリポジトリ
     */
    public GetAllPasswordsUseCase(@NonNull PasswordRepository repository) {
        this.repository = repository;
    }

    /**
     * すべてのパスワードを取得（LiveData）
     *
     * <p>LiveDataを返すため、ViewModelから監視可能。
     * データベース変更時に自動的に更新される。</p>
     *
     * @return PasswordEntryのLiveDataリスト（暗号化済み）
     */
    public LiveData<List<PasswordEntry>> execute() {
        Log.d(TAG, "Executing GetAllPasswordsUseCase");
        return repository.getAllPasswords();
    }

    /**
     * カテゴリでフィルタしてパスワードを取得
     *
     * @param category カテゴリ名
     * @return PasswordEntryのLiveDataリスト（暗号化済み）
     */
    public LiveData<List<PasswordEntry>> executeByCategory(@NonNull String category) {
        Log.d(TAG, "Executing GetAllPasswordsUseCase with category: " + category);

        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("カテゴリは必須です");
        }

        return repository.getPasswordsByCategory(category);
    }

    /**
     * 検索クエリでパスワードを取得
     *
     * @param query 検索クエリ
     * @return PasswordEntryのLiveDataリスト（暗号化済み）
     */
    public LiveData<List<PasswordEntry>> executeSearch(@NonNull String query) {
        Log.d(TAG, "Executing GetAllPasswordsUseCase with query: " + query);

        if (query == null || query.isEmpty()) {
            // 空クエリの場合は全件取得
            return repository.getAllPasswords();
        }

        return repository.searchPasswords(query);
    }
}
