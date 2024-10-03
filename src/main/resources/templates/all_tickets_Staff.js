document.addEventListener('DOMContentLoaded', function() {
    fetch('http://localhost:8080/minh/tickets')
        .then(response => response.json())
        .then(tickets => {
            const tableBody = document.getElementById('tickets-table').getElementsByTagName('tbody')[0];
            const loadingMessage = document.getElementById('loading-message');

            tickets.forEach(ticket => {
                const row = document.createElement('tr');

                const idCell = document.createElement('td');
                idCell.textContent = ticket.id;
                row.appendChild(idCell);

                const ticketIdCell = document.createElement('td');
                ticketIdCell.textContent = ticket.ticketId;
                row.appendChild(ticketIdCell);

                const customerIdCell = document.createElement('td');
                customerIdCell.textContent = ticket.customerId;
                row.appendChild(customerIdCell);

                const issueCell = document.createElement('td');
                issueCell.textContent = ticket.issue;
                row.appendChild(issueCell);

                const descriptionCell = document.createElement('td');
                descriptionCell.textContent = ticket.description;
                row.appendChild(descriptionCell);

                const dateCell = document.createElement('td');
                dateCell.textContent = ticket.date;
                row.appendChild(dateCell);

                tableBody.appendChild(row);
            });

            // Remove loading message
            loadingMessage.style.display = 'none';
        })
        .catch(error => {
            console.error('Error fetching tickets:', error);
        });
});
