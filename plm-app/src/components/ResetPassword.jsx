import React, { useState } from 'react';
import axios from 'axios';

const ResetPasswordModal = (props) => {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);  // เพิ่ม state สำหรับการโหลด

    function handleNewPasswordChange(event) {
        setNewPassword(event.target.value);
    }

    function handleConfirmPasswordChange(event) {
        setConfirmPassword(event.target.value);
    }

    const handleSubmit = async () => {
        if (newPassword !== confirmPassword) {
            setErrorMessage("Passwords don't match!");
            return;
        }

        // ถ้ารหัสผ่านตรงกัน เรียกใช้ฟังก์ชัน onPasswordReset เพื่อส่งรหัสผ่านใหม่ไปที่ backend
        if (props.onPasswordReset) {
            setIsLoading(true); // เริ่มโหลด
            try {
                await props.onPasswordReset(newPassword);  // เรียกฟังก์ชันที่ส่งไปยัง backend
                setIsLoading(false);  // หยุดการโหลด
                alert("Password reset successfully!");  // แจ้งเตือนเมื่อสำเร็จ
            } catch (error) {
                setIsLoading(false);  // หยุดการโหลด
                setErrorMessage("Failed to reset password. Please try again.");  // แสดงข้อความผิดพลาด
            }
        }

        // รีเซ็ตฟิลด์และปิด modal
        setNewPassword('');
        setConfirmPassword('');
        setErrorMessage('');
        props.onClose();
    };

    const styles = {
        overlay: {
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
        },
        modal: {
            backgroundColor: 'white',
            padding: '20px',
            borderRadius: '8px',
            width: '300px',
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
        },
        errorText: {
            color: 'red',
            marginBottom: '10px',
        },
        input: {
            width: '100%',
            padding: '8px',
            marginBottom: '10px',
        },
        buttonContainer: {
            display: 'flex',
            justifyContent: 'space-between',
        },
        cancelButton: {
            padding: '8px 12px',
        },
        submitButton: {
            padding: '8px 12px',
            backgroundColor: 'blue',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
        },
        loadingButton: {
            padding: '8px 12px',
            backgroundColor: 'gray',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
        },
    };

    if (!props.show) return null;

    return (
        <div style={styles.overlay}>
            <div style={styles.modal}>
                <h3>Reset Password</h3>
                {errorMessage && <p style={styles.errorText}>{errorMessage}</p>}
                <input
                    type="password"
                    placeholder="New Password"
                    value={newPassword}
                    onChange={handleNewPasswordChange}
                    style={styles.input}
                />
                <input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={handleConfirmPasswordChange}
                    style={styles.input}
                />
                <div style={styles.buttonContainer}>
                    <button onClick={props.onClose} style={styles.cancelButton}>Cancel</button>
                    <button 
                        onClick={handleSubmit} 
                        style={isLoading ? styles.loadingButton : styles.submitButton}
                        disabled={isLoading} // ปิดการใช้งานปุ่มเมื่อกำลังโหลด
                    >
                        {isLoading ? 'Loading...' : 'Submit'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ResetPasswordModal;
