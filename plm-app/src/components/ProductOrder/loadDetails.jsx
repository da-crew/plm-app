
import React from 'react';
import './ViewProductOrder.css';
import axios from 'axios'; // Assuming Axios is used for API calls
import { useAuthenticate, WEB_SERVICE_URL } from '../../users';

function LoadDetails({ params, productOrders, onDeleteCar }) {
    let [validCreds, userInfo, password] = useAuthenticate();

    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    // Group cars by truck
    const trucks = thisProductOrder.cars.reduce((acc, car) => {
        if (!acc[car.truck]) {
            acc[car.truck] = [];
        }
        acc[car.truck].push(car);
        return acc;
    }, {});



    // Function to delete car by ID
    const handleDeleteCar = async (carId) => {
        const payload = {
            caller: {
                username: userInfo.username,
                password: password,
            }
        };
        try {
            const response = await axios.delete(
                WEB_SERVICE_URL + "/product-orders/"+params+"/cars/"+ carId,
                {
                    headers: {
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*"
                    },
                    data: payload // Include payload in the config
                }
                // payload,
                // {
                //     headers: {
                //         "Content-Type": "application/json",
                //     },
                // }
                // data: payload
            );
        } catch (error) {
            if (error.response) {
                console.error("Error deleting car:", error.response.data);
            } else {
                console.error("Error deleting car:", error.message);
            }
        };
    };
   

    return (
        <div className="load-details">
            <h4>Load Details</h4>
            {Object.entries(trucks).map(([truck, cars]) => (
                <div key={truck} className="truck-details">
                    <p><strong>Truck:</strong> {truck}</p>
                    <p><strong>Contains ID:</strong></p>
                    <div className="cars-container">
                        {cars.map((car) => (
                            <div key={car.id} className="car-item">
                                {car.id}
                                <button onClick={() => handleDeleteCar(car.id)} className="delete-button">🗑️</button>
                            </div>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
}

export default LoadDetails;