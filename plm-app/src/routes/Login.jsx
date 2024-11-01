import { useState, useEffect } from "react"; 
import { useCookies } from "react-cookie";
import { Navigate, redirect } from "react-router";
import { authenticate, authToken, COOKIES_NAME } from "../users";


export default function Login() {
    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");
    let [loginFailed, setLoginFailed] = useState(false);
    let [isRedirect, setRedirect] = useState(false);

    let [cookies, setCookies, removeCookies] = useCookies([COOKIES_NAME]);
    

    function submitCreds() {
        authenticate(username, password)
            .then((value) => {
                setCookies(COOKIES_NAME, value.data.token);
                setRedirect(true);
            })
            .catch((err) => {
                setLoginFailed(true);
            });
    }

    useEffect(() => {
        if (cookies.loginToken != null) {
            authToken(cookies.loginToken)
                .then((value) => {})
                .catch((e) => {
                    setCookies(COOKIES_NAME, null);
                });
        }
    }, []);

    if (isRedirect) {
        return <Navigate to="/"/>
    }


    return (<>
        <h2>Login</h2>
        {loginFailed ? <p>Username or password is incorrect!</p> : <p></p>}
        <form>
            <label htmlFor="username">Username: </label>
            <input type="text" id="username" onChange={(e) => setUsername(e.target.value)}></input><br/>
            <label htmlFor="password">Password: </label>
            <input type="password" id="password" onChange={(e) => setPassword(e.target.value)}></input>
        </form>
        <button onClick={() => {submitCreds()}}>Login</button>
    </>);
}