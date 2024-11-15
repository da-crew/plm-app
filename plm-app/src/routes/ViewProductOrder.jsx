import { useEffect, useState } from "react";
import { Navigate, useParams } from "react-router";
import DashboardHeader from "../components/DashboardHeader";
import Header from "../components/Header";
import '../components/ProductOrder/ViewProductOrder.css';
import { authenticate, COOKIES_NAME, Role, useAuthenticate } from "../users";
import MainDetails from "../components/ProductOrder/mainDetails"
import { useCookies } from "react-cookie"


export default function ProductOrder() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);
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
        <Header employeeName={userInfo.username} onLogout={() => setToLogin(true)} role={userInfo.role} />
        <h1>This is a ProductOrder</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <div className="center-block">
            <div className="product-order-detail-container">
                {/* Left Column: Contains main product order details, damage and load details, and action buttons */}
                <div className="left-column">

                    {/* B/L and Order Info */}
                    <MainDetails/>
                    

                    {/* Product Order Table */}
                    <div className="product-order-table">
                        <table>
                            <thead>
                                <tr>
                                    <th>Mark & Nos.</th>
                                    <th>Pkgs.</th>
                                    <th>Description</th>
                                    <th>Remarks</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>MN</td>
                                    <td>1</td>
                                    <td>MASERATI TOTAL 1 UNIT</td>
                                    <td>1.870 TON 18.280 CBM</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    {/* Damage Report and Load Details Section */}
                    <div className="report-section">
                        <div className="damage-report">
                            <h4>Damage Report</h4>
                            <p>Vehicle registration number : AB 123</p>
                            <p>Report : window broken</p>
                            <img src="damage-image.jpg" alt="Damage" className="damage-image" />
                        </div>
                        <div className="load-details">
                            <h4>Load Details</h4>
                            <p>Truck : AB 123</p>
                            <p>Contains : QW 456</p>
                        </div>
                    </div>

                    {/* Action Buttons */}
                    <div class="action-buttons">
                        <div class="add-buttons">
                            <button class="add-damage-report">Add Damage Report</button>
                            <button class="add-load-details">Add Load Details</button>
                        </div>
                        <div class="other-buttons">
                            <button class="return-dispatcher">Return to Dispatcher</button>
                            <button class="forward-exporter">Forward to Exporter</button>
                            <button class="edit-product-order">Edit Product Order</button>
                        </div>
                    </div>
                </div>

                {/* Right Column: Contains status and dispatcher information */}
                <div className="right-column">
                    {/* Status Information */}
                    <div className="status-info">
                        <p><strong>Status :</strong> Checking</p>
                    </div>

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