import { useState } from 'react';
import axios from 'axios';
import { useAuthenticate, WEB_SERVICE_URL } from '../../users';
import './addLoadandDamage.css'
import { useNavigate } from "react-router-dom";


const AddLoadComponent = ({ blnum }) => {
    const [truckLicense, setTruckLicense] = useState("");
    const [vehicleLicense, setVehicleLicense] = useState("");
    const [vehicles, setVehicles] = useState([]);
    let [validCreds, userInfo, password] = useAuthenticate();

    const navigate = useNavigate(); // React Router's hook for navigation
    const goBack = () => {
        navigate(-2); // Go back to the previous page
    };

    const addVehicle = () => {
        if (vehicleLicense.trim() && vehicles.length < 8) {
            setVehicles([...vehicles, vehicleLicense.trim()]);
            setVehicleLicense("");
        }
    };

    const deleteVehicle = (index) => {
        setVehicles(vehicles.filter((_, i) => i !== index));
    };

    // const confirmLoadDetails = () => {
    //     alert(`Truck License: ${truckLicense}, Vehicles: ${vehicles.join(", ")}`);
    // };
    const confirmLoadDetails = async () => {
        try {
            const payload = {
                truckNumber: truckLicense,
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
                console.log(`Error: ${error.response.data}`);
            } else {
                alert("Error: Unable to connect to the server.");
            }
        }

        try {

            for (const vehicle of vehicles) {
                const payload = {
                    caller: {
                        username: userInfo.username,
                        password: password,
                    },
                    truckNumber: truckLicense,
                    carModel: vehicle, // Send one car at a time
                };

                // Make the POST request for each vehicle
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
            }

            goBack();
            alert("All vehicles added successfully!");
        } catch (error) {
            // Handle error response
            if (error.response) {
                alert(`Error: ${error.response.data}`);
            } else {
                alert("Error: Unable to connect to the server.");
            }
        }
    };

    return (
        
            <div className="load-details-container">
                <h3>Damage Report</h3>
                <div className="input-group">
                    <label>Please enter truck license plate number:</label>
                    <input
                        type="text"
                        value={truckLicense}
                        onChange={(e) => setTruckLicense(e.target.value)}
                        className="input-field"
                    />
                </div>
                <div className="input-group">
                    <label>
                        Please enter vehicles license plate number (max 8):
                    </label>
                    <input
                        type="text"
                        value={vehicleLicense}
                        onChange={(e) => setVehicleLicense(e.target.value)}
                        className="input-field"
                    />
                    <button onClick={addVehicle} className="add-button">
                        Add
                    </button>
                </div>
                <div className="vehicle-list">
                    <label>Contains:</label>
                    <ul className="vehicle-ul">
                        {vehicles.map((vehicle, index) => (
                            <li key={index} className="vehicle-li">
                                {vehicle}
                                <button
                                    onClick={() => deleteVehicle(index)}
                                    className="delete-button"
                                >
                                    Delete car
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
                <button onClick={confirmLoadDetails} className="confirm-button">
                    Confirm Load details
                </button>
            </div>
    );
};
export default AddLoadComponent