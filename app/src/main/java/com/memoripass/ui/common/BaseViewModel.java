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

package com.memoripass.ui.common;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel基底クラス
 *
 * <p>ViewStateの管理とエラーハンドリングの共通機能を提供する。</p>
 *
 * @since 1.0
 */
public class BaseViewModel extends AndroidViewModel {

    private static final String TAG = "BaseViewModel";

    protected final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        viewState.setValue(ViewState.SUCCESS);
    }

    /**
     * ViewStateを取得
     *
     * @return ViewStateのLiveData
     */
    public LiveData<ViewState> getViewState() {
        return viewState;
    }

    /**
     * ローディング状態に設定
     */
    protected void setLoading() {
        viewState.postValue(ViewState.LOADING);  // postValue使用（スレッドセーフ）
    }

    /**
     * 成功状態に設定
     */
    protected void setSuccess() {
        viewState.postValue(ViewState.SUCCESS);  // postValue使用（スレッドセーフ）
    }

    /**
     * 空状態に設定
     */
    protected void setEmpty() {
        viewState.postValue(ViewState.EMPTY);  // postValue使用（スレッドセーフ）
    }

    /**
     * エラー状態に設定
     *
     * @param message エラーメッセージ
     */
    protected void setError(@NonNull String message) {
        viewState.postValue(ViewState.error(message));  // postValue使用（スレッドセーフ）
        Log.e(TAG, "Error: " + message);
    }

    /**
     * エラーハンドリング
     *
     * @param throwable エラー
     */
    protected void handleError(@NonNull Throwable throwable) {
        setError(throwable.getMessage() != null ? throwable.getMessage() : "Unknown error");
    }

    /**
     * 例外ハンドリング
     *
     * @param exception 例外
     */
    protected void handleException(@NonNull Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isEmpty()) {
            message = "An unexpected error occurred";
        }
        setError(message);
        Log.e(TAG, "Exception handled", exception);
    }
}
