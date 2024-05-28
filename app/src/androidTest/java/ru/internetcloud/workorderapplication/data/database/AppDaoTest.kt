package ru.internetcloud.workorderapplication.data.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.internetcloud.workorderapplication.common.data.model.PartnerDbModel

@RunWith(AndroidJUnit4::class)
@MediumTest
class AppDaoTest {

    private lateinit var database: ru.internetcloud.workorderapplication.common.data.database.AppDatabase
    private lateinit var dao: ru.internetcloud.workorderapplication.common.data.database.AppDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ru.internetcloud.workorderapplication.common.data.database.AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.appDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addPartnerList() = runBlockingTest {
        val partnerList = mutableListOf<PartnerDbModel>()

        partnerList.add(PartnerDbModel(id = "1", name = "Company A", fullName = "Company A, Apple", inn = "00001", code1C = "01"))
        partnerList.add(PartnerDbModel(id = "2", name = "Company B", fullName = "Company B, Bridge", inn = "00002", code1C = "02"))
        partnerList.add(PartnerDbModel(id = "3", name = "Company C", fullName = "Company C, Compex", inn = "00003", code1C = "03"))

        dao.addPartnerList(partnerList)

        val actual = dao.getPartnerList()
        val expected = partnerList.toList().takeLast(1)

        assertEquals(expected, actual)
    }

    @Test
    fun test1() {
        val actual = 2
        val expected = 3

        assertEquals(expected, actual)
    }
}
