import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import ProductOrder from "../components/ProductOrder";
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";

export default function Create() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo] = useAuthenticate();

    useEffect(() => console.log(JSON.stringify(userInfo)), [userInfo]);

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />;
    }

    return (
        <>
            <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
            <DashboardHeader />
            <h1>This is a Create</h1>
            <p>Welcome, {userInfo.username}</p>
            <p>Role: {Role.toString(userInfo.role)}</p>

            <ProductOrder /> {/* Use ProductOrder component */}
        </>
    );


}