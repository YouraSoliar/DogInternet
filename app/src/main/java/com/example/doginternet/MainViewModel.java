package com.example.doginternet;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status";
    private MutableLiveData<DogImage> dogImage = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isInternet = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsInternet() {
        return isInternet;
    }

    public LiveData<DogImage> getDogImage() {
        return dogImage;
    }

    public void loadDogImage() {
        Disposable disposable = loadDogImageRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                        isInternet.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<DogImage>() {
                    @Override
                    public void accept(DogImage image) throws Throwable {
                        dogImage.setValue(image);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        isInternet.setValue(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    private Single<DogImage> loadDogImageRx() {
        return ApiFactory.getApiService().loadDogImage();
    }
}
