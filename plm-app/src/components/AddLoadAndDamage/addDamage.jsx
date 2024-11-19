import { useState } from 'react';
import axios from 'axios';
import { useAuthenticate, WEB_SERVICE_URL } from '../../users';
import './addLoadandDamage.css'
import { useNavigate } from "react-router-dom";
import { useParams } from 'react-router-dom';
import { useAllProductOrders } from '../../users';


const AddDamageComponent = () => {
    const [carModel, setCarModel] = useState("");/////carmodel/
    const [reportDetails, setReportDetails] = useState("");
    const [file, setFile] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false); // **Added state for submission status**
    const navigate = useNavigate();
    let [validCreds, userInfo, password] = useAuthenticate();
    const { blnum } = useParams();
    let [productOrders, setProductOrders, succ] = useAllProductOrders();

    const getCarIdByBLAndModel = (blnumber, modelName) => {
        // Find the product order by BL number
        const productOrder = productOrders.find(order => order.blnumber === blnumber);

        // If the product order exists, find the first matching car
        if (productOrder) {
            const car = productOrder.cars.find(car => car.truck === "none" && car.modelName === modelName);
            return car ? car.id : null; // Return the ID or null if not found
        }

        // Return null if no matching product order is found
        return null;
    };
    const isTruckNoneExist = (blnumber) => {
        // Find the product order by BL number
        const productOrder = productOrders.find(order => order.blnumber === blnumber);

        // If the product order exists, check if any car has truck "none"
        if (productOrder) {
            return productOrder.cars.some(car => car.truck === "none");
        }

        // Return false if no matching product order is found
        return false;
    };
    function isThisCarNotExist( blnumber, carModelName) {
        // Find the product order with the matching blnumber
        const productOrder = productOrders.find(order => order.blnumber === blnumber);
        
        // If no such product order exists, return true (car doesn't exist for non-existent blnumber)
        if (!productOrder) return true;
    
        // Check if the car with the given modelName exists in the cars array
        return !productOrder.cars.some(car => car.modelName === carModelName);
    }

    const handleFileChange = (event) => {
        setFile(event.target.files[0]); // **Handles file selection**
    };

    const handleSubmit = async () => {
        if (!carModel || !reportDetails) { // **Validates mandatory fields**
            alert("License plate and details are required!");
            return;
        }
        if (!isTruckNoneExist(blnum)) {/////////////////////////////////////////////////////////////////////
            try {

                const payload = {
                    truckNumber: "none",
                    caller: {
                        username: userInfo.username,
                        password: password,
                    },
                };
                const response = await axios.post(
                    WEB_SERVICE_URL + "/trucks",
                    payload,
                    {
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                );
            } catch (error) {
                if (error.response) {
                  //  console.log(`Error: add truck   ${error.response.data}`);
                } else {
                    alert("Error: Unable to connect to the server.");
                }
            }
        }
        
        try {///// add car ///////////////////////////////////////////////////////////////////////

            const payload = {
                caller: {
                    username: userInfo.username,
                    password: password,
                },
                truckNumber: "none",
                carModel: carModel, // Send one car 

            };
            const response = await axios.post(
                WEB_SERVICE_URL + `/product-orders/${blnum}/cars`,
                payload,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            console.log(`Added vehicle: ${vehicle}`, response.data);
        } catch (error) {
            // Handle error response
            if (error.response) {
                alert(`Error: ${error.response.data}`);
            } else {
                alert("Error: Unable to connect to the server.");
            }
        }
    
        const formData = new FormData();
        formData.append("report", reportDetails); // Adds report details to form-data
        formData.append(
            "caller",
            JSON.stringify({
                username: userInfo.username,
                password: password
            })
        ); // Serializes the caller object into a JSON string
        formData.append("image", file); // Adds the file to form-data

        setIsSubmitting(true); // **Disables the button during submission**
        try {///////////////REPORT//////////REPORT/////////////////REPORT////////////REPORT////////////

            const carId = getCarIdByBLAndModel(blnum, carModel);
            alert(carId)
            const response = await axios.post(
                `${WEB_SERVICE_URL}/product-orders/${blnum}/cars/${carId}/damage-report`, // **API endpoint URL**
                formData,
                {
                    headers: {
                        //"Content-Type": "multipart/form-data", // **Specifies form-data content type**
                        "Access-Control-Allow-Origin": "*"
                    },
                }
            );

            if (response.status === 201) { // **Checks for successful creation**
                alert("Damage recorded successfully!");
                //navigate("/success"); // **Redirects on success**
            } else {
                alert(`Error: ${response.data}`); // **Handles API errors**
            }
        } catch (error) {
            console.error("Failed to record damage report:", error);
            alert("An error occurred while submitting the form. Please try again."); // **Error feedback**
        } finally {
            setIsSubmitting(false); // **Re-enables the button**
        }
    };

    return (
        <div className="damage-report-container">
            <h3>Damage Report</h3>
            <div className="form-group">
                <label htmlFor="licensePlate">Please enter car's ID:</label>
                <input
                    type="text"
                    id="licensePlate"
                    value={carModel}
                    onChange={(e) => setCarModel(e.target.value)}
                    placeholder="Enter license plate"
                    className="form-input"
                />
            </div>
            <div className="form-group">
                <label htmlFor="fileUpload">Upload evidence:</label>
                <input
                    type="file"
                    id="fileUpload"
                    onChange={handleFileChange}
                    className="form-input"
                />
            </div>
            <div className="form-group">
                <label htmlFor="reportDetails">Enter report details:</label>
                <textarea
                    id="reportDetails"
                    value={reportDetails}
                    onChange={(e) => setReportDetails(e.target.value)}
                    placeholder="Enter report details"
                    className="form-textarea"
                ></textarea>
            </div>
            <button
                onClick={handleSubmit}
                className="submit-button"
                disabled={isSubmitting} // **Button is disabled while submitting**
            >
                {isSubmitting ? "Submitting..." : "Record Damage"}
            </button>
        </div>
    );
};

export default AddDamageComponent;
