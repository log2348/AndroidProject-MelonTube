package com.example.melontubeproject.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melontubeproject.R;
import com.example.melontubeproject.interfaces.OnPlayBtnClicked;
import com.example.melontubeproject.interfaces.OndeleteBtnClicked;
import com.example.melontubeproject.models.Music;

import java.util.ArrayList;
import java.util.List;

public class MyMusicListAdapter extends RecyclerView.Adapter<MyMusicListAdapter.MyMusicViewHolder> {
    public List<Music> myMusicList = new ArrayList<>();
    private ImageView playMusicBtn;
    private ImageView deleteMusicBtn;
    private OnPlayBtnClicked onPlayBtnClicked;
    private OndeleteBtnClicked ondeleteBtnClicked;

    public void setOndeleteBtnClicked(OndeleteBtnClicked ondeleteBtnClicked) {
        this.ondeleteBtnClicked = ondeleteBtnClicked;
        notifyDataSetChanged();
    }

    public void setOnPlayBtnClicked(OnPlayBtnClicked onPlayBtnClicked) {
        this.onPlayBtnClicked = onPlayBtnClicked;
        notifyDataSetChanged();
    }

    public void initMyMusicList(List<Music> myMusicList) {
        this.myMusicList = myMusicList;
        notifyDataSetChanged();
    }

    public void addMyMusicItem(List<Music> addList) {
        this.myMusicList.addAll(myMusicList.size(), addList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.my_music_list_item,parent,false);

        return new MyMusicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMusicViewHolder holder, int position) {
        Music music = myMusicList.get(position);
        holder.getMusicList(music);

        playMusicBtn = holder.itemView.findViewById(R.id.my_music_play);
        deleteMusicBtn = holder.itemView.findViewById(R.id.my_music_delete);

        playMusicBtn.setOnClickListener(event ->{
            onPlayBtnClicked.playMusic(music);
        });

        deleteMusicBtn.setOnClickListener(event ->{
            Log.d("TAG","마이뮤직리스트 delete버튼 클릭!"+ music.getId());
            ondeleteBtnClicked.deleteMusic(music);
        });
    }

    @Override
    public int getItemCount() {
        return myMusicList.size();
    }


    public static class MyMusicViewHolder extends RecyclerView.ViewHolder {

        private View mymusiclistview;
        private ImageView imageView;
        private TextView titleView;
        private TextView singerView;

        public MyMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mymusiclistview = itemView;
            imageView = mymusiclistview.findViewById(R.id.my_music_imageview);
            titleView = mymusiclistview.findViewById(R.id.my_music_title);
            singerView = mymusiclistview.findViewById(R.id.my_music_singer);
        }

        public void getMusicList(Music music) {
            titleView.setText(music.getTitle());
            singerView.setText(music.getSinger());

            Glide.with(imageView.getContext())
                    .load(music.getImageUrl())
                    .centerCrop()
                    .into(imageView);
        }
    }
}

