import { useEffect, useState } from "react";
import { Navigate, useParams } from "react-router";
import DashboardHeader from "../components/DashboardHeader";
import Header from "../components/Header";
import '../components/ProductOrder/ViewProductOrder.css';
import {useAllProductOrders, authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import MainDetails from "../components/ProductOrder/mainDetails"
import { useCookies } from "react-cookie"
import LoadDetails from "../components/ProductOrder/loadDetails";
import ActionButtons from "../components/ProductOrder/actionButtons";
import OrderTable from "../components/ProductOrder/orderTable";
import Status from "../components/ProductOrder/status";

export default function ProductOrder() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
    let [productOrders, succ] = useAllProductOrders();
    const {blnum} = useParams();////////


    useEffect(() => {console.log(JSON.stringify(userInfo))
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }
    }, [toLogin]);

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
        
    }

    return (<>
        <Header  onLogout={() => setToLogin(true)}  user={userInfo} />
        <h1>This is a ProductOrder</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <div className="center-block">
            <div className="product-order-detail-container">
                {/* Left Column: Contains main product order details, damage and load details, and action buttons */}
                <div className="left-column">

                    {/* B/L and Order Info */}
                    <MainDetails params= {blnum} productOrders={productOrders}/>
                    

                    {/* Product Order Table */}
                    <OrderTable params={blnum} productOrders={productOrders}/>

                    {/* Damage Report and Load Details Section */}
                    <div className="report-section">
                        <div className="damage-report">
                            <h4>Damage Report</h4>
                            <p>Vehicle registration number : AB 123</p>
                            <p>Report : window broken</p>
                            <img src="damage-image.jpg" alt="Damage" className="damage-image" />
                        </div>
                        {/* Load detail */}
                        <LoadDetails params = {blnum} productOrders= {productOrders}/>
                    </div>

                    {/* Action Buttons */}
                    <ActionButtons user = {userInfo} params={blnum} productOrders={productOrders}/>
                </div>

                {/* Right Column: Contains status and dispatcher information */}
                <div className="right-column">
                    {/* Status Information */}
                    <Status params = {blnum} productOrders= {productOrders}/>

                    {/* Dispatcher Information */}
                    <div className="dispatcher-info">
                        <p><strong>Dispatcher Name :</strong> Employee Admin</p>
                        <p><strong>Date :</strong> 04/10/2023</p>
                        <p><strong>Time :</strong> 16:00</p>
                    </div>
                </div>
            </div>
        </div>

    </>);


}