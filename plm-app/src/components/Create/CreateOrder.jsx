import React from 'react';
import './Create.css'

    function ReceiptDetail() {

        return (
            <div className="receipt-info-container">
                {/* Left Section: Form */}
                <div className="form-section">
                    <h2>Receipt Info</h2>
                    <div className="form-group">
                        <label>Date:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>B/L No.:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Vessel:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Voy. No.:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Consignee:</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>TO:</label>
                        <select>
                            <option>Select</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <button>Choose file</button>
                    </div>
                </div>

                {/* Right Section: Table */}
                <div className="table-section">
                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns: "repeat(4, 1fr)",
                            backgroundColor: "#007bff",
                            color: "white",
                            padding: "10px",
                            borderRadius: "4px 4px 0 0",
                            fontSize: "14px",
                            fontWeight: "bold",
                            textAlign: "center",
                        }}
                    >
                        <div>Mark & Nos.</div>
                        <div>Pkgs.</div>
                        <div>Description</div>
                        <div>Remarks</div>
                    </div>
                    <div className="table-body">
                        <div>
                            <input type="text" />
                        </div>
                        <div>
                            <input type="text" />
                        </div>
                        <div>
                            <input type="text" />
                        </div>
                        <div>
                            <input type="text" />
                        </div>
                    </div>
                </div>

                {/* Save Button */}
                <div className="save-button-section">
                    <button>SAVE</button>
                </div>
            </div>
        );
    }
    export default ReceiptDetail;