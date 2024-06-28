import React, { FormEvent, useState } from 'react'
import Input from '../../components/Input'
import { api, user_Api } from '../../services/apiService'
import logo from '../../assets/logo.svg';
import toast from 'react-hot-toast';
import { Button } from '@/components/ui/button';
import { Loader2 } from 'lucide-react';

export function Signup() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [tel, setTel] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSignup(event : FormEvent) {
    setLoading(true);
    const toastId = toast.loading("Criando conta...");
    event.preventDefault();

    const data = {
      name,
      email,
      tel,
      password
    };

    console.log(data);

    await user_Api.post("/users/create", data)
    .then(resp => {
      console.log(resp.data);
      toast.dismiss(toastId);
      toast.success("Conta criada com sucesso!");
      setLoading(false);
    }).catch(err => {
      setLoading(false);
      console.log(err);
    });
  }

  return (
    <main className="w-full h-screen flex items-center justify-center bg-fundo bg-cover">
        <div className="flex items-center justify-center h-full w-full backdrop-brightness-50 backdrop-blur-sm">
            <div className="flex flex-col bg-white rounded-md h-fit w-full max-w-[320px] items-center p-10 shadow-md">
                <img src={logo}></img>
                <h1 className="text-2xl font-bold">Inscreva-se</h1>
                <form onSubmit={handleSignup} className="flex flex-col w-full mt-8 gap-2">
                <Input type='text' onChange={event => setName(event.target.value)}>Nome</Input>
                <Input type='email' required onChange={event => setEmail(event.target.value)}>Email</Input>
                <Input type='tel' onChange={event => setTel(event.target.value)}>Telefone</Input>
                <Input type='password' required onChange={event => setPassword(event.target.value)}>Senha</Input>

                { loading ? 
                    <Button disabled>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Carregando...
                    </Button>
                    :
                    <Button type='submit' disabled={false} className="bg-zinc-900 text-white">
                    Inscrever-se
                    </Button>
                }
                </form>
                <p className="text-xs font-light">Já tem uma conta ? <a href="/Login" className="font-semibold underline">Clique Aqui</a></p>
            </div>
        </div>
    </main>
  )
}
