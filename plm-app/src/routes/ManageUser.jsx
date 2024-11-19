import { useEffect, useState } from "react";
import { Navigate } from "react-router";
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
                console.log(response.data);  // ตรวจสอบ URL ของ API
                setUsers(response.data);  // อัปเดต state ของ users
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchUsers();  

    }, []);  // [] หมายถึงจะดึงข้อมูลเมื่อ component ติดตั้งครั้งแรก

    useEffect(() => {
        console.log(JSON.stringify(userInfo));
        if (toLogin || !validCreds) {
            removeCookie("username");
            removeCookie("password");
        }
    }, [toLogin]);

    if (toLogin || !validCreds) {
        return <Navigate to="/login" />
    }

    function handleOnReset(username) { // คลิก reset
        setSelectedUser(username);
        setShowReset(true);
    }

    function handleNewPassword(NewPassword) { // คลิก submit สำหรับการเปลี่ยนรหัสผ่าน
        console.log('submit reset: ' + NewPassword);
    }

    // ใช้ useEffect เพื่อตรวจสอบการอัปเดต selectedUser
    useEffect(() => {
        console.log(selectedUser);
    }, [selectedUser]);

    return (<>
        <Header onLogout={() => setToLogin(true)} user={userInfo} />
        <h1>This is a ManageUser</h1>
        <p>Welcome, {userInfo.username}</p>
        <p>Role: {Role.toString(userInfo.role)}</p>
        <div className="center-block">
            <BackButton />
            <UserTable users={users} onReset={handleOnReset} /> {/* ส่งข้อมูลผู้ใช้ไปยัง UserTable */}
        </div>
        <ResetPasswordModal
            show={showReset}
            onClose={() => setShowReset(false)}
            onPasswordReset={handleNewPassword}
        />
    </>);
}
