import React from 'react';
import './ViewProductOrder.css';

function OrderTable({ params, productOrders }) {
    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    // Destructure data for easier usage
    const {
        mark_nos,
        pkgs,
        description,
        remarks
    } = thisProductOrder;

    return (
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
                        <td>MN {mark_nos}</td>
                        <td>1 {pkgs}</td>
                        <td>MASERATI TOTAL 1 UNIT {description}</td>
                        <td>1.870 TON 18.280 CBM {remarks}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
}

export default OrderTable;