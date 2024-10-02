async function getDetail() {
    const urlParams = new URLSearchParams(window.location.search);
    const ticketId = urlParams.get('ticketId');

    if (!ticketId) {
        alert('No Ticket ID provided in the URL.');
        return;
    }

    try {
        // Fetch ticket data from server
        const response = await fetch(`http://localhost:8080/minh/tickets/${ticketId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch ticket details.');
        }

        const ticketData = await response.json();

        // Ensure that ticketData has the ticket details
        const detailsBody = document.getElementById('ticket-details-body');
        detailsBody.innerHTML = ''; // Clear existing table content

        for (const [key, value] of Object.entries(ticketData)) {
            if (key === 'id') continue;
            const row = document.createElement('tr');
            const cellKey = document.createElement('td');
            cellKey.textContent = key[0].toUpperCase() + key.slice(1);
            const cellValue = document.createElement('td');
            cellValue.textContent = value;
            row.appendChild(cellKey);
            row.appendChild(cellValue);
            detailsBody.appendChild(row);
        }
    } catch (error) {
        console.error('Error fetching ticket details:', error);
        alert('An error occurred while fetching ticket details.');
    }
}

// Run when the page loads
document.addEventListener('DOMContentLoaded', getDetail);
