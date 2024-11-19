import {react , useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";

import { useCookies } from "react-cookie";
import { Role, useAuthenticate, WEB_SERVICE_URL, useAllProductOrders } from "../users";
import EditOrder from "../components/Create/EditOrder"
import BackButton from "../components/BackButton";
import { useParams } from "react-router";
import { useNavigate } from "react-router";

export default function EditProductOrder() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
    let [refreshTrigger, setRefreshTrigger] = useState(false);
    let [productOrders, setProductOrders, succ] = useAllProductOrders(refreshTrigger);//
    const navigate = useNavigate();
    const { blnum } = useParams();//
    console.log(blnum)
    useEffect(() => {
        console.log(JSON.stringify(userInfo));
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }

    }, [toLogin]);
    
    useEffect(() => {
        // Logic to refresh productOrders when refreshTrigger changes
        // Assume fetchProductOrders is a function in useAllProductOrders
        const fetchUpdatedOrders = async () => {
            try {
                // Re-fetch data logic
                let response = await axios.get(WEB_SERVICE_URL + "/product-orders");
                setProductOrders(response.data); // Update local state if needed
            } catch (error) {
                console.error("Error refreshing product orders:", error);
            }
        };
        fetchUpdatedOrders();
    }, [refreshTrigger]); // Dependency on refreshTrigger

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />;
    }

    //<img src={`${WEB_SERVICE_URL}/files/40d3d987-cde8-4224-befb-218946b38c4d`} style={{width: '100px',height: undefined}}/>
    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        <div className="center-block">
            <BackButton />
            <EditOrder params={blnum} productOrders={productOrders} handleOut={() => {navigate(-2)}} />

        </div>
    </>
    );


}