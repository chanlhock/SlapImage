package com.example.slapimage.solitaire_cg;

import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import com.example.slapimage.R;

public class Options {

  public Options(final SolitaireCG solitaire, final DrawMaster drawMaster) {

    solitaire.setContentView(R.layout.options);
    View view = (View) solitaire.findViewById(R.id.options_view);
    view.setFocusable(true);
    view.setFocusableInTouchMode(true);

    // Display stuff
    final boolean displayTime = solitaire.GetSettings().getBoolean("DisplayTime", true);
    ((CheckBox)solitaire.findViewById(R.id.display_time)).setChecked(displayTime);

    final boolean bigCards = solitaire.GetSettings().getBoolean("DisplayBigCards", false);
    ((CheckBox)solitaire.findViewById(R.id.big_cards)).setChecked(bigCards);

    final boolean lockLandscape = solitaire.GetSettings().getBoolean("LockLandscape", false);
    ((CheckBox)solitaire.findViewById(R.id.lock_landscape)).setChecked(lockLandscape);

    // Automove
    final int autoMove = solitaire.GetSettings().getInt("AutoMoveLevel", Rules.AUTO_MOVE_FLING_ONLY);
    ((RadioButton)solitaire.findViewById(R.id.auto_move_always)).setChecked(autoMove == Rules.AUTO_MOVE_ALWAYS);
    ((RadioButton)solitaire.findViewById(R.id.auto_move_fling_only)).setChecked(autoMove == Rules.AUTO_MOVE_FLING_ONLY);
    ((RadioButton)solitaire.findViewById(R.id.auto_move_never)).setChecked(autoMove == Rules.AUTO_MOVE_NEVER);

    view.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode) {
          case KeyEvent.KEYCODE_BACK:
            // If options are changed then save when back key pressed
            boolean commit = false;
            SharedPreferences.Editor editor = solitaire.GetSettings().edit();

            if (displayTime != ((CheckBox)solitaire.findViewById(R.id.display_time)).isChecked()) {
              editor.putBoolean("DisplayTime", !displayTime);
              commit = true;
            }

            if (bigCards != ((CheckBox)solitaire.findViewById(R.id.big_cards)).isChecked()) {
              editor.putBoolean("DisplayBigCards", !bigCards);
              commit = true;
              drawMaster.DrawCards(!bigCards);
            }

            if (lockLandscape != ((CheckBox)solitaire.findViewById(R.id.lock_landscape)).isChecked()) {
              editor.putBoolean("LockLandscape", !lockLandscape);
              commit = true;
            }

            int newAutoMove = Rules.AUTO_MOVE_NEVER;
            if (((RadioButton)solitaire.findViewById(R.id.auto_move_always)).isChecked()) {
              newAutoMove = Rules.AUTO_MOVE_ALWAYS;
            } else if (((RadioButton)solitaire.findViewById(R.id.auto_move_fling_only)).isChecked()) {
              newAutoMove = Rules.AUTO_MOVE_FLING_ONLY;
            }

            if (newAutoMove != autoMove) {
              editor.putInt("AutoMoveLevel", newAutoMove);
              commit = true;
            }

            if (commit) {
              // Save option changes
              editor.commit();
              solitaire.RefreshOptions();
            }

            solitaire.CancelOptions();
            return true;
          case KeyEvent.KEYCODE_MENU:
            // Disable menu in options screen
            return true;
        }
        return false;
      }
    });
    view.requestFocus();
  }
}
