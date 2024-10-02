class TicketDetails {
    constructor() {
        this.bindElements();
    }

    bindElements() {
        const urlParams = new URLSearchParams(window.location.search);
        this.ticketId = urlParams.get('ticketId');

        if (!this.ticketId) {
            alert('No Ticket ID provided in the URL.');
            return;
        }

        this.ticketDetailsBody = document.getElementById('ticket-details-body');
        this.responsesBody = document.querySelector('.table-container tbody');
        this.messageDisplay = document.getElementById('message');

        // Fetch and display the ticket details and responses
        this.getTicketDetail();
    }

    async getTicketDetail() {
        try {
            const response = await fetch(`http://localhost:8080/minh/tickets/${this.ticketId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch ticket details.');
            }

            const ticketData = await response.json();
            this.populateTicketDetails(ticketData);

            // Fetch responses for this ticket
            await this.getResponses(this.ticketId);

        } catch (error) {
            console.error('Error fetching ticket details:', error);
            alert('An error occurred while fetching ticket details.');
        }
    }

    populateTicketDetails(ticketData) {
        this.ticketDetailsBody.innerHTML = ''; // Clear existing table content

        for (const [key, value] of Object.entries(ticketData)) {
            if (key === 'id' || key === 'responseIds') continue; // Skip ID and responseIds

            const row = document.createElement('tr');
            const cellKey = document.createElement('td');
            cellKey.textContent = key[0].toUpperCase() + key.slice(1); // Capitalize the first letter of the key
            const cellValue = document.createElement('td');
            cellValue.textContent = value;
            row.appendChild(cellKey);
            row.appendChild(cellValue);
            this.ticketDetailsBody.appendChild(row);
        }
    }

    async getResponses(ticketId) {
        try {
            const response = await fetch(`http://localhost:8080/minh/tickets/${ticketId}/responses`);
            if (!response.ok) {
                throw new Error('Failed to fetch ticket responses.');
            }

            const responsesData = await response.json();
            this.displayResponses(responsesData);

        } catch (error) {
            console.error('Error fetching ticket responses:', error);
            alert('An error occurred while fetching ticket responses.');
        }
    }

    displayResponses(responses) {
        this.responsesBody.innerHTML = ''; // Clear existing response content

        // Check if there are responses
        if (responses.length > 0) {
            responses.forEach(response => {
                const row = document.createElement('tr');
                const cellContent = document.createElement('td');

                // Assuming 'content' is a field in your Response object
                cellContent.textContent = response.response || 'No content available';

                row.appendChild(cellContent);
                this.responsesBody.appendChild(row);
            });
        } else {
            // If no responses found, display a message
            const row = document.createElement('tr');
            const cellContent = document.createElement('td');
            cellContent.textContent = 'No responses found for this ticket.';
            row.appendChild(cellContent);
            this.responsesBody.appendChild(row);
        }
    }
}

// Run when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new TicketDetails();
});
