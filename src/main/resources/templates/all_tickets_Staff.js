document.addEventListener('DOMContentLoaded', function() {
    let ticketsData = []; // Store the fetched tickets data
    const tableBody = document.getElementById('tickets-table').getElementsByTagName('tbody')[0];
    const loadingMessage = document.getElementById('loading-message');
    let colorMap = {}; // Mapping to assign colors to customer IDs
    let lastColor = null; // To keep track of the last used color

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

    // Function to generate a random RGB color
    function getRandomColor() {
        const r = Math.floor(Math.random() * 256);
        const g = Math.floor(Math.random() * 256);
        const b = Math.floor(Math.random() * 256);
        return `rgb(${r}, ${g}, ${b})`;
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
            // Assign a random color for each customer ID
            let assignedColor;
            do {
                assignedColor = getRandomColor();
            } while (assignedColor === lastColor); // Ensure it's not the same as the last used color

            colorMap[customerId] = assignedColor; // Store the assigned color
            lastColor = assignedColor; // Update the last color used

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

                // Append the row with random background color to the table body
                tableBody.appendChild(row);
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

