import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { Navigate, redirect } from "react-router";
import { authenticate, COOKIES_NAME } from "../users";
import '../components/LoginForm.css'

export default function Login() {
    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");
    let [loginFailed, setLoginFailed] = useState(false);
    let [isRedirect, setRedirect] = useState(false);
    let [cookies, setCookies, removeCookies] = useCookies(['username', 'password']);


    function submitCreds() {
        authenticate(username, password)
            .then((value) => {
                setCookies('username', username);
                setCookies('password', password);
                console.log("login successful! " + JSON.stringify(value.data));
                setRedirect(true);
            })
            .catch((err) => {
                setLoginFailed(true);
            });
    }

    useEffect(() => {
        authenticate(cookies.username, cookies.password)
            .then((value) => { })
            .catch((err) => {
                setCookies(COOKIES_NAME, null);
            });
    }, []);

    if (isRedirect) {
        return <Navigate to="/" />
    }


    return (<>
        <div className="container">
            <div className="login-form">
                <h2>Login</h2>
                {loginFailed ? <p>Username or password is incorrect!</p> : <p></p>}
                <form>

                    <input
                        type="text"
                        id="username"
                        placeholder="Username"
                        onChange={(e) => setUsername(e.target.value)}>
                    </input>
                    <br />
                    <input
                        type="password"
                        id="password"
                        placeholder="Password"
                        onChange={(e) => setPassword(e.target.value)}>
                    </input>
                </form>
                <button onClick={() => { submitCreds() }}>Login</button>
            </div>
        </div>
    </>);
}