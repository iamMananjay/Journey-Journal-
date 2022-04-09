//package com.example.data.repositories;
//
//import com.example.data.sources.LoginLocalDataSource;
//import com.example.data.sources.LoginRemoteDataSource;
//import com.example.domain.models.LoginModel;
//import com.example.domain.repositories.LoginRepository;
//
//public class LoginRepositoryImpl  implements LoginRepository {
//    private final LoginRemoteDataSource loginRemoteDataSource;
//    private final LoginLocalDataSource loginLocalDataSource;
//
//    public LoginRepositoryImpl(
//            LoginRemoteDataSource loginRemoteDataSource,
//            LoginLocalDataSource loginLocalDataSource
//    ){
//        this.loginRemoteDataSource=loginRemoteDataSource;
//        this.loginLocalDataSource=loginLocalDataSource;
//    }
//
//    @Override
//    public LoginModel authenticateLogin(String email,String password){
//        boolean isInternetWorking = loginRemoteDataSource.isInternetWorking();
//        if(isInternetWorking){
//            return loginRemoteDataSource.authenticateLoginInRemoteServer(email,password);
//        }else {
//            return loginLocalDataSource.authenticateLoginInLocalDatabase(email,password);
//        }
//    }
//}
