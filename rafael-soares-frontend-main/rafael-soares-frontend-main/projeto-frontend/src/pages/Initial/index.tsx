
import {useNavigate } from 'react-router-dom';
import logo from '../../assets/logo.svg';

export function Initial() {

    const _navigate = useNavigate();


  return (
    
    <div className="bg-fundo bg-cover w-screen">
   
        <header className="backdrop-brightness-50 backdrop-blur-lg pt-5 flex w-screen ">
            <img src={logo} className="h-12 ml-28" />
            <p className="text-white mt-3 ml-2">Bootplay</p>
            <div className="flex flex-auto justify-end mr-24">
                <button onClick={() => _navigate("/login")} className=" p-2 text-white mr-4 bg-black transition mb-3 hover:bg-slate-900 rounded-2xl">Entrar</button>
                <button onClick={() => _navigate("/signup")} className=" p-1 bg-blue-300 transition mb-3 hover:bg-blue-500 rounded-2xl font-semibold">Inscrever-se</button>
            </div>
        </header>
        <div className="flex items-center justify-center h-screen backdrop-brightness-50">
            <div className="flex flex-col items-center mr-96 ml-28">
                <h1 className="text-5xl  font-semibold text-white">A história da música não pode ser esquecida!</h1>
                <p className="text-xl text-white mr-72">Crie já sua conta e curta os sucessos que marcaram os tempos no Vinil.</p>
                <button onClick={() => _navigate("/signup")} className=" p-3 bg-blue-300 transition mb-3 hover:bg-blue-500 rounded-2xl font-semibold mr-80">Inscrever-se</button>
            </div>
        </div>
    </div>
  )
}
