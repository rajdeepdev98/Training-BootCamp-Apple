import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Table, Form, Button, Alert, Spinner } from 'react-bootstrap';
// import BootstrapTable from 'react-bootstrap-table-next';
// import paginationFactory from 'react-bootstrap-table2-paginator';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [allData, setAllData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [searchId, setSearchId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const API_BASE_URL = 'http://localhost:8080/api/aggregated-data'; // Replace with your API base URL

  // Fetch all data
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`${API_BASE_URL}`);
        setAllData(response.data);
        setFilteredData(response.data);
      } catch (err) {
        setError('Failed to fetch data');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // Handle search
  const handleSearch = async () => {
    if (!searchId.trim()) {
      setFilteredData(allData);
      return;
    }

    setLoading(true);
    setError('');
    try {
      const response = await axios.get(`${API_BASE_URL}/${searchId}`);
      setFilteredData([response.data]); // The API returns a single object for a specific ID
    } catch (err) {
      setError('Sensor ID not found');
      setFilteredData([]);
    } finally {
      setLoading(false);
    }
  };
  

  return (
    <div className="root-container d-flex justify-content-center align-items-start min-vh-100">
    <Container className="py-5">
      <div >

      <h1 className="mb-4">Sensor Data Viewer</h1>

{/* Search Form */}
<Form className="mb-4">
  <Form.Group controlId="searchId">
    <Form.Label>Search by Sensor ID</Form.Label>
    <Form.Control
      type="text"
      placeholder="Enter Sensor ID"
      value={searchId}
      onChange={(e) => setSearchId(e.target.value)}
    />
  </Form.Group>
  <Button className="mt-2" onClick={handleSearch} variant="primary">
    Search
  </Button>
  <Button
    className="mt-2 ms-2"
    variant="secondary"
    onClick={() => {
      setSearchId('');
      setFilteredData(allData);
    }}
  >
    Reset
  </Button>
</Form>

      </div>
      
      {/* Error Alert */}
      {error && <Alert variant="danger">{error}</Alert>}

      {/* Loading Spinner */}
      {loading && (
        <div className="d-flex justify-content-center my-3">
          <Spinner animation="border" size="lg" />
        </div>
      )}

      {/* Data Table */}
      {filteredData.length > 0 ? (
        <div className="table-responsive">
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Sensor ID</th>
              <th>Avg Temperature</th>
              <th>Avg Humidity</th>
              <th>Min Temperature</th>
              <th>Max Temperature</th>
              <th>Min Humidity</th>
              <th>Max Humidity</th>
              {/* <th>Data Count</th> */}
            </tr>
          </thead>
          <tbody>
            {filteredData.map((item) => (
              <tr key={item.sensorId}>
                <td>{item.sensorId}</td>
                <td>{item.avgTemperature}</td>
                <td>{item.avgHumidity}</td>
                <td>{item.minTemperature}</td>
                <td>{item.maxTemperature}</td>
                <td>{item.minHumidity}</td>
                <td>{item.maxHumidity}</td>
                {/* <td>{item.dataCount}</td> */}
              </tr>
            ))}
          </tbody>
        </Table>
        
        </div>
      ) : (
        !loading && <Alert variant="info">No data available to display.</Alert>
      )}
    </Container>
    </div>
  );
}

export default App;
