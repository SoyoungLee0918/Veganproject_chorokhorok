package org.techtown.veganproject.ui.barcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class barcodeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public barcodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}