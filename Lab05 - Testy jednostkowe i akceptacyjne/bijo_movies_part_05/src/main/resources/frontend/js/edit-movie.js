document.addEventListener('DOMContentLoaded', () => {
    const searchParams = new URLSearchParams(window.location.search);
    const movieId = searchParams.get('movieId');

    const backBtn = document.getElementById('back');
    const form = document.getElementById('edit-movie-form');
    const targetDiv = document.getElementById('target');

    // Przycisk powrotu do szczegółów filmu
    backBtn.addEventListener('click', () => {
        window.location.href = `details-movie.html?movieId=${movieId}`;
    });

    // Walidacja obecności ID w adresie URL
    if (!movieId) {
        alert('Brak identyfikatora filmu w adresie URL!');
        window.location.href = 'movies.html';
        return;
    }

    // Pobranie aktualnych danych filmu i wstępne wypełnienie formularza
    fetch(`http://127.0.0.1:8080/api/movies/${movieId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Nie udało się pobrać danych filmu.');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('title').value = data.title || '';
            document.getElementById('image').value = data.image || '';
            document.getElementById('year').value = data.year || '';
            document.getElementById('videoId').value = data.videoId || '';
        })
        .catch(error => {
            console.error('Błąd pobierania danych:', error);
            targetDiv.innerHTML = "<div class='error-msg'>Nie udało się załadować danych filmu.</div>";
        });

    // Przechwycenie wysłania formularza i wywołanie PUT /api/movies/{movieId}
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const movieData = {
            title: document.getElementById('title').value,
            image: document.getElementById('image').value,
            year: parseInt(document.getElementById('year').value, 10) || null,
            videoId: document.getElementById('videoId').value || null
        };

        try {
            const response = await fetch(`http://127.0.0.1:8080/api/movies/${movieId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(movieData)
            });

            if (!response.ok) {
                throw new Error('Serwer zwrócił błąd');
            }

            console.log('Zaktualizowano pomyślnie: ', movieData);

            // Po zapisie wracamy do szczegółów filmu
            window.location.href = `details-movie.html?movieId=${movieId}`;

        } catch (error) {
            console.error('Błąd zapisu:', error);
            targetDiv.innerHTML = "<div class='error-msg'>Coś poszło nie tak... Usługa nie działa poprawnie :/</div>";
        }
    });
});

