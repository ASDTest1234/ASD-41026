let currentResponseId;

document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    currentResponseId = urlParams.get('response_id');

    if (currentResponseId) {
        fetchResponseData(currentResponseId);
    } else {
        document.getElementById("loading-message").textContent = "No response ID found in the URL.";
    }

    document.getElementById("submit-button").addEventListener("click", updateResponse);

    // Add event listener for the reset button
    document.getElementById("reset-button").addEventListener("click", resetResponseInput);

    // Add event listener for the cancel button
    document.getElementById("cancel-button").addEventListener("click", function() {
        window.location.href = 'all_responses_Staff.html'; // Redirect to the specified page
    });
});

function fetchResponseData(responseId) {
    const url = `http://localhost:8080/minh/response/${responseId}`; // Update the URL accordingly
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            populateTable(data);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            document.getElementById("loading-message").textContent = "Failed to load response data.";
        });
}

function populateTable(data) {
    const tableBody = document.getElementById("responses-table").querySelector("tbody");
    tableBody.innerHTML = ''; // Clear any existing rows

    const row = document.createElement("tr");
    const responseId = data.response_id;
    const responseContent = data.response;
    const timestamp = data.id.timestamp;
    const date = new Date(data.id.date);
    const formattedDate = date.toLocaleString();

    // Create a new row with editable input for the response
    row.innerHTML = `
      <td>${responseId}</td>
      <td><input type="text" id="response-input" value="${responseContent}"></td>
      <td>${timestamp}</td>
      <td>${formattedDate}</td>
    `;

    tableBody.appendChild(row);
    document.getElementById("loading-message").style.display = "none"; // Hide the loading message
}

function updateResponse() {
    const newResponse = document.getElementById("response-input").value; // Get the edited response input
    const payload = {
        response: newResponse
    };

    const url = `http://localhost:8080/minh/response/update/${currentResponseId}`; // Update the URL for the PUT request
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Update successful:', data);
        // Inform the user that the update was successful
        alert('Response updated successfully!');
        // Optionally re-fetch the data or update the UI accordingly
    })
    .catch(error => {
        console.error('There was a problem with the update operation:', error);
        // Inform the user if there was an error
        alert('Failed to update the response. Please try again.');
    });
}

// Function to clear the response input field
function resetResponseInput() {
    document.getElementById("response-input").value = ''; // Clear the input field
}
