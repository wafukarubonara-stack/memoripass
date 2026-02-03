# 開発日誌 - 2026年2月3日

## 📅 日付
2026年2月3日（月曜日）

---

## 🎯 今日の目標
- Phase 0: プロジェクト基盤構築の完了
- Phase 1-A: Android Studioプロジェクトの基本セットアップ

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

### Phase 1: Android Studioプロジェクト作成

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

---

### Phase 1-A: 基本セットアップ（完了）

#### 1. AndroidManifest.xml の設定
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

#### 2. MainActivity.java の更新
- **FLAG_SECURE 追加**
  - スクリーンショット防止
  - 画面録画防止
  ```java
  getWindow().setFlags(
      WindowManager.LayoutParams.FLAG_SECURE,
      WindowManager.LayoutParams.FLAG_SECURE
  );
  ```
- **Apache 2.0 ライセンスヘッダー追加**
- **Javadoc コメント追加**

#### 3. build.gradle.kts の設定
- **ViewBinding 有効化**
  ```kotlin
  buildFeatures {
      viewBinding = true
  }
  ```
- **依存関係の修正**
  - バージョンカタログ方式から直接指定方式に変更
  - Gradle同期成功 (32 sec, 552 ms)
- **ProGuard/R8 設定**
  - リリースビルドで難読化有効
  - リソース圧縮有効

#### 4. パッケージ構造の作成
- **作成したパッケージ**
  - `com.memoripass.auth` - 認証ロジック
  - `com.memoripass.crypto` - 暗号化・鍵管理
  - `com.memoripass.data` - データモデル・リポジトリ
  - `com.memoripass.ui` - UIコンポーネント
  - `com.memoripass.util` - ユーティリティ

#### 5. 最終ビルド成功
- **BUILD SUCCESSFUL in 37s**
- **36 actionable tasks: 20 executed, 16 up-to-date**

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
│   │   │   │   ├── crypto/
│   │   │   │   ├── data/
│   │   │   │   ├── ui/
│   │   │   │   ├── util/
│   │   │   │   └── MainActivity.java
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── docs/
│   ├── requirements/
│   │   └── SRS-v2.0.md
│   └── ai-sessions/
├── gradle/
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

### ビルド統計
- **初回ビルド**: 53秒
- **Gradle同期**: 32秒
- **最終ビルド**: 37秒
- **実行タスク数**: 36 tasks

---

## 🎓 学んだこと

### 1. Android Studioプロジェクト作成のベストプラクティス
- 空のディレクトリで作成するのが最も確実
- 既存ファイルがある場合は事前に退避

### 2. Gitの履歴統合
- `--allow-unrelated-histories` で異なる履歴をマージ可能
- マージコンフリクトは `--ours` / `--theirs` で選択可能

### 3. Gradleのバージョン管理
- バージョンカタログ方式は便利だが、設定ミスでエラーが発生しやすい
- 直接バージョン指定の方が確実で分かりやすい

### 4. セキュリティ設定の重要性
- FLAG_SECURE は必須（スクリーンショット防止）
- StrongBox は Google Pixel 9 以降で必須指定可能
- バックアップ無効化で端末売却時の情報漏洩を防止

---

## 📝 次回の予定

### Phase 1-B: 認証機能の実装
1. **BiometricPrompt の実装**
   - AuthenticationManager クラス作成
   - BiometricHelper クラス作成
   - 生体認証の初期化処理

2. **デバイス認証情報のフォールバック**
   - PIN/パターン/パスワード認証
   - 生体認証が利用できない場合の代替手段

3. **オートロック機能**
   - バックグラウンド移行時のタイマー開始
   - 30秒後に自動ロック
   - onPause/onResume での制御

4. **認証状態の管理**
   - SharedPreferences での初回起動判定
   - 認証成功/失敗のハンドリング

---

## 💭 所感

今日は基盤構築から Android Studio プロジェクトのセットアップまで、多くのハードルを乗り越えました。特に Git 関連のトラブルシューティングは良い学習機会となりました。

セキュリティ設定を最初から組み込むことで、後からの追加実装よりも確実に保護できる基盤ができました。FLAG_SECURE、StrongBox要件、バックアップ無効化など、重要な設定をすべて初期段階で完了できたのは大きな成果です。

次回からは実際の機能実装（生体認証）に入るため、より実践的な開発フェーズに突入します。Claude との協働開発も順調で、問題解決のスピードが速く、質の高いコードが書けています。

---

## 📚 参考資料

- [Android Developers - Keystore](https://developer.android.com/training/articles/keystore)
- [Android Developers - BiometricPrompt](https://developer.android.com/training/sign-in/biometric-auth)
- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [NIST SP 800-38D](https://csrc.nist.gov/publications/detail/sp/800-38d/final)

---

**開発時間**: 約10時間  
**コミット数**: 4件  
**追加ファイル数**: 3件（AndroidManifest.xml, MainActivity.java, build.gradle.kts）  
**作成パッケージ数**: 5件

**次回開発予定日**: 2026年2月4日
