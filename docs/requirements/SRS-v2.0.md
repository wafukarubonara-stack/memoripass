# ソフトウェア要件定義書
**Memoripass (メモリパス)**  
**バージョン**: 2.0  
**作成日**: 2026年2月1日  
**ステータス**: Approved

---

## 改訂履歴

| バージョン | 日付 | 作成者 | 変更内容 |
|---|---|---|---|
| 1.0 | 2026-01-15 | - | 初版作成 |
| 1.3 | 2026-01-25 | - | セキュリティ要件追加 |
| 2.0 | 2026-02-01 | - | 全体再構成、AI開発向け最適化 |

---

## 目次

1. [プロジェクト概要](#1-プロジェクト概要)
2. [スコープ](#2-スコープ)
3. [機能要件](#3-機能要件)
4. [非機能要件](#4-非機能要件)
5. [制約条件](#5-制約条件)
6. [受入基準](#6-受入基準)
7. [リスクと対策](#7-リスクと対策)

---

## 1. プロジェクト概要

### 1.1 システム名称
Memoripass（メモリパス）

### 1.2 開発目的
既存のクラウド型パスワード管理サービスにおける以下のリスクを完全に排除する:
- 通信傍受による情報漏洩
- サーバー侵害による大規模漏洩
- サービス終了によるデータ消失

これらを解決するため、**完全端末内完結型**の高セキュリティパスワード管理アプリを開発する。

### 1.3 ターゲットユーザー
- セキュリティ意識の高い個人ユーザー
- 機密情報を扱う専門職（弁護士、医師、研究者など）
- 企業の情報管理担当者

### 1.4 技術環境

| 項目 | 内容 |
|---|---|
| ターゲットデバイス | Google Pixel 9 (Android 15以降) |
| 開発環境 | Ubuntu Linux, Android Studio |
| プログラミング言語 | Java (JDK 17) |
| 最小SDKバージョン | API Level 35 (Android 15) |
| ターゲットSDK | API Level 35以降 |

---

## 2. スコープ

### 2.1 対象範囲（In Scope）
- Android端末上でのパスワード管理機能
- 生体認証による安全なアクセス制御
- 端末内完結型の暗号化データ保管
- 手動エクスポート/インポート機能

### 2.2 対象外範囲（Out of Scope）
- クラウド同期機能
- 複数デバイス間の自動同期
- Webブラウザ拡張機能
- iOS版アプリ
- パスワード自動入力機能（Autofill Service）- v1.0では対象外
- 他社パスワード管理ツールからの自動インポート

### 2.3 将来の拡張可能性
- VPN経由の暗号化バックアップ（v2.0以降）
- 自己ホスト型サーバーとの同期（v2.5以降）
- Autofill Service対応（v3.0以降）

---

## 3. 機能要件

### 3.1 認証・アクセス制御

#### FR-AUTH-01: 起動時認証【必須】
**優先度**: P0（最高）  
**実装フェーズ**: Phase 1

**要件内容**:
- アプリ起動時、必ずOS標準の`BiometricPrompt`を呼び出す
- 指紋認証または顔認証（クラス3）を要求
- 認証成功まではいかなる画面も表示しない

**受入基準**:
- 起動から認証ダイアログ表示まで1.0秒以内
- 認証失敗時は適切なエラーメッセージを表示
- 3回連続失敗後はデバイスPIN/パターンにフォールバック

**テストケース**:
- 指紋認証成功パターン
- 顔認証成功パターン
- 認証失敗後のリトライ
- フォールバック移行

---

#### FR-AUTH-02: オートロック【必須】
**優先度**: P0  
**実装フェーズ**: Phase 1

**要件内容**:
以下の条件で自動的にセッションを破棄:
- アプリがバックグラウンドに移動後30秒経過
- 画面消灯時（即時）
- ユーザーによる手動ロック操作

復帰時は再認証を要求する。

**受入基準**:
- バックグラウンド移行から正確に30秒（±0.5秒）でロック
- 画面消灯時は即座にメモリ内のセンシティブデータをクリア
- ロック時に一覧画面から空画面に切り替わる

**実装詳細**:
- `Application.ActivityLifecycleCallbacks`を使用
- `Handler.postDelayed()`で30秒タイマー
- `onPause()`でメモリクリア

---

#### FR-AUTH-03: フォールバック認証【必須】
**優先度**: P0  
**実装フェーズ**: Phase 1

**要件内容**:
以下の場合、デバイスPIN/パターン認証にフォールバック:
- 生体認証に3回連続失敗
- 生体センサーの故障またはエラー
- 生体情報が登録されていない端末

**受入基準**:
- フォールバック移行時に適切な説明メッセージを表示
- PIN入力は標準のAndroid認証UIを使用
- フォールバック後も同等のセキュリティレベルを維持

---

### 3.2 データ管理

#### FR-DATA-01: アカウント情報登録【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

**基本フィールド**（必須）:
- サイト名/サービス名（最大100文字）
- ユーザーID/メールアドレス（最大200文字）
- パスワード（最大500文字）

**追加フィールド**（任意、無制限）:
- カスタムフィールド名と値のペア
- 例: 取引パスワード、秘密の質問、PINコード、備考など

**受入基準**:
- 1アカウントあたり最大20個のカスタムフィールド
- フィールド名は最大50文字、値は最大500文字
- サイト名の重複を警告（禁止はしない）

**データモデル**:
```json
{
  "id": "uuid",
  "siteName": "GitHub",
  "userId": "user@example.com",
  "password": "encrypted",
  "customFields": [
    {"name": "2FA Secret", "value": "encrypted"},
    {"name": "Recovery Code", "value": "encrypted"}
  ],
  "createdAt": "2026-02-01T10:00:00Z",
  "updatedAt": "2026-02-01T10:00:00Z"
}
```

---

#### FR-DATA-02: CRUD操作【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

**要件内容**:
- **作成**: 新規アカウント情報の登録
- **読取**: 登録済み情報の参照
- **更新**: 既存情報の編集
- **削除**: 論理削除（30日間は復元可能）+ 完全削除

**受入基準**:
- 各操作後、即座にデータを暗号化して保存
- 削除時は確認ダイアログを表示
- ごみ箱機能で30日以内の復元を可能にする

---

#### FR-DATA-03: 検索・フィルタ機能【必須】
**優先度**: P1  
**実装フェーズ**: Phase 3

**要件内容**:
- インクリメンタルサーチ（リアルタイム絞り込み）
- 検索対象: サイト名、ID、カスタムフィールド名
- カテゴリによるフィルタリング（任意タグ機能）

**受入基準**:
- 1000件登録時でも検索結果表示0.3秒以内
- 検索履歴の保存（最大10件）
- 大文字小文字を区別しない検索

---

#### FR-DATA-04: データ整合性保証【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

**要件内容**:
- アトミック保存（書き込み途中のクラッシュでもデータ破損なし）
- 保存前後のチェックサム検証
- 破損検出時の自動バックアップからの復元

**実装方式**:
- Write-Ahead Logging (WAL) パターン
- 一時ファイルへの書き込み → アトミックなrename
- SHA-256ハッシュによる整合性チェック

**受入基準**:
- 起動時の整合性チェック実行
- 破損検出時のユーザー通知
- 復元成功率100%

---

### 3.3 セキュリティ支援機能

#### FR-SEC-01: パスワード生成【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

**生成仕様**:
- `java.security.SecureRandom`を使用
- 長さ: 8〜64文字（デフォルト16文字）
- 文字種: 英大文字/英小文字/数字/記号（個別ON/OFF可能）
- 記号の種類: `!@#$%^&*()-_=+[]{}|;:,.<>?`
- 類似文字の除外オプション（0/O、1/I/lなど）

**受入基準**:
- エントロピー128bit以上を保証
- 生成時間0.1秒以内
- 文字種のランダム性検証

---

#### FR-SEC-02: パスワード強度評価【必須】
**優先度**: P1  
**実装フェーズ**: Phase 3

**評価基準**:
登録・編集時に以下を評価:
- 長さ（8文字未満: 弱、8-12: 普通、13-16: 強、17+: 非常に強）
- 文字種の多様性（1種: 弱、2種: 普通、3種: 強、4種: 非常に強）
- 辞書攻撃への耐性（一般的な単語チェック）
- 連続文字のチェック（"123456", "abcdef"など）

5段階評価で表示: 非常に弱い/弱い/普通/強い/非常に強い

---

#### FR-SEC-03: セキュアコピー【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

**要件内容**:
- パスワードコピー後、60秒で自動的にクリップボードをクリア
- コピー実行時にトースト通知で警告
- クリア前のカウントダウン表示（通知バーに）

**受入基準**:
- 60秒（±1秒）で確実にクリア
- バックグラウンド移行時も動作継続
- Android 13以降のクリップボード監視に対応

**実装詳細**:
```java
ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
ClipData clip = ClipData.newPlainText("password", password);
clipboard.setPrimaryClip(clip);

// 60秒後にクリア
handler.postDelayed(() -> {
    clipboard.setPrimaryClip(ClipData.newPlainText("", ""));
}, 60000);
```

---

### 3.4 エクスポート・インポート機能

#### FR-EXPORT-01: 手動エクスポート【必須】
**優先度**: P0  
**実装フェーズ**: Phase 4

**エクスポート仕様**:
- 形式: JSON（AES-256-GCMで暗号化）
- パスワード保護（独自のエクスポートパスワード設定）
- ファイル名: `memoripass_backup_YYYYMMDD_HHmmss.mpb`
- 保存先: Androidの共有機能（Sharesheet）経由

**ファイル構造**:
```json
{
  "version": "1.0",
  "exportDate": "2026-02-01T10:00:00Z",
  "encryptionMethod": "AES-256-GCM",
  "data": "base64-encoded-encrypted-data",
  "checksum": "sha256-hash"
}
```

**受入基準**:
- エクスポート実行前に警告ダイアログ表示
- ファイルサイズ上限100MB
- エクスポート履歴の記録（日時、件数）

---

#### FR-EXPORT-02: インポート【必須】
**優先度**: P0  
**実装フェーズ**: Phase 4

**インポート仕様**:
- `.mpb`ファイルの読み込み
- エクスポートパスワードによる復号
- 既存データとの統合（上書き/マージ/スキップを選択可能）

**受入基準**:
- ファイル検証（フォーマット、バージョン互換性）
- インポート前のプレビュー表示
- ロールバック機能（インポート失敗時の復元）

---

### 3.5 UI/UX要件

#### FR-UI-01: ダークモード対応【必須】
**優先度**: P1  
**実装フェーズ**: Phase 3

- システム設定に連動したダークモード
- 手動切り替えオプション（ライト/ダーク/システム連動）

---

#### FR-UI-02: 情報の秘匿表示【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

- パスワードフィールドは伏せ字（`●●●●●●●●`）で表示
- タップで一時的に表示（5秒後に自動で再伏せ字化）
- 「表示/非表示」トグルボタン（目のアイコン）

---

## 4. 非機能要件

### 4.1 セキュリティ要件

#### NFR-SEC-01: 暗号化【必須】
**優先度**: P0  
**実装フェーズ**: Phase 1

**暗号化仕様**:
- **アルゴリズム**: AES-256-GCM（認証付き暗号）
- **鍵長**: 256bit
- **IV（初期化ベクトル）**: 各暗号化操作ごとに`SecureRandom`で生成（12バイト）
- **認証タグ**: 128bit

**実装例**:
```java
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
GCMParameterSpec spec = new GCMParameterSpec(128, iv);
cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
```

**受入基準**:
- NIST SP 800-38D準拠
- 暗号化/復号化の単体テスト実施
- テストベクトルによる検証

---

#### NFR-SEC-02: 鍵管理【必須】
**優先度**: P0  
**実装フェーズ**: Phase 1

**鍵管理仕様**:
- **鍵保管場所**: Pixel 9のTitan M2チップ内StrongBox
- **鍵生成**: `AndroidKeyStore`の`setIsStrongBoxBacked(true)`
- **鍵のライフサイクル**: アプリアンインストール時のみ削除
- **鍵の抽出**: 不可能（Hardware-backed）

**実装例**:
```java
KeyGenerator keyGen = KeyGenerator.getInstance(
    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
    "memoripass_master_key",
    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
    .setKeySize(256)
    .setIsStrongBoxBacked(true) // Titan M2使用
    .setUserAuthenticationRequired(true)
    .setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG)
    .build();

keyGen.init(spec);
SecretKey key = keyGen.generateKey();
```

**受入基準**:
- `KeyInfo.isInsideSecureHardware()`がtrueを返すこと
- StrongBox非対応端末での適切なエラーハンドリング

---

#### NFR-SEC-03: 画面保護【必須】
**優先度**: P0  
**実装フェーズ**: Phase 1

**要件内容**:
以下を全画面に適用:
- `FLAG_SECURE`: スクリーンショット、画面録画の禁止
- タスクスイッチャーでの内容非表示

**実装例**:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    );
    setContentView(R.layout.activity_main);
}
```

**受入基準**:
- 全Activity/Fragmentでの設定確認
- スクリーンショット試行時のブロック確認

---

#### NFR-SEC-04: コード難読化【必須】
**優先度**: P0  
**実装フェーズ**: Release前

**難読化仕様**:
- **リリースビルド**: R8/ProGuardによる難読化
- **難読化レベル**: 最高（クラス名、メソッド名、フィールド名）
- **Mapping保存**: デバッグ用に保管

**proguard-rules.pro**:
```proguard
# センシティブなクラスは特に強力に難読化
-keepclassmembers class com.memoripass.crypto.** { *; }
-keep class androidx.biometric.** { *; }

# リフレクション使用クラスは保持
-keepattributes Signature
-keepattributes *Annotation*
```

---

#### NFR-SEC-05: メモリ保護【必須】
**優先度**: P0  
**実装フェーズ**: Phase 2

**要件内容**:
- センシティブデータのメモリ保持時間を最小化
- 使用後の明示的なゼロクリア
- GC待ちではなく即座にクリア

**実装例**:
```java
// String は不変なので char[] を使用
char[] password = getPasswordFromUser();
try {
    // パスワード使用
    processPassword(password);
} finally {
    // 即座にゼロクリア
    Arrays.fill(password, '\0');
}
```

**受入基準**:
- センシティブデータは`char[]`で管理
- `finally`ブロックでの確実なクリア
- メモリダンプでの検証

---

### 4.2 パフォーマンス要件

#### NFR-PERF-01: 応答時間【必須】
**優先度**: P0  
**実装フェーズ**: 全Phase

| 操作 | 目標時間 |
|---|---|
| アプリ起動→認証ダイアログ表示 | 1.0秒以内 |
| 認証成功→一覧画面表示 | 0.5秒以内 |
| データ復号化（1000件） | 0.5秒以内 |
| 検索結果表示 | 0.3秒以内 |
| 新規登録・更新の保存 | 0.2秒以内 |

**測定方法**:
- Android Profilerでの計測
- `System.currentTimeMillis()`での時間測定
- Firebase Performance Monitoringでの本番監視

---

### 4.3 ネットワーク要件

#### NFR-NET-01: 完全オフライン動作【必須】
**優先度**: P0  
**実装フェーズ**: Phase 1

**要件内容**:
- `android.permission.INTERNET`の非宣言
- OSレベルでの通信遮断
- ネットワークAPIの未使用

**AndroidManifest.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- INTERNET権限は宣言しない -->
    
    <application
        android:usesCleartextTraffic="false"
        ...>
    </application>
</manifest>
```

**受入基準**:
- AndroidManifest.xmlの確認
- 静的解析ツールでのネットワークコール検出0件
- 実機でのネットワーク通信監視

---

## 5. 制約条件

### 5.1 技術的制約

| 制約項目 | 内容 |
|---|---|
| 対象OS | Android 15以降のみ |
| 対象デバイス | Google Pixelシリーズ（StrongBox搭載機種） |
| ネットワーク | 完全オフライン（INTERNET権限なし） |
| ストレージ | 内部ストレージのみ（外部SDカード非対応） |
| 生体認証 | クラス3（強力な生体認証）必須 |

### 5.2 運用制約

| 制約項目 | 内容 |
|---|---|
| データ復旧 | 不可能（マスター認証情報紛失時） |
| バックアップ | 手動のみ（自動バックアップなし） |
| 移行 | エクスポート/インポートのみ |

---

## 6. 受入基準

### 6.1 機能受入基準

以下をすべて満たすこと:
- [ ] 全機能要件（P0）の実装完了
- [ ] 全機能要件（P1）の80%以上実装
- [ ] 受入テストケース100%実施
- [ ] クリティカルバグ0件
- [ ] メジャーバグ5件以下

### 6.2 性能受入基準

以下をすべて満たすこと:
- [ ] NFR-PERF-01の全項目クリア（Pixel 9実機）
- [ ] クラッシュ率0.1%以下
- [ ] ANR率0.05%以下
- [ ] メモリリーク0件

### 6.3 セキュリティ受入基準

以下をすべて満たすこと:
- [ ] 全セキュリティ要件（NFR-SEC-XX）の実装完了
- [ ] OWASP Mobile Top 10チェックリスト完了
- [ ] 静的解析ツール（SonarQube等）でクリティカル脆弱性0件
- [ ] コード難読化の検証完了

---

## 7. リスクと対策

### 7.1 技術リスク

| リスク | 影響度 | 発生確率 | 対策 |
|---|---|---|---|
| StrongBoxの互換性問題 | 高 | 中 | 初期段階での実機検証、フォールバック実装 |
| Android OSアップデートでの動作不良 | 高 | 中 | Betaプログラムでの事前検証 |
| 暗号化性能の低下 | 中 | 低 | 非同期処理、プログレス表示 |

### 7.2 セキュリティリスク

| リスク | 影響度 | 発生確率 | 対策 |
|---|---|---|---|
| ゼロデイ脆弱性（Android OS） | 高 | 低 | 迅速なパッチ適用 |
| サイドチャネル攻撃 | 中 | 低 | タイミング攻撃対策 |
| ソーシャルエンジニアリング | 高 | 中 | ユーザー教育 |

---

## 8. 脅威モデリングと対策

| 脅威分類 | 具体的脅威 | 影響 | 対策 | 残存リスク |
|---|---|---|---|---|
| **物理的攻撃** | 端末の盗難・紛失 | 高 | Titan M2 + AES-256 + 生体認証 | 低 |
| | 画面の覗き見 | 中 | 伏せ字表示、プライバシースクリーン | 中 |
| **マルウェア** | スクリーンレコーダー型 | 高 | FLAG_SECURE | 低 |
| | キーロガー型 | 高 | OS標準キーボードの推奨 | 中 |
| **ネットワーク** | 通信傍受 | なし | INTERNET権限なし | なし |
| **リバースエンジニアリング** | APK解析 | 中 | R8難読化 | 中 |

---

## 付録A: 用語集

| 用語 | 説明 |
|---|---|
| StrongBox | Android 9以降で利用可能な、ハードウェアベースの鍵保管機能 |
| Titan M2 | Google Pixelシリーズに搭載されるセキュリティチップ |
| BiometricPrompt | Android標準の生体認証API |
| FLAG_SECURE | 画面キャプチャを防止するWindowフラグ |
| AES-256-GCM | 256bitAES暗号をGalois/Counterモードで使用する認証付き暗号 |

---

## 付録B: 開発フェーズ

### Phase 1: 基盤構築（2週間）
- FR-AUTH-01, FR-AUTH-02, FR-AUTH-03
- NFR-SEC-01, NFR-SEC-02, NFR-SEC-03

### Phase 2: コア機能（3週間）
- FR-DATA-01, FR-DATA-02, FR-DATA-04
- FR-SEC-01, FR-SEC-03
- FR-UI-02

### Phase 3: 拡張機能（2週間）
- FR-DATA-03
- FR-SEC-02
- FR-UI-01

### Phase 4: エクスポート/テスト（2週間）
- FR-EXPORT-01, FR-EXPORT-02
- 総合テスト

**合計**: 約9週間（リリース準備含め10週間）

---

**最終更新**: 2026年2月1日  
**次回レビュー予定**: 2026年3月1日
