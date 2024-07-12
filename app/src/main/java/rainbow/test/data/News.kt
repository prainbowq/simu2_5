package rainbow.test.data

import org.json.JSONObject

data class News(
    val id: Int,
    val publishDate: String,
    val title: String,
    val organizer: String,
    val views: Int
) {
    companion object {
        fun fromJson(json: JSONObject) = News(
            id = json.getInt("ID"),
            publishDate = json.getString("Publish Date"),
            title = json.getString("Title"),
            organizer = json.getString("Organizer"),
            views = json.getInt("Views"),
        )
    }
}
