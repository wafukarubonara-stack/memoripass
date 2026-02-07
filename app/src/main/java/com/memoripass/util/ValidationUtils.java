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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.regex.Pattern;

/**
 * バリデーションユーティリティ
 *
 * <p>入力値の検証を行うユーティリティクラス。</p>
 *
 * @since 1.0
 */
public final class ValidationUtils {

    // URL検証用の正規表現
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?://)" +
            "([\\da-z.-]+)\\.([a-z.]{2,6})" +
            "([/\\w .-]*)*/?$",
            Pattern.CASE_INSENSITIVE
    );

    // メールアドレス検証用の正規表現
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    // インスタンス化を防止
    private ValidationUtils() {
        throw new AssertionError("Cannot instantiate ValidationUtils class");
    }

    /**
     * 文字列が空かnullかチェック
     *
     * @param text チェックする文字列
     * @return true: 空またはnull, false: 値あり
     */
    public static boolean isEmpty(@Nullable String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * 文字列が空でないかチェック
     *
     * @param text チェックする文字列
     * @return true: 値あり, false: 空またはnull
     */
    public static boolean isNotEmpty(@Nullable String text) {
        return !isEmpty(text);
    }

    /**
     * パスワードタイトルを検証
     *
     * @param title タイトル
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validateTitle(@Nullable String title) {
        if (isEmpty(title)) {
            return ValidationResult.error(Constants.ErrorMessages.EMPTY_TITLE);
        }

        if (title.length() > Constants.Password.TITLE_MAX_LENGTH) {
            return ValidationResult.error(Constants.ErrorMessages.TITLE_TOO_LONG);
        }

        return ValidationResult.success();
    }

    /**
     * パスワードを検証
     *
     * @param password パスワード
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validatePassword(@Nullable String password) {
        if (isEmpty(password)) {
            return ValidationResult.error(Constants.ErrorMessages.EMPTY_PASSWORD);
        }

        if (password.length() < Constants.Password.MIN_LENGTH) {
            return ValidationResult.error("パスワードは" + Constants.Password.MIN_LENGTH + "文字以上で入力してください");
        }

        if (password.length() > Constants.Password.MAX_LENGTH) {
            return ValidationResult.error(Constants.ErrorMessages.PASSWORD_TOO_LONG);
        }

        return ValidationResult.success();
    }

    /**
     * URLを検証
     *
     * @param url URL
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validateUrl(@Nullable String url) {
        if (isEmpty(url)) {
            return ValidationResult.success(); // URLは任意
        }

        if (!URL_PATTERN.matcher(url).matches()) {
            return ValidationResult.error(Constants.ErrorMessages.INVALID_URL);
        }

        if (url.length() > Constants.Password.URL_MAX_LENGTH) {
            return ValidationResult.error("URLは500文字以内で入力してください");
        }

        return ValidationResult.success();
    }

    /**
     * ユーザー名を検証
     *
     * @param username ユーザー名
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validateUsername(@Nullable String username) {
        if (isEmpty(username)) {
            return ValidationResult.success(); // ユーザー名は任意
        }

        if (username.length() > Constants.Password.USERNAME_MAX_LENGTH) {
            return ValidationResult.error("ユーザー名は100文字以内で入力してください");
        }

        return ValidationResult.success();
    }

    /**
     * メモを検証
     *
     * @param notes メモ
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validateNotes(@Nullable String notes) {
        if (isEmpty(notes)) {
            return ValidationResult.success(); // メモは任意
        }

        if (notes.length() > Constants.Password.NOTES_MAX_LENGTH) {
            return ValidationResult.error("メモは1000文字以内で入力してください");
        }

        return ValidationResult.success();
    }

    /**
     * カテゴリを検証
     *
     * @param category カテゴリ
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validateCategory(@Nullable String category) {
        if (isEmpty(category)) {
            return ValidationResult.success(); // カテゴリは任意
        }

        if (category.length() > Constants.Password.CATEGORY_MAX_LENGTH) {
            return ValidationResult.error("カテゴリは50文字以内で入力してください");
        }

        return ValidationResult.success();
    }

    /**
     * メールアドレスを検証
     *
     * @param email メールアドレス
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validateEmail(@Nullable String email) {
        if (isEmpty(email)) {
            return ValidationResult.error("メールアドレスを入力してください");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.error("有効なメールアドレスを入力してください");
        }

        return ValidationResult.success();
    }

    /**
     * パスワード生成の長さを検証
     *
     * @param length 長さ
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validatePasswordGenerationLength(int length) {
        if (length < Constants.PasswordGeneration.MIN_LENGTH) {
            return ValidationResult.error("パスワードは" + Constants.PasswordGeneration.MIN_LENGTH + "文字以上にしてください");
        }

        if (length > Constants.PasswordGeneration.MAX_LENGTH) {
            return ValidationResult.error("パスワードは" + Constants.PasswordGeneration.MAX_LENGTH + "文字以下にしてください");
        }

        return ValidationResult.success();
    }

    /**
     * PINコードの長さを検証
     *
     * @param length 長さ
     * @return ValidationResult
     */
    @NonNull
    public static ValidationResult validatePinLength(int length) {
        if (length < Constants.PasswordGeneration.PIN_MIN_LENGTH) {
            return ValidationResult.error("PINコードは" + Constants.PasswordGeneration.PIN_MIN_LENGTH + "桁以上にしてください");
        }

        if (length > Constants.PasswordGeneration.PIN_MAX_LENGTH) {
            return ValidationResult.error("PINコードは" + Constants.PasswordGeneration.PIN_MAX_LENGTH + "桁以下にしてください");
        }

        return ValidationResult.success();
    }

    /**
     * パスワードエントリ全体を検証
     *
     * @param title タイトル
     * @param password パスワード
     * @param username ユーザー名
     * @param url URL
     * @param notes メモ
     * @param category カテゴリ
     * @return ValidationResult（最初のエラー、または成功）
     */
    @NonNull
    public static ValidationResult validatePasswordEntry(
            @Nullable String title,
            @Nullable String password,
            @Nullable String username,
            @Nullable String url,
            @Nullable String notes,
            @Nullable String category
    ) {
        ValidationResult result;

        result = validateTitle(title);
        if (!result.isValid()) return result;

        result = validatePassword(password);
        if (!result.isValid()) return result;

        result = validateUsername(username);
        if (!result.isValid()) return result;

        result = validateUrl(url);
        if (!result.isValid()) return result;

        result = validateNotes(notes);
        if (!result.isValid()) return result;

        result = validateCategory(category);
        if (!result.isValid()) return result;

        return ValidationResult.success();
    }

    /**
     * バリデーション結果
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        /**
         * 成功結果を作成
         *
         * @return ValidationResult
         */
        @NonNull
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        /**
         * エラー結果を作成
         *
         * @param errorMessage エラーメッセージ
         * @return ValidationResult
         */
        @NonNull
        public static ValidationResult error(@NonNull String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        /**
         * バリデーションが成功したか
         *
         * @return true: 成功, false: 失敗
         */
        public boolean isValid() {
            return valid;
        }

        /**
         * エラーメッセージを取得
         *
         * @return エラーメッセージ（成功時はnull）
         */
        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
