import React from 'react';
import './ViewProductOrder.css';

function MainDetails({ params, productOrders }) {
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
      blnumber,
      orderDate,
      vesselName,
      voyNumber,
      cosigneeName,
      wharfReceiptImgUrl,
    } = thisProductOrder;
  
    return (
      <div className="bl-info">
        <p>
          <strong>B/L No. :</strong> {blnumber}
        </p>
        <p>
          <strong>Date :</strong> {new Date(orderDate).toLocaleDateString()}
        </p>
        <p>
          <strong>Vessel :</strong> {vesselName}
        </p>
        <p>
          <strong>VOY :</strong> {voyNumber}
        </p>
        <p>
          <strong>Consignee :</strong> {cosigneeName}
        </p>
        <img
          src={wharfReceiptImgUrl}
          alt="Document"
          className="document-image"
        />
      </div>
    );
  }
  
  export default MainDetails;