# APK署名設定 完全解説

> 情報処理安全支援士 試験対策資料 | Memoripass v1.0 開発記録 | 2026-02-14

---

## 目次

| No. | セクション | 試験関連キーワード |
|-----|-----------|----------------|
| 1 | [全体の流れ](#1-全体の流れ) | デジタル署名の概要 |
| 2 | [キーストアの作成](#2-キーストアの作成) | keytoolコマンド・JKS・RSA・有効期間 |
| 3 | [自己署名証明書](#3-自己署名証明書x509) | X.509・CA・SHA256withRSA・DN |
| 4 | [keystore.properties](#4-keystoreproperties秘密情報の分離) | 秘密情報の分離・ハードコーディング禁止 |
| 5 | [build.gradle.kts](#5-buildgradlekts署名設定の実装) | 署名設定の実装・安全な読み込み方法 |
| 6 | [署名スキームの検証](#6-署名スキームの検証) | v1/v2/v3の違い・apksignerによる確認 |
| 7 | [デジタル署名の目的](#7-デジタル署名の目的) | 完全性・認証・否認防止・機密性との違い |
| 8 | [試験頻出まとめ](#8-試験頻出キーワードまとめ) | 重要キーワード一覧・ひっかけ問題 |

---

## 1. 全体の流れ

APK署名は以下の4つのステップで完成します。
```
キーストア作成  →  keystore.properties  →  build.gradle.kts  →  署名済みAPK
  (STEP 1)           (STEP 2)                 (STEP 3)             (STEP 4)
```

| STEP | ファイル | Gitに含める | 内容 |
|------|---------|-----------|------|
| 1 | `keystore/memoripass.jks` | ❌ 除外必須 | 秘密鍵・公開鍵・自己署名証明書 |
| 2 | `keystore/keystore.properties` | ❌ 除外必須 | キーストアのパスワード等 |
| 3 | `app/build.gradle.kts` | ✅ 含める | 署名設定の参照コード（パスワードなし） |
| 4 | `app-release.apk` | ❌ 通常除外 | 署名済みリリースAPK |

---

## 2. キーストアの作成

キーストアとはJava KeyStore（JKS）形式のファイルで、秘密鍵・公開鍵・証明書を安全に保管します。

### 実行したコマンド

\`\`\`bash
keytool -genkey -v \
  -keystore keystore/memoripass.jks \
  -alias memoripass \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
\`\`\`

### 各オプションの解説

| オプション | 値 | 解説 | 試験ポイント |
|-----------|-----|------|-----------|
| `-keystore` | memoripass.jks | キーストアファイルの保存先 | JKS = Java KeyStore。秘密鍵の保管庫 |
| `-alias` | memoripass | 鍵のエイリアス（別名） | 1つのJKSに複数の鍵を保管可能。エイリアスで区別 |
| `-keyalg` | RSA | 公開鍵暗号アルゴリズム | RSA = 公開鍵暗号。素因数分解の困難性に基づく |
| `-keysize` | 2048 | 鍵長（ビット数） | NIST推奨: 2048bit以上。2030年以降も継続推奨 |
| `-validity` | 10000 | 有効期間（日数）= 約27年 | 一般的なSSL証明書は1〜2年。個人配布用は長期可 |

### JKSファイルの内部構造

\`\`\`
memoripass.jks
├── 秘密鍵（Private Key）  ← 外部に絶対出さない
├── 公開鍵（Public Key）   ← 署名検証に使用
└── 自己署名証明書（X.509）← 公開鍵と所有者情報をまとめたもの
\`\`\`

### RSA暗号のしくみ（試験頻出）

> RSAは公開鍵暗号方式の代表的なアルゴリズムです。「大きな数の素因数分解は計算困難」という数学的性質を利用します。秘密鍵で署名し、対応する公開鍵で検証することで「秘密鍵の持ち主が署名した」ことを証明できます。鍵長が長いほど安全ですが処理コストも増加します。現在の推奨は2048bit以上です。

---

## 3. 自己署名証明書（X.509）

### 生成された証明書の内容

\`\`\`
10,000日間有効な2,048ビットのRSAのキー・ペアと
自己署名型証明書（SHA256withRSA）を生成しています
ディレクトリ名: CN=Eliza, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=JP
\`\`\`

### X.509証明書のDN（識別名）フィールド

| 略称 | 正式名称 | 入力値 | SSL証明書での使用例 |
|-----|---------|-------|-----------------|
| CN | Common Name | Eliza | ドメイン名（例: example.com） |
| OU | Organizational Unit | Unknown | 部署名（例: Engineering） |
| O | Organization | Unknown | 組織名（例: Acme Corp） |
| L | Locality | Unknown | 市区町村（例: Tokyo） |
| ST | State/Province | Unknown | 都道府県（例: Tokyo） |
| C | Country | JP | 国コード2文字（ISO 3166-1） |

### 通常の証明書 vs 自己署名証明書（試験頻出）

| 項目 | 通常のSSL/TLS証明書 | APKの自己署名証明書 |
|-----|------------------|----------------|
| 署名者 | 認証局（CA）が署名 | 開発者自身が署名 |
| 信頼の根拠 | ルートCAへの信頼チェーン（PKI） | 開発者を直接信頼 |
| 検証方法 | ブラウザがCAを自動検証 | AndroidがAndroid OSが開発者の公開鍵で検証 |
| 有効期限 | 1〜2年が一般的 | 長期間（今回は約27年） |
| 用途 | Webサイト・APIの認証 | アプリの開発者認証・改ざん検知 |
| Play Store | 不要 | Googleが別途アプリを審査 |

### SHA256withRSAの意味

| 部分 | 内容 | 解説 |
|-----|------|------|
| SHA256 | ハッシュ関数 | APKデータからハッシュ値（256bit）を生成。データの「指紋」として機能 |
| with | 組み合わせ | ハッシュ関数と署名アルゴリズムの組み合わせを示す |
| RSA | 署名アルゴリズム | 生成したハッシュ値をRSA秘密鍵で暗号化して署名を作成 |

### ハッシュ関数の比較（試験頻出）

| アルゴリズム | 出力長 | 安全性 | 備考 |
|-----------|-------|-------|------|
| MD5 | 128bit | ❌ 危険 | 衝突攻撃が実証済み。使用禁止 |
| SHA-1 | 160bit | ⚠️ 非推奨 | 衝突攻撃が可能。廃止推進 |
| SHA-256 | 256bit | ✅ 現在の標準 | 今回使用。NISTが推奨 |
| SHA-3 | 可変 | ✅ 次世代標準 | SHA-2の後継として標準化 |

---

## 4. keystore.properties（秘密情報の分離）

### ファイルの内容

\`\`\`properties
storeFile=keystore/memoripass.jks
storePassword=（キーストアのパスワード）
keyAlias=memoripass
keyPassword=（鍵のパスワード）
\`\`\`

### 2種類のパスワードがある理由

| パスワード種別 | 保護対象 | 比喩 |
|-------------|---------|------|
| storePassword | JKSファイル全体 | 金庫の扉の鍵 |
| keyPassword | JKS内の個別の鍵 | 金庫内の各引き出しの鍵 |

1つのJKSに複数のエイリアス（鍵）を保管できるため、鍵ごとに個別パスワードを設定できる設計になっています。

### なぜbuild.gradle.ktsに直接書いてはいけないか（試験頻出：ハードコーディング禁止）

| 種別 | コード例 | 問題点 |
|-----|---------|-------|
| ❌ 悪い例（ハードコーディング） | `storePassword = "mysecret"` をbuild.gradle.ktsに直接記述 | Gitにコミット→GitHubに公開→世界中に漏洩。攻撃者が同じ署名でAPKを偽造可能 |
| ✅ 良い例（分離管理） | keystore.propertiesに記載し.gitignoreで除外 | パスワードはGitに含まれない。コードと認証情報を分離 |

### .gitignoreへの追加

\`\`\`gitignore
# キーストア関連ファイルを除外
keystore/
keystore/keystore.properties
\`\`\`

> ⚠️ **重要**: 秘密鍵が漏洩した場合、攻撃者は同じ署名で悪意のあるAPKを作成し、正規アプリに見せかけて配布できます。これは「なりすまし攻撃」に分類されます。

---

## 5. build.gradle.kts（署名設定の実装）

### 完成したbuild.gradle.kts（署名設定部分）

\`\`\`kotlin
// Step1: Javaクラスのインポート（Kotlin DSLでは明示的インポートが必要）
import java.util.Properties
import java.io.FileInputStream

// Step2: keystore.propertiesの読み込み
val keystorePropertiesFile = rootProject.file("keystore/keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {  // ファイルがない環境でもエラーにならない
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    // Step3: 署名設定の定義
    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    // Step4: リリースビルドに署名設定を適用
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")  // 署名設定を適用
        }
    }
}
\`\`\`

### 重要なポイント解説

| コード | 解説 |
|-------|------|
| `if (keystorePropertiesFile.exists())` | CI/CD環境や他の開発者の環境でkeystoreがなくてもビルドエラーにならない |
| `rootProject.file(...)` | プロジェクトルートからの相対パスで解決。app/内からも正しくルートを参照できる |
| `isMinifyEnabled = true` | ProGuardによるコードの難読化・最適化を有効化。リバースエンジニアリング対策にもなる |
| `isShrinkResources = true` | 使用していないリソースを削除してAPKサイズを削減 |

---

## 6. 署名スキームの検証

### apksignerによる検証結果

\`\`\`bash
$ ~/Android/Sdk/build-tools/35.0.0/apksigner verify --verbose app-release.apk

Verifies
Verified using v1 scheme (JAR signing):                 false
Verified using v2 scheme (APK Signature Scheme v2):     true  ← 有効！
Verified using v3 scheme (APK Signature Scheme v3):     false
Verified using v3.1 scheme (APK Signature Scheme v3.1): false
Verified using v4 scheme (APK Signature Scheme v4):     false
Number of signers: 1
\`\`\`

### 各署名スキームの比較（試験頻出）

| スキーム | 導入 | 今回 | 特徴・試験ポイント |
|---------|------|-----|----------------|
| v1 (JAR signing) | Android 1.0〜 | false | APK内の各ファイルにJAR署名。META-INFフォルダへのファイル追加は検知できない脆弱性があった |
| v2 (APK Sig v2) | Android 7.0〜 | **true ✅** | APKバイナリ全体を「APK署名ブロック」で保護。1バイトの改ざんも検知可能。検証速度も向上 |
| v3 (APK Sig v3) | Android 9.0〜 | false | v2に加えて鍵のローテーション（鍵の更新）が可能になった |
| v3.1 | Android 13〜 | false | v3の改良版。ターゲットSDKに基づく署名の選択が可能 |
| v4 | Android 11〜 | false | インクリメンタルインストール対応。v2またはv3の署名も必要 |

### v1とv2の根本的な違い

| 項目 | v1 (JAR signing) | v2 (APK Signature Scheme v2) |
|-----|----------------|---------------------------|
| 署名対象 | APK内の各ファイル個別 | APKファイル全体（バイナリ） |
| 署名場所 | META-INF/内 | APKファイルのZIPデータの前に挿入された署名ブロック |
| 脆弱性 | META-INFへのファイル追加を検知できない | ファイル全体を保護するため改ざん不可 |
| 検証速度 | 各ファイルを解凍して検証（遅い） | APK全体をスキャンして検証（速い） |

---

## 7. デジタル署名の目的

### デジタル署名で実現できること・できないこと（試験最頻出）

| セキュリティ目的 | 実現 | APKでの意味 | 試験ポイント |
|---------------|-----|-----------|-----------|
| 完全性（Integrity） | ✅ 実現 | APKが改ざんされていないことを保証。ハッシュ値の一致で確認 | ハッシュ値が変化すれば即座に検知可能 |
| 認証（Authentication） | ✅ 実現 | 同じ開発者が作ったことを保証。秘密鍵の所有者のみが署名可能 | 公開鍵で署名を検証し開発者を確認 |
| 否認防止（Non-repudiation） | ✅ 実現 | 「私は作っていない」と言えない。秘密鍵で署名した事実は消せない | デジタル署名の重要な特性 |
| 機密性（Confidentiality） | ❌ 実現不可 | 署名はデータを暗号化しない。APKの内容は誰でも読める | **頻出ひっかけ問題！署名≠暗号化** |

### デジタル署名のしくみ

\`\`\`
【署名する側（開発者）】
APKデータ
  → SHA-256ハッシュ関数
  → ハッシュ値（256bit）
  → RSA秘密鍵で暗号化
  → 署名

【検証する側（Android OS）】
署名 → RSA公開鍵で復号 → ハッシュ値A
APKデータ → SHA-256ハッシュ関数 → ハッシュ値B

ハッシュ値A = ハッシュ値B ? → 一致すれば「正規のAPK」と判定
\`\`\`

### 情報セキュリティの3要素（CIA）とデジタル署名

| 3要素（CIA） | 英語 | デジタル署名との関係 |
|------------|------|-----------------|
| 機密性 | Confidentiality | デジタル署名では実現しない。暗号化（AES等）で実現する |
| 完全性 | Integrity | ✅ ハッシュ値で改ざんを検知。デジタル署名の主目的 |
| 可用性 | Availability | デジタル署名では直接実現しない。冗長化等で実現する |

---

## 8. 試験頻出キーワードまとめ

### キーワード一覧

| キーワード | 意味 | 関連項目 |
|-----------|------|---------|
| 公開鍵暗号方式 | 暗号化と復号に異なる鍵を使う方式 | RSA, 楕円曲線暗号 |
| RSA | 素因数分解の困難性に基づく公開鍵暗号 | 鍵長2048bit推奨 |
| デジタル署名 | 秘密鍵で署名・公開鍵で検証する仕組み | 完全性・認証・否認防止 |
| ハッシュ関数 | 任意長データを固定長に変換する一方向関数 | SHA-256, MD5（非推奨） |
| X.509 | 公開鍵証明書の標準形式 | CN, O, C等のDNフィールド |
| PKI | 公開鍵基盤。CAによる証明書の発行・管理 | ルートCA, 中間CA, 証明書チェーン |
| 自己署名証明書 | CAの署名なし、自分で署名した証明書 | オレオレ証明書とも呼ばれる |
| JKS | Java KeyStore。鍵と証明書の保管形式 | 秘密鍵・公開鍵・証明書を格納 |
| CIA | 機密性・完全性・可用性（情報セキュリティの3要素） | デジタル署名は完全性・認証を担保 |
| ハードコーディング禁止 | パスワード等をソースコードに直接記述しない原則 | 環境変数・設定ファイルで分離 |
| なりすまし攻撃 | 秘密鍵漏洩時に攻撃者が偽APKを正規品として配布 | 署名による認証で防止 |
| 衝突攻撃 | 異なるデータから同じハッシュ値を生成する攻撃 | MD5・SHA-1は脆弱 |
| APK署名スキームv2 | APKバイナリ全体を保護する署名方式 | Android 7.0以降で推奨 |
| サイドローディング | Play Store以外からAPKをインストールすること | 提供元不明のアプリを許可が必要 |

### 頻出ひっかけ問題パターン

| 問題パターン | 正しい答え |
|------------|---------|
| 「デジタル署名で機密性を実現できる」 | ❌ 誤り。デジタル署名は機密性を実現しない。暗号化とは別概念 |
| 「自己署名証明書はセキュリティ上問題がある」 | △ 用途による。APK署名では自己署名で問題ない |
| 「SHA-256は256bitの鍵長を持つ」 | ❌ 誤り。SHA-256はハッシュ関数でハッシュ値が256bit。鍵長ではない |
| 「デジタル署名で改ざんを防止できる」 | △ 厳密には「検知」できるが「防止」はできない |
| 「RSA 1024bitは現在も安全」 | ❌ 誤り。1024bitは非推奨。2048bit以上が必要 |

---

*Memoripass v1.0 開発記録 | APK署名設定 完全解説 | 情報処理安全支援士 試験対策資料 | 2026-02-14*
