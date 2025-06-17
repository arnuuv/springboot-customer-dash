import React, { useState } from 'react';
import axios from 'axios';
import './CustomerExportImport.css';

const CustomerExportImport = () => {
    const [importFile, setImportFile] = useState(null);
    const [importResult, setImportResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file && file.type === 'text/csv') {
            setImportFile(file);
            setError(null);
        } else {
            setError('Please select a valid CSV file');
            setImportFile(null);
        }
    };

    const handleExport = async () => {
        try {
            setLoading(true);
            const response = await axios.get('http://localhost:8088/api/v1/customers/export/csv', {
                responseType: 'blob'
            });

            // Create download link
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'customers.csv');
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);

        } catch (err) {
            setError('Failed to export customers');
            console.error('Export error:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleImport = async () => {
        if (!importFile) {
            setError('Please select a file to import');
            return;
        }

        try {
            setLoading(true);
            const formData = new FormData();
            formData.append('file', importFile);

            const response = await axios.post('http://localhost:8088/api/v1/customers/import/csv', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            setImportResult(response.data);
            setImportFile(null);
            // Reset file input
            document.getElementById('file-input').value = '';

        } catch (err) {
            setError('Failed to import customers');
            console.error('Import error:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="export-import-container">
            <div className="export-import-header">
                <h1>Customer Data Management</h1>
                <p>Export and import customer data in CSV format</p>
            </div>

            <div className="export-import-grid">
                {/* Export Section */}
                <div className="export-section">
                    <div className="section-header">
                        <h2>📤 Export Customers</h2>
                        <p>Download all customer data as a CSV file</p>
                    </div>
                    
                    <div className="export-content">
                        <p>This will export all customer information including:</p>
                        <ul>
                            <li>Customer ID, Name, Email</li>
                            <li>Age and Gender</li>
                            <li>Profile Image ID</li>
                        </ul>
                        
                        <button 
                            onClick={handleExport} 
                            disabled={loading}
                            className="export-btn"
                        >
                            {loading ? 'Exporting...' : '📥 Export to CSV'}
                        </button>
                    </div>
                </div>

                {/* Import Section */}
                <div className="import-section">
                    <div className="section-header">
                        <h2>📥 Import Customers</h2>
                        <p>Import customer data from a CSV file</p>
                    </div>
                    
                    <div className="import-content">
                        <div className="file-upload-area">
                            <input
                                id="file-input"
                                type="file"
                                accept=".csv"
                                onChange={handleFileChange}
                                className="file-input"
                            />
                            <label htmlFor="file-input" className="file-label">
                                {importFile ? importFile.name : 'Choose CSV file'}
                            </label>
                        </div>

                        <div className="import-info">
                            <p><strong>CSV Format:</strong></p>
                            <code>Name,Email,Age,Gender</code>
                            <p className="note">Note: Passwords will be auto-generated for imported users</p>
                        </div>

                        <button 
                            onClick={handleImport} 
                            disabled={!importFile || loading}
                            className="import-btn"
                        >
                            {loading ? 'Importing...' : '📤 Import from CSV'}
                        </button>
                    </div>
                </div>
            </div>

            {/* Import Results */}
            {importResult && (
                <div className="import-results">
                    <h3>Import Results</h3>
                    <div className="results-grid">
                        <div className="result-item success">
                            <span className="result-label">Successfully Imported:</span>
                            <span className="result-value">{importResult.successCount}</span>
                        </div>
                        <div className="result-item error">
                            <span className="result-label">Errors:</span>
                            <span className="result-value">{importResult.errorCount}</span>
                        </div>
                    </div>
                    
                    {importResult.errors.length > 0 && (
                        <div className="error-list">
                            <h4>Error Details:</h4>
                            <ul>
                                {importResult.errors.map((error, index) => (
                                    <li key={index} className="error-item">{error}</li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            )}

            {/* Error Display */}
            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            {/* Instructions */}
            <div className="instructions">
                <h3>📋 Instructions</h3>
                <div className="instructions-grid">
                    <div className="instruction-item">
                        <h4>Export Format</h4>
                        <p>The exported CSV will contain all customer data in a structured format that can be opened in Excel or any spreadsheet application.</p>
                    </div>
                    <div className="instruction-item">
                        <h4>Import Format</h4>
                        <p>Your CSV should have columns: Name, Email, Age, Gender. The header row will be automatically skipped.</p>
                    </div>
                    <div className="instruction-item">
                        <h4>Data Validation</h4>
                        <p>Duplicate emails will be skipped during import. All imported users will receive auto-generated passwords.</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CustomerExportImport; 