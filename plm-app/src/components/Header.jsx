import React from 'react';

function Header(props) {
  return (
    <header style={styles.header}>
      
      <div style={styles.userSection}>
        <button style={styles.logoutButton} onClick={props.onLogout}>Logout</button>
        <span className='employeeName' style={styles.employeeText}>{props.employeeName}  Role: {props.role}</span>
        <div style={styles.profileIcon}>👤</div>
      </div>
    </header>
  );
}

const styles = {
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    backgroundColor: '#323136',
    padding: '10px 20px',
  },
  
  userSection: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    
  },
  logoutButton: {
    backgroundColor: '#D32F2F',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '10px 15px',
    cursor: 'pointer',
  },
  employeeText: {
    color: 'white',
    fontSize: '14px',
  },
  profileIcon: {
    fontSize: '24px',
    color: 'white',
  },
};

export default Header;