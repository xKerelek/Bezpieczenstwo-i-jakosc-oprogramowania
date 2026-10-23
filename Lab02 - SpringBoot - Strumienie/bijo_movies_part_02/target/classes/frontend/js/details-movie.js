document.addEventListener('DOMContentLoaded', () => {
    const searchParams = new URLSearchParams(window.location.search);
    const movieId = searchParams.get('movieId');

    const backBtn = document.getElementById('back');
    const titleHeading = document.getElementById('title');
    const movieTitleHeading = document.getElementById('movie-title');
    const trailerFrame = document.getElementById('trailer-frame');
    const noVideoMsg = document.getElementById('no-video-msg');

    // Przycisk powrotu do listy filmów
    backBtn.addEventListener('click', () => {
        window.location.href = 'movies.html';
    });

    // Walidacja obecności ID w adresie URL
    if (!movieId) {
        alert('Brak identyfikatora filmu w adresie URL!');
        window.location.href = 'movies.html';
        return;
    }

    // Pobranie danych szczegółowych filmu z API
    fetch(`http://127.0.0.1:8080/api/movies/${movieId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Nie udało się pobrać danych filmu.');
            }
            return response.json();
        })
        .then(data => {
            console.log('Szczegóły filmu:', data);

            // Wyświetlenie tytułu
            const movieTitle = data.title || 'Brak tytułu';
            titleHeading.textContent = movieTitle;
            movieTitleHeading.textContent = movieTitle;

            // Obsługa wideo (jeśli videoId istnieje w JSON-ie)
            if (data.videoId && data.videoId.trim() !== "") {
                trailerFrame.src = `https://www.youtube.com/embed/${data.videoId}?autoplay=1`;
                trailerFrame.style.display = 'block';
            } else {
                noVideoMsg.style.display = 'block';
            }
        })
        .catch(error => {
            console.error('Błąd:', error);
            movieTitleHeading.textContent = 'Błąd wczytywania danych';
            noVideoMsg.textContent = 'Nie udało się pobrać szczegółów z serwera.';
            noVideoMsg.style.display = 'block';
        });
});