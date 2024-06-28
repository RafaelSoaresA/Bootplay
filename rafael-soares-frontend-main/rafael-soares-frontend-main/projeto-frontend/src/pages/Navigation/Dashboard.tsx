import { AlbumModel } from '@/models/AlbumModel';
import { album_Api } from '@/services/apiService';
import React, { FormEvent, useEffect, useState } from 'react';
import { useAuth } from '@/hooks/UseAuth';
import { useNavigate } from 'react-router-dom';
import profile from '../../assets/profile.svg';
import logo from '../../assets/logo.svg';
import Modal from '@/components/Modal';
import toast from 'react-hot-toast';

export function Dashboard() {


    //Usabilidades declaradas
    const [albums, setAlbums] = useState<AlbumModel[]>([]);
    const { logout } = useAuth();
    const [search, setSearch] = useState('');
    const _navigate = useNavigate();
    const [selectedAlbum, setSelectedAlbum] = useState<AlbumModel | null>(null);
    const [visibleModal, setVisibleModal] = useState(false);

    


    useEffect(() => {
        album_Api.get('/albums/all?search=Breaking Benjamin')
        .then((resp) => {
            // Limit the result to the first 6 albums
            const limitedAlbums = resp.data.slice(0, 5);
            setAlbums(limitedAlbums);
            console.log(limitedAlbums);
        });
    }, []);

    function handlePurchase(album: AlbumModel) {
        const albumData = {
            name: album.name,
            idSpotify: album.id,
            artistName: album.artists[0].name,
            imageUrl: album.images[0].url,
            value: album.value
        }

        album_Api.post('/albums/sale', albumData).then((resp) => {
            toast.success("Compra efetuada");
            console.log(resp);
            _navigate("/discos");
        }).catch((err) => {
            toast.error("Não foi possível comprar o album");
        });
        
    }
    
    function handleSearch(event: FormEvent) {

        event.preventDefault();

        album_Api.get(`/albums/all?search=${search}`).then((resp) => {
            setAlbums(resp.data);
            console.log(resp);
        })
    }

    const handleClick = (album: AlbumModel) => {
        setSelectedAlbum(album);
        setVisibleModal(true);
    }


    /*function handleLink(url?: string){
        window.open(url, '_blank');
    }*/

  return (
    <main className="flex flex-col items-center justify-center h-full w-full gap-4 bg-fundoDash bg-cover">

        <header className="backdrop-brightness-50 backdrop-blur-lg pt-5 flex w-screen ">
            <img src={logo} className="h-12 ml-28" />
            <p className="text-white mt-3 ml-2">Bootplay</p>
            <div className="flex flex-auto justify-end mr-24">
                <p onClick={() => _navigate("/discos")} className="p-2 text-white mr-4  transition mb-3 hover:bg-slate-900 rounded-2xl cursor-pointer">Meus Discos</p>
                <p className="p-2 text-white mr-4  transition mb-3 hover:bg-slate-900 rounded-2xl cursor-pointer">Carteira</p>
                <img src={profile} className="ml-5 h-11 cursor-pointer" onClick={() => logout()} onDoubleClick={() => _navigate("/")}/>
            </div>
        </header>

        <div className="mr-[700px] mt-24">
            <h1 className="text-3xl font-semibold text-white">A história da música não pode ser esquecida!</h1>
            <p className=" text-gray-400">Sucessos que marcaram o tempo!!!</p>
        </div>

        <section className='flex flex-wrap gap-4 items-center justify-center h-full bg-gradient-to-b from-current to-gray-600 mt-24'>
                
            
            <form onSubmit={handleSearch}>
                <input type="text" className="bg-zinc-900 w-[400px] text-xl font-bold text-white focus: outline-none mt-2 ring-2 ring-zinc-300 rounded-md p-1" onChange={s => setSearch(s.target.value)}/>
                <button className="text-white hover:bg-slate-900 ml-6 border-spacing-3 border-white p- rounded-md ring-2 ring-zinc-300" type="submit"> Pesquisar </button>
                {/*<img src= {lupaIcon} className=" w-10"/>*/}
            </form>

            <div className=" mr-[900px]">
                    {albums.slice(0,1).map((album) => (
                        <h1 className="text-4xl font-semibold text-white mt-32">{album.artists[0].name}</h1>
                    ))}                
            </div>


            
            <Modal isVisible={visibleModal} album={selectedAlbum} onClose={() => setVisibleModal(false)}>
                {selectedAlbum && (
                    <>
                    <div className='flex flex-row h-96'>   
                        <div style ={{'--bg-modal': `url(${selectedAlbum.images[0].url})`} as React.CSSProperties} 
                            className='w-1/2 flex bg-[image:var(--bg-modal)] bg-cover bg-no-repeat rounded-l-2xl shrink'>
                        </div>

                        <div className='flex flex-col w-1/2 p-4 justify-center leading-8'>
                            <h1 className='font-bold mt-4 mx-auto font-lato text-2xl'>{selectedAlbum.artists[0].name}</h1>

                            <div className='m-8 font-lato text-zinc-600'>
                                <p>Nome: {selectedAlbum.name}</p>
                                <p>Tipo: {selectedAlbum.type}</p>
                                {/*<p>Ano de lançamento: {format(selectedAlbum.releaseDate, 'dd/MM/yyyy')}</p>*/}
                                <p>Preço: R$ {selectedAlbum.value}</p>
                            </div>

                            <button onClick={() => handlePurchase(selectedAlbum)}className="text-white text-3xl mt-4 rounded-lg font-bold text-center font-poppins mx-auto bg-yellow-500 py-2 px-4 w-2/3">Comprar</button>
                        </div>
                    </div>
                    </>
                )}
            </Modal>

            <section className="flex justify-center h-full m-2">
                
                <div className='flex flex-wrap items-center justify-center gap-4 mt-20 '>
                    { albums?.map((album, i) => (
                    <div key={i} style ={{'--bg-card': `url(${album.images[0].url})`} as React.CSSProperties} className='relative bg-[image:var(--bg-card)] flex-shrink-0 bg-cover bg-no-repeat w-60 h-[245px] rounded-md hover:scale-110 transition'>
                        <div onClick={() => handleClick(album)} className='flex flex-col backdrop-brightness-50 h-full p-9 cursor-pointer'>
                        <h1 className='font-bold text-white text-2xl items-center text-center'>
                                {album.name}
                            </h1>
                            <h1 className='font-bold text-white text-1xl mt-2 items-center text-center'>
                                {album.artists[0].name}
                            </h1>
                            <span className='absolute order-last bottom-0 font-lato font-bold right-0 p-4 text-xl text-white'>
                                R${album.value}
                            </span>
                        </div>
                    </div>                          
                    )) }                
                </div>
            </section>       

            {/* Card */}

                   
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
