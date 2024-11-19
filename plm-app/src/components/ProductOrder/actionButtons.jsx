import React from 'react';
import './ViewProductOrder.css';
import axios from 'axios';
import { Role } from '../../users';
import { useAuthenticate } from '../../users';

// function ActionButtons(props)
function ActionButtons({ params, productOrders, user, handleForward, handleReturn, onReport, onLoad, onEdit }) {
    let [validCreds, userInfo, password] = useAuthenticate();
    


    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    const addDamageReport = <button className="add-damage-report" onClick={onReport}>Add Damage Report</button>
    const addLoadDetails = <button className="add-load-details" onClick={onLoad}>Add Load Details</button>

    const returnDispatcher = <button className="return-dispatcher"onClick={handleReturn}>Return to Dispatcher</button>
    const returnChecker = <button className="return-dispatcher"onClick={handleReturn}>Return to Checker</button>

    const forwardChecker = <button className="forward"onClick={handleForward}>Forward to Checker</button>
    const forwardExporter = <button className="forward-exporter"onClick={handleForward}>Forward to Exporter</button>
    const confirm = <button className="forward-exporter"onClick={handleForward}>Confirm Export</button>

    const edit = <button className="edit-product-order"onClick={onEdit}>Edit Product Order</button>


    if (thisProductOrder.statusName === "CHECKING") {
        if ((user.role === Role.CHECKER && user.username === thisProductOrder.checker) || (user.role === Role.ADMIN)) {// status = checking , role = checker, current account username = this productorder checker     or  status= checking  role = admin
            return (
                <div className="action-buttons">
                    <div className="add-buttons">
                        {addDamageReport}
                        {addLoadDetails}
                    </div>
                    <div className="other-buttons">
                        {returnDispatcher}
                        {forwardExporter}
                        {user.role === Role.ADMIN && (
                            edit
                        )}
                    </div>
                </div>
                
            );
        }
    }
    else if (thisProductOrder.statusName == "REPORTED") {
        if ((user.role == Role.DISPATCHER && user.username == thisProductOrder.dispatcher) || (user.role == Role.ADMIN)) {
            return (
                <div className="action-buttons">
                    <div className="other-buttons">
                        {forwardChecker}
                        {user.role === Role.ADMIN && (
                            edit
                        )}
                    </div>
                </div>
            );
        }
    }
    else if (thisProductOrder.statusName == "EXPORTING") {
        if ((user.role == Role.EXPORTER) || (user.role == Role.ADMIN)) {
            return (
                <div className="action-buttons">
                    <div className="other-buttons">
                        {returnChecker}
                        {confirm}
                        {user.role === Role.ADMIN && (
                            edit
                        )}
                    </div>
                </div>
            );
        }
    }
    else if (thisProductOrder.statusName == "FINISHED") {
        if ((user.role == Role.ADMIN)) {
            return (
                <div className="action-buttons">
                    <div className="other-buttons">
                        {edit}
                    </div>
                </div>
            );
        }
    }
    else {
        return null;
    }

}

export default ActionButtons;