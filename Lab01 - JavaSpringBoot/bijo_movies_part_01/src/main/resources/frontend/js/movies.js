document.addEventListener('DOMContentLoaded', () => {
    const moviesContainer = document.getElementById('movies');
    const addMovieBtn = document.getElementById('add-movie');

    // Obsługa przycisku nawigacji
    addMovieBtn.addEventListener('click', () => {
        window.location.href = 'add-movie.html';
    });

    // Pobranie danych przy uruchomieniu
    fetchMovies();

    async function fetchMovies() {
        try {
            const response = await fetch('http://127.0.0.1:8080/api/movies');

            if (!response.ok) {
                throw new Error('Błąd sieci lub serwera');
            }

            const data = await response.json();
            console.log('Pobrane dane:', data);

            renderMovies(data);
        } catch (error) {
            console.error('Błąd pobierania:', error);
            moviesContainer.innerHTML = `
                <div class="error-msg" style="grid-column: 1 / -1;">
                    Mam problem z wyświetleniem danych... :/ <br>
                    (Upewnij się, że aplikacja Spring Boot jest uruchomiona)
                </div>`;
        }
    }

    function renderMovies(movies) {
        if (!movies || movies.length === 0) {
            moviesContainer.innerHTML = "<p style='grid-column: 1 / -1; text-align: center;'>Brak filmów w bazie.</p>";
            return;
        }

        const html = movies.map(movie => {
            const imageUrl = isImage(movie.image) ? movie.image : getPlaceholder();
            const year = movie.year ? movie.year : "????";

            return `
                <div class="card">
                    <img src="${imageUrl}" alt="Plakat filmu ${movie.title}">
                    <div class="card-content">
                        <h3>${movie.title}</h3>
                        <p class="title-label">Rok produkcji</p>
                        <p class="year">${year}</p>
                        <button onclick="deleteMovie(${movie.movieId})">ID: ${movie.movieId}</button>
                    </div>
                </div>
            `;
        }).join(''); // join łączący tablicę w jeden długi string HTML

        moviesContainer.innerHTML = html;
    }

    window.deleteMovie = async function(id) {
        try {
            const response = await fetch(`http://127.0.0.1:8080/api/movies/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                throw new Error('Błąd podczas usuwania filmu');
            }

            console.log(`Film o ID ${id} został usunięty`);
            fetchMovies(); // odświeżenie listy po usunięciu
        } catch (error) {
            console.error('Błąd usuwania:', error);
        }
    }

    // Funkcje pomocnicze
    function isImage(url) {
        if (!url) return false;
        const lowerUrl = url.toLowerCase();
        return lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".png") || lowerUrl.endsWith(".jpeg");
    }

    function getPlaceholder() {
        return './img/placeholder.png';
    }
});