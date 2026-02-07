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

package com.memoripass.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.memoripass.R;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.ui.common.BaseFragment;

/**
 * パスワード一覧Fragment
 *
 * <p>登録されているパスワードの一覧を表示する。</p>
 *
 * <p>機能:</p>
 * <ul>
 *   <li>パスワード一覧表示</li>
 *   <li>検索</li>
 *   <li>タップで詳細画面へ遷移</li>
 *   <li>長押しで削除確認</li>
 * </ul>
 *
 * @since 1.0
 */
public class PasswordListFragment extends BaseFragment {

    private static final String TAG = "PasswordListFragment";

    private PasswordListViewModel viewModel;
    private PasswordListAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 仮のレイアウト（後でカスタムレイアウトに置き換え）
        recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupViewModel();
    }

    /**
     * RecyclerViewのセットアップ
     */
    private void setupRecyclerView() {
        adapter = new PasswordListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // アイテムクリックリスナー
        adapter.setOnItemClickListener(new PasswordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PasswordEntry passwordEntry) {
                onPasswordClick(passwordEntry);
            }

            @Override
            public void onItemLongClick(PasswordEntry passwordEntry) {
                onPasswordLongClick(passwordEntry);
            }
        });
    }

    /**
     * ViewModelのセットアップ
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PasswordListViewModel.class);

        // パスワード一覧を監視
        viewModel.getPasswords().observe(getViewLifecycleOwner(), passwords -> {
            adapter.setPasswords(passwords);
            viewModel.onPasswordsLoaded(passwords);
        });

        // ViewStateを監視
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::handleViewState);
    }

    /**
     * パスワードがクリックされた
     *
     * @param passwordEntry クリックされたパスワードエントリ
     */
    private void onPasswordClick(PasswordEntry passwordEntry) {
        Toast.makeText(
                requireContext(),
                "クリック: " + passwordEntry.getTitle(),
                Toast.LENGTH_SHORT
        ).show();

        // TODO: 詳細画面へ遷移
        // navigateTo(PasswordDetailFragment.newInstance(passwordEntry.getId()));
    }

    /**
     * パスワードが長押しされた
     *
     * @param passwordEntry 長押しされたパスワードエントリ
     */
    private void onPasswordLongClick(PasswordEntry passwordEntry) {
        // 削除確認ダイアログ
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("削除確認")
                .setMessage(passwordEntry.getTitle() + " を削除しますか？")
                .setPositiveButton("削除", (dialog, which) -> {
                    viewModel.deletePassword(passwordEntry);
                    showMessage("削除しました");
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    /**
     * 新しいインスタンスを作成
     *
     * @return PasswordListFragment
     */
    public static PasswordListFragment newInstance() {
        return new PasswordListFragment();
    }
}
