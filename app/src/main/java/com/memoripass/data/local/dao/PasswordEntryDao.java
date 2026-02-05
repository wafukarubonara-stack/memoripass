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

package com.memoripass.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.memoripass.data.model.PasswordEntry;

import java.util.List;

/**
 * パスワードエントリDAO
 *
 * <p>パスワードエントリテーブルへのアクセスを提供する。
 * RoomがSQL文を自動生成し、実装コードを生成する。</p>
 *
 * <p>LiveDataを返すメソッドは自動的にバックグラウンドで実行され、
 * データ変更時にUIが自動更新される。</p>
 *
 * @since 1.0
 */
@Dao
public interface PasswordEntryDao {

    /**
     * すべてのパスワードエントリを取得（更新日時降順）
     *
     * @return パスワードエントリのLiveDataリスト
     */
    @Query("SELECT * FROM password_entries ORDER BY updated_at DESC")
    LiveData<List<PasswordEntry>> getAllPasswords();

    /**
     * IDでパスワードエントリを取得
     *
     * @param id エントリID
     * @return パスワードエントリのLiveData
     */
    @Query("SELECT * FROM password_entries WHERE id = :id LIMIT 1")
    LiveData<PasswordEntry> getPasswordById(String id);

    /**
     * カテゴリでパスワードエントリを取得
     *
     * @param category カテゴリ名
     * @return パスワードエントリのLiveDataリスト
     */
    @Query("SELECT * FROM password_entries WHERE category = :category ORDER BY updated_at DESC")
    LiveData<List<PasswordEntry>> getPasswordsByCategory(String category);

    /**
     * パスワードを検索（タイトル・ユーザー名で部分一致）
     *
     * @param query 検索クエリ
     * @return パスワードエントリのLiveDataリスト
     */
    @Query("SELECT * FROM password_entries WHERE title LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' ORDER BY updated_at DESC")
    LiveData<List<PasswordEntry>> searchPasswords(String query);

    /**
     * パスワードエントリを挿入
     *
     * @param entry パスワードエントリ
     */
    @Insert
    void insert(PasswordEntry entry);

    /**
     * パスワードエントリを更新
     *
     * @param entry パスワードエントリ
     */
    @Update
    void update(PasswordEntry entry);

    /**
     * パスワードエントリを削除
     *
     * @param entry パスワードエントリ
     */
    @Delete
    void delete(PasswordEntry entry);

    /**
     * IDでパスワードエントリを削除
     *
     * @param id エントリID
     */
    @Query("DELETE FROM password_entries WHERE id = :id")
    void deleteById(String id);

    /**
     * すべてのパスワードエントリを削除
     */
    @Query("DELETE FROM password_entries")
    void deleteAll();

    /**
     * パスワードエントリの総数を取得
     *
     * @return エントリ数
     */
    @Query("SELECT COUNT(*) FROM password_entries")
    int getPasswordCount();
}
