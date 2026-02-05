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

package com.memoripass.domain.model;

import androidx.annotation.NonNull;

import java.util.UUID;

/**
 * パスワードドメインモデル
 *
 * <p>ビジネスロジックで使用するパスワードモデル。
 * データベースのPasswordEntry（暗号化済み）とは異なり、
 * このクラスは復号済みの平文パスワードを保持する。</p>
 *
 * <p>セキュリティ注意:</p>
 * <ul>
 *   <li>メモリ内でのみ使用</li>
 *   <li>使用後は速やかに破棄</li>
 *   <li>ログ出力禁止</li>
 * </ul>
 *
 * @since 1.0
 */
public class Password {

    private final String id;
    private final String title;
    private final String username;
    private final String password;  // 復号済み平文パスワード
    private final String url;
    private final String notes;
    private final String category;
    private final long createdAt;
    private final long updatedAt;

    /**
     * プライベートコンストラクタ（Builderパターン）
     */
    private Password(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.username = builder.username;
        this.password = builder.password;
        this.url = builder.url;
        this.notes = builder.notes;
        this.category = builder.category;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
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
    public String getPassword() {
        return password;
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

    /**
     * Builderクラス
     */
    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String title;
        private String username;
        private String password;
        private String url;
        private String notes;
        private String category;
        private long createdAt = System.currentTimeMillis();
        private long updatedAt = System.currentTimeMillis();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        /**
         * Passwordオブジェクトを構築
         *
         * @return Password
         * @throws IllegalArgumentException 必須フィールドが未設定
         */
        public Password build() {
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Title is required");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            return new Password(this);
        }
    }

    @Override
    public String toString() {
        // セキュリティ: パスワードは出力しない
        return "Password{" +
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
