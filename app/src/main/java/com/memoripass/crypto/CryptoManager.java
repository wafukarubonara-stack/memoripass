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

package com.memoripass.crypto;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * 暗号化管理クラス
 *
 * <p>AES-256-GCMを使用してデータを暗号化/復号する。
 * NIST SP 800-38D準拠の実装。</p>
 *
 * <p>暗号化仕様:</p>
 * <ul>
 *   <li>アルゴリズム: AES-256-GCM</li>
 *   <li>鍵サイズ: 256ビット</li>
 *   <li>IVサイズ: 96ビット（12バイト）</li>
 *   <li>認証タグ: 128ビット（16バイト）</li>
 * </ul>
 *
 * <p>セキュリティ機能:</p>
 * <ul>
 *   <li>認証付き暗号化（改ざん検出）</li>
 *   <li>ランダムIV生成（暗号文の非決定性）</li>
 *   <li>NIST準拠の実装</li>
 * </ul>
 *
 * @since 1.0
 */
public class CryptoManager {

    private static final String TAG = "CryptoManager";

    // 暗号化アルゴリズム
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    // IV（Initialization Vector）のサイズ（バイト）
    // NIST SP 800-38D推奨: 96ビット（12バイト）
    private static final int IV_SIZE_BYTES = 12;

    // 認証タグのサイズ（ビット）
    // NIST推奨: 128ビット
    private static final int AUTH_TAG_SIZE_BITS = 128;

    private final KeyManager keyManager;
    private final SecureRandom secureRandom;

    /**
     * コンストラクタ
     *
     * @throws CryptoException 初期化に失敗
     */
    public CryptoManager() throws CryptoException {
        try {
            this.keyManager = new KeyManager();
            this.secureRandom = new SecureRandom();

            // マスター鍵が存在しない場合は生成
            if (!keyManager.hasMasterKey()) {
                Log.i(TAG, "Master key not found, generating new key");
                keyManager.generateMasterKey();
            }

            Log.d(TAG, "CryptoManager initialized successfully");
        } catch (KeyStoreException e) {
            Log.e(TAG, "Failed to initialize CryptoManager", e);
            throw new CryptoException("Failed to initialize CryptoManager", e);
        }
    }

    /**
     * データを暗号化
     *
     * <p>暗号化データのフォーマット:</p>
     * <pre>
     * [IV (12 bytes)] [暗号文 + 認証タグ (n + 16 bytes)]
     * </pre>
     *
     * @param plaintext 平文
     * @return Base64エンコードされた暗号化データ
     * @throws CryptoException 暗号化に失敗
     */
    @NonNull
    public String encrypt(@NonNull String plaintext) throws CryptoException {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new CryptoException("Plaintext cannot be null or empty");
        }

        try {
            // マスター鍵を取得
            SecretKey masterKey = keyManager.getMasterKey();

            // ランダムIVを生成
            byte[] iv = new byte[IV_SIZE_BYTES];
            secureRandom.nextBytes(iv);

            // Cipherを初期化
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(AUTH_TAG_SIZE_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, masterKey, spec);

            // 暗号化
            byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(plaintextBytes);

            // IV + 暗号文を結合
            ByteBuffer buffer = ByteBuffer.allocate(IV_SIZE_BYTES + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);

            // Base64エンコード
            String encoded = Base64.encodeToString(buffer.array(), Base64.NO_WRAP);

            Log.d(TAG, "Encryption successful");
            Log.d(TAG, "IV size: " + iv.length + " bytes");
            Log.d(TAG, "Ciphertext size: " + ciphertext.length + " bytes");

            // センシティブデータをクリア
            Arrays.fill(plaintextBytes, (byte) 0);

            return encoded;

        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException e) {
            Log.e(TAG, "Encryption failed", e);
            throw new CryptoException("Encryption failed", e);
        }
    }

    /**
     * データを復号
     *
     * @param encryptedData Base64エンコードされた暗号化データ
     * @return 平文
     * @throws CryptoException 復号に失敗
     */
    @NonNull
    public String decrypt(@NonNull String encryptedData) throws CryptoException {
        if (encryptedData == null || encryptedData.isEmpty()) {
            throw new CryptoException("Encrypted data cannot be null or empty");
        }

        try {
            // Base64デコード
            byte[] decodedData = Base64.decode(encryptedData, Base64.NO_WRAP);

            if (decodedData.length < IV_SIZE_BYTES) {
                throw new CryptoException("Invalid encrypted data format");
            }

            // IVと暗号文を分離
            ByteBuffer buffer = ByteBuffer.wrap(decodedData);
            byte[] iv = new byte[IV_SIZE_BYTES];
            buffer.get(iv);

            byte[] ciphertext = new byte[buffer.remaining()];
            buffer.get(ciphertext);

            // マスター鍵を取得
            SecretKey masterKey = keyManager.getMasterKey();

            // Cipherを初期化
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(AUTH_TAG_SIZE_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, masterKey, spec);

            // 復号
            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);

            Log.d(TAG, "Decryption successful");

            // センシティブデータをクリア
            Arrays.fill(plaintextBytes, (byte) 0);
            Arrays.fill(iv, (byte) 0);
            Arrays.fill(ciphertext, (byte) 0);

            return plaintext;

        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException e) {
            Log.e(TAG, "Decryption failed", e);
            throw new CryptoException("Decryption failed", e);
        }
    }

    /**
     * 暗号化が利用可能かチェック
     *
     * @return true: 利用可能, false: 利用不可
     */
    public boolean isEncryptionAvailable() {
        return keyManager.hasMasterKey();
    }

    /**
     * マスター鍵を再生成
     *
     * <p>警告: すべての暗号化データが復号不可能になります。</p>
     *
     * @throws CryptoException 鍵の再生成に失敗
     */
    public void regenerateMasterKey() throws CryptoException {
        try {
            Log.w(TAG, "Regenerating master key - all encrypted data will be lost!");
            keyManager.deleteMasterKey();
            keyManager.generateMasterKey();
            Log.i(TAG, "Master key regenerated successfully");
        } catch (KeyStoreException e) {
            Log.e(TAG, "Failed to regenerate master key", e);
            throw new CryptoException("Failed to regenerate master key", e);
        }
    }

    /**
     * 暗号化情報を出力（デバッグ用）
     */
    public void printCryptoInfo() {
        Log.d(TAG, "=== Crypto Information ===");
        Log.d(TAG, "Transformation: " + TRANSFORMATION);
        Log.d(TAG, "IV size: " + IV_SIZE_BYTES + " bytes");
        Log.d(TAG, "Auth tag size: " + AUTH_TAG_SIZE_BITS + " bits");
        Log.d(TAG, "Master key exists: " + keyManager.hasMasterKey());
        Log.d(TAG, "StrongBox available: " + keyManager.isStrongBoxAvailable());
        Log.d(TAG, "=========================");

        keyManager.printKeyStoreInfo();
    }

    /**
     * 暗号化例外クラス
     */
    public static class CryptoException extends Exception {
        public CryptoException(String message) {
            super(message);
        }

        public CryptoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}