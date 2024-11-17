import React, { useState } from 'react';
import './Create.css'
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


// function ReceiptDetail() {
//     const [formData, setFormData] = useState({
//         caller: "",
//         checker: "",
//         productOrder: {
//             BLNumber: "",
//             orderDate: "",
//             vesselName: "",
//             voyNumber: "",
//             cosigneeName: "",
//         },
//     });

//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         try {
//             const response = await axios.post("/product-orders/create", formData);
//             if (response.status === 200) {
//                 setMessage("Product order created successfully!");
//             }
//         } catch (error) {
//             if (error.response) {
//                 // Server responded with a status code other than 2xx
//                 setMessage(`Error: ${error.response.data}`);
//             } else if (error.request) {
//                 // No response received from server
//                 setMessage("Error: No response from server.");
//             } else {
//                 // Other errors
//                 setMessage(`Error: ${error.message}`);
//             }
//         }
//     };

//     return (
//         <div className="receipt-info-container">
//             {/* Left Section: Form */}
//             <div className="form-section">
//                 <h2>Receipt Info</h2>
//                 <div className="form-group">
//                     <label>Date:</label>
//                     <input 
//                     type="text" 
//                     value={productOrder.orderDate}
//                      />
//                 </div>
//                 <div className="form-group">
//                     <label>B/L No.:</label>
//                     <input type="text" />
//                 </div>
//                 <div className="form-group">
//                     <label>Vessel:</label>
//                     <input type="text" />
//                 </div>
//                 <div className="form-group">
//                     <label>Voy. No.:</label>
//                     <input type="text" />
//                 </div>
//                 <div className="form-group">
//                     <label>Consignee:</label>
//                     <input type="text" />
//                 </div>
//                 <div className="form-group">
//                     <label>TO:</label>
//                     <select>
//                         <option>Select</option>
//                     </select>
//                 </div>
//                 <div className="form-group">
//                     <button>Choose file</button>
//                 </div>
//             </div>

//             {/* Right Section: Table */}
//             <div className="table-section">
//                 <div
//                     style={{
//                         display: "grid",
//                         gridTemplateColumns: "repeat(4, 1fr)",
//                         backgroundColor: "#007bff",
//                         color: "white",
//                         padding: "10px",
//                         borderRadius: "4px 4px 0 0",
//                         fontSize: "14px",
//                         fontWeight: "bold",
//                         textAlign: "center",
//                     }}
//                 >
//                     <div>Mark & Nos.</div>
//                     <div>Pkgs.</div>
//                     <div>Description</div>
//                     <div>Remarks</div>
//                 </div>
//                 <div className="table-body">
//                     <div>
//                         <input type="text" />
//                     </div>
//                     <div>
//                         <input type="text" />
//                     </div>
//                     <div>
//                         <input type="text" />
//                     </div>
//                     <div>
//                         <input type="text" />
//                     </div>
//                 </div>
//             </div>

//             {/* Save Button */}
//             <div className="save-button-section">
//                 <button>SAVE</button>
//             </div>
//         </div>
//     );
// }
// export default ReceiptDetail;

import axios from "axios";
import { useAuthenticate } from '../../users';



const ReceiptInfo = ({ productOrder }) => {

    const handleDateChange = (date) => {
        // Convert the selected date to ISO format and update state
        setFormData({
            ...formData,
            orderDate: date ? date.toISOString() : null,
        });
    };

    const [checkers, setCheckers] = React.useState([]);
    let [validCreds, userInfo, password] = useAuthenticate();
    const [formData, setFormData] = useState({
        orderDate: productOrder?.orderDate || "",
        BLNumber: "",
        vesselName: "",
        voyNumber: "",
        cosigneeName: "",
        markAndNumsText: "",
        packagesText: "",
        description: "",
        remarks: "",
        checker: "",
    });


    React.useEffect(() => {
        async function fetchCheckers() {
            try {
                const response = await fetch('http://localhost:8080/checkers'); // API to fetch checkers
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

    function populateCheckerOptions(checkers) {
        // Find the <select> element
        const checkerSelect = document.querySelector('select[name="checker"]');

        // Clear existing options except the "Select" placeholder
        checkerSelect.innerHTML = `
            <option value="">Select</option>
        `;

        // Add an <option> for each checker
        checkers.forEach(checker => {
            const option = document.createElement('option');
            option.value = checker.id; // Use the checker's ID or any unique identifier
            option.textContent = `${checker.firstname} ${checker.lastname}`;
            checkerSelect.appendChild(option);
        });
    }



    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSave = async () => {
        const payload = {
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
            caller: {
                username: userInfo.username, // Replace with dynamic values if needed
                password: password, // Replace with dynamic values if needed
            },
            checker: formData.checker, // Checker's username
        };

        try {
            const response = await axios.post(
                "http://localhost:8080/product-orders/create",
                payload,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            if (response.status === 200) {
                alert("Product Order Created Successfully!");
            } else {
                alert(`Error: ${response.status}`);
            }
        } catch (error) {
            if (error.response) {
                alert(`Error: ${error.response.status} - ${error.response.data}`);
            } else {
                console.error("Error:", error.message);
            }
        }
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
                    <label>TO:</label>
                    <select
                        name="checker"
                        value={formData.checker}
                        onChange={handleInputChange}
                    >
                        <option value="">Select</option>
                        <option value="carlos_H">Carlos Hernandez</option>
                        {checkers.map((checker) => (

                            <option key={checker.id} value={checker.username}>
                                {checker.firstname} {checker.lastname}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <button>Choose file</button>
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
            <p>{userInfo.username}</p>
            <p>{password}</p>

        </div>
    );
};

export default ReceiptInfo;