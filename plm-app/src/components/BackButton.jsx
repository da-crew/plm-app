import React from "react";
import { useNavigate } from "react-router-dom"; 

const BackButton = () => {
    const navigate = useNavigate(); // React Router's hook for navigation

    const goBack = () => {
        navigate(-2); // Go back to the previous page
    };

    // Inline styles remain the same
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

    // Hover state logic
    const [isHovered, setIsHovered] = React.useState(false);

    return (
        <div
            className="back"
            style={{
                ...styles.back,
                color: isHovered ? styles.hover.color : styles.back.color,
                textDecoration: isHovered ? styles.hover.textDecoration : styles.back.textDecoration,
            }}
            onClick={goBack} // Handles navigation
            onMouseEnter={() => setIsHovered(true)} // Sets hover state
            onMouseLeave={() => setIsHovered(false)} // Resets hover state
        >
            <span style={styles.arrow}>&larr;</span> Back
        </div>
    );
};

export default BackButton;