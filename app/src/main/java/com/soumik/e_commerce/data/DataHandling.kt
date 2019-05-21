package com.soumik.e_commerce.data

import android.content.Context
import io.paperdb.Paper

object DataHandling {

    private val USER_PHONE_NUMBER = "phoneNumber"
    private val USER_PASSWORD = "password"

     fun getPaperContext(context: Context) {
        return Paper.init(context)
    }

    fun setUserPhoneNumber(phone: String?){
        Paper.book().write(USER_PHONE_NUMBER,phone)
    }

    fun setUserPassword(password: String?){
        Paper.book().write(USER_PASSWORD,password)
    }

    fun getUserPhoneNumber():String?{
        return Paper.book().read(USER_PHONE_NUMBER)
    }

    fun getUserPassword():String?{
        return Paper.book().read(USER_PASSWORD)
    }

    fun clearUserData(){
        Paper.book().destroy()
    }
}