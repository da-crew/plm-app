import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import { useCookies } from "react-cookie"



export default function Create() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);


    useEffect(() => {console.log(JSON.stringify(userInfo));
    if (toLogin || !validCreds) {
        removeCookie("username");
        removeCookie("password");
    }

}, [toLogin]);
    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    return (<>
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a Create</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        

    </>);


}