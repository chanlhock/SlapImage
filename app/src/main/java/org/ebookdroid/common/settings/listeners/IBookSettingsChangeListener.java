package org.ebookdroid.common.settings.listeners;

import com.example.slapimage.ibook.foobnix.model.AppBook;

public interface IBookSettingsChangeListener {

    void onBookSettingsChanged(AppBook oldSettings, AppBook newSettings, AppBook.Diff diff);

}
