import { useEffect, useState, useRef } from "react";
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { authenticate, authToken, COOKIES_NAME } from "../users";

export default function Testing() {

    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");

    let [displayName, setDisplayName] = useState("");

    let [isLogin, setLogin] = useState(false);
    let [loginFailed, setLoginFailed] = useState(false);
    let [cookies, setCookies, removeCookies] = useCookies([COOKIES_NAME]);
    let [token, setToken] = useState(null);
    
    if (cookies.loginToken != null) {
        authToken(cookies.loginToken)
            .then((value) => {
                setToken(cookies.loginToken);
                setLogin(true);
                setDisplayName(value.data.username);
                console.log("Login successful");
            }).catch((e) => {
                setCookies(COOKIES_NAME, null);
            });
    }



    return (<>
        <h2>Testing Page</h2>
        <p>{loginFailed ? "Login failed" : ""}</p>
        {
            isLogin ? <div>
                <p>Welcome, {displayName}</p>
                <p>Login token: {token}</p>
            </div> : <p>Please Login.</p>
        }
        <form>
            <label forhtml="username">username:</label>
            <input id="username" type="text" onChange={e => setUsername(e.target.value)}></input><br />
            <label forhtml="password">password:</label>
            <input id="password" type="text" onChange={e => setPassword(e.target.value)}></input><br />
            <input type="button" value="Submit" onClick={(e) => {
                e.preventDefault();
                console.log("authenticating");
                authenticate(username, password)
                    .then((value) => {
                        setToken(value.data.token);
                        setCookies("loginToken", value.data.token);
                        setLoginFailed(false);
                        setDisplayName(username);
                        setLogin(true);
                        console.log("Login successful");
                    }
                    ).catch((error) => {
                        setLoginFailed(true);
                        console.log("Login failed");
                    });
            }}></input>
        </form>

        <button onClick={() => {
            console.log("Testing");
            axios.get("http://localhost:8080/test")
                .then((value) => console.log(`success: ${JSON.stringify(value.data)} \nstatus: ${value.status}`))
                .catch((error) => console.log("error " + error));
        }}>Test</button><br></br>

        <button onClick={() => {
            if (token === "") {
                console.log("Token is empty!");
                return;
            }
            console.log("Checking role");
            axios.get("http://localhost:8080/users/role", { params: { token: token } })
                .then((value) => console.log(`success: ${JSON.stringify(value.data)} \nstatus: ${value.status}`))
                .catch((error) => console.log("error " + error));
        }}>Check role</button><br/>
        <button onClick={() => {
            setCookies("loginToken", null);
        }}>
        Clear Cookies
        </button>
    </>);
}