import { useProductOrders, useAuthenticate } from "../users";
import { useEffect, useState } from "react";

export default function FetchTest() {
    
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [productOrders, succ] = useProductOrders();

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    useEffect(() => {
        if (succ) {
            console.log(JSON.stringify(productOrders));
        } else {
            console.log("failed FAILED FAILED!!!!!!");
        }
    }, [productOrders]);

    
    
}