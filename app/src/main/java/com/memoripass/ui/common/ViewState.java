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

import androidx.annotation.Nullable;

/**
 * ビューステート
 *
 * <p>UIの状態を表現するクラス。
 * ViewModel から Fragment/Activity に状態を伝達する。</p>
 *
 * <p>状態の種類:</p>
 * <ul>
 *   <li>LOADING: データ読み込み中</li>
 *   <li>SUCCESS: データ読み込み成功</li>
 *   <li>EMPTY: データが空</li>
 *   <li>ERROR: エラー発生</li>
 * </ul>
 *
 * @since 1.0
 */
public class ViewState {

    /**
     * 状態の種類
     */
    public enum State {
        LOADING,
        SUCCESS,
        EMPTY,
        ERROR
    }

    private final State state;
    private final String message;

    /**
     * プライベートコンストラクタ
     */
    private ViewState(State state, String message) {
        this.state = state;
        this.message = message;
    }

    // 定数インスタンス

    /**
     * ローディング状態
     */
    public static final ViewState LOADING = new ViewState(State.LOADING, null);

    /**
     * 成功状態
     */
    public static final ViewState SUCCESS = new ViewState(State.SUCCESS, null);

    /**
     * 空状態
     */
    public static final ViewState EMPTY = new ViewState(State.EMPTY, null);

    /**
     * エラー状態を生成
     *
     * @param message エラーメッセージ
     * @return エラー状態のViewState
     */
    public static ViewState error(String message) {
        return new ViewState(State.ERROR, message);
    }

    // Getters

    /**
     * 状態を取得
     *
     * @return 状態
     */
    public State getState() {
        return state;
    }

    /**
     * メッセージを取得
     *
     * @return メッセージ（エラー時のみ）
     */
    @Nullable
    public String getMessage() {
        return message;
    }

    /**
     * ローディング状態かチェック
     *
     * @return true: ローディング中
     */
    public boolean isLoading() {
        return state == State.LOADING;
    }

    /**
     * 成功状態かチェック
     *
     * @return true: 成功
     */
    public boolean isSuccess() {
        return state == State.SUCCESS;
    }

    /**
     * 空状態かチェック
     *
     * @return true: 空
     */
    public boolean isEmpty() {
        return state == State.EMPTY;
    }

    /**
     * エラー状態かチェック
     *
     * @return true: エラー
     */
    public boolean isError() {
        return state == State.ERROR;
    }

    @Override
    public String toString() {
        return "ViewState{" +
                "state=" + state +
                ", message='" + message + '\'' +
                '}';
    }
}
