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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * パスワード一覧アダプター
 *
 * <p>RecyclerViewでパスワードエントリを表示するためのアダプター。</p>
 *
 * <p>表示内容:</p>
 * <ul>
 *   <li>タイトル</li>
 *   <li>ユーザー名</li>
 *   <li>更新日時</li>
 * </ul>
 *
 * <p>セキュリティ:</p>
 * <ul>
 *   <li>パスワードは表示しない（暗号化されたまま）</li>
 *   <li>タイトルとユーザー名のみ表示</li>
 * </ul>
 *
 * @since 1.0
 */
public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.ViewHolder> {

    private List<PasswordEntry> passwords = new ArrayList<>();
    private OnItemClickListener listener;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm",
            Locale.getDefault()
    );

    /**
     * アイテムクリックリスナー
     */
    public interface OnItemClickListener {
        /**
         * アイテムがクリックされた
         *
         * @param passwordEntry クリックされたパスワードエントリ
         */
        void onItemClick(PasswordEntry passwordEntry);

        /**
         * アイテムが長押しされた
         *
         * @param passwordEntry 長押しされたパスワードエントリ
         */
        void onItemLongClick(PasswordEntry passwordEntry);
    }

    /**
     * リスナーを設定
     *
     * @param listener リスナー
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * データを設定
     *
     * @param passwords パスワードリスト
     */
    public void setPasswords(List<PasswordEntry> passwords) {
        this.passwords = passwords != null ? passwords : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
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
        private final TextView subtitleTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(android.R.id.text1);
            subtitleTextView = itemView.findViewById(android.R.id.text2);

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

            // サブタイトル（ユーザー名 + 更新日時）
            StringBuilder subtitle = new StringBuilder();

            if (entry.getUsername() != null && !entry.getUsername().isEmpty()) {
                subtitle.append(entry.getUsername());
                subtitle.append(" • ");
            }

            subtitle.append(dateFormat.format(new Date(entry.getUpdatedAt())));

            subtitleTextView.setText(subtitle.toString());
        }
    }
}
