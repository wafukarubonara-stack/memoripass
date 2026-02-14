# Memoripass

**完全端末内完結型パスワード管理Androidアプリ**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android%2015+-green.svg)](https://developer.android.com)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://github.com/wafukarubonara-stack/memoripass)
[![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)](https://github.com/wafukarubonara-stack/memoripass/releases/tag/v1.0.0)

Memoripassは、セキュリティとプライバシーを最優先に設計された、完全端末内完結型のパスワード管理アプリです。すべてのデータは端末内で暗号化され、外部サーバーとの通信は一切行いません。

---

## ✨ 主な特徴

### 🔐 セキュリティ

- **AES-256-GCM暗号化** - NIST SP 800-38D準拠の強力な暗号化
- **生体認証** - 指紋・顔認証による安全なアクセス
- **オートロック** - 30秒のアイドル後に自動ロック
- **ハードウェアセキュリティ** - Android KeyStore + StrongBox（Titan M2）対応
- **スクリーンショット防止** - FLAG_SECUREによる画面録画防止

### 🎯 機能

- ✅ パスワードの追加・編集・削除
- ✅ パスワード詳細表示とコピー
- ✅ セキュアなパスワード生成（8〜128文字）
- ✅ パスワード強度チェック（5段階評価）
- ✅ カテゴリ別管理
- ✅ リアルタイム検索
- ✅ ダークモード対応
- ✅ Material Design 3 UI
- ✅ 完全オフライン動作

### 🏗️ アーキテクチャ

- **Clean Architecture** - レイヤー分離による保守性
- **MVVM + Repository Pattern** - テスタブルな設計
- **Room Database** - 高速なローカルストレージ
- **LiveData** - リアクティブなUI更新

---

## 📱 システム要件

- **OS**: Android 15 (API Level 35) 以上
- **ストレージ**: 50MB以上の空き容量
- **セキュリティ**: 生体認証（指紋/顔認証）またはデバイスロック設定済み
- **推奨**: Google Pixel 9以降（Titan M2 StrongBox対応）

---

## 🚀 インストール

### 開発版ビルド

```bash
# リポジトリをクローン
git clone https://github.com/wafukarubonara-stack/memoripass.git
cd memoripass

# ビルド
./gradlew build

# デバイスにインストール
./gradlew installDebug
```

### リリース版（準備中）

Google Play Storeでの公開を予定しています。

---

## 📖 使用方法

### 初回起動

1. アプリを起動
2. 生体認証を実行
3. 認証成功後、パスワード一覧画面が表示されます

### パスワードの追加

1. 右下の「+」ボタンをタップ
2. 必要な情報を入力
   - **タイトル** (必須): サービス名など
   - **ユーザー名**: メールアドレスやIDなど
   - **パスワード** (必須): 手動入力または「生成」ボタンで自動生成
   - **URL**: Webサイトのアドレス
   - **カテゴリ**: 分類用
   - **メモ**: 追加情報
3. 「保存」をタップ

### パスワードの検索

1. 一覧画面上部の検索バーに入力
2. タイトル・ユーザー名でリアルタイム絞り込み
3. 入力を消すと全件表示に戻る

### パスワードの表示

1. 一覧からエントリをタップ
2. 詳細画面が表示されます
3. 「パスワードをコピー」でクリップボードにコピー

### パスワードの編集

1. 詳細画面で「編集」をタップ
2. 情報を変更
3. 「保存」をタップ

### パスワードの削除

1. 一覧画面でエントリを長押し
2. 削除確認ダイアログで「削除」をタップ

---

## 🏛️ アーキテクチャ

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  ┌─────────────────────────────────┐   │
│  │ Activity / Fragment             │   │
│  │  - MainActivity                 │   │
│  │  - PasswordListFragment         │   │
│  │  - AddPasswordFragment          │   │
│  │  - PasswordDetailFragment       │   │
│  │  - EditPasswordFragment         │   │
│  └──────────────┬──────────────────┘   │
│                 │                       │
│  ┌──────────────▼──────────────────┐   │
│  │ ViewModel                       │   │
│  │  - PasswordListViewModel        │   │
│  │  - AddPasswordViewModel         │   │
│  │  - PasswordDetailViewModel      │   │
│  │  - EditPasswordViewModel        │   │
│  └──────────────┬──────────────────┘   │
└─────────────────┼───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Domain Layer                   │
│  ┌─────────────────────────────────┐   │
│  │ Use Cases                       │   │
│  │  - AddPasswordUseCase           │   │
│  │  - UpdatePasswordUseCase        │   │
│  │  - DeletePasswordUseCase        │   │
│  │  - GetPasswordUseCase           │   │
│  │  - GetAllPasswordsUseCase       │   │
│  └──────────────┬──────────────────┘   │
│                 │                       │
│  ┌──────────────▼──────────────────┐   │
│  │ Domain Models                   │   │
│  │  - Password (decrypted)         │   │
│  └─────────────────────────────────┘   │
└─────────────────┼───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Data Layer                    │
│  ┌─────────────────────────────────┐   │
│  │ Repository                      │   │
│  │  - PasswordRepository           │   │
│  └──────────────┬──────────────────┘   │
│                 │                       │
│  ┌──────────────▼──────────────────┐   │
│  │ Data Access Object (DAO)        │   │
│  │  - PasswordEntryDao             │   │
│  │  - CategoryDao                  │   │
│  └──────────────┬──────────────────┘   │
│                 │                       │
│  ┌──────────────▼──────────────────┐   │
│  │ Room Database                   │   │
│  │  - AppDatabase                  │   │
│  └──────────────┬──────────────────┘   │
│                 │                       │
│  ┌──────────────▼──────────────────┐   │
│  │ Data Models                     │   │
│  │  - PasswordEntry (encrypted)    │   │
│  │  - Category                     │   │
│  └─────────────────────────────────┘   │
└─────────────────┼───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Infrastructure Layer               │
│  ┌─────────────────────────────────┐   │
│  │ Security                        │   │
│  │  - CryptoManager (AES-256-GCM)  │   │
│  │  - KeyManager (KeyStore)        │   │
│  │  - AuthenticationManager        │   │
│  └─────────────────────────────────┘   │
│  ┌─────────────────────────────────┐   │
│  │ Utilities                       │   │
│  │  - PasswordGenerator            │   │
│  │  - PasswordStrengthChecker      │   │
│  │  - ValidationUtils              │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

---

## 🔒 セキュリティ仕様

### 暗号化

- **アルゴリズム**: AES-256-GCM
- **鍵長**: 256ビット
- **IV長**: 12バイト（96ビット）
- **認証タグ**: 128ビット
- **鍵管理**: Android KeyStore（ハードウェアバックド）
- **準拠規格**: NIST SP 800-38D

### 認証

- **生体認証**: BiometricPrompt API
- **強度**: BIOMETRIC_STRONG + DEVICE_CREDENTIAL
- **オートロック**: 30秒（変更可能）
- **セッション管理**: メモリ内のみ保持

### データ保護

- **ストレージ**: 端末内のみ（外部バックアップ無効）
- **画面保護**: FLAG_SECURE（スクリーンショット防止）
- **メモリ**: 使用後にゼロクリア
- **ログ**: 機密情報の出力禁止

---

## 🧪 テスト

### 実行済みテスト（Google Pixel 9 / Android 15）

- ✅ TC001: 初回起動時の生体認証
- ✅ TC004: オートロック（30秒）
- ✅ TC101: データベース初期化
- ✅ TC201: パスワード一覧画面の表示
- ✅ PH6-001〜008: Phase 6 UI/UX全テスト（8/8合格）

### テストの実行

```bash
# 単体テスト
./gradlew test

# UIテスト
./gradlew connectedAndroidTest
```

---

## 📊 実装状況

| フェーズ | 内容 | 状態 |
|---------|------|------|
| Phase 0 | セキュリティ基盤（暗号化・認証） | ✅ 完了 |
| Phase 1 | データ層（Database, DAO, Repository） | ✅ 完了 |
| Phase 2 | ドメイン層（UseCase, Domain Model） | ✅ 完了 |
| Phase 3 | UI基礎（ViewModel, Fragment, Adapter） | ✅ 完了 |
| Phase 4 | UI拡張（Add/Detail/Edit画面） | ✅ 完了 |
| Phase 5 | ユーティリティ（Generator, Checker） | ✅ 完了 |
| Phase 6 | UI/UX改善（Material Design 3） | ✅ 完了 |
| v1.0リリース準備 | ProGuard, アイコン, CHANGELOG | ✅ 完了 |

---

## 🛣️ ロードマップ

### v1.0.0（✅ リリース済み - 2026-02-14）

- ✅ 基本的なパスワード管理機能（追加・編集・削除）
- ✅ AES-256-GCM暗号化
- ✅ 生体認証・オートロック
- ✅ パスワード自動生成（8〜128文字）
- ✅ パスワード強度チェック（5段階）
- ✅ リアルタイム検索
- ✅ Material Design 3 UI
- ✅ ダークモード対応
- ✅ カスタムアプリアイコン

### v1.1（予定）

- カテゴリ管理UI
- パスワード履歴
- エクスポート/インポート（暗号化済み）
- アニメーション・画面遷移改善

### v2.0（将来）

- 自動入力サービス
- ブラウザ拡張機能連携
- 2要素認証コード管理
- 安全なパスワード共有

---

## 🤝 コントリビューション

現在、このプロジェクトは個人開発中です。フィードバックやバグ報告は歓迎します！

### 報告方法

1. [Issues](https://github.com/wafukarubonara-stack/memoripass/issues)でバグ報告
2. セキュリティ上の問題は非公開で報告してください

---

## 📄 ライセンス

```
Copyright 2026 wafukarubonara-stack

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 🙏 謝辞

- [Android Jetpack](https://developer.android.com/jetpack)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Material Components for Android](https://github.com/material-components/material-components-android)

---

## 📮 連絡先

- GitHub: [@wafukarubonara-stack](https://github.com/wafukarubonara-stack)
- リポジトリ: [memoripass](https://github.com/wafukarubonara-stack/memoripass)

---

## 📖 ドキュメント

プロジェクトの詳細なドキュメントは[docs/](docs/)ディレクトリに整理されています。

| カテゴリ | ドキュメント | 内容 |
|---------|-------------|------|
| 📋 **プロジェクト管理** | [CHANGELOG](docs/project/CHANGELOG.md) | バージョン履歴と変更内容 |
| | [SECURITY](docs/project/SECURITY.md) | セキュリティポリシー |
| 💻 **開発記録** | [開発日誌](docs/development/DEVELOPMENT_JOURNAL.md) | 日別の詳細な開発記録 |
| | [AI協働](docs/development/AI_COLLABORATION.md) | Claudeとの協働記録 |
| | [開発ガイド](docs/development/DEVELOPMENT.md) | 環境構築・ビルド方法 |
| 🏗️ **アーキテクチャ** | [アーキテクチャ概要](docs/architecture/ARCHITECTURE.md) | システム設計と実装状況 |
| 🧪 **テスト** | [テスト計画](docs/testing/TESTING.md) | テストケースと実行結果 |

**📚 すべてのドキュメントを見る**: [docs/README.md](docs/README.md)

---

**⚠️ 重要な注意事項**

このアプリはパスワードを安全に管理しますが、完全な安全性を保証するものではありません。以下の点にご注意ください：

- 端末の紛失・盗難に備えて、重要なパスワードは別途バックアップしてください
- 定期的にパスワードを変更してください
- 信頼できないアプリや改造版は使用しないでください
- 端末のOSとセキュリティパッチを最新の状態に保ってください
