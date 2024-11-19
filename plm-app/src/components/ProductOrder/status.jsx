import React from 'react';
import './ViewProductOrder.css';

function Status({ params, productOrders }) {
    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    return (
        <div className="status-info">
            <p><strong>Status :</strong> {thisProductOrder.statusName}</p>
        </div>
    );
}

export default Status;