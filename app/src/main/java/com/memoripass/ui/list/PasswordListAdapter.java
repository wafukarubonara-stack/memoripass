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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.memoripass.R;
import com.memoripass.data.model.PasswordEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * パスワード一覧アダプター
 *
 * <p>RecyclerViewでパスワードエントリを表示するためのアダプター。</p>
 *
 * @since 1.0
 */
public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.ViewHolder> {

    private List<PasswordEntry> passwords = new ArrayList<>();
    private OnItemClickListener listener;

    /**
     * アイテムクリックリスナー
     */
    public interface OnItemClickListener {
        void onItemClick(PasswordEntry passwordEntry);
        void onItemLongClick(PasswordEntry passwordEntry);
    }

    /**
     * リスナーを設定
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * データを設定
     */
    public void setPasswords(List<PasswordEntry> passwords) {
        this.passwords = passwords != null ? passwords : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_password.xmlを使用
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordEntry entry = passwords.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    /**
     * ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView usernameTextView;
        private final TextView categoryTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            usernameTextView = itemView.findViewById(R.id.username);
            categoryTextView = itemView.findViewById(R.id.category);

            // クリックリスナー
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(passwords.get(position));
                }
            });

            // 長押しリスナー
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemLongClick(passwords.get(position));
                    return true;
                }
                return false;
            });
        }

        void bind(PasswordEntry entry) {
            // タイトル
            titleTextView.setText(entry.getTitle());

            // ユーザー名
            if (entry.getUsername() != null && !entry.getUsername().isEmpty()) {
                usernameTextView.setText(entry.getUsername());
                usernameTextView.setVisibility(View.VISIBLE);
            } else {
                usernameTextView.setText("ユーザー名未設定");
                usernameTextView.setVisibility(View.VISIBLE);
            }

            // カテゴリバッジ
            if (entry.getCategory() != null && !entry.getCategory().isEmpty()) {
                categoryTextView.setText(entry.getCategory());
                categoryTextView.setVisibility(View.VISIBLE);
            } else {
                categoryTextView.setVisibility(View.GONE);
            }
        }
    }
}
