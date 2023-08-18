package com.android.metube;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostVoteAdapter extends RecyclerView.Adapter<PostVoteAdapter.ViewHolder> {
    private ArrayList<VoteDTO> mData = new ArrayList<>();
    private Context mContext;

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    PostVoteAdapter(ArrayList<VoteDTO> list, Context context) {
        mData = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.post_vote_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if(mData.get(position).getContent()!=null){
//            holder.optionEdittext.setText(mData.get(position).getContent());
//        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView xButton;
        TextView letterLength;
        EditText optionEdittext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letterLength = itemView.findViewById(R.id.letter_length);
            optionEdittext = itemView.findViewById(R.id.option_Edittext);
            xButton = itemView.findViewById(R.id.x_button);

            optionEdittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mData.get(getAbsoluteAdapterPosition()).setContent(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                    letterLength.setText(s.length()+"/60");
                }
            });




            xButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    Log.d("포지션", "onClick: "+getAbsoluteAdapterPosition()+mListener);
                    if(pos!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            Log.d("포지션 들어오는지", "onClick: ");
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }

    }

}
