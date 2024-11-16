import React from "react";
import { useNavigate } from "react-router-dom"; // If using React Router

const BackButton = () => {
    const navigate = useNavigate(); // React Router's hook for navigation

    const goBack = () => {
        navigate(-1); // Go back to the previous page
    };

    // Inline styles
    const styles = {
        back: {
            display: "inline-flex",
            alignItems: "center",
            fontFamily: "Arial, sans-serif",
            fontSize: "24px", // Adjust size
            fontWeight: "bold",
            color: "black", // Adjust color
            textDecoration: "none", // Remove underline
            cursor: "pointer",
        },
        arrow: {
            marginRight: "8px", // Space between arrow and text
            fontSize: "24px", // Adjust size of the arrow
            lineHeight: 1,
        },
        hover: {
            color: "#1976D2", // Hover effect color
            textDecoration: "underline", // Optional hover effect
        },
    };

    return (
        <div
            className="back"
            style={styles.back}
            onClick={goBack}
            onMouseEnter={(e) => (e.target.style.color = styles.hover.color)} // Hover effect
            onMouseLeave={(e) => (e.target.style.color = styles.back.color)} // Reset color
        >
            <span style={styles.arrow}>&larr;</span> Back
        </div>
    );
};

export default BackButton;