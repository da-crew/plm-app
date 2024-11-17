import React from 'react';
import './ViewProductOrder.css';


function DamageReport({ params, productOrders }) {
    // Find the product order with the matching BL Number


    const thisProductOrder = productOrders.find(
        (item) => item.blnumber === params
    );

    // If no matching product order is found
    if (!thisProductOrder) {
        return <p>No matching product order found.</p>;
    }

    // Filter cars that have reports
    const reportedCars = thisProductOrder.cars.filter(
        (car) => car.reports.length > 0
    );

    // If no cars with reports are found
    if (reportedCars.length === 0) {
        return (
        <div className= "damage-report">
            <h4>Damage Report</h4>
        <p>No damage reports available for this product order.</p>
        </div>
        )
    }

    

    return (
        <div className="damage-report">
            <h4>Damage Report</h4>
            {reportedCars.map((car) => (//
                <div key={car.id} className="truck-details">
                    <p><strong>Car ID:</strong> {car.id}</p>
                    {car.reports.map((report, index) => (
                        <div key={index} className="report-item">
                            <p><strong>Report:</strong> {report.description}</p>
                            {report.imageUrl && (
                                <img
                                    src={report.imageUrl}
                                    alt="Damage"
                                    className="damage-image"
                                />
                            )}
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
}
const sampleOrders = [// for test
    {
        blnumber: "BL2023045",
        cars: [
            {
                id: 21,
                reports: [
                    {
                        description: "Window broken",
                        imageUrl: "damage-window.jpg",
                    },
                ],
            },
            {
                id: 22,
                reports: [
                    {
                        description: "Scratched bumper",
                        imageUrl: "damage-bumper.jpg",
                    },
                ],
            },
            {
                id: 23,
                reports: [],
            },
            {
                id: 24,
                reports: [
                    {
                        description: "Flat tire",
                        imageUrl: "damage-tire.jpg",
                    },
                ],
            },
        ],
    },
    {
        blnumber: "BL2023050",
        cars: [
            {
                id: 31,
                reports: [],
            },
            {
                id: 32,
                reports: [
                    {
                        description: "Cracked windshield",
                        imageUrl: "damage-windshield.jpg",
                    },
                ],
            },
        ],
    },
];
export default DamageReport