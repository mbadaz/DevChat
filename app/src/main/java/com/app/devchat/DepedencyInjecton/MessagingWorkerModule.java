package com.app.devchat.DepedencyInjecton;

import com.app.devchat.backgroundServices.BackgroundMessagingWorker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (includes = AppModule.class)
public class MessagingWorkerModule {

    private static BackgroundMessagingWorker worker;

    public MessagingWorkerModule(BackgroundMessagingWorker worker) {
        MessagingWorkerModule.worker = worker;
    }

    @Provides
    @Singleton
    static BackgroundMessagingWorker provideMessagingWorker(){
        return worker;
    }
}
