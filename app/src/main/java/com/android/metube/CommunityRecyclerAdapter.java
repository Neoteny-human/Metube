package com.android.metube;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import kr.co.prnd.readmore.ReadMoreTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CommunityPostDTO> posts;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    static int viewType;
    ChannelDTO channel;


    CommunityRecyclerAdapter(ArrayList<CommunityPostDTO> posts, ChannelDTO channel) {
        this.posts = posts;
        this.channel = channel;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == 1) {
            view = inflater.inflate(R.layout.channel_post_item1, parent, false);
            return new VideoViewHolder(view);
        } else if (viewType == 2) {
            view = inflater.inflate(R.layout.channel_post_item2, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == 4) {
            view = inflater.inflate(R.layout.channel_post_item4, parent, false);
            return new VoteViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.channel_post_item3, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommunityPostDTO post = posts.get(position);
        if (holder instanceof VideoViewHolder) {
            //비디오id를 가지고 비디오의 정보를 get
            int vid = post.getVideoId();
            if (vid != 0) {
                String TAG = "레트로핏2비디오";
                VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
                Call<VideoDTO> call = api.getAVideo(vid);
                call.enqueue(new Callback<VideoDTO>() {
                    @Override
                    public void onResponse(Call<VideoDTO> call, Response<VideoDTO> response) {
                        VideoDTO video = response.body();
                        Glide.with(holder.itemView.getContext()).load(video.getVideo())
                                .into(((VideoViewHolder) holder).thumbnail);
                        Glide.with(holder.itemView.getContext()).load(video.getProfile())
                                .override(140, 140)
                                .fitCenter()
                                .circleCrop()
                                .into(((VideoViewHolder) holder).videoProfileImage);
                        ((VideoViewHolder) holder).videoTitle.setText(video.getTitle());
                        ((VideoViewHolder) holder).channelName.setText(video.getChannelName());
                        ((VideoViewHolder) holder).viewCount.setText(String.valueOf(video.getViewCount()));
                        ((VideoViewHolder) holder).videoUploadDate.setText(video.getUploadDate());
                    }

                    @Override
                    public void onFailure(Call<VideoDTO> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }

            if (post.getContent() != null) {
                ((VideoViewHolder) holder).content.setText(post.getContent());
            } else {
                ((VideoViewHolder) holder).content.setVisibility(View.GONE);
            }
            ((VideoViewHolder) holder).uploadDate.setText(post.getUploadDate());

            String TAG = "레트로핏채널1";
            ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
            Call<ChannelDTO> call = api.getAChannel(post.getChannelId());
            call.enqueue(new Callback<ChannelDTO>() {
                @Override
                public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Glide.with(holder.itemView.getContext()).load(response.body().getProfile())
                            .override(140, 140)
                            .fitCenter()
                            .circleCrop()
                            .into(((VideoViewHolder) holder).profileImage);
                    ((VideoViewHolder) holder).channelName.setText(response.body().getChannelName());
                }

                @Override
                public void onFailure(Call<ChannelDTO> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else if (holder instanceof ImageViewHolder) {
            if (post.getContent() != null) {
                ((ImageViewHolder) holder).content.setText(post.getContent());
            } else {
                ((ImageViewHolder) holder).content.setVisibility(View.GONE);
            }
            ((ImageViewHolder) holder).uploadDate.setText(post.getUploadDate());

            String TAG = "레트로핏채널2";
            ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
            Call<ChannelDTO> call = api.getAChannel(post.getChannelId());
            call.enqueue(new Callback<ChannelDTO>() {
                @Override
                public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Glide.with(holder.itemView.getContext()).load(response.body().getProfile())
                            .override(140, 140)
                            .fitCenter()
                            .circleCrop()
                            .into(((ImageViewHolder) holder).profileImage);
                    ((ImageViewHolder) holder).channelName.setText(response.body().getChannelName());
                }

                @Override
                public void onFailure(Call<ChannelDTO> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });


            //이미지 스플릿
            ArrayList<String> images = new ArrayList<>();
            String[] image = post.getImages().split(",");
            for (int i = 0; i < image.length; i++) {
                images.add("https://d1all5rd9y3ynf.cloudfront.net/image/" + image[i]);
            }
            Log.d("이미지", "onBindViewHolder: 이미지 스플릿" + images.get(0));


            //서브 리사이클러뷰
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    ((ImageViewHolder) holder).imageRecyclerView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            layoutManager.setInitialPrefetchItemCount(images.size());

            ImageAdapter imageAdapter = new ImageAdapter(images);

            ((ImageViewHolder) holder).imageRecyclerView.setLayoutManager(layoutManager);
            ((ImageViewHolder) holder).imageRecyclerView.setAdapter(imageAdapter);
            ((ImageViewHolder) holder).imageRecyclerView.setRecycledViewPool(viewPool);
//            //리사이클러뷰 사이의 간격(높이) 조절이 필요할 때
//            RecyclerDecoration heightDecoration = new RecyclerDecoration(20);
//            ((ImageViewHolder) holder).imageRecyclerView.addItemDecoration(heightDecoration);


























        } else if (holder instanceof VoteViewHolder) {
            if (post.getContent() != null) {
                ((VoteViewHolder) holder).content.setText(post.getContent());
            } else {
                ((VoteViewHolder) holder).content.setVisibility(View.GONE);
            }
            ((VoteViewHolder) holder).uploadDate.setText(post.getUploadDate());
            ((VoteViewHolder) holder).likeCount.setText(String.valueOf(post.getLike()));


            String TAG = "레트로핏채널2";
            Glide.with(holder.itemView.getContext()).load(channel.getProfile())
                    .override(140, 140)
                    .fitCenter()
                    .circleCrop()
                    .into(((VoteViewHolder) holder).profileImage);
            ((VoteViewHolder) holder).channelName.setText(channel.getChannelName());


            CommunityPostInterface api2 = ApiClient.getApiClient().create(CommunityPostInterface.class);
            Call<String> call3 = api2.isLike(channel.getId(), post.getId());
            call3.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    assert response.body() != null;
                    if (response.body().equals("true")) {
                        ((VoteViewHolder) holder).like.setTag("true");
                        ((VoteViewHolder) holder).like.setImageResource(R.drawable.like_black);
                    } else {
                        ((VoteViewHolder) holder).like.setTag("false");
                        ((VoteViewHolder) holder).like.setImageResource(R.drawable.like_white);
                        Call<String> call4 = api2.isDislike(channel.getId(), post.getId());
                        call4.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                assert response.body() != null;
                                if (response.body().equals("true")) {
                                    ((VoteViewHolder) holder).dislike.setTag("true");
                                    ((VoteViewHolder) holder).dislike.setImageResource(R.drawable.dislike_black);
                                } else {
                                    ((VoteViewHolder) holder).dislike.setTag("false");
                                    ((VoteViewHolder) holder).dislike.setImageResource(R.drawable.dislike_white);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });


            //VoteDTO 다 불러오기.
            Call<ArrayList<VoteDTO>> call2 = api2.getVote(channel.getId(), post.getId());
            call2.enqueue(new Callback<ArrayList<VoteDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<VoteDTO>> call, Response<ArrayList<VoteDTO>> response) {
                    ArrayList<VoteDTO> votes = response.body();
                    Log.d(TAG, "onResponse: " + votes);
                    final int[] total = {0};
                    if (votes != null) {
                        for (int i = 0; i < votes.size(); i++) {
                            total[0] += votes.get(i).getVoteNum();
                            if (votes.get(i).getVote() == 1) {
                                for (int j = 0; j < votes.size(); j++) {
                                    votes.get(j).setShow(true);
                                }
                            }
                            Log.d("콘텐츠 확인", "onResponse: " + votes.get(i).getContent());
                        }
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            ((VoteViewHolder) holder).voteRecyclerView.getContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                    );
                    assert votes != null;
                    layoutManager.setInitialPrefetchItemCount(votes.size());

                    VoteAdapter voteAdapter = new VoteAdapter(votes, total[0]);
                    voteAdapter.setOnItemClickListener(new VoteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            //클릭받으면 전부 vote = 0으로 지우고 현재 누른 포지션이 1이면 0으로 0이면 1로!
                            int current = votes.get(position).getVote();
                            for (int i = 0; i < votes.size(); i++) {
                                if (votes.get(i).getVote() == 1) {
                                    votes.get(i).setVote(0);
                                    //deleteVote
                                    votes.get(i).setVoteNum(votes.get(i).getVoteNum() - 1);
                                    total[0] -= 1;
                                    voteAdapter.setTotal(total[0]);
                                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                                    Call<String> call = api.cancelVote(channel.getId(), votes.get(i).getId());
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                        }
                                    });
                                }
                            }
                            if (current == 0) {
                                votes.get(position).setVote(1);
                                //doVote
                                votes.get(position).setVoteNum(votes.get(position).getVoteNum() + 1);
                                CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                                Call<String> call = api.doVote(channel.getId(), votes.get(position).getId());
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                    }
                                });
                                total[0] += 1;
                                voteAdapter.setTotal(total[0]);
                                for (int j = 0; j < votes.size(); j++) {
                                    votes.get(j).setShow(true);
                                }
                            } else {
                                for (int j = 0; j < votes.size(); j++) {
                                    votes.get(j).setShow(false);
                                }
                            }
                            ((VoteViewHolder) holder).totalVote.setText(total[0] + "명 투표");
                            voteAdapter.notifyDataSetChanged();
                        }
                    });

                    ((VoteViewHolder) holder).voteRecyclerView.setLayoutManager(layoutManager);
                    ((VoteViewHolder) holder).voteRecyclerView.setAdapter(voteAdapter);
                    ((VoteViewHolder) holder).voteRecyclerView.setRecycledViewPool(viewPool);
                    ((VoteViewHolder) holder).totalVote.setText(total[0] + "명 투표");
                }

                @Override
                public void onFailure(Call<ArrayList<VoteDTO>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });


        } else {
            ((ViewHolder) holder).content.setText(post.getContent());
            ((ViewHolder) holder).uploadDate.setText(post.getUploadDate());

            String TAG = "레트로핏채널2";
            ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
            Call<ChannelDTO> call = api.getAChannel(post.getChannelId());
            call.enqueue(new Callback<ChannelDTO>() {
                @Override
                public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Glide.with(holder.itemView.getContext()).load(response.body().getProfile())
                            .override(140, 140)
                            .fitCenter()
                            .circleCrop()
                            .into(((ViewHolder) holder).profileImage);
                    ((ViewHolder) holder).channelName.setText(response.body().getChannelName());
                }

                @Override
                public void onFailure(Call<ChannelDTO> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return posts.get(position).getViewType();
    }

    public ArrayList<CommunityPostDTO> getPosts() {
        return posts;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView profileImage, threeDots, thumbnail, videoProfileImage, videoThreeDots, like, dislike;
        TextView channelName, uploadDate, videoTitle, videoChannelName, viewCount, videoUploadDate, likeCount;
        ReadMoreTextView content;


        VideoViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            threeDots = itemView.findViewById(R.id.three_dots);
            channelName = itemView.findViewById(R.id.channel_name);
            uploadDate = itemView.findViewById(R.id.upload_date);
            content = itemView.findViewById(R.id.content);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            likeCount = itemView.findViewById(R.id.like_count);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            videoProfileImage = itemView.findViewById(R.id.video_profile_image);
            videoThreeDots = itemView.findViewById(R.id.video_three_dots);
            videoTitle = itemView.findViewById(R.id.video_title);
            videoChannelName = itemView.findViewById(R.id.video_channel_name);
            viewCount = itemView.findViewById(R.id.view_count);
            videoUploadDate = itemView.findViewById(R.id.video_upload_date);

            threeDots.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            viewType = 1;
            menu.add(0, R.id.delete, getAbsoluteAdapterPosition(), "Delete");
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView profileImage, threeDots, like, dislike;
        TextView channelName, uploadDate, likeCount;
        ReadMoreTextView content;
        RecyclerView imageRecyclerView;

        ImageViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            threeDots = itemView.findViewById(R.id.three_dots);
            channelName = itemView.findViewById(R.id.channel_name);
            uploadDate = itemView.findViewById(R.id.upload_date);
            content = itemView.findViewById(R.id.content);
            imageRecyclerView = itemView.findViewById(R.id.image_recyclerview);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            likeCount = itemView.findViewById(R.id.like_count);

            threeDots.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            viewType = 2;
            menu.add(0, R.id.delete, getAbsoluteAdapterPosition(), "Delete");
        }
    }

    public class VoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView profileImage, threeDots, like, dislike;
        TextView channelName, uploadDate, totalVote, likeCount;
        ReadMoreTextView content;
        RecyclerView voteRecyclerView;

        VoteViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            threeDots = itemView.findViewById(R.id.three_dots);
            channelName = itemView.findViewById(R.id.channel_name);
            uploadDate = itemView.findViewById(R.id.upload_date);
            content = itemView.findViewById(R.id.content);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            likeCount = itemView.findViewById(R.id.like_count);

            voteRecyclerView = itemView.findViewById(R.id.vote_recyclerview);
            totalVote = itemView.findViewById(R.id.total_vote);

            threeDots.setOnCreateContextMenuListener(this);



            //클릭받으면 전부 지우고 현재 누른 포지션이 1이면 0으로 0이면 1로!
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("좋태그", "onClick: "+like.getTag()+ dislike.getTag());
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    if(dislike.getTag().equals("true")) {
                        dislike.setImageResource(R.drawable.dislike_white);
                        dislike.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike()-1);
                        Call<String> call = api.postDislike(posts.get(getAbsoluteAdapterPosition()).getId(), 7, "false");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        //싫어요 -1 싫어요 관계 삭제
                    }

                    if(like.getTag().equals("true")){
                        like.setImageResource(R.drawable.like_white);
                        like.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setLike(posts.get(getAbsoluteAdapterPosition()).getLike()-1);
                        Call<String> call = api.postLike(posts.get(getAbsoluteAdapterPosition()).getId(), 7, "false");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        //좋아요 -1 좋아요 관계 삭제
                    }else{
                        like.setImageResource(R.drawable.like_black);
                        like.setTag("true");
                        posts.get(getAbsoluteAdapterPosition()).setLike(posts.get(getAbsoluteAdapterPosition()).getLike()+1);
                        Call<String> call = api.postLike(posts.get(getAbsoluteAdapterPosition()).getId(), 7, "true");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        //좋아요 +1 좋아요 관계 생성
                    }
                    notifyDataSetChanged();
                }
            });

            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Log.d("좋태그", "onClick: "+like.getTag()+ dislike.getTag());
                    if(like.getTag().equals("true")){
                        like.setImageResource(R.drawable.like_white);
                        like.setTag("false");
                        Log.d("좋", "onClick: "+posts.get(getAbsoluteAdapterPosition()).getLike());
                        posts.get(getAbsoluteAdapterPosition()).setLike(posts.get(getAbsoluteAdapterPosition()).getLike()-1);
                        Log.d("좋", "onClick: "+posts.get(getAbsoluteAdapterPosition()).getLike());
                        Call<String> call = api.postLike(posts.get(getAbsoluteAdapterPosition()).getId(), 7, "false");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        //좋아요 -1 좋아요 관계 삭제
                    }
                    if(dislike.getTag().equals("true")){
                        dislike.setImageResource(R.drawable.dislike_white);
                        dislike.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike()-1);
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike()-1);
                        Call<String> call = api.postDislike(posts.get(getAbsoluteAdapterPosition()).getId(), 7, "false");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        //싫어요 -1 싫어요 관계 삭제
                    }else{
                        dislike.setImageResource(R.drawable.dislike_black);
                        dislike.setTag("true");
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike()+1);
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike()-1);
                        Call<String> call = api.postDislike(posts.get(getAbsoluteAdapterPosition()).getId(), 7, "true");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        //싫어요 +1 싫어요 관계 생성
                    }
                }
            });


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            viewType = 4;
            menu.add(0, R.id.delete, getAbsoluteAdapterPosition(), "Delete");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView profileImage, threeDots, like, dislike;
        TextView channelName, uploadDate, likeCount;
        ReadMoreTextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            threeDots = itemView.findViewById(R.id.three_dots);
            channelName = itemView.findViewById(R.id.channel_name);
            uploadDate = itemView.findViewById(R.id.upload_date);
            content = itemView.findViewById(R.id.content);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            likeCount = itemView.findViewById(R.id.like_count);

            threeDots.setOnCreateContextMenuListener(this);


            //클릭받으면 전부 지우고 현재 누른 포지션이 1이면 0으로 0이면 1로!
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dislike.getTag() == "true") {
                        dislike.setImageResource(R.drawable.dislike_white);
                        dislike.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike() - 1);
                        //싫어요 -1 싫어요 관계 삭제
                    }
                    if (like.getTag() == "true") {
                        like.setImageResource(R.drawable.like_white);
                        like.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setLike(posts.get(getAbsoluteAdapterPosition()).getLike() - 1);
                        //좋아요 -1 좋아요 관계 삭제
                    } else {
                        like.setImageResource(R.drawable.like_black);
                        like.setTag("true");
                        posts.get(getAbsoluteAdapterPosition()).setLike(posts.get(getAbsoluteAdapterPosition()).getLike() + 1);
                        //좋아요 +1 좋아요 관계 생성
                    }
                    notifyDataSetChanged();
                }
            });

            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (like.getTag() == "true") {
                        like.setImageResource(R.drawable.like_white);
                        like.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setLike(posts.get(getAbsoluteAdapterPosition()).getLike() - 1);
                        //좋아요 -1 좋아요 관계 삭제
                    }
                    if (dislike.getTag() == "true") {
                        dislike.setImageResource(R.drawable.dislike_white);
                        dislike.setTag("false");
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike() - 1);
                        //싫어요 -1 싫어요 관계 삭제
                    } else {
                        dislike.setImageResource(R.drawable.dislike_black);
                        dislike.setTag("true");
                        posts.get(getAbsoluteAdapterPosition()).setDislike(posts.get(getAbsoluteAdapterPosition()).getDislike() + 1);
                        //싫어요 +1 싫어요 관계 생성
                    }
                }
            });

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            viewType = 3;
            menu.add(0, R.id.delete, getAbsoluteAdapterPosition(), "Delete");
        }
    }

}
