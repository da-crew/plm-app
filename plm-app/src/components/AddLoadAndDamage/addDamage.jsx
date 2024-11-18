import { useState } from 'react';
import axios from 'axios';
import { useAuthenticate, WEB_SERVICE_URL } from '../../users';
//import './addLoad.css'
import { useNavigate } from "react-router-dom";

const AddLoadComponent = ({blnum}) => {
    const [licensePlate, setLicensePlate] = useState("");
    const [reportDetails, setReportDetails] = useState("");
    const [file, setFile] = useState(null);
  
    const handleFileChange = (event) => {
      setFile(event.target.files[0]);
    };
  
    const handleSubmit = () => {
      // Logic to handle form submission
      console.log("License Plate:", licensePlate);
      console.log("Report Details:", reportDetails);
      console.log("Uploaded File:", file);
      alert("Damage recorded successfully!");
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
        <button onClick={handleSubmit} className="submit-button">
          Record Damage
        </button>
      </div>
    );

}


export default AddLoadComponent;