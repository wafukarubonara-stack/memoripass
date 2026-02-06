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

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Fragment基底クラス
 *
 * <p>すべてのFragmentの共通機能を提供する。</p>
 *
 * <p>共通機能:</p>
 * <ul>
 *   <li>ローディング表示</li>
 *   <li>エラー表示</li>
 *   <li>トースト表示</li>
 *   <li>画面遷移</li>
 * </ul>
 *
 * @since 1.0
 */
public abstract class BaseFragment extends Fragment {

    /**
     * ローディングを表示
     *
     * <p>サブクラスでオーバーライドして
     * プログレスバーなどを表示する。</p>
     */
    protected void showLoading() {
        // デフォルト実装なし
        // サブクラスで実装
    }

    /**
     * ローディングを非表示
     */
    protected void hideLoading() {
        // デフォルト実装なし
        // サブクラスで実装
    }

    /**
     * エラーメッセージを表示
     *
     * @param message エラーメッセージ
     */
    protected void showError(@NonNull String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 成功メッセージを表示
     *
     * @param message メッセージ
     */
    protected void showMessage(@NonNull String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 画面遷移
     *
     * @param fragment 遷移先Fragment
     */
    protected void navigateTo(@NonNull Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * 前の画面に戻る
     */
    protected void navigateBack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * ViewStateの変更を処理
     *
     * @param viewState ViewState
     */
    protected void handleViewState(@NonNull ViewState viewState) {
        switch (viewState.getState()) {
            case LOADING:
                showLoading();
                break;

            case SUCCESS:
                hideLoading();
                break;

            case EMPTY:
                hideLoading();
                showMessage("データがありません");
                break;

            case ERROR:
                hideLoading();
                if (viewState.getMessage() != null) {
                    showError(viewState.getMessage());
                }
                break;
        }
    }
}
