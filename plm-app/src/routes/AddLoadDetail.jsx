import { useEffect, useState } from "react";
import { Navigate, useParams } from "react-router";
import DashboardHeader from "../components/DashboardHeader";
import Header from "../components/Header";
import '../components/ProductOrder/ViewProductOrder.css';
import { useAllProductOrders, authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import MainDetails from "../components/ProductOrder/mainDetails"
import { useCookies } from "react-cookie"
import BackButton from "../components/BackButton";

import ActionButtons from "../components/ProductOrder/actionButtons";
import OrderTable from "../components/ProductOrder/orderTable";
import Status from "../components/ProductOrder/status";
import DamageReport from "../components/ProductOrder/damageReport";
import axios from 'axios';
import AddLoadComponent from "../components/AddLoadAndReport/addLoad";

export default function AddLoadDetail() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
    const { blnum } = useParams();

    useEffect(() => {
        console.log(JSON.stringify(userInfo));
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }

    }, [toLogin]);

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />;
    }

    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        
        <div className="center-block">
        <div
            style={{
                display: "flex",
                alignItems: "center",
                paddingLeft: "450px"
            }}
        >
            <BackButton />
        </div>
            <AddLoadComponent blnum={blnum} />




        </div>



    </>);


}