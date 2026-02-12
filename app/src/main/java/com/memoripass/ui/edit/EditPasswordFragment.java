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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.memoripass.R;
import com.memoripass.domain.model.Password;
import com.memoripass.ui.common.BaseFragment;

public class EditPasswordFragment extends BaseFragment {

    private static final String TAG = "EditPasswordFragment";
    private static final String ARG_PASSWORD_ID = "password_id";

    private EditPasswordViewModel viewModel;
    private String passwordId;
    private Password currentPassword;

    private TextInputEditText titleEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText urlEditText;
    private TextInputEditText notesEditText;
    private TextInputEditText categoryEditText;

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
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        titleEditText = view.findViewById(R.id.edit_title);
        usernameEditText = view.findViewById(R.id.edit_username);
        passwordEditText = view.findViewById(R.id.edit_password);
        urlEditText = view.findViewById(R.id.edit_url);
        notesEditText = view.findViewById(R.id.edit_notes);
        categoryEditText = view.findViewById(R.id.edit_category);

        view.findViewById(R.id.btn_save).setOnClickListener(v -> savePassword());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> navigateBack());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(EditPasswordViewModel.class);

        viewModel.getViewState().observe(getViewLifecycleOwner(), this::handleViewState);

        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                showMessage("パスワードを更新しました");
                navigateBack();
            }
        });

        viewModel.loadPassword(passwordId);
        viewModel.getPassword().observe(getViewLifecycleOwner(), this::displayPassword);
    }

    private void savePassword() {
        if (currentPassword == null) {
            showError("パスワードが読み込まれていません");
            return;
        }

        String title = titleEditText.getText() != null ? titleEditText.getText().toString() : "";
        String username = usernameEditText.getText() != null ? usernameEditText.getText().toString() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString() : "";
        String url = urlEditText.getText() != null ? urlEditText.getText().toString() : "";
        String notes = notesEditText.getText() != null ? notesEditText.getText().toString() : "";
        String category = categoryEditText.getText() != null ? categoryEditText.getText().toString() : "";

        viewModel.updatePassword(
                currentPassword.getId(),
                title, username, password, url, notes, category,
                currentPassword.getCreatedAt()
        );
    }

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
