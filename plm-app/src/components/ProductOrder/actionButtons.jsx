import React from 'react';
import './ViewProductOrder.css';
import axios from 'axios';
import { Role } from '../../users';

function ActionButtons({ params, productOrders, user }) {
    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    const addDamageReport = <button class="add-damage-report">Add Damage Report</button>
    const addLoadDetails = <button class="add-load-details">Add Load Details</button>

    const returnDispatcher = <button class="return-dispatcher">Return to Dispatcher</button>
    const returnChecker = <button class="return-dispatcher">Return to Checker</button>

    const forwardChecker = <button class="forward">Forward to Checker</button>
    const forwardExporter = <button class="forward-exporter">Forward to Exporter</button>

    const edit = <button className="edit-product-order">Edit Product Order</button>

    const confirm = <button class="forward-exporter">Confirm Export</button>

    if (thisProductOrder.statusName == "CHECKING") {
        if ((user.role == Role.CHECKER && user.username == thisProductOrder.checker) || (user.role == Role.ADMIN)) {// status = checking , role = checker, current account username = this productorder checker     or  status= checking  role = admin
            return (
                <div class="action-buttons">
                    <div class="add-buttons">
                        {addDamageReport}
                        {addLoadDetails}
                    </div>
                    <div class="other-buttons">
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
                <div class="action-buttons">
                    <div class="other-buttons">
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
                <div class="action-buttons">
                    <div class="other-buttons">
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
                <div class="action-buttons">
                    <div class="other-buttons">
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