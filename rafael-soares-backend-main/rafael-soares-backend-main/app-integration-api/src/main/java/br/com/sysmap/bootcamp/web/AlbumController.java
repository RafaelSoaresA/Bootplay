package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.service.AlbumService;
import br.com.sysmap.bootcamp.domain.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;


import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {


    private final AlbumService albumService;


    @GetMapping("/all")

    public ResponseEntity<List<AlbumModel>> getAlbums(@RequestParam("search") String search) throws IOException, ParseException, SpotifyWebApiException {
        return ResponseEntity.ok(this.albumService.getAlbums(search));
    }

    @PostMapping("/sale")
    public ResponseEntity<Album> saveAlbum(@RequestBody Album album, @RequestHeader("Authorization") String token) {
        //String token = "cmFmYWVsQGVtYWlsLmNvbTokMmEkMTAkNmNNTWEvOU9CaVhZampnTzU2cmxwT0hNVWl4VDIwQ29VYm1YelUuY2cyZzh3bWZFam03a0c";
        return ResponseEntity.ok(this.albumService.saveAlbum(album, token));
    }

    @GetMapping("/my-collection")
    public ResponseEntity<List<AlbumModel>> getMyAlbums() throws IOException, ParseException, SpotifyWebApiException {
        return ResponseEntity.ok(this.albumService.myAlbums());
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Album> removeAlbum(@PathVariable Long id) {
       albumService.removeAlbum(id);
       return ResponseEntity.ok().build();
    }
}
