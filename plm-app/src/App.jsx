import { useState } from "react";
import axios from 'axios';

export default function App() {
    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");

    function auth() {
        const url = 'http://localhost:8080/test/users';
        const authUrl = 'http://localhost:8080/auth';
        const testUrl = 'http://localhost:8080/test'
        console.log("Sending a request");

        let data = {
            username: username,
            password: password,
            
            headers: {
                'Content-Type': 'application/json',
            }
        };

        let res = axios.get(authUrl, data)
            .then((value) => console.log("success: " + JSON.stringify(value.data) + "\nstatus: " + value.status))
            .catch((error) => console.log("error " + error));
    }

    return (<form>
        <label for="username">username:</label>
        <input id="username" type="text" onChange={e => setUsername(e.target.value)}></input><br/>
        <label for="password">password:</label>
        <input id="password" type="text" onChange={e => setPassword(e.target.value)}></input><br/>
        <input type="button" value="Submit" onClick={auth}></input>
    </form>);
}
