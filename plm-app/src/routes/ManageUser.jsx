import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router';
import Header from "../components/Header";
import UserTable from "../components/UserTable";
import ResetPasswordModal from "../components/ResetPassword";
import axios from 'axios';
import { useCookies } from "react-cookie";
import BackButton from "../components/BackButton";
import { useAuthenticate } from "../users";
import { Role } from "../users";

export default function ManageUser() {

    let [toLogin, setToLogin] = useState(false);
    let [validCreds, userInfo, password] = useAuthenticate();
    let [cookies, setCookies, removeCookie] = useCookies(['username', 'password']);

    const [users, setUsers] = useState([]);  // State ที่เก็บข้อมูลผู้ใช้

    let [showReset, setShowReset] = useState(false);
    let [selectedUser, setSelectedUser] = useState(null);  // State สำหรับผู้ใช้ที่เลือก

    // ดึงข้อมูลผู้ใช้จาก backend
    useEffect(() => {
        // ทำการเรียกข้อมูลผู้ใช้จาก backend
        const fetchUsers = async () => {
            try {
                const response = await axios.get('http://localhost:8080/users');
                setUsers(response.data);  // อัปเดต state ของ users
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchUsers();  
    }, []);

    // กรองผู้ใช้ที่ไม่ใช่ admin
    const filteredUsers = users.filter(user => user.role !== Role.ADMIN);

    // ฟังก์ชันลบผู้ใช้
    const handleDelete = async (username) => {
        try {
            // แทนที่ `{username}` ด้วยค่าจริงของ `username`
            const response = await axios.delete(`http://localhost:8080/users/${username}/delete`, {
                data: {
                    caller: {
                        username: userInfo.username,
                        password: password,
                    }
                }
            });
    
            // อัปเดต state ใน ManageUser
            setUsers((prevUsers) => prevUsers.filter((user) => user.username !== username));
            alert(`Deleted user ${username} successfully`);
        } catch (error) {
            console.error("Error deleting user:", error.response ? error.response.data : error.message);
            alert("Failed to delete user. Please try again.");
        }
    };
    

    // ฟังก์ชันอัปเดตผู้ใช้
    const handleUpdate = async (username) => {
        if (userInfo.role !== Role.ADMIN) {
            alert("You do not have permission to update users.");
            return; // ถ้าไม่ใช่ admin ให้ยกเลิกการอัปเดต
        }

        const foundUser = users.find((user) => user.username === username);

        const updatedUser = {
            username: foundUser.username,
            firstname: foundUser.firstname,
            lastname: foundUser.lastname,
            role: foundUser.role
        };

        const data = {
            caller: {
                username: userInfo.username,
                password: password,
            },
            user: updatedUser
        };

        if (!updatedUser) return;
        try {
            await axios.post(`http://localhost:8080/users/${username}/update`, data);
            alert("User updated successfully!");
        } catch (error) {
            console.error("Error updating user:", error);
            alert("Failed to update user. Please try again.");
        }
    };

    // ฟังก์ชันเปิด Modal สำหรับรีเซ็ตรหัสผ่าน
    const handleOnReset = (username) => {
        setSelectedUser(username); // เลือกผู้ใช้ที่ต้องการรีเซ็ตรหัสผ่าน
        setShowReset(true); // เปิด Modal
    };

    // ฟังก์ชันรีเซ็ตรหัสผ่าน
    const handlePasswordReset = async (newPassword) => {
        if (userInfo.role !== Role.ADMIN) {
            alert("You do not have permission to reset passwords.");
            return; // ถ้าไม่ใช่ admin ให้ยกเลิกการรีเซ็ตรหัสผ่าน
        }
        if (!selectedUser || !newPassword) return; // ตรวจสอบข้อมูล
        try {
            await axios.post(`http://localhost:8080/users/${selectedUser}/reset-password`, {
                caller: {
                    username: userInfo.username,
                    password: password,
                },
                password: newPassword,
            });
            alert("Password reset successfully!");
            setShowReset(false); // ปิด Modal
            setSelectedUser(null); // รีเซ็ต state
        } catch (error) {
            console.error("Error resetting password:", error);
        }
    };

    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        <h1>This is a ManageUser</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <div className="center-block">
            <BackButton />
            <UserTable 
                users={filteredUsers} 
                onDelete={handleDelete} 
                onUpdate={handleUpdate}
                onReset={handleOnReset}
            />
        </div>
        <ResetPasswordModal
            show={showReset}
            onClose={() => setShowReset(false)}
            onPasswordReset={handlePasswordReset}  // ใช้ฟังก์ชันที่เชื่อมต่อกับ Backend
        />
    </>);
}
