import { useEffect, useState } from "react";
import { Navigate, useParams } from "react-router";
import DashboardHeader from "../components/DashboardHeader";
import Header from "../components/Header";
import '../components/ProductOrder/ViewProductOrder.css';
import { useAllProductOrders, authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import MainDetails from "../components/ProductOrder/mainDetails"
import { useCookies } from "react-cookie"

import ActionButtons from "../components/ProductOrder/actionButtons";
import OrderTable from "../components/ProductOrder/orderTable";
import Status from "../components/ProductOrder/status";
import DamageReport from "../components/ProductOrder/damageReport";
import axios from 'axios';

export default function AddLoadDetail() {







    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        <div className="center-block">
            <BackButton />



        </div>



    </>);


}