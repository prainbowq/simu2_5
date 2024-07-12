package rainbow.test.data

import org.json.JSONArray

class ErrorsRepository(errorsString: String) {
    private val errors = JSONArray(errorsString)
    fun getError(id1: Int, id2: Int) = errors
        .getJSONObject(id1 - 1)
        .getString("ErrorMeg$id1-$id2")
}
