# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Phase 5 - In Progress
- パスワード生成機能の統合
- パスワード強度表示の追加
- 検索機能の強化

---

## [0.4.0] - 2026-02-07

### Phase 4 - UI Extension

#### Added
- **AddPasswordFragment**: パスワード追加画面の実装
  - タイトル、ユーザー名、パスワード、URL、カテゴリ、メモの入力
  - バリデーション機能
  - 暗号化処理の統合
  
- **PasswordDetailFragment**: パスワード詳細画面の実装
  - パスワード情報の表示
  - クリップボードへのコピー機能
  - 編集画面への遷移
  
- **EditPasswordFragment**: パスワード編集画面の実装
  - 既存パスワードの編集
  - 更新日時の自動設定
  
- **FloatingActionButton**: パスワード一覧画面に追加ボタンを配置

#### Changed
- PasswordListFragmentの更新
  - 詳細画面への遷移機能を追加
  - FABによる追加画面への遷移

#### Fixed
- なし

---

## [0.3.0] - 2026-02-06

### Phase 3 - UI Layer Foundation

#### Added
- **BaseViewModel**: ViewModel基底クラス
  - ViewState管理
  - エラーハンドリング
  
- **BaseFragment**: Fragment基底クラス
  - 共通UI処理
  - ナビゲーション機能
  
- **ViewState**: UI状態管理クラス
  - Loading, Success, Empty, Error状態
  
- **PasswordListViewModel**: パスワード一覧画面のViewModel
  - LiveDataによるリアクティブなデータ監視
  - 検索・フィルタ機能
  - 削除機能
  
- **PasswordListAdapter**: RecyclerViewアダプター
  - パスワードエントリの一覧表示
  - クリック・長押しリスナー
  
- **PasswordListFragment**: パスワード一覧画面
  - RecyclerViewによる一覧表示
  - 削除確認ダイアログ

#### Changed
- MainActivityの更新
  - 認証成功後にPasswordListFragmentを表示
  - activity_main.xmlからHello World TextViewを削除

#### Fixed
- なし

---

## [0.2.0] - 2026-02-05

### Phase 2 - Domain Layer

#### Added
- **Password**: ドメインモデル（復号済みパスワード）
  - Builderパターンによる構築
  - バリデーション機能
  
- **AddPasswordUseCase**: パスワード追加のビジネスロジック
  - 入力バリデーション
  - 暗号化処理
  
- **UpdatePasswordUseCase**: パスワード更新のビジネスロジック
  - 更新日時の自動設定
  
- **DeletePasswordUseCase**: パスワード削除のビジネスロジック
  
- **GetPasswordUseCase**: パスワード取得・復号のビジネスロジック
  - LiveData変換
  - 自動復号
  
- **GetAllPasswordsUseCase**: パスワード一覧取得のビジネスロジック
  - カテゴリフィルタ
  - 検索機能

#### Changed
- なし

#### Fixed
- なし

---

## [0.1.0] - 2026-02-04

### Phase 1 - Data Layer

#### Added
- **Category**: カテゴリエンティティ
  - ID、名前、色コード、アイコン、作成日時
  
- **PasswordEntry**: パスワードエンティティ（暗号化済み）
  - ID、タイトル、ユーザー名、暗号化パスワード、URL、メモ、カテゴリ、作成日時、更新日時
  
- **AppDatabase**: Room データベース
  - シングルトンパターン
  - password_entriesテーブル
  - categoriesテーブル
  
- **PasswordEntryDao**: パスワードエントリDAO
  - CRUD操作
  - 検索・フィルタクエリ
  - LiveDataサポート
  
- **CategoryDao**: カテゴリDAO
  - CRUD操作
  
- **PasswordRepository**: パスワードリポジトリ
  - データアクセスの抽象化
  - 暗号化・復号処理の統合
  - バックグラウンドスレッドでの実行

#### Changed
- なし

#### Fixed
- なし

---

## [0.0.1] - 2026-02-03

### Phase 0 - Project Foundation

#### Added
- プロジェクトの初期化
  - Android Studio プロジェクト作成
  - Clean Architecture + MVVM パターンの採用
  
- **CryptoManager**: 暗号化マネージャー
  - AES-256-GCM暗号化
  - NIST SP 800-38D準拠
  - IV自動生成
  - 認証タグ検証
  
- **KeyManager**: 鍵管理マネージャー
  - Android KeyStore統合
  - StrongBox対応（Titan M2）
  - ハードウェアバックド鍵
  
- **AuthenticationManager**: 認証マネージャー
  - BiometricPrompt統合
  - 生体認証（指紋・顔認証）
  - オートロック機能（30秒）
  
- **MainActivity**: メインアクティビティ
  - FLAG_SECURE（スクリーンショット防止）
  - 初回起動時の生体認証
  - バックグラウンド復帰時の再認証

#### Changed
- なし

#### Fixed
- なし

---

## 既知の問題

### セキュリティ
- なし（現時点で報告なし）

### 機能
- 暗号化処理のスレッド安全性の改善が必要（Phase 5で対応予定）
- テストデータ追加時のエラーハンドリング強化（Phase 5で対応予定）

### UI/UX
- レイアウトXMLの作成（Phase 6で対応予定）
- アニメーション・トランジション（Phase 6で対応予定）
- ダークモード対応（Phase 6で対応予定）

---

## 今後の予定

### v0.5.0 - Phase 5 (進行中)
- ✅ PasswordGenerator実装
- ✅ PasswordStrengthChecker実装
- 🔄 AddPasswordFragmentへの統合
- 📋 Constants定義
- 📋 ValidationUtils実装

### v0.6.0 - Phase 6 (予定)
- カスタムレイアウトXML
- Material Design 3対応
- アニメーション
- ダークモード

### v1.0.0 - Release (予定)
- 包括的なテスト
- パフォーマンス最適化
- ドキュメント完成
- Google Play Store公開準備

---

[Unreleased]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.4.0...HEAD
[0.4.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/wafukarubonara-stack/memoripass/compare/v0.0.1...v0.1.0
[0.0.1]: https://github.com/wafukarubonara-stack/memoripass/releases/tag/v0.0.1
