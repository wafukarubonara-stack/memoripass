# Memoripass - シーケンス図

## 1. アプリ起動・認証フロー

```mermaid
sequenceDiagram
    actor User
    participant MainActivity
    participant AuthManager as AuthenticationManager
    participant BiometricPrompt
    participant KeyStore as AndroidKeyStore
    
    User->>MainActivity: アプリ起動
    activate MainActivity
    
    MainActivity->>MainActivity: onCreate()
    MainActivity->>MainActivity: setFlags(FLAG_SECURE)
    Note over MainActivity: スクリーンショット防止
    
    MainActivity->>AuthManager: isAuthenticated()
    AuthManager-->>MainActivity: false
    
    MainActivity->>MainActivity: showLockScreen()
    MainActivity->>AuthManager: authenticate(callback)
    activate AuthManager
    
    AuthManager->>AuthManager: isBiometricAvailable()
    AuthManager->>BiometricPrompt: authenticate(promptInfo)
    activate BiometricPrompt
    
    BiometricPrompt->>User: 生体認証プロンプト表示
    User->>BiometricPrompt: 指紋認証
    
    BiometricPrompt->>KeyStore: 鍵アクセス要求
    KeyStore-->>BiometricPrompt: 認証成功、鍵提供
    
    BiometricPrompt->>AuthManager: onAuthenticationSucceeded()
    deactivate BiometricPrompt
    
    AuthManager->>AuthManager: isAuthenticated = true
    AuthManager->>MainActivity: callback.onAuthenticationSuccess()
    deactivate AuthManager
    
    MainActivity->>MainActivity: showMainContent()
    MainActivity->>MainActivity: PasswordListFragment表示
    deactivate MainActivity
    
    Note over User,MainActivity: 認証完了、メイン画面表示
```

## 2. パスワード追加フロー

```mermaid
sequenceDiagram
    actor User
    participant AddFragment as AddPasswordFragment
    participant AddViewModel as AddPasswordViewModel
    participant AddUseCase as AddPasswordUseCase
    participant Repository as PasswordRepository
    participant CryptoManager
    participant KeyManager
    participant Dao as PasswordEntryDao
    participant Database as Room Database
    
    User->>AddFragment: パスワード入力
    User->>AddFragment: 保存ボタンタップ
    
    activate AddFragment
    AddFragment->>AddFragment: validateInput()
    AddFragment->>AddViewModel: addPassword(password)
    activate AddViewModel
    
    AddViewModel->>AddUseCase: execute(password)
    activate AddUseCase
    
    AddUseCase->>AddUseCase: ビジネスバリデーション
    Note over AddUseCase: タイトル・パスワード必須チェック
    
    AddUseCase->>Repository: encryptPassword(plainPassword)
    activate Repository
    
    Repository->>CryptoManager: encrypt(plaintext)
    activate CryptoManager
    
    CryptoManager->>KeyManager: getMasterKey()
    activate KeyManager
    KeyManager->>KeyManager: AndroidKeyStoreから鍵取得
    KeyManager-->>CryptoManager: SecretKey
    deactivate KeyManager
    
    CryptoManager->>CryptoManager: ランダムIV生成
    CryptoManager->>CryptoManager: AES-GCM暗号化
    CryptoManager-->>Repository: encryptedPassword (Base64)
    deactivate CryptoManager
    
    Repository-->>AddUseCase: encryptedPassword
    deactivate Repository
    
    AddUseCase->>AddUseCase: PasswordEntry作成
    AddUseCase->>Repository: insert(passwordEntry)
    activate Repository
    
    Repository->>Dao: insert(entry)
    activate Dao
    Dao->>Database: INSERT INTO password_entries
    Database-->>Dao: success
    deactivate Dao
    
    Repository-->>AddUseCase: success
    deactivate Repository
    
    AddUseCase-->>AddViewModel: success
    deactivate AddUseCase
    
    AddViewModel->>AddViewModel: viewState.setValue(SUCCESS)
    AddViewModel-->>AddFragment: LiveData更新
    deactivate AddViewModel
    
    AddFragment->>AddFragment: onSuccess()
    AddFragment->>User: "保存しました" Toast表示
    AddFragment->>AddFragment: 画面を閉じる
    deactivate AddFragment
```

## 3. パスワード一覧取得フロー

```mermaid
sequenceDiagram
    actor User
    participant ListFragment as PasswordListFragment
    participant ListViewModel as PasswordListViewModel
    participant Repository as PasswordRepository
    participant Dao as PasswordEntryDao
    participant Database as Room Database
    participant RecyclerView
    
    User->>ListFragment: 画面表示
    activate ListFragment
    
    ListFragment->>ListFragment: onViewCreated()
    ListFragment->>ListViewModel: getAllPasswords()
    activate ListViewModel
    
    ListViewModel->>Repository: getAllPasswords()
    activate Repository
    
    Repository->>Dao: getAllPasswords()
    activate Dao
    
    Dao->>Database: SELECT * FROM password_entries
    Database-->>Dao: List<PasswordEntry>
    
    Dao-->>Repository: LiveData<List<PasswordEntry>>
    deactivate Dao
    
    Repository-->>ListViewModel: LiveData<List<PasswordEntry>>
    deactivate Repository
    
    ListViewModel-->>ListFragment: LiveData<List<PasswordEntry>>
    deactivate ListViewModel
    
    ListFragment->>ListFragment: observe(allPasswords)
    
    Note over ListFragment: LiveDataが更新されたら
    
    ListFragment->>ListFragment: onChanged(passwords)
    ListFragment->>RecyclerView: adapter.submitList(passwords)
    activate RecyclerView
    
    RecyclerView->>RecyclerView: データバインディング
    RecyclerView-->>User: パスワード一覧表示
    deactivate RecyclerView
    
    deactivate ListFragment
```

## 4. パスワード詳細表示・復号フロー

```mermaid
sequenceDiagram
    actor User
    participant ListFragment as PasswordListFragment
    participant DetailFragment as PasswordDetailFragment
    participant DetailViewModel as PasswordDetailViewModel
    participant Repository as PasswordRepository
    participant CryptoManager
    participant KeyManager
    participant Dao as PasswordEntryDao
    
    User->>ListFragment: パスワード項目タップ
    activate ListFragment
    
    ListFragment->>DetailFragment: navigate(passwordId)
    deactivate ListFragment
    activate DetailFragment
    
    DetailFragment->>DetailFragment: onViewCreated()
    DetailFragment->>DetailViewModel: loadPassword(passwordId)
    activate DetailViewModel
    
    DetailViewModel->>Repository: getPasswordById(passwordId)
    activate Repository
    
    Repository->>Dao: getPasswordById(id)
    activate Dao
    Dao-->>Repository: LiveData<PasswordEntry>
    deactivate Dao
    
    Repository-->>DetailViewModel: LiveData<PasswordEntry>
    deactivate Repository
    
    DetailViewModel-->>DetailFragment: observe開始
    deactivate DetailViewModel
    
    Note over DetailFragment: データ受信
    
    DetailFragment->>DetailFragment: displayPassword(entry)
    DetailFragment-->>User: タイトル・ユーザー名表示
    
    User->>DetailFragment: パスワード表示ボタンタップ
    DetailFragment->>DetailViewModel: decryptPassword(encryptedPwd)
    activate DetailViewModel
    
    DetailViewModel->>Repository: decryptPassword(encrypted)
    activate Repository
    
    Repository->>CryptoManager: decrypt(encryptedData)
    activate CryptoManager
    
    CryptoManager->>KeyManager: getMasterKey()
    activate KeyManager
    KeyManager-->>CryptoManager: SecretKey
    deactivate KeyManager
    
    CryptoManager->>CryptoManager: Base64デコード
    CryptoManager->>CryptoManager: IV分離
    CryptoManager->>CryptoManager: AES-GCM復号
    CryptoManager-->>Repository: plainPassword
    deactivate CryptoManager
    
    Repository-->>DetailViewModel: plainPassword
    deactivate Repository
    
    DetailViewModel-->>DetailFragment: パスワード復号成功
    deactivate DetailViewModel
    
    DetailFragment->>DetailFragment: showPassword(plainPassword)
    DetailFragment-->>User: パスワード表示
    
    Note over DetailFragment: 10秒後自動的に隠す
    
    deactivate DetailFragment
```

## 5. オートロックフロー

```mermaid
sequenceDiagram
    actor User
    participant MainActivity
    participant AuthManager as AuthenticationManager
    participant Handler
    
    User->>MainActivity: アプリ使用中
    
    User->>MainActivity: ホームボタン（バックグラウンド移行）
    activate MainActivity
    
    MainActivity->>MainActivity: onPause()
    MainActivity->>AuthManager: startAutoLock()
    activate AuthManager
    
    AuthManager->>Handler: postDelayed(autoLockRunnable, 30秒)
    activate Handler
    AuthManager-->>MainActivity: タイマー開始
    deactivate AuthManager
    deactivate MainActivity
    
    Note over Handler: 30秒経過
    
    Handler->>AuthManager: autoLockRunnable実行
    activate AuthManager
    AuthManager->>AuthManager: lock()
    AuthManager->>AuthManager: isAuthenticated = false
    deactivate AuthManager
    deactivate Handler
    
    User->>MainActivity: アプリ再開
    activate MainActivity
    MainActivity->>MainActivity: onResume()
    MainActivity->>AuthManager: isAuthenticated()
    AuthManager-->>MainActivity: false
    
    MainActivity->>MainActivity: showLockScreen()
    MainActivity->>AuthManager: authenticate(callback)
    deactivate MainActivity
    
    Note over User,AuthManager: 再認証フローへ
```

## 6. パスワード削除フロー

```mermaid
sequenceDiagram
    actor User
    participant DetailFragment as PasswordDetailFragment
    participant DetailViewModel as PasswordDetailViewModel
    participant DeleteUseCase as DeletePasswordUseCase
    participant Repository as PasswordRepository
    participant Dao as PasswordEntryDao
    participant Database as Room Database
    
    User->>DetailFragment: 削除ボタンタップ
    activate DetailFragment
    
    DetailFragment->>DetailFragment: 確認ダイアログ表示
    User->>DetailFragment: 削除確定
    
    DetailFragment->>DetailViewModel: deletePassword(passwordId)
    activate DetailViewModel
    
    DetailViewModel->>DeleteUseCase: execute(passwordId)
    activate DeleteUseCase
    
    DeleteUseCase->>Repository: delete(passwordEntry)
    activate Repository
    
    Repository->>Dao: delete(entry)
    activate Dao
    
    Dao->>Database: DELETE FROM password_entries WHERE id = ?
    Database-->>Dao: success
    deactivate Dao
    
    Repository-->>DeleteUseCase: success
    deactivate Repository
    
    DeleteUseCase-->>DetailViewModel: success
    deactivate DeleteUseCase
    
    DetailViewModel->>DetailViewModel: viewState.setValue(SUCCESS)
    DetailViewModel-->>DetailFragment: LiveData更新
    deactivate DetailViewModel
    
    DetailFragment->>DetailFragment: onDeleteSuccess()
    DetailFragment->>User: "削除しました" Toast表示
    DetailFragment->>DetailFragment: 一覧画面に戻る
    deactivate DetailFragment
```

## 7. パスワード検索フロー

```mermaid
sequenceDiagram
    actor User
    participant ListFragment as PasswordListFragment
    participant ListViewModel as PasswordListViewModel
    participant Repository as PasswordRepository
    participant Dao as PasswordEntryDao
    participant Database as Room Database
    
    User->>ListFragment: 検索ボックスに入力
    activate ListFragment
    
    ListFragment->>ListFragment: onTextChanged(query)
    ListFragment->>ListViewModel: searchPasswords(query)
    activate ListViewModel
    
    ListViewModel->>Repository: searchPasswords(query)
    activate Repository
    
    Repository->>Dao: searchPasswords(query)
    activate Dao
    
    Dao->>Database: SELECT * WHERE title LIKE '%query%' OR username LIKE '%query%'
    Database-->>Dao: List<PasswordEntry>
    
    Dao-->>Repository: LiveData<List<PasswordEntry>>
    deactivate Dao
    
    Repository-->>ListViewModel: LiveData<List<PasswordEntry>>
    deactivate Repository
    
    ListViewModel-->>ListFragment: LiveData更新
    deactivate ListViewModel
    
    ListFragment->>ListFragment: onChanged(filteredPasswords)
    ListFragment->>ListFragment: adapter.submitList(filtered)
    ListFragment-->>User: 検索結果表示
    deactivate ListFragment
```

---

**作成日**: 2026年2月5日  
**作成者**: Claude + wafukarubonara-stack
