package com.app.devchat.DepedencyInjecton;

import com.app.devchat.backgroundServices.MessagingService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MessagingServiceModule.class})
public interface MessagingServiceComponent {

    void inject (MessagingService service);
}
