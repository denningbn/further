package com.example.further;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;

public class SettingsRepository {
    private final SettingsDAO settingsDao;

    public SettingsRepository(SettingsDAO settingsDao) {
        this.settingsDao = settingsDao;
    }

    public Single<Settings> getSettingsByIdAsync(final long id) {
        return Single.create(new SingleOnSubscribe<Settings>() {
            @Override
            public void subscribe(SingleEmitter<Settings> emitter) throws Exception {
                Settings settings = settingsDao.getSettingsById(id);
                if (settings != null) {
                    emitter.onSuccess(settings);
                } else {
                    emitter.onError(new Exception("Settings not found"));
                }
            }
        });
    }
}