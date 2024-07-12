package rainbow.test

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import rainbow.test.data.*

private const val spacedTime = 0

class ApplicationUITesting {
    private lateinit var device: UiDevice
    private lateinit var errorsRepository: ErrorsRepository
    private lateinit var newsRepository: NewsRepository
    private lateinit var userDao: User.Dao

    private fun has(selector: BySelector) = device.wait(Until.hasObject(selector), 10000)
    private fun hasText(text: String) = has(By.text(text))
    private fun hasTextContains(substring: String) = has(By.textContains(substring))
    private fun hasRes(res: String) = has(By.res(res))
    private fun hasError(id1: Int, id2: Int) = hasText(errorsRepository.getError(id1, id2))

    private fun gone(selector: BySelector) = device.wait(Until.gone(selector), 10000)
    private fun goneText(text: String) = gone(By.text(text))
    private fun goneTextContains(substring: String) = gone(By.textContains(substring))
    private fun goneRes(res: String) = gone(By.res(res))

    private fun find(selector: BySelector) = device.wait(Until.findObject(selector), 10000)
    private fun findText(text: String) = find(By.text(text))
    private fun findTextContains(substring: String) = find(By.textContains(substring))
    private fun findRes(res: String) = find(By.res(res))

    @Before
    fun startTesting() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        device = UiDevice.getInstance(instrumentation)
        instrumentation.targetContext.run {
            errorsRepository = ErrorsRepository(
                assets.open("3.錯誤訊息庫_test.json")
                    .readBytes()
                    .decodeToString()
            )
            newsRepository = NewsRepository(
                assets.open("1.最新消息.json")
                    .readBytes()
                    .decodeToString()
            )
            startActivity(packageManager.getLaunchIntentForPackage(packageName))
            assertTrue("startActivity()", has(By.pkg(packageName)))
            userDao = DatabaseRepository.database.userDao
        }
        userDao.deleteAll()
    }

    @After
    fun sleep() {
        Thread.sleep(spacedTime * 1000L)
    }

    private val user = User("Test", "test@domain.com", "ABcd1234")

    @Test
    fun test2_1() {
        assertTrue(hasText("Login"))
    }

    @Test
    fun test2_2() {
        findRes("email").text = "wrong@@domain.com"
        findText("登入").click()
        assertTrue(hasError(1, 1))
    }

    @Test
    fun test2_3() {
        userDao.insert(user)
        findRes("email").text = user.email
        findRes("password").text = user.password
        findText("登入").click()
        assertTrue(hasText("News"))
    }

    @Test
    fun test2_4() {
        userDao.insert(user)
        findRes("email").text = user.email
        findRes("password").text = "Wrong5678"
        findText("登入").click()
        assertTrue(hasError(1, 2))
    }

    @Test
    fun test2_5() {
        findRes("email").text = user.email
        findRes("password").text = user.password
        findText("登入").click()
        assertTrue(hasError(1, 2))
    }

    private fun goSignup() {
        findText("註冊").click()
    }

    @Test
    fun test4_1() {
        fun check(id2: Int) {
            findText("註冊").click()
            assertTrue(hasError(2, id2))
            findText("確認").click()
        }
        goSignup()
        check(2)
        findRes("name").text = user.name
        check(3)
        findRes("email").text = user.email
        check(4)
        findRes("password").text = user.password
        check(1)
    }

    @Test
    fun test4_2() {
        goSignup()
        findRes("name").text = "1234567890A"
        findText("註冊").click()
        assertTrue(hasError(2, 2))
    }

    @Test
    fun test4_3() {
        goSignup()
        findRes("name").text = user.name
        findRes("email").text = "abcdefghijklmnopqrstuvwxyz@domain.com"
        findText("註冊").click()
        assertTrue(hasError(2, 3))
    }

    @Test
    fun test4_4() {
        goSignup()
        findRes("name").text = user.name
        findRes("email").text = "Wrong@@domain.com"
        findText("註冊").click()
        assertTrue(hasError(2, 3))
    }

    @Test
    fun test4_5() {
        fun check(password: String) {
            findRes("password").text = password
            findText("註冊").click()
            assertTrue(hasError(2, 4))
            findText("確認").click()
        }
        goSignup()
        findRes("name").text = user.name
        findRes("email").text = user.email
        check("Short0")
        check("NoNumber")
        check("no0upper")
        check("NO0LOWER")
        check("Too000000000Long")
    }

    @Test
    fun test4_6() {
        fun check(name: String) {
            findRes(name).text = user.password
            assertTrue(goneText(user.password))
            findRes("${name}_toggle").click()
            assertTrue(hasText(user.password))
            findRes("${name}_toggle").click()
            assertTrue(goneText(user.password))
            findRes(name).text = ""
        }
        goSignup()
        check("password")
        check("confirmation")
    }

    @Test
    fun test4_7() {
        fun check(confirmation: String) {
            findRes("confirmation").text = confirmation
            findText("註冊").click()
            assertTrue(hasError(2, 1))
            findText("確認").click()
        }
        goSignup()
        findRes("name").text = user.name
        findRes("email").text = user.email
        findRes("password").text = user.password
        check("Short0")
        check("NoNumber")
        check("no0upper")
        check("NO0LOWER")
        check("Too000000000Long")
    }

    @Test
    fun test4_8() {
        goSignup()
        findRes("name").text = user.name
        findRes("email").text = user.email
        findRes("password").text = user.password
        findRes("confirmation").text = "Wrong5678"
        findText("註冊").click()
        assertTrue(hasError(2, 1))
    }

    @Test
    fun test4_9() {
        goSignup()
        findRes("name").text = user.name
        findRes("email").text = user.email
        findRes("password").text = user.password
        findRes("confirmation").text = user.password
        findText("註冊").click()
        assertTrue(hasText("Login"))
    }

    private fun goHome() {
        userDao.insert(user)
        findRes("email").text = user.email
        findRes("password").text = user.password
        findText("登入").click()
    }

    @Test
    fun test6_1() {
        goHome()
        findRes("go_user").click()
        assertTrue(hasText("個人資訊"))
    }

    @Test
    fun test6_2() {
        goHome()
        findRes("open_drawer").click()
        assertTrue(hasRes("drawer"))
        findRes("close_drawer").click()
        assertTrue(goneRes("drawer"))
    }

    @Test
    fun test6_3() {
        goHome()
        newsRepository.newsList.take(5).forEach {
            assertTrue(hasTextContains("${it.id}. ${it.title.take(8)}"))
        }
        assertTrue(goneTextContains("6. ${newsRepository.newsList[6].title.take(8)}"))
    }

    @Test
    fun test6_4() {
        goHome()
        newsRepository.newsList.take(5).forEach {
            findTextContains("${it.id}. ${it.title.take(8)}").click()
            assertTrue(hasText("${it.id}. ${it.title}"))
            assertTrue(hasText(it.organizer))
            assertTrue(hasText(it.publishDate))
            assertTrue(hasText(newsRepository.content))
            findRes("back").click()
        }
    }

    private fun goUser() {
        goHome()
        findRes("go_user").click()
    }

    @Test
    fun test8_1() {
        goUser()
        assertTrue(hasText(user.name))
    }

    @Test
    fun test8_2() {
        goUser()
        assertTrue(hasText(user.email))
    }

    @Test
    fun test8_3() {
        goUser()
        findText("修改密碼").click()
        assertTrue(hasRes("modification"))
    }

    @Test
    fun test8_4() {
        goUser()
        findText("登出").click()
        assertTrue(hasText("Login"))
    }

    private val newPassword = "NewP5678"

    private fun goModification() {
        goUser()
        findText("修改密碼").click()
    }

    private fun modifyPassword() {
        findRes("password").text = newPassword
        findRes("confirmation").text = newPassword
        findText("修改").click()
    }

    @Test
    fun test10_1() {
        fun check(password: String) {
            findRes("password").text = password
            findText("修改").click()
            assertTrue(hasError(2, 4))
            findText("確認").click()
        }
        goModification()
        check("Short0")
        check("NoNumber")
        check("no0upper")
        check("NO0LOWER")
        check("Too000000000Long")
    }

    @Test
    fun test10_2() {
        goModification()
        findRes("password").text = newPassword
        findRes("confirmation").text = "Wrong5678"
        findText("修改").click()
        assertTrue(hasError(2, 1))
    }

    @Test
    fun test10_3() {
        goModification()
        modifyPassword()
        assertTrue(hasText("個人資訊"))
    }

    @Test
    fun test10_4() {
        goModification()
        modifyPassword()
        findText("登出").click()
        findRes("email").text = user.email
        findRes("password").text = user.password
        findText("登入").click()
        assertTrue(hasError(1, 2))
    }

    @Test
    fun test10_5() {
        goModification()
        modifyPassword()
        findText("登出").click()
        findRes("email").text = user.email
        findRes("password").text = newPassword
        findText("登入").click()
        assertTrue(hasText("News"))
    }

    private fun goNewsList() {
        goHome()
        findRes("open_drawer").click()
        findText("最新消息").click()
    }

    @Test
    fun test12_1() {
        goNewsList()
        val news = newsRepository.newsList.maxBy { it.id }
        assertTrue(hasTextContains("${news.id}. ${news.title.take(8)}"))
        assertTrue(hasText(news.organizer))
        assertTrue(hasText(news.publishDate))
        assertTrue(hasText("觀看次數：${news.views}"))
    }

    @Test
    fun test12_2() {
        goNewsList()
        newsRepository.newsList.sortedByDescending { it.id }.take(5).forEach {
            assertTrue(hasTextContains("${it.id}. ${it.title.take(8)}"))
        }
    }

    @Test
    fun test12_3() {
        fun check(newsList: List<News>) {
            newsList.take(5).forEach {
                assertTrue(hasTextContains("${it.id}. ${it.title.take(8)}"))
            }
        }
        goNewsList()
        newsRepository.newsList.run {
            check(sortedByDescending { it.id })
            findText("編號").click()
            check(sortedBy { it.id })
            findText("發佈日期").click()
            check(sortedByDescending { it.publishDate })
            findText("發佈日期").click()
            check(sortedBy { it.publishDate })
        }
    }

    @Test
    fun test12_4() {
        goNewsList()
        newsRepository.newsList.sortedByDescending { it.id }.take(5).forEach {
            findTextContains("${it.id}. ${it.title.take(8)}").click()
            assertTrue(hasText("${it.id}. ${it.title}"))
            assertTrue(hasText(it.organizer))
            assertTrue(hasText(it.publishDate))
            assertTrue(hasText(newsRepository.content))
            findRes("back").click()
        }
    }

    @Test
    fun test12_5() {
        goNewsList()
        val target = "感"
        findRes("keyword").text = target
        findRes("search").click()
        newsRepository.newsList
            .filter { it.title.contains(target, ignoreCase = true) }
            .sortedByDescending { it.id }
            .forEach {
                assertTrue(hasTextContains("${it.id}. ${it.title.take(8)}"))
            }
    }

    private val news get() = newsRepository.newsList.first()

    private fun goNews() {
        goHome()
        findTextContains("${news.id}. ${news.title.take(8)}").click()
    }

    @Test
    fun test14_1() {
        goNews()
        assertTrue(hasTextContains("${news.id}. ${news.title.take(8)}"))
    }

    @Test
    fun test14_2() {
        goNews()
        assertTrue(hasText(newsRepository.content))
    }

    @Test
    fun test14_3() {
        goNews()
        assertTrue(hasText(news.publishDate))
    }

    @Test
    fun test14_4() {
        goNews()
        assertTrue(hasText(news.organizer))
    }

    @Test
    fun test14_5() {
        goNews()
        findRes("back").click()
        assertTrue(hasText("News"))
    }
}
