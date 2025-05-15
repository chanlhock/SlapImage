package com.example.slapimage.solitaire_cg;

public class Move {
  private int mFrom;
  private int mToBegin;
  private int mToEnd;
  private int mCount;
  private int mFlags;

  private static final int FLAGS_INVERT = 0x0001;
  private static final int FLAGS_UNHIDE = 0x0002;
  private static final int FLAGS_ADD_DEAL_COUNT = 0x0004;

  public Move() {
    mFrom = -1;
    mToBegin = -1;
    mToEnd = -1;
    mCount = 0;
    mFlags = 0;
  }
  public Move(Move move) {
    mFrom = move.mFrom;
    mToBegin = move.mToBegin;
    mToEnd = move.mToEnd;
    mCount = move.mCount;
    mFlags = move.mFlags;
  }
  public Move(int from, int toBegin, int toEnd, int count, boolean invert,
              boolean unhide) {
    mFrom = from;
    mToBegin = toBegin;
    mToEnd = toEnd;
    mCount = count;
    mFlags = 0;
    if (invert)
      mFlags |= FLAGS_INVERT;
    if (unhide)
      mFlags |= FLAGS_UNHIDE;
  }

  public Move(int from, int to, int count, boolean invert,
              boolean unhide) {
    mFrom = from;
    mToBegin = to;
    mToEnd = to;
    mCount = count;
    mFlags = 0;
    if (invert)
      mFlags |= FLAGS_INVERT;
    if (unhide)
      mFlags |= FLAGS_UNHIDE;
  }

  public Move(int from, int to, int count, boolean invert,
              boolean unhide, boolean addDealCount) {
    mFrom = from;
    mToBegin = to;
    mToEnd = to;
    mCount = count;
    mFlags = 0;
    if (invert)
      mFlags |= FLAGS_INVERT;
    if (unhide)
      mFlags |= FLAGS_UNHIDE;
    if (addDealCount)
      mFlags |= FLAGS_ADD_DEAL_COUNT;
  }

  public Move(int from, int toBegin, int toEnd, int count, int flags) {
    mFrom = from;
    mToBegin = toBegin;
    mToEnd = toEnd;
    mCount = count;
    mFlags = flags;
  }

  public int GetFrom() { return mFrom; }
  public int GetToBegin() { return mToBegin; }
  public int GetToEnd() { return mToEnd; }
  public int GetCount() { return mCount; }
  public int GetFlags() { return mFlags; }
  public boolean GetInvert() { return (mFlags & FLAGS_INVERT) != 0; }
  public boolean GetUnhide() { return (mFlags & FLAGS_UNHIDE) != 0; }
  public boolean GetAddDealCount() { return (mFlags & FLAGS_ADD_DEAL_COUNT) != 0; } 
}
