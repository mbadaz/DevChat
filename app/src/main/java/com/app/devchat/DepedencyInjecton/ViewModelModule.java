package com.app.devchat.DepedencyInjecton;

import com.app.devchat.chat.ChatActivityViewModel;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    public ViewModelModule() {
    }

    @Provides
    @Singleton
    static ViewModel provideViewModel(ChatActivityViewModel viewModel){
      return  viewModel;
    }

}
