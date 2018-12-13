package com.example.administrator.glasshouse.repository

import androidx.lifecycle.LiveData
import com.example.administrator.glasshouse.util.AppExecutors
import com.example.administrator.glasshouse.api.GraphQL
import com.example.administrator.glasshouse.db.UserDao
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val userDao: UserDao,
        private val graphQL: GraphQL

) : LiveData<User>() {

    fun loadUser(): LiveData<User> {
        return userDao.loadUser()
    }

    fun login(
            email: String, password: String
    ): LiveData<Resource<User>> {

        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.resetTable()
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true
            }

            override fun loadFromDb() = userDao.loadUser()

            override fun createCall() = graphQL.login(email, password)
        }.asLiveData()

    }

    fun register(
            email: String, name: String, password: String
    ): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.resetTable()
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true
            }

            override fun loadFromDb() = userDao.loadUser()

            override fun createCall() = graphQL.register(email, name, password)
        }.asLiveData()

    }
}