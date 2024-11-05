import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
//import Table from "../components/Table";

export default function Dashboard() {

    let [toLogin, setToLogin] = useState(false);
    let [toCreate, setToCreate] = useState(false);
    let [toManageUser, setToManageUser] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    
    useEffect(() => console.log(JSON.stringify(userInfo)), []);
    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    if (toCreate) {
        return <Navigate to="/create" />
    }
    if (toManageUser) {
        return <Navigate to="/ManageUser" />
    }

    return (<>
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a dashboard</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <p></p>
        <div className="center-block">
            <DashboardHeader role = {userInfo.role} onCreate = {() => setToCreate(true) } onManage = {() => setToManageUser(true) }/>
            {/* List of product order here*/}
            

        </div>

    </>);


}