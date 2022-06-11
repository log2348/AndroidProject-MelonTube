package com.example.melontubeproject;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.melontubeproject.databinding.ActivityMusicPlayBinding;
import com.example.melontubeproject.databinding.CustomExoViewBinding;
import com.example.melontubeproject.models.Music;
import com.example.melontubeproject.repository.MusicService;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicPlayActivity extends AppCompatActivity {

    private Music music;
    private ActivityMusicPlayBinding binding;
    private MusicService musicService;

    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;

    private ImageButton playBtn;
    private ImageButton skipNextBtn;
    private ImageButton skipPreviousBtn;

    private final String TAG = MusicPlayActivity.class.getName();

    public final String OBJ_NAME = "music";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayBinding.inflate(getLayoutInflater());
        musicService = MusicService.retrofit.create(MusicService.class);
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            music = (Music) getIntent().getSerializableExtra(OBJ_NAME);
            setNewMusic();
            playMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

    private void setNewMusic() {
        initData(music);
        showLyrics();
        addEventListener();
    }

    private void initData(Music music) {
        binding.titlePlayText.setText(music.getTitle());
        binding.singerPlayText.setText(music.getSinger());
        binding.lyricsTextView.setText(music.getLyrics());
        binding.scrollView.setVisibility(View.INVISIBLE);

        Glide.with(this)
                .load(music.getImageUrl())
                .into(binding.elbumPlayImage);

    }

    // 앨범 커버 클릭시 가사 출력 -> 재클릭시 다시 커버
    private void showLyrics() {

        binding.elbumPlayImage.setOnClickListener(v -> {
            binding.scrollView.setVisibility(View.VISIBLE);
            Log.d(TAG, "가사 보이기");
        });

        binding.lyricsTextView.setOnClickListener(v -> {
            binding.scrollView.setVisibility(View.GONE);
            Log.d(TAG, "가사 안보이기");
        });
    }

    private void addEventListener() {
        // 다음 노래 재생 버튼 클릭
        playBtn = findViewById(R.id.playMusicBtn);
        skipNextBtn = findViewById(R.id.skipNextBtn);
        skipPreviousBtn = findViewById(R.id.skipPreviousBtn);

        skipNextBtn.setOnClickListener(v -> {
            skipNextMusic();
        });

        skipPreviousBtn.setOnClickListener(v -> {
            skipPreviousMusic();
        });
    }

    private void playMusic() {
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        PlayerControlView playerControlView = binding.playerControlView;
        playerControlView.setPlayer(simpleExoPlayer);

        setMusic(music);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
    }

    private void setMusic(Music music) {

        musicService.playMusic(music.getTitle())
                .enqueue(new Callback<Music>() {
                    @Override
                    public void onResponse(Call<Music> call, Response<Music> response) {
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(getString(R.string.app_name));
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(Uri.parse(response.body().getAudioUrl()));

                    }

                    @Override
                    public void onFailure(Call<Music> call, Throwable t) {

                    }
                });
    }

    private void skipNextMusic() {
        musicService.skipNextMusic(music.getId())
                .enqueue(new Callback<Music>() {
                    @Override
                    public void onResponse(Call<Music> call, Response<Music> response) {
                        music = response.body();
                        setNewMusic();

                        Log.d(TAG, "다음 노래 재생 !!!!");
                    }

                    @Override
                    public void onFailure(Call<Music> call, Throwable t) {
                        Toast.makeText(MusicPlayActivity.this, "네트워크 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void skipPreviousMusic() {
        musicService.skipPreviousMusic(music.getId())
                .enqueue(new Callback<Music>() {
                    @Override
                    public void onResponse(Call<Music> call, Response<Music> response) {
                        music = response.body();
                        setNewMusic();
                        Log.d(TAG, "이전 노래 재생 !!!!");
                    }

                    @Override
                    public void onFailure(Call<Music> call, Throwable t) {
                        Toast.makeText(MusicPlayActivity.this, "네트워크 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}