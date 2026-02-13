# Memoripass テスト計画

## 📋 テスト戦略

Memoripassは、セキュリティとデータ整合性が最重要のアプリケーションです。以下のテスト戦略を採用しています：

1. **単体テスト** - 各コンポーネントの機能検証
2. **統合テスト** - レイヤー間の連携検証
3. **UIテスト** - ユーザーシナリオの検証
4. **セキュリティテスト** - 暗号化・認証の検証
5. **手動テスト** - 実機での動作確認

---
## ✅ 実行済みテスト（2026-02-08更新）

### 実機テスト結果 - Google Pixel 9 (Android 15)

**実施日**: 2026年2月8日  
**デバイス**: Google Pixel 9  
**Android**: Android 15 (API 35)  
**ビルド**: app-debug.apk  

| ID | テスト名 | 結果 | 実行時刻 | 備考 |
|----|---------|------|---------|------|
| TC001 | 初回起動時の生体認証 | ✅ 合格 | 12:52:59 | 指紋認証正常動作 |
| TC201 | パスワード追加 | ✅ 合格 | 12:53:14 | 暗号化・保存成功 |
| TC401 | パスワード詳細表示 | ✅ 合格 | 12:53:16 | 復号化成功、コピー機能確認 |
| TC501 | パスワード編集 | ✅ 合格 | 12:53:19 | 更新・再暗号化成功 |
| TC601 | パスワード削除 | ✅ 合格 | 12:53:29 | 削除確認ダイアログ・削除成功 |
| TC004 | オートロック（30秒） | ✅ 合格 | 12:55:36 | バックグラウンド後の再認証確認 |
| TC005 | スクリーンショット防止 | ✅ 合格 | 12:57:40 | FLAG_SECURE動作確認 |

**実行済み**: 7/7テスト (100%)  
**合格率**: 7/7 (100%) ✅  
**失敗**: 0

### テスト中に発見・修正した問題

#### UI配置問題
- **問題**: すべてのFragmentでステータスバー・インカメラと重なり
- **修正**: パディング調整 (top: 16→120)
- **影響**: AddPasswordFragment, PasswordDetailFragment, EditPasswordFragment, PasswordListFragment

#### Android 15 KeyStore問題
- **問題**: IV自動生成の強制、認証タイムアウト
- **修正**: CryptoManager.java, KeyManager.java
- **詳細**: DEVELOPMENT_JOURNAL.md参照

### 確認された機能

#### セキュリティ ✅
- 生体認証（指紋認証）
- AES-256-GCM暗号化
- Android KeyStore + StrongBox
- オートロック（30秒）
- スクリーンショット防止

#### パスワード管理 ✅
- パスワードの追加・表示・編集・削除
- 暗号化/復号化
- クリップボードコピー

#### UI/UX ✅
- 生体認証ダイアログ
- FloatingActionButton
- RecyclerView
- Toast通知
- ステータスバー対応

### Logcat確認事項

成功時のログパターン：
```
D/AddPasswordViewModel: Encrypting password on main thread...
D/CryptoManager: Encryption successful
I/AddPasswordViewModel: Password saved successfully
D/CryptoManager: Decryption successful
I/PasswordListViewModel: Password deleted successfully
```

### 次回テスト予定

以下は今後実施予定：
- TC102: 大量データ（100件以上）のパフォーマンステスト
- TC103: 長いパスワード（256文字）のテスト
- TC202: バリデーションエラーのテスト
- TC301: 検索機能のテスト（実装後）
- セキュリティテスト（暗号化強度、メモリダンプ）

### テスト環境

- Ubuntu 22.04
- Android Studio
- ADB (Android Debug Bridge)
- Logcat監視## ✅ 実行済みテスト（2026-02-08更新）

### 実機テスト結果 - Google Pixel 9 (Android 15)

**実施日**: 2026年2月8日  
**デバイス**: Google Pixel 9  
**Android**: Android 15 (API 35)  
**ビルド**: app-debug.apk  

| ID | テスト名 | 結果 | 実行時刻 | 備考 |
|----|---------|------|---------|------|
| TC001 | 初回起動時の生体認証 | ✅ 合格 | 12:52:59 | 指紋認証正常動作 |
| TC201 | パスワード追加 | ✅ 合格 | 12:53:14 | 暗号化・保存成功 |
| TC401 | パスワード詳細表示 | ✅ 合格 | 12:53:16 | 復号化成功、コピー機能確認 |
| TC501 | パスワード編集 | ✅ 合格 | 12:53:19 | 更新・再暗号化成功 |
| TC601 | パスワード削除 | ✅ 合格 | 12:53:29 | 削除確認ダイアログ・削除成功 |
| TC004 | オートロック（30秒） | ✅ 合格 | 12:55:36 | バックグラウンド後の再認証確認 |
| TC005 | スクリーンショット防止 | ✅ 合格 | 12:57:40 | FLAG_SECURE動作確認 |

**実行済み**: 7/7テスト (100%)  
**合格率**: 7/7 (100%) ✅  
**失敗**: 0

### テスト中に発見・修正した問題

#### UI配置問題
- **問題**: すべてのFragmentでステータスバー・インカメラと重なり
- **修正**: パディング調整 (top: 16→120)
- **影響**: AddPasswordFragment, PasswordDetailFragment, EditPasswordFragment, PasswordListFragment

#### Android 15 KeyStore問題
- **問題**: IV自動生成の強制、認証タイムアウト
- **修正**: CryptoManager.java, KeyManager.java
- **詳細**: DEVELOPMENT_JOURNAL.md参照

### 確認された機能

#### セキュリティ ✅
- 生体認証（指紋認証）
- AES-256-GCM暗号化
- Android KeyStore + StrongBox
- オートロック（30秒）
- スクリーンショット防止

#### パスワード管理 ✅
- パスワードの追加・表示・編集・削除
- 暗号化/復号化
- クリップボードコピー

#### UI/UX ✅
- 生体認証ダイアログ
- FloatingActionButton
- RecyclerView
- Toast通知
- ステータスバー対応

### Logcat確認事項

成功時のログパターン：
```
D/AddPasswordViewModel: Encrypting password on main thread...
D/CryptoManager: Encryption successful
I/AddPasswordViewModel: Password saved successfully
D/CryptoManager: Decryption successful
I/PasswordListViewModel: Password deleted successfully
```

### 次回テスト予定

以下は今後実施予定：
- TC102: 大量データ（100件以上）のパフォーマンステスト
- TC103: 長いパスワード（256文字）のテスト
- TC202: バリデーションエラーのテスト
- TC301: 検索機能のテスト（実装後）
- セキュリティテスト（暗号化強度、メモリダンプ）

### テスト環境

- Ubuntu 22.04
- Android Studio
- ADB (Android Debug Bridge)
- Logcat監視

#########################################
## ✅ 実行済みテスト（2026-02-07更新）

### セキュリティ・認証テスト

| ID | テスト名 | 結果 | 実行日 | 備考 |
|----|---------|------|--------|------|
| TC001 | 初回起動時の生体認証 | ✅ 合格 | 2026-02-07 | Google Pixel 9で確認 |
| TC002 | 認証失敗時の動作 | ⏳ 未実施 | - | 次回実施予定 |
| TC003 | 認証キャンセル時の動作 | ⏳ 未実施 | - | 次回実施予定 |
| TC004 | オートロック（30秒） | ⏳ 未実施 | - | 次回実施予定 |
| TC005 | スクリーンショット防止 | ⏳ 未実施 | - | 次回実施予定 |

### データ層テスト

| ID | テスト名 | 結果 | 実行日 | 備考 |
|----|---------|------|--------|------|
| TC101 | データベース初期化 | ✅ 合格 | 2026-02-06 | Logcatで確認 |
| TC102 | 暗号化・復号テスト | 🔄 修正中 | 2026-02-07 | スレッド問題修正済み |

### UI層テスト

| ID | テスト名 | 結果 | 実行日 | 備考 |
|----|---------|------|--------|------|
| TC201 | パスワード一覧画面の表示 | ✅ 合格 | 2026-02-06 | 空リスト表示確認 |
| TC202 | パスワード追加画面の表示 | ✅ 合格 | 2026-02-07 | UI修正後 |
| TC203 | パスワード追加機能 | 🔄 修正中 | 2026-02-07 | スレッド問題修正済み |

---

## 📝 詳細テストケース

### TC001: 初回起動時の生体認証

**前提条件:**
- アプリが初回起動
- デバイスに生体認証が設定済み

**テスト手順:**
1. アプリを起動
2. 生体認証ダイアログが表示されることを確認
3. 指紋/顔認証を実行

**期待結果:**
- ✅ 生体認証ダイアログが表示される
- ✅ 認証成功後、パスワード一覧画面が表示される
- ✅ Toast「認証に成功しました」が表示される

**Logcat確認:**
```
D/AuthenticationManager: Biometric authentication started
I/MainActivity: Authentication successful
D/MainActivity: PasswordListFragment displayed
```

**結果:** ✅ 合格

---

### TC002: 認証失敗時の動作

**前提条件:**
- アプリ起動時

**テスト手順:**
1. アプリを起動
2. 生体認証で意図的に失敗（間違った指紋など）
3. 再度認証を試行

**期待結果:**
- ✅ Toast「認証に失敗しました。もう一度お試しください。」が表示
- ✅ 認証ダイアログが再表示される
- ✅ アプリは終了しない

**Logcat確認:**
```
W/MainActivity: Authentication failed
```

**結果:** ⏳ 未実施（デバイス接続待ち）

---

### TC003: 認証キャンセル時の動作

**前提条件:**
- アプリ起動時

**テスト手順:**
1. アプリを起動
2. 生体認証ダイアログで「キャンセル」をタップ

**期待結果:**
- ✅ Toast「認証がキャンセルされました」が表示
- ✅ アプリが終了する

**Logcat確認:**
```
D/MainActivity: Authentication cancelled
```

**結果:** ⏳ 未実施（デバイス接続待ち）

---

### TC004: オートロック（30秒）

**前提条件:**
- アプリが認証済み状態

**テスト手順:**
1. アプリを認証後、パスワード一覧画面を表示
2. ホームボタンでバックグラウンドに移行
3. 30秒以上待機
4. アプリに戻る

**期待結果:**
- ✅ 再度生体認証が要求される
- ✅ 認証成功後、元の画面に戻る

**Logcat確認:**
```
D/AuthenticationManager: Auto-lock timer started
D/AuthenticationManager: Auto-lock triggered after 30 seconds
```

**結果:** ✅ 合格

---

### TC005: スクリーンショット防止

**前提条件:**
- アプリが起動中

**テスト手順:**
1. アプリを起動
2. スクリーンショットを撮影（音量Down + 電源ボタン）

**期待結果:**
- ✅ スクリーンショットが失敗する
- ✅ 「スクリーンショットを撮影できません」のメッセージが表示される

**Logcat確認:**
（特になし - OSレベルで防止される）

**結果:** ⏳ 未実施（デバイス接続待ち）

---

### TC101: データベース初期化

**前提条件:**
- アプリ初回起動

**テスト手順:**
1. アプリを起動
2. 認証を完了

**期待結果:**
- ✅ データベースが正常に作成される
- ✅ テーブル（password_entries, categories）が作成される

**Logcat確認:**
```
D/AppDatabase: Database created
D/PasswordRepository: PasswordRepository initialized
```

**結果:** ✅ 合格

---

### TC102: 暗号化・復号テスト

**前提条件:**
- CryptoManagerが初期化済み

**テスト手順:**
1. テスト用パスワード「TestPassword123!」を暗号化
2. 暗号化されたデータを復号
3. 元のパスワードと一致するか確認

**期待結果:**
- ✅ 暗号化後のデータが平文と異なる
- ✅ 復号後のデータが平文と一致する

**Logcat確認:**
```
D/CryptoManager: Encryption successful
D/CryptoManager: Decryption successful
```

**結果:** ⏳ 未実施（実機テスト待ち）

---

### TC201: パスワード一覧画面の表示

**前提条件:**
- アプリが認証済み
- データベースにデータなし

**テスト手順:**
1. 認証後、パスワード一覧画面が表示される

**期待結果:**
- ✅ RecyclerViewが表示される
- ✅ 空のリストが表示される

**Logcat確認:**
```
D/PasswordListViewModel: PasswordListViewModel initialized successfully
D/GetAllPasswordsUseCase: Executing GetAllPasswordsUseCase
```

**結果:** ✅ 合格

---

### TC202: LiveDataの監視

**前提条件:**
- パスワード一覧画面が表示中

**テスト手順:**
1. データベースに変更を加える（別プロセスでデータ追加）
2. UIが自動更新されるか確認

**期待結果:**
- ✅ データ変更後、自動的にRecyclerViewが更新される

**Logcat確認:**
```
D/PasswordListViewModel: Passwords loaded: [count]
```

**結果:** ⏳ 未実施（実機テスト待ち）

---

## 🧪 今後実施予定のテスト

### Phase 4: パスワード追加・編集・削除テスト

| ID | テスト名 | 優先度 |
|----|---------|--------|
| TC301 | パスワード追加（正常系） | 高 |
| TC302 | パスワード追加（バリデーションエラー） | 高 |
| TC401 | パスワード詳細表示 | 高 |
| TC402 | パスワード検索 | 中 |
| TC501 | パスワード削除 | 高 |
| TC502 | パスワード更新 | 高 |

### Phase 5: ユーティリティテスト

| ID | テスト名 | 優先度 |
|----|---------|--------|
| TC601 | パスワード生成（デフォルト） | 中 |
| TC602 | パスワード生成（カスタム） | 中 |
| TC603 | パスワード強度チェック | 低 |

---

## 🔐 セキュリティテスト

### 暗号化強度テスト

**テスト項目:**
1. ✅ AES-256-GCM使用確認
2. ⏳ IV長さ（12バイト）確認
3. ⏳ 認証タグ長さ（128ビット）確認
4. ⏳ KeyStore使用確認
5. ⏳ StrongBox使用確認（対応デバイスのみ）

### メモリセキュリティテスト

**テスト項目:**
1. ⏳ パスワード使用後のメモリゼロクリア確認
2. ⏳ メモリダンプでの平文パスワード検出テスト
3. ⏳ ログ出力での機密情報漏洩確認

### 認証バイパステスト

**テスト項目:**
1. ⏳ バックグラウンド復帰時の再認証確認
2. ⏳ タスクスイッチャーからの再認証確認
3. ⏳ アプリ強制終了後の認証確認

---

## 📱 デバイステスト

### テスト対象デバイス

| デバイス | OS | 状態 |
|---------|-----|------|
| Google Pixel 9 | Android 15 | ⏳ 予定 |
| Samsung Galaxy S24 | Android 15 | ⏳ 予定 |
| OnePlus 12 | Android 15 | ⏳ 予定 |

### エミュレータテスト

| エミュレータ | OS | 状態 |
|------------|-----|------|
| Pixel 9 API 35 | Android 15 | ⏳ 予定 |

---

## 🐛 既知の問題

### 重要度：高

なし

### 重要度：中

1. **暗号化のスレッド安全性**
   - 現象：バックグラウンドスレッドから暗号化すると失敗
   - 対策：メインスレッドで暗号化処理を実行（実装済み）
   - 状態：修正済み

### 重要度：低

なし

---

## 📊 テストカバレッジ目標

### Phase 5完了時点

| レイヤー | 目標カバレッジ | 現在 |
|---------|--------------|------|
| Domain Layer | 80% | 0% |
| Data Layer | 70% | 0% |
| Presentation Layer | 60% | 0% |
| Infrastructure Layer | 90% | 0% |

### v1.0リリース時点

| レイヤー | 目標カバレッジ |
|---------|--------------|
| Domain Layer | 90% |
| Data Layer | 85% |
| Presentation Layer | 75% |
| Infrastructure Layer | 95% |

---

## 🚀 テスト実行方法

### 単体テスト

```bash
# すべてのテストを実行
./gradlew test

# 特定のテストクラスを実行
./gradlew test --tests com.memoripass.crypto.CryptoManagerTest

# カバレッジレポートを生成
./gradlew jacocoTestReport
```

### UIテスト

```bash
# UIテストを実行（デバイス接続必須）
./gradlew connectedAndroidTest

# 特定のテストを実行
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.memoripass.ui.list.PasswordListFragmentTest
```

### 手動テスト

```bash
# デバッグビルドをインストール
./gradlew installDebug

# Logcatを監視
adb logcat | grep "com.memoripass"
```

---

## 📝 テストレポート

テスト実行後、以下の場所にレポートが生成されます：

- **単体テスト**: `app/build/reports/tests/testDebugUnitTest/index.html`
- **UIテスト**: `app/build/reports/androidTests/connected/index.html`
- **カバレッジ**: `app/build/reports/jacoco/jacocoTestReport/html/index.html`

---

## 🔄 継続的テスト

### GitHub Actions（予定）

```yaml
# .github/workflows/test.yml
name: Run Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Unit Tests
        run: ./gradlew test
      - name: Generate Coverage Report
        run: ./gradlew jacocoTestReport
```

---

## 📮 バグ報告

バグを発見した場合：

1. [Issues](https://github.com/wafukarubonara-stack/memoripass/issues)で新しいissueを作成
2. 以下の情報を含める：
   - テストケースID（該当する場合）
   - デバイス情報
   - OS情報
   - 再現手順
   - 期待される動作
   - 実際の動作
   - Logcat出力
   - スクリーンショット（可能な場合）

### セキュリティ上の問題

セキュリティに関する問題は、**公開issueではなく非公開で報告**してください。

---

## 🙏 テストへの貢献

テストケースの追加や改善の提案は歓迎します！

1. テストケースを作成
2. プルリクエストを送信
3. レビュー後にマージ

---

## 📚 参考資料

- [Android Testing Guide](https://developer.android.com/training/testing)
- [Espresso](https://developer.android.com/training/testing/espresso)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
---

## Phase 6 実機テスト結果

### テスト実施情報

| 項目 | 内容 |
|-----|------|
| **実施日** | 2026年2月12日 |
| **デバイス** | Google Pixel 9 |
| **Android** | Android 15 (API 35) |
| **フェーズ** | Phase 6（UI/UX改善） |

### テスト結果

| テストID | テスト内容 | 結果 | 実行時刻 |
|---------|-----------|------|---------|
| PH6-001 | パスワード追加（新UI） | ✅ 合格 | 23:20:14 |
| PH6-002 | パスワード追加（2回目） | ✅ 合格 | 23:20:32 |
| PH6-003 | パスワード詳細表示 | ✅ 合格 | 23:20:34 |
| PH6-004 | パスワード詳細表示（別エントリ） | ✅ 合格 | 23:20:37 |
| PH6-005 | パスワード編集画面遷移 | ✅ 合格 | 23:20:39 |
| PH6-006 | パスワード編集（複数回） | ✅ 合格 | 23:21:02 |
| PH6-007 | 詳細→編集→詳細の画面遷移 | ✅ 合格 | 23:21:15 |
| PH6-008 | ステータスバー重なり修正確認 | ✅ 合格 | 2026-02-14 |

**合格率: 8/8 (100%)** ✅

### 確認されたUI変更

| 画面 | 変更内容 | 確認 |
|-----|---------|------|
| PasswordListFragment | MaterialToolbar、FAB、MaterialCardView | ✅ |
| AddPasswordFragment | TextInputLayout、パスワード表示切替 | ✅ |
| PasswordDetailFragment | MaterialCardView、コピーボタン | ✅ |
| EditPasswordFragment | TextInputLayout、既存データ表示 | ✅ |

### 発見・修正したバグ

| バグID | 内容 | 修正方法 | 状態 |
|-------|------|---------|------|
| BUG-PH6-001 | ステータスバーとの重なり（全Fragment） | fitsSystemWindows="true"追加 | ✅ 修正済み |
| BUG-PH6-002 | 全角スペース混入（fragment_add_password.xml） | sed で半角に変換 | ✅ 修正済み |

### Logcat確認事項

```
23:20:14  AddPasswordViewModel: Password saved successfully: あああ
23:20:32  AddPasswordViewModel: Password saved successfully: かかか
23:20:34  CryptoManager: Decryption successful
23:20:39  EditPasswordViewModel: Loading password: ac4cef3a...
23:21:02  CryptoManager: Decryption successful
```

**エラー: 0件 / クラッシュ: 0件**

