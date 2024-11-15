import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";
import DashboardHeader from "../components/DashboardHeader";
import { useAllProductOrders, authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import Table from "../components/Table";
import { useCookies } from "react-cookie"



export default function Dashboard() {

    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
    let [toLogin, setToLogin] = useState(false);
    let [toCreate, setToCreate] = useState(false);
    let [toManageUser, setToManageUser] = useState(false);
    let [toProductOrder, setToProductOrder] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [productOrders, succ] = useAllProductOrders();
    const [selectedItem, setSelectedItem] = useState(null);//Product Order ที่กด

    let [data, setData] = useState([])

    useEffect(() => {
        if (succ) {
            setData(productOrders)
        } else {
            console.log("error");
        }
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password")
        }


    }, [productOrders, toLogin]);
    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }



    if (toCreate) {
        return <Navigate to="/create" />
    }
    if (toManageUser) {
        return <Navigate to="/ManageUser" />
    }
    if (toProductOrder && selectedItem) {
        return <Navigate to={"/ProductOrder/" + selectedItem.blnumber} />
    }


    function handlePOClick(item) {//when click product order in list
        console.log("Item clicked", item);
        setSelectedItem(item);
        setToProductOrder(true);
    }

    return (<>
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a dashboard</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <p></p>
        <div className="center-block">
            <DashboardHeader role={userInfo.role} onCreate={() => setToCreate(true)} onManage={() => setToManageUser(true)} />

            <Table data={data} onRowClick={handlePOClick} />{/*<-- List of product order */}

        </div>

    </>);

}