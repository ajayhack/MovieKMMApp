import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel {
    private val popularMovieStreamData = MutableStateFlow<MutableList<Popular>?>(mutableListOf())
    val streamData = popularMovieStreamData.asStateFlow()
    private var listOfPopularMovies : MutableList<Popular> ? = null
    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }
    
    suspend fun getPopularMovies() : MutableList<Popular>? {
        listOfPopularMovies = fetchPopularMovies().results
        popularMovieStreamData.value = listOfPopularMovies
        /*viewModelScope.launch {
            listOfPopularMovies = fetchPopularMovies().results
            popularMovieStreamData.value = listOfPopularMovies
        }*/
        return listOfPopularMovies
    }

    private suspend fun fetchPopularMovies(): PopularMovies =
        httpClient
            .get(TMDBUrl)
            .body<PopularMovies>()
    
   /* override fun onCleared() {
        httpClient.close()
    }*/
}