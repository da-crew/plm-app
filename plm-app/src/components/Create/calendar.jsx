import React from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";



    return (
        <div className="form-group">
            <label>Date:</label>
            <DatePicker
                selected={formData.orderDate ? new Date(formData.orderDate) : null}
                onChange={handleDateChange}
                showTimeSelect
                dateFormat="yyyy-MM-dd'T'HH:mm:ssXXX"
                timeFormat="HH:mm"
            />
        </div>
    );


export default Calendar;