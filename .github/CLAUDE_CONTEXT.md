# Claude開発支援コンテキスト

> このファイルは、Claudeとの開発セッション開始時に参照してもらうための情報です。

---

## 📋 プロジェクト基本情報

### プロジェクト名
**Memoripass** (メモリパス)

### 目的
完全端末内完結型の高セキュリティパスワード管理Androidアプリ

### キーコンセプト
- 🔒 **ゼロトラスト**: クラウド通信を一切行わない
- 🛡️ **ハードウェアセキュリティ**: Titan M2チップで鍵を保護
- 🎯 **シンプルさ**: セキュリティと使いやすさの両立

---

## 🔧 技術スタック

### 言語・プラットフォーム
- **言語**: Java (JDK 17)
- **プラットフォーム**: Android 15+ (API Level 35+)
- **ターゲットデバイス**: Google Pixel 9シリーズ

### 主要技術
- **暗号化**: AES-256-GCM
- **鍵管理**: AndroidKeyStore (StrongBox)
- **認証**: BiometricPrompt API
- **ビルド**: Gradle 8.x
- **テスト**: JUnit 4, Mockito, Espresso

---

## 📐 開発原則

### セキュリティ最優先
すべての設計判断において、セキュリティを第一優先とする。
利便性とセキュリティがトレードオフの場合、セキュリティを選択。

### シンプルさ
- 過度な抽象化を避ける
- 必要最小限の機能に絞る
- コードの可読性を重視

### テスタビリティ
- すべての重要機能にユニットテスト
- カバレッジ目標: 80%以上
- モックを活用した独立テスト

### ドキュメント重視
- コードと同時にドキュメント更新
- Javadocは必須
- 設計判断の記録

---

## 📝 コーディング規約

### 命名規則
```java
// クラス名: PascalCase
public class AuthenticationManager { }

// メソッド名: camelCase
public void authenticateUser() { }

// 定数: UPPER_SNAKE_CASE
private static final int SESSION_TIMEOUT = 1800;

// 変数名: camelCase
private String userName;

// パッケージ名: 小文字、ドット区切り
package com.memoripass.crypto;
```

### コメント
- **クラス**: 必ずJavadocコメント
- **public/protected メソッド**: Javadocコメント
- **複雑なロジック**: インラインコメント（日本語可）
- **TODO/FIXME**: Issue番号を併記

```java
/**
 * 生体認証を管理するクラス
 * 
 * <p>BiometricPromptを使用して、指紋・顔認証を提供します。
 * 認証失敗時は自動的にデバイスPIN/パターンにフォールバックします。</p>
 * 
 * @see BiometricPrompt
 * @since 1.0
 */
public class AuthenticationManager {
    
    /**
     * 生体認証を実行します
     * 
     * @param callback 認証結果を受け取るコールバック
     * @throws IllegalStateException 生体認証が利用不可の場合
     */
    public void authenticate(AuthCallback callback) {
        // TODO(#123): タイムアウト処理を追加
    }
}
```

---

## 🔐 セキュリティチェックリスト

**コード生成・レビュー時に必ず確認**:

### 1. ログ出力
- [ ] センシティブデータ（パスワード、鍵等）のログ出力なし
- [ ] デバッグビルドでもセンシティブ情報は非出力
- [ ] スタックトレースに秘密情報が含まれない

### 2. エラーハンドリング
- [ ] 例外メッセージで詳細情報を漏らさない
- [ ] ユーザーに見せるエラーは一般的な内容
- [ ] 内部エラーは安全にログ記録

### 3. メモリ管理
- [ ] センシティブデータは`char[]`で管理（`String`禁止）
- [ ] 使用後の即座なゼロクリア（`Arrays.fill()`）
- [ ] `try-finally`で確実にクリア

### 4. ハードコーディング
- [ ] APIキー、秘密鍵等のハードコード禁止
- [ ] テストデータも本番に見える形で含めない
- [ ] 設定値は適切に外部化

### 5. 暗号化
- [ ] 推奨アルゴリズムのみ使用（AES-256-GCM）
- [ ] IVは毎回`SecureRandom`で生成
- [ ] 認証タグのサイズは128bit

### 6. 権限
- [ ] 必要最小限の権限のみ要求
- [ ] 実行時権限チェック実施
- [ ] 権限拒否時の適切な処理

---

## 📂 プロジェクト構造

```
memoripass/
├── docs/
│   ├── requirements/
│   │   └── SRS-v2.0.md          # 要件定義書（最重要）
│   ├── design/
│   │   └── architecture.md       # アーキテクチャ設計
│   └── ai-sessions/              # AI相談ログ
│
├── src/
│   ├── main/
│   │   ├── java/com/memoripass/
│   │   │   ├── auth/            # 認証関連
│   │   │   ├── crypto/          # 暗号化関連
│   │   │   ├── data/            # データ管理
│   │   │   ├── ui/              # UI層
│   │   │   └── util/            # ユーティリティ
│   │   └── res/                 # リソースファイル
│   │
│   └── test/
│       └── java/com/memoripass/ # ユニットテスト
│
└── prompts/                      # 再利用プロンプト集
```

---

## 🎯 現在の開発状況

### 現在のフェーズ
**Phase 0: 基盤構築**

### 完了した作業
- [x] 要件定義書作成（SRS v2.0）
- [x] GitHubリポジトリ初期化
- [x] プロジェクト構造決定
- [x] AI協働ガイド作成

### 次のタスク
- [ ] Android Studioプロジェクト作成
- [ ] Gradleビルド設定
- [ ] 基本的なパッケージ構造作成
- [ ] FR-AUTH-01実装開始（生体認証）

### 直近の目標（2週間）
Phase 1完了: 認証基盤とセキュリティ基盤の構築

---

## 📚 重要ドキュメント参照

### 最優先で確認すべきドキュメント
1. **要件定義書**: `docs/requirements/SRS-v2.0.md`
   - 機能要件（FR-XXX-XX）
   - 非機能要件（NFR-XXX-XX）
   - 受入基準

2. **AI協働ガイド**: `AI_COLLABORATION.md`
   - 効果的な質問方法
   - 相談ログの記録方法

### コード実装時の参照
- Android公式ドキュメント: https://developer.android.com
- OWASP Mobile Security: https://owasp.org/www-project-mobile-security/
- AndroidKeyStore Guide: https://developer.android.com/training/articles/keystore

---

## 🤝 Claudeへの依頼時の基本ルール

### コード生成時
1. **必ずJavadocを含める**
2. **エラーハンドリングを実装**
3. **セキュリティチェックリストに基づく実装**
4. **テストケースも併せて生成**

### レビュー依頼時
1. **OWASP Mobile Top 10の観点で確認**
2. **メモリリークの可能性をチェック**
3. **Androidライフサイクルの考慮**
4. **パフォーマンスへの影響を評価**

### 設計相談時
1. **要件IDを明示**
2. **制約条件を列挙**
3. **セキュリティ要件を強調**
4. **テスタビリティを考慮**

---

## ⚠️ 絶対に避けるべきこと

### コーディング
- ❌ `String`でパスワードを保持
- ❌ センシティブ情報をログ出力
- ❌ ハードコードされた秘密情報
- ❌ `Thread.sleep()`などのブロッキング処理（UIスレッド）

### セキュリティ
- ❌ 独自の暗号化アルゴリズム
- ❌ 固定のIV/Salt
- ❌ Base64だけの「暗号化」
- ❌ MD5/SHA1などの弱いハッシュ

### アーキテクチャ
- ❌ Activity/Fragmentへの過度な責務集中
- ❌ グローバル変数の乱用
- ❌ ハードコーディングされた依存関係

---

## 📊 品質基準

### コードメトリクス
- **テストカバレッジ**: 80%以上
- **Lintエラー**: 0件
- **Lintワーニング**: 10件以下
- **重複コード**: 5%以下

### パフォーマンス（Pixel 9実機）
- **起動時間**: 1.0秒以内
- **UI応答**: 0.5秒以内
- **メモリ使用**: 100MB以下
- **クラッシュ率**: 0.1%以下

---

## 🔄 開発サイクル

### 理想的な1日の流れ

**朝（計画）**:
1. 今日実装する要件を確認
2. Claudeに設計相談
3. 実装方針を決定

**昼（実装）**:
4. コード実装
5. Claudeにレビュー依頼
6. 指摘箇所を修正

**夕（テスト）**:
7. テストコード実装
8. Claudeにテストレビュー
9. カバレッジ確認

**夜（記録）**:
10. ドキュメント更新
11. AI相談ログ記録
12. Git commit & push

---

## 📞 緊急時の対応

### ビルドエラー
1. エラーログ全文をClaudeに共有
2. `build.gradle`と`AndroidManifest.xml`も併せて共有
3. 環境情報（Android Studio version等）を明記

### 実行時エラー
1. Logcatの関連部分を抽出
2. 問題が発生する操作手順を明記
3. 該当コードをピンポイントで提示

### セキュリティ懸念
1. 具体的な懸念点を明示
2. OWASP Mobile Top 10のどの項目に関連するか特定
3. 現在の実装を共有してレビュー依頼

---

## 🎓 学習リソース

### 必読ドキュメント
- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)
- [BiometricPrompt Guide](https://developer.android.com/training/sign-in/biometric-auth)
- [Android Keystore System](https://developer.android.com/training/articles/keystore)

### 参考実装
- [Google Android Architecture Samples](https://github.com/android/architecture-samples)
- [OWASP MASVS](https://github.com/OWASP/owasp-masvs)

---

## 📝 よくある質問と回答

### Q1: どのAndroidバージョンをターゲットにすべきか？
**A**: Android 15 (API Level 35) 以降。StrongBoxの安定性のため。

### Q2: なぜJavaでKotlinではないのか？
**A**: プロジェクト方針。将来的にKotlinへの移行は検討可能。

### Q3: MVP/MVVM/MVIどれを採用すべきか？
**A**: 初期はシンプルなMVVMパターン。過度な抽象化は避ける。

### Q4: DIライブラリは使うべきか？
**A**: Phase 1では手動DI。複雑化したらHiltを検討。

---

## 🚀 次のマイルストーン

### Phase 1目標（2週間後）
- FR-AUTH-01, 02, 03 完了
- NFR-SEC-01, 02, 03 完了
- ユニットテストカバレッジ 80%
- 実機での動作確認完了

### Phase 2目標（5週間後）
- FR-DATA-01, 02, 04 完了
- FR-SEC-01, 03 完了
- データの暗号化保存完了
- 100件のテストデータで動作確認

---

**最終更新**: 2026年2月1日  
**次回更新予定**: Phase 1完了時（2026年2月15日）

---

> 💡 **Claudeへ**: このファイルの内容を踏まえて、常にプロジェクトのコンテキストを意識した回答をお願いします。特にセキュリティ要件は妥協せず、テスタビリティとドキュメント化を重視してください。
