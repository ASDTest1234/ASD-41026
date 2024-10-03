document.addEventListener('DOMContentLoaded', function() {
    fetch('http://localhost:8080/minh/response/allresponses')  // Ensure this URL is correct
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(responses => {
            console.log('Fetched responses:', responses); // Log the responses for debugging
            const tableBody = document.getElementById('responses-table').getElementsByTagName('tbody')[0];
            const loadingMessage = document.getElementById('loading-message');

            // Clear existing rows first if needed
            tableBody.innerHTML = '';

            if (!Array.isArray(responses) || responses.length === 0) {
                loadingMessage.textContent = 'No responses found.'; // Handle empty responses
                return;
            }

            // Create rows for each response
            responses.forEach(response => {
                const row = document.createElement('tr');

                // Create cell for Response ID
                const responseIdCell = document.createElement('td');
                responseIdCell.textContent = response.response_id || 'N/A'; // Response ID
                row.appendChild(responseIdCell);

                // Create cell for Response body
                const responseBodyCell = document.createElement('td');
                responseBodyCell.textContent = response.response || 'N/A'; // Response text
                row.appendChild(responseBodyCell);

                // Create cell for Timestamp
                const timestampCell = document.createElement('td');
                timestampCell.textContent = response.id.timestamp || 'N/A'; // Timestamp (UNIX format)
                row.appendChild(timestampCell);

                // Create cell for Date
                const dateCell = document.createElement('td');
                dateCell.textContent = response.id.date || 'N/A'; // Date in ISO format
                row.appendChild(dateCell);

                // Add click event to the row to navigate if required
                row.addEventListener('click', function() {
                    window.location.href = `a_ticket_Staff.html?ticketId=${response.response_id}`; // Redirecting based on response_id or related logic
                });

                tableBody.appendChild(row);
            });

            // Remove loading message
            loadingMessage.style.display = 'none';
        })
        .catch(error => {
            console.error('Error fetching responses:', error);
            document.getElementById('loading-message').textContent = 'Failed to load responses.';
        });
});
