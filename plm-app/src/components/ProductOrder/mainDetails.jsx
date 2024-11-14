import React from 'react';
import './ViewProductOrder.css';

function MainDetails() {
    
    return (
    <div className="bl-info">
        <p><strong>B/L No. :</strong> NYKS630512660</p>
        <p><strong>Date :</strong> 03/10/2023</p>
        <p><strong>Vessel :</strong> PositiveLeader</p>
        <p><strong>VOY :</strong> 114</p>
        <p><strong>Consignee :</strong> Modena Motorwork CO.LTD</p>
        <img src="document-image.jpg" alt="Document" className="document-image" />
    </div>
    );
}
export default MainDetails;