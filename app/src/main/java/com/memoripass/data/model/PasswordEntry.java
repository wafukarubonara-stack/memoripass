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

package com.memoripass.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * パスワードエントリのデータモデル
 *
 * <p>1つのパスワードエントリを表すエンティティクラス。
 * Roomデータベースのテーブルとして使用される。</p>
 *
 * <p>フィールド:</p>
 * <ul>
 *   <li>id: エントリの一意識別子（UUID）</li>
 *   <li>title: タイトル（例: "Gmail", "Amazon"）</li>
 *   <li>username: ユーザー名（例: "user@example.com"）</li>
 *   <li>encryptedPassword: 暗号化されたパスワード（Base64エンコード）</li>
 *   <li>url: URL（例: "https://gmail.com"）</li>
 *   <li>notes: メモ</li>
 *   <li>category: カテゴリ（例: "仕事", "プライベート"）</li>
 *   <li>createdAt: 作成日時（UNIXタイムスタンプ、ミリ秒）</li>
 *   <li>updatedAt: 更新日時（UNIXタイムスタンプ、ミリ秒）</li>
 * </ul>
 *
 * @since 1.0
 */
@Entity(tableName = "password_entries")
public class PasswordEntry {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "encrypted_password")
    private String encryptedPassword;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    /**
     * コンストラクタ
     *
     * @param id エントリID（UUID推奨）
     * @param title タイトル
     * @param encryptedPassword 暗号化されたパスワード
     */
    public PasswordEntry(@NonNull String id, @NonNull String title, @NonNull String encryptedPassword) {
        this.id = id;
        this.title = title;
        this.encryptedPassword = encryptedPassword;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    @NonNull
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getUrl() {
        return url;
    }

    public String getNotes() {
        return notes;
    }

    public String getCategory() {
        return category;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // Setters

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncryptedPassword(@NonNull String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 更新日時を現在時刻に設定
     */
    public void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
