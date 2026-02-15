# Memoripass ProGuard Rules
# Generated for v1.0 release

# ============================================================
# 基本設定
# ============================================================

# デバッグ用にスタックトレースの行番号を保持
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# アノテーションを保持
-keepattributes *Annotation*

# ============================================================
# Android基本クラス
# ============================================================

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends androidx.fragment.app.Fragment

# ============================================================
# Room Database
# ============================================================

# Roomエンティティを保持
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.paging.**

# Room生成クラスを保持
-keep class **_Impl { *; }
-keep class **_Impl$* { *; }

# ============================================================
# Biometric Authentication
# ============================================================

-keep class androidx.biometric.** { *; }
-dontwarn androidx.biometric.**

# ============================================================
# Security / Crypto
# ============================================================

-keep class androidx.security.crypto.** { *; }
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }
-dontwarn androidx.security.crypto.**

# ============================================================
# ViewModel / LiveData
# ============================================================

-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# ============================================================
# Material Design Components
# ============================================================

-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# ============================================================
# Memoripassアプリ固有
# ============================================================

# データモデルを保持（暗号化/復号化で使用）
-keep class com.memoripass.data.model.** { *; }
-keep class com.memoripass.domain.model.** { *; }

# セキュリティクラスを保持
-keep class com.memoripass.infrastructure.security.** { *; }

# ViewModelを保持
-keep class com.memoripass.ui.**.** extends androidx.lifecycle.ViewModel { *; }

# ============================================================
# 警告を無視（既知の問題）
# ============================================================

-dontwarn kotlin.**
-dontwarn kotlinx.**
-dontwarn org.jetbrains.**


# ============================================================
# 自動生成された警告抑制ルール
# ============================================================

-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.concurrent.GuardedBy
# ============================================================
# ログ除去（リリースビルド用）
# ============================================================
# Log.d, Log.v, Log.i をリリースビルドから除去
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
