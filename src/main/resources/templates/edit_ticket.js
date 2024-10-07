class SupportTicket {
  constructor() {
    this.ticketData = {
      ticketId: '',
      customerId: '',
      issue: '',
      description: '',
      date: ''
    };

    // Parse the ticketId from the URL query parameters
    this.parseUrlParams();
    this.bindElements();
    this.getTicketData(); // Fetch and populate the ticket data using the ticket ID
  }

  parseUrlParams() {
    const urlParams = new URLSearchParams(window.location.search);
    this.ticketData.ticketId = urlParams.get('ticketId');
  }

  async getTicketData() {
    if (!this.ticketData.ticketId) {
      this.messageDisplay.textContent = 'No ticket ID provided in the URL.';
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/minh/tickets/${this.ticketData.ticketId}`);
      if (!response.ok) throw new Error('Failed to fetch ticket details.');

      const ticketData = await response.json();
      if (ticketData) {
        Object.assign(this.ticketData, ticketData);
        this.updateInputFields();
      } else {
        this.messageDisplay.textContent = 'Ticket not found.';
      }
    } catch (error) {
      this.messageDisplay.textContent = 'Error: ' + error.message;
    }
  }

  bindElements() {
    this.ticketIdInput = document.getElementById('ticket-id');
    this.customerIdInput = document.getElementById('customer-id');
    this.issueInput = document.getElementById('issue');
    this.descriptionInput = document.getElementById('description');
    this.dateInput = document.getElementById('date');
    this.submitButton = document.getElementById('submit-button');
    this.resetButton = document.getElementById('reset-button');
    this.cancelButton = document.getElementById('cancel-button');
    this.messageDisplay = document.getElementById('message');

    this.submitButton.addEventListener('click', () => this.submit());
    this.resetButton.addEventListener('click', () => this.reset());
    this.cancelButton.addEventListener('click', () => this.cancel());
  }

  updateInputFields() {
    this.ticketIdInput.value = this.ticketData.ticketId;
    this.customerIdInput.value = this.ticketData.customerId;
    this.issueInput.value = this.ticketData.issue;
    this.descriptionInput.value = this.ticketData.description;
    this.dateInput.value = this.ticketData.date;
  }

  async submit() {
      this.ticketData.issue = this.issueInput.value;
      this.ticketData.description = this.descriptionInput.value;

      if (this.ticketData.issue && this.ticketData.description) {
        try {
          const updates = {
            issue: this.ticketData.issue,
            description: this.ticketData.description,
          };

          const response = await fetch(`http://localhost:8080/minh/tickets/${this.ticketData.ticketId}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates)
          });

          if (!response.ok) throw new Error('Failed to update ticket.');

          const data = await response.json();
          this.messageDisplay.textContent = 'Ticket updated successfully!';
        } catch (error) {
          this.messageDisplay.textContent = 'Error: ' + error.message;
        }
      } else {
        this.messageDisplay.textContent = 'Please complete the editable fields.';
      }
  }

  reset() {
    this.issueInput.value = '';
    this.descriptionInput.value = '';
    this.messageDisplay.textContent = '';
    console.log('Form has been reset.');
  }

  cancel() {
    window.location.href = 'my_tickets.html';
  }
}

document.addEventListener('DOMContentLoaded', () => {
  new SupportTicket();
});
