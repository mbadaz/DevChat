package com.app.devchat.DepedencyInjecton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelsFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelProviders;

    @Inject
    public ViewModelsFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelProviders) {
        this.viewModelProviders = viewModelProviders;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) viewModelProviders.get(modelClass).get();
    }
}
