import React from 'react';

const TableComponent = ({ data }) => {
  const handleRowClick = (item) => {
    console.log("Row clicked:", item);
  };

  const styles = {
    tableContainer: {
      overflowX: 'auto',
      backgroundColor: 'white',
      borderRadius: '8px',
      boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
      margin: '20px 0',
    },
    table: {
      width: '100%',
      borderCollapse: 'collapse',
    },
    tableHeader: {
      backgroundColor: '#f8f8f8',
      fontWeight: 'bold',
    },
    cell: {
      padding: '12px',
      textAlign: 'left',
      borderBottom: '1px solid #ddd',
    },
    emptyRow: {
      textAlign: 'center',
      color: '#999',
      fontStyle: 'italic',
    },
    clickableRow: {
      cursor: 'pointer',
    },
  };

  return (
    <div style={styles.tableContainer}>
      <table style={styles.table}>
        <thead>
          <tr>
            <th style={{ ...styles.cell, ...styles.tableHeader }}>B/L No.</th>
            <th style={{ ...styles.cell, ...styles.tableHeader }}>Date</th>
            <th style={{ ...styles.cell, ...styles.tableHeader }}>C. No.</th>
            <th style={{ ...styles.cell, ...styles.tableHeader }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {data.length > 0 ? (
            data.map((item, index) => (
              <tr
                key={index}
                style={styles.clickableRow}
                onClick={() => handleRowClick(item)}
              >
                <td style={styles.cell}>{item.blNo}</td>
                <td style={styles.cell}>{item.date}</td>
                <td style={styles.cell}>{item.cNo}</td>
                <td style={styles.cell}>{item.status}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" style={{ ...styles.cell, ...styles.emptyRow }}>
                Empty
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

// Sample data to test the component
const sampleData = [
  { blNo: "12345", date: "2024-11-04", cNo: "C001", status: "Pending" },
  { blNo: "12346", date: "2024-11-04", cNo: "C002", status: "Completed" },
];

// Test
const App = () => {
  return <TableComponent data={sampleData} />;
};

//
export default Table;