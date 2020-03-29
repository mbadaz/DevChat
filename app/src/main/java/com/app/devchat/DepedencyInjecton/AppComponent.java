package com.app.devchat.DepedencyInjecton;

import com.app.devchat.chat.ChatActivity;

import javax.inject.Singleton;

import androidx.work.Worker;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(ChatActivity activity);
    void inject(Worker worker);
}
