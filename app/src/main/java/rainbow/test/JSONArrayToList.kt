package rainbow.test

import org.json.JSONArray
import org.json.JSONObject

fun <T> JSONArray.toList(factory: (JSONObject) -> T) = List(length(), ::getJSONObject).map(factory)
