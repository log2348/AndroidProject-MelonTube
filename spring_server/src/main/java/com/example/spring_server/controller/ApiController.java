package com.example.spring_server.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_server.dto.Album;
import com.example.spring_server.dto.Data;
import com.example.spring_server.dto.Music;

@RequestMapping("/melontube")
@RestController
public class ApiController {

	private static List<Music> sampleMusic = Music.sampledata();

	@GetMapping("/musiclist")
	public Data musicList() {
		Data data = new Data();
		data.setMusicList(sampleMusic);
		return data;
	}

	// 마지막 곡이면 다음 곡 첫 곡으로
	@GetMapping("/skipmusic/next")
	public Music skipNextMusic(@RequestParam int id) {
		Music music = new Music();
		if(id == sampleMusic.size()) {
			music = sampleMusic.get(0);
		} else {
			music = sampleMusic.get(id);
		}
		return music;
	}

	// 첫 곡이면 이전 곡 마지막 곡으로
	@GetMapping("/skipmusic/previous")
	public Music skipPreviousMusic(@RequestParam int id) {
		Music music = new Music();
		if (id == 1) {
			music = sampleMusic.get(sampleMusic.size() - 1);
		} else {
			music = sampleMusic.get(id - 2);
		}
		return music;
	}

//	@GetMapping(path = "/albumlist/{albumTitle}")
//	public Album albumMusicList(@PathVariable(name = "albumTitle") String albumTitle) {
//		List<Music> albumList = new ArrayList<Music>();
//		Album album = new Album();
//
//		for (int i = 0; i < sampleMusic.size(); i++) {
//			if (sampleMusic.get(i).getAlbumTitle().equals(albumTitle)) {
//				albumList.add(sampleMusic.get(i));
//				album.setAlbumImageUrl(sampleMusic.get(i).getAlbumTitle());
//				album.setAlbumTitle(sampleMusic.get(i).getAlbumTitle());
//				album.setAlbumSinger(sampleMusic.get(i).getSinger());
//			}
//		}
//		album.setTrackList(albumList);
//		return album;
//
//	}
	
	// 앨범 상세정보 출력 (수록곡)
	@GetMapping(path="/albumlist/{albumTitle}")
	public Album albumInfo(@PathVariable String albumTitle) {
		List<Music> albumList = new ArrayList<Music>();
		Album album = new Album();

		for (int i = 0; i < sampleMusic.size(); i++) {
			if (sampleMusic.get(i).getAlbumTitle().equals(albumTitle)) {
				albumList.add(sampleMusic.get(i));
				album.setAlbumImageUrl(sampleMusic.get(i).getImageUrl());
				album.setAlbumTitle(sampleMusic.get(i).getAlbumTitle());
				album.setAlbumSinger(sampleMusic.get(i).getSinger());
			}
		}
		album.setTrackList(albumList);
		return album;

	}

	@GetMapping("/albumlist")
	public List<Album> albumMusicList() {

		List<Album> albums = new ArrayList<Album>();
		
		List<Music> albumList = new ArrayList<Music>();

		for (int i = 0; i < sampleMusic.size(); i++) {
			Album album = new Album();
			album.setAlbumTitle(sampleMusic.get(i).getAlbumTitle());
			album.setAlbumImageUrl(sampleMusic.get(i).getImageUrl());
			album.setAlbumSinger(sampleMusic.get(i).getSinger());
			
			albums.add(album);
		}

		return albums;
	}

	@PostMapping("/addmylist")
	public Music addMyList(@RequestBody Music myMusic) {
		Music music = new Music();
		music.setId(myMusic.getId());
		music.setTitle(myMusic.getTitle());
		music.setSinger(myMusic.getSinger());
		music.setImageUrl(myMusic.getImageUrl());
		music.setLyrics(myMusic.getLyrics());
		return music;
	}

	@GetMapping("/searchlist")
	public Data searchlist(@RequestParam Map<String, String> map) {
		Data data = new Data();
		HashSet<Music> tempMusics = new HashSet<Music>();
		// key 값이 제목일떄와 가수일떄 구분해주어야한다.

		map.entrySet().forEach(entry -> {
			if (entry.getKey().equals("title")) {
				for (Music music : sampleMusic) {
					if (music.getTitle().contains(entry.getValue())) {
						tempMusics.add(music);
					}
				}
			} else if (entry.getKey().equals("singer")) {
				for (Music music : sampleMusic) {
					if (music.getSinger().contains(entry.getValue())) {
						tempMusics.add(music);
					}
				}
			}
		});
		ArrayList<Music> muscis = new ArrayList<Music>();
		muscis.addAll(tempMusics);

		data.setMusicList(muscis);
		return data;

	}

}
