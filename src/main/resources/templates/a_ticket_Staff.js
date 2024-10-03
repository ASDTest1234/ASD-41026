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

            // Button elements
            this.backButton = document.getElementById('back-button');
            this.editButton = document.getElementById('edit-button');
            this.deleteButton = document.getElementById('delete-button');

            // Add event listeners for buttons
            this.backButton.addEventListener('click', () => this.goBack());
            this.editButton.addEventListener('click', () => this.editTicket());
            this.deleteButton.addEventListener('click', () => this.deleteTicket());

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

                this.editButton.disabled = true;
                this.deleteButton.disabled = true;
            });
        } else {
            this.editButton.disabled = false;
            this.deleteButton.disabled = false;
            // If no responses found, display a message
            const row = document.createElement('tr');
            const cellContent = document.createElement('td');
            cellContent.textContent = 'No responses found for this ticket.';
            row.appendChild(cellContent);
            this.responsesBody.appendChild(row);
        }
    }
    goBack() {
            // Navigate to the previous page or a specific URL
        window.location.href = 'all_tickets_Staff.html'; // Change to desired page URL
    }

    editTicket() {
            // Check if the ticket can be edited
            if (this.editButton.disabled) {
                alert('You cannot edit this ticket because there are responses.');
                return;
            }
            // Navigate to the edit page for the ticket
            window.location.href = `edit_ticket.html?ticketId=${this.ticketId}`; // Assuming you have an edit page
    }

    async deleteTicket() {
        // Check if the ticket can be deleted
        if (this.deleteButton.disabled) {
            alert('You cannot delete this ticket because there are responses.');
            return; // Prevent deletion if the button is disabled
        }

        const confirmDelete = confirm("Are you sure you want to delete this ticket?");
        if (!confirmDelete) {
            return; // User canceled, do nothing
        }

        try {
            const response = await fetch(`http://localhost:8080/minh/tickets/${this.ticketId}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Failed to delete the ticket.');
            }

            // Optionally show a success message and redirect or perform an action
            alert('Ticket deleted successfully.');

            // Redirect to a specific page after deletion
            window.location.href = 'my_tickets.html'; // Change to the desired page URL

        } catch (error) {
            console.error('Error deleting the ticket:', error);
            alert('An error occurred while deleting the ticket.'); // Inform the user about the error
        }
    }

}

// Run when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new TicketDetails();
});
