import kotlinx.serialization.Serializable

@Serializable
data class PopularMovies(var results : MutableList<Popular>? = null , val page : Int? = Int.MIN_VALUE , val total_pages : Int? = Int.MIN_VALUE , val total_results : Int? = Int.MIN_VALUE)
@Serializable
data class Popular(var adult : Boolean? = false, var backdrop_path : String? = null,
                   var id : Int? = Int.MIN_VALUE, var genre_ids : MutableList<String>? = null,
                   var original_language : String? = null, var original_title : String? = null,
                   var overview : String? = null, var popularity : Double? = 0.0,
                   var poster_path : String? = null, var release_date : String? = null,
                   var title : String? = null, var video : Boolean? = false,
                   var vote_average : Double? = 0.0, var vote_count : Int? = Int.MIN_VALUE)