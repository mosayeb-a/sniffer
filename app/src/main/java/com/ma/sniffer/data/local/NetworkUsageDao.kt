package com.ma.sniffer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkUsageDao {
    @Insert
    suspend fun insert(record: NetworkUsage)

    @Query("""
        SELECT * 
        FROM network_usage
        WHERE timestamp >= :start
          AND timestamp < :end
        ORDER BY timestamp ASC
    """)
    fun getRecordsBetween(
        start: Long,
        end: Long
    ): Flow<List<NetworkUsage>>

    @Query("""
        DELETE FROM network_usage
        WHERE timestamp < :cutoff
    """)
    suspend fun deleteOlderThan(cutoff: Long)

    @Transaction
    suspend fun insert(record: NetworkUsage, cutoff: Long) {
        insert(record)
        deleteOlderThan(cutoff)
    }
}