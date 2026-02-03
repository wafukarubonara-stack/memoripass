# 🔐 Memoripass

**完全端末内完結型パスワード管理Androidアプリ**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/Platform-Android%2015+-green.svg)](https://developer.android.com)

---

## 📋 概要

Memoripassは、クラウド型パスワード管理の「通信傍受」「サーバー漏洩」リスクを完全に排除する、端末内完結型の高セキュリティパスワード管理アプリです。

### 主な特徴

- 🔒 **完全オフライン動作** - インターネット権限なし、通信傍受のリスクゼロ
- 🛡️ **ハードウェアセキュリティ** - Pixel 9のTitan M2チップ（StrongBox）で鍵を保護
- 🔐 **AES-256-GCM暗号化** - 軍事グレードの暗号化
- 👆 **生体認証** - 指紋・顔認証でセキュアなアクセス
- 📱 **スクリーンショット保護** - 画面キャプチャ完全ブロック
- 🎯 **シンプルなUX** - セキュリティと使いやすさの両立

---

## 🎯 プロジェクトステータス

**現在のフェーズ**: 設計・基盤構築  
**進捗**: 20%

- [x] 要件定義完了
- [x] プロジェクト基盤構築
- [x] ドキュメント整備
- [ ] 設計フェーズ（進行中）
- [ ] 実装フェーズ
- [ ] テストフェーズ
- [ ] リリース準備

---

## 🔧 技術スタック

| カテゴリ | 技術 |
|---------|------|
| 言語 | Java (JDK 17) |
| プラットフォーム | Android 15+ (API Level 35+) |
| ターゲットデバイス | Google Pixel 9シリーズ |
| 暗号化 | AES-256-GCM |
| 鍵管理 | AndroidKeyStore (StrongBox) |
| 認証 | BiometricPrompt API |
| ビルドツール | Gradle 8.x |
| テスト | JUnit 4, Mockito, Espresso |

---

## 📚 ドキュメント

- [要件定義書](docs/requirements/SRS-v2.0.md) - 機能要件・非機能要件の詳細
- [開発ガイド](DEVELOPMENT.md) - 開発環境のセットアップ
- [AI活用ガイド](AI_COLLABORATION.md) - Claudeとの協働方法
- [セキュリティポリシー](SECURITY.md) - 脆弱性報告方法

---

## 🚀 開発環境セットアップ

### 前提条件

- Ubuntu Linux 22.04+ または macOS
- Android Studio Koala | 2024.1.1+
- JDK 17
- Git
- Google Pixel 9 または Android 15+のエミュレータ

### セットアップ

```bash
# リポジトリをクローン
git clone https://github.com/wafukarubonara-stack/memoripass.git
cd memoripass

# Android Studioでプロジェクトを開く
# File > Open > memoripassディレクトリを選択

# 依存関係を同期
./gradlew build

# テスト実行
./gradlew test

# APKビルド
./gradlew assembleDebug
```

詳細は [DEVELOPMENT.md](DEVELOPMENT.md) を参照

---

## 🔐 セキュリティ

このプロジェクトはセキュリティを最優先事項としています。

### セキュリティ機能

- **ハードウェアバックド鍵**: Titan M2チップ内で鍵生成・保管
- **認証付き暗号**: AES-256-GCMで改ざん検出
- **メモリ保護**: センシティブデータの即座なゼロクリア
- **画面保護**: FLAG_SECUREによるキャプチャ防止
- **コード難読化**: R8/ProGuardによる逆コンパイル対策
- **ルート検出**: ルート化端末での警告表示

### 脆弱性報告

セキュリティ上の問題を発見された場合は、[SECURITY.md](SECURITY.md)に従ってご報告ください。

---

## 🤖 AI開発について

このプロジェクトは、Claude (Anthropic) の支援を受けて開発されています。
AI活用のログやプロンプト集は `docs/ai-sessions/` で公開しています。

AI協働開発に興味がある方は [AI_COLLABORATION.md](AI_COLLABORATION.md) をご覧ください。

---

## 📄 ライセンス

このプロジェクトは [Apache License 2.0](LICENSE) の下で公開されています。

---

## 👤 作成者

**wafukarubonara-stack**

- GitHub: [@wafukarubonara-stack](https://github.com/wafukarubonara-stack)

---

## 📊 開発ログ

開発の進捗や学びは [AI Sessions](docs/ai-sessions/) で記録しています。

---

## ⚠️ 免責事項

このアプリは現在開発中です。本番環境での使用は推奨されません。

マスター認証情報（生体認証設定）を紛失すると、保存されたすべてのパスワードが完全に復元不可能になります。定期的なバックアップを強く推奨します。

---

**最終更新**: 2026年2月2日
