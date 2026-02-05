# Memoripass - アーキテクチャ設計書

**バージョン**: 1.0  
**作成日**: 2026年2月5日  
**対象**: Android 15+ (API Level 35+)

---

## 目次

1. [アーキテクチャ概要](#1-アーキテクチャ概要)
2. [レイヤー構成](#2-レイヤー構成)
3. [パッケージ構造](#3-パッケージ構造)
4. [主要クラス設計](#4-主要クラス設計)
5. [データフロー](#5-データフロー)
6. [実装優先順位](#6-実装優先順位)

---

## 1. アーキテクチャ概要

### 1.1 採用パターン

**Clean Architecture + MVVM + Repository Pattern**

```
┌─────────────────────────────────────────────────────┐
│              Presentation Layer (UI)                 │
│  ┌──────────────┐         ┌──────────────┐         │
│  │  Activity    │────────▶│  ViewModel   │         │
│  └──────────────┘         └──────────────┘         │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│              Domain Layer (Business Logic)           │
│  ┌──────────────┐         ┌──────────────┐         │
│  │  Use Cases   │────────▶│   Entities   │         │
│  └──────────────┘         └──────────────┘         │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│              Data Layer (Data Access)                │
│  ┌──────────────┐         ┌──────────────┐         │
│  │ Repository   │────────▶│ Data Source  │         │
│  └──────────────┘         └──────────────┘         │
└─────────────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│         Infrastructure Layer (Framework)             │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐│
│  │   Crypto     │  │  KeyStore    │  │  Database ││
│  └──────────────┘  └──────────────┘  └───────────┘│
└─────────────────────────────────────────────────────┘
```

### 1.2 設計原則

- **単一責任の原則**: 各クラスは1つの責任のみを持つ
- **依存性逆転の原則**: 上位レイヤーは下位レイヤーの抽象に依存
- **関心の分離**: UI、ビジネスロジック、データアクセスを分離
- **テスタビリティ**: 各レイヤーを独立してテスト可能

---

## 2. レイヤー構成

### 2.1 Presentation Layer (プレゼンテーション層)

**責務**: ユーザーインターフェースとユーザー入力の処理

- **Activity/Fragment**: UI表示とユーザー操作の受付
- **ViewModel**: UI状態の管理とビジネスロジックへの橋渡し
- **ViewState**: UI状態を表すデータクラス

### 2.2 Domain Layer (ドメイン層)

**責務**: ビジネスロジックの実装

- **Use Cases**: 具体的なビジネスロジック
- **Entities**: ビジネスエンティティ（データモデル）
- **Repository Interface**: データアクセスの抽象化

### 2.3 Data Layer (データ層)

**責務**: データの永続化と取得

- **Repository Implementation**: Repositoryインターフェースの実装
- **Data Source**: データソース（Room Database）
- **DAO**: データベースアクセスオブジェクト

### 2.4 Infrastructure Layer (インフラストラクチャ層)

**責務**: 暗号化、認証などの技術的機能

- **CryptoManager**: 暗号化/復号処理
- **KeyManager**: 鍵管理
- **AuthenticationManager**: 生体認証

---

## 3. パッケージ構造

### 3.1 推奨ディレクトリ構造

```
com.memoripass/
├── MainActivity.java                    # メインエントリーポイント
│
├── auth/                                # 認証関連
│   ├── AuthenticationManager.java      # [既存] 生体認証管理
│   └── AuthState.java                  # [新規] 認証状態
│
├── crypto/                              # 暗号化関連
│   ├── CryptoManager.java              # [既存] 暗号化処理
│   └── KeyManager.java                 # [既存] 鍵管理
│
├── data/                                # データ層
│   ├── model/                          # データモデル
│   │   ├── PasswordEntry.java         # [既存] パスワードエントリ
│   │   └── Category.java              # [新規] カテゴリ
│   │
│   ├── local/                          # ローカルデータソース
│   │   ├── AppDatabase.java           # [新規] Roomデータベース
│   │   ├── dao/                       # DAO層
│   │   │   ├── PasswordEntryDao.java # [新規] パスワードDAO
│   │   │   └── CategoryDao.java      # [新規] カテゴリDAO
│   │   │
│   │   └── entity/                    # Roomエンティティ
│   │       └── (PasswordEntry.javaを移動)
│   │
│   └── repository/                     # リポジトリ層
│       ├── PasswordRepository.java    # [新規] パスワードリポジトリ
│       └── CategoryRepository.java    # [新規] カテゴリリポジトリ
│
├── domain/                              # ドメイン層
│   ├── usecase/                        # ユースケース
│   │   ├── password/                  # パスワード操作
│   │   │   ├── AddPasswordUseCase.java
│   │   │   ├── UpdatePasswordUseCase.java
│   │   │   ├── DeletePasswordUseCase.java
│   │   │   ├── GetPasswordUseCase.java
│   │   │   └── GetAllPasswordsUseCase.java
│   │   │
│   │   └── auth/                      # 認証操作
│   │       ├── AuthenticateUseCase.java
│   │       └── LockAppUseCase.java
│   │
│   └── model/                          # ドメインモデル
│       └── Password.java              # [新規] パスワードドメインモデル
│
├── ui/                                  # UI層
│   ├── main/                           # メイン画面
│   │   ├── MainFragment.java          # [新規] メインFragment
│   │   └── MainViewModel.java         # [新規] メインViewModel
│   │
│   ├── list/                           # パスワード一覧
│   │   ├── PasswordListFragment.java  # [新規] 一覧Fragment
│   │   ├── PasswordListViewModel.java # [新規] 一覧ViewModel
│   │   └── PasswordListAdapter.java   # [新規] RecyclerViewアダプター
│   │
│   ├── detail/                         # パスワード詳細
│   │   ├── PasswordDetailFragment.java  # [新規] 詳細Fragment
│   │   └── PasswordDetailViewModel.java # [新規] 詳細ViewModel
│   │
│   ├── add/                            # パスワード追加
│   │   ├── AddPasswordFragment.java   # [新規] 追加Fragment
│   │   └── AddPasswordViewModel.java  # [新規] 追加ViewModel
│   │
│   ├── edit/                           # パスワード編集
│   │   ├── EditPasswordFragment.java  # [新規] 編集Fragment
│   │   └── EditPasswordViewModel.java # [新規] 編集ViewModel
│   │
│   ├── lock/                           # ロック画面
│   │   ├── LockFragment.java          # [新規] ロックFragment
│   │   └── LockViewModel.java         # [新規] ロックViewModel
│   │
│   └── common/                         # 共通UI
│       ├── BaseFragment.java          # [新規] Fragment基底クラス
│       ├── BaseViewModel.java         # [新規] ViewModel基底クラス
│       └── ViewState.java             # [新規] UI状態
│
└── util/                                # ユーティリティ
    ├── Constants.java                  # [新規] 定数
    ├── Logger.java                     # [新規] ロギング
    ├── DateFormatter.java              # [新規] 日付フォーマット
    ├── PasswordGenerator.java          # [新規] パスワード生成
    └── ValidationUtils.java            # [新規] 入力検証
```

### 3.2 現在の状態との差分

**既存ファイル** (5個):
- ✅ `MainActivity.java`
- ✅ `auth/AuthenticationManager.java`
- ✅ `crypto/CryptoManager.java`
- ✅ `crypto/KeyManager.java`
- ✅ `data/PasswordEntry.java`

**追加が必要なファイル** (約35個):
- データ層: 6個 (Database, DAO, Repository)
- ドメイン層: 8個 (UseCase, Model)
- UI層: 15個 (Fragment, ViewModel, Adapter)
- ユーティリティ層: 6個

---

## 4. 主要クラス設計

### 4.1 Data Layer

#### 4.1.1 AppDatabase.java

```java
@Database(
    entities = {PasswordEntry.class, Category.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    
    public abstract PasswordEntryDao passwordEntryDao();
    public abstract CategoryDao categoryDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "memoripass_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
```

**責務**: Roomデータベースのセットアップと管理

#### 4.1.2 PasswordEntryDao.java

```java
@Dao
public interface PasswordEntryDao {
    @Query("SELECT * FROM password_entries ORDER BY updated_at DESC")
    LiveData<List<PasswordEntry>> getAllPasswords();
    
    @Query("SELECT * FROM password_entries WHERE id = :id")
    LiveData<PasswordEntry> getPasswordById(String id);
    
    @Query("SELECT * FROM password_entries WHERE category = :category ORDER BY updated_at DESC")
    LiveData<List<PasswordEntry>> getPasswordsByCategory(String category);
    
    @Query("SELECT * FROM password_entries WHERE title LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%'")
    LiveData<List<PasswordEntry>> searchPasswords(String query);
    
    @Insert
    void insert(PasswordEntry entry);
    
    @Update
    void update(PasswordEntry entry);
    
    @Delete
    void delete(PasswordEntry entry);
    
    @Query("DELETE FROM password_entries WHERE id = :id")
    void deleteById(String id);
    
    @Query("DELETE FROM password_entries")
    void deleteAll();
}
```

**責務**: パスワードエントリのCRUD操作

#### 4.1.3 PasswordRepository.java

```java
public class PasswordRepository {
    private final PasswordEntryDao passwordDao;
    private final CryptoManager cryptoManager;
    private final LiveData<List<PasswordEntry>> allPasswords;
    
    public PasswordRepository(Context context) throws CryptoManager.CryptoException {
        AppDatabase db = AppDatabase.getInstance(context);
        passwordDao = db.passwordEntryDao();
        cryptoManager = new CryptoManager();
        allPasswords = passwordDao.getAllPasswords();
    }
    
    // 読み取り操作
    public LiveData<List<PasswordEntry>> getAllPasswords() {
        return allPasswords;
    }
    
    public LiveData<PasswordEntry> getPasswordById(String id) {
        return passwordDao.getPasswordById(id);
    }
    
    public LiveData<List<PasswordEntry>> getPasswordsByCategory(String category) {
        return passwordDao.getPasswordsByCategory(category);
    }
    
    public LiveData<List<PasswordEntry>> searchPasswords(String query) {
        return passwordDao.searchPasswords(query);
    }
    
    // 書き込み操作
    public void insert(PasswordEntry entry) {
        new Thread(() -> passwordDao.insert(entry)).start();
    }
    
    public void update(PasswordEntry entry) {
        new Thread(() -> passwordDao.update(entry)).start();
    }
    
    public void delete(PasswordEntry entry) {
        new Thread(() -> passwordDao.delete(entry)).start();
    }
    
    // 暗号化ヘルパー
    public String encryptPassword(String plainPassword) throws CryptoManager.CryptoException {
        return cryptoManager.encrypt(plainPassword);
    }
    
    public String decryptPassword(String encryptedPassword) throws CryptoManager.CryptoException {
        return cryptoManager.decrypt(encryptedPassword);
    }
}
```

**責務**: データソースと暗号化処理の統合

---

### 4.2 Domain Layer

#### 4.2.1 Password.java (ドメインモデル)

```java
public class Password {
    private final String id;
    private final String title;
    private final String username;
    private final String password;  // 復号済みパスワード（メモリ内のみ）
    private final String url;
    private final String notes;
    private final String category;
    private final long createdAt;
    private final long updatedAt;
    
    // Builderパターンで構築
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
        
        public Password build() {
            return new Password(this);
        }
    }
    
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
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUrl() { return url; }
    public String getNotes() { return notes; }
    public String getCategory() { return category; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
}
```

**責務**: ビジネスロジックで使用するパスワードモデル（復号済み）

#### 4.2.2 AddPasswordUseCase.java

```java
public class AddPasswordUseCase {
    private final PasswordRepository repository;
    
    public AddPasswordUseCase(PasswordRepository repository) {
        this.repository = repository;
    }
    
    public void execute(Password password) throws CryptoManager.CryptoException {
        // ビジネスバリデーション
        if (password.getTitle() == null || password.getTitle().isEmpty()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }
        
        if (password.getPassword() == null || password.getPassword().isEmpty()) {
            throw new IllegalArgumentException("パスワードは必須です");
        }
        
        // パスワードを暗号化
        String encryptedPassword = repository.encryptPassword(password.getPassword());
        
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
        
        // データベースに保存
        repository.insert(entry);
    }
}
```

**責務**: パスワード追加のビジネスロジック

---

### 4.3 Presentation Layer

#### 4.3.1 PasswordListViewModel.java

```java
public class PasswordListViewModel extends AndroidViewModel {
    private final PasswordRepository repository;
    private final LiveData<List<PasswordEntry>> allPasswords;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    
    public PasswordListViewModel(@NonNull Application application) throws CryptoManager.CryptoException {
        super(application);
        repository = new PasswordRepository(application);
        allPasswords = repository.getAllPasswords();
        viewState.setValue(ViewState.LOADING);
    }
    
    public LiveData<List<PasswordEntry>> getAllPasswords() {
        return allPasswords;
    }
    
    public LiveData<ViewState> getViewState() {
        return viewState;
    }
    
    public void searchPasswords(String query) {
        searchQuery.setValue(query);
    }
    
    public void deletePassword(PasswordEntry entry) {
        repository.delete(entry);
    }
    
    public void onPasswordsLoaded(List<PasswordEntry> passwords) {
        if (passwords == null || passwords.isEmpty()) {
            viewState.setValue(ViewState.EMPTY);
        } else {
            viewState.setValue(ViewState.SUCCESS);
        }
    }
    
    public void onError(String message) {
        viewState.setValue(ViewState.error(message));
    }
}
```

**責務**: パスワード一覧画面のUI状態管理

#### 4.3.2 ViewState.java

```java
public class ViewState {
    public enum State {
        LOADING,
        SUCCESS,
        EMPTY,
        ERROR
    }
    
    private final State state;
    private final String message;
    
    private ViewState(State state, String message) {
        this.state = state;
        this.message = message;
    }
    
    public static final ViewState LOADING = new ViewState(State.LOADING, null);
    public static final ViewState SUCCESS = new ViewState(State.SUCCESS, null);
    public static final ViewState EMPTY = new ViewState(State.EMPTY, null);
    
    public static ViewState error(String message) {
        return new ViewState(State.ERROR, message);
    }
    
    public State getState() {
        return state;
    }
    
    public String getMessage() {
        return message;
    }
}
```

**責務**: UI状態の表現

---

## 5. データフロー

### 5.1 パスワード追加フロー

```
[User Input]
    ↓
[AddPasswordFragment]
    ↓ (入力データ収集)
[AddPasswordViewModel]
    ↓ (UI検証)
[AddPasswordUseCase]
    ↓ (ビジネスバリデーション)
[PasswordRepository]
    ↓ (暗号化)
[CryptoManager]
    ↓ (暗号化実行)
[PasswordEntryDao]
    ↓ (データベース保存)
[Room Database]
```

### 5.2 パスワード取得フロー

```
[User Action]
    ↓
[PasswordListFragment]
    ↓ (データ要求)
[PasswordListViewModel]
    ↓ (LiveData監視)
[PasswordRepository]
    ↓ (データ取得)
[PasswordEntryDao]
    ↓ (クエリ実行)
[Room Database]
    ↓ (LiveData更新)
[PasswordListViewModel]
    ↓ (UI更新)
[PasswordListFragment]
    ↓ (RecyclerView更新)
[User]
```

### 5.3 認証フロー

```
[App Launch]
    ↓
[MainActivity]
    ↓ (onCreate)
[AuthenticationManager]
    ↓ (認証チェック)
[BiometricPrompt]
    ↓ (生体認証)
[User Biometric]
    ↓ (認証成功)
[MainActivity]
    ↓ (showMainContent)
[PasswordListFragment]
```

---

## 6. 実装優先順位

### フェーズ1: データ層の完成 (優先度: 最高)

1. **AppDatabase.java** - Roomデータベース設定
2. **PasswordEntryDao.java** - CRUD操作
3. **CategoryDao.java** - カテゴリ操作
4. **PasswordRepository.java** - リポジトリパターン実装

### フェーズ2: ドメイン層の実装 (優先度: 高)

5. **Password.java** - ドメインモデル
6. **AddPasswordUseCase.java** - パスワード追加
7. **UpdatePasswordUseCase.java** - パスワード更新
8. **DeletePasswordUseCase.java** - パスワード削除
9. **GetPasswordUseCase.java** - パスワード取得
10. **GetAllPasswordsUseCase.java** - 一覧取得

### フェーズ3: UI層の基礎 (優先度: 高)

11. **BaseFragment.java** - Fragment基底クラス
12. **BaseViewModel.java** - ViewModel基底クラス
13. **ViewState.java** - UI状態モデル
14. **PasswordListFragment.java** - 一覧画面
15. **PasswordListViewModel.java** - 一覧ViewModel
16. **PasswordListAdapter.java** - RecyclerViewアダプター

### フェーズ4: UI層の拡張 (優先度: 中)

17. **AddPasswordFragment.java** - 追加画面
18. **AddPasswordViewModel.java** - 追加ViewModel
19. **EditPasswordFragment.java** - 編集画面
20. **EditPasswordViewModel.java** - 編集ViewModel
21. **PasswordDetailFragment.java** - 詳細画面
22. **PasswordDetailViewModel.java** - 詳細ViewModel

### フェーズ5: ユーティリティ (優先度: 中)

23. **Constants.java** - 定数定義
24. **Logger.java** - ロギング
25. **DateFormatter.java** - 日付フォーマット
26. **PasswordGenerator.java** - パスワード生成
27. **ValidationUtils.java** - 入力検証

### フェーズ6: UI/UXの改善 (優先度: 低)

28. レイアウトXMLファイルの作成
29. アニメーション・トランジション
30. エラーハンドリングの強化
31. ユニットテストの追加

---

## 次のステップ

1. ✅ **現状確認完了** - 既存コードの把握
2. ⬜ **フェーズ1開始** - データ層の実装
3. ⬜ **フェーズ2開始** - ドメイン層の実装
4. ⬜ **フェーズ3開始** - UI層の実装

---

**作成者**: Claude + wafukarubonara-stack  
**ライセンス**: Apache License 2.0
