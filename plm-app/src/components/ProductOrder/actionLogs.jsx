import React from 'react';
import './ViewProductOrder.css';
import './actionLogs.css';

function ActionLogs({ params, productOrders }) {
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    const { actionLogs } = thisProductOrder;

    return (
        <div className="action-logs-container">
            <h4 id='title'>Action Logs</h4>
            {actionLogs && actionLogs.length > 0 ? ( 
                actionLogs.map((log) => (
                    <table key={log.id} className="action-log">
                        <tr>
                            <th>Timestamp</th>
                            <th className='info'>{new Date(log.timestamp).toLocaleString()}</th>
                        </tr>
                        <tr>
                            <th>Action</th>
                            <th className='info'>{log.actionText}</th>
                        </tr>
                        <tr>
                            <th>Employee</th>
                            <th className='info'>{log.employee}</th>
                        </tr>
                    </table>
                ))
            ) : (
                <p>No action logs available for this product order.</p>
            )}
        </div>
    );
}
export default ActionLogs;