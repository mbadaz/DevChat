package com.app.devchat.DepedencyInjecton;

import com.app.devchat.backgroundMessaging.MessagingService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class MessagingServiceModule {

    private static MessagingService service;

    public MessagingServiceModule(MessagingService service) {
        this.service = service;
    }

    @Provides
    @Singleton
    static MessagingService provideMessagingService() {
        return service;
    }
}
