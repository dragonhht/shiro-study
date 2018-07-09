package hht.dragon.shiro

import hht.dragon.shiro.utils.RedisUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest
class ShiroStudyApplicationTests {

    @Resource
    private lateinit var redis : RedisUtil

    @Test
    fun contextLoads() {
        redis.set("hello", "session")
    }

}
