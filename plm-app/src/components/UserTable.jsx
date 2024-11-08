import React, { useState } from 'react';
import { Role } from '../users';

const UserTable = (props) => {

    const [users, setUsers] = useState(props.users)

    const roles = [Role.DISPATCHER, Role.CHECKER, Role.EXPORTER, Role.ADMIN];

    // Handler to update username or role
    const handleInputChange = (id, field, value) => {
        setUsers((prevUsers) =>
            prevUsers.map((user) =>
                user.id === id ? { ...user, [field]: value } : user
            )
        );
    };

    const handleDelete = (id) => {
        console.log(`Delete user with ID: ${id}`);
        if (props.onDelete) {
            props.onDelete(id);
        }
    };


    const handleUpdate = (id) => {
        console.log(`Update user with ID: ${id}`);
        if (props.onDelete) {
            props.onUpdate(id);
        }
    };

    const handleResetPassword = (id) => {
        console.log(`Reset password with ID: ${id}`);
        if (props.onReset) {
            props.onReset(id);
        };
       
    };

    
        return (
            <div className="table-container">
                <table className="table">
                    <thead>
                        <tr>
                            <th className="cell table-header"></th>
                            <th className="cell table-header"><div style={{ paddingLeft: 8 }}>ID</div></th>
                            <th className="cell table-header"><div style={{ paddingLeft: 10 }}>User name</div></th>
                            <th className="cell table-header"><div style={{ paddingLeft: 25 }}>Role</div></th>
                            <th className="cell table-header"></th>
                            <th className="cell table-header"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => (
                            <tr key={user.id}>
                                <td className="cell">{/* Delete  ************************************/}
                                    <button
                                        onClick={() => handleDelete(user.id)}
                                        style={{ backgroundColor: '#D32F2F', color: 'white', padding: '5px 10px', border: 'none', borderRadius: '5px' }}
                                    >
                                        Delete
                                    </button>
                                </td>
                                <td className="cell">{user.id}</td>
                                <td className="cell">{/* Username  ************************************/}
                                    <input
                                        type="text"
                                        value={user.username}
                                        onChange={(e) => handleInputChange(user.id, 'username', e.target.value)}
                                        style={{ textAlign: 'center', padding: '5px', width: '100px', borderRadius: '4px', border: '1px solid #B0BEC5' }}
                                    />
                                </td>
                                <td className="cell">{/* Role  ************************************/}
                                    <select
                                        value={user.role}
                                        onChange={(e) => handleInputChange(user.id, 'role', e.target.value)}
                                        style={{ textAlign: 'center', padding: '5px', width: '100px', borderRadius: '4px', border: '1px solid #B0BEC5' }}
                                    >
                                        {roles.map((role) => (
                                            <option key={Role.toString(role)} value={Role.toString(role)}>
                                                {Role.toString(role)}
                                            </option>
                                        ))}
                                    </select>
                                </td>
                                <td className="cell">{/* Reset Password  ************************************/}
                                    <button
                                        onClick={() => handleResetPassword(user.id)}
                                        style={{ backgroundColor: '#1976D2', color: 'white', padding: '5px 10px', border: 'none', borderRadius: '5px' }}
                                    >
                                        Reset Password
                                    </button>
                                </td>
                                <td className="cell">{/* Update  ************************************/}
                                    <button
                                        onClick={() => handleUpdate(user.id)}
                                        style={{ backgroundColor: '#1976D2', color: 'white', padding: '5px 10px', border: 'none', borderRadius: '5px' }}
                                    >
                                        Update
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>

            </div>
        );
    };


export default UserTable;