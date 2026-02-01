# 🛠️ 開発ガイド

このドキュメントは、Memoripassの開発環境をセットアップし、開発を開始するための完全ガイドです。

---

## 目次

1. [前提条件](#前提条件)
2. [開発環境のセットアップ](#開発環境のセットアップ)
3. [プロジェクト構造](#プロジェクト構造)
4. [ビルドと実行](#ビルドと実行)
5. [テスト](#テスト)
6. [デバッグ](#デバッグ)
7. [コーディング規約](#コーディング規約)
8. [Git ワークフロー](#gitワークフロー)

---

## 前提条件

### 必須

- **OS**: Ubuntu Linux 22.04+ または macOS 12+
- **JDK**: OpenJDK 17 (推奨: Temurin)
- **Android Studio**: Koala | 2024.1.1 以降
- **Android SDK**: API Level 35 (Android 15)
- **Git**: 2.30+
- **実機またはエミュレータ**: Google Pixel 9 または Android 15+

### 推奨

- **メモリ**: 16GB以上
- **ストレージ**: 10GB以上の空き容量
- **ネットワーク**: 高速インターネット接続（初回セットアップ時）

---

## 開発環境のセットアップ

### Step 1: JDK 17のインストール

#### Ubuntu / Linux

```bash
# OpenJDK 17をインストール
sudo apt update
sudo apt install openjdk-17-jdk

# バージョン確認
java -version
# 出力例: openjdk version "17.0.x"

# JAVA_HOMEを設定
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

#### macOS

```bash
# Homebrewでインストール
brew install openjdk@17

# シンボリックリンクを作成
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk \
     /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# JAVA_HOMEを設定
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

---

### Step 2: Android Studioのインストール

#### Ubuntu / Linux

```bash
# snapでインストール（推奨）
sudo snap install android-studio --classic

# または手動でダウンロード
# https://developer.android.com/studio からダウンロード
wget https://redirector.gvt1.com/edgedl/android/studio/ide-zips/2024.1.1.11/android-studio-2024.1.1.11-linux.tar.gz
tar -xzf android-studio-*-linux.tar.gz
sudo mv android-studio /opt/
/opt/android-studio/bin/studio.sh
```

#### macOS

```bash
# Homebrewでインストール
brew install --cask android-studio

# または公式サイトからダウンロード
# https://developer.android.com/studio
```

---

### Step 3: Android SDKのセットアップ

Android Studio初回起動時に、SDK Manager が開きます：

1. **SDK Platforms** タブ
   - ✅ Android 15.0 (API Level 35) にチェック
   - ✅ Show Package Details にチェック
     - Android SDK Platform 35
     - Sources for Android 35

2. **SDK Tools** タブ
   - ✅ Android SDK Build-Tools 35.0.0
   - ✅ Android SDK Command-line Tools
   - ✅ Android Emulator
   - ✅ Android SDK Platform-Tools

3. **Apply** をクリックしてインストール

---

### Step 4: リポジトリのクローン

```bash
# SSHの場合（推奨）
git clone git@github.com:wafukarubonara-stack/memoripass.git

# HTTPSの場合
git clone https://github.com/wafukarubonara-stack/memoripass.git

# ディレクトリに移動
cd memoripass
```

---

### Step 5: プロジェクトを開く

1. Android Studioを起動
2. **Open** をクリック
3. `memoripass` ディレクトリを選択
4. **Trust Project** をクリック
5. Gradleの同期を待つ（初回は5-10分かかる場合があります）

---

## プロジェクト構造

```
memoripass/
├── .github/
│   ├── workflows/           # GitHub Actions CI/CD
│   └── CLAUDE_CONTEXT.md    # AI開発支援コンテキスト
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/memoripass/
│   │   │   │   ├── auth/            # 認証関連
│   │   │   │   │   ├── AuthenticationManager.java
│   │   │   │   │   └── BiometricHelper.java
│   │   │   │   ├── crypto/          # 暗号化関連
│   │   │   │   │   ├── CryptoManager.java
│   │   │   │   │   ├── KeyManager.java
│   │   │   │   │   └── SecureStorage.java
│   │   │   │   ├── data/            # データ管理
│   │   │   │   │   ├── model/
│   │   │   │   │   ├── repository/
│   │   │   │   │   └── dao/
│   │   │   │   ├── ui/              # UI層
│   │   │   │   │   ├── main/
│   │   │   │   │   ├── detail/
│   │   │   │   │   └── settings/
│   │   │   │   └── util/            # ユーティリティ
│   │   │   │       ├── PasswordGenerator.java
│   │   │   │       └── SecurityUtils.java
│   │   │   ├── res/                 # リソースファイル
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   ├── test/                    # ユニットテスト
│   │   │   └── java/com/memoripass/
│   │   │
│   │   └── androidTest/             # インストルメンテーションテスト
│   │       └── java/com/memoripass/
│   │
│   ├── build.gradle.kts             # アプリレベルのGradle設定
│   └── proguard-rules.pro           # ProGuard難読化設定
│
├── docs/
│   ├── requirements/
│   │   └── SRS-v2.0.md              # 要件定義書
│   ├── design/
│   │   └── architecture.md          # アーキテクチャ設計
│   ├── testing/
│   │   └── test-plan.md             # テスト計画
│   └── ai-sessions/                 # AI相談ログ
│
├── prompts/                          # 再利用可能なプロンプト集
│   ├── code-review.md
│   ├── unit-test-generation.md
│   └── security-check.md
│
├── build.gradle.kts                  # プロジェクトレベルのGradle設定
├── settings.gradle.kts               # Gradle設定
├── gradle.properties                 # Gradleプロパティ
├── README.md                         # プロジェクト概要
├── DEVELOPMENT.md                    # このファイル
├── CONTRIBUTING.md                   # 貢献ガイドライン
├── SECURITY.md                       # セキュリティポリシー
├── AI_COLLABORATION.md               # AI協働ガイド
├── LICENSE                           # Apache License 2.0
└── .gitignore                        # Git無視リスト
```

---

## ビルドと実行

### Gradle コマンド

```bash
# プロジェクトのクリーン
./gradlew clean

# デバッグビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# すべてのテストを実行
./gradlew test

# Lintチェック
./gradlew lint

# 依存関係の更新チェック
./gradlew dependencyUpdates
```

---

### Android Studioから実行

1. **実機の接続**
   - Google Pixel 9をUSBで接続
   - 開発者オプションを有効化
   - USBデバッグを有効化

2. **エミュレータの作成**（実機がない場合）
   - Tools → Device Manager
   - Create Device
   - Phone → Pixel 9
   - System Image → Android 15 (API 35)
   - Finish

3. **アプリの実行**
   - ツールバーの▶️（Run）ボタンをクリック
   - または `Shift + F10`

---

## テスト

### ユニットテスト

```bash
# すべてのユニットテストを実行
./gradlew test

# 特定のテストクラスを実行
./gradlew test --tests com.memoripass.crypto.CryptoManagerTest

# カバレッジレポート生成
./gradlew testDebugUnitTestCoverage
# レポート: app/build/reports/coverage/test/debug/index.html
```

### インストルメンテーションテスト（実機・エミュレータ必要）

```bash
# すべてのAndroidテストを実行
./gradlew connectedAndroidTest

# 特定のテストクラスを実行
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.memoripass.ui.MainActivityTest
```

### テストの書き方

#### ユニットテストの例

```java
// src/test/java/com/memoripass/crypto/CryptoManagerTest.java

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CryptoManagerTest {
    
    private CryptoManager cryptoManager;
    
    @Before
    public void setUp() {
        cryptoManager = new CryptoManager();
    }
    
    @Test
    public void testEncryptDecrypt() {
        String plaintext = "TestPassword123";
        
        byte[] encrypted = cryptoManager.encrypt(plaintext);
        assertNotNull(encrypted);
        
        String decrypted = cryptoManager.decrypt(encrypted);
        assertEquals(plaintext, decrypted);
    }
    
    @Test
    public void testEncryptionIsNonDeterministic() {
        String plaintext = "TestPassword123";
        
        byte[] encrypted1 = cryptoManager.encrypt(plaintext);
        byte[] encrypted2 = cryptoManager.encrypt(plaintext);
        
        // 同じ平文でも暗号文は異なる（IVが異なるため）
        assertFalse(Arrays.equals(encrypted1, encrypted2));
    }
}
```

---

## デバッグ

### Logcat の使用

```java
import android.util.Log;

public class CryptoManager {
    private static final String TAG = "CryptoManager";
    
    public void encrypt(String data) {
        Log.d(TAG, "Encrypting data...");  // デバッグログ
        
        // 暗号化処理
        
        Log.i(TAG, "Encryption successful");  // 情報ログ
    }
}
```

⚠️ **重要**: センシティブな情報（パスワード、鍵など）は**絶対にログ出力しない**

---

### Android Studio デバッガ

1. ブレークポイントを設定（行番号の左をクリック）
2. 🐞（Debug）ボタンでアプリを起動
3. ブレークポイントで停止
4. Variables パネルで変数を確認
5. Step Over (F8) / Step Into (F7) で実行

---

### メモリリーク検出

```bash
# LeakCanaryを依存関係に追加（build.gradle.kts）
dependencies {
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}

# アプリを実行すると、メモリリークが自動検出される
```

---

## コーディング規約

### 命名規則

```java
// クラス: PascalCase
public class AuthenticationManager { }

// メソッド: camelCase
public void authenticateUser() { }

// 定数: UPPER_SNAKE_CASE
private static final int MAX_RETRY_COUNT = 3;

// 変数: camelCase
private String userName;

// パッケージ: 小文字、ドット区切り
package com.memoripass.crypto;
```

---

### コメント規約

```java
/**
 * パスワードを暗号化するクラス
 * 
 * <p>このクラスはAES-256-GCMを使用してパスワードを暗号化します。
 * 鍵はAndroidKeyStoreのStrongBoxに保管されます。</p>
 * 
 * <p>使用例:</p>
 * <pre>{@code
 * CryptoManager manager = new CryptoManager();
 * byte[] encrypted = manager.encrypt("myPassword");
 * }</pre>
 * 
 * @see KeyManager
 * @since 1.0
 */
public class CryptoManager {
    
    /**
     * データを暗号化します
     * 
     * @param plaintext 暗号化する平文
     * @return 暗号化されたバイト配列
     * @throws CryptoException 暗号化に失敗した場合
     */
    public byte[] encrypt(String plaintext) throws CryptoException {
        // 実装
    }
}
```

---

### セキュリティチェックリスト

すべてのコードは以下を満たす必要があります：

- [ ] センシティブデータのログ出力なし
- [ ] パスワードは`char[]`で管理、使用後に`Arrays.fill()`でクリア
- [ ] 例外メッセージで詳細情報を漏らさない
- [ ] ハードコードされた秘密情報なし
- [ ] 適切な権限チェック
- [ ] 入力値の検証とサニタイズ

---

## Git ワークフロー

### ブランチ戦略

```
main            本番リリース可能なコード
  ├── develop   開発中の統合ブランチ
  │    ├── feature/FR-AUTH-01-biometric  機能開発
  │    ├── feature/FR-DATA-01-crud       機能開発
  │    └── bugfix/fix-encryption-bug     バグ修正
  └── hotfix/security-patch              緊急修正
```

---

### 開発フロー

```bash
# 1. 最新のdevelopブランチを取得
git checkout develop
git pull origin develop

# 2. 機能ブランチを作成
git checkout -b feature/FR-AUTH-01-biometric

# 3. 開発・コミット
git add .
git commit -m "feat(auth): Implement BiometricPrompt authentication

- Add AuthenticationManager class
- Support fingerprint and face authentication
- Add fallback to device PIN/pattern
- Refs: #1"

# 4. プッシュ
git push origin feature/FR-AUTH-01-biometric

# 5. GitHub でPull Requestを作成
# 6. レビュー・マージ
```

---

### コミットメッセージ規約

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type**:
- `feat`: 新機能
- `fix`: バグ修正
- `docs`: ドキュメント変更
- `style`: コードフォーマット
- `refactor`: リファクタリング
- `test`: テスト追加・修正
- `chore`: ビルド・ツール変更

**例**:
```
feat(crypto): Add AES-256-GCM encryption

- Implement CryptoManager with GCM mode
- Add IV generation using SecureRandom
- Add authentication tag validation

Closes #5
```

---

## トラブルシューティング

### Gradle同期エラー

```bash
# キャッシュをクリア
./gradlew clean
rm -rf ~/.gradle/caches/

# Android Studioで:
# File → Invalidate Caches → Invalidate and Restart
```

---

### ビルドエラー: "SDK location not found"

```bash
# local.propertiesを作成
echo "sdk.dir=/home/[username]/Android/Sdk" > local.properties

# または環境変数を設定
export ANDROID_HOME=/home/[username]/Android/Sdk
```

---

### StrongBox使用時のエラー

```
StrongBox unavailable on this device
```

**原因**: エミュレータはStrongBoxをサポートしていません。

**解決**: 実機（Google Pixel 9）で実行してください。

---

## 参考リンク

### 公式ドキュメント
- [Android Developers](https://developer.android.com)
- [Android Keystore System](https://developer.android.com/training/articles/keystore)
- [BiometricPrompt](https://developer.android.com/training/sign-in/biometric-auth)

### セキュリティガイドライン
- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [NIST Cryptographic Standards](https://csrc.nist.gov/publications)

### プロジェクト内ドキュメント
- [要件定義書](docs/requirements/SRS-v2.0.md)
- [AI協働ガイド](AI_COLLABORATION.md)
- [貢献ガイドライン](CONTRIBUTING.md)

---

## サポート

質問や問題がある場合:
1. [Issue](https://github.com/wafukarubonara-stack/memoripass/issues) を検索
2. 新しいIssueを作成
3. [Discussions](https://github.com/wafukarubonara-stack/memoripass/discussions) で質問

---

**最終更新**: 2026年2月1日
