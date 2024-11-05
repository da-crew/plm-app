import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";

export default function ManageUser() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();

    useEffect(() => console.log(JSON.stringify(userInfo)), []);
    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    return (<>
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a ManageUser</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        

    </>);


}