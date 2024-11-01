import React from 'react';

function DashboardHeader() {
  return (
    <div style={styles.panelContainer}>
      <div style={styles.buttonContainer}>
        <button style={styles.createButton}>Create</button>
        <button style={styles.manageUserButton}>Manage User</button>
      </div>

      <div style={styles.searchContainer}>
        <input type="text" placeholder="Enter B/L No..." style={styles.searchInput} />
        <button style={styles.searchButton}>Search</button>
      </div>
    </div>
  );
}

const styles = {
  panelContainer: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: '#E0E0E0',
    padding: '10px',
    borderRadius: '10px',
    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.2)',
    marginBottom: '10px',
  },
  buttonContainer: {
    display: 'flex',
    gap: '10px',
  },
  createButton: {
    padding: '10px 20px',
    backgroundColor: '#1976D2',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  manageUserButton: {
    padding: '10px 20px',
    backgroundColor: '#1976D2',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  searchContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  searchInput: {
    padding: '10px',
    border: '1px solid #B0BEC5',
    borderRadius: '5px',
    marginRight: '5px',
    width: '200px',
  },
  searchButton: {
    padding: '10px 20px',
    backgroundColor: '#4CAF50',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
};

export default DashboardHeader;