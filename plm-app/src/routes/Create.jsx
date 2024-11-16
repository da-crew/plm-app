import { useEffect, useState } from "react";
import { Navigate } from "react-router";
import Header from "../components/Header";

import { useCookies } from "react-cookie";
import '../components/create.css';
import { Role, useAuthenticate } from "../users";

export default function Create() {
    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);


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
            <div className="receipt-info-container">
                {/* Left Section: Form */}
                <div className="form-section">
                    <h2>Receipt Info</h2>
                    <div className="form-group">
                        <label>Date:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>B/L No.:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Vessel:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Voy. No.:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Consignee:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>TO:</label>
                        <select>
                            <option>Select</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <button>Choose file</button>
                    </div>
                </div>

                {/* Right Section: Table */}
                <div className="table-section">
                    <div className="table-header">
                        <div>Mark & Nos.</div>
                        <div>Pkgs.</div>
                        <div>Description</div>
                        <div>Remarks</div>
                    </div>
                    <div className="table-body">
                        <div>
                            <input type="text" />
                        </div>
                        <div>
                            <input type="text" />
                        </div>
                        <div>
                            <input type="text" />
                        </div>
                        <div>
                            <input type="text" />
                        </div>
                    </div>
                </div>

                {/* Save Button */}
                <div className="save-button-section">
                    <button>SAVE</button>
                </div>
            </div>
        </div>
    </>
    );


}