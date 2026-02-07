/*
 * Copyright 2026 wafukarubonara-stack
 */

package com.memoripass.ui.edit;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.memoripass.crypto.CryptoManager;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.data.repository.PasswordRepository;
import com.memoripass.domain.model.Password;
import com.memoripass.domain.usecase.GetPasswordUseCase;
import com.memoripass.ui.common.BaseViewModel;

public class EditPasswordViewModel extends BaseViewModel {

    private static final String TAG = "EditPasswordViewModel";

    private final PasswordRepository repository;
    private final GetPasswordUseCase getPasswordUseCase;
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private LiveData<Password> password;

    public EditPasswordViewModel(@NonNull Application application) {
        super(application);

        try {
            this.repository = new PasswordRepository(application);
            this.getPasswordUseCase = new GetPasswordUseCase(repository);
            Log.d(TAG, "EditPasswordViewModel initialized successfully");

        } catch (CryptoManager.CryptoException e) {
            Log.e(TAG, "Failed to initialize EditPasswordViewModel", e);
            throw new RuntimeException("Failed to initialize ViewModel", e);
        }
    }

    public void loadPassword(@NonNull String passwordId) {
        Log.d(TAG, "Loading password: " + passwordId);
        setLoading();

        try {
            password = getPasswordUseCase.execute(passwordId);
            setSuccess();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load password", e);
            setError("パスワードの読み込みに失敗しました");
        }
    }

    public LiveData<Password> getPassword() {
        return password;
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public void updatePassword(
            @NonNull String id,
            @NonNull String title,
            String username,
            @NonNull String password,
            String url,
            String notes,
            String category,
            long createdAt
    ) {
        Log.d(TAG, "Updating password: " + title);
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

            PasswordEntry entry = new PasswordEntry(id, title.trim(), encryptedPassword);
            entry.setUsername(username);
            entry.setUrl(url);
            entry.setNotes(notes);
            entry.setCategory(category);
            entry.setCreatedAt(createdAt);
            entry.setUpdatedAt(System.currentTimeMillis());

            new Thread(() -> {
                try {
                    repository.update(entry);
                    Log.i(TAG, "Password updated successfully: " + title);
                    updateSuccess.postValue(true);
                    setSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "Database update error", e);
                    setError("更新に失敗しました: " + e.getMessage());
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
