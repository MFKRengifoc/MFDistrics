package com.manoffocus.mfdistricts.data

sealed class Resource<T>(var data: T? = null, var message: String? = null, var code: Int? = null){
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null, code: Int? = null): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Empty<T>(data: T? = null): Resource<T>(data)
}
