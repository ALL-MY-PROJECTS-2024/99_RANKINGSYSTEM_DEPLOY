package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.AlbumDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import com.creator.imageAndMusic.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public boolean memberJoin(UserDto dto, Model model, HttpServletRequest request) throws Exception;

    boolean uploadAlbum(AlbumDto dto) throws IOException;


    public List<ImagesFileInfo> getUserItems() throws Exception;

    public List<ImagesFileInfo> getAllItems() throws Exception;
    public List<ImagesFileInfo> getUserItem(Long imageid) throws Exception;

    User getUser(UserDto userDto);

    boolean removeAlbumFile(Long fileid);

    boolean confirmIdPw(String username, String password);


    @Transactional(rollbackFor = Exception.class)
    boolean modifiedMyInfo(UserDto userDto, Model model);

    boolean uploadMusicAlbum(AlbumDto dto);

    @Transactional(rollbackFor = Exception.class)
    public List<MusicFileInfo> getUserMusicItems() throws Exception;

    @Transactional(rollbackFor = Exception.class)
    List<MusicFileInfo> getUserMusicItem(Long musicid);

    boolean removeAlbumMusicFile(Long fileid);

    boolean removeUser(UserDto userDto);
}
