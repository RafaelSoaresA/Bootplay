import { AlbumModel } from '@/models/AlbumModel';
import { album_Api } from '@/services/apiService';
import React, {useEffect, useState} from 'react'


export function Landing() {

    const [albums, setAlbums] = useState<AlbumModel[]>([]);
  


    useEffect(() => {
        //album_Api.defaults.headers.common.Authorization = "Basic dGVzdGV0ZXN0ZUB0ZXN0ZS5jb206JDJhJDEwJFF2NTBGVnJkdFZRZ2hxQi5YVEpxSWUxSVRkOTlOSW41Rm9tSnBxQVJXbVZZMC9xc2V2c3pT"
        album_Api.get('/api/albums/all?search=Rock' , {headers: {'Authorization': "Basic dGVzdGV0ZXN0ZUB0ZXN0ZS5jb206JDJhJDEwJFF2NTBGVnJkdFZRZ2hxQi5YVEpxSWUxSVRkOTlOSW41Rm9tSnBxQVJXbVZZMC9xc2V2c3pT"}})
        .then((resp) => {
            setAlbums(resp.data);
            console.log(albums);
        })
    }, []);

    function handleLink(url?: string){
        window.open(url, '_blank');
    }

  return (
    <main className="flex flex-col items-center justify-center h-full mt-10 gap-4">
      <h1 className="text-2xl font-semibold">Albums</h1>
      <input type='text' />
      <button>Buscar</button>

      <section className='flex flex-wrap gap-4 items-center justify-center h-screen'>
        {/* Card */}
   {albums?.map((album, i) => (
              <div style={{'--bg-card': `url(${album.images[0].url})`} as React.CSSProperties} className="bg-[image:var(--bg-card)] bg-cover bg-no-repeat w-60 h-[245px] rounded-md">
              <div onClick={() => handleLink(album.externalUrls.externalUrls.spotify)} className="flex h-full justify-center items-center backdrop-brightness-50 p-6 cursor-pointer">
                <h1 className="text-2xl font-semibold text-white">{album.name}</h1>
              </div>
            </div>
        )) }  

      {/*  {albums?.map((album, i) => (
                <div className="w-60 h-[245px] rounded-md">
                    <div className="absolute w-60 h-[245px] backdrop-brightness-50">
                        <h1 className="text-2xl font-semibold text-white">{album.name}</h1>
                    </div>
                    <img src={album.images[0].url} alt={album.name} className="object-cover" />
                </div>
                )) } */}
        {/* Card */}
      </section>
    </main>
  )
}
