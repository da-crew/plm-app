import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { Navigate } from "react-router";
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
        return <Navigate to="/login"/>
    }


    return (<>
        <h1>This is a dashboard</h1>
        <p>Welcome, {username}</p>
        <p>Role: {Role.toString(role)}</p>
        <p></p>
    </>);
}