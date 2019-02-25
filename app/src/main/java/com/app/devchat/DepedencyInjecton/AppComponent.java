package com.app.devchat.DepedencyInjecton;

import com.app.devchat.ui.ChatActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ViewModelModule.class})
public interface AppComponent {
    void inject(ChatActivity activity);
}
