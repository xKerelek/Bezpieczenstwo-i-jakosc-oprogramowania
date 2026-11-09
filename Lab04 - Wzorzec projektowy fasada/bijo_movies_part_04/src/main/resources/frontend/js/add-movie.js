document.addEventListener('DOMContentLoaded', () => {
    const backBtn = document.getElementById('back');
    const form = document.getElementById('add-movie-form');
    const targetDiv = document.getElementById('target');

    // Obsługa przycisku nawigacji
    backBtn.addEventListener('click', () => {
        window.location.href = 'movies.html';
    });

    // Przechwycenie wysłania formularza
    form.addEventListener('submit', async (e) => {
        e.preventDefault(); // Zatrzymuje domyślne przeładowanie strony

        const movieData = {
            title: document.getElementById('title').value,
            image: document.getElementById('image').value,
            year: document.getElementById('year').value,
            videoId: document.getElementById('videoId').value || null
        };

        try {
            const response = await fetch('http://127.0.0.1:8080/api/movies', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(movieData)
            });

            if (!response.ok) {
                throw new Error('Serwer zwrócił błąd');
            }

            console.log('Dodano pomyślnie: ', movieData);

            // Jeśli sukces, wracamy do listy
            window.location.href = 'movies.html';

        } catch (error) {
            console.error('Błąd zapisu:', error);
            targetDiv.innerHTML = "<div class='error-msg'>Coś poszło nie tak... Usługa nie działa poprawnie :/</div>";
        }
    });
});