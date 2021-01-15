package com.example.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tasks.service.model.PriorityModel

@Dao
interface PriorityDAO {
    @Insert
    fun save(list: List<PriorityModel>)

    @Query("Delete from priority")
    fun clear()

    @Query("Select description from priority where id= :id" )
    fun getDescription(id: Int): String

    @Query("Select * from priority")
    fun list(): List<PriorityModel>
}