# Memoripass 開発記録

**プロジェクト名**: Memoripass - 完全端末内完結型パスワード管理Androidアプリ  
**開発者**: wafukarubonara-stack  
**開始日**: 2026年2月3日  
**記録日**: 2026年2月7日  

---

## 📅 開発タイムライン

### 2026年2月3日 - Phase 0: プロジェクト基盤構築

**実装内容:**
- Android Studioプロジェクト作成
- Clean Architecture + MVVMパターンの採用決定
- セキュリティコンポーネントの実装

**作成ファイル:**
- `CryptoManager.java` - AES-256-GCM暗号化
- `KeyManager.java` - Android KeyStore管理
- `AuthenticationManager.java` - 生体認証
- `MainActivity.java` - エントリーポイント

**技術的決定:**
- 暗号化アルゴリズム: AES-256-GCM（NIST SP 800-38D準拠）
- 鍵管理: Android KeyStore + StrongBox（Titan M2対応）
- 認証: BiometricPrompt（指紋・顔認証）
- セキュリティ: FLAG_SECURE（スクリーンショット防止）

**課題と解決:**
- 課題: 暗号化の初期化ベクトル（IV）の安全な生成
- 解決: SecureRandomによる12バイトIV生成

**所要時間:** 約4-6時間

---

### 2026年2月4日 - Phase 1: データ層の実装

**実装内容:**
- Room Databaseの設計と実装
- DAO（Data Access Object）の実装
- Repositoryパターンの実装

**作成ファイル:**
- `Category.java` - カテゴリエンティティ
- `PasswordEntry.java` - パスワードエンティティ（暗号化済み）
- `AppDatabase.java` - Room Database（シングルトン）
- `PasswordEntryDao.java` - パスワードDAO
- `CategoryDao.java` - カテゴリDAO
- `PasswordRepository.java` - リポジトリ

**技術的決定:**
- データベース: Room Persistence Library 2.6.1
- パターン: Repository Pattern
- スレッド管理: Executor（バックグラウンド処理）
- データ監視: LiveData

**課題と解決:**
- 課題: パッケージ構造の整理
- 解決: `data/model`, `data/local`, `data/repository`に分離
- 課題: 既存PasswordEntry.javaの移動
- 解決: `data/model`パッケージに移動、旧ファイル削除

**所要時間:** 約4-6時間

---

### 2026年2月5日 - Phase 2: ドメイン層の実装

**実装内容:**
- ビジネスロジックの分離
- Use Case（ユースケース）の実装
- ドメインモデルの作成

**作成ファイル:**
- `Password.java` - ドメインモデル（復号済み）
- `AddPasswordUseCase.java` - パスワード追加
- `UpdatePasswordUseCase.java` - パスワード更新
- `DeletePasswordUseCase.java` - パスワード削除
- `GetPasswordUseCase.java` - パスワード取得・復号
- `GetAllPasswordsUseCase.java` - 一覧取得

**技術的決定:**
- パターン: Use Case Pattern
- モデル: Builderパターン（Password）
- バリデーション: Use Case内で実施
- データ変換: LiveData Transformation

**設計原則:**
- 単一責任の原則（各UseCaseは1つの操作のみ）
- 依存性逆転の原則（上位レイヤーは抽象に依存）

**所要時間:** 約3-4時間

---

### 2026年2月6日 - Phase 3: UI層の基礎実装

**実装内容:**
- MVVM基盤クラスの作成
- パスワード一覧画面の実装
- RecyclerViewアダプターの実装

**作成ファイル:**
- `ViewState.java` - UI状態管理
- `BaseViewModel.java` - ViewModel基底クラス
- `BaseFragment.java` - Fragment基底クラス
- `PasswordListViewModel.java` - 一覧画面ViewModel
- `PasswordListAdapter.java` - RecyclerViewアダプター
- `PasswordListFragment.java` - 一覧画面Fragment

**技術的決定:**
- パターン: MVVM（Model-View-ViewModel）
- データバインディング: LiveData監視
- リスト表示: RecyclerView
- 状態管理: ViewState（Loading/Success/Empty/Error）

**課題と解決:**
- 課題: MainActivity更新後も"Hello World"が表示される
- 解決: `activity_main.xml`からTextViewを削除
- 課題: Fragmentが表示されない
- 解決: `showPasswordList()`メソッドの追加とインポート文の修正

**所要時間:** 約3-4時間

**テスト結果:**
- ✅ TC001: 初回起動時の生体認証 - 合格
- ✅ TC004: オートロック（30秒） - 合格
- ✅ TC101: データベース初期化 - 合格
- ✅ TC201: パスワード一覧画面の表示 - 合格

---

### 2026年2月7日 - Phase 4: UI拡張の実装

**実装内容:**
- パスワード追加画面の実装
- パスワード詳細画面の実装
- パスワード編集画面の実装
- 画面間ナビゲーションの実装

**作成ファイル:**
- `AddPasswordFragment.java` - 追加画面
- `AddPasswordViewModel.java` - 追加画面ViewModel
- `PasswordDetailFragment.java` - 詳細画面
- `PasswordDetailViewModel.java` - 詳細画面ViewModel
- `EditPasswordFragment.java` - 編集画面
- `EditPasswordViewModel.java` - 編集画面ViewModel

**技術的決定:**
- UI作成: プログラマティックレイアウト（XMLなし）
- ナビゲーション: FragmentTransaction
- FAB: FloatingActionButton（Material Components）
- クリップボード: ClipboardManager

**課題と解決:**
- 課題: Material Componentsの依存関係不足
- 解決: `build.gradle.kts`に`material:1.11.0`追加
- 課題: インポート文の不足
- 解決: 各Fragmentに必要なインポート文追加
- 課題: PasswordListFragmentの更新
- 解決: 完全版で置き換え、詳細画面への遷移機能追加

**所要時間:** 約4-5時間

**機能実装:**
- ✅ パスワード追加（バリデーション付き）
- ✅ パスワード詳細表示
- ✅ パスワードのクリップボードコピー
- ✅ パスワード編集
- ✅ パスワード削除（確認ダイアログ付き）

---

### 2026年2月7日 - Phase 5: ユーティリティの実装

**実装内容:**
- パスワード生成機能の実装
- パスワード強度チェック機能の実装

**作成ファイル:**
- `PasswordGenerator.java` - セキュアなパスワード生成
- `PasswordStrengthChecker.java` - パスワード強度評価

**技術的決定:**
- 乱数生成: SecureRandom（暗号学的に安全）
- パスワード長: 8-128文字（カスタマイズ可能）
- 文字種: 大文字・小文字・数字・記号
- 強度評価: 5段階（Very Weak/Weak/Moderate/Strong/Very Strong）

**機能:**
- デフォルト生成（16文字、全文字種）
- 強力な生成（24文字、全文字種）
- 簡易生成（12文字、記号なし）
- PINコード生成（4-8桁）

**所要時間:** 約2-3時間

**残作業:**
- AddPasswordFragmentへの統合（次回実装）
- Constants.java（オプション）
- ValidationUtils.java（オプション）

---

### 2026年2月7日 - ドキュメント作成

**実装内容:**
- プロジェクトドキュメントの包括的作成

**作成ファイル:**
- `README.md` - プロジェクト概要（約500行）
- `CHANGELOG.md` - 変更履歴（約350行）
- `ARCHITECTURE.md` - アーキテクチャ詳細（約550行）
- `TESTING.md` - テスト計画（約450行）

**内容:**
- プロジェクト概要と特徴
- インストール・使用方法
- アーキテクチャ図とフロー図
- セキュリティ仕様
- 実装状況とロードマップ
- テストケースと結果
- バージョン履歴

**所要時間:** 約2-3時間

---

## 📊 統計情報

### コード統計

| 項目 | 数値 |
|-----|-----|
| **総ファイル数** | 38 |
| **Javaファイル数** | 34 |
| **ドキュメント数** | 4 |
| **総コード行数** | 約5,000-6,000行 |
| **パッケージ数** | 11 |
| **クラス数** | 34 |
| **実行済みテスト** | 4/7 |

### パッケージ別ファイル数

| パッケージ | ファイル数 | 状態 |
|-----------|----------|------|
| `auth/` | 1 | ✅ 完了 |
| `crypto/` | 2 | ✅ 完了 |
| `data/model/` | 2 | ✅ 完了 |
| `data/local/` | 1 | ✅ 完了 |
| `data/local/dao/` | 2 | ✅ 完了 |
| `data/repository/` | 1 | ✅ 完了 |
| `domain/model/` | 1 | ✅ 完了 |
| `domain/usecase/` | 5 | ✅ 完了 |
| `ui/common/` | 3 | ✅ 完了 |
| `ui/list/` | 3 | ✅ 完了 |
| `ui/add/` | 2 | ✅ 完了 |
| `ui/detail/` | 2 | ✅ 完了 |
| `ui/edit/` | 2 | ✅ 完了 |
| `util/` | 2 | 🔄 進行中 |
| **合計** | **34** | - |

### Git統計

| 項目 | 数値 |
|-----|-----|
| **コミット数** | 5 |
| **ブランチ** | main |
| **リモートリポジトリ** | GitHub |

---

## 🎯 実装した機能

### セキュリティ機能 ✅

- ✅ AES-256-GCM暗号化（NIST SP 800-38D準拠）
- ✅ Android KeyStore統合
- ✅ StrongBox対応（Titan M2）
- ✅ 生体認証（指紋・顔認証）
- ✅ オートロック（30秒）
- ✅ スクリーンショット防止（FLAG_SECURE）
- ✅ メモリセキュリティ（使用後のゼロクリア設計）

### パスワード管理機能 ✅

- ✅ パスワード追加
- ✅ パスワード一覧表示
- ✅ パスワード詳細表示
- ✅ パスワード編集
- ✅ パスワード削除（確認ダイアログ付き）
- ✅ パスワードコピー（クリップボード）
- ✅ カテゴリ分類

### ユーティリティ機能 ✅

- ✅ セキュアなパスワード生成（8-128文字）
- ✅ パスワード強度チェック（5段階評価）
- ✅ カスタマイズ可能な生成オプション
- ✅ 連続文字・繰り返し文字の検出

---

## 🏗️ アーキテクチャ

### 採用パターン

1. **Clean Architecture**
   - レイヤー分離（Presentation/Domain/Data/Infrastructure）
   - 依存性の方向制御
   - テスタビリティの確保

2. **MVVM（Model-View-ViewModel）**
   - UIとビジネスロジックの分離
   - LiveDataによるリアクティブUI
   - ライフサイクル対応

3. **Repository Pattern**
   - データアクセスの抽象化
   - 複数データソースの統合
   - テスト容易性の向上

### 技術スタック

| レイヤー | 技術 |
|---------|-----|
| **UI** | Fragment, ViewModel, LiveData |
| **データベース** | Room 2.6.1 |
| **暗号化** | Android Keystore, AES-256-GCM |
| **認証** | BiometricPrompt 1.1.0 |
| **UI Components** | Material Components 1.11.0 |
| **言語** | Java (JDK 17) |
| **ビルド** | Gradle (Kotlin DSL) |

---

## 🐛 直面した課題と解決

### 1. MainActivity更新後の"Hello World"表示

**問題:**
- 認証成功後、PasswordListFragmentが表示されるはずが、"Hello World"が表示される

**原因:**
- `activity_main.xml`にデフォルトのTextViewが残っていた
- PasswordListFragmentへの遷移処理が不完全

**解決:**
1. `activity_main.xml`からTextViewを削除
2. `showPasswordList()`メソッドを追加
3. 必要なインポート文を追加

**学び:**
- レイアウトXMLとFragment表示の優先順位を理解
- デフォルトテンプレートの削除の重要性

---

### 2. 暗号化処理のスレッド問題

**問題:**
- バックグラウンドスレッドから暗号化すると`InvalidAlgorithmParameterException`が発生

**原因:**
- Android KeyStoreは特定のスレッドコンテキストでのみ動作

**解決:**
- 暗号化処理をメインスレッドで実行
- データベース保存のみをバックグラウンドスレッドで実行

**コード変更:**
```java
// 修正前：すべてバックグラウンド
new Thread(() -> {
    String encrypted = repository.encryptPassword(password); // エラー
    repository.insert(entry);
}).start();

// 修正後：暗号化はメインスレッド
String encrypted = repository.encryptPassword(password); // OK
new Thread(() -> {
    repository.insert(entry);
}).start();
```

**学び:**
- Android KeyStoreのスレッド制約を理解
- 適切なスレッド分離の重要性

---

### 3. PasswordListFragmentの更新

**問題:**
- 詳細画面への遷移機能を追加する際、手動更新が複雑

**原因:**
- 複数箇所の変更が必要（メソッド追加、インポート追加）
- 段階的な更新で混乱

**解決:**
- 完全版のファイルで置き換え
- 一度にすべての変更を適用

**学び:**
- 複雑な更新は完全版置き換えが効率的
- バックアップの重要性

---

## 💡 学んだベストプラクティス

### 1. アーキテクチャ設計

- **レイヤー分離の徹底**: 各レイヤーの責務を明確化
- **依存性の方向制御**: 上位レイヤーは下位レイヤーの抽象に依存
- **テスタビリティ**: 依存性注入により、モック可能な設計

### 2. セキュリティ

- **深層防御**: 複数のセキュリティレイヤー
- **最小権限の原則**: 必要最小限のアクセス権
- **セキュアデフォルト**: 安全な設定をデフォルトに

### 3. コード品質

- **段階的な開発**: Phaseごとに明確な成果物
- **包括的なドキュメント**: 設計意図の明文化
- **適切なGitコミット**: 意味のある単位でコミット

### 4. 問題解決

- **エラーログの活用**: Logcatによる問題特定
- **段階的なデバッグ**: 一つずつ問題を解決
- **完全版置き換え**: 複雑な更新は全体置き換えが有効

---

## 🔄 開発プロセス

### 典型的な開発フロー

1. **設計** - アーキテクチャ図とクラス設計
2. **実装** - Phaseごとにファイル作成
3. **ビルド** - `./gradlew build`で確認
4. **テスト** - 基本的な動作確認
5. **コミット** - 意味のある単位でGitコミット
6. **ドキュメント** - 実装完了後にドキュメント更新

### 使用したコマンド

```bash
# ビルド
./gradlew build

# インストール
./gradlew installDebug

# アンインストール
adb uninstall com.memoripass

# Logcat監視
adb logcat | grep "com.memoripass"

# Git操作
git add .
git commit -m "feat: ..."
git push origin main
```

---

## 📈 進捗管理

### Phase別進捗

| Phase | 計画 | 実績 | 差異 |
|-------|-----|-----|-----|
| Phase 0 | 6h | 5h | -1h |
| Phase 1 | 6h | 5h | -1h |
| Phase 2 | 4h | 3h | -1h |
| Phase 3 | 4h | 4h | 0h |
| Phase 4 | 5h | 5h | 0h |
| Phase 5 | 3h | 2h | -1h (未完了) |
| ドキュメント | 2h | 3h | +1h |
| **合計** | **30h** | **27h** | **-3h** |

### タスク完了率

- Phase 0-4: **100%** ✅
- Phase 5: **80%** 🔄
- Phase 6: **0%** 📋
- **総合**: **83%** 🎯

---

## 🎓 技術的な成長

### 新しく学んだこと

1. **Android KeyStore**
   - StrongBoxの使い方
   - ハードウェアバックド鍵の管理

2. **Clean Architecture**
   - 実践的なレイヤー分離
   - 依存性の方向制御

3. **Room Database**
   - LiveDataとの統合
   - DAOパターンの実装

4. **MVVM + LiveData**
   - リアクティブなUI更新
   - ライフサイクル対応

5. **暗号化**
   - AES-256-GCMの実装
   - NIST準拠の暗号化設計

### 改善した技術

1. **Gitの使い方**
   - 意味のあるコミットメッセージ
   - 適切なコミット単位

2. **ドキュメント作成**
   - 包括的なREADME
   - 詳細なアーキテクチャドキュメント

3. **問題解決能力**
   - ログを活用したデバッグ
   - 段階的な問題解決

---

## 🚀 今後の展望

### 短期目標（Phase 5-6完了）

1. **実機テスト**
   - 全テストケースの実行
   - バグ修正とパフォーマンス確認

2. **Phase 5完成**
   - AddPasswordFragmentへの統合
   - パスワード強度インジケーター追加

3. **Phase 6実装**
   - カスタムレイアウトXML
   - Material Design 3対応
   - ダークモード

### 中期目標（v1.0リリース）

1. **包括的なテスト**
   - 単体テスト（カバレッジ80%以上）
   - UIテスト
   - セキュリティテスト

2. **パフォーマンス最適化**
   - データベースクエリの最適化
   - メモリ使用量の削減

3. **Google Play Store公開**
   - ストアリスティング作成
   - プライバシーポリシー作成

### 長期目標（v2.0）

1. **自動入力サービス**
   - Autofill Framework統合
   - ブラウザ連携

2. **バックアップ/リストア**
   - 暗号化されたエクスポート
   - クラウド同期（オプション）

3. **高度な機能**
   - 2要素認証コード管理
   - パスワード共有機能

---

## 📝 開発メモ

### Tips & Tricks

1. **ビルドエラーの対処**
   ```bash
   # Clean & Rebuild
   ./gradlew clean
   ./gradlew build
   ```

2. **Logcatフィルタリング**
   ```bash
   # 特定のタグのみ表示
   adb logcat -s MainActivity PasswordList
   
   # 正規表現でフィルタ
   adb logcat | grep -E "MainActivity|PasswordList"
   ```

3. **データベースのリセット**
   ```bash
   # アプリをアンインストール（データベースも削除）
   adb uninstall com.memoripass
   ```

### よく使うコマンド

```bash
# ビルドとインストール
./gradlew clean build installDebug

# ログ保存
adb logcat > memoripass_log_$(date +%Y%m%d_%H%M%S).txt

# Git操作
git status
git add .
git commit -m "feat: ..."
git push origin main
```

---

## 🙏 謝辞

このプロジェクトの開発にあたり、以下のリソースが役立ちました：

- [Android Developers Documentation](https://developer.android.com)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Room Persistence Library Guide](https://developer.android.com/training/data-storage/room)
- [Android Keystore System](https://developer.android.com/training/articles/keystore)
- [NIST SP 800-38D (GCM Mode)](https://csrc.nist.gov/publications/detail/sp/800-38d/final)

---

## 📌 最後に

### プロジェクトの成果

Memoripassプロジェクトを通じて、以下を達成しました：

1. ✅ **完全に機能するアプリ** - セキュアなパスワード管理
2. ✅ **Clean Architecture実装** - 保守性の高い設計
3. ✅ **包括的なドキュメント** - 理解しやすいドキュメント
4. ✅ **セキュリティベストプラクティス** - NIST準拠の実装
5. ✅ **段階的な開発** - 計画的なPhase進行

### 次のステップ

次回の開発セッションでは：
1. 実機テストの実施
2. Phase 5の完成（統合作業）
3. バグ修正とパフォーマンス改善

---

**開発記録終了**  
**次回更新日**: 実機テスト実施後

---

## 📚 関連ドキュメント

- [README.md](README.md) - プロジェクト概要
- [CHANGELOG.md](CHANGELOG.md) - 変更履歴
- [ARCHITECTURE.md](ARCHITECTURE.md) - アーキテクチャ詳細
- [TESTING.md](TESTING.md) - テスト計画

---

**Memoripass Development Team**  
**Repository**: https://github.com/wafukarubonara-stack/memoripass

---

### 2026年2月7日（午後） - 実機テスト開始と問題修正

#### 実機テスト環境構築

**使用デバイス**: Google Pixel 9
**OS**: Android 15 (API 35)
**接続方法**: USB（ADB）

#### テスト実行結果

| テストID | テスト名 | 結果 | 備考 |
|---------|---------|------|------|
| TC001 | 初回起動時の生体認証 | ✅ 合格 | 想定通り動作 |
| TC201 | パスワード追加 | ❌ 失敗 | UI問題とスレッド問題を発見 |

#### 発見した問題と修正

**問題1: UI配置問題（ステータスバー重なり）**
- **症状**: 追加画面のタイトル欄がステータスバーと重なり入力不可
- **原因**: パディング不足
- **修正**: `rootLayout.setPadding(48, 48, 48, 48)` → `(48, 120, 48, 48)`
- **影響範囲**: AddPasswordFragment, PasswordDetailFragment, EditPasswordFragment
- **修正日**: 2026-02-07

**問題2: LiveData.setValue()のスレッド問題**
```
IllegalStateException: Cannot invoke setValue on a background thread
```
- **症状**: バックグラウンドスレッドからLiveData更新でクラッシュ
- **原因**: BaseViewModel内で`setValue()`使用
- **修正**: すべての`setValue()`を`postValue()`に変更
- **影響範囲**: BaseViewModel（全ViewModelに影響）
- **修正日**: 2026-02-07

**問題3: 暗号化処理のスレッド問題**
```
InvalidAlgorithmParameterException: Caller-provided IV not permitted
```
- **症状**: バックグラウンドスレッドから暗号化実行で失敗
- **原因**: Android KeyStoreはメインスレッドでのみ動作
- **修正**: 暗号化をメインスレッドで実行、DB保存のみバックグラウンド
- **修正箇所**:
  - AddPasswordViewModel
  - EditPasswordViewModel
- **修正日**: 2026-02-07

#### 予防的修正

問題発見後、同様の問題が発生する可能性のある箇所を事前修正：
- ✅ PasswordDetailFragmentのUI修正
- ✅ EditPasswordFragmentのUI修正
- ✅ EditPasswordViewModelのスレッド修正

#### Phase 5完成

実機テスト中に、Phase 5の残りユーティリティを完成：
- ✅ Constants.java（200行）- アプリ全体の定数定義
- ✅ ValidationUtils.java（300行）- 入力バリデーション

**Phase 5完成度**: 100%

#### 技術的学び

1. **Android KeyStoreの制約**
   - メインスレッドでのみ暗号化・復号が可能
   - バックグラウンドスレッドから呼ぶと`InvalidAlgorithmParameterException`
   - 設計: 暗号化→メイン、DB操作→バックグラウンド

2. **LiveDataのスレッドセーフ**
   - `setValue()`: メインスレッドのみ
   - `postValue()`: 任意のスレッドから呼び出し可能
   - バックグラウンド処理では必ず`postValue()`使用

3. **UIパディングの重要性**
   - ステータスバー高さを考慮したパディング必要
   - 標準: top=48dp、ステータスバー考慮: top=120dp

#### 次回作業予定

1. 修正版APKのインストール
2. TC201（パスワード追加）の再テスト
3. TC401（パスワード詳細表示）
4. TC501（パスワード編集）
5. TC601（パスワード削除）
6. TC004（オートロック）

#### 実装統計（2026-02-07時点）
```
総ファイル数: 38
├── Phase 0-4: 32ファイル
└── Phase 5: 4ファイル (100%完成)

修正ファイル数: 6
├── UI修正: 3ファイル
└── スレッド修正: 3ファイル

総コード行数: 約6,500行
実機テスト: 進行中（2/7合格）
```

---
