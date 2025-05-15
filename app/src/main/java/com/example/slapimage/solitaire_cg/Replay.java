package com.example.slapimage.solitaire_cg;

import android.util.Log;
import java.util.Stack;

public class Replay implements Runnable {
  private Stack<Move> mMoveStack;
  private SolitaireView mView;
  private AnimateCard mAnimateCard;
  private CardAnchor[] mCardAnchor;
  private boolean mIsPlaying;

  private Card[] mSinkCard;
  private int mSinkCount;
  private CardAnchor mSinkAnchor;
  private CardAnchor mSinkFrom;
  private boolean mSinkUnhide;

  public Replay(SolitaireView view, AnimateCard animateCard) {
    mView = view;
    mAnimateCard = animateCard;
    mIsPlaying = false;
    mMoveStack = new Stack<Move>();
    mSinkCard = new Card[104];
  }

  public boolean IsPlaying() { return mIsPlaying; }
  public void StopPlaying() { mIsPlaying = false; }

  public void StartReplay(Stack<Move> history, CardAnchor[] anchor) {
    mCardAnchor = anchor;
    mMoveStack.clear();
    while (!history.empty()) {
      Move move = history.peek();
      if (move.GetToBegin() != move.GetToEnd()) {
        for (int i = move.GetToEnd(); i >= move.GetToBegin(); i--) {
          mMoveStack.push(new Move(move.GetFrom(), i, 1, false, false));
        }
      } else {
        mMoveStack.push(move);
      }
      mView.Undo();
    }
    mView.DrawBoard();
    mIsPlaying = true;
    PlayNext();
  }

  public void PlayNext() {
    if (!mIsPlaying || mMoveStack.empty()) {
      mIsPlaying = false;
      mView.StopAnimating();
      return;
    }
    Move move = mMoveStack.pop();

    if (move.GetToBegin() == move.GetToEnd()) {
      mSinkCount = move.GetCount();
      mSinkAnchor = mCardAnchor[move.GetToBegin()];
      mSinkUnhide = move.GetUnhide();
      mSinkFrom = mCardAnchor[move.GetFrom()];

      if (move.GetInvert()) {
        for (int i = 0; i < mSinkCount; i++) {
          mSinkCard[i] = mSinkFrom.PopCard();
        }
      } else {
        for (int i = mSinkCount-1; i >= 0; i--) {
          mSinkCard[i] = mSinkFrom.PopCard();
        }
      }
      mAnimateCard.MoveCards(mSinkCard, mSinkAnchor, mSinkCount, this);
    } else {
      Log.e("Replay.java", "Invalid move encountered, aborting.");
      mIsPlaying = false;
    }
  }

  public void run() {
    if (mIsPlaying) {
      if (mSinkUnhide) {
        mSinkFrom.UnhideTopCard();
      }
      PlayNext();
    }
  }
}
