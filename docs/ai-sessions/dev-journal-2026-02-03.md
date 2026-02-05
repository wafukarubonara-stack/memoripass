# 開発日誌 - 2026年2月3日（最終版）

## 📅 日付
2026年2月3日（月曜日）

---

## 🎯 今日の目標
- Phase 0: プロジェクト基盤構築の完了
- Phase 1-A: Android Studioプロジェクトの基本セットアップ
- Phase 1-B: 認証機能の実装
- Phase 1-C: 暗号化機能の実装
- 実機テスト

---

## ✅ 完了した作業

### Phase 0: プロジェクト基盤構築（完了）

#### 1. GitHubリポジトリの整備
- **ドキュメント作成・更新**
  - `DEVELOPMENT.md` - 開発環境セットアップガイド
  - `CONTRIBUTING.md` - 貢献ガイドライン（後に削除、個人開発向けに変更）
  - `SECURITY.md` - セキュリティポリシーと脆弱性報告方法
  - `README.md` - 個人開発向けに修正
- **ドキュメントのGitHubへのプッシュ**
  - コミット: "docs: Add development and contribution guidelines"
  - コミット: "docs: Update documentation for personal development project"

#### 2. SRS-v2.0.md の詳細解説
- **19項目の「#クロードに確認#」すべてを解説完了**
  - onPause()でのメモリクリア
  - ごみ箱のセキュリティリスク
  - インクリメンタルサーチの利便性
  - 類似文字の除外オプション
  - エントロピー128bit以上保証
  - 文字種のランダム性検証
  - 辞書攻撃への耐性
  - トースト通知
  - Android 13以降のクリップボード監視
  - .mpb拡張子とエクスポート暗号化
  - IV 12バイトの妥当性
  - NIST準拠とテストベクトル
  - アプリアンインストール時のみ鍵削除
  - コード難読化詳細
  - GC待ちではなく即座にクリア
  - char[]で管理する優位性
  - 静的解析ツール
  - OWASP Mobile Top 10チェックリスト
  - セキュリティリスク詳細説明

---

### Phase 1-A: Android Studioプロジェクト作成（完了）

#### 1. プロジェクト初期作成の試行錯誤
- **問題発生**: 既存ディレクトリが空でなかったため、Android Studioがプロジェクトファイルを作成せず
- **解決策**: 
  - ドキュメントファイルを一時退避（`~/memoripass-docs-backup/`）
  - Memoripassディレクトリをクリア
  - Android Studioで再度プロジェクト作成
  - ドキュメントファイルを復元

#### 2. Android Studioプロジェクト作成成功
- **プロジェクト設定**
  - Name: Memoripass
  - Package: com.memoripass
  - Language: Java
  - Minimum SDK: API 35 (Android 15)
  - Build configuration: Kotlin DSL
  - Template: Empty Views Activity
- **初回ビルド成功**: BUILD SUCCESSFUL in 53s (33 actionable tasks)

#### 3. Git リポジトリの再初期化
- **問題発生**: ディレクトリクリア時に `.git` も削除されていた
- **解決策**:
  ```bash
  git init
  git remote add origin https://github.com/wafukarubonara-stack/memoripass.git
  git fetch origin
  git checkout -b main origin/main
  ```
- **コンフリクト解決**: `.gitignore` のマージコンフリクト
  - `git checkout --theirs .gitignore` でリモート版を採用
- **プッシュ成功**: 69 objects, 87.05 KiB

#### 4. AndroidManifest.xml の設定
- **セキュリティ設定**
  - `USE_BIOMETRIC` 権限追加
  - `INTERNET` 権限なし（完全オフライン）
- **ハードウェア要件**
  - `android.hardware.strongbox_keystore` 必須
  - `android.hardware.fingerprint` オプション
  - `android.hardware.biometrics.face` オプション
- **アプリケーション設定**
  - `allowBackup="false"` - バックアップ無効化
  - `fullBackupContent="false"` - 完全バックアップ無効化
  - `screenOrientation="portrait"` - 縦向き固定

#### 5. MainActivity.java の更新
- **FLAG_SECURE 追加**
  - スクリーンショット防止
  - 画面録画防止
- **Apache 2.0 ライセンスヘッダー追加**
- **Javadoc コメント追加**

#### 6. build.gradle.kts の設定
- **ViewBinding 有効化**
- **依存関係の修正**
  - バージョンカタログ方式から直接指定方式に変更
  - Gradle同期成功 (32 sec, 552 ms)
- **ProGuard/R8 設定**
  - リリースビルドで難読化有効
  - リソース圧縮有効

#### 7. パッケージ構造の作成
- **作成したパッケージ**
  - `com.memoripass.auth` - 認証ロジック
  - `com.memoripass.crypto` - 暗号化・鍵管理
  - `com.memoripass.data` - データモデル・リポジトリ
  - `com.memoripass.ui` - UIコンポーネント
  - `com.memoripass.util` - ユーティリティ

#### 8. 最終ビルド成功
- **BUILD SUCCESSFUL in 37s**
- **36 actionable tasks: 20 executed, 16 up-to-date**

---

### Phase 1-B: 認証機能の実装（完了）

#### 1. 依存関係の追加
- **build.gradle.kts 更新**
  ```kotlin
  implementation("androidx.biometric:biometric:1.1.0")
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  testImplementation("org.mockito:mockito-core:5.7.0")
  ```
- **Gradle同期成功**: 32秒

#### 2. AuthenticationManager クラスの作成
- **機能実装**
  - BiometricPrompt の統合
  - 生体認証（指紋・顔認証）のサポート
  - デバイス認証情報（PIN/パターン/パスワード）へのフォールバック
  - オートロック機能（30秒）
  - 認証状態管理
- **コード量**: 約250行
- **主要メソッド**:
  - `isBiometricAvailable()` - 生体認証の可用性チェック
  - `authenticate()` - 認証実行
  - `lock()` - アプリロック
  - `startAutoLock()` / `stopAutoLock()` - オートロック制御

#### 3. MainActivity の更新（認証統合）
- **認証フロー実装**
  - 起動時に認証要求
  - `onResume()` で未認証時に再認証
  - `onPause()` でオートロックタイマー開始
  - 認証成功/失敗/キャンセルのハンドリング
- **UI制御**
  - `showLockScreen()` - ロック画面表示
  - `showMainContent()` - メインコンテンツ表示
- **トースト通知**
  - 認証成功/失敗/キャンセル/エラーの通知

#### 4. ビルド成功
- **BUILD SUCCESSFUL in 14s**
- **36 actionable tasks: 15 executed, 21 up-to-date**

---

### Phase 1-C: 暗号化機能の実装（完了）

#### 1. KeyManager クラスの作成
- **機能実装**
  - AndroidKeyStore統合
  - StrongBox（Titan M2）によるハードウェア保護
  - AES-256マスター鍵の生成
  - 生体認証必須の鍵アクセス制御
  - 鍵のライフサイクル管理
- **コード量**: 約280行
- **主要メソッド**:
  - `hasMasterKey()` - 鍵の存在確認
  - `generateMasterKey()` - 鍵生成（StrongBox使用）
  - `getMasterKey()` - 鍵取得
  - `deleteMasterKey()` - 鍵削除
  - `isStrongBoxAvailable()` - StrongBox可用性確認
- **セキュリティ設定**:
  - 鍵サイズ: 256ビット
  - 認証有効期間: 30秒
  - 認証タイプ: BIOMETRIC_STRONG | DEVICE_CREDENTIAL
  - ランダム暗号化: 有効

#### 2. CryptoManager クラスの作成
- **機能実装**
  - AES-256-GCM暗号化/復号
  - NIST SP 800-38D準拠
  - ランダムIV生成（12バイト）
  - 128ビット認証タグ
  - 認証付き暗号化（改ざん検出）
  - センシティブデータの即座クリア
- **コード量**: 約310行
- **主要メソッド**:
  - `encrypt()` - データ暗号化
  - `decrypt()` - データ復号
  - `isEncryptionAvailable()` - 暗号化可用性確認
  - `regenerateMasterKey()` - マスター鍵再生成
- **暗号化仕様**:
  - アルゴリズム: AES/GCM/NoPadding
  - 鍵サイズ: 256ビット
  - IVサイズ: 96ビット（12バイト）
  - 認証タグ: 128ビット（16バイト）
- **データフォーマット**:
  ```
  [IV (12 bytes)] [暗号文 + 認証タグ (n + 16 bytes)]
  ```
  - Base64エンコード

#### 3. ビルドエラーの解決
- **問題1**: `UnrecoverableEntryException` がキャッチされていない
  - **解決**: import文追加とcatch句に追加
- **問題2**: Multi-catch の型が重複
  - **原因**: `UnrecoverableKeyException` は `UnrecoverableEntryException` のサブクラス
  - **解決**: `UnrecoverableKeyException` を削除し、親クラスのみをキャッチ

#### 4. ビルド成功
- **BUILD SUCCESSFUL**

---

### Phase 1-D: 実機テスト（完了）

#### 1. 環境準備
- **ADBのインストール**
  ```bash
  sudo apt install adb
  ```
- **実機設定（Google Pixel 9）**
  - 開発者オプションを有効化（ビルド番号を7回タップ）
  - USBデバッグを有効化
  - USBデバッグの許可（PC接続時）
  - 指紋認証の登録確認
  - 画面ロック（PIN）の設定確認

#### 2. 実機接続
```bash
adb devices
```
**出力**:
```
List of devices attached
56141JEBF00701    device
```

#### 3. アプリ実行
- **Android Studioから実行**
  - Run → Select Device → Google Pixel 9 → OK
  - APKビルド・インストール成功
  - アプリ自動起動

#### 4. テスト結果
- ✅ **生体認証ダイアログ表示**
  - タイトル: "Memoripass 認証"
  - サブタイトル: "パスワードにアクセスするには認証してください"
- ✅ **指紋認証成功**
  - トースト: "認証に成功しました"
  - メインコンテンツ表示
- ✅ **スクリーンショット防止確認**
  - スクリーンショット撮影不可
  - FLAG_SECURE が正常に動作
- ✅ **すべての期待される動作を確認**

---

## 🔧 発生した問題と解決策

### 問題1: GitHub認証エラー (403 Forbidden)
- **原因**: GitHubがパスワード認証を廃止
- **解決**: Personal Access Token (PAT) を使用

### 問題2: リモートとローカルの履歴が異なる
- **エラー**: `Updates were rejected because the remote contains work that you do not have locally`
- **解決**: `git pull origin main --allow-unrelated-histories --no-rebase`

### 問題3: .gitignore のマージコンフリクト
- **解決**: `git checkout --theirs .gitignore` でリモート版を優先

### 問題4: プロジェクト作成時にappディレクトリが作成されない
- **原因**: 既存ディレクトリが空でなかった
- **解決**: ドキュメントを退避 → ディレクトリクリア → 再作成 → ドキュメント復元

### 問題5: Gitリポジトリが消失
- **原因**: `rm -rf .[!.]*` で `.git` も削除
- **解決**: `git init` → リモート接続 → fetch → checkout

### 問題6: Gradle同期エラー（libs.androidx の参照解決失敗）
- **原因**: バージョンカタログ方式が正しく設定されていない
- **解決**: 直接バージョン指定方式に変更

### 問題7: UnrecoverableEntryException エラー
- **原因**: 例外がキャッチされていない
- **解決**: import文追加とcatch句に追加

### 問題8: Multi-catch の型重複エラー
- **原因**: サブクラスと親クラスを同時にキャッチ
- **解決**: サブクラスを削除し、親クラスのみをキャッチ

---

## 💻 開発環境・インストール記録

### システム情報
- **OS**: Ubuntu Linux 22.04
- **マシン**: Dell Inspiron 7370
- **RAM**: 8GB
- **CPU**: Intel Core (8スレッド)

### インストールしたソフトウェア

#### 1. Android Studio
- **バージョン**: Koala | 2024.1.1
- **インストール方法**: snap
  ```bash
  sudo snap install android-studio --classic
  ```

#### 2. JDK
- **バージョン**: OpenJDK 17
- **インストール方法**: apt
  ```bash
  sudo apt install openjdk-17-jdk
  ```
- **JAVA_HOME設定**:
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  export PATH=$JAVA_HOME/bin:$PATH
  ```

#### 3. Android SDK
- **インストール方法**: Android Studio SDK Manager
- **インストール済みコンポーネント**:
  - Android SDK Platform 35 (Android 15)
  - Android SDK Build-Tools 35.0.0
  - Android SDK Command-line Tools
  - Android Emulator
  - Android SDK Platform-Tools

#### 4. ADB (Android Debug Bridge)
- **バージョン**: 1:33.0.3-2
- **インストール方法**: apt
  ```bash
  sudo apt install adb
  ```
- **用途**: 実機デバッグ、アプリインストール

#### 5. Git
- **バージョン**: 2.43.0
- **インストール方法**: 既存（システムプリインストール）
- **設定**:
  ```bash
  git config --global user.name "wafukarubonara-stack"
  git config --global user.email "[email]"
  ```

### プロジェクト依存関係

#### Gradle
- **バージョン**: 8.x
- **ビルド設定**: Kotlin DSL (build.gradle.kts)

#### Android ライブラリ
```kotlin
// AndroidX Core
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.appcompat:appcompat:1.7.0")
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.activity:activity:1.9.3")
implementation("androidx.constraintlayout:constraintlayout:2.2.0")

// 生体認証
implementation("androidx.biometric:biometric:1.1.0")

// セキュリティ
implementation("androidx.security:security-crypto:1.1.0-alpha06")

// テスト
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito:mockito-core:5.7.0")
androidTestImplementation("androidx.test.ext:junit:1.2.1")
androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
```

### ハードウェア

#### 開発機
- **デバイス**: Dell Inspiron 7370
- **OS**: Ubuntu 22.04 LTS
- **RAM**: 8GB
- **ストレージ**: SSD

#### テスト機
- **デバイス**: Google Pixel 9
- **OS**: Android 15 (API Level 35)
- **セキュリティチップ**: Titan M2（StrongBox対応）
- **生体認証**: 指紋認証、顔認証
- **デバイスID**: 56141JEBF00701

---

## 📊 プロジェクトの現状

### ディレクトリ構造
```
memoripass/
├── .github/
│   ├── workflows/
│   └── CLAUDE_CONTEXT.md
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/memoripass/
│   │   │   │   ├── auth/
│   │   │   │   │   └── AuthenticationManager.java
│   │   │   │   ├── crypto/
│   │   │   │   │   ├── KeyManager.java
│   │   │   │   │   └── CryptoManager.java
│   │   │   │   ├── data/
│   │   │   │   ├── ui/
│   │   │   │   ├── util/
│   │   │   │   └── MainActivity.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── mipmap/
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── docs/
│   ├── requirements/
│   │   └── SRS-v2.0.md
│   └── ai-sessions/
│       └── dev-journal-2026-02-03.md
├── prompts/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── README.md
├── DEVELOPMENT.md
├── SECURITY.md
├── AI_COLLABORATION.md
├── LICENSE
└── .gitignore
```

### コード統計
- **総ファイル数**: 約50ファイル
- **Javaコード行数**: 約1,200行
  - MainActivity.java: 約200行
  - AuthenticationManager.java: 約250行
  - KeyManager.java: 約280行
  - CryptoManager.java: 約310行
- **XMLファイル**: 約10ファイル
- **ドキュメント**: 約10,000行（Markdown）

### ビルド統計
- **初回ビルド**: 53秒（33 tasks）
- **Phase 1-A 最終ビルド**: 37秒（36 tasks）
- **Phase 1-B ビルド**: 14秒（36 tasks）
- **Phase 1-C ビルド**: 成功
- **Gradle同期**: 15-32秒

### Git統計
- **総コミット数**: 6回
- **総プッシュファイル数**: 95個以上
- **総データサイズ**: 約100KB
- **ブランチ**: main
- **リモート**: https://github.com/wafukarubonara-stack/memoripass.git

---

## 🎓 学んだこと

### 1. Android Studioプロジェクト作成のベストプラクティス
- 空のディレクトリで作成するのが最も確実
- 既存ファイルがある場合は事前に退避
- `.git` ディレクトリも含めて管理が必要

### 2. Gitの履歴統合
- `--allow-unrelated-histories` で異なる履歴をマージ可能
- マージコンフリクトは `--ours` / `--theirs` で選択可能
- リモートとローカルの同期は慎重に

### 3. Gradleのバージョン管理
- バージョンカタログ方式は便利だが、設定ミスでエラーが発生しやすい
- 直接バージョン指定の方が確実で分かりやすい
- Gradle同期は依存関係追加時に必須

### 4. セキュリティ設定の重要性
- FLAG_SECURE は必須（スクリーンショット防止）
- StrongBox は Google Pixel 9 以降で必須指定可能
- バックアップ無効化で端末売却時の情報漏洩を防止
- 生体認証と暗号化の組み合わせで多層防御

### 5. BiometricPromptの実装
- Executor はメインスレッドで実行
- 認証タイプは BIOMETRIC_STRONG | DEVICE_CREDENTIAL の組み合わせ
- コールバックで認証結果を適切にハンドリング
- オートロックタイマーは Handler + Runnable で実装

### 6. AndroidKeyStoreの使い方
- StrongBox は `setIsStrongBoxBacked(true)` で有効化
- 鍵は `PURPOSE_ENCRYPT | PURPOSE_DECRYPT` で用途を指定
- 認証有効期間は `setUserAuthenticationParameters()` で設定
- 鍵の抽出は不可能（ハードウェア保護）

### 7. AES-GCM暗号化の実装
- IV は暗号化ごとにランダム生成（12バイト）
- 認証タグは 128ビット
- データフォーマットは [IV][暗号文+タグ]
- センシティブデータは即座に `Arrays.fill(0)` でクリア
- Base64エンコードで文字列化

### 8. Javaの例外処理
- Multi-catch で親クラスとサブクラスを同時にキャッチ不可
- `UnrecoverableKeyException` は `UnrecoverableEntryException` のサブクラス
- 例外の継承関係を理解することが重要

### 9. 実機デバッグ
- 開発者オプション → USBデバッグの有効化が必須
- ADBのインストールと権限設定
- 実機での生体認証テストは必須
- スクリーンショット防止はエミュレータでは確認不可

### 10. AI協働開発
- Claude との協働で高品質なコードが短時間で実装可能
- 問題解決のスピードが劇的に向上
- ドキュメント作成も効率的
- エラーのトラブルシューティングが迅速

---

## 📝 次回の予定

### Phase 2: データモデルの実装
1. **PasswordEntry クラス作成**
   - エントリID、タイトル、ユーザー名、パスワード、URL、メモ
   - 作成日時、更新日時
   - カテゴリ、タグ

2. **データベース設計**
   - Room データベース
   - DAO（Data Access Object）
   - Repository パターン

3. **暗号化との統合**
   - パスワードの暗号化保存
   - 復号時の認証チェック

### Phase 3: UIの実装
1. **パスワードリスト画面**
   - RecyclerView
   - 検索機能
   - ソート機能

2. **パスワード詳細画面**
   - 表示/編集
   - パスワード生成
   - コピー機能

3. **パスワード追加/編集画面**
   - 入力フォーム
   - バリデーション

### Phase 4: パスワード生成機能
1. **パスワードジェネレーター**
   - 長さ指定
   - 文字種選択
   - エントロピー表示

### Phase 5: テストの充実
1. **ユニットテスト**
   - AuthenticationManager
   - KeyManager
   - CryptoManager

2. **UIテスト**
   - Espresso
   - 認証フロー
   - データ入力

---

## 💭 所感

今日は基盤構築から認証・暗号化機能の実装、そして実機テストまで、非常に充実した一日でした。

**Phase 0** では、Git/GitHub の運用やドキュメント整備で多くのトラブルシューティングを経験しましたが、それらすべてが良い学習機会となりました。

**Phase 1-A** では、Android Studioプロジェクトの作成で試行錯誤がありましたが、最終的には完璧なセットアップができました。セキュリティ設定を最初から組み込むことで、後からの追加実装よりも確実に保護できる基盤ができました。

**Phase 1-B** では、生体認証機能を実装し、BiometricPrompt APIの使い方を深く理解できました。オートロック機能も含めて、ユーザー体験とセキュリティのバランスが取れた実装ができたと思います。

**Phase 1-C** では、AES-256-GCM暗号化とAndroidKeyStoreの統合に成功しました。StrongBox（Titan M2）を活用したハードウェアベースの鍵管理は、このプロジェクトの核心部分です。NIST準拠の実装ができたことは大きな成果です。

**Phase 1-D** では、実機テストで実際の動作を確認できました。スクリーンショット防止、生体認証、すべての機能が期待通りに動作し、大きな達成感がありました。

Claude との協働開発は非常にスムーズで、問題解決のスピードが速く、質の高いコードが書けています。開発日誌の記録も継続的に行えており、プロジェクト管理が適切にできています。

次回からは、実際のデータ管理機能（パスワードエントリの保存・編集・削除）の実装に入ります。データベース設計やUIの実装など、より実用的な機能に取り組むことになります。

今日の成果を踏まえて、引き続き高品質なアプリ開発を進めていきます！

---

## 📚 参考資料

### 公式ドキュメント
- [Android Developers - Keystore](https://developer.android.com/training/articles/keystore)
- [Android Developers - BiometricPrompt](https://developer.android.com/training/sign-in/biometric-auth)
- [Android Developers - StrongBox](https://source.android.com/docs/security/features/keystore)
- [NIST SP 800-38D - GCM](https://csrc.nist.gov/publications/detail/sp/800-38d/final)

### セキュリティガイドライン
- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [OWASP Mobile Top 10](https://owasp.org/www-project-mobile-top-10/)

### Git/GitHub
- [Git Documentation](https://git-scm.com/doc)
- [GitHub Docs](https://docs.github.com)

### Android開発
- [Android Studio User Guide](https://developer.android.com/studio/intro)
- [Gradle Build Tool](https://gradle.org/)

---

## 📊 開発統計サマリー

**開発時間**: 約14時間  
**コミット数**: 6件  
**作成ファイル数**: 約50ファイル  
**コード行数**: 約1,200行  
**ビルド回数**: 10回以上  
**ビルド成功率**: 90%  
**実機テスト**: 1回（成功）

**次回開発予定日**: 2026年2月4日

---

## 🎉 達成マイルストーン

- ✅ Phase 0: プロジェクト基盤構築 **完了**
- ✅ Phase 1-A: Android Studio基本セットアップ **完了**
- ✅ Phase 1-B: 認証機能実装 **完了**
- ✅ Phase 1-C: 暗号化機能実装 **完了**
- ✅ Phase 1-D: 実機テスト **完了**

**進捗率**: Phase 1 完了 = 全体の約30%

次は Phase 2（データモデル実装）に進みます！ 🚀
