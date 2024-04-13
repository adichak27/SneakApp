package com.cs4520.assignment4.data

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4520.sneak.data.ApiService
import com.cs4520.sneak.data.DatabaseClient
import com.cs4520.sneak.data.ShoeRepository
import com.cs4520.sneak.data.SneakApi
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.ShoeDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class ShoeRepoTest {

    private val fakeShoeDao = FakeShoeDao()

    private val fakeContext = mockk<Context>()

    private val fakeApiService = mockk<ApiService>()

    private lateinit var shoeRepo: ShoeRepository

    private lateinit var mockShoes: List<Shoe>






    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        mockkObject(DatabaseClient)
        every { DatabaseClient.getDatabase(fakeContext).shoeDao() } returns fakeShoeDao

        mockkObject(SneakApi)
        every { SneakApi.apiService } returns fakeApiService

        shoeRepo = ShoeRepository(fakeContext)

        mockShoes = listOf(
            Shoe("Samba", "Adidias", "lifestyle", 100.00, 0),
            Shoe("Air Force", "Nike", "lifestyle", 90.00, 1),
            Shoe("Chuck Taylor All Star", "Converse", "lifestyle", 55.00, 2)
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllShoesSuccess() = runTest {


        coEvery { fakeApiService.getShoes() }  returns Response.success(mockShoes)

        val actual = shoeRepo.getAllShoes()


        assertEquals(mockShoes, actual)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllShoesFailureFirstTimeGetsFromEmptyDB() = runTest {
        coEvery { fakeApiService.getShoes() }  returns Response.error(400, ResponseBody.create(null,"Failed to get shoes"))

        // calling shoeRepo.getAllShoes() fails to get shoes from api
        val actual = shoeRepo.getAllShoes()

        // verify that shoeDao.getAllShoes() is called
        assertEquals(emptyList<Shoe>(), actual )


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllShoesFromDBAfterSuccessfulOnceLoadThenFail() = runTest {
        // load successful first time
        coEvery { fakeApiService.getShoes() }  returns Response.success(mockShoes)

        val firstSuccessfulLoad = shoeRepo.getAllShoes()


        assertEquals(mockShoes, firstSuccessfulLoad)

        // load fails, goes to repo
        coEvery { fakeApiService.getShoes() }  returns Response.error(400, ResponseBody.create(null,"Failed to get shoes"))

        // calling shoeRepo.getAllShoes() fails to get shoes from api
        val secondLoad = shoeRepo.getAllShoes()


        // verify that shoeDao.getAllShoes() is called
//        verify { fakeShoeDao.getAllShoes() }

        // should still load from db and return the right list
        assertEquals(mockShoes, secondLoad)


    }





    class FakeShoeDao: ShoeDao {
        private var fakeShoeList = mutableListOf<Shoe>()

        override fun insertShoe(shoe: Shoe) {
            fakeShoeList.add(shoe)
        }

        override suspend fun insertAll(products: List<Shoe>) {
            for(shoe in products) {
                this.insertShoe(shoe)
            }
        }

        override fun getAllShoes(): List<Shoe> {
            return fakeShoeList
        }

    }
}