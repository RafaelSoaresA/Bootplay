package br.com.sysmap.bootcamp.domain.mapper;

import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;

import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;

public class AlbumMapperSpotify  extends AlbumMapperImpl{

    /*@Override
    public List<AlbumModel> toModel(AlbumSimplified[] albumSimplifiedPaging) {
        if ( albumSimplifiedPaging == null ) {
            return null;
        }

        List<AlbumModel> list = new ArrayList<AlbumModel>( albumSimplifiedPaging.length );
        for ( AlbumSimplified albumSimplified : albumSimplifiedPaging ) {
            list.add( albumSimplifiedToAlbumModel2( albumSimplified ) );
        }

        return list;
    }*/

    @Override
    protected AlbumModel albumSimplifiedToAlbumModel(AlbumSimplified albumSimplified) {
        if ( albumSimplified == null ) {
            return null;
        }

        AlbumModel albumModel = new AlbumModel();

        albumModel.setAlbumType( albumSimplified.getAlbumType() );
        ArtistSimplified[] artists = albumSimplified.getArtists();
        if ( artists != null ) {
            albumModel.setArtists( Arrays.copyOf( artists, artists.length ) );
        }

        albumModel.setExternalUrls( albumSimplified.getExternalUrls() );
        albumModel.setId( albumSimplified.getId() );
        albumModel.setName( albumSimplified.getName() );

        Image[] images = albumSimplified.getImages();
        if ( images != null ) {
            albumModel.setImages( Arrays.copyOf( images, images.length ) );
        }
        albumModel.setReleaseDate( albumSimplified.getReleaseDate() );
        albumModel.setType( albumSimplified.getType() );

        return albumModel;
    }

    public AlbumModel albumToAlbumModel(Album album) {
        if ( album == null ) {
            return null;
        }

        AlbumModel albumModel = new AlbumModel();

        albumModel.setAlbumType( album.getAlbumType() );
        ArtistSimplified[] artists = album.getArtists();
        if ( artists != null ) {
            albumModel.setArtists( Arrays.copyOf( artists, artists.length ) );
        }

        albumModel.setExternalUrls( album.getExternalUrls() );
        albumModel.setId( album.getId() );
        albumModel.setName( album.getName() );

        Image[] images = album.getImages();
        if ( images != null ) {
            albumModel.setImages( Arrays.copyOf( images, images.length ) );
        }
        albumModel.setReleaseDate( album.getReleaseDate() );
        albumModel.setType( album.getType() );

        return albumModel;
    }

}
