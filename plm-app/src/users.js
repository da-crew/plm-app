import axios from "axios";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";

export const Role = Object.freeze({ 
    UNKNOWN: Symbol.for("UNKNOWN"),
    DISPATCHER: Symbol.for("DISPATCHER"),
    EXPORTER: Symbol.for("EXPORTER"),
    CHECKER: Symbol.for("CHECKER"),
    ADMIN: Symbol.for("ADMIN"),

    fromString(s) {
        switch(s) {
            case "UNKNOWN":
                return Role.UNKNOWN;
            case "DISPATCHER":
                return Role.DISPATCHER;
            case "EXPORTER":
                return Role.EXPORTER;
            case "CHECKER":
                return Role.CHECKER;
            case "ADMIN":
                return Role.ADMIN;
            default:
                return Role.UNKNOWN;
        }
    },

    toString(r) {
        switch(r) {
            case Role.UNKNOWN:
                return "UNKNOWN";
            case Role.DISPATCHER:
                return "DISPATCHER";
            case Role.EXPORTER:
                return "EXPORTER";
            case Role.CHECKER:
                return "CHECKER";
            case Role.ADMIN:
                return "ADMIN";
            default:
                return "UNKNOWN";
        }
    }
});

export const COOKIES_NAME = "loginCreds";

export const WEB_SERVICE_URL = "http://localhost:8080";

/*
String token: login token to be checked

returns: Role
*/
export function checkRole(token) {
    if (token === "") {
        return Role.UNKNOWN;
    }

    let resp = Role.UNKNOWN;
    return axios.get(WEB_SERVICE_URL + "/users/role", {params:{token: token}});
}

/*
String username
String password
returns: login token of type String
*/
export function authenticate(username, password) {
    console.log("Sending a request");

    let data = {
        params: {
            username: username,
            password: password
        },
    };
    return axios.get(WEB_SERVICE_URL + "/users/auth", data)
}

export function useAuthenticate() {
    let [userInfo, setUserInfo] = useState({});
    let [cookies, setCookies, removeCookies] = useCookies(['username', 'password']);
    let [success, setSuccess] = useState(true);

    useEffect(() => {
        if (cookies.username != null && cookies.password != null) {
            console.log("authenticating");
            authenticate(cookies.username, cookies.password)
                .then((value) => {
                    setUserInfo(value.data);
                })
                .catch((e) => {
                    console.log("Invalid token");
                    setCookies('username', null);
                    setCookies('password', null);
                    setSuccess(false);
                });
        } else {
            setSuccess(false);
        }
    }, [cookies.username, cookies.password, setCookies, setSuccess, success]);

    let user = {
        username: userInfo.username, 
        firstname: userInfo.firstname, 
        lastname: userInfo.lastname, 
        role: Role.fromString(userInfo.role)
    };

    console.log(userInfo.role);

    return [success, user, cookies.password];
}


//CALL useAuthenticate() FIRST BEFORE USING THIS!!!!!
export function useProductOrders() {
    let [success, setSuccess] = useState(true);
    let [cookies, setCookies, removeCookies] = useCookies(['username', 'password']);
    let [productOrders, setProductOrders] = useState([]);

    useEffect(() => {
        if (cookies.username != null && success){
            //console.log("Sending Request to " + WEB_SERVICE_URL + "/product-orders/" + cookies.username + "/");
            axios.get(WEB_SERVICE_URL + "/product-orders/" + cookies.username + "/")
                .then((response) => {
                    setProductOrders(response.data);
                    //console.log("response data " + response.data);
                })
                .catch((error) => {
                    //console.log("Error " + error);
                    setSuccess(false);
                });
        } else {
            //console.log("Authorization unsuccessful");
        }
    }, [success]);


    return [productOrders, success]
}

export function useAllProductOrders() {
    let [success, setSuccess] = useState(true);
    let [productOrders, setProductOrders] = useState([]);

    useEffect(() => {
        if (success){
            axios.get(WEB_SERVICE_URL + "/product-orders")
                .then((response) => {
                    setProductOrders(response.data);
                })
                .catch((error) => {
                    setSuccess(false);
                });
        } else {
        }
    }, [success]);


    return [productOrders, success]
}