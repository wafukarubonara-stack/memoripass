# 🛠️ 開発ガイド

このドキュメントは、Memoripassの開発環境セットアップと開発の進め方を説明します。

---

## 目次

1. [前提条件](#前提条件)
2. [開発環境のセットアップ](#開発環境のセットアップ)
3. [プロジェクト構造](#プロジェクト構造)
4. [ビルドと実行](#ビルドと実行)
5. [テスト](#テスト)
6. [デバッグ](#デバッグ)
7. [コーディング規約](#コーディング規約)

---

## 前提条件

### 必須

- **OS**: Ubuntu Linux 22.04+ または macOS 12+
- **JDK**: OpenJDK 17 (推奨: Temurin)
- **Android Studio**: Koala | 2024.1.1 以降
- **Android SDK**: API Level 35 (Android 15)
- **Git**: 2.30+
- **実機**: Google Pixel 9 (StrongBox必須)

### 推奨

- **メモリ**: 16GB以上
- **ストレージ**: 10GB以上の空き容量

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

# JAVA_HOMEを設定
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

#### macOS

```bash
# Homebrewでインストール
brew install openjdk@17

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
# https://developer.android.com/studio
```

#### macOS

```bash
# Homebrewでインストール
brew install --cask android-studio
```

---

### Step 3: Android SDKのセットアップ

Android Studio初回起動時に、SDK Manager が開きます：

**SDK Platforms**:
- ✅ Android 15.0 (API Level 35)

**SDK Tools**:
- ✅ Android SDK Build-Tools 35.0.0
- ✅ Android SDK Command-line Tools
- ✅ Android Emulator
- ✅ Android SDK Platform-Tools

---

### Step 4: プロジェクトを開く

1. Android Studioを起動
2. **Open** をクリック
3. `memoripass` ディレクトリを選択
4. Gradleの同期を待つ

---

## プロジェクト構造

```
memoripass/
├── .github/
│   ├── workflows/           # CI/CD（将来）
│   └── CLAUDE_CONTEXT.md    # AI開発コンテキスト
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/memoripass/
│   │   │   │   ├── auth/            # 認証
│   │   │   │   ├── crypto/          # 暗号化
│   │   │   │   ├── data/            # データ管理
│   │   │   │   ├── ui/              # UI
│   │   │   │   └── util/            # ユーティリティ
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                    # ユニットテスト
│   │   └── androidTest/             # UIテスト
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── docs/
│   ├── requirements/
│   │   └── SRS-v2.0.md              # 要件定義書
│   ├── design/
│   │   └── architecture.md          # アーキテクチャ
│   └── ai-sessions/                 # AI相談ログ
│
├── prompts/                          # 再利用プロンプト
│
├── README.md
├── DEVELOPMENT.md                    # このファイル
├── SECURITY.md
├── AI_COLLABORATION.md
└── LICENSE
```

---

## ビルドと実行

### Gradle コマンド

```bash
# クリーン
./gradlew clean

# デバッグビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# テスト実行
./gradlew test

# Lint
./gradlew lint
```

---

### 実機での実行

1. **Google Pixel 9をUSBで接続**
2. **開発者オプションを有効化**
   - 設定 → デバイス情報 → ビルド番号を7回タップ
3. **USBデバッグを有効化**
   - 設定 → システム → 開発者向けオプション → USBデバッグ
4. **Android Studioから実行**
   - ▶️（Run）ボタンをクリック

---

## テスト

### ユニットテスト

```bash
# すべてのテストを実行
./gradlew test

# カバレッジレポート生成
./gradlew testDebugUnitTestCoverage
# レポート: app/build/reports/coverage/test/debug/index.html
```

### テストの書き方

```java
import org.junit.Test;
import static org.junit.Assert.*;

public class CryptoManagerTest {
    
    @Test
    public void testEncryptDecrypt() {
        CryptoManager manager = new CryptoManager();
        String plaintext = "TestPassword123";
        
        byte[] encrypted = manager.encrypt(plaintext);
        String decrypted = manager.decrypt(encrypted);
        
        assertEquals(plaintext, decrypted);
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
        Log.d(TAG, "Encrypting data...");
        // 処理
    }
}
```

⚠️ **重要**: センシティブな情報は**絶対にログ出力しない**

---

### Android Studio デバッガ

1. ブレークポイントを設定（行番号の左をクリック）
2. 🐞（Debug）ボタンでアプリを起動
3. Step Over (F8) / Step Into (F7) で実行

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
```

---

### Javadoc

```java
/**
 * パスワードを暗号化するクラス
 * 
 * <p>AES-256-GCMを使用してパスワードを暗号化します。</p>
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
- [ ] 適切な入力検証

---

## Git ワークフロー

### 開発フロー

```bash
# 1. 機能ブランチを作成
git checkout -b feature/FR-AUTH-01-biometric

# 2. 開発・コミット
git add .
git commit -m "feat(auth): Implement BiometricPrompt

- Add AuthenticationManager class
- Support fingerprint and face authentication"

# 3. mainにマージ
git checkout main
git merge feature/FR-AUTH-01-biometric

# 4. プッシュ
git push origin main
```

---

### コミットメッセージ規約

```
<type>(<scope>): <subject>

<body>
```

**Type**:
- `feat`: 新機能
- `fix`: バグ修正
- `docs`: ドキュメント変更
- `refactor`: リファクタリング
- `test`: テスト追加・修正

**例**:
```
feat(crypto): Add AES-256-GCM encryption

- Implement CryptoManager with GCM mode
- Add IV generation using SecureRandom
```

---

## トラブルシューティング

### Gradle同期エラー

```bash
./gradlew clean
rm -rf ~/.gradle/caches/

# Android Studio: File → Invalidate Caches → Invalidate and Restart
```

---

### "SDK location not found"

```bash
echo "sdk.dir=$HOME/Android/Sdk" > local.properties
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
- [セキュリティポリシー](SECURITY.md)

---

**最終更新**: 2026年2月2日
