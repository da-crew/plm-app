import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { RouterProvider, createBrowserRouter } from 'react-router-dom';

import Login from './routes/Login.jsx';
import Dashboard from './routes/Dashboard.jsx';
import LoginTest from './routes/LoginTest.jsx';
import Create from './routes/Create.jsx';
import ManageUser from './routes/ManageUser.jsx';

import ProductOrder from './routes/ViewProductOrder.jsx'
import AddLoadDetail from "./routes/AddLoadDetail.jsx"; 
import FetchTest from './routes/FetchTest.jsx';


const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/",
    element: <Dashboard />,

  },
  {
    path: "/Create",
    element: <Create />,
  },
  {
    path: "/ManageUser",
    element: <ManageUser />,
  },
  {
    path: "/ProductOrder/:blnum",
    element: <ProductOrder />,
  },
  {
    path: "/ProductOrder/:blnum/add-load",
    element: <AddLoadDetail/>,
  },
  {
    path: "/login-test",
    element: <LoginTest />,
  },
  {
    path: "/fetch-test",
    element: <FetchTest />
  }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
