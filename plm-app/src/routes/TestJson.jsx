import axios from "axios";
import { useEffect } from "react";
import { WEB_SERVICE_URL } from "../users";


export default function TestJson() {

    useEffect(() => {
        let config = {
            headers: {
                "Content-Type" : "application/json",
            }
        };

        let data = {
            username: "googa3324",
            password: "0489352544"
        };
        
        axios.post(WEB_SERVICE_URL + "/users/auth", {params: data, headers: config.headers})
            .then((value) => {
                console.log(value.data);
            })
            .catch((reason) => {
                console.log(reason.message);
            });
    }, []);

    return (<>
    </>);
}