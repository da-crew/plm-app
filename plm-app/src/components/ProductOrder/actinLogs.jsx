// ActionLogs.jsx
import React from 'react';
import './ViewProductOrder.css';
import { useParams } from 'react-router';

function ActionLogs({ params, productOrders }) {
    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
      );
    
      // If no matching product order is found
      if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
      }

    // return (<div>
    //     <h4>Action Logs</h4>
    //     <div className="action-logs">
            
    //         {actionLogs.map((log) => (
    //             <div key={log.id} className="action-log-item">
    //                 <p><strong>Timestamp:</strong> {new Date(log.timestamp).toLocaleString()}</p>
    //                 <p><strong>Action:</strong> {log.actionText}</p>
    //                 <p><strong>Employee:</strong> {log.employee}</p>
    //             </div>
    //         ))}
    //     </div>
    //     </div>
    // );
    const { actionLogs } = thisProductOrder; // Extract `actionLogs` dynamically

    return (
        <div className="action-logs"> {/*  Added wrapper for CSS styling */}
            <h4>Action Logs</h4>
            {actionLogs && actionLogs.length > 0 ? ( // Added conditional rendering for `actionLogs`
                actionLogs.map((log) => (
                    <div key={log.id} className="action-log-item dispatcher-info">
                        <p>
                            <strong>Timestamp:</strong>{' '}
                            {new Date(log.timestamp).toLocaleString()} 
                        </p>
                        <p>
                            <strong>Action:</strong> {log.actionText} 
                        </p>
                        <p>
                            <strong>Employee:</strong> {log.employee} 
                        </p>
                    </div>
                ))
            ) : (
                <p>No action logs available for this product order.</p> 
            )}
        </div>
    );
}
export default ActionLogs;