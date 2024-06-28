package br.com.sysmap.bootcamp.domain.service;


import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.service.integration.SpotifyApi;
import br.com.sysmap.bootcamp.domain.service.integration.WalletApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlbumService {


    private final SpotifyApi spotifyApi;
    private final WalletApi walletApi;

    private final AlbumRepository albumRepository;
    private final UsersService usersService;


    public List<AlbumModel> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {
        return this.spotifyApi.getAlbums(search);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Album saveAlbum(Album album, String token) {
        var user = getUser();
        /*WalletDto walletDto = new WalletDto("", BigDecimal.ZERO);
        if(album.getId() == 0){
            walletDto.setValue(album.getValue());
            walletDto.setEmail(user.getEmail());
        }*/

        //album.setId(user.getId());


        walletApi.creditApiRequest(token);

        album.setUsers(user);
        return albumRepository.save(album);
    }

    private Users getUser(){
       /* String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return usersService.findByEmail(username);*/
        Users user = usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user;
    }

    public List<AlbumModel> myAlbums() throws IOException, ParseException, SpotifyWebApiException {
        var user = getUser();
       //return albumRepository.findAllByUsers(user).stream().toList();
        List<Album> albumList =  albumRepository.findAllByUsers(user).stream().toList();
        List<AlbumModel> albumModelList = new ArrayList<AlbumModel>();
        for (Album album : albumList) {
            //List<AlbumModel> albumModel =  this.spotifyApi.getAlbums(album.getIdSpotify());
            AlbumModel albumModel = this.spotifyApi.getAlbum(album.getIdSpotify());
            albumModel.setValue(album.getValue());
            albumModelList.add(albumModel);
        }

        return albumModelList;
    }

    public void removeAlbum(Long id){
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if(optionalAlbum.isPresent()){
            albumRepository.deleteById(id);
        }
    }

}
