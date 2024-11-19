import { useEffect, useState } from "react";
import { Navigate, useParams } from "react-router";
import Header from "../components/Header";
import '../components/ProductOrder/ViewProductOrder.css';
import { useAuthenticate } from "../users";
import { useCookies } from "react-cookie"
import BackButton from "../components/BackButton";

import AddLoadComponent from "../components/AddLoadAndDamage/addLoad";

export default function AddLoadDetail() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
    const { blnum } = useParams();

    useEffect(() => {
        console.log(JSON.stringify(userInfo));
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }

    }, [toLogin]);

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />;
    }

    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
       
        
        <div className="center-block">
            <div style={{marginLeft: '225px'}}><BackButton/></div>
            <AddLoadComponent blnum={blnum} />




        </div>



    </>);


}