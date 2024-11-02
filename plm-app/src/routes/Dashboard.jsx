import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { authToken, Role } from "../users";


export default function Dashboard() {
    let [cookies, setCookies, removeCookies] = useCookies(['loginToken']);
    let [toLogin, setToLogin] = useState(false);
    let [username, setUsername] = useState("");
    let [role, setRole] = useState(Role.UNKNOWN);

    useEffect(() => {
        if (cookies.loginToken != null) {
            authToken(cookies.loginToken)
                .then((value) => {
                    setUsername(value.data.username);
                    setRole(Role.fromString(value.data.role));
                })
                .catch((e) => {
                    console.log("Invalid token");
                    setCookies("loginCookies", null);
                    setToLogin(true);
                });
        } else {
            console.log("Invalid token");
            setToLogin(true);
        }
    }, []);

    if (toLogin) {
        return <Navigate to="/login" />
    }


    return (<>
        <Header employeeName={username} onLogout={() => setToLogin(true)} role={Role.toString(role)} />
        <h1>This is a dashboard</h1>
        <p>Welcome, {username}</p>
        <p>Role: {Role.toString(role)}</p>
        <p></p>
        <div className="center-block">
            <DashboardHeader isDispatcher={Role.toString(role) == "DISPATCHER" | Role.toString(role) == "ADMIN"}
                isAdmin={Role.toString(role) == "ADMIN"}
            />
            {/* List of product order here*/}







        </div>

    </>);


}