# 🤝 貢献ガイドライン

Memoripassへの貢献に興味を持っていただき、ありがとうございます！

このドキュメントは、プロジェクトへの貢献方法を説明します。

---

## 目次

1. [行動規範](#行動規範)
2. [貢献方法](#貢献方法)
3. [開発環境のセットアップ](#開発環境のセットアップ)
4. [Issue の作成](#issueの作成)
5. [Pull Request の作成](#pull-requestの作成)
6. [コーディング規約](#コーディング規約)
7. [コミットメッセージ](#コミットメッセージ)
8. [レビュープロセス](#レビュープロセス)

---

## 行動規範

このプロジェクトは [Code of Conduct](CODE_OF_CONDUCT.md) を採用しています。
参加することで、この規範を遵守することに同意したものとみなされます。

---

## 貢献方法

貢献には以下のような方法があります：

### 🐛 バグ報告
- バグを見つけた場合は [Issue](https://github.com/wafukarubonara-stack/memoripass/issues/new?template=bug_report.md) を作成してください
- 再現手順、期待される動作、実際の動作を明記してください

### 💡 機能提案
- 新機能のアイデアがある場合は [Issue](https://github.com/wafukarubonara-stack/memoripass/issues/new?template=feature_request.md) を作成してください
- 提案の背景と具体的なユースケースを説明してください

### 📝 ドキュメント改善
- タイポ修正、説明の改善、翻訳などのPRを歓迎します
- ドキュメントのみの変更でもPRを作成してください

### 🔧 コード貢献
- バグ修正や新機能の実装
- テストの追加・改善
- パフォーマンス改善

### 🎨 デザイン貢献
- UI/UXの改善提案
- アイコンやグラフィックの作成

---

## 開発環境のセットアップ

詳細は [DEVELOPMENT.md](DEVELOPMENT.md) を参照してください。

### クイックスタート

```bash
# 1. フォーク
# GitHubでリポジトリをフォーク

# 2. クローン
git clone git@github.com:[your-username]/memoripass.git
cd memoripass

# 3. リモートを追加
git remote add upstream git@github.com:wafukarubonara-stack/memoripass.git

# 4. 依存関係をインストール
./gradlew build

# 5. ブランチを作成
git checkout -b feature/my-awesome-feature

# 6. 開発・テスト
# ... コード編集 ...
./gradlew test

# 7. コミット
git add .
git commit -m "feat: Add awesome feature"

# 8. プッシュ
git push origin feature/my-awesome-feature

# 9. Pull Requestを作成
# GitHubでPRを作成
```

---

## Issue の作成

### バグ報告

以下の情報を含めてください：

```markdown
## バグの説明
明確で簡潔なバグの説明

## 再現手順
1. '...'に移動
2. '...'をクリック
3. '...'までスクロール
4. エラーを確認

## 期待される動作
何が起こるべきだったかの説明

## 実際の動作
実際に何が起こったかの説明

## スクリーンショット
可能であればスクリーンショットを添付

## 環境
- デバイス: [例: Google Pixel 9]
- OS: [例: Android 15]
- アプリバージョン: [例: 1.0.0]

## 追加情報
その他の関連情報
```

---

### 機能提案

以下の情報を含めてください：

```markdown
## 機能の説明
提案する機能の明確で簡潔な説明

## 問題・ニーズ
この機能がどのような問題を解決するか

## 提案する解決策
機能の実装方法の説明

## 代替案
検討した他のソリューション

## 追加情報
モックアップ、参考資料など
```

---

## Pull Request の作成

### PR作成前のチェックリスト

- [ ] 最新の `develop` ブランチから作業している
- [ ] コードが[コーディング規約](#コーディング規約)に従っている
- [ ] すべてのテストがパスする (`./gradlew test`)
- [ ] 新しいコードにテストを追加した
- [ ] Lintエラーがない (`./gradlew lint`)
- [ ] ドキュメントを更新した（必要な場合）
- [ ] セキュリティチェックリストを確認した

---

### PRテンプレート

```markdown
## 変更内容
この PRで何を変更したか

## 関連Issue
Closes #123

## 変更の種類
- [ ] バグ修正 (非破壊的変更)
- [ ] 新機能 (非破壊的変更)
- [ ] 破壊的変更 (既存機能に影響)
- [ ] ドキュメント更新

## テスト方法
変更をテストする方法

## スクリーンショット
UI変更がある場合はスクリーンショット

## チェックリスト
- [ ] コードがコーディング規約に従っている
- [ ] セルフレビュー完了
- [ ] コメントを追加（複雑な部分）
- [ ] ドキュメント更新
- [ ] テスト追加
- [ ] すべてのテストがパス
- [ ] Lintエラーなし
```

---

## コーディング規約

### Java スタイルガイド

[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) に従います。

#### 主要なルール

**命名規則**
```java
// クラス: PascalCase
public class AuthenticationManager { }

// メソッド: camelCase
public void authenticateUser() { }

// 定数: UPPER_SNAKE_CASE
private static final int MAX_ATTEMPTS = 3;

// 変数: camelCase
private String userName;
```

**インデント**
- スペース4つ（タブ不可）

**行の長さ**
- 最大100文字

**import文**
- 静的インポートを最初に
- アルファベット順
- ワイルドカード（`*`）は避ける

**Javadoc**
```java
/**
 * パスワードを暗号化します
 * 
 * @param password 暗号化するパスワード
 * @return 暗号化されたバイト配列
 * @throws CryptoException 暗号化に失敗した場合
 */
public byte[] encrypt(String password) throws CryptoException {
    // ...
}
```

---

### セキュリティ規約

#### 必須事項

1. **センシティブデータのログ出力禁止**
```java
// ❌ 悪い例
Log.d(TAG, "Password: " + password);

// ✅ 良い例
Log.d(TAG, "Authentication attempt");
```

2. **パスワードは char[] で管理**
```java
// ❌ 悪い例
String password = "secret";

// ✅ 良い例
char[] password = {'s', 'e', 'c', 'r', 'e', 't'};
try {
    // 使用
} finally {
    Arrays.fill(password, '\0');
}
```

3. **例外メッセージで詳細情報を漏らさない**
```java
// ❌ 悪い例
throw new Exception("Failed to decrypt: key = " + key);

// ✅ 良い例
throw new CryptoException("Decryption failed");
```

4. **ハードコードされた秘密情報禁止**
```java
// ❌ 悪い例
private static final String API_KEY = "sk_live_xxxxx";

// ✅ 良い例
private String getApiKey() {
    return BuildConfig.API_KEY;  // Gradle で注入
}
```

---

### テストの書き方

#### ユニットテストの要件

- すべてのpublic/protectedメソッドをテスト
- エッジケースをカバー
- テストメソッド名は明確に（`test[何を]_[条件]_[期待結果]`）

```java
@Test
public void testEncrypt_validInput_returnsEncryptedData() {
    // Arrange
    String input = "password";
    
    // Act
    byte[] result = cryptoManager.encrypt(input);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.length > 0);
}

@Test(expected = IllegalArgumentException.class)
public void testEncrypt_nullInput_throwsException() {
    cryptoManager.encrypt(null);
}
```

---

## コミットメッセージ

[Conventional Commits](https://www.conventionalcommits.org/) に従います。

### フォーマット

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type

- `feat`: 新機能
- `fix`: バグ修正
- `docs`: ドキュメントのみの変更
- `style`: コードの意味に影響しない変更（空白、フォーマット等）
- `refactor`: バグ修正でも機能追加でもないコード変更
- `perf`: パフォーマンス改善
- `test`: テストの追加・修正
- `chore`: ビルドプロセスやツールの変更

### Scope

- `auth`: 認証関連
- `crypto`: 暗号化関連
- `data`: データ管理
- `ui`: UI関連
- `util`: ユーティリティ

### 例

```
feat(auth): Add biometric authentication support

- Implement BiometricPrompt integration
- Add fallback to device PIN/pattern
- Add authentication state management

Closes #15
```

```
fix(crypto): Fix IV reuse vulnerability

The encryption method was reusing the same IV for multiple
encryptions, which compromises security. This fix generates
a new random IV for each encryption operation.

Fixes #42
```

---

## レビュープロセス

### レビュー観点

#### 機能性
- [ ] 要件を満たしているか
- [ ] エッジケースが考慮されているか
- [ ] エラーハンドリングが適切か

#### セキュリティ
- [ ] OWASP Mobile Top 10 に準拠しているか
- [ ] センシティブデータの扱いが適切か
- [ ] 暗号化が正しく実装されているか

#### コード品質
- [ ] コーディング規約に従っているか
- [ ] 可読性が高いか
- [ ] 適切にコメントされているか

#### テスト
- [ ] テストカバレッジが十分か
- [ ] テストが意味のあるものか
- [ ] すべてのテストがパスするか

#### パフォーマンス
- [ ] 不要な処理がないか
- [ ] メモリリークの可能性がないか
- [ ] 応答時間の要件を満たしているか

---

### レビュー後の対応

1. **フィードバックを受ける**
   - すべてのコメントを確認
   - 質問があれば返信

2. **修正を実施**
   ```bash
   # 同じブランチで修正
   git add .
   git commit -m "fix: Address review comments"
   git push origin feature/my-feature
   ```

3. **再レビュー依頼**
   - 修正完了後、レビュアーに通知

4. **マージ**
   - 承認後、メンテナーがマージ
   - スカッシュマージを使用（コミット履歴を整理）

---

## よくある質問

### Q: どのブランチから作業すべきですか？
**A**: `develop` ブランチから新しいブランチを作成してください。

### Q: 小さな修正でもPRを作成すべきですか？
**A**: はい。タイポ修正でもPRを作成してください。

### Q: テストを書くのが難しいです
**A**: まずはPRを作成してください。レビュアーがテストの書き方を支援します。

### Q: セキュリティ上の問題を見つけました
**A**: 公開Issueではなく、[SECURITY.md](SECURITY.md) に記載の方法で報告してください。

### Q: AIツール（Claude、GitHub Copilotなど）を使ってもいいですか？
**A**: はい。ただし生成されたコードは必ず理解し、レビューしてください。

---

## 謝辞

貢献してくださるすべての方に感謝します！

貢献者一覧は [Contributors](https://github.com/wafukarubonara-stack/memoripass/graphs/contributors) で確認できます。

---

## サポート

質問がある場合:
- [Discussions](https://github.com/wafukarubonara-stack/memoripass/discussions) で質問
- [Discord](#) に参加（将来的に開設予定）

---

**ハッピーコーディング！** 🚀

---

**最終更新**: 2026年2月1日
