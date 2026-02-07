# Memoripass アーキテクチャドキュメント

## 📐 アーキテクチャ概要

Memoripassは、**Clean Architecture** + **MVVM** + **Repository Pattern**を採用した、保守性とテスタビリティに優れた設計となっています。

---

## 🏗️ レイヤー構成

```
┌─────────────────────────────────────────────────┐
│                Presentation Layer               │
│  ┌──────────────────────────────────────────┐  │
│  │ UI Components (Activity/Fragment)        │  │
│  │ - MainActivity                           │  │
│  │ - PasswordListFragment                   │  │
│  │ - AddPasswordFragment                    │  │
│  │ - PasswordDetailFragment                 │  │
│  │ - EditPasswordFragment                   │  │
│  └──────────────────┬───────────────────────┘  │
│                     │                           │
│  ┌──────────────────▼───────────────────────┐  │
│  │ ViewModels (MVVM)                        │  │
│  │ - BaseViewModel                          │  │
│  │ - PasswordListViewModel                  │  │
│  │ - AddPasswordViewModel                   │  │
│  │ - PasswordDetailViewModel                │  │
│  │ - EditPasswordViewModel                  │  │
│  └──────────────────┬───────────────────────┘  │
│                     │                           │
│  ┌──────────────────▼───────────────────────┐  │
│  │ ViewState & Adapters                     │  │
│  │ - ViewState (UI状態管理)                 │  │
│  │ - PasswordListAdapter                    │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────┼───────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────┐
│                  Domain Layer                   │
│  ┌──────────────────────────────────────────┐  │
│  │ Use Cases (Business Logic)               │  │
│  │ - AddPasswordUseCase                     │  │
│  │ - UpdatePasswordUseCase                  │  │
│  │ - DeletePasswordUseCase                  │  │
│  │ - GetPasswordUseCase                     │  │
│  │ - GetAllPasswordsUseCase                 │  │
│  └──────────────────┬───────────────────────┘  │
│                     │                           │
│  ┌──────────────────▼───────────────────────┐  │
│  │ Domain Models                            │  │
│  │ - Password (復号済み・ビジネスロジック用) │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────┼───────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────┐
│                   Data Layer                    │
│  ┌──────────────────────────────────────────┐  │
│  │ Repository (データアクセスの抽象化)        │  │
│  │ - PasswordRepository                     │  │
│  │ - CategoryRepository                     │  │
│  └──────────────────┬───────────────────────┘  │
│                     │                           │
│  ┌──────────────────▼───────────────────────┐  │
│  │ Data Access Objects (DAO)                │  │
│  │ - PasswordEntryDao (Room)                │  │
│  │ - CategoryDao (Room)                     │  │
│  └──────────────────┬───────────────────────┘  │
│                     │                           │
│  ┌──────────────────▼───────────────────────┐  │
│  │ Database                                 │  │
│  │ - AppDatabase (Room Singleton)           │  │
│  └──────────────────┬───────────────────────┘  │
│                     │                           │
│  ┌──────────────────▼───────────────────────┐  │
│  │ Data Models (Entity)                     │  │
│  │ - PasswordEntry (暗号化済み)              │  │
│  │ - Category                               │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────┼───────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────┐
│              Infrastructure Layer               │
│  ┌──────────────────────────────────────────┐  │
│  │ Security Components                      │  │
│  │ - CryptoManager (AES-256-GCM)            │  │
│  │ - KeyManager (Android KeyStore)          │  │
│  │ - AuthenticationManager (Biometric)      │  │
│  └──────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────┐  │
│  │ Utility Components                       │  │
│  │ - PasswordGenerator                      │  │
│  │ - PasswordStrengthChecker                │  │
│  │ - Constants                              │  │
│  │ - ValidationUtils                        │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

---

## 📦 パッケージ構成

```
com.memoripass/
├── MainActivity.java                    # エントリーポイント
│
├── auth/                                # 認証関連
│   └── AuthenticationManager.java      # 生体認証管理
│
├── crypto/                              # 暗号化関連
│   ├── CryptoManager.java               # AES-256-GCM暗号化
│   └── KeyManager.java                  # Android KeyStore管理
│
├── data/                                # データ層
│   ├── local/                           # ローカルストレージ
│   │   ├── AppDatabase.java             # Room Database
│   │   └── dao/                         # Data Access Objects
│   │       ├── PasswordEntryDao.java    # パスワードDAO
│   │       └── CategoryDao.java         # カテゴリDAO
│   │
│   ├── model/                           # データモデル
│   │   ├── PasswordEntry.java           # パスワードエンティティ（暗号化済み）
│   │   └── Category.java                # カテゴリエンティティ
│   │
│   └── repository/                      # リポジトリ
│       ├── PasswordRepository.java      # パスワードリポジトリ
│       └── CategoryRepository.java      # カテゴリリポジトリ
│
├── domain/                              # ドメイン層
│   ├── model/                           # ドメインモデル
│   │   └── Password.java                # パスワード（復号済み）
│   │
│   └── usecase/                         # ユースケース
│       ├── AddPasswordUseCase.java      # パスワード追加
│       ├── UpdatePasswordUseCase.java   # パスワード更新
│       ├── DeletePasswordUseCase.java   # パスワード削除
│       ├── GetPasswordUseCase.java      # パスワード取得
│       └── GetAllPasswordsUseCase.java  # 一覧取得
│
├── ui/                                  # プレゼンテーション層
│   ├── common/                          # 共通UI
│   │   ├── BaseFragment.java            # Fragment基底クラス
│   │   ├── BaseViewModel.java           # ViewModel基底クラス
│   │   └── ViewState.java               # UI状態管理
│   │
│   ├── list/                            # 一覧画面
│   │   ├── PasswordListFragment.java    # パスワード一覧Fragment
│   │   ├── PasswordListViewModel.java   # パスワード一覧ViewModel
│   │   └── PasswordListAdapter.java     # RecyclerView Adapter
│   │
│   ├── add/                             # 追加画面
│   │   ├── AddPasswordFragment.java     # パスワード追加Fragment
│   │   └── AddPasswordViewModel.java    # パスワード追加ViewModel
│   │
│   ├── detail/                          # 詳細画面
│   │   ├── PasswordDetailFragment.java  # パスワード詳細Fragment
│   │   └── PasswordDetailViewModel.java # パスワード詳細ViewModel
│   │
│   └── edit/                            # 編集画面
│       ├── EditPasswordFragment.java    # パスワード編集Fragment
│       └── EditPasswordViewModel.java   # パスワード編集ViewModel
│
└── util/                                # ユーティリティ
    ├── PasswordGenerator.java           # パスワード生成
    ├── PasswordStrengthChecker.java     # パスワード強度チェック
    ├── Constants.java                   # 定数定義
    └── ValidationUtils.java             # バリデーション
```

---

## 🔄 データフロー

### パスワード追加フロー

```
User Input
    │
    ▼
AddPasswordFragment
    │ (title, username, password, ...)
    ▼
AddPasswordViewModel
    │ (validate input)
    ▼
AddPasswordUseCase
    │ (business logic)
    ▼
PasswordRepository
    │ (encrypt password)
    ▼
CryptoManager.encrypt()
    │ (AES-256-GCM)
    ▼
KeyManager.getMasterKey()
    │ (Android KeyStore)
    ▼
PasswordRepository.insert()
    │ (save encrypted data)
    ▼
PasswordEntryDao.insert()
    │ (Room SQL)
    ▼
AppDatabase
    │ (SQLite)
    ▼
LiveData Update
    │ (observer notification)
    ▼
PasswordListViewModel
    │ (UI update)
    ▼
PasswordListAdapter
    │ (RecyclerView refresh)
    ▼
User sees new entry
```

### パスワード表示フロー

```
User Click
    │
    ▼
PasswordListFragment
    │ (passwordEntry.getId())
    ▼
PasswordDetailFragment
    │
    ▼
PasswordDetailViewModel.loadPassword()
    │
    ▼
GetPasswordUseCase.execute()
    │
    ▼
PasswordRepository.getPasswordById()
    │
    ▼
PasswordEntryDao.getPasswordById()
    │ (Room query)
    ▼
LiveData<PasswordEntry>
    │ (encrypted data)
    ▼
Repository.decryptPassword()
    │
    ▼
CryptoManager.decrypt()
    │ (AES-256-GCM)
    ▼
Password Domain Model
    │ (decrypted)
    ▼
LiveData Transformation
    │
    ▼
PasswordDetailFragment
    │ (display)
    ▼
User sees decrypted password
```

---

## 🔐 セキュリティアーキテクチャ

### 暗号化フロー

```
Plaintext Password
    │
    ▼
CryptoManager.encrypt()
    │
    ├─► KeyManager.getMasterKey()
    │   │
    │   └─► Android KeyStore
    │       │
    │       └─► StrongBox (Titan M2)
    │           └─► Hardware-backed Key
    │
    ├─► Generate IV (12 bytes, SecureRandom)
    │
    ├─► AES-256-GCM Encryption
    │   ├─ Key: 256 bits
    │   ├─ IV: 96 bits
    │   └─ Auth Tag: 128 bits
    │
    └─► Base64 Encode
        │
        └─► Encrypted Data
            │
            └─► Store in Room Database
```

### 認証フロー

```
App Launch
    │
    ▼
MainActivity.onCreate()
    │
    ▼
AuthenticationManager.isAuthenticated()
    │
    ├─► false
    │   │
    │   └─► Show Lock Screen
    │       │
    │       └─► Request Biometric Auth
    │           │
    │           ├─► Success → Set authenticated = true
    │           ├─► Failed → Show error, retry
    │           └─► Cancelled → Exit app
    │
    └─► true
        │
        └─► Show Main Content
            │
            └─► PasswordListFragment
```

---

## 📊 実装状況

### Phase 0: プロジェクト基盤 ✅

| コンポーネント | ファイル | 状態 |
|--------------|---------|------|
| 暗号化 | CryptoManager.java | ✅ 完了 |
| 鍵管理 | KeyManager.java | ✅ 完了 |
| 認証 | AuthenticationManager.java | ✅ 完了 |
| エントリーポイント | MainActivity.java | ✅ 完了 |

### Phase 1: データ層 ✅

| コンポーネント | ファイル | 状態 |
|--------------|---------|------|
| Database | AppDatabase.java | ✅ 完了 |
| DAO | PasswordEntryDao.java | ✅ 完了 |
| DAO | CategoryDao.java | ✅ 完了 |
| Entity | PasswordEntry.java | ✅ 完了 |
| Entity | Category.java | ✅ 完了 |
| Repository | PasswordRepository.java | ✅ 完了 |

### Phase 2: ドメイン層 ✅

| コンポーネント | ファイル | 状態 |
|--------------|---------|------|
| Domain Model | Password.java | ✅ 完了 |
| Use Case | AddPasswordUseCase.java | ✅ 完了 |
| Use Case | UpdatePasswordUseCase.java | ✅ 完了 |
| Use Case | DeletePasswordUseCase.java | ✅ 完了 |
| Use Case | GetPasswordUseCase.java | ✅ 完了 |
| Use Case | GetAllPasswordsUseCase.java | ✅ 完了 |

### Phase 3: UI基礎 ✅

| コンポーネント | ファイル | 状態 |
|--------------|---------|------|
| Base Classes | BaseFragment.java | ✅ 完了 |
| Base Classes | BaseViewModel.java | ✅ 完了 |
| State Management | ViewState.java | ✅ 完了 |
| List Screen | PasswordListFragment.java | ✅ 完了 |
| List Screen | PasswordListViewModel.java | ✅ 完了 |
| Adapter | PasswordListAdapter.java | ✅ 完了 |

### Phase 4: UI拡張 ✅

| コンポーネント | ファイル | 状態 |
|--------------|---------|------|
| Add Screen | AddPasswordFragment.java | ✅ 完了 |
| Add Screen | AddPasswordViewModel.java | ✅ 完了 |
| Detail Screen | PasswordDetailFragment.java | ✅ 完了 |
| Detail Screen | PasswordDetailViewModel.java | ✅ 完了 |
| Edit Screen | EditPasswordFragment.java | ✅ 完了 |
| Edit Screen | EditPasswordViewModel.java | ✅ 完了 |

### Phase 5: ユーティリティ 🔄

| コンポーネント | ファイル | 状態 |
|--------------|---------|------|
| Password Gen | PasswordGenerator.java | ✅ 完了 |
| Strength Check | PasswordStrengthChecker.java | ✅ 完了 |
| Constants | Constants.java | ⏳ 未実装 |
| Validation | ValidationUtils.java | ⏳ 未実装 |

### Phase 6: UI/UX改善 📋

| コンポーネント | 内容 | 状態 |
|--------------|------|------|
| Custom Layouts | XMLレイアウト作成 | 📋 予定 |
| Material Design | MD3対応 | 📋 予定 |
| Animations | トランジション | 📋 予定 |
| Dark Mode | ダークモード | 📋 予定 |

---

## 🎯 設計原則

### 1. 関心の分離 (Separation of Concerns)

各レイヤーは明確な責務を持ち、他のレイヤーに依存しない：

- **Presentation**: UIとユーザー入力の処理
- **Domain**: ビジネスロジック
- **Data**: データアクセスと永続化
- **Infrastructure**: 外部システムとの統合

### 2. 依存性逆転 (Dependency Inversion)

上位レイヤーは下位レイヤーの抽象に依存：

```java
// ViewModel → UseCase → Repository (抽象に依存)
public class PasswordListViewModel {
    private final GetAllPasswordsUseCase useCase;
    
    public PasswordListViewModel(GetAllPasswordsUseCase useCase) {
        this.useCase = useCase;  // インターフェースではないが、抽象化されている
    }
}
```

### 3. 単一責任 (Single Responsibility)

各クラスは1つの責務のみを持つ：

- `CryptoManager`: 暗号化・復号のみ
- `AuthenticationManager`: 認証のみ
- `PasswordRepository`: データアクセスのみ

### 4. テスタビリティ

依存性注入により、モック可能な設計：

```java
// テスト時にMockRepositoryを注入可能
public class AddPasswordUseCaseTest {
    @Test
    public void testAddPassword() {
        PasswordRepository mockRepo = mock(PasswordRepository.class);
        AddPasswordUseCase useCase = new AddPasswordUseCase(mockRepo);
        // ...
    }
}
```

---

## 📈 パフォーマンス最適化

### 1. データベース

- **LiveData**: 自動的にバックグラウンドで実行
- **Indexing**: 検索頻度の高いカラムにインデックス
- **Pagination**: 大量データ対応（将来実装予定）

### 2. 暗号化

- **メインスレッド**: 暗号化処理はメインスレッドで実行（KeyStore要件）
- **バックグラウンド**: DB操作はバックグラウンドスレッドで実行

### 3. UI

- **RecyclerView**: リスト表示の効率化
- **ViewBinding**: findViewById()の削減
- **LiveData**: 必要最小限の更新

---

## 🔮 将来の拡張性

### 予定されている機能

1. **バックアップ/リストア**
   - 暗号化されたエクスポート
   - インポート機能

2. **自動入力サービス**
   - Autofill Framework統合
   - ブラウザ連携

3. **パスワード共有**
   - 暗号化された共有
   - 期限付きアクセス

4. **監査ログ**
   - アクセス履歴
   - 変更履歴

---

## 📚 参考資料

- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVVM Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Android Keystore System](https://developer.android.com/training/articles/keystore)
- [NIST SP 800-38D (GCM)](https://csrc.nist.gov/publications/detail/sp/800-38d/final)
