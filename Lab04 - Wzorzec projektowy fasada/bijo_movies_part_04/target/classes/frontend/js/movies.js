document.addEventListener('DOMContentLoaded', () => {
    const moviesContainer = document.getElementById('movies');
    const addMovieBtn = document.getElementById('add-movie');

    // Obsługa przycisku nawigacji do dodawania filmu
    addMovieBtn.addEventListener('click', () => {
        window.location.href = 'add-movie.html';
    });

    // Delegacja zdarzeń wewnątrz kontenera filmów
    moviesContainer.addEventListener('click', async (event) => {
        // 1. Kliknięcie w obrazek filmu -> przejście do szczegółów
        const movieImg = event.target.closest('.movie-img');
        if (movieImg) {
            const movieId = movieImg.dataset.id;
            window.location.href = `details-movie.html?movieId=${movieId}`;
            return;
        }

        // 2. Kliknięcie w przycisk usuwania
        const deleteBtn = event.target.closest('.delete-btn');
        if (deleteBtn) {
            const movieId = deleteBtn.dataset.id;

            if (confirm(`Czy na pewno chcesz usunąć film o ID: ${movieId}?`)) {
                await deleteMovie(movieId);
            }
        }
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

    // Usługa usuwania filmu
    async function deleteMovie(id) {
        try {
            const response = await fetch(`http://127.0.0.1:8080/api/movies/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                throw new Error('Nie udało się usunąć filmu');
            }

            console.log(`Pomyślnie usunięto film o ID: ${id}`);

            // Odświeżenie listy filmów po usunięciu
            fetchMovies();
        } catch (error) {
            console.error('Błąd usuwania:', error);
            alert('Wystąpił błąd podczas usuwania filmu.');
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
                    <img src="${imageUrl}" alt="Plakat filmu ${movie.title}" class="movie-img" data-id="${movie.movieId}" style="cursor: pointer;">
                    <div class="card-content">
                        <h3>${movie.title}</h3>
                        <p class="title-label">Rok produkcji</p>
                        <p class="year">${year}</p>
                        <button class="delete-btn" data-id="${movie.movieId}">Usuń film</button>
                    </div>
                </div>
            `;
        }).join('');

        moviesContainer.innerHTML = html;
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