package com.example.tryanimation.try_architecture_code.injection

import android.content.Context
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.example.tryanimation.BuildConfig
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.data.const.Constants
import com.example.tryanimation.try_architecture_code.database.MyDatabaseTry
import com.example.tryanimation.try_architecture_code.database.user.UserDao
import com.example.tryanimation.try_architecture_code.database.user.UserRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext

@Module
@InstallIn(SingletonComponent::class)
class ComponentModule {


    @Provides
    @Singleton
    fun provideGson() =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        MyDatabaseTry.getMyDatabaseTry(context)


    @Provides
    @Singleton
    fun provideUserDao(database: MyDatabaseTry) = database.userDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao) = UserRepository(userDao)

    @Provides
    @Singleton
    fun provideSession(@ApplicationContext context: Context) =
        CoreSession(context)


    @Provides
    @Singleton
    fun provideOkHttpClient(session: CoreSession): OkHttpClient {
        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val accessToken = session.getString(Constants.TOKEN.API_TOKEN)
                Timber.tag("ComponentModuleOkhttp").d("AccessToken: $accessToken")
                val requestBuilder = original.newBuilder()
                    .header("Authorization", accessToken) // "access token" diganti Authorization
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptors)
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://3.1.6.193/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient) // -> Tambahkan ini
            .build().create(ApiService::class.java)
    }
}