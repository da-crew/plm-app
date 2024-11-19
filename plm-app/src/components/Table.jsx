import React from 'react';
import './Table.css';

function TableComponent(props) {
    function handleRowClick(item) {
        console.log("Row clicked:", item);
        if (props.onRowClick) {
            props.onRowClick(item); // Call the parent's callback
        }
    }

    return (
        <div className="table-container">
            <table className="table">
                <thead>
                    <tr>
                        <th className="cell table-header">B/L No.</th>
                        <th className="cell table-header">Date</th>
                        <th className="cell table-header">Checker</th>
                        <th className="cell table-header">Dispatcher</th>
                        <th className="cell table-header">Status</th>
                    </tr>
                </thead>
                <tbody>
                    {props.data.length > 0 ? (
                        props.data.map((item, index) => (
                            <tr
                                key={index}
                                className="clickable-row"
                                onClick={() => handleRowClick(item)}
                            >
                                <td className="cell"> {item.blnumber}</td>
                                <td className="cell"> {item.orderDate}</td>
                                <td className="cell"> {item.checker}</td>
                                <td className="cell"> {item.dispatcher}</td>
                                <td className="cell"> {item.statusName}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="4" className="cell empty-row">
                                Empty
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

// // Sample data to test the component
// const sampleData = [
//   { blNo: "12345", date: "2024-11-04", cNo: "C001", status: "Pending" },
//   { blNo: "12346", date: "2024-11-04", cNo: "C002", status: "Completed" },
// ];

// //just for Test
// const App = () => {
//   return <TableComponent data={sampleData} />;
// };

// //
export default TableComponent;