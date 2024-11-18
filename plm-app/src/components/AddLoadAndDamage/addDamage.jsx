import { useState } from 'react';
import axios from 'axios';
import { useAuthenticate, WEB_SERVICE_URL } from '../../users';
import './addLoadandDamage.css'
import { useNavigate } from "react-router-dom";
import { useParams } from 'react-router-dom';

const AddLoadComponent = () => {
    const [licensePlate, setLicensePlate] = useState("");
    const [reportDetails, setReportDetails] = useState("");
    const [file, setFile] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false); // **Added state for submission status**
    const navigate = useNavigate();
    let [validCreds, userInfo, password] = useAuthenticate();
    const {blnum} = useParams();


    const handleFileChange = (event) => {
        setFile(event.target.files[0]); // **Handles file selection**
    };

    const handleSubmit = async () => {
        if (!licensePlate || !reportDetails) { // **Validates mandatory fields**
            alert("License plate and details are required!");
            return;
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
        try {
            const carId = /* Logic to retrieve or map carId from licensePlate */ 1; // **Placeholder for carId**
            const response = await axios.post(
                `${WEB_SERVICE_URL}/product-orders/${blnum}/cars/${carId}/damage-report`, // **API endpoint URL**
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data", // **Specifies form-data content type**
                        "Access-Control-Allow-Origin": "*"
                    },
                }
            );

            if (response.status === 201) { // **Checks for successful creation**
                alert("Damage recorded successfully!");
                navigate("/success"); // **Redirects on success**
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
                <label htmlFor="licensePlate">Please enter vehicle's license plate number:</label>
                <input
                    type="text"
                    id="licensePlate"
                    value={licensePlate}
                    onChange={(e) => setLicensePlate(e.target.value)}
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

export default AddLoadComponent;

// const AddDamageComponent = () => {
//     const [licensePlate, setLicensePlate] = useState("");
//     const [reportDetails, setReportDetails] = useState("");
//     const [file, setFile] = useState(null);
//     const blnum = useParams();

//     const handleFileChange = (event) => {
//         setFile(event.target.files[0]);
//     };

//     const handleSubmit = async () => {
//         if (!licensePlate || !file) { // **Validates mandatory fields**
//             alert("License plate and file are required!");
//             return;
//         }
//         console.log("License Plate:", licensePlate);
//         console.log("Report Details:", reportDetails);
//         console.log("Uploaded File:", file);
//         alert("Damage recorded successfully!");
//         try {
//             const carId = /* Logic to retrieve or map carId from licensePlate */ 1; // **Placeholder for carId**
//             const response = await axios.post(
//                 `${WEB_SERVICE_URL}/product-orders/${blnum}/cars/${carId}/damage-report`, // **API endpoint URL**
//                 formData,
//                 {
//                     headers: {
//                         "Content-Type": "multipart/form-data", // **Specifies form-data content type**
//                     },
//                 }
//             );

//             if (response.status === 201) { // **Checks for successful creation**
//                 alert("Damage recorded successfully!");
//                 navigate("/success"); // **Redirects on success**
//             } else {
//                 alert(`Error: ${response.data}`); // **Handles API errors**
//             }
//         } catch (error) {
//             console.error("Failed to record damage report:", error);
//             alert("An error occurred while submitting the form. Please try again."); // **Error feedback**
//         }
//     };

//     return (
//         <div className="damage-report-container">
//             <h3>Damage Report{blnum}</h3>
//             <div className="form-group">
//                 <label htmlFor="licensePlate">Please enter vehicle's license plate number:</label>
//                 <input
//                     type="text"
//                     id="licensePlate"
//                     value={licensePlate}
//                     onChange={(e) => setLicensePlate(e.target.value)}
//                     placeholder="Enter license plate"
//                     className="form-input"
//                 />
//             </div>
//             <div className="form-group">
//                 <label htmlFor="fileUpload">Upload evidence:</label>
//                 <input
//                     type="file"
//                     id="fileUpload"
//                     onChange={handleFileChange}
//                     className="form-input"
//                 />
//             </div>
//             <div className="form-group">
//                 <label htmlFor="reportDetails">Enter report details:</label>
//                 <textarea
//                     id="reportDetails"
//                     value={reportDetails}
//                     onChange={(e) => setReportDetails(e.target.value)}
//                     placeholder="Enter report details"
//                     className="form-textarea"
//                 ></textarea>
//             </div>
//             <button onClick={handleSubmit} className="submit-button">
//                 Record Damage
//             </button>
//         </div>
//     );

// }


// export default AddDamageComponent;