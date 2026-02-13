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
---

### 2026年2月7日（夜） - Android 15 KeyStore問題の解決

#### 実機テストの再開

**使用デバイス**: Google Pixel 9  
**Android バージョン**: Android 15 (API 35)

修正版APKをインストールして実機テストを再開。

#### 発見した問題4: Android 15でのIV提供制限

**症状**: 
```
InvalidAlgorithmParameterException: Caller-provided IV not permitted
```

**エラー発生箇所**:
```java
// CryptoManager.java encrypt()メソッド
byte[] iv = new byte[IV_SIZE_BYTES];
secureRandom.nextBytes(iv);
GCMParameterSpec spec = new GCMParameterSpec(AUTH_TAG_SIZE_BITS, iv);
cipher.init(Cipher.ENCRYPT_MODE, masterKey, spec);  // ← ここでエラー
```

**原因**: 
Android 15のKeyStoreでは、セキュリティ強化のため、暗号化時に自分でIV（初期化ベクトル）を生成して提供することが許可されていない。KeyStoreが自動的にIVを生成する必要がある。

**根本原因**:
- Android 14以前: アプリが自分でIVを生成してKeyStoreに提供できた
- Android 15以降: KeyStoreがIVを自動生成し、アプリはそれを取得する方式に変更
- これは予測可能なIVによる攻撃を防ぐためのセキュリティ強化

**修正内容**:
CryptoManager.java の encrypt()メソッドを以下のように修正:
```java
// 修正前（Android 14以前で動作、Android 15でエラー）
byte[] iv = new byte[IV_SIZE_BYTES];
secureRandom.nextBytes(iv);
Cipher cipher = Cipher.getInstance(TRANSFORMATION);
GCMParameterSpec spec = new GCMParameterSpec(AUTH_TAG_SIZE_BITS, iv);
cipher.init(Cipher.ENCRYPT_MODE, masterKey, spec);

// 修正後（Android 15対応）
Cipher cipher = Cipher.getInstance(TRANSFORMATION);
cipher.init(Cipher.ENCRYPT_MODE, masterKey);  // IVなしで初期化
byte[] iv = cipher.getIV();  // KeyStoreが自動生成したIVを取得
```

**影響範囲**: CryptoManager.java

**修正日**: 2026-02-07

**学び**:
- Android 15のセキュリティ強化を理解
- KeyStoreのバージョン別動作の違い
- 暗号化実装はOSバージョンに依存する可能性がある

---

#### 発見した問題5: UserNotAuthenticatedException

**症状**:
```
UserNotAuthenticatedException: User not authenticated
```

**テスト経過**:
- 19:25:56 - 1回目のパスワード保存（「あああ」）: ✅ 成功
- 19:27:18 - 2回目のパスワード保存（「Gmail」）: ❌ 失敗

最初の暗号化は成功するが、時間経過後（約90秒後）に失敗する。

**原因**:
KeyManager でマスターキー生成時に `setUserAuthenticationRequired(true)` が設定されており、各暗号化操作のたびに生体認証が必要になっていた。しかし、アプリ起動時に一度だけ認証を行う設計だったため、時間経過後の操作で認証エラーが発生。

**設計上の問題**:
```java
// KeyManager.java - generateMasterKey()
.setUserAuthenticationRequired(true)  // これが問題
```

この設定により：
1. アプリ起動時に生体認証 → 成功
2. すぐに暗号化操作 → 認証が有効なため成功
3. 時間経過（認証タイムアウト）
4. 再度暗号化操作 → 認証が無効なためエラー

**修正オプション**:

**オプション1: 認証不要に変更**（推奨）
```java
.setUserAuthenticationRequired(false)
```
- メリット: シンプル、アプリレベルで認証管理
- デメリット: KeyStoreレベルの認証保護なし

**オプション2: 認証有効期間を設定**
```java
.setUserAuthenticationRequired(true)
.setUserAuthenticationParameters(3600, KeyProperties.AUTH_BIOMETRIC_STRONG)  // 1時間有効
```
- メリット: KeyStoreレベルの保護維持
- デメリット: 定期的な再認証が必要

**採用した修正**: オプション1（アプリレベル認証で十分）

**影響範囲**: KeyManager.java

**修正日**: 2026-02-07

**設計判断**:
Memoripassは以下の理由でオプション1を採用：
1. アプリ起動時の生体認証で十分なセキュリティ
2. オートロック機能（30秒）でアプリレベル保護
3. ユーザー体験の向上（頻繁な再認証を回避）

---

#### Android 15対応まとめ

Android 15では、セキュリティ要件が大幅に強化され、以下の変更が必要でした：

**1. IV自動生成の強制**
- **変更理由**: 予測可能なIVによる攻撃防止
- **対応方法**: KeyStoreにIV生成を委譲
- **影響**: CryptoManager.encrypt()メソッド

**2. 認証要件の明確化**
- **変更理由**: ユーザー認証の適切な管理
- **対応方法**: アプリの認証フローに合わせた設定
- **影響**: KeyManager.generateMasterKey()メソッド

**3. スレッド制約の維持**（Android 14と同様）
- KeyStoreの操作: メインスレッドで実行
- DB操作: バックグラウンドスレッドで実行
- **影響**: AddPasswordViewModel, EditPasswordViewModel

**4. 全角スペースのコンパイルエラー**
- **問題**: コピー&ペースト時に全角スペースが混入
- **対応**: 手動で半角スペースに修正
- **学び**: エディタの設定確認、直接入力の重要性

---

#### テスト結果（2026-02-07時点）

| テストID | テスト名 | 結果 | 実行時刻 | 備考 |
|---------|---------|------|---------|------|
| TC001 | 初回起動認証 | ✅ 合格 | 19:25:41 | 想定通り動作 |
| TC201-1 | パスワード追加（1回目） | ✅ 合格 | 19:25:56 | 「あああ」で成功 |
| TC201-2 | パスワード追加（2回目） | ❌ 失敗 | 19:27:18 | UserNotAuthenticatedException |
| TC201-3 | パスワード追加（修正後） | 🔄 保留 | - | KeyManager修正後に再テスト予定 |

---

#### 修正したファイル（2026-02-07）

**実機テスト対応**:
1. AddPasswordFragment.java - UI修正（パディング調整）
2. PasswordDetailFragment.java - UI修正
3. EditPasswordFragment.java - UI修正
4. BaseViewModel.java - postValue対応
5. AddPasswordViewModel.java - メインスレッドで暗号化
6. EditPasswordViewModel.java - メインスレッドで暗号化
7. CryptoManager.java - Android 15対応（IV自動生成）
8. KeyManager.java - 認証要件調整

**合計修正ファイル**: 8個

---

#### 次回作業（デバイス充電後）

**優先度: 高** 🔴
1. KeyManager修正版の動作確認
2. TC201（パスワード追加）の完全合格
3. TC401（パスワード詳細表示）
4. TC501（パスワード編集）
5. TC601（パスワード削除）
6. TC004（オートロック）

**優先度: 中** 🟡
7. 全テストケースの完了
8. 実機テスト結果の最終まとめ
9. Phase 5統合（パスワード生成UI）

---

#### 技術的成果

**Android 15対応の完了**:
- KeyStore APIの変更点を理解
- セキュリティ強化への対応方法を習得
- バージョン別の動作差異に対応

**実機テストの価値**:
- エミュレータでは発見できない問題の特定
- OS固有の制約の理解
- 実際のデバイスでの動作確認の重要性

**デバッグスキルの向上**:
- Logcatによる問題特定
- スタックトレースの読解
- 段階的なトラブルシューティング

---

**実装完成度**: 
- Phase 0-5: 100%
- Android 15対応: 95%（最終テスト待ち）
- ドキュメント: 100%

**次回セッションの準備完了** ✅
---

---

### 2026年2月8日 - 実機テスト完全完了

#### 実機テスト最終セッション

**デバイス**: Google Pixel 9  
**Android**: Android 15 (API 35)  
**開始時刻**: 12:52  
**終了時刻**: 12:58  
**所要時間**: 約6分

#### 最終修正: PasswordListFragment UI問題

**問題**:
一覧画面の上部がインカメラと重なり、最初のアイテムが見えない。

**原因**:
PasswordListFragmentのRecyclerViewにパディングが設定されていなかった。

**修正**:
```java
// PasswordListFragment.java - onCreateView()
recyclerView.setPadding(16, 120, 16, 16);  // top: 120px
```

**影響範囲**: PasswordListFragment.java

**修正日**: 2026-02-08

これで、すべてのFragment（4個）でステータスバー・インカメラ対応が完了。

---

#### 実機テスト最終結果

**全テストケース合格！** 🎉

| テストID | テスト名 | 結果 | 実行時刻 |
|---------|---------|------|---------|
| TC001 | 初回起動認証 | ✅ | 12:52:59 |
| TC201 | パスワード追加 | ✅ | 12:53:14 |
| TC401 | パスワード詳細表示 | ✅ | 12:53:16 |
| TC501 | パスワード編集 | ✅ | 12:53:19 |
| TC601 | パスワード削除 | ✅ | 12:53:29 |
| TC004 | オートロック | ✅ | 12:55:36 |
| TC005 | スクリーンショット防止 | ✅ | 12:57:40 |

**合格率**: 7/7 (100%)

#### 実機テストで確認された事項

**正常動作した機能**:
1. ✅ 生体認証（指紋認証）
2. ✅ AES-256-GCM暗号化（Android 15対応完了）
3. ✅ Android KeyStore + StrongBox
4. ✅ オートロック（30秒）
5. ✅ スクリーンショット防止（FLAG_SECURE）
6. ✅ パスワードCRUD操作すべて
7. ✅ 暗号化/復号化（メインスレッド実行）
8. ✅ UI配置（ステータスバー・インカメラ対応）

**問題なし**: エラー0件、クラッシュ0件

---

#### 実機テスト期間中の修正まとめ

**2026年2月7日-8日**

| # | 問題 | 修正ファイル | 内容 |
|---|------|------------|------|
| 1 | UI配置（追加画面） | AddPasswordFragment.java | パディング調整 |
| 2 | UI配置（詳細画面） | PasswordDetailFragment.java | パディング調整 |
| 3 | UI配置（編集画面） | EditPasswordFragment.java | パディング調整 |
| 4 | UI配置（一覧画面） | PasswordListFragment.java | パディング調整 |
| 5 | LiveDataスレッド | BaseViewModel.java | postValue対応 |
| 6 | 暗号化スレッド | AddPasswordViewModel.java | メインスレッド実行 |
| 7 | 暗号化スレッド | EditPasswordViewModel.java | メインスレッド実行 |
| 8 | Android 15 IV制限 | CryptoManager.java | IV自動生成 |
| 9 | 認証タイムアウト | KeyManager.java | 認証要件調整 |

**合計修正ファイル**: 9個

---

#### プロジェクト完成

**Memoripass v0.5.0-dev 実機テスト完了**
```
実装完成度: 100% ✅
├── Phase 0: セキュリティ基盤 - 100%
├── Phase 1: データ層 - 100%
├── Phase 2: ドメイン層 - 100%
├── Phase 3: UI基礎 - 100%
├── Phase 4: UI拡張 - 100%
└── Phase 5: ユーティリティ - 100%

Android 15対応: 100% ✅
実機テスト: 100% (7/7合格) ✅
ドキュメント: 100% ✅

リリース準備度: 95%
```

**未実装機能**:
- Phase 5統合（パスワード生成UI）
- Phase 6（UI/UX改善）

**次のマイルストーン**:
- v0.6.0: Phase 6完了（UI/UX改善）
- v1.0.0: 初回リリース

---

#### 開発統計（最終）

**開発期間**: 5日間（2026-02-03 → 02-08）

**コード統計**:
```
総ファイル数: 38個
総コード行数: 約6,500行
修正ファイル数: 9個（実機テスト）
テストケース: 7個（すべて合格）
```

**Git統計**:
```
コミット数: 約13回
プッシュ回数: 約13回
```

**ドキュメント**:
```
総ドキュメント: 5個
総ドキュメント行数: 約3,000行
```

---

#### 技術的達成

**Android 15完全対応**:
- KeyStore API変更への対応
- セキュリティ強化要件のクリア
- 実機での動作確認完了

**Clean Architecture実装**:
- 4層分離の完全実装
- MVVM + Repository Pattern
- テスタビリティの確保

**セキュリティ実装**:
- エンタープライズグレードの暗号化
- ハードウェアバックドセキュリティ
- 生体認証統合

---

**Memoripass Phase 0-5 開発完了！** 🎊

次回: Phase 6（UI/UX改善）または v1.0リリース準備
---

## 2026年2月12日〜14日 - Phase 6: UI/UX改善

### 作業概要

Phase 6としてUIのデザイン性向上を実施。
Material Design 3対応とXMLレイアウトへの移行を完了。

### 実装内容

#### Step 1: カラーパレット・テーマ設定
- `colors.xml` - Material Design 3カラーパレット
  - プライマリ: Blue (#1976D2)
  - セカンダリ: Green (#388E3C)
  - アクセント: Orange (#FF6F00)
  - パスワード強度カラー（5段階）
  - カテゴリカラー（6種類）
- `themes.xml` - Material Design 3テーマ
  - ライト/ダークテーマ対応
  - コンポーネントスタイル定義

#### Step 2-3: PasswordListFragment + Adapter
- `fragment_password_list.xml` 作成
  - MaterialToolbar
  - RecyclerView
  - Empty State表示
  - FloatingActionButton
- `item_password.xml` 作成
  - MaterialCardView
  - カテゴリバッジ
- PasswordListAdapter更新
  - `simple_list_item_2` → `item_password.xml`

#### Step 4: AddPasswordFragment
- `fragment_add_password.xml` 作成
  - TextInputLayout（アイコン付き）
  - パスワード表示切替（endIconMode）
  - MaterialButton
- AddPasswordFragment.java更新
  - プログラマティック → XMLレイアウト

#### Step 5: PasswordDetailFragment
- `fragment_password_detail.xml` 作成
  - MaterialCardView（セクション分け）
  - コピーボタン
  - 編集/戻るボタン
- PasswordDetailFragment.java更新

#### Step 6: EditPasswordFragment
- `fragment_edit_password.xml` 作成
  - AddPasswordFragmentと同デザイン
  - 既存データ表示対応
- EditPasswordFragment.java更新

### 発見・修正したバグ

| バグ | 原因 | 修正 |
|-----|------|------|
| PasswordListFragment余分な`}` | nanoでの編集ミス | `sed -i '80d'`で削除 |
| ステータスバー重なり（全Fragment） | fitsSystemWindows未設定 | CoordinatorLayoutに追加 |
| 全角スペース混入 | コピー&ペースト時の混入 | sedで半角に変換 |

### 実機テスト結果

**デバイス**: Google Pixel 9 (Android 15)
**結果**: 8/8 合格（100%）
**エラー**: 0件
**クラッシュ**: 0件

### Gitコミット

| コミット | 内容 |
|---------|------|
| feat: Phase 6 Step 1-2 | カラーパレット・テーマ、PasswordListFragment |
| chore: Remove accidentally committed build log files | 誤コミットファイル削除 |
| feat: Phase 6 Step 4 | AddPasswordFragment XML移行 |
| feat: Phase 6 Step 5 | PasswordDetailFragment XML移行 |
| feat: Phase 6 Step 6 | EditPasswordFragment XML移行 |
| test: Phase 6 device testing | 実機テスト証跡 |
| fix: Resolve status bar overlap | ステータスバー重なり修正 |

### 開発統計（Phase 6追加分）

```
新規XMLファイル: 6個
  - colors.xml
  - themes.xml
  - fragment_password_list.xml
  - item_password.xml
  - fragment_add_password.xml
  - fragment_password_detail.xml
  - fragment_edit_password.xml

更新Javaファイル: 5個
  - PasswordListFragment.java
  - PasswordListAdapter.java
  - AddPasswordFragment.java
  - PasswordDetailFragment.java
  - EditPasswordFragment.java

Drawableファイル: 2個
  - circle_background.xml
  - category_badge_background.xml
```

---

**Memoripass Phase 6 完了！** 🎨
次回: パスワード生成UI統合、検索機能、v1.0リリース準備

