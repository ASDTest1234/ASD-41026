class MyTickets {
    constructor() {
        this.bindElements();
    }

    bindElements() {
        this.customerIdInput = document.getElementById('customer-id');
        this.searchButton = document.getElementById('search-button');
        this.createTicketButton = document.getElementById('create-ticket-button');
        this.messageDisplay = document.getElementById('message');
        this.ticketInfoCell = document.getElementById('ticket_info');

        this.searchButton.addEventListener('click', () => this.searchTickets());
        this.createTicketButton.addEventListener('click', () => this.createTicket());
        this.addInputListeners();
    }

    addInputListeners() {
        const inputId = 'customer-id';
        const displayId = 'customer-id-display';

        const input = document.getElementById(inputId);
        input.addEventListener('focus', () => this.toggleDisplayText(inputId, displayId));
        input.addEventListener('blur', () => this.toggleDisplayText(inputId, displayId));
        input.addEventListener('input', () => {
            this.toggleDisplayText(inputId, displayId);
        });

        this.toggleDisplayText(inputId, displayId);
    }

    toggleDisplayText(inputId, displayId) {
        const input = document.getElementById(inputId);
        const displayText = document.getElementById(displayId);
        displayText.style.visibility = input.value === '' ? 'visible' : 'hidden';
    }

    async searchTickets() {
        const customerId = this.customerIdInput.value.trim();
        if (!customerId) {
            this.messageDisplay.textContent = 'Please enter a Customer ID to search.';
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/minh/tickets/mytickets/${customerId}`, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('No tickets found for the given Customer ID.');
            }

            const tickets = await response.json();

            if (tickets.length > 0) {
                this.messageDisplay.textContent = `Found ${tickets.length} ticket(s) for Customer ID: ${customerId}`;
                console.log('Tickets:', tickets); // Log the found tickets
                this.displayTickets(tickets); // Call the function to display fetched tickets
            } else {
                this.messageDisplay.textContent = 'No tickets found for this Customer ID.';
                this.ticketInfoCell.textContent = 'No tickets found for this Customer ID.'; // Update the ticket info cell accordingly
            }
        } catch (error) {
            this.messageDisplay.textContent = `Error: ${error.message}`;
            console.error('Error fetching tickets:', error); // Log error details
            this.ticketInfoCell.textContent = 'Error fetching tickets. Please try again.'; // Show error in ticket info cell
        }
    }

    displayTickets(tickets) {
        // Clear previous ticket information
        this.ticketInfoCell.innerHTML = ''; // Reset cell

        // Create an internal table within the ticket info cell
        const table = document.createElement('table');
        table.className = 'table2';

        const headerRow = document.createElement('tr');
        ['Ticket ID', 'Issue', 'Description', 'Date'].forEach(headerText => {
            const th = document.createElement('th');
            th.textContent = headerText;
            th.style.border = '1px solid #dddddd';
            th.style.padding = '8px';
            th.style.backgroundColor = '#f4f4f9';
            th.style.color = 'black';
            headerRow.appendChild(th);
        });
        table.appendChild(headerRow);

        tickets.forEach(ticket => {
            const row = document.createElement('tr');
            ['ticketId', 'issue', 'description', 'date'].forEach(key => {
                const td = document.createElement('td');
                td.textContent = ticket[key];
                td.style.border = '1px solid #dddddd';
                td.style.padding = '8px';
                td.style.textAlign = 'center';
                row.appendChild(td);
            });
            row.addEventListener('click', () => {
                // Use the ticket ID to generate the appropriate URL
                window.location.href = `ticket_detail.html?ticketId=${ticket.ticketId}`;
            });

            table.appendChild(row);
        });

        this.ticketInfoCell.appendChild(table);
    }
    createTicket() {
        window.location.href = 'support_ticket.html';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new MyTickets();
});
