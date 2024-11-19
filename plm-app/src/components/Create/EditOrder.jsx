import React, { useState } from 'react';
import './Create.css'
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import axios from "axios";
import { useAuthenticate, WEB_SERVICE_URL } from '../../users';



const EditOrder = ({ productOrder , productOrders, params,handleOut}) => {
    
const thisProductOrder = productOrders.find(
    (item) => item.blnumber === params
  );
  // If no matching product order is found
  if (!thisProductOrder) {
    return <p>No matching product order found.</p>;
  }
 

    


    
    const [file, setFile] = useState(null);
    const [checkers, setCheckers] = React.useState([]);
    const [dispatchers, setDispatchers] = React.useState([]);
    let [validCreds, userInfo, password] = useAuthenticate();
    const [formData, setFormData] = useState({
        orderDate: thisProductOrder?.orderDate || "",
        BLNumber: thisProductOrder?.blnumber || "",
        vesselName: thisProductOrder?.vesselName || "",
        voyNumber: thisProductOrder?.voyNumber || "",
        cosigneeName: thisProductOrder?.cosigneeName || "",// cosignee consignee
        markAndNumsText: thisProductOrder?.markAndNumsText || "",
        packagesText: thisProductOrder?.packagesText || "",
        description: thisProductOrder?.description || "",
        remarks: thisProductOrder?.remarks || "",
        checker: thisProductOrder?.checker || "",
        dispatcher: thisProductOrder?.dispatcher || "",
    });
    const handleFileChange = (event) => {
        setFile(event.target.files[0]); // **Handles file selection**
    };
    const handleDateChange = (date) => {
        // Convert the selected date to ISO format and update state
        setFormData({
            ...formData,
            orderDate: date ? date.toISOString() : null,
        });
    };

    React.useEffect(() => {
        async function fetchCheckers() {
            try {
                const response = await fetch(WEB_SERVICE_URL + '/users/checkers'); // API to fetch checkers
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                const data = await response.json();
                setCheckers(data); // Update the state with fetched checkers
            } catch (error) {
                console.error('Error fetching checkers:', error);
            }
        }

        fetchCheckers(); // Trigger fetch on mount
    }, []);
    React.useEffect(() => {
        async function fetchDispatchers() {
            try {
                const response = await fetch(WEB_SERVICE_URL + '/users/dispatchers'); // API to fetch 
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                const data = await response.json();
                setDispatchers(data); // Update the state with fetched dispatchers
            } catch (error) {
                console.error('Error fetching dispatchers:', error);
            }
        }

        fetchDispatchers(); // Trigger fetch on mount
    }, []);

    // function populateCheckerOptions(checkers) {
    //     // Find the <select> element
    //     const checkerSelect = document.querySelector('select[name="checker"]');

    //     // Clear existing options except the "Select" placeholder
    //     checkerSelect.innerHTML = `
    //         <option value="">Select</option>
    //     `;

    //     // Add an <option> for each checker
    //     checkers.forEach(checker => {
    //         const option = document.createElement('option');
    //         option.value = checker.id; // Use the checker's ID or any unique identifier
    //         option.textContent = `${checker.firstname} ${checker.lastname}`;
    //         checkerSelect.appendChild(option);
    //     });
    // }



    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSave = async() => {
        const payload = {
            
            caller: {
                username: userInfo.username,
                password: password, 
            },
            productOrder: {
                BLNumber: formData.BLNumber,
                orderDate: formData.orderDate,
                vesselName: formData.vesselName,
                voyNumber: formData.voyNumber,
                cosigneeName: formData.cosigneeName,
                markAndNumsText: formData.markAndNumsText,
                packagesText: formData.packagesText,
                description: formData.description,
                remarks: formData.remarks,
            },
            checker: formData.checker, // Checker's username
            dispatcher: formData.dispatcher//formData.dispatcher
        };
        console.log(formData.markAndNumsText)
        try {
            const response = await axios.post(
                WEB_SERVICE_URL+ "/product-orders/"+params+"/edit",
                payload,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );
            console.log(`Response status: ${response.status}`);
            alert("Order updated successfully!");
            handleOut();
        } catch (error) {
            console.error("Error updating order:");
        
            if (error.response) { // Server responded with a status other than 2xx
                console.error(`Status code: ${error.response.status}`);
                console.error(`Response data: ${JSON.stringify(error.response.data)}`);
                alert(`There was an error with the server: ${error.response.data.message || 'Please try again later.'}`);
            } else if (error.request) { // No response received
                console.error(`No response received: ${error.request}`);
                alert("The server did not respond. Please check your internet connection and try again.");
            } else { // Other errors like request setup or network issues
                console.error(`Error message: ${error.message}`);
                alert(`An error occurred: ${error.message}`);
            }
        }
    //    }
    //     let imgFormData = new FormData()
    //     imgFormData.append("file", file);
    //     imgFormData.append("caller", JSON.stringify({
    //         username: userInfo.username,
    //         password: password
    //     }));
    //     for (let pair of imgFormData.entries()) {
    //         console.log(pair[0], pair[1]);
    //     }

    //     try {
    //         let response = await axios.post(`${WEB_SERVICE_URL}/product-orders/${formData.BLNumber}/set-image`, 
    //             imgFormData,
    //             {
    //                 headers: {
    //                     //"Content-Type": "multipart/form-data", // **Specifies form-data content type**
    //                     "Access-Control-Allow-Origin": "*"
    //                 },
    //             }
    //         );
    //     } catch (error) {

    //     }
    };

    return (
        <div className="receipt-info-container">
            {/* Left Section: Form */}
            <div className="form-section">
                <h2>Receipt Info</h2>
                <div className="form-group">
                    <label>Date:</label>
                    {/* <input
                        type="text"
                        name="orderDate"
                        value={formData.orderDate}
                        onChange={handleInputChange}
                    /> */}
                    <DatePicker
                        selected={formData.orderDate ? new Date(formData.orderDate) : null}
                        onChange={handleDateChange}
                        showTimeSelect
                        dateFormat="yyyy-MM-dd'T'HH:mm:ssXXX"
                        timeFormat="HH:mm"
                    />
                </div>
                <div className="form-group">
                    <label>B/L No.:</label>
                    <input
                        type="text"
                        name="BLNumber"
                        value={formData.BLNumber}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="form-group">
                    <label>Vessel:</label>
                    <input
                        type="text"
                        name="vesselName"
                        value={formData.vesselName}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="form-group">
                    <label>Voy. No.:</label>
                    <input
                        type="text"
                        name="voyNumber"
                        value={formData.voyNumber}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="form-group">
                    <label>Consignee:</label>
                    <input
                        type="text"
                        name="cosigneeName"
                        value={formData.cosigneeName}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="form-group">
                    <label>CHECKER:</label>
                    <select
                        name="checker"
                        value={formData.checker}
                        onChange={handleInputChange}
                    >
                        <option value="">Select</option>
                        {checkers.map((checker) => (

                            <option key={checker.id} value={checker.username}>
                                {checker.firstname} {checker.lastname}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label>DISPATCHER:</label>
                    <select
                        name="dispatcher"
                        value={formData.dispatcher}
                        onChange={handleInputChange}
                    >
                        <option value="">Select</option>
                        {dispatchers.map((dispatcher) => (
                            <option key={dispatcher.id} value={dispatcher.username}>
                                {dispatcher.firstname} {dispatcher.lastname}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                <label htmlFor="fileUpload">Choose file</label>
                <input
                    type="file"
                    id="fileUpload"
                    onChange={handleFileChange}
                    className="form-input"
                />
                </div>
            </div>

            {/* Right Section: Table */}
            <div className="table-section">
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(4, 1fr)",
                        backgroundColor: "#007bff",
                        color: "white",
                        padding: "10px",
                        borderRadius: "4px 4px 0 0",
                        fontSize: "14px",
                        fontWeight: "bold",
                        textAlign: "center",
                    }}
                >
                    <div>Mark & Nos.</div>
                    <div>Pkgs.</div>
                    <div>Description</div>
                    <div>Remarks</div>
                </div>
                <div className="table-body">
                    <div>
                        <input
                            type="text"
                            name="markAndNumsText"
                            value={formData.markAndNumsText}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div>
                        <input
                            type="text"
                            name="packagesText"
                            value={formData.packagesText}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div>
                        <input
                            type="text"
                            name="description"
                            value={formData.description}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div>
                        <input
                            type="text"
                            name="remarks"
                            value={formData.remarks}
                            onChange={handleInputChange}
                        />
                    </div>
                </div>
            </div>

            {/* Save Button */}
            <div className="save-button-section">
                <button onClick={handleSave}>SAVE</button>
            </div>
        </div>
    );
};

export default EditOrder;