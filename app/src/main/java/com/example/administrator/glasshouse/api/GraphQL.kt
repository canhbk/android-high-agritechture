package com.example.administrator.glasshouse.api

import androidx.lifecycle.LiveData
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Monitor
import com.example.administrator.glasshouse.vo.User

interface GraphQL {

    fun loadGates(userId: String): LiveData<ApiResponse<List<Gate>>>
    fun addGate(userId: String, idGate: String, gateName: String): LiveData<ApiResponse<Gate>>
    fun deleteGate(userId: String, idGate: String): LiveData<ApiResponse<Gate>>
    fun editGate(userId: String, idGate: String, gateName: String): LiveData<ApiResponse<Gate>>

    fun loadMonitors(serviceTag: String): LiveData<ApiResponse<List<Monitor>>>
    fun addMonitor(serviceTag: String, tag: String, name: String): LiveData<ApiResponse<Monitor>>
    fun deleteMonitor(tag: String): LiveData<ApiResponse<Monitor>>
    fun editMonitor(tag: String, name: String): LiveData<ApiResponse<Monitor>>

    fun login(email: String, password: String): LiveData<ApiResponse<User>>
    fun register(email: String, name: String, password: String): LiveData<ApiResponse<User>>
}
