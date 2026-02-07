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

import java.security.SecureRandom;

/**
 * パスワード生成ユーティリティ
 *
 * <p>セキュアなランダムパスワードを生成する。
 * SecureRandomを使用して、暗号学的に安全な乱数を生成。</p>
 *
 * <p>使用例:</p>
 * <pre>
 * String password = PasswordGenerator.generate(16, true, true, true, true);
 * </pre>
 *
 * @since 1.0
 */
public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final SecureRandom random = new SecureRandom();

    /**
     * パスワードを生成
     *
     * @param length パスワードの長さ（8〜128文字）
     * @param useUppercase 大文字を含む
     * @param useLowercase 小文字を含む
     * @param useDigits 数字を含む
     * @param useSymbols 記号を含む
     * @return 生成されたパスワード
     * @throws IllegalArgumentException 無効なパラメータ
     */
    @NonNull
    public static String generate(
            int length,
            boolean useUppercase,
            boolean useLowercase,
            boolean useDigits,
            boolean useSymbols
    ) {
        // バリデーション
        if (length < 8 || length > 128) {
            throw new IllegalArgumentException("パスワードの長さは8〜128文字にしてください");
        }

        if (!useUppercase && !useLowercase && !useDigits && !useSymbols) {
            throw new IllegalArgumentException("少なくとも1つの文字種を選択してください");
        }

        // 使用する文字セットを構築
        StringBuilder characterSet = new StringBuilder();
        if (useUppercase) characterSet.append(UPPERCASE);
        if (useLowercase) characterSet.append(LOWERCASE);
        if (useDigits) characterSet.append(DIGITS);
        if (useSymbols) characterSet.append(SYMBOLS);

        // パスワードを生成
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(index));
        }

        // 各文字種が少なくとも1つ含まれているか確認
        String generated = password.toString();
        if (!isValidPassword(generated, useUppercase, useLowercase, useDigits, useSymbols)) {
            // 再生成（稀に全ての文字種が含まれない場合）
            return generate(length, useUppercase, useLowercase, useDigits, useSymbols);
        }

        return generated;
    }

    /**
     * デフォルト設定でパスワードを生成
     * （16文字、すべての文字種を含む）
     *
     * @return 生成されたパスワード
     */
    @NonNull
    public static String generateDefault() {
        return generate(16, true, true, true, true);
    }

    /**
     * 強力なパスワードを生成
     * （24文字、すべての文字種を含む）
     *
     * @return 生成されたパスワード
     */
    @NonNull
    public static String generateStrong() {
        return generate(24, true, true, true, true);
    }

    /**
     * 簡単なパスワードを生成
     * （12文字、記号なし）
     *
     * @return 生成されたパスワード
     */
    @NonNull
    public static String generateSimple() {
        return generate(12, true, true, true, false);
    }

    /**
     * PINコードを生成
     *
     * @param length 長さ（4〜8桁）
     * @return 生成されたPINコード
     */
    @NonNull
    public static String generatePin(int length) {
        if (length < 4 || length > 8) {
            throw new IllegalArgumentException("PINコードは4〜8桁にしてください");
        }
        return generate(length, false, false, true, false);
    }

    /**
     * パスワードが有効か検証
     *
     * @param password パスワード
     * @param requireUppercase 大文字必須
     * @param requireLowercase 小文字必須
     * @param requireDigits 数字必須
     * @param requireSymbols 記号必須
     * @return true: 有効
     */
    private static boolean isValidPassword(
            String password,
            boolean requireUppercase,
            boolean requireLowercase,
            boolean requireDigits,
            boolean requireSymbols
    ) {
        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            return false;
        }
        if (requireLowercase && !password.matches(".*[a-z].*")) {
            return false;
        }
        if (requireDigits && !password.matches(".*[0-9].*")) {
            return false;
        }
        if (requireSymbols && !password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?].*")) {
            return false;
        }
        return true;
    }
}
