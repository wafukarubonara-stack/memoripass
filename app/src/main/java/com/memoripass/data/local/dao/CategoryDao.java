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

import com.memoripass.data.model.Category;

import java.util.List;

/**
 * カテゴリDAO
 *
 * <p>カテゴリテーブルへのアクセスを提供する。</p>
 *
 * @since 1.0
 */
@Dao
public interface CategoryDao {

    /**
     * すべてのカテゴリを取得
     *
     * @return カテゴリのLiveDataリスト
     */
    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();

    /**
     * IDでカテゴリを取得
     *
     * @param id カテゴリID
     * @return カテゴリのLiveData
     */
    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    LiveData<Category> getCategoryById(String id);

    /**
     * カテゴリを挿入
     *
     * @param category カテゴリ
     */
    @Insert
    void insert(Category category);

    /**
     * カテゴリを更新
     *
     * @param category カテゴリ
     */
    @Update
    void update(Category category);

    /**
     * カテゴリを削除
     *
     * @param category カテゴリ
     */
    @Delete
    void delete(Category category);

    /**
     * IDでカテゴリを削除
     *
     * @param id カテゴリID
     */
    @Query("DELETE FROM categories WHERE id = :id")
    void deleteById(String id);
}
