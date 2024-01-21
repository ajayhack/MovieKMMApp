import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        var currentMovieDetail : Popular? = null
        val appViewModel = AppViewModel()
        var isNavigateToDetailPage by rememberSaveable { mutableStateOf(false) }
        var isPopularMovieListHasData by rememberSaveable { mutableStateOf(false) }
        var popularMoviesStream : MutableList<Popular>? = mutableListOf()

        CoroutineScope(Dispatchers.IO).launch {
            popularMoviesStream = appViewModel.getPopularMovies()
            Logger.i("PopularMovies:- " + popularMoviesStream?.toString())
            isPopularMovieListHasData = popularMoviesStream?.isNotEmpty() == true
        }
        if(isPopularMovieListHasData || isNavigateToDetailPage){
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color.Black,
                        title = {
                            Text("Popular Movies" , fontSize = 16.sp , fontFamily = FontFamily.SansSerif , color = Color.White)
                        } ,
                        navigationIcon = {
                            if(isNavigateToDetailPage){
                                IconButton(onClick = {
                                    currentMovieDetail = null
                                    isNavigateToDetailPage = false
                                    Logger.i("PopularMovies:- " + popularMoviesStream?.toString())
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Exit App" ,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    )
                }
            ) {
                Logger.i("PopularMovies:- " + popularMoviesStream?.toString())
                if(!isNavigateToDetailPage){
                    Column(Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = popularMoviesStream?.isNotEmpty() ?: false) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(180.dp),
                                contentPadding = PaddingValues(5.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(items = popularMoviesStream ?: emptyList()) {currentMovie ->
                                    Column {
                                        KamelImage(
                                            resource = asyncPainterResource(TMDBImageLoadURL + currentMovie.poster_path),
                                            contentDescription = "",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxWidth().aspectRatio(0.56f).clickable {
                                                currentMovieDetail = currentMovie
                                                isNavigateToDetailPage = true
                                                Logger.i("MovieName:- " + currentMovie.title) },
                                        )
                                        Text(
                                            modifier = Modifier.wrapContentSize(),
                                            text = currentMovie.title ?: "" ,
                                            fontSize = 14.sp ,
                                            fontFamily = FontFamily.SansSerif ,
                                            textAlign = TextAlign.Start ,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if(isNavigateToDetailPage){
                    Logger.i("currentMovieDetail:- $currentMovieDetail")
                    InflateMovieDetails(currentMovieDetail)
                }
            }
        }
    }
}

@Composable
fun InflateMovieDetails(movieItem : Popular?){
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())){
        KamelImage(
            resource = asyncPainterResource(TMDBImageLoadURL + movieItem?.poster_path),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().aspectRatio(0.56f).clickable { Logger.i("MovieName:- " + movieItem?.title) },
        )
        Text(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            text = ("Language: " + movieItem?.original_language) ?: "",
            fontSize = 14.sp ,
            fontFamily = FontFamily.SansSerif ,
            textAlign = TextAlign.Start ,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            text = ("Rating: " + if(movieItem?.adult == true) "A" else "U") ?: "",
            fontSize = 14.sp ,
            fontFamily = FontFamily.SansSerif ,
            textAlign = TextAlign.Start ,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            text = ("Release Date: " + movieItem?.release_date) ?: "",
            fontSize = 14.sp ,
            fontFamily = FontFamily.SansSerif ,
            textAlign = TextAlign.Start ,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            text = ("Title: " + movieItem?.title) ?: "",
            fontSize = 16.sp ,
            fontFamily = FontFamily.SansSerif ,
            textAlign = TextAlign.Start ,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            text = ("Popularity: " + movieItem?.vote_average?.toInt() + "/10") ?: "",
            fontSize = 16.sp ,
            fontFamily = FontFamily.SansSerif ,
            textAlign = TextAlign.Start ,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            text = ("Overview: " + "\n \n" + movieItem?.overview) ?: "",
            fontSize = 14.sp ,
            fontFamily = FontFamily.SansSerif ,
            textAlign = TextAlign.Start ,
            color = Color.Gray
        )
    }
}