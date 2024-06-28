package br.com.sysmap.bootcamp.domain.service.integration;

import br.com.sysmap.bootcamp.domain.mapper.AlbumMapper;
import br.com.sysmap.bootcamp.domain.mapper.AlbumMapperSpotify;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class SpotifyApi {

    private se.michaelthelin.spotify.SpotifyApi spotifyApi = new se.michaelthelin.spotify.SpotifyApi.Builder()
            .setClientId("59352df32bd14e368d3fba928cea08a6")
            .setClientSecret("5004b50316114877873511e65841a8ce")
            .build();

    public List<AlbumModel> getAlbums(String search ) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApi.setAccessToken(getToken());
        //var x = Arrays.stream(spotifyApi.searchAlbums(search).market(CountryCode.BR).limit(30).build().execute().getItems()).toList();
        AlbumMapperSpotify xAlbumMapperSpotify = new AlbumMapperSpotify();
        return xAlbumMapperSpotify.toModel(spotifyApi.searchAlbums(search).market(CountryCode.BR)
                        .limit(30)
                        .build().execute().getItems()).stream()
                .peek(album -> album.setValue(BigDecimal.valueOf((Math.random() * ((100.00 - 12.00) + 1)) + 12.00)
                        .setScale(2, BigDecimal.ROUND_HALF_UP))).toList();

        /*return AlbumMapperSpotify.INSTANCE.toModel(spotifyApi.searchAlbums(search).market(CountryCode.BR)
                        .limit(30)
                        .build().execute().getItems()).stream()
                        .peek(album -> album.setValue(BigDecimal.valueOf((Math.random() * ((100.00 - 12.00) + 1)) + 12.00)
                        .setScale(2, BigDecimal.ROUND_HALF_UP))).toList();*/
    }

    public AlbumModel getAlbum(String idSpotify ) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApi.setAccessToken(getToken());
        //var x = Arrays.stream(spotifyApi.searchAlbums(search).market(CountryCode.BR).limit(30).build().execute().getItems()).toList();
        AlbumMapperSpotify xAlbumMapperSpotify = new AlbumMapperSpotify();
        Album vAlbum = spotifyApi.getAlbum(idSpotify).market(CountryCode.BR).build().execute();
        return xAlbumMapperSpotify.albumToAlbumModel(vAlbum);
    }


    public String getToken() throws IOException, ParseException, SpotifyWebApiException {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        return clientCredentialsRequest.execute().getAccessToken();
    }
}
