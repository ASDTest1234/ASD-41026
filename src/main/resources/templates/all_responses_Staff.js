document.addEventListener('DOMContentLoaded', function() {
    fetch('http://localhost:8080/minh/response/allresponses')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(responses => {
            const tableBody = document.getElementById('responses-table').getElementsByTagName('tbody')[0];
            const loadingMessage = document.getElementById('loading-message');
            tableBody.innerHTML = '';

            if (!Array.isArray(responses) || responses.length === 0) {
                loadingMessage.textContent = 'No responses found.';
                return;
            }

            let selectedRow = null;
            let selectedResponseId = ''; // Variable to store selected response ID

            responses.forEach(response => {
                const row = document.createElement('tr');
                const responseIdCell = document.createElement('td');
                responseIdCell.textContent = response.response_id || 'N/A';
                row.appendChild(responseIdCell);

                const responseBodyCell = document.createElement('td');
                responseBodyCell.textContent = response.response || 'N/A';
                row.appendChild(responseBodyCell);

                const timestampCell = document.createElement('td');
                timestampCell.textContent = response.id.timestamp || 'N/A';
                row.appendChild(timestampCell);

                const dateCell = document.createElement('td');
                dateCell.textContent = response.id.date || 'N/A';
                row.appendChild(dateCell);

                row.addEventListener('click', function() {
                    if (selectedRow) {
                        selectedRow.classList.remove('selected');
                    }
                    row.classList.add('selected');
                    selectedRow = row;

                    // Store the response ID of the selected row
                    selectedResponseId = response.response_id;
                });

                tableBody.appendChild(row);
            });

            loadingMessage.style.display = 'none';

            const backButton = document.getElementById('back-button');
            backButton.addEventListener('click', function() {
                window.location.href = 'all_tickets_Staff.html';
            });

            const editButton = document.getElementById('edit-button');
            editButton.addEventListener('click', function() {
                if (selectedResponseId) {
                    // Redirect to the edit page and pass the response_id as a query parameter
                    window.location.href = `edit_response_Staff.html?response_id=${selectedResponseId}`;
                } else {
                    alert('Please select a row to edit.');
                }
            });


            const deleteButton = document.getElementById('delete-button');
            deleteButton.addEventListener('click', function() {
                if (selectedResponseId) {
                    // Show confirmation dialog
                    const userConfirmed = confirm('Are you sure you want to delete this response? This action cannot be undone.');
                    if (userConfirmed) {
                        // Make a DELETE request to delete the response
                        fetch(`http://localhost:8080/minh/response/delete/${selectedResponseId}`, {
                            method: 'DELETE'
                        })
                        .then(response => {
                            if (response.ok) {
                                // Remove the selected row from the table
                                tableBody.removeChild(selectedRow);
                                selectedRow = null; // Deselect after deletion
                                selectedResponseId = ''; // Reset selectedResponseId
                            } else {
                                alert('Failed to delete the response.');
                            }
                        })
                        .catch(error => {
                            console.error('Error during deletion:', error);
                            alert('Error during deletion.');
                        });
                    }
                } else {
                    alert('Please select a row to delete.');
                }
            });

        })
        .catch(error => {
            console.error('Error fetching responses:', error);
            document.getElementById('loading-message').textContent = 'Failed to load responses.';
        });
});
