package com.android.metube;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

public class PlaylistBottomSheetAdapter extends RecyclerView.Adapter<PlaylistBottomSheetAdapter.Holder>{
    ArrayList<PlaylistDTO> playlists = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_bottomsheet_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PlaylistDTO item = playlists.get(position);
        holder.bind(item);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelected(isChecked);
            }
        });
        if(playlists.get(position).getCnt() == 1) {
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setItem(ArrayList<PlaylistDTO> playlists){
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    public void setCheck(ArrayList<PlaylistDTO> playLists){

    }

    static class Holder extends ViewHolder {
        CheckBox checkBox;
        TextView view;

        public Holder(View itemView){
            super(itemView);
            view = itemView.findViewById(R.id.playlist_name);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void bind(PlaylistDTO item){
            view.setText(item.getName());
        }

    }
}
