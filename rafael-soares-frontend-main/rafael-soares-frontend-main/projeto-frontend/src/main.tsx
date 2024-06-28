import ReactDOM from 'react-dom/client'
import './global.css';
import React from 'react';
import { Toaster } from 'react-hot-toast';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Page01 } from './pages/Navigation/Page01';
import { Dashboard} from './pages/Navigation/Dashboard';
import { ErrorPage } from './pages/Navigation/Error';
import { Login } from './pages/Login';
import { AuthProvider } from './context/AuthContext';
import { PrivateRoutes } from './utils/PrivateRoutes';
import { Initial } from './pages/Initial';
import { Signup } from './pages/Signup';
import { Discos } from './pages/Navigation/Discos';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.Fragment>
    <Toaster position='top-right' toastOptions={{ duration: 2000 }} />
    <AuthProvider>
      {/* REACT ROUTER */}
      <BrowserRouter>
        <Routes>
          <Route path='*' element={<ErrorPage />} />
          <Route path='/' index element={<Initial />} />
          <Route path='/signup' element={<Signup />} />
          <Route path='/login' element={<Login />} />
          <Route path='' element={<PrivateRoutes />}>
            <Route path='/page01' element={<Page01 />} />
            <Route path='/dashboard' element={<Dashboard />} /> 
            <Route path='/discos' element={<Discos />} /></Route>
          
        </Routes>
      </BrowserRouter>
      {/* REACT ROUTER */}
    </AuthProvider>
  </React.Fragment>
)