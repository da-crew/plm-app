import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";

import { useCookies } from "react-cookie";
import { Role, useAuthenticate } from "../users";
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




    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />

        <div className="center-block">
            <BackButton />
            <ReceiptDetail />


        </div>
    </>
    );


}