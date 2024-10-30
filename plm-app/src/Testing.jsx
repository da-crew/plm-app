import { useState } from "react";
import axios from 'axios';


export default function Testing() {
    
    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");
    let [token, setToken] = useState("");
    
    const authUrl = 'http://localhost:8080/users/auth';

    function auth() {
        if (username === "" || password === "")
            return;
        console.log("Sending a request");

        let data = {
            params: {
                username: username,
                password: password
            },
        };

        axios.get(authUrl, data)
            .then((value) => {
                console.log(`success: ${JSON.stringify(value.data.token)} \nstatus: ${value.status}`);
                setToken(value.data.token);
            })
            .catch((error) => console.log("error " + error));
    }

    return (<>
    <h2>Testing Page</h2>
    <form>
        <label for="username">username:</label>
        <input id="username" type="text" onChange={e => setUsername(e.target.value)}></input><br/>
        <label for="password">password:</label>
        <input id="password" type="text" onChange={e => setPassword(e.target.value)}></input><br/>
        <input type="button" value="Submit" onClick={auth}></input>
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
        axios.get("http://localhost:8080/users/role", {params: {token: token}})
                        .then((value) => console.log(`success: ${JSON.stringify(value.data)} \nstatus: ${value.status}`))
                        .catch((error) => console.log("error " + error));
    }}>Check role</button>
    </>);
}