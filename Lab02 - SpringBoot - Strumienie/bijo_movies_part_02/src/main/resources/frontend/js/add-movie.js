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

        const rawVideoId = document.getElementById('videoId').value.trim();
        const movieData = {
            title: document.getElementById('title').value,
            image: document.getElementById('image').value,
            year: document.getElementById('year').value,
            videoId: extractVideoId(rawVideoId)
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

    function extractVideoId(urlOrId) {
        if (!urlOrId) return "";
        try {
            if (urlOrId.includes("watch?v=")) {
                return new URL(urlOrId).searchParams.get("v") || urlOrId;
            }
            if (urlOrId.includes("youtu.be/")) {
                return urlOrId.split("youtu.be/")[1].split("?")[0];
            }
        } catch (e) {
            // fallback jeśli nie uda się sparsować jako URL
        }
        return urlOrId;
    }
});