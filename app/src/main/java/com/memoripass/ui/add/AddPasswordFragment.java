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

package com.memoripass.ui.add;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.memoripass.R;
import com.memoripass.ui.common.BaseFragment;
import com.memoripass.util.PasswordGenerator;
import com.memoripass.util.PasswordStrengthChecker;

/**
 * パスワード追加Fragment
 *
 * <p>新しいパスワードを追加する画面。</p>
 * <p>パスワード自動生成・強度チェック機能付き。</p>
 *
 * @since 1.0
 */
public class AddPasswordFragment extends BaseFragment {

    private static final String TAG = "AddPasswordFragment";

    private AddPasswordViewModel viewModel;
    private TextInputEditText titleEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText urlEditText;
    private TextInputEditText notesEditText;
    private TextInputEditText categoryEditText;
    private ProgressBar strengthBar;
    private TextView strengthText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_password, container, false);

        // ビューの取得
        titleEditText    = view.findViewById(R.id.edit_title);
        usernameEditText = view.findViewById(R.id.edit_username);
        passwordEditText = view.findViewById(R.id.edit_password);
        urlEditText      = view.findViewById(R.id.edit_url);
        notesEditText    = view.findViewById(R.id.edit_notes);
        categoryEditText = view.findViewById(R.id.edit_category);
        strengthBar      = view.findViewById(R.id.strength_bar);
        strengthText     = view.findViewById(R.id.strength_text);

        // ボタンリスナー
        view.findViewById(R.id.btn_save).setOnClickListener(v -> savePassword());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> navigateBack());
        view.findViewById(R.id.btn_generate).setOnClickListener(v -> generatePassword());

        // パスワード入力時に強度を更新
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateStrengthIndicator(s.toString());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }

    /**
     * パスワードを自動生成
     */
    private void generatePassword() {
        String generated = PasswordGenerator.generateDefault();
        passwordEditText.setText(generated);
        showMessage("パスワードを生成しました");
    }

    /**
     * パスワード強度インジケーターを更新
     */
    private void updateStrengthIndicator(String password) {
        if (password.isEmpty()) {
            strengthBar.setProgress(0);
            strengthText.setText("強度: -");
            return;
        }

        PasswordStrengthChecker.StrengthResult result =
                PasswordStrengthChecker.checkStrength(password);

        int progress = result.getScore();
        String label = result.getMessage();
        int color;

        switch (result.getLevel()) {
            case VERY_WEAK:
                color = getResources().getColor(R.color.strength_very_weak, null);
                break;
            case WEAK:
                color = getResources().getColor(R.color.strength_weak, null);
                break;
            case MODERATE:
                color = getResources().getColor(R.color.strength_moderate, null);
                break;
            case STRONG:
                color = getResources().getColor(R.color.strength_strong, null);
                break;
            case VERY_STRONG:
                color = getResources().getColor(R.color.strength_very_strong, null);
                break;
            default:
                color = getResources().getColor(android.R.color.darker_gray, null);
        }

        strengthBar.setProgress(progress);
        strengthBar.setProgressTintList(
                android.content.res.ColorStateList.valueOf(color));
        strengthText.setText(label);
        strengthText.setTextColor(color);
    }

    /**
     * ViewModelのセットアップ
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AddPasswordViewModel.class);

        viewModel.getViewState().observe(getViewLifecycleOwner(), this::handleViewState);

        viewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                showMessage("パスワードを保存しました");
                navigateBack();
            }
        });
    }

    /**
     * パスワードを保存
     */
    private void savePassword() {
        String title    = titleEditText.getText()    != null ? titleEditText.getText().toString()    : "";
        String username = usernameEditText.getText() != null ? usernameEditText.getText().toString() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString() : "";
        String url      = urlEditText.getText()      != null ? urlEditText.getText().toString()      : "";
        String notes    = notesEditText.getText()    != null ? notesEditText.getText().toString()    : "";
        String category = categoryEditText.getText() != null ? categoryEditText.getText().toString() : "";

        viewModel.savePassword(title, username, password, url, notes, category);
    }

    /**
     * 新しいインスタンスを作成
     */
    public static AddPasswordFragment newInstance() {
        return new AddPasswordFragment();
    }
}
