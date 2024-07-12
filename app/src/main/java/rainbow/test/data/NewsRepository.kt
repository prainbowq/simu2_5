package rainbow.test.data

import org.json.JSONArray
import rainbow.test.toList

class NewsRepository(newsListString: String) {
    private val newsListJson = JSONArray(newsListString)
    val newsList = newsListJson.toList(News::fromJson)
    val content = newsListJson.getJSONObject(0).getString("Content")
}
