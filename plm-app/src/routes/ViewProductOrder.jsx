import { useEffect, useState } from "react";
import { Navigate, useParams } from "react-router";
import DashboardHeader from "../components/DashboardHeader";
import Header from "../components/Header";
import '../components/ProductOrder/ViewProductOrder.css';
import { useAllProductOrders, authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import MainDetails from "../components/ProductOrder/mainDetails"
import { useCookies } from "react-cookie"
import LoadDetails from "../components/ProductOrder/loadDetails";
import ActionButtons from "../components/ProductOrder/actionButtons";
import OrderTable from "../components/ProductOrder/orderTable";
import Status from "../components/ProductOrder/status";
import DamageReport from "../components/ProductOrder/damageReport";
import axios from 'axios';
import { WEB_SERVICE_URL } from "../users";
import BackButton from "../components/BackButton";
import ActionLogs from "../components/ProductOrder/actionLogs";

export default function ProductOrder() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
    let [refreshTrigger, setRefreshTrigger] = useState(false);
    let [productOrders, setProductOrders, succ] = useAllProductOrders(refreshTrigger);
    let [toReprot, setToReport] = useState(false)
    let [toLoad, setToLoad] = useState(false)

    
    const { blnum } = useParams();


    useEffect(() => {
        console.log(JSON.stringify(userInfo))
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }
    }, [toLogin]);

    useEffect(() => {
        // Logic to refresh productOrders when refreshTrigger changes
        // Assume fetchProductOrders is a function in useAllProductOrders
        const fetchUpdatedOrders = async () => {
            try {
                // Re-fetch data logic
                let response = await axios.get(WEB_SERVICE_URL + "/product-orders");
                setProductOrders(response.data); // Update local state if needed
            } catch (error) {
                console.error("Error refreshing product orders:", error);
            }
        };
        fetchUpdatedOrders();
    }, [refreshTrigger]); // Dependency on refreshTrigger

    if (toReprot) {
        return <Navigate to={`/ProductOrder/${blnum}/add-Damage`} />;
    }
    if (toLoad) {
        return <Navigate to={`/ProductOrder/${blnum}/add-load`} />;
    }

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />

    }
    const handleForward = async () => {
        const payload = {
            caller: {
                username: userInfo.username,
                password: password,
            }
        };
        try {
            const response = await axios.post(
                WEB_SERVICE_URL + "/product-orders/" + blnum + "/forward",
                payload,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );
            alert("Action successful!");
            setRefreshTrigger(!refreshTrigger);
        } catch (error) {
            console.error("Error during forward request:", error);
            if (error.response) {
                alert(`Error: ${error.response.status} - ${error.response.data}`);
            } else {
                console.error("Error:", error.message);
            }
        }

    }
    const handleReturn = async () => {
        const payload = {
            caller: {
                username: userInfo.username,
                password: password,
            }
        };
        try {
            const response = await axios.post(
                WEB_SERVICE_URL + "/product-orders/" + blnum + "/return",
                payload,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                },
            );
            alert("Action successful!");
            setRefreshTrigger(!refreshTrigger);
        } catch (error) {
            console.error("Error during forward request:", error);
            if (error.response) {
                alert(`Error: ${error.response.status} - ${error.response.data}`);
            } else {
                console.error("Error:", error.message);
            }
        }

    }

    function handleRefresh() {//when click product order in list
        setRefreshTrigger(!refreshTrigger);
    }

    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        <div className="center-block">
            <BackButton/>
            <div className="product-order-detail-container">
                {/* Left Column: Contains main product order details, damage and load details, and action buttons */}
                <div className="left-column">

                    {/* B/L and Order Info */}
                    <MainDetails params={blnum} productOrders={productOrders} />


                    {/* Product Order Table */}
                    <OrderTable params={blnum} productOrders={productOrders} />

                    {/* Damage Report and Load Details Section */}
                    <div className="report-section">
                        <DamageReport params={blnum} productOrders={productOrders} />

                        {/* Load detail */}
                        <LoadDetails params={blnum} productOrders={productOrders} onDeleteCar={(handleRefresh)} />
                    </div>

                    {/* Action Buttons */}
                    <ActionButtons user={userInfo} params={blnum} 
                    productOrders={productOrders} 
                    handleForward={handleForward} 
                    handleReturn={handleReturn} 
                    onReport={()=> setToReport(true)}
                    onLoad={() => setToLoad(true)} />
                </div>

                {/* Right Column: Contains status and dispatcher information */}
                <div className="right-column">
                    {/* Status Information */}
                    <Status params={blnum} productOrders={productOrders} />
                    {/* Dispatcher Information */}
                    <ActionLogs params={blnum} productOrders={productOrders}/>
                </div>
            </div>
        </div>

    </>);


}