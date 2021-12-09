package com.uqac.proximty.hilt;

import android.content.Context;

import com.uqac.proximty.dao.AppDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ActivityComponent.class)
public class HiltModule {
    /*@Provides
    public static AppDatabase provideAppDatabase(
            // Potential dependencies of this type
            @ApplicationContext Context context
    ) {
        return AppDatabase.getDatabase(context);
    }*/
}
