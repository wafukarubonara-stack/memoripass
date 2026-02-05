# Memoripass - 実装ガイド (フェーズ1: データ層)

**対象フェーズ**: フェーズ1 - データ層の完成  
**作成日**: 2026年2月5日  
**推定工数**: 4-6時間

---

## 📋 概要

このガイドでは、Memoripassのデータ層を実装します。Clean Architectureの基盤となる部分で、以下を含みます：

- Room Databaseのセットアップ
- DAO（Data Access Object）の実装
- Repositoryパターンの実装

---

## 🎯 実装項目

### ✅ 既に完成しているもの
- ✅ `PasswordEntry.java` - Roomエンティティ
- ✅ `CryptoManager.java` - 暗号化処理
- ✅ `KeyManager.java` - 鍵管理

### 🔨 これから実装するもの
1. `Category.java` - カテゴリエンティティ
2. `AppDatabase.java` - Roomデータベース
3. `PasswordEntryDao.java` - パスワードDAO
4. `CategoryDao.java` - カテゴリDAO
5. `PasswordRepository.java` - リポジトリ
6. `CategoryRepository.java` - カテゴリリポジトリ

---

## 📦 1. カテゴリエンティティの作成

### ファイルパス
`app/src/main/java/com/memoripass/data/model/Category.java`

### 実装内容

```java
/*
 * Copyright 2026 wafukarubonara-stack
 * Licensed under the Apache License, Version 2.0
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
     */
    public Category(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.colorCode = 0xFF2196F3; // デフォルト: 青
        this.iconResId = 0;
    }

    // Getters and Setters
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public long getCreatedAt() {
        return createdAt;
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
                ", createdAt=" + createdAt +
                '}';
    }
}
```

### テストケース（参考）

```java
@Test
public void testCategoryCreation() {
    Category category = new Category("cat_1", "仕事");
    assertEquals("cat_1", category.getId());
    assertEquals("仕事", category.getName());
    assertTrue(category.getCreatedAt() > 0);
}
```

---

## 📦 2. AppDatabaseの作成

### ファイルパス
`app/src/main/java/com/memoripass/data/local/AppDatabase.java`

### 実装内容

```java
/*
 * Copyright 2026 wafukarubonara-stack
 * Licensed under the Apache License, Version 2.0
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
     */
    public abstract PasswordEntryDao passwordEntryDao();

    /**
     * CategoryDaoを取得
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
```

---

## 📦 3. PasswordEntryDaoの作成

### ファイルパス
`app/src/main/java/com/memoripass/data/local/dao/PasswordEntryDao.java`

### 実装内容

```java
/*
 * Copyright 2026 wafukarubonara-stack
 * Licensed under the Apache License, Version 2.0
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
```

---

## 📦 4. CategoryDaoの作成

### ファイルパス
`app/src/main/java/com/memoripass/data/local/dao/CategoryDao.java`

### 実装内容

```java
/*
 * Copyright 2026 wafukarubonara-stack
 * Licensed under the Apache License, Version 2.0
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
```

---

## 📦 5. PasswordRepositoryの作成

### ファイルパス
`app/src/main/java/com/memoripass/data/repository/PasswordRepository.java`

### 実装内容

```java
/*
 * Copyright 2026 wafukarubonara-stack
 * Licensed under the Apache License, Version 2.0
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
```

---

## 📦 6. PasswordEntry.javaの移動

### 現在の場所
`app/src/main/java/com/memoripass/data/PasswordEntry.java`

### 移動先
`app/src/main/java/com/memoripass/data/model/PasswordEntry.java`

### 変更内容

パッケージ宣言を変更：
```java
package com.memoripass.data.model;
```

---

## 🧪 動作確認

### 1. ビルド確認

```bash
./gradlew build
```

エラーがないことを確認。

### 2. 簡単なテスト（MainActivityに追加）

```java
// テスト用コード（onCreate内）
try {
    AppDatabase database = AppDatabase.getInstance(this);
    PasswordRepository repository = new PasswordRepository(this);
    
    Log.d("TEST", "Database initialized successfully");
    Log.d("TEST", "Repository initialized successfully");
    
} catch (CryptoManager.CryptoException e) {
    Log.e("TEST", "Initialization failed", e);
}
```

### 3. ログで確認

Logcatで以下のログを確認：
- `KeyStore initialized successfully`
- `Master key generated successfully`
- `PasswordRepository initialized`

---

## ✅ チェックリスト

フェーズ1完了前に以下を確認：

- [ ] すべてのファイルがコンパイルエラーなく作成されている
- [ ] `PasswordEntry.java`が正しく移動されている
- [ ] `AppDatabase.java`がシングルトンで動作する
- [ ] DAOメソッドがすべて定義されている
- [ ] `PasswordRepository`が暗号化と統合されている
- [ ] ビルドが成功する
- [ ] ログで初期化が確認できる

---

## 📝 次のステップ

フェーズ1完了後：

1. **フェーズ2**: ドメイン層の実装
   - Use Caseクラスの作成
   - ビジネスロジックの実装

2. **フェーズ3**: UI層の実装
   - ViewModelの作成
   - Fragmentの作成
   - レイアウトXMLの作成

---

**作成日**: 2026年2月5日  
**作成者**: Claude + wafukarubonara-stack  
**推定所要時間**: 4-6時間
