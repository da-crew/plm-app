import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { RouterProvider, createBrowserRouter } from 'react-router-dom';

import Login from './routes/Login.jsx';
import Dashboard from './routes/Dashboard.jsx';
import LoginTest from './routes/LoginTest.jsx';

const router = createBrowserRouter([
    {
        path: "/login",
        element: <Login/>,
    },
    {
        path: "/",
        element: <Dashboard/>,
        
    },
    {
        path: "/login-test",
        element: <LoginTest/>,
    }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
