// import React, { useState } from 'react';
// import './ViewProductOrder.css';


// function LoadDetails({ params, productOrders }) {
//     // Find the product order with the matching BL Number
//     const thisProductOrder = productOrders.find(
//         (item) => item.blnumber === params
//     );

//     // If no matching product order is found
//     if (!thisProductOrder) {
//         return <p>No matching product order found.</p>;
//     }

//     let [cars, setCars] = useState([])
//     let [trucks, setTrucks] = useState([])
//     // productOrders.map((car[key, index]))
//     for (let i = 0; i < thisProductOrder.cars.length; i++) {
//         if (!cars.includes(thisProductOrder.cars[i])) { setCars = [previousCars + thisProductOrder.cars[i]] }
//         if (!trucks.includes(thisProductOrder.cars[i].truck)) { setTrucks = [previousTruck + thisProductOrder.cars[i].truck] }
//     }

//     return (
//         <div className="load-details">
//             <h4>Load Details</h4>

//             {
//                 expected output:

//                 truck: TRK-1023
//                 contains i: 5 , 6 , 7
//             }

//         </div>
//     );
// }

// export default LoadDetails;
import React, { useState, useEffect } from 'react';
import './ViewProductOrder.css';

function LoadDetails({ params, productOrders }) {
    // Find the product order with the matching BL Number
    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    // State to store truck details as an array of objects, each with a truck and its associated IDs
    const [truckDetails, setTruckDetails] = useState([]);

    // useEffect to populate truck details only once when the component mounts
    useEffect(() => {
        const truckMap = {};

        // Iterate over the cars array to group car IDs by their truck
        thisProductOrder.cars.forEach((car) => {
            const { truck, id } = car;
            if (!truckMap[truck]) {
                truckMap[truck] = []; // Initialize array for new truck
            }
            truckMap[truck].push(id); // Add car ID to the truck's array
        });

        // Convert the truckMap object to an array format for easier mapping in JSX
        const truckArray = Object.entries(truckMap).map(([truck, ids]) => ({
            truck,
            ids,
        }));

        setTruckDetails(truckArray);
    }, [thisProductOrder.cars]);

    return (
        <div className="load-details">
            <h4>Load Details</h4>
            {/* Render each truck with its associated car IDs */}
            {truckDetails.map(({ truck, ids }) => (
                <div key={truck} className="truck-info">
                    <p><strong>Truck:</strong> {truck}</p>
                    <p><strong>Contains ID:</strong> {ids.join(', ')}</p>
                </div>
            ))}
        </div>
    );
}

export default LoadDetails;