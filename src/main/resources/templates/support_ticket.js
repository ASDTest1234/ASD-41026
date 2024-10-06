class support_ticket {
    constructor() {
        this.ticketData = {
            ticketId: '',
            customerId: '',
            issue: '',
            description: '',
            date: new Date().toLocaleDateString()
        };

        this.bindElements();
        this.updateInputFields(); // Populate inputs with initial empty values
    }

    bindElements() {
        this.ticketIdInput = document.getElementById('ticket-id');
        this.customerIdInput = document.getElementById('customer-id');
        this.issueInput = document.getElementById('issue');
        this.descriptionInput = document.getElementById('description');
        this.submitButton = document.getElementById('submit-button');
        this.resetButton = document.getElementById('reset-button');
        this.cancelButton = document.getElementById('cancel-button');
        this.viewTicketsButton = document.getElementById('view-tickets-button');
        this.dateDisplay = document.getElementById('date-display');
        this.messageDisplay = document.getElementById('message');

        this.dateDisplay.textContent = this.ticketData.date;

        this.submitButton.addEventListener('click', () => this.submit());
        this.resetButton.addEventListener('click', () => this.reset());
        this.cancelButton.addEventListener('click', () => this.cancel());
        this.viewTicketsButton.addEventListener('click', () => this.viewMyTickets());

        // Add event listeners for input fields to toggle display text
        this.addInputListeners();
    }

    updateInputFields() {
        this.ticketIdInput.value = this.ticketData.ticketId;
        this.customerIdInput.value = this.ticketData.customerId;
        this.issueInput.value = this.ticketData.issue;
        this.descriptionInput.value = this.ticketData.description;
    }

    addInputListeners() {
        const inputs = [
            { inputId: 'ticket-id', displayId: 'ticket-id-display' },
            { inputId: 'customer-id', displayId: 'customer-id-display' },
            { inputId: 'issue', displayId: 'issue-display' },
            { inputId: 'description', displayId: 'description-display' }
        ];

        inputs.forEach(({ inputId, displayId }) => {
            const input = document.getElementById(inputId);
            input.addEventListener('focus', () => this.toggleDisplayText(inputId, displayId));
            input.addEventListener('blur', () => this.toggleDisplayText(inputId, displayId));
            input.addEventListener('input', () => {
                this.toggleDisplayText(inputId, displayId);
                console.log(`Input change detected in ${inputId}: ${input.value}`); // Debug log
            });

            // Initial check on input fields
            this.toggleDisplayText(inputId, displayId);
        });
    }

    toggleDisplayText(inputId, displayId) {
        const input = document.getElementById(inputId);
        const displayText = document.getElementById(displayId);
        displayText.style.visibility = input.value === '' ? 'visible' : 'hidden';
    }

    async submit() {
            this.ticketData.ticketId = this.ticketIdInput.value;
            this.ticketData.customerId = this.customerIdInput.value;
            this.ticketData.issue = this.issueInput.value;
            this.ticketData.description = this.descriptionInput.value;

            // Debug logs for ticket data
            console.log(`Submitting ticket data:`, this.ticketData);

            if (this.ticketData.ticketId && this.ticketData.customerId && this.ticketData.issue && this.ticketData.description) {
                try {
                    const response = await fetch('http://localhost:8080/minh/tickets', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(this.ticketData)
                    });

                    if (!response.ok) throw new Error('Network response was not ok');

                    const data = await response.json();
                    this.messageDisplay.textContent = 'Ticket created successfully: ' + JSON.stringify(data);
                    console.log('Successfully created ticket:', data); // Log the response data
                } catch (error) {
                    this.messageDisplay.textContent = 'Error: ' + error.message;
                    console.log('Submission failed:', error); // Debug log
                }
            } else {
                this.messageDisplay.textContent = 'Please fill in all fields.';
                console.log('Submission failed: All fields must be filled.'); // Debug log
            }
    }

    reset() {
        this.ticketIdInput.value = '';
        this.customerIdInput.value = '';
        this.issueInput.value = '';
        this.descriptionInput.value = '';
        this.messageDisplay.textContent = '';

        // Reset ticket data
        this.ticketData = { ticketId: '', customerId: '', issue: '', description: '', date: this.ticketData.date };

        this.updateInputFields(); // Clear inputs
        this.addInputListeners(); // Re-add listeners after reset

        console.log('Form has been reset.'); // Debug log
    }

    cancel() {
        console.log('Cancel button clicked.'); // Verify it gets called
        this.messageDisplay.textContent = 'Returning to homepage...';
        // Optionally, you could add a redirect or hide the form as needed here
    }
    viewMyTickets() {
        window.location.href = 'my_tickets.html';
    }

}

document.addEventListener('DOMContentLoaded', () => {
    new support_ticket();
});
