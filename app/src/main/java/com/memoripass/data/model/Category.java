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
 * カテゴリエンティティ
 *
 * <p>パスワードを分類するためのカテゴリを表すエンティティクラス。
 * Roomデータベースのテーブルとして使用される。</p>
 *
 * <p>フィールド:</p>
 * <ul>
 *   <li>id: カテゴリの一意識別子（UUID）</li>
 *   <li>name: カテゴリ名（例: "仕事", "プライベート"）</li>
 *   <li>colorCode: 色コード（ARGB形式）</li>
 *   <li>iconResId: アイコンリソースID</li>
 *   <li>createdAt: 作成日時（UNIXタイムスタンプ、ミリ秒）</li>
 * </ul>
 *
 * @since 1.0
 */
@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color_code")
    private int colorCode;

    @ColumnInfo(name = "icon_res_id")
    private int iconResId;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    /**
     * コンストラクタ
     *
     * @param id カテゴリID（UUID推奨）
     * @param name カテゴリ名
     */
    public Category(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.colorCode = 0xFF2196F3; // デフォルト: 青
        this.iconResId = 0;
    }

    // Getters

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getColorCode() {
        return colorCode;
    }

    public int getIconResId() {
        return iconResId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // Setters

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", colorCode=" + colorCode +
                ", iconResId=" + iconResId +
                ", createdAt=" + createdAt +
                '}';
    }
}
