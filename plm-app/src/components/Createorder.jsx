import React from 'react';

function ProductOrder() {
  const [formData, setFormData] = useState({
    date: '',
    blNo: '',
    vessel: '',
    voyNo: '',
    consignee: '',
    to: '',
    file: null,
    rows: [
      { markNos: '', pkgs: '', description: '', remarks: '' }
    ],
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleFileChange = (e) => {
    setFormData({
      ...formData,
      file: e.target.files[0],
    });
  };

  const handleRowChange = (index, e) => {
    const { name, value } = e.target;
    const rows = [...formData.rows];
    rows[index][name] = value;
    setFormData({
      ...formData,
      rows,
    });
  };

  const addRow = () => {
    setFormData({
      ...formData,
      rows: [...formData.rows, { markNos: '', pkgs: '', description: '', remarks: '' }]
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);
  };

  return (
    <div>
      <button onClick={() => window.history.back()}>Back</button>
      <h1>Receipt Info</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="date">Date:</label>
          <input
            type="date"
            id="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="blNo">B/L No.:</label>
          <input
            type="text"
            id="blNo"
            name="blNo"
            value={formData.blNo}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="vessel">Vessel:</label>
          <input
            type="text"
            id="vessel"
            name="vessel"
            value={formData.vessel}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="voyNo">Voy. No.:</label>
          <input
            type="text"
            id="voyNo"
            name="voyNo"
            value={formData.voyNo}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="consignee">Consignee:</label>
          <input
            type="text"
            id="consignee"
            name="consignee"
            value={formData.consignee}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="to">TO:</label>
          <select
            id="to"
            name="to"
            value={formData.to}
            onChange={handleChange}
          >
            <option value="">Select</option>
            {/* ใส่ตัวเลือกของคุณที่นี่ */}
          </select>
        </div>
        <div>
          <label htmlFor="file">Choose file:</label>
          <input
            type="file"
            id="file"
            name="file"
            onChange={handleFileChange}
          />
        </div>
        
        <table>
          <thead>
            <tr>
              <th>Mark&Nos.</th>
              <th>Pkgs.</th>
              <th>Description</th>
              <th>Remarks</th>
            </tr>
          </thead>
          <tbody>
            {formData.rows.map((row, index) => (
              <tr key={index}>
                <td>
                  <input
                    type="text"
                    name="markNos"
                    value={row.markNos}
                    onChange={(e) => handleRowChange(index, e)}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="pkgs"
                    value={row.pkgs}
                    onChange={(e) => handleRowChange(index, e)}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="description"
                    value={row.description}
                    onChange={(e) => handleRowChange(index, e)}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="remarks"
                    value={row.remarks}
                    onChange={(e) => handleRowChange(index, e)}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <button type="button" onClick={addRow}>Add Row</button>
        
        <button type="submit">SAVE</button>
      </form>
    </div>
  );
}