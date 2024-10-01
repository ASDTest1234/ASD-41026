class MyTickets {
    constructor() {
        this.bindElements();
    }

    bindElements() {
        this.customerIdInput = document.getElementById('customer-id');
        this.searchButton = document.getElementById('search-button');
        this.messageDisplay = document.getElementById('message');

        this.searchButton.addEventListener('click', () => this.searchTickets());
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
            } else {
                this.messageDisplay.textContent = 'No tickets found for this Customer ID.';
            }
        } catch (error) {
            this.messageDisplay.textContent = `Error: ${error.message}`;
            console.error('Error fetching tickets:', error); // Log error details
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new MyTickets();
});
