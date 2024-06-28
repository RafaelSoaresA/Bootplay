
import { useNavigate} from 'react-router-dom'
import logo from '../../assets/logo.svg';
import profile from '../../assets/profile.svg';
import { AlbumModel } from '@/models/AlbumModel';
import { album_Api } from '@/services/apiService';
import React, {useEffect, useState} from 'react'
import iconVideo from '../../assets/iconVideo.svg';
import dollarSign from '../../assets/dollarSign.svg';

export function Discos() {

    
    //bg-slate-900 bg-cover h-screen flex flex-col items-center justify-center
  const _navigate = useNavigate();
  const [albums, setAlbums] = useState<AlbumModel[]>([]);

  /*useEffect(() => {
    album_Api.get('/albums/all?search=Trends')
    .then((resp) => {
        // Limit the result to the first 6 albums
        const limitedAlbums = resp.data;
        setAlbums(limitedAlbums);
        console.log(limitedAlbums);
    });
}, []);*/

        useEffect(() => {
        album_Api.get('/albums/my-collection')
        .then((resp) => {
            console.log(resp.data);
            setAlbums(resp.data);
        })
    }, []);





    function handleLink(url?: string){
        window.open(url, '_blank');
    }

  return (
    <main className="bg-slate-900 bg-cover h-full w-full">
        <header className="backdrop-brightness-50 backdrop-blur-lg pt-5 flex w-screen">
            <img src={logo} className="h-12 ml-28" />
            <p className="text-white mt-3 ml-2">Bootplay</p>
            <div className="flex flex-auto justify-end mr-24">
                <p onClick={() => _navigate("/discos")} className="p-2 text-white mr-4  transition mb-3 hover:bg-slate-900 rounded-2xl cursor-pointer">Meus Discos</p>
                <p className="p-2 text-white mr-4  transition mb-3 hover:bg-slate-900 rounded-2xl cursor-pointer">Carteira</p>
                <img src={profile} className="ml-5 h-11" />
            </div>
        </header>

        <div className="flex">
            <h1 className="text-white text-4xl ml-16 mt-8">Meus Discos</h1>
        </div>
        <div className="flex flex-auto justify-end mr-24">
            <button onClick={() => _navigate("/dashboard")} className="bg-red-600 p-2 text-white">Voltar</button>
        </div>

        <div className="flex flex-auto">
            <div className="bg-white ml-28 rounded-md px-3 p-3 flex flex-wrap">
                <img src={iconVideo} className=" bg-black rounded-md p-2" />
                <h1 className=" ml-2 text-2xl">Total de albums:</h1>
                    <h1 className=" text-3xl  ml-2">{albums.length}</h1>
            </div>

            <div className="bg-white  ml-5 rounded-md px-3 p-3 flex flex-wrap">
                <img src={dollarSign} className=" bg-black rounded-md p-2" />
                <h1 className="text-2xl ml-2">Valor total investido: R${albums.reduce((acc, curr) => acc + curr.value, 0)}</h1>
            </div>
        </div>

        <section className='flex flex-wrap gap-4 items-center justify-center h-screen'>
            {/* Card */}
                    {albums?.map((album, i) => (
                        <div style={{'--bg-card': `url(${album.images[0].url})`} as React.CSSProperties} className="relative bg-[image:var(--bg-card)] flex-shrink-0 bg-cover bg-no-repeat w-60 h-[245px] rounded-md hover:scale-110 transition">
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
