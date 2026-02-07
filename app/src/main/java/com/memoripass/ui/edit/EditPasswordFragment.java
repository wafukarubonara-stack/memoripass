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

package com.memoripass.ui.edit;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.memoripass.domain.model.Password;
import com.memoripass.ui.common.BaseFragment;

/**
 * パスワード編集Fragment
 *
 * <p>既存のパスワードを編集する画面。</p>
 *
 * @since 1.0
 */
public class EditPasswordFragment extends BaseFragment {

    private static final String TAG = "EditPasswordFragment";
    private static final String ARG_PASSWORD_ID = "password_id";

    private EditPasswordViewModel viewModel;
    private String passwordId;
    private Password currentPassword;

    private EditText titleEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText urlEditText;
    private EditText notesEditText;
    private EditText categoryEditText;
    private Button saveButton;
    private Button cancelButton;

    /**
     * 新しいインスタンスを作成
     *
     * @param passwordId パスワードID
     * @return EditPasswordFragment
     */
    public static EditPasswordFragment newInstance(@NonNull String passwordId) {
        EditPasswordFragment fragment = new EditPasswordFragment();
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
        return createLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupListeners();
    }

    /**
     * レイアウトを作成
     */
    private View createLayout() {
        LinearLayout rootLayout = new LinearLayout(requireContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(48, 48, 48, 48);

        // ScrollView
        ScrollView scrollView = new ScrollView(requireContext());
        LinearLayout formLayout = new LinearLayout(requireContext());
        formLayout.setOrientation(LinearLayout.VERTICAL);

        // タイトル
        titleEditText = createEditText("タイトル *", InputType.TYPE_CLASS_TEXT);
        formLayout.addView(titleEditText);

        // ユーザー名
        usernameEditText = createEditText("ユーザー名", InputType.TYPE_CLASS_TEXT);
        formLayout.addView(usernameEditText);

        // パスワード
        passwordEditText = createEditText("パスワード *", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        formLayout.addView(passwordEditText);

        // URL
        urlEditText = createEditText("URL", InputType.TYPE_TEXT_VARIATION_URI);
        formLayout.addView(urlEditText);

        // カテゴリ
        categoryEditText = createEditText("カテゴリ", InputType.TYPE_CLASS_TEXT);
        formLayout.addView(categoryEditText);

        // メモ
        notesEditText = createEditText("メモ", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        notesEditText.setMinLines(3);
        formLayout.addView(notesEditText);

        scrollView.addView(formLayout);
        rootLayout.addView(scrollView);

        // ボタンレイアウト
        LinearLayout buttonLayout = new LinearLayout(requireContext());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setPadding(0, 32, 0, 0);

        // 保存ボタン
        saveButton = new Button(requireContext());
        saveButton.setText("保存");
        LinearLayout.LayoutParams saveParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        saveParams.setMargins(0, 0, 16, 0);
        saveButton.setLayoutParams(saveParams);
        buttonLayout.addView(saveButton);

        // キャンセルボタン
        cancelButton = new Button(requireContext());
        cancelButton.setText("キャンセル");
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        cancelButton.setLayoutParams(cancelParams);
        buttonLayout.addView(cancelButton);

        rootLayout.addView(buttonLayout);

        return rootLayout;
    }

    /**
     * EditTextを作成
     */
    private EditText createEditText(String hint, int inputType) {
        EditText editText = new EditText(requireContext());
        editText.setHint(hint);
        editText.setInputType(inputType);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        editText.setLayoutParams(params);
        return editText;
    }

    /**
     * ViewModelのセットアップ
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(EditPasswordViewModel.class);

        // ViewStateを監視
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::handleViewState);

        // 更新成功を監視
        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                showMessage("パスワードを更新しました");
                navigateBack();
            }
        });

        // パスワードを読み込み
        viewModel.loadPassword(passwordId);

        // パスワードを監視
        viewModel.getPassword().observe(getViewLifecycleOwner(), this::displayPassword);
    }

    /**
     * リスナーのセットアップ
     */
    private void setupListeners() {
        // 保存ボタン
        saveButton.setOnClickListener(v -> {
            if (currentPassword == null) {
                showError("パスワードが読み込まれていません");
                return;
            }

            String title = titleEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String url = urlEditText.getText().toString();
            String notes = notesEditText.getText().toString();
            String category = categoryEditText.getText().toString();

            viewModel.updatePassword(
                    currentPassword.getId(),
                    title,
                    username,
                    password,
                    url,
                    notes,
                    category,
                    currentPassword.getCreatedAt()
            );
        });

        // キャンセルボタン
        cancelButton.setOnClickListener(v -> navigateBack());
    }

    /**
     * パスワードを表示
     *
     * @param password パスワード
     */
    private void displayPassword(Password password) {
        if (password == null) {
            showError("パスワードが見つかりません");
            return;
        }

        currentPassword = password;

        titleEditText.setText(password.getTitle());
        usernameEditText.setText(password.getUsername() != null ? password.getUsername() : "");
        passwordEditText.setText(password.getPassword());
        urlEditText.setText(password.getUrl() != null ? password.getUrl() : "");
        categoryEditText.setText(password.getCategory() != null ? password.getCategory() : "");
        notesEditText.setText(password.getNotes() != null ? password.getNotes() : "");
    }
}
