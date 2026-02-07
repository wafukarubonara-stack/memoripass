# Memoripass ドキュメント

このディレクトリには、Memoripassプロジェクトのすべてのドキュメントが整理されています。

---

## 📚 ドキュメント構成

### 🎯 プロジェクト管理 (`project/`)

プロジェクト全体の管理情報

- **[CHANGELOG.md](project/CHANGELOG.md)** - バージョン履歴と変更内容
- **[SECURITY.md](project/SECURITY.md)** - セキュリティポリシーと脆弱性報告

**用途**: バージョン間の変更を確認したい、セキュリティポリシーを知りたい

---

### 💻 開発記録 (`development/`)

開発プロセスと日々の記録

- **[DEVELOPMENT_JOURNAL.md](development/DEVELOPMENT_JOURNAL.md)** - 日別開発記録（最重要）
  - 2026年2月3日〜7日の詳細な開発履歴
  - 直面した問題と解決方法
  - 技術的な学びと判断の記録
  
- **[AI_COLLABORATION.md](development/AI_COLLABORATION.md)** - AI協働開発の記録
  - Claudeとの協働方法
  - プロンプトの工夫
  - 効果的なコミュニケーション方法

- **[DEVELOPMENT.md](development/DEVELOPMENT.md)** - 開発ガイド
  - セットアップ手順
  - ビルド方法
  - 開発環境の構築

- **[ai-sessions/](development/ai-sessions/)** - 各開発セッションの詳細ログ

**用途**: 開発の経緯を振り返りたい、問題解決方法を参照したい

---

### 🏗️ アーキテクチャ (`architecture/`)

システム設計とアーキテクチャ

- **[ARCHITECTURE.md](architecture/ARCHITECTURE.md)** - アーキテクチャ概要
  - Clean Architecture実装
  - レイヤー構成
  - データフロー
  - 実装状況

- **[ARCHITECTURE_DESIGN.md](architecture/ARCHITECTURE_DESIGN.md)** - 詳細設計
  
- **[CLASS_DIAGRAM.mermaid](architecture/CLASS_DIAGRAM.mermaid)** - クラス図

- **[SEQUENCE_DIAGRAMS.md](architecture/SEQUENCE_DIAGRAMS.md)** - シーケンス図

**用途**: システム設計を理解したい、アーキテクチャパターンを学びたい

---

### 🧪 テスト (`testing/`)

テスト計画と実行結果

- **[TESTING.md](testing/TESTING.md)** - テスト計画と結果
  - テストケース定義
  - 実行済みテスト結果
  - 既知の問題
  - 実機テスト記録（Google Pixel 9）

**用途**: テスト状況を確認したい、バグ情報を参照したい

---

### 📋 要件定義 (`requirements/`)

機能要件と仕様

- **[SRS-v2.0.md](requirements/SRS-v2.0.md)** - ソフトウェア要件仕様書

**用途**: プロジェクトの要件を確認したい

---

### 🔧 実装詳細 (`implementation/`)

各フェーズの実装ガイド

- **[PHASE1_DATA_LAYER.md](implementation/PHASE1_DATA_LAYER.md)** - Phase 1実装ガイド

**用途**: 実装の詳細手順を参照したい

---

### 🎨 デザイン (`design/`)

UIデザインとユーザー体験

**用途**: UIデザインを確認したい（将来実装予定）

---

### 📖 ユーザーマニュアル (`user-manual/`)

エンドユーザー向けドキュメント

**用途**: アプリの使い方を説明したい（将来実装予定）

---

## 🗺️ ドキュメント読み方ガイド

### 初めてプロジェクトを見る人

1. [../README.md](../README.md) - プロジェクト概要
2. [architecture/ARCHITECTURE.md](architecture/ARCHITECTURE.md) - システム構造
3. [development/DEVELOPMENT_JOURNAL.md](development/DEVELOPMENT_JOURNAL.md) - 開発の経緯

### 開発に参加したい人

1. [development/DEVELOPMENT.md](development/DEVELOPMENT.md) - 環境構築
2. [architecture/ARCHITECTURE.md](architecture/ARCHITECTURE.md) - コード構造
3. [testing/TESTING.md](testing/TESTING.md) - テスト方法

### 開発の経緯を振り返りたい人

1. [development/DEVELOPMENT_JOURNAL.md](development/DEVELOPMENT_JOURNAL.md) - 日別詳細記録
2. [project/CHANGELOG.md](project/CHANGELOG.md) - バージョン履歴
3. [development/ai-sessions/](development/ai-sessions/) - セッション別ログ

### 問題解決方法を探したい人

1. [development/DEVELOPMENT_JOURNAL.md](development/DEVELOPMENT_JOURNAL.md) - 「発見した問題と解決」セクション
2. [testing/TESTING.md](testing/TESTING.md) - 「既知の問題」セクション

### 技術的な判断の背景を知りたい人

1. [development/DEVELOPMENT_JOURNAL.md](development/DEVELOPMENT_JOURNAL.md) - 「技術的決定」セクション
2. [architecture/ARCHITECTURE.md](architecture/ARCHITECTURE.md) - 「設計原則」セクション

---

## 📅 更新履歴

このドキュメント構造は以下の日程で整理されました：

- **2026-02-03**: プロジェクト開始、初期ドキュメント作成
- **2026-02-07**: ドキュメント構造の再編成（カテゴリ別整理）

---

## 🔗 外部リンク

- [GitHub Repository](https://github.com/wafukarubonara-stack/memoripass)
- [Issues](https://github.com/wafukarubonara-stack/memoripass/issues)

---

## 📮 ドキュメントへの貢献

ドキュメントの改善提案や誤字脱字の報告は、GitHubのIssueまたはPull Requestでお願いします。

---

**最終更新**: 2026年2月7日
