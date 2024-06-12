package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Music;
import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music,Long> {
    List<Music> findAllByUsername(String username);
}