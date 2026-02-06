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
 * <p>すべてのViewModelの共通機能を提供する。</p>
 *
 * <p>共通機能:</p>
 * <ul>
 *   <li>ViewStateの管理</li>
 *   <li>エラーハンドリング</li>
 *   <li>ローディング状態の管理</li>
 * </ul>
 *
 * @since 1.0
 */
public abstract class BaseViewModel extends AndroidViewModel {

    private static final String TAG = "BaseViewModel";

    protected final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    /**
     * コンストラクタ
     *
     * @param application アプリケーション
     */
    public BaseViewModel(@NonNull Application application) {
        super(application);
        viewState.setValue(ViewState.LOADING);
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
        viewState.setValue(ViewState.LOADING);
    }

    /**
     * 成功状態に設定
     */
    protected void setSuccess() {
        viewState.setValue(ViewState.SUCCESS);
    }

    /**
     * 空状態に設定
     */
    protected void setEmpty() {
        viewState.setValue(ViewState.EMPTY);
    }

    /**
     * エラー状態に設定
     *
     * @param message エラーメッセージ
     */
    protected void setError(String message) {
        viewState.setValue(ViewState.error(message));
    }

    /**
     * エラーをハンドリング
     *
     * @param throwable エラー
     */
    protected void handleError(@NonNull Throwable throwable) {
        Log.e(TAG, "Error occurred", throwable);
        String message = throwable.getMessage();
        if (message == null || message.isEmpty()) {
            message = "エラーが発生しました";
        }
        setError(message);
    }

    /**
     * 例外をハンドリング
     *
     * @param exception 例外
     */
    protected void handleException(@NonNull Exception exception) {
        Log.e(TAG, "Exception occurred", exception);
        String message = exception.getMessage();
        if (message == null || message.isEmpty()) {
            message = "予期しないエラーが発生しました";
        }
        setError(message);
    }
}
