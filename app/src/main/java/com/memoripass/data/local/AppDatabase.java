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

package com.memoripass.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.memoripass.data.local.dao.CategoryDao;
import com.memoripass.data.local.dao.PasswordEntryDao;
import com.memoripass.data.model.Category;
import com.memoripass.data.model.PasswordEntry;

/**
 * Roomデータベース
 *
 * <p>アプリのメインデータベース。
 * パスワードエントリとカテゴリを管理する。</p>
 *
 * <p>セキュリティ機能:</p>
 * <ul>
 *   <li>端末内のみに保存（外部バックアップ無効）</li>
 *   <li>暗号化されたパスワードのみ保存</li>
 *   <li>シングルトンパターンでインスタンス管理</li>
 * </ul>
 *
 * @since 1.0
 */
@Database(
    entities = {PasswordEntry.class, Category.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "memoripass_database";
    private static volatile AppDatabase INSTANCE;

    /**
     * PasswordEntryDaoを取得
     *
     * @return PasswordEntryDao
     */
    public abstract PasswordEntryDao passwordEntryDao();

    /**
     * CategoryDaoを取得
     *
     * @return CategoryDao
     */
    public abstract CategoryDao categoryDao();

    /**
     * データベースインスタンスを取得（シングルトン）
     *
     * @param context アプリケーションコンテキスト
     * @return データベースインスタンス
     */
    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    // マイグレーション失敗時は再構築（開発中のみ）
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * データベースインスタンスを破棄（テスト用）
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
