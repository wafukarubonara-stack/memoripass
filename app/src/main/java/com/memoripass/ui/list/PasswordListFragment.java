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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.memoripass.R;
import com.memoripass.data.model.PasswordEntry;
import com.memoripass.ui.add.AddPasswordFragment;
import com.memoripass.ui.common.BaseFragment;
import com.memoripass.ui.detail.PasswordDetailFragment;

/**
 * パスワード一覧Fragment
 *
 * <p>登録されているパスワードの一覧を表示する。</p>
 *
 * @since 1.0
 */
public class PasswordListFragment extends BaseFragment {

    private static final String TAG = "PasswordListFragment";

    private PasswordListViewModel viewModel;
    private PasswordListAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private View emptyStateView;
    private TextInputEditText searchEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_list, container, false);

        recyclerView  = view.findViewById(R.id.recycler_view);
        fab           = view.findViewById(R.id.fab_add);
        emptyStateView = view.findViewById(R.id.empty_state);
        searchEditText = view.findViewById(R.id.edit_search);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupViewModel();
        setupFab();
        setupSearch();
    }

    /**
     * RecyclerViewのセットアップ
     */
    private void setupRecyclerView() {
        adapter = new PasswordListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

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

        viewModel.getPasswords().observe(getViewLifecycleOwner(), passwords -> {
            adapter.setPasswords(passwords);
            viewModel.onPasswordsLoaded(passwords);
        });

        viewModel.getViewState().observe(getViewLifecycleOwner(), this::handleViewState);
    }

    /**
     * FABのセットアップ
     */
    private void setupFab() {
        fab.setOnClickListener(v -> navigateTo(AddPasswordFragment.newInstance()));
    }

    /**
     * 検索機能のセットアップ
     */
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    viewModel.clearSearch();
                } else {
                    viewModel.searchPasswords(query);
                }
            }
        });
    }

    private void onPasswordClick(PasswordEntry passwordEntry) {
        navigateTo(PasswordDetailFragment.newInstance(passwordEntry.getId()));
    }

    private void onPasswordLongClick(PasswordEntry passwordEntry) {
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

    public static PasswordListFragment newInstance() {
        return new PasswordListFragment();
    }
}
