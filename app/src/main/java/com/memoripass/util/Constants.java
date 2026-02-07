/*
 * Copyright 2026 wafukarubonara-stack
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.memoripass.util;

/**
 * アプリケーション定数
 *
 * <p>アプリ全体で使用する定数を定義する。</p>
 *
 * @since 1.0
 */
public final class Constants {

    // インスタンス化を防止
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }

    /**
     * データベース定数
     */
    public static final class Database {
        public static final String NAME = "memoripass_db";
        public static final int VERSION = 1;

        private Database() {
        }
    }

    /**
     * 暗号化定数
     */
    public static final class Crypto {
        public static final String MASTER_KEY_ALIAS = "memoripass_master_key";
        public static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
        public static final int KEY_SIZE = 256;
        public static final int IV_SIZE = 12;  // 96 bits
        public static final int TAG_SIZE = 128; // bits

        private Crypto() {
        }
    }

    /**
     * 認証定数
     */
    public static final class Authentication {
        public static final String TITLE = "Memoripass";
        public static final String SUBTITLE = "生体認証でロックを解除";
        public static final String NEGATIVE_BUTTON = "キャンセル";
        public static final int AUTO_LOCK_TIMEOUT = 30000; // 30秒（ミリ秒）

        private Authentication() {
        }
    }

    /**
     * パスワード定数
     */
    public static final class Password {
        public static final int MIN_LENGTH = 1;
        public static final int MAX_LENGTH = 256;
        public static final int TITLE_MIN_LENGTH = 1;
        public static final int TITLE_MAX_LENGTH = 100;
        public static final int USERNAME_MAX_LENGTH = 100;
        public static final int URL_MAX_LENGTH = 500;
        public static final int NOTES_MAX_LENGTH = 1000;
        public static final int CATEGORY_MAX_LENGTH = 50;

        private Password() {
        }
    }

    /**
     * パスワード生成定数
     */
    public static final class PasswordGeneration {
        public static final int MIN_LENGTH = 8;
        public static final int MAX_LENGTH = 128;
        public static final int DEFAULT_LENGTH = 16;
        public static final int STRONG_LENGTH = 24;
        public static final int SIMPLE_LENGTH = 12;
        public static final int PIN_MIN_LENGTH = 4;
        public static final int PIN_MAX_LENGTH = 8;

        private PasswordGeneration() {
        }
    }

    /**
     * パスワード強度定数
     */
    public static final class PasswordStrength {
        public static final int VERY_WEAK_THRESHOLD = 20;
        public static final int WEAK_THRESHOLD = 40;
        public static final int MODERATE_THRESHOLD = 60;
        public static final int STRONG_THRESHOLD = 80;
        // 80以上は VERY_STRONG

        private PasswordStrength() {
        }
    }

    /**
     * UI定数
     */
    public static final class UI {
        public static final int TOAST_DURATION_SHORT = 2000; // ミリ秒
        public static final int TOAST_DURATION_LONG = 3500;  // ミリ秒
        public static final int ANIMATION_DURATION = 300;    // ミリ秒
        public static final int DEBOUNCE_DELAY = 500;        // ミリ秒

        private UI() {
        }
    }

    /**
     * 検索定数
     */
    public static final class Search {
        public static final int MIN_QUERY_LENGTH = 1;
        public static final int DEBOUNCE_DELAY = 300; // ミリ秒

        private Search() {
        }
    }

    /**
     * エラーメッセージ
     */
    public static final class ErrorMessages {
        public static final String EMPTY_TITLE = "タイトルを入力してください";
        public static final String EMPTY_PASSWORD = "パスワードを入力してください";
        public static final String TITLE_TOO_LONG = "タイトルは100文字以内で入力してください";
        public static final String PASSWORD_TOO_LONG = "パスワードは256文字以内で入力してください";
        public static final String INVALID_URL = "有効なURLを入力してください";
        public static final String ENCRYPTION_FAILED = "暗号化に失敗しました";
        public static final String DECRYPTION_FAILED = "復号に失敗しました";
        public static final String DATABASE_ERROR = "データベースエラーが発生しました";
        public static final String AUTHENTICATION_FAILED = "認証に失敗しました";
        public static final String AUTHENTICATION_CANCELLED = "認証がキャンセルされました";
        public static final String UNKNOWN_ERROR = "不明なエラーが発生しました";

        private ErrorMessages() {
        }
    }

    /**
     * 成功メッセージ
     */
    public static final class SuccessMessages {
        public static final String PASSWORD_SAVED = "パスワードを保存しました";
        public static final String PASSWORD_UPDATED = "パスワードを更新しました";
        public static final String PASSWORD_DELETED = "パスワードを削除しました";
        public static final String PASSWORD_COPIED = "パスワードをコピーしました";
        public static final String AUTHENTICATION_SUCCESS = "認証に成功しました";

        private SuccessMessages() {
        }
    }

    /**
     * ログタグ
     */
    public static final class LogTags {
        public static final String MAIN = "Memoripass";
        public static final String CRYPTO = "CryptoManager";
        public static final String AUTH = "AuthenticationManager";
        public static final String DATABASE = "AppDatabase";
        public static final String REPOSITORY = "PasswordRepository";
        public static final String VIEW_MODEL = "ViewModel";

        private LogTags() {
        }
    }

    /**
     * プリファレンスキー
     */
    public static final class PreferenceKeys {
        public static final String PREFERENCES_NAME = "memoripass_prefs";
        public static final String AUTO_LOCK_ENABLED = "auto_lock_enabled";
        public static final String AUTO_LOCK_TIMEOUT = "auto_lock_timeout";
        public static final String BIOMETRIC_ENABLED = "biometric_enabled";
        public static final String FIRST_LAUNCH = "first_launch";

        private PreferenceKeys() {
        }
    }

    /**
     * インテント定数
     */
    public static final class Intents {
        public static final String EXTRA_PASSWORD_ID = "password_id";
        public static final String EXTRA_TITLE = "title";
        public static final String ACTION_LOCK_APP = "com.memoripass.ACTION_LOCK_APP";

        private Intents() {
        }
    }

    /**
     * デフォルトカテゴリ
     */
    public static final class DefaultCategories {
        public static final String WORK = "仕事";
        public static final String PERSONAL = "個人";
        public static final String FINANCE = "金融";
        public static final String SOCIAL = "ソーシャル";
        public static final String SHOPPING = "ショッピング";
        public static final String OTHER = "その他";

        private DefaultCategories() {
        }
    }
}
