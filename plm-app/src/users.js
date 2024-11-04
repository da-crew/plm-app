import axios from "axios";

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

export const COOKIES_NAME = "loginToken";

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

export function authToken(token) {
    console.log("Authenticating using token");
    return axios.get(WEB_SERVICE_URL + "/users/auth-token", {params: {token: token}});
}