import React from 'react';
import './ViewProductOrder.css';

function OrderTable({ params, productOrders }) {
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    const { mark_nos, pkgs, description, remarks } = thisProductOrder;

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
                        <td>{mark_nos ?? "None"}</td>
                        <td>{pkgs ?? "None"}</td>
                        <td>{description ?? "None"}</td>
                        <td>{remarks ?? "None"}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
}

export default OrderTable;