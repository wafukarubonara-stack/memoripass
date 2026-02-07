/*
 * Copyright 2026 wafukarubonara-stack
 */

package com.memoripass.ui.add;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.ui.common.BaseViewModel;

public class AddPasswordViewModel extends BaseViewModel {

    private static final String TAG = "AddPasswordViewModel";
    private final PasswordRepository repository;
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    public AddPasswordViewModel(@NonNull Application application) {
        super(application);
        try {
            this.repository = new PasswordRepository(application);
            Log.d(TAG, "AddPasswordViewModel initialized successfully");
        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to initialize AddPasswordViewModel", e);
            throw new RuntimeException("Failed to initialize ViewModel", e);
        }
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public void savePassword(
            @NonNull String title,
            String username,
            @NonNull String password,
            String url,
            String notes,
            String category
    ) {
        Log.d(TAG, "Saving password: " + title);
        setLoading();

        if (title.trim().isEmpty()) {
            setError("タイトルを入力してください");
            return;
        }
        if (password.isEmpty()) {
            setError("パスワードを入力してください");
            return;
        }

        try {
            Log.d(TAG, "Encrypting password on main thread...");
            String encryptedPassword = repository.encryptPassword(password);
            Log.d(TAG, "Encryption successful");

            String id = java.util.UUID.randomUUID().toString();
            PasswordEntry entry = new PasswordEntry(id, title.trim(), encryptedPassword);
            entry.setUsername(username);
            entry.setUrl(url);
            entry.setNotes(notes);
            entry.setCategory(category);

            new Thread(() -> {
                try {
                    repository.insert(entry);
                    Log.i(TAG, "Password saved successfully: " + title);
                    saveSuccess.postValue(true);
                    setSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "Database insert error", e);
                    setError("保存に失敗しました: " + e.getMessage());
                }
            }).start();

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Encryption error", e);
            setError("暗号化に失敗しました: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error", e);
            setError("エラーが発生しました: " + e.getMessage());
        }
    }
}
