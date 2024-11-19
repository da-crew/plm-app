import React from "react";
import { useNavigate } from "react-router-dom"; 
import "./BackButton.css";

const BackButton = () => {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-2); // Go back to the previous page
    };

    return (
        <div className="back-button" onClick={goBack}>
            <span className="arrow">&larr;</span> Back
        </div>
    );
};

export default BackButton;