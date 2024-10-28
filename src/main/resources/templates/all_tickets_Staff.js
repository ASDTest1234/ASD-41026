document.addEventListener('DOMContentLoaded', function() {
    let ticketsData = []; // Store the fetched tickets data
    const tableBody = document.getElementById('tickets-table').getElementsByTagName('tbody')[0];
    const loadingMessage = document.getElementById('loading-message');
    let colorMap = {}; // Mapping to assign colors to customer IDs
    let colorCounter = 0; // Counter to track how many colors have been used
    const colors = ["#FFDDC1", "#FFABAB", "#FFC3A0", "#FF677D", "#DDF6E4", "#F9FBCB", "#D4C5E1"]; // Array of colors to use

    fetch('http://localhost:8080/minh/tickets')
        .then(response => response.json())
        .then(tickets => {
            ticketsData = tickets; // Save the fetched ticket data
            renderTable(ticketsData); // Render the table with the initial data
            loadingMessage.style.display = 'none';
        })
        .catch(error => {
            console.error('Error fetching tickets:', error);
        });

    // Function to render the table rows
    function renderTable(tickets) {
        tableBody.innerHTML = ''; // Clear previous rows
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

            row.addEventListener('click', function() {
                window.location.href = `a_ticket_Staff.html?ticketId=${ticket.ticketId}`;
            });

            tableBody.appendChild(row);
        });
    }

    // Function to color rows by customer ID and group them
    function applyColorByCustomerID() {
        const sortedTickets = [...ticketsData].sort((a, b) => {
            return a.customerId.localeCompare(b.customerId); // Sort by customerId
        });

        const uniqueCustomers = new Set();
        tableBody.innerHTML = ''; // Clear current rows

        // Create color mapping for unique customer IDs
        sortedTickets.forEach(ticket => {
            uniqueCustomers.add(ticket.customerId);
        });

        uniqueCustomers.forEach(customerId => {
            // Assign a color if not yet assigned
            if (!colorMap[customerId] && colorCounter < colors.length) {
                colorMap[customerId] = colors[colorCounter++];
            }

            // Get tickets that belong to this customerId
            const customerTickets = sortedTickets.filter(ticket => ticket.customerId === customerId);

            // Generate rows for these tickets
            customerTickets.forEach(ticket => {
                const row = document.createElement('tr');
                row.style.backgroundColor = colorMap[customerId]; // Set background color

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

                row.addEventListener('click', function() {
                    window.location.href = `a_ticket_Staff.html?ticketId=${ticket.ticketId}`;
                });

                tableBody.appendChild(row); // Append the row to the table body
            });
        });
    }

    // Function to sort tickets by date
    function sortByDate() {
        const sortedTickets = [...ticketsData].sort((a, b) => {
            const dateA = new Date(a.date);
            const dateB = new Date(b.date);
            return dateB - dateA; // Sort in descending order
        });
        renderTable(sortedTickets); // Render sorted table
        clearColorMap(); // Clear any color mappings
    }

    // Function to sort tickets by customer ID and apply colors
    function sortByCustomerID() {
        applyColorByCustomerID(); // Render sorted table with colors applied
    }

    // Function to clear the color mapping
    function clearColorMap() {
        colorMap = {}; // Reset the color mapping
        colorCounter = 0; // Reset color counter
    }

    // Attach event listeners to dropdown options
    document.querySelector('.dropdown-content a:nth-child(1)').addEventListener('click', (e) => {
        e.preventDefault(); // Prevent default link behavior
        sortByDate(); // Sort tickets by date
    });

    document.querySelector('.dropdown-content a:nth-child(2)').addEventListener('click', (e) => {
        e.preventDefault(); // Prevent default link behavior
        sortByCustomerID(); // Sort tickets by customer ID and apply colors
    });
});
