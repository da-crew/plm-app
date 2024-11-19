import React, { useState, useEffect } from 'react';
import { Role } from '../users';

const UserTable = (props) => {
    
    const [users, setUsers] = useState(props.users);

    useEffect(() => {
        
        setUsers(props.users);
    }, [props.users]); 

    const roles = [Role.DISPATCHER, Role.CHECKER, Role.EXPORTER, Role.ADMIN];

    // Handler to update username or role
    const handleInputChange = (username, field, value) => {
        setUsers((prevUsers) =>
            prevUsers.map((user) =>
                user.username === username ? { ...user, [field]: value } : user
            )
        );
    };

    const handleDelete = (username) => {
        console.log(`Delete user with username: ${username}`);
        if (props.onDelete) {
            props.onDelete(username);
        }
    };

    const handleUpdate = (username) => {
        console.log(`Update user with username: ${username}`);
        if (props.onDelete) {
            props.onUpdate(username);
        }
    };

    const handleResetPassword = (username) => {
        console.log(`Reset password with username: ${username}`);
        if (props.onReset) {
            props.onReset(username);
        }
    };

    return (
        <div className="table-container">
            <table className="table">
                <thead>
                    <tr>
                        <th className="cell table-header"></th>
                        <th className="cell table-header"><div style={{ paddingLeft: 10 }}>User name</div></th>
                        <th className="cell table-header"><div style={{ paddingLeft: 10 }}>First name</div></th>
                        <th className="cell table-header"><div style={{ paddingLeft: 10 }}>Last name</div></th>
                        <th className="cell table-header"><div style={{ paddingLeft: 25 }}>Role</div></th>
                        <th className="cell table-header"></th>
                        <th className="cell table-header"></th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td className="cell">
                                <button
                                    onClick={() => handleDelete(user.username)}
                                    style={{ backgroundColor: '#D32F2F', color: 'white', padding: '5px 10px', border: 'none', borderRadius: '5px' }}
                                >
                                    Delete
                                </button>
                            </td>

                            <td className="cell">
                                <input
                                    type="text"
                                    value={user.username}
                                    onChange={(e) => handleInputChange(user.username, 'username', e.target.value)}
                                    style={{ textAlign: 'center', padding: '5px', width: '100px', borderRadius: '4px', border: '1px solid #B0BEC5' }}
                                />
                            </td>
                            <td className="cell">
                                <input
                                    type="text"
                                    value={user.firstname}
                                    onChange={(e) => handleInputChange(user.username, 'firstname', e.target.value)}
                                    style={{ textAlign: 'center', padding: '5px', width: '100px', borderRadius: '4px', border: '1px solid #B0BEC5' }}
                                />
                            </td>
                            <td className="cell">
                                <input
                                    type="text"
                                    value={user.lastname}
                                    onChange={(e) => handleInputChange(user.username, 'lastname', e.target.value)}
                                    style={{ textAlign: 'center', padding: '5px', width: '100px', borderRadius: '4px', border: '1px solid #B0BEC5' }}
                                />
                            </td>
                            <td className="cell">
                                <select
                                    value={user.role}
                                    onChange={(e) => handleInputChange(user.username, 'role', e.target.value)}
                                    style={{ textAlign: 'center', padding: '5px', width: '100px', borderRadius: '4px', border: '1px solid #B0BEC5' }}
                                >
                                    {roles.map((role) => (
                                        <option key={Role.toString(role)} value={Role.toString(role)}>
                                            {Role.toString(role)}
                                        </option>
                                    ))}
                                </select>
                            </td>
                            <td className="cell">
                                <button
                                    onClick={() => handleResetPassword(user.username)}
                                    style={{ backgroundColor: '#1976D2', color: 'white', padding: '5px 10px', border: 'none', borderRadius: '5px' }}
                                >
                                    Reset Password
                                </button>
                            </td>
                            <td className="cell">
                                <button
                                    onClick={() => handleUpdate(user.username)}
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
