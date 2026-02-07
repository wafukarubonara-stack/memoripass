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

/**
 * パスワード強度チェッカー
 *
 * <p>パスワードの強度を評価する。
 * 長さ、文字種の多様性、一般的なパターンを考慮。</p>
 *
 * @since 1.0
 */
public class PasswordStrengthChecker {

    /**
     * パスワード強度レベル
     */
    public enum StrengthLevel {
        VERY_WEAK,  // 非常に弱い
        WEAK,       // 弱い
        MODERATE,   // 普通
        STRONG,     // 強い
        VERY_STRONG // 非常に強い
    }

    /**
     * パスワード強度結果
     */
    public static class StrengthResult {
        private final StrengthLevel level;
        private final int score;
        private final String message;

        public StrengthResult(StrengthLevel level, int score, String message) {
            this.level = level;
            this.score = score;
            this.message = message;
        }

        public StrengthLevel getLevel() {
            return level;
        }

        public int getScore() {
            return score;
        }

        public String getMessage() {
            return message;
        }
    }

    // 一般的な弱いパスワードのリスト（一部）
    private static final String[] COMMON_WEAK_PASSWORDS = {
            "password", "12345678", "123456789", "qwerty", "abc123",
            "111111", "password1", "12345", "1234567890", "000000"
    };

    /**
     * パスワード強度を評価
     *
     * @param password 評価するパスワード
     * @return 強度結果
     */
    @NonNull
    public static StrengthResult checkStrength(@NonNull String password) {
        if (password.isEmpty()) {
            return new StrengthResult(StrengthLevel.VERY_WEAK, 0, "パスワードを入力してください");
        }

        int score = 0;

        // 1. 長さのチェック
        int length = password.length();
        if (length < 8) {
            return new StrengthResult(StrengthLevel.VERY_WEAK, 0, "パスワードは8文字以上にしてください");
        } else if (length >= 8 && length < 12) {
            score += 10;
        } else if (length >= 12 && length < 16) {
            score += 20;
        } else if (length >= 16 && length < 20) {
            score += 30;
        } else {
            score += 40;
        }

        // 2. 文字種の多様性チェック
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSymbol = password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?].*");

        int varietyCount = 0;
        if (hasUppercase) { score += 10; varietyCount++; }
        if (hasLowercase) { score += 10; varietyCount++; }
        if (hasDigit) { score += 10; varietyCount++; }
        if (hasSymbol) { score += 20; varietyCount++; }

        // 3. 文字種の組み合わせボーナス
        if (varietyCount >= 3) score += 10;
        if (varietyCount == 4) score += 10;

        // 4. 一般的な弱いパスワードのチェック
        String lowerPassword = password.toLowerCase();
        for (String weak : COMMON_WEAK_PASSWORDS) {
            if (lowerPassword.contains(weak)) {
                score -= 30;
                break;
            }
        }

        // 5. 連続文字のチェック
        if (hasSequentialChars(password)) {
            score -= 10;
        }

        // 6. 繰り返し文字のチェック
        if (hasRepeatingChars(password)) {
            score -= 10;
        }

        // スコアを0〜100に正規化
        score = Math.max(0, Math.min(100, score));

        // 強度レベルとメッセージを決定
        return determineStrengthLevel(score);
    }

    /**
     * スコアから強度レベルを決定
     */
    private static StrengthResult determineStrengthLevel(int score) {
        if (score < 20) {
            return new StrengthResult(
                    StrengthLevel.VERY_WEAK,
                    score,
                    "非常に弱いパスワードです。より長く、複雑なパスワードを使用してください。"
            );
        } else if (score < 40) {
            return new StrengthResult(
                    StrengthLevel.WEAK,
                    score,
                    "弱いパスワードです。大文字、小文字、数字、記号を組み合わせてください。"
            );
        } else if (score < 60) {
            return new StrengthResult(
                    StrengthLevel.MODERATE,
                    score,
                    "普通の強度です。より長くすることをおすすめします。"
            );
        } else if (score < 80) {
            return new StrengthResult(
                    StrengthLevel.STRONG,
                    score,
                    "強いパスワードです。"
            );
        } else {
            return new StrengthResult(
                    StrengthLevel.VERY_STRONG,
                    score,
                    "非常に強いパスワードです！"
            );
        }
    }

    /**
     * 連続文字をチェック（例: abc, 123）
     */
    private static boolean hasSequentialChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);

            if (c2 == c1 + 1 && c3 == c2 + 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 繰り返し文字をチェック（例: aaa, 111）
     */
    private static boolean hasRepeatingChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char c = password.charAt(i);
            if (password.charAt(i + 1) == c && password.charAt(i + 2) == c) {
                return true;
            }
        }
        return false;
    }
}
