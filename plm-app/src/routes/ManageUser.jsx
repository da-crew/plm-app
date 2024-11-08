import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import UserTable from "../components/UserTable";

export default function ManageUser() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();

    useEffect(() => console.log(JSON.stringify(userInfo)), []);
    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    const [users, setUsers] = useState([//mock data
        { id: '0001', username: 'Dispatcher001', role: 'Dispatcher' },
        { id: '0004', username: 'Checker001', role: 'Checker' },
        { id: '0005', username: 'Checker002', role: 'Checker' },
        { id: '0006', username: 'Checker003', role: 'Checker' },
        { id: '0007', username: 'Exporter001', role: 'Exporter' },
    ]);

    return (<>
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a ManageUser</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <div className="center-block">
            <UserTable users={users} />
        </div>

    </>);


}