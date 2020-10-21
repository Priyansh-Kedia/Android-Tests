package com.kedia.androidtestproject.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.kedia.androidtestproject.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {


    /** This rule is used to specify that the functions will be called one after another
     This basically calls everything in a synchronous fashion
     This rule has to be implemented in order to take care of the LiveData, because the LiveData
     is called asynchronously
    **/
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: ShoppingItemsDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemsDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.shoppingDao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 1, 143f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 1234f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 1, 1234f, "url", id = 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(1*143f + 4*1234f + 1*1234f)
    }

}