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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.memoripass.domain.model.Password;
import com.memoripass.ui.common.BaseFragment;
import com.memoripass.ui.edit.EditPasswordFragment;

/**
 * パスワード詳細Fragment
 *
 * <p>パスワードの詳細を表示する画面。</p>
 *
 * <p>機能:</p>
 * <ul>
 *   <li>パスワード情報の表示</li>
 *   <li>パスワードのコピー</li>
 *   <li>編集画面への遷移</li>
 * </ul>
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
    private Button copyPasswordButton;
    private Button editButton;
    private Button backButton;

    /**
     * 新しいインスタンスを作成
     *
     * @param passwordId パスワードID
     * @return PasswordDetailFragment
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
        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.VERTICAL);

        // タイトル
        titleTextView = createTextView("タイトル", 24, true);
        contentLayout.addView(titleTextView);

        // ユーザー名
        usernameTextView = createTextView("ユーザー名", 16, false);
        contentLayout.addView(usernameTextView);

        // パスワード
        passwordTextView = createTextView("••••••••", 16, false);
        contentLayout.addView(passwordTextView);

        // パスワードコピーボタン
        copyPasswordButton = new Button(requireContext());
        copyPasswordButton.setText("パスワードをコピー");
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        copyParams.setMargins(0, 0, 0, 32);
        copyPasswordButton.setLayoutParams(copyParams);
        contentLayout.addView(copyPasswordButton);

        // URL
        urlTextView = createTextView("URL", 14, false);
        contentLayout.addView(urlTextView);

        // カテゴリ
        categoryTextView = createTextView("カテゴリ", 14, false);
        contentLayout.addView(categoryTextView);

        // メモ
        notesTextView = createTextView("メモ", 14, false);
        contentLayout.addView(notesTextView);

        scrollView.addView(contentLayout);
        rootLayout.addView(scrollView);

        // ボタンレイアウト
        LinearLayout buttonLayout = new LinearLayout(requireContext());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setPadding(0, 32, 0, 0);

        // 編集ボタン
        editButton = new Button(requireContext());
        editButton.setText("編集");
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        editParams.setMargins(0, 0, 16, 0);
        editButton.setLayoutParams(editParams);
        buttonLayout.addView(editButton);

        // 戻るボタン
        backButton = new Button(requireContext());
        backButton.setText("戻る");
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        backButton.setLayoutParams(backParams);
        buttonLayout.addView(backButton);

        rootLayout.addView(buttonLayout);

        return rootLayout;
    }

    /**
     * TextViewを作成
     */
    private TextView createTextView(String hint, int textSize, boolean bold) {
        TextView textView = new TextView(requireContext());
        textView.setHint(hint);
        textView.setTextSize(textSize);
        if (bold) {
            textView.setTypeface(null, android.graphics.Typeface.BOLD);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);
        textView.setLayoutParams(params);
        return textView;
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
     * リスナーのセットアップ
     */
    private void setupListeners() {
        // パスワードコピーボタン
        copyPasswordButton.setOnClickListener(v -> copyPasswordToClipboard());

        // 編集ボタン
        //editButton.setOnClickListener(v -> {
            // TODO: 編集画面へ遷移
            //showMessage("編集機能は次のステップで実装します");
        //});

	// 変更後
	// 編集ボタン
	editButton.setOnClickListener(v -> {
	    // 編集画面へ遷移
	    navigateTo(EditPasswordFragment.newInstance(passwordId));
	});

        // 戻るボタン
        backButton.setOnClickListener(v -> navigateBack());
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

        titleTextView.setText(password.getTitle());
        usernameTextView.setText(password.getUsername() != null ? password.getUsername() : "");
        passwordTextView.setText("••••••••");
        passwordTextView.setTag(password.getPassword()); // パスワードをタグに保存
        urlTextView.setText(password.getUrl() != null ? password.getUrl() : "");
        categoryTextView.setText(password.getCategory() != null ? password.getCategory() : "");
        notesTextView.setText(password.getNotes() != null ? password.getNotes() : "");
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

        showMessage("パスワードをコピーしました");
    }
}
