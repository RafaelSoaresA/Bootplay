import { UserModel} from '@/models/UserModel';
import { user_Api } from "@/services/apiService";
import { album_Api } from '@/services/apiService';
import { createContext, useCallback, useEffect, useState } from "react";
import { Navigate} from 'react-router-dom';

interface AuthContextModel extends UserModel {
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<string | void>;
  logout: () => void;
}

export const AuthContext = createContext({} as AuthContextModel);

interface Props {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<Props> = ({children}) => {
  const [userData, setUserData] = useState<UserModel>();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  useEffect(() => {
    const data: UserModel = JSON.parse(localStorage.getItem('@Auth.Data') || "{}");
    if(data.id){
        setIsAuthenticated(true);
        setUserData(data);
        
    }
    Logout();
  }, []);

  const Login = useCallback(async (email: string, password: string) => {
    const respAuth = await user_Api.post('/users/auth', {email, password});

    if(respAuth instanceof Error) {
      return respAuth.message;
    }


    user_Api.defaults.headers.common.Authorization = `Basic ${respAuth.data.token}`;
    album_Api.defaults.headers.common.Authorization = `Basic ${respAuth.data.token}`;
    const respUserInfo = await user_Api.get(`/users/${respAuth.data.id}`);

    if(respUserInfo instanceof Error) {
      return respUserInfo.message;
    }
    setUserData(respUserInfo.data);

    localStorage.setItem('@Auth.Data', JSON.stringify(respUserInfo.data));
  }, []);

  const Logout = useCallback(() => {
    localStorage.removeItem('@Auth.Data');
    setUserData(undefined);
   // window.alert("123");
   setIsAuthenticated(false);
    return <Navigate to="/" />
  }, []);

  return (
    <AuthContext.Provider value={{ isAuthenticated: !!userData, login: Login, logout: Logout }}>
      {children}
    </AuthContext.Provider>
  );
}