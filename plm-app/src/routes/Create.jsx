import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";

import { useCookies } from "react-cookie";
import { Role, useAuthenticate, WEB_SERVICE_URL } from "../users";
import ReceiptDetail from "../components/Create/CreateOrder"
import BackButton from "../components/BackButton";

export default function Create() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);


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

    //<img src={`${WEB_SERVICE_URL}/files/40d3d987-cde8-4224-befb-218946b38c4d`} style={{width: '100px',height: undefined}}/>
    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        <div className="center-block">
            <BackButton />
            <ReceiptDetail />
        </div>
    </>
    );


}