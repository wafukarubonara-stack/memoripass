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

package com.memoripass.ui.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.memoripass.R;
import com.memoripass.domain.model.Password;
import com.memoripass.ui.common.BaseFragment;
import com.memoripass.ui.edit.EditPasswordFragment;

/**
 * パスワード詳細Fragment
 *
 * <p>パスワードの詳細を表示する画面。</p>
 *
 * @since 1.0
 */
public class PasswordDetailFragment extends BaseFragment {

    private static final String TAG = "PasswordDetailFragment";
    private static final String ARG_PASSWORD_ID = "password_id";

    private PasswordDetailViewModel viewModel;
    private String passwordId;

    private TextView titleTextView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView urlTextView;
    private TextView categoryTextView;
    private TextView notesTextView;

    /**
     * 新しいインスタンスを作成
     */
    public static PasswordDetailFragment newInstance(@NonNull String passwordId) {
        PasswordDetailFragment fragment = new PasswordDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PASSWORD_ID, passwordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            passwordId = getArguments().getString(ARG_PASSWORD_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // XMLレイアウトをインフレート
        View view = inflater.inflate(R.layout.fragment_password_detail, container, false);

        // ビューの取得
        titleTextView = view.findViewById(R.id.text_title);
        usernameTextView = view.findViewById(R.id.text_username);
        passwordTextView = view.findViewById(R.id.text_password);
        urlTextView = view.findViewById(R.id.text_url);
        categoryTextView = view.findViewById(R.id.text_category);
        notesTextView = view.findViewById(R.id.text_notes);

        // ボタンリスナー
        view.findViewById(R.id.btn_copy_password).setOnClickListener(v -> copyPasswordToClipboard());
        view.findViewById(R.id.btn_edit).setOnClickListener(v ->
                navigateTo(EditPasswordFragment.newInstance(passwordId)));
        view.findViewById(R.id.btn_back).setOnClickListener(v -> navigateBack());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }

    /**
     * ViewModelのセットアップ
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PasswordDetailViewModel.class);

        // ViewStateを監視
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::handleViewState);

        // パスワードを読み込み
        viewModel.loadPassword(passwordId);

        // パスワードを監視
        viewModel.getPassword().observe(getViewLifecycleOwner(), this::displayPassword);
    }

    /**
     * パスワードを表示
     */
    private void displayPassword(Password password) {
        if (password == null) {
            showError("パスワードが見つかりません");
            return;
        }

        titleTextView.setText(password.getTitle());
        usernameTextView.setText(password.getUsername() != null ? password.getUsername() : "未設定");
        passwordTextView.setText("••••••••");
        passwordTextView.setTag(password.getPassword());
        urlTextView.setText(password.getUrl() != null ? password.getUrl() : "未設定");
        categoryTextView.setText(password.getCategory() != null ? password.getCategory() : "未設定");
        notesTextView.setText(password.getNotes() != null ? password.getNotes() : "なし");
    }

    /**
     * パスワードをクリップボードにコピー
     */
    private void copyPasswordToClipboard() {
        String password = (String) passwordTextView.getTag();
        if (password == null) {
            showError("パスワードが読み込まれていません");
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) requireContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("password", password);
        clipboard.setPrimaryClip(clip);
        // 30秒後にクリップボードを自動クリア
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            ClipboardManager cm = (ClipboardManager) requireContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("", ""));
        }, 30_000);

        showMessage("パスワードをコピーしました");
    }
}
