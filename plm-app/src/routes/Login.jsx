import { useEffect } from "react";
import { Navigate, redirect } from "react-router";


export default function Login() {
    useEffect(() => {
        console.log("Effect");
        redirect("/testing");
    });

    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");

    
    return <h1>Login Now!</h1>;
}