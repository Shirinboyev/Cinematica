    function showContent(section) {
    const content = document.getElementById('content');
    let message = '';
    switch (section) {
    case 'profile':
    message = 'Profile details will be shown here.';
    break;
    case 'showMovies':
    message = 'Here are all the movies in the database.';
    break;
    case 'showHalls':
    message = 'Here are all the halls available.';
    break;
}
    content.innerHTML = message;
}

    function showModal() {
    const modal = document.getElementById('myModal');
    modal.style.display = "block";
}

    function closeModal() {
    const modal = document.getElementById('myModal');
    modal.style.display = "none";
}

    function addMovie() {
    const title = document.getElementById('movieTitle').value;
    const category = document.getElementById('movieCategory').value;
    const description = document.getElementById('movieDescription').value;
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;
    const posterImage = document.getElementById('posterImage').files[0];

    // Check if all fields are filled
    if (!title || !category || !description || !startTime || !endTime || !posterImage) {
    alert('Please fill in all fields before adding the movie.');
    return;
}

    // Display the movie details
    alert(`Movie Added: \nTitle: ${title}\nCategory: ${category}\nDescription: ${description}\nStart Time: ${startTime}\nEnd Time: ${endTime}\nPoster Image: ${posterImage.name}`);

    closeModal();
}


    window.onclick = function(event) {
    const modal = document.getElementById('myModal');
    if (event.target == modal) {
    modal.style.display = "none";
}
}