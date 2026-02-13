# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### 今後の予定
- パスワード生成UIの統合
- 検索機能の強化
- ダークモード対応

---

## [1.0.0] - 2026-02-14

### v1.0.0 - Initial Release

#### Added
- **ProGuardルール**: リリースビルド用の難読化・最適化ルール
  - Room Database保護
  - Biometric認証クラス保護
  - セキュリティ/暗号化クラス保護
  - ViewModel/LiveData保護

#### Changed
- **リリースビルド設定**: minify・shrinkResources有効化を確認

#### Fixed
- なし

---

## [0.6.0] - 2026-02-12〜14

### Phase 6 - UI/UX Improvement

#### Added
- **Material Design 3 カラーパレット** (`colors.xml`)
  - プライマリカラー: Blue (#1976D2)
  - セカンダリカラー: Green (#388E3C)
  - アクセントカラー: Orange (#FF6F00)
  - パスワード強度カラー（5段階）
  - カテゴリカラー（6種類）

- **Material Design 3 テーマ** (`themes.xml`)
  - ライト/ダークテーマ対応
  - コンポーネントスタイル定義
  - Typography設定

- **XMLレイアウト** (4画面)
  - `fragment_password_list.xml`: MaterialToolbar、RecyclerView、Empty State、FAB
  - `fragment_add_password.xml`: TextInputLayout、パスワード表示切替、MaterialButton
  - `fragment_password_detail.xml`: MaterialCardView（セクション分け）、コピーボタン
  - `fragment_edit_password.xml`: TextInputLayout、既存データ表示対応

- **RecyclerViewアイテムレイアウト** (`item_password.xml`)
  - MaterialCardViewによるカード形式
  - カテゴリバッジ表示

- **Drawableリソース**
  - `circle_background.xml`: アイコン背景
  - `category_badge_background.xml`: カテゴリバッジ背景

#### Changed
- **PasswordListFragment**: プログラマティック → XMLレイアウト
- **PasswordListAdapter**: `simple_list_item_2` → `item_password.xml`
- **AddPasswordFragment**: プログラマティック → XMLレイアウト、EditText → TextInputEditText
- **PasswordDetailFragment**: プログラマティック → XMLレイアウト
- **EditPasswordFragment**: プログラマティック → XMLレイアウト

#### Fixed
- **ステータスバー重なり**: 全FragmentにfitsSystemWindows="true"を追加
- **全角スペース混入**: fragment_add_password.xmlの全角スペースを修正

---

## [0.5.0] - 2026-02-07〜08

### Phase 5 - Utilities & Device Testing

#### Added
- **PasswordGenerator**: パスワード生成ユーティリティ
  - 文字種別設定（大文字、小文字、数字、記号）
  - 長さ指定（8〜128文字）
  - セキュアランダム生成

- **PasswordStrengthChecker**: パスワード強度チェッカー
  - 5段階評価（Very Weak〜Very Strong）
  - 長さ、文字種別、パターンの評価

- **Constants**: アプリ定数管理
  - セキュリティ定数
  - UI定数
  - データベース定数

- **ValidationUtils**: バリデーションユーティリティ
  - タイトル、ユーザー名、パスワード、URL、メモの検証

#### Changed
- **CryptoManager**: Android 15対応（IV自動生成）
- **KeyManager**: 認証要件の調整
- **BaseViewModel**: LiveData postValue対応
- **AddPasswordViewModel**: メインスレッド暗号化対応
- **EditPasswordViewModel**: メインスレッド暗号化対応

#### Fixed
- **Android 15 IV生成エラー**: `InvalidAlgorithmParameterException`を修正
- **UserNotAuthenticatedException**: KeyStore認証要件を調整
- **UI配置問題**: 全FragmentのステータスバーPadding修正

---

## [0.4.0] - 2026-02-07

### Phase 4 - UI Extension

#### Added
- **AddPasswordFragment**: パスワード追加画面の実装
- **PasswordDetailFragment**: パスワード詳細画面の実装
- **EditPasswordFragment**: パスワード編集画面の実装
- **FloatingActionButton**: パスワード一覧画面に追加ボタンを配置

#### Changed
- PasswordListFragmentの更新（詳細画面への遷移、FAB追加）

#### Fixed
- なし

---

## [0.3.0] - 2026-02-06

### Phase 3 - UI Layer Foundation

#### Added
- **BaseViewModel**: ViewModel基底クラス
- **BaseFragment**: Fragment基底クラス
- **ViewState**: UI状態管理クラス
- **PasswordListViewModel**: パスワード一覧画面のViewModel
- **PasswordListAdapter**: RecyclerViewアダプター
- **PasswordListFragment**: パスワード一覧画面

#### Changed
- MainActivityの更新（認証成功後にPasswordListFragmentを表示）

#### Fixed
- なし

---

## [0.2.0] - 2026-02-05

### Phase 2 - Domain Layer

#### Added
- **Password**: ドメインモデル（復号済みパスワード）
- **AddPasswordUseCase**: パスワード追加のビジネスロジック
- **UpdatePasswordUseCase**: パスワード更新のビジネスロジック
- **DeletePasswordUseCase**: パスワード削除のビジネスロジック
- **GetPasswordUseCase**: パスワード取得・復号のビジネスロジック
- **GetAllPasswordsUseCase**: パスワード一覧取得のビジネスロジック

#### Changed
- なし

#### Fixed
- なし

---

## [0.1.0] - 2026-02-04

### Phase 1 - Data Layer

#### Added
- **Category**: カテゴリエンティティ
- **PasswordEntry**: パスワードエンティティ（暗号化済み）
- **AppDatabase**: Room データベース
- **PasswordEntryDao**: パスワードエントリDAO
- **CategoryDao**: カテゴリDAO
- **PasswordRepository**: パスワードリポジトリ

#### Changed
- なし

#### Fixed
- なし

---

## [0.0.1] - 2026-02-03

### Phase 0 - Project Foundation

#### Added
- プロジェクトの初期化（Clean Architecture + MVVM）
- **CryptoManager**: AES-256-GCM暗号化
- **KeyManager**: Android KeyStore + StrongBox対応
- **AuthenticationManager**: BiometricPrompt統合
- **MainActivity**: FLAG_SECURE、生体認証、オートロック

#### Changed
- なし

#### Fixed
- なし

---

## 既知の問題

### セキュリティ
- なし

### 機能
- パスワード生成UIは未統合（次バージョンで対応予定）
- 検索機能は基本実装のみ（次バージョンで強化予定）

### UI/UX
- ダークモード対応は次バージョン予定
- アニメーション・トランジションは次バージョン予定

---

[Unreleased]: https://github.com/wafukarubonara-stack/memoripass/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.6.0...v1.0.0
[0.6.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.5.0...v0.6.0
[0.5.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.0.1...v0.1.0
[0.0.1]: https://github.com/wafukarubonara-stack/memoripass/releases/tag/v0.0.1
