package com.memoripass.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.memoripass.data.local.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 操作ログ記録クラス（監査ログ）
 *
 * <p>パスワードの追加・変更・削除・閲覧などの操作を記録する。
 * 否認防止（Non-repudiation）を実現し、不正操作の追跡を可能にする。</p>
 *
 * <p>記録する操作:</p>
 * <ul>
 *   <li>認証成功・失敗</li>
 *   <li>パスワード追加・変更・削除・閲覧</li>
 *   <li>root化検知</li>
 *   <li>クリップボードコピー</li>
 * </ul>
 *
 * @since 1.0.1
 */
public class AuditLogger {

    private static final String TAG = "AuditLogger";
    private static final int MAX_LOG_ENTRIES = 1000;

    private final Context context;
    private final SimpleDateFormat dateFormat;

    /**
     * 操作種別
     */
    public enum Action {
        // 認証
        AUTH_SUCCESS("認証成功"),
        AUTH_FAILED("認証失敗"),
        AUTH_LOCKED("認証ロック"),
        AUTO_LOCKED("自動ロック"),

        // パスワード操作
        PASSWORD_ADDED("パスワード追加"),
        PASSWORD_VIEWED("パスワード閲覧"),
        PASSWORD_COPIED("パスワードコピー"),
        PASSWORD_UPDATED("パスワード更新"),
        PASSWORD_DELETED("パスワード削除"),

        // セキュリティイベント
        ROOT_DETECTED("root化検知"),
        DEBUGGER_DETECTED("デバッガー検知"),
        APP_LAUNCHED("アプリ起動"),
        APP_BACKGROUNDED("バックグラウンド移行");

        private final String displayName;

        Action(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public AuditLogger(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
    }

    /**
     * 操作を記録
     *
     * @param action 操作種別
     * @param detail 詳細情報（機密情報は含めない）
     */
    public void log(@NonNull Action action, String detail) {
        String timestamp = dateFormat.format(new Date());
        String logEntry = String.format("[%s] %s: %s",
                timestamp, action.getDisplayName(),
                detail != null ? detail : "");

        // Logcatへの出力（ProGuardでリリース時は除去）
        Log.i(TAG, logEntry);

        // SharedPreferencesへの保存（ローカル監査ログ）
        saveToPreferences(action, detail, timestamp);
    }

    /**
     * 認証イベントを記録
     */
    public void logAuth(@NonNull Action action) {
        log(action, null);
    }

    /**
     * パスワード操作を記録（タイトルのみ、パスワード本体は記録しない）
     */
    public void logPasswordAction(@NonNull Action action, @NonNull String title) {
        log(action, "title=" + title);
    }

    /**
     * セキュリティイベントを記録
     */
    public void logSecurityEvent(@NonNull Action action, String detail) {
        log(action, detail);
    }

    /**
     * SharedPreferencesに保存
     */
    private void saveToPreferences(Action action, String detail, String timestamp) {
        try {
            SecurePreferences prefs = new SecurePreferences(context);
            int count = prefs.getInt("audit_log_count", 0);

            // 上限を超えたら古いログを削除
            if (count >= MAX_LOG_ENTRIES) {
                count = 0;
                Log.i(TAG, "Audit log rotated");
            }

            String key = "audit_log_" + count;
            String value = timestamp + "|" + action.name() + "|"
                    + (detail != null ? detail : "");
            prefs.putString(key, value);
            prefs.putInt("audit_log_count", count + 1);
            prefs.putString("audit_log_last", timestamp);

        } catch (Exception e) {
            Log.e(TAG, "Failed to save audit log", e);
        }
    }

    /**
     * 最終ログ記録日時を取得
     */
    public String getLastLogTime() {
        try {
            SecurePreferences prefs = new SecurePreferences(context);
            return prefs.getString("audit_log_last", "なし");
        } catch (Exception e) {
            return "取得失敗";
        }
    }
}
