import { useEffect, useState, useRef } from "react";
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { authenticate, COOKIES_NAME, useAuthenticate } from "../users";

export default function LoginTest() {

    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");

    let [displayName, setDisplayName] = useState("");

    let [isLogin, setLogin] = useState(false);
    let [loginFailed, setLoginFailed] = useState(false);

    let [toLogin, userInfo] = useAuthenticate();

    return (<>
        <h2>Testing Page</h2>
        <p>{toLogin ? "invalid cookies" : ""}</p>
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
                        setCookies("username", username);
                        setCookies("password", password);
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
            setCookies("loginToken", null);
        }}>
        Clear Cookies
        </button>
    </>);
}