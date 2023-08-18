package com.android.metube;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ImageViewHolder> {
    private ArrayList<VoteDTO> votes;
    int total;

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private VoteAdapter.OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(VoteAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    VoteAdapter(ArrayList<VoteDTO> votes, int total){
        this.votes = votes;
        this.total = total;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.content.setText(votes.get(position).getContent());
        if(votes.get(position).isShow()) {
            holder.percentage.setVisibility(View.VISIBLE);
            int percent = 0;
            if (total != 0) {
                percent = (int) (((double)(votes.get(position).getVoteNum()) / (double) total) * 100);
                Log.d("진짜 뭐?", "onBindViewHolder: "+votes.get(position).getVoteNum()/total);
            }
            holder.progressBar.setProgress(percent);
            holder.percentage.setText(percent + "%");
            if (votes.get(position).getVote() == 1) {
                holder.progressBar.setBackgroundColor(Color.BLACK);
            } else {
                holder.progressBar.setBackgroundColor(Color.WHITE);
            }
        }else{
            holder.percentage.setVisibility(View.INVISIBLE);
            holder.progressBar.setProgress(0);
        }

    }


    @Override
    public int getItemCount() {
        return votes.size();
    }

    public void setTotal(int total){
        this.total = total;
    }




    class ImageViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView content, percentage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            content = itemView.findViewById(R.id.content);
            percentage = itemView.findViewById(R.id.percentage);

            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });


        }

    }


}
