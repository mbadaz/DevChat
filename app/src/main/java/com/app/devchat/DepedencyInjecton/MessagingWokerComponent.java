package com.app.devchat.DepedencyInjecton;

import com.app.devchat.backgroundServices.BackgroundMessagingWorker;


import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component (modules = MessagingWorkerModule.class)
public interface MessagingWokerComponent {

    void inject (BackgroundMessagingWorker worker);
}
