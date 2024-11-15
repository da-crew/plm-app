import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import UserTable from "../components/UserTable";
import ResetPasswordModal from "../components/ResetPassword";
import axios from 'axios';
import { useCookies } from "react-cookie"

export default function ManageUser() {

    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);


    const [users, setUsers] = useState([//mock data
    { username: 'Dispatcher001', firstname: "Nattapol", lastname: "Aunsri", role: 'Dispatcher' },
    { username: 'Dispatcher002', firstname: "Ananya", lastname: "Chai", role: 'Dispatcher' },
    { username: 'Checker001', firstname: "Sarun", lastname: "Phan", role: 'Checker' },
    { username: 'Checker002', firstname: "Kanya", lastname: "Tham", role: 'Checker' },
    { username: 'GateOut001', firstname: "Preecha", lastname: "Thongchai", role: 'GateOut' },
    { username: 'Admin001', firstname: "Supaporn", lastname: "Yim", role: 'Admin' },
    { username: 'Dispatcher003', firstname: "Somsak", lastname: "Chaidee", role: 'Dispatcher' },
    { username: 'Checker003', firstname: "Patchara", lastname: "Yuen", role: 'Checker' },
    { username: 'GateOut002', firstname: "Wichai", lastname: "Dee", role: 'GateOut' },
    { username: 'Admin002', firstname: "Somchai", lastname: "Kam", role: 'Admin' },
    ]);
    let [showReset, setShowReset] = useState(false);
    let [selectedUser, setSelectedUser] = useState(users.username);



    useEffect(() => {
        console.log(JSON.stringify(userInfo));
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }
    }, [toLogin]);

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    function handleOnReset(username) {//click reset
        setSelectedUser(username)
        setShowReset(true)
        console.log(selectedUser)
    }
    function handleNewPassword(NewPassword) {//click submit// password ที่จะเปลี่ยน
        console.log('submit reset: ' + NewPassword)
    }

    return (<>
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a ManageUser</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <div className="center-block">
            <UserTable users={users} onReset={handleOnReset} />
        </div>
        <ResetPasswordModal show={showReset} onClose={() => setShowReset(false)}
            onPasswordReset={handleNewPassword/**/} />

    </>);

    
}