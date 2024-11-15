
import React from 'react';
import './ViewProductOrder.css';
import axios from 'axios'; // Assuming Axios is used for API calls

function LoadDetails({ params, productOrders, onDeleteCar }) {
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
        try {
            await axios.delete(`/api/cars/${carId}`); // Replace with your actual API endpoint
            onDeleteCar(carId); // Update state in parent component to reflect deletion
        } catch (error) {
            console.error("Error deleting car:", error);
        }
    };

    return (
        <div className="load-details">
            <h4>Load Details</h4>
            {Object.entries(trucks).map(([truck, cars]) => (
                <div key={truck} className="truck-details">
                    <p><strong>Truck:</strong> {truck}</p>
                    <p><strong>Contains ID:</strong> {cars.map((car, index) => (
                        <span key={car.id}>
                            {car.id}
                            <button onClick={() => handleDeleteCar(car.id)} className="delete-button">🗑️</button>
                            {index < cars.length - 1 ? ', ' : ''}
                        </span>
                    ))}</p>
                </div>
            ))}
        </div>
    );
}

export default LoadDetails;