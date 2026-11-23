package pl.edu.atar.movie.e2e

import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import pl.edu.atar.movie.domain.MovieFacade
import pl.edu.atar.movie.dto.MovieDto
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieE2ESpec extends Specification {

    @LocalServerPort
    int port

    @Autowired
    MovieFacade movieFacade

    WebDriver driver
    WebDriverWait wait

    def setup() {
        ChromeOptions options = new ChromeOptions()
        options.addArguments("--remote-allow-origins=*")

        if (Boolean.getBoolean("e2e.headless")) {
            options.addArguments("--headless=new")
        }
        driver = new ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3))
        wait = new WebDriverWait(driver, Duration.ofSeconds(5))
    }

    def cleanup() {
        driver?.quit()
    }

    // ==========================================
    // Zadanie 1: Test bazowy nagłówka strony głównej
    // ==========================================
    def "should display main page header"() {
        given: "the application main page URL"
        def url = "http://127.0.0.1:${port}/movies.html"

        when: "the user opens the main page in the browser"
        driver.get(url)
        def header = driver.findElement(By.tagName("h1"))

        then: "the correct header is displayed"
        header.isDisplayed()
        header.text == "Biblioteka filmów"
    }

    // ==========================================
    // Zadanie 2: Dodawanie filmu
    // ==========================================
    def "should add a new movie and display it on the main page"() {
        given: "the user is on the main page"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-movie")))

        when: "user clicks 'DODAJ FILM' button"
        driver.findElement(By.id("add-movie")).click()

        then: "user is navigated to add movie page"
        wait.until(ExpectedConditions.urlContains("add-movie.html"))

        when: "user fills out the form with valid movie data and submits"
        driver.findElement(By.id("title")).sendKeys("Incepcja E2E")
        driver.findElement(By.id("image")).sendKeys("./img/movies/system.jpg")
        driver.findElement(By.id("year")).sendKeys("2010")
        driver.findElement(By.id("videoId")).sendKeys("YoHD9XEInc0")
        driver.findElement(By.cssSelector("button[type='submit']")).click()

        then: "user is redirected back to the main movies list"
        wait.until(ExpectedConditions.urlContains("movies.html"))

        and: "the newly added movie is visible in the library"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movies"), "Incepcja E2E"))
        def titles = driver.findElements(By.className("movie-title")).collect { it.text }
        titles.contains("Incepcja E2E")
    }

    // ==========================================
    // Zadanie 2: Szczegóły filmu (z trailerem)
    // ==========================================
    def "should display movie details with YouTube trailer when videoId is present"() {
        given: "the user is on the main page with movies loaded"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card")))

        when: "user clicks on the poster of movie 'System'"
        def systemPoster = driver.findElement(By.cssSelector("img[alt='Plakat filmu System']"))
        systemPoster.click()

        then: "user is navigated to details-movie.html"
        wait.until(ExpectedConditions.urlContains("details-movie.html"))

        and: "movie title is displayed correctly"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movie-title"), "System"))
        driver.findElement(By.id("movie-title")).text == "System"

        and: "trailer iframe is displayed with the correct YouTube URL"
        WebElement trailer = driver.findElement(By.id("trailer-frame"))
        wait.until(ExpectedConditions.visibilityOf(trailer))
        trailer.isDisplayed()
        trailer.getAttribute("src").contains("46qKHq7REI4")

        and: "no video message is hidden"
        !driver.findElement(By.id("no-video-msg")).isDisplayed()
    }

    // ==========================================
    // Zadanie 2: Szczegóły filmu (bez trailera)
    // ==========================================
    def "should display informative message when movie has no trailer videoId"() {
        given: "a movie without trailer exists in the database"
        def movieWithoutTrailer = movieFacade.add(MovieDto.builder()
                .title("Film Bez Trailera E2E")
                .image("./img/movies/hackers.jpg")
                .year(2020)
                .videoId(null)
                .build())

        when: "user opens the details page of this movie directly"
        driver.get("http://127.0.0.1:${port}/details-movie.html?movieId=${movieWithoutTrailer.movieId}")

        then: "movie title is displayed"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movie-title"), "Film Bez Trailera E2E"))

        and: "no trailer message is displayed"
        WebElement noVideoMsg = driver.findElement(By.id("no-video-msg"))
        wait.until(ExpectedConditions.visibilityOf(noVideoMsg))
        noVideoMsg.isDisplayed()
        noVideoMsg.text.contains("Brak dostępnego zwiastuna dla tego filmu.")

        and: "trailer iframe is hidden"
        !driver.findElement(By.id("trailer-frame")).isDisplayed()
    }

    // ==========================================
    // Zadanie 2: Aktualizacja filmu
    // ==========================================
    def "should update movie details from details view and show updated values"() {
        given: "a movie exists in the system"
        def movie = movieFacade.add(MovieDto.builder()
                .title("Stary Tytuł E2E")
                .image("./img/movies/system.jpg")
                .year(2000)
                .videoId("abc123")
                .build())

        and: "user navigates to its details page"
        driver.get("http://127.0.0.1:${port}/details-movie.html?movieId=${movie.movieId}")
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movie-title"), "Stary Tytuł E2E"))

        when: "user clicks 'Edytuj film'"
        driver.findElement(By.id("edit-btn")).click()

        then: "user is on the edit page"
        wait.until(ExpectedConditions.urlContains("edit-movie.html"))

        when: "user modifies the title and year and submits the form"
        WebElement titleInput = driver.findElement(By.id("title"))
        titleInput.clear()
        titleInput.sendKeys("Zaktualizowany Tytuł E2E")

        WebElement yearInput = driver.findElement(By.id("year"))
        yearInput.clear()
        yearInput.sendKeys("2024")

        driver.findElement(By.cssSelector("button[type='submit']")).click()

        then: "user is redirected back to details page with updated title"
        wait.until(ExpectedConditions.urlContains("details-movie.html"))
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movie-title"), "Zaktualizowany Tytuł E2E"))
        driver.findElement(By.id("movie-title")).text == "Zaktualizowany Tytuł E2E"

        when: "user clicks 'POWRÓT' to go to the main page"
        driver.findElement(By.id("back")).click()

        then: "updated movie is visible on the main page list"
        wait.until(ExpectedConditions.urlContains("movies.html"))
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movies"), "Zaktualizowany Tytuł E2E"))
        def titles = driver.findElements(By.className("movie-title")).collect { it.text }
        titles.contains("Zaktualizowany Tytuł E2E")
    }

    // ==========================================
    // Zadanie 2: Usuwanie filmu
    // ==========================================
    def "should delete movie when user confirms deletion in dialog"() {
        given: "a dedicated movie to delete exists"
        def movieToDelete = movieFacade.add(MovieDto.builder()
                .title("Film Do Usunięcia E2E")
                .image("./img/movies/system.jpg")
                .year(2022)
                .videoId("del123")
                .build())

        and: "user opens the main page and sees the movie"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movies"), "Film Do Usunięcia E2E"))

        when: "user clicks 'Usuń film' button for this movie"
        WebElement deleteBtn = driver.findElement(By.cssSelector("button.delete-btn[data-id='${movieToDelete.movieId}']"))
        deleteBtn.click()

        and: "confirms the browser alert dialog"
        wait.until(ExpectedConditions.alertIsPresent())
        Alert alert = driver.switchTo().alert()
        alert.accept()

        then: "the movie disappears from the page"
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("button.delete-btn[data-id='${movieToDelete.movieId}']")
        ))
        def titles = driver.findElements(By.className("movie-title")).collect { it.text }
        !titles.contains("Film Do Usunięcia E2E")
    }

    // ==========================================
    // Zadanie 2: Scenariusze negatywne (walidacja i obsługa błędów)
    // ==========================================
    def "should block submission when adding movie with missing required title"() {
        given: "the user is on add-movie page"
        driver.get("http://127.0.0.1:${port}/add-movie.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-movie-form")))

        when: "user leaves title empty and attempts to submit"
        driver.findElement(By.id("image")).sendKeys("./img/movies/system.jpg")
        driver.findElement(By.id("year")).sendKeys("2023")
        driver.findElement(By.cssSelector("button[type='submit']")).click()

        then: "HTML5 validation prevents form submission and user remains on the same page"
        driver.currentUrl.contains("add-movie.html")
    }

    def "should display error message when opening details of non-existing movie id"() {
        when: "user navigates to details of a non-existing movie id"
        driver.get("http://127.0.0.1:${port}/details-movie.html?movieId=999999")

        then: "error information is displayed to the user"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("movie-title"), "Błąd wczytywania danych"))
        driver.findElement(By.id("movie-title")).text == "Błąd wczytywania danych"
        driver.findElement(By.id("no-video-msg")).text.contains("Nie udało się pobrać szczegółów z serwera.")
    }

    // ==========================================
    // Zadanie 3: Testy wyszukiwarki (filtrowanie filmów)
    // ==========================================
    def "should filter movies by title fragment"() {
        given: "the user is on the main page"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card")))

        when: "user enters 'Tron' in the title search input and clicks search"
        driver.findElement(By.id("search-title")).sendKeys("Tron")
        driver.findElement(By.id("search-btn")).click()

        then: "only movies with 'Tron' in title are displayed"
        def titles = driver.findElements(By.className("movie-title")).collect { it.text }
        titles.size() >= 2
        titles.every { it.toLowerCase().contains("tron") }
        !titles.contains("System")
        !titles.contains("Hakerzy")
    }

    def "should filter movies by production year"() {
        given: "the user is on the main page"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card")))

        when: "user enters '1995' in the year search input and clicks search"
        driver.findElement(By.id("search-year")).sendKeys("1995")
        driver.findElement(By.id("search-btn")).click()

        then: "only movies from year 1995 are displayed"
        def years = driver.findElements(By.className("year")).collect { it.text }
        years.size() >= 2
        years.every { it == "1995" }
        def titles = driver.findElements(By.className("movie-title")).collect { it.text }
        titles.contains("System")
        titles.contains("Hakerzy")
        !titles.contains("Tron")
    }

    def "should filter movies by both title fragment and production year simultaneously"() {
        given: "the user is on the main page"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card")))

        when: "user searches for title 'Tron' and year '1982'"
        driver.findElement(By.id("search-title")).sendKeys("Tron")
        driver.findElement(By.id("search-year")).sendKeys("1982")
        driver.findElement(By.id("search-btn")).click()

        then: "only 'Tron' from 1982 is displayed, while 'Tron: Dziedzictwo' (2010) is filtered out"
        def cards = driver.findElements(By.className("card"))
        cards.size() == 1
        driver.findElement(By.className("movie-title")).text == "Tron"
        driver.findElement(By.className("year")).text == "1982"
    }

    def "should reset filters and restore all movies when reset button is clicked"() {
        given: "a filtered view on the main page"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card")))
        int initialCount = driver.findElements(By.className("card")).size()

        driver.findElement(By.id("search-title")).sendKeys("Tron")
        driver.findElement(By.id("search-btn")).click()
        assert driver.findElements(By.className("card")).size() < initialCount

        when: "user clicks 'Resetuj filtry' button"
        driver.findElement(By.id("reset-btn")).click()

        then: "search inputs are cleared"
        driver.findElement(By.id("search-title")).getAttribute("value") == ""
        driver.findElement(By.id("search-year")).getAttribute("value") == ""

        and: "all original movies are displayed again"
        driver.findElements(By.className("card")).size() == initialCount
    }

    def "should display informative message when no movies match the search criteria"() {
        given: "the user is on the main page"
        driver.get("http://127.0.0.1:${port}/movies.html")
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card")))

        when: "user searches for non-existing title or year"
        driver.findElement(By.id("search-title")).sendKeys("NieistniejacyTytulFilmu123xyz")
        driver.findElement(By.id("search-btn")).click()

        then: "no movie cards are displayed"
        driver.findElements(By.className("card")).isEmpty()

        and: "no-movies message is displayed"
        WebElement noMoviesMsg = driver.findElement(By.id("no-movies-msg"))
        noMoviesMsg.isDisplayed()
        noMoviesMsg.text.contains("Brak filmów w bazie")
    }
}
